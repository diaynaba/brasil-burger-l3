using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System;
using System.Linq;
using System.Threading.Tasks;
using BrasilBurger.Web.Models.Entities;
using BrasilBurger.Web.Models.ViewModels;
using BrasilBurger.Web.Repositories;
using BrasilBurger.Web.Services;
using BrasilBurger.Web.Helpers;

namespace BrasilBurger.Web.Controllers
{
    [Authorize]
    public class CommandeController : Controller
    {
        private readonly IUnitOfWork _unitOfWork;
        private readonly IPanierService _panierService;

        public CommandeController(IUnitOfWork unitOfWork, IPanierService panierService)
        {
            _unitOfWork = unitOfWork;
            _panierService = panierService;
        }

        [HttpGet]
        public async Task<IActionResult> PasserCommande()
        {
            var panier = _panierService.GetPanier();
            
            if (panier.Items.Count == 0)
            {
                TempData["ErrorMessage"] = "Votre panier est vide.";
                return RedirectToAction("Index", "Panier");
            }

            var zones = await _unitOfWork.Zones.FindAsync(z => z.IsActive);

            var model = new PasserCommandeViewModel
            {
                ZonesDisponibles = zones.ToList(),
                SousTotal = panier.SousTotal,
                FraisLivraison = 0,
                Total = panier.SousTotal
            };

            return View(model);
        }

        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> PasserCommande(PasserCommandeViewModel model)
        {
            var panier = _panierService.GetPanier();
            
            if (panier.Items.Count == 0)
            {
                TempData["ErrorMessage"] = "Votre panier est vide.";
                return RedirectToAction("Index", "Panier");
            }

            if (model.TypeService == "Livraison" && !model.ZoneId.HasValue)
            {
                ModelState.AddModelError("ZoneId", "Veuillez sélectionner une zone de livraison.");
            }

            if (!ModelState.IsValid)
            {
                var zones = await _unitOfWork.Zones.FindAsync(z => z.IsActive);
                model.ZonesDisponibles = zones.ToList();
                return View(model);
            }

            var clientId = HttpContext.GetClientId();
            if (!clientId.HasValue)
            {
                return RedirectToAction("Login", "Auth");
            }

            decimal fraisLivraison = 0;
            if (model.TypeService == "Livraison" && model.ZoneId.HasValue)
            {
                var zone = await _unitOfWork.Zones.GetByIdAsync(model.ZoneId.Value);
                fraisLivraison = zone?.PrixLivraison ?? 0;
            }

            var commande = new Commande
            {
                ClientId = clientId.Value,
                NumeroCommande = NumeroCommandeGenerator.Generate(),
                TypeService = model.TypeService,
                Etat = "En attente",
                MontantTotal = panier.SousTotal + fraisLivraison,
                AdresseLivraison = model.AdresseLivraison,
                ZoneId = model.ZoneId,
                Note = model.Note,
                DateCommande = DateTime.Now
            };

            await _unitOfWork.Commandes.AddAsync(commande);
            await _unitOfWork.SaveAsync();

            // Ajouter les items
            foreach (var item in panier.Items)
            {
                var commandeItem = new CommandeItem
                {
                    CommandeId = commande.Id,
                    BurgerId = item.Type == "Burger" ? item.ItemId : (int?)null,
                    MenuId = item.Type == "Menu" ? item.ItemId : (int?)null,
                    Quantite = item.Quantite,
                    PrixUnitaire = item.PrixUnitaire,
                    PrixTotal = item.PrixTotal
                };

                await _unitOfWork.CommandeItems.AddAsync(commandeItem);

                // Ajouter les compléments
                foreach (var complement in item.Complements)
                {
                    var commandeComplement = new CommandeComplement
                    {
                        CommandeId = commande.Id,
                        ComplementId = complement.Id,
                        Quantite = complement.Quantite,
                        PrixUnitaire = complement.Prix,
                        PrixTotal = complement.Prix * complement.Quantite
                    };

                    await _unitOfWork.CommandeComplements.AddAsync(commandeComplement);
                }
            }

            await _unitOfWork.SaveAsync();

            // Vider le panier
            _panierService.ViderPanier();

            return RedirectToAction("Paiement", new { commandeId = commande.Id });
        }

        [HttpGet]
        public async Task<IActionResult> Paiement(int commandeId)
        {
            var commande = await _unitOfWork.Commandes.GetByIdAsync(commandeId);
            
            if (commande == null)
            {
                return NotFound();
            }

            var clientId = HttpContext.GetClientId();
            if (commande.ClientId != clientId)
            {
                return Forbid();
            }

            var model = new PaiementViewModel
            {
                CommandeId = commande.Id,
                Montant = commande.MontantTotal,
                NumeroCommande = commande.NumeroCommande,
                Commande = commande
            };

            return View(model);
        }

        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Paiement(PaiementViewModel model)
        {
            if (!ModelState.IsValid)
            {
                var commande = await _unitOfWork.Commandes.GetByIdAsync(model.CommandeId);
                model.Commande = commande;
                model.NumeroCommande = commande.NumeroCommande;
                return View(model);
            }

            var paiement = new Paiement
            {
                CommandeId = model.CommandeId,
                Methode = model.Methode,
                Montant = model.Montant,
                Reference = ReferenceGenerator.GenerateReference(model.Methode),
                DatePaiement = DateTime.Now,
                Statut = "Validé"
            };

            await _unitOfWork.Paiements.AddAsync(paiement);

            var commandeToUpdate = await _unitOfWork.Commandes.GetByIdAsync(model.CommandeId);
            commandeToUpdate.Etat = "Validé";
            _unitOfWork.Commandes.Update(commandeToUpdate);

            await _unitOfWork.SaveAsync();

            TempData["SuccessMessage"] = "Paiement effectué avec succès !";
            return RedirectToAction("Confirmation", new { commandeId = model.CommandeId });
        }

        [HttpGet]
        public async Task<IActionResult> Confirmation(int commandeId)
        {
            var commande = await _unitOfWork.Commandes.GetByIdAsync(commandeId);
            
            if (commande == null)
            {
                return NotFound();
            }

            var clientId = HttpContext.GetClientId();
            if (commande.ClientId != clientId)
            {
                return Forbid();
            }

            return View(commande);
        }

        [HttpGet]
        public async Task<IActionResult> MesCommandes()
        {
            var clientId = HttpContext.GetClientId();
            if (!clientId.HasValue)
            {
                return RedirectToAction("Login", "Auth");
            }

            var commandes = await _unitOfWork.Commandes
                .FindAsync(c => c.ClientId == clientId.Value);

            var model = new MesCommandesViewModel
            {
                CommandesEnCours = commandes.Where(c => 
                    c.Etat == "En attente" || c.Etat == "Validé" || c.Etat == "En cours")
                    .OrderByDescending(c => c.DateCommande)
                    .ToList(),
                CommandesTerminees = commandes.Where(c => c.Etat == "Terminé")
                    .OrderByDescending(c => c.DateCommande)
                    .ToList(),
                CommandesAnnulees = commandes.Where(c => c.Etat == "Annulé")
                    .OrderByDescending(c => c.DateCommande)
                    .ToList()
            };

            return View(model);
        }

        [HttpGet]
        public async Task<IActionResult> Detail(int id)
        {
            var commande = await _unitOfWork.Commandes.GetByIdAsync(id);
            
            if (commande == null)
            {
                return NotFound();
            }

            var clientId = HttpContext.GetClientId();
            if (commande.ClientId != clientId)
            {
                return Forbid();
            }

            var items = await _unitOfWork.CommandeItems
                .FindAsync(ci => ci.CommandeId == id);

            var complements = await _unitOfWork.CommandeComplements
                .FindAsync(cc => cc.CommandeId == id);

            var model = new CommandeDetailViewModel
            {
                Commande = commande,
                Items = items.ToList(),
                Complements = complements.ToList()
            };

            return View(model);
        }
    }
}