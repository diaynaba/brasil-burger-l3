using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using BrasilBurger.Web.Models.Entities;

namespace BrasilBurger.Web.Models.ViewModels
{
    public class PasserCommandeViewModel
    {
        [Required(ErrorMessage = "Le type de service est requis")]
        public string TypeService { get; set; } // "Sur place", "À emporter", "Livraison"

        public string AdresseLivraison { get; set; }
        
        public int? ZoneId { get; set; }
        
        public string Note { get; set; }
        
        public List<Zone> ZonesDisponibles { get; set; } = new List<Zone>();
        
        public decimal SousTotal { get; set; }
        
        public decimal FraisLivraison { get; set; }
        
        public decimal Total { get; set; }
    }

    public class PaiementViewModel
    {
        public int CommandeId { get; set; }
        
        [Required(ErrorMessage = "La méthode de paiement est requise")]
        public string Methode { get; set; } // "Wave", "Orange Money"
        
        [Required]
        public decimal Montant { get; set; }
        
        public string NumeroCommande { get; set; }
        
        public Commande Commande { get; set; }
    }

    public class MesCommandesViewModel
    {
        public List<Commande> CommandesEnCours { get; set; } = new List<Commande>();
        public List<Commande> CommandesTerminees { get; set; } = new List<Commande>();
        public List<Commande> CommandesAnnulees { get; set; } = new List<Commande>();
    }

    public class CommandeDetailViewModel
    {
        public Commande Commande { get; set; }
        public Client Client { get; set; }
        public List<CommandeItem> Items { get; set; } = new List<CommandeItem>();
        public List<CommandeComplement> Complements { get; set; } = new List<CommandeComplement>();
        public Paiement Paiement { get; set; }
        public Zone Zone { get; set; }
    }
}