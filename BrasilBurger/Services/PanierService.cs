using System;
using System.Collections.Generic;
using System.Linq;
using Microsoft.AspNetCore.Http;
using Newtonsoft.Json;
using BrasilBurger.Web.Models.ViewModels;

namespace BrasilBurger.Web.Services
{
    public interface IPanierService
    {
        PanierViewModel GetPanier();
        void AjouterItem(PanierItemViewModel item);
        void ModifierQuantite(int itemId, string type, int quantite);
        void SupprimerItem(int itemId, string type);
        void ViderPanier();
        int GetNombreItems();
    }

    public class PanierService : IPanierService
    {
        private readonly IHttpContextAccessor _httpContextAccessor;
        private const string PanierSessionKey = "Panier";

        public PanierService(IHttpContextAccessor httpContextAccessor)
        {
            _httpContextAccessor = httpContextAccessor;
        }

        private ISession Session => _httpContextAccessor.HttpContext.Session;

        public PanierViewModel GetPanier()
        {
            var panierJson = Session.GetString(PanierSessionKey);
            
            if (string.IsNullOrEmpty(panierJson))
            {
                return new PanierViewModel();
            }

            var panier = JsonConvert.DeserializeObject<PanierViewModel>(panierJson);
            CalculerTotaux(panier);
            return panier;
        }

        private void SavePanier(PanierViewModel panier)
        {
            CalculerTotaux(panier);
            var panierJson = JsonConvert.SerializeObject(panier);
            Session.SetString(PanierSessionKey, panierJson);
        }

        public void AjouterItem(PanierItemViewModel item)
        {
            var panier = GetPanier();
            
            var existingItem = panier.Items.FirstOrDefault(i => 
                i.ItemId == item.ItemId && i.Type == item.Type);

            if (existingItem != null)
            {
                existingItem.Quantite += item.Quantite;
                existingItem.PrixTotal = existingItem.PrixUnitaire * existingItem.Quantite;
                
                // Ajouter les compléments
                foreach (var complement in item.Complements)
                {
                    var existingComplement = existingItem.Complements
                        .FirstOrDefault(c => c.Id == complement.Id);
                    
                    if (existingComplement != null)
                    {
                        existingComplement.Quantite += complement.Quantite;
                    }
                    else
                    {
                        existingItem.Complements.Add(complement);
                    }
                }
            }
            else
            {
                item.PrixTotal = item.PrixUnitaire * item.Quantite;
                panier.Items.Add(item);
            }

            SavePanier(panier);
        }

        public void ModifierQuantite(int itemId, string type, int quantite)
        {
            var panier = GetPanier();
            var item = panier.Items.FirstOrDefault(i => i.ItemId == itemId && i.Type == type);

            if (item != null)
            {
                if (quantite <= 0)
                {
                    panier.Items.Remove(item);
                }
                else
                {
                    item.Quantite = quantite;
                    item.PrixTotal = item.PrixUnitaire * item.Quantite;
                }

                SavePanier(panier);
            }
        }

        public void SupprimerItem(int itemId, string type)
        {
            var panier = GetPanier();
            var item = panier.Items.FirstOrDefault(i => i.ItemId == itemId && i.Type == type);

            if (item != null)
            {
                panier.Items.Remove(item);
                SavePanier(panier);
            }
        }

        public void ViderPanier()
        {
            Session.Remove(PanierSessionKey);
        }

        public int GetNombreItems()
        {
            var panier = GetPanier();
            return panier.Items.Sum(i => i.Quantite);
        }

        private void CalculerTotaux(PanierViewModel panier)
        {
            panier.SousTotal = panier.Items.Sum(i => i.PrixTotal);
            
            // Calculer le prix des compléments
            foreach (var item in panier.Items)
            {
                panier.SousTotal += item.Complements.Sum(c => c.Prix * c.Quantite);
            }

            panier.Total = panier.SousTotal + panier.FraisLivraison;
            panier.NombreItems = panier.Items.Sum(i => i.Quantite);
        }
    }
}