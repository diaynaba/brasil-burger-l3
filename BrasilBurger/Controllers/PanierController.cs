using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using BrasilBurger.Web.Models.ViewModels;
using BrasilBurger.Web.Services;
using BrasilBurger.Web.Repositories;

namespace BrasilBurger.Web.Controllers
{
    public class PanierController : Controller
    {
        private readonly IPanierService _panierService;
        private readonly IUnitOfWork _unitOfWork;

        public PanierController(IPanierService panierService, IUnitOfWork unitOfWork)
        {
            _panierService = panierService;
            _unitOfWork = unitOfWork;
        }

        public IActionResult Index()
        {
            var panier = _panierService.GetPanier();
            return View(panier);
        }

        [HttpPost]
        public async Task<IActionResult> AjouterBurger(int burgerId, int quantite = 1, List<int>? complementIds = null)
        {
            var burger = await _unitOfWork.Burgers.GetByIdAsync(burgerId);
            
            if (burger == null || burger.IsArchived || !burger.IsDisponible)
            {
                return Json(new { success = false, message = "Burger non disponible" });
            }

            var item = new PanierItemViewModel
            {
                Type = "Burger",
                ItemId = burger.Id,
                Nom = burger.Nom,
                ImageUrl = burger.ImageUrl,
                PrixUnitaire = burger.Prix,
                Quantite = quantite,
                Complements = new List<ComplementPanierViewModel>()
            };

            if (complementIds != null && complementIds.Count > 0)
            {
                var complements = await _unitOfWork.Complements
                    .FindAsync(c => complementIds.Contains(c.Id) && !c.IsArchived && c.IsDisponible);

                foreach (var complement in complements)
                {
                    item.Complements.Add(new ComplementPanierViewModel
                    {
                        Id = complement.Id,
                        Nom = complement.Nom,
                        Prix = complement.Prix,
                        Quantite = 1
                    });
                }
            }

            _panierService.AjouterItem(item);

            return Json(new 
            { 
                success = true, 
                message = "Burger ajouté au panier",
                nombreItems = _panierService.GetNombreItems()
            });
        }

        [HttpPost]
        public async Task<IActionResult> AjouterMenu(int menuId, int quantite = 1)
        {
            var menu = await _unitOfWork.Menus.GetByIdAsync(menuId);
            
            if (menu == null || menu.IsArchived || !menu.IsDisponible)
            {
                return Json(new { success = false, message = "Menu non disponible" });
            }

            var item = new PanierItemViewModel
            {
                Type = "Menu",
                ItemId = menu.Id,
                Nom = menu.Nom,
                ImageUrl = menu.ImageUrl ?? string.Empty,
                PrixUnitaire = menu.Prix,
                Quantite = quantite
            };

            _panierService.AjouterItem(item);

            return Json(new 
            { 
                success = true, 
                message = "Menu ajouté au panier",
                nombreItems = _panierService.GetNombreItems()
            });
        }

        [HttpPost]
        public async Task<IActionResult> AjouterComplement(int complementId, int quantite = 1)
        {
            var complement = await _unitOfWork.Complements.GetByIdAsync(complementId);
            
            if (complement == null || complement.IsArchived || !complement.IsDisponible)
            {
                return Json(new { success = false, message = "Complément non disponible" });
            }

            var item = new PanierItemViewModel
            {
                Type = "Complement",
                ItemId = complement.Id,
                Nom = complement.Nom,
                ImageUrl = complement.ImageUrl ?? string.Empty,
                PrixUnitaire = complement.Prix,
                Quantite = quantite
            };

            _panierService.AjouterItem(item);

            return Json(new 
            { 
                success = true, 
                message = "Complément ajouté au panier",
                nombreItems = _panierService.GetNombreItems()
            });
        }

        [HttpPost]
        public IActionResult ModifierQuantite(int itemId, string type, int quantite)
        {
            _panierService.ModifierQuantite(itemId, type, quantite);
            
            var panier = _panierService.GetPanier();
            
            return Json(new 
            { 
                success = true,
                sousTotal = panier.SousTotal,
                total = panier.Total,
                nombreItems = panier.NombreItems
            });
        }

        [HttpPost]
        public IActionResult SupprimerItem(int itemId, string type)
        {
            _panierService.SupprimerItem(itemId, type);
            
            var panier = _panierService.GetPanier();
            
            return Json(new 
            { 
                success = true,
                sousTotal = panier.SousTotal,
                total = panier.Total,
                nombreItems = panier.NombreItems
            });
        }

        [HttpPost]
        public IActionResult Vider()
        {
            _panierService.ViderPanier();
            return Json(new { success = true });
        }

        public IActionResult GetNombreItems()
        {
            return Json(new { nombreItems = _panierService.GetNombreItems() });
        }
    }
}