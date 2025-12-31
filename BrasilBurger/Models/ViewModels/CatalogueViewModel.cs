using System.Collections.Generic;
using BrasilBurger.Web.Models.Entities;

namespace BrasilBurger.Web.Models.ViewModels
{
    public class CatalogueViewModel
    {
        public List<Burger> Burgers { get; set; } = new List<Burger>();
        public List<Menu> Menus { get; set; } = new List<Menu>();
        public List<Complement> Complements { get; set; } = new List<Complement>();
        public string FilterType { get; set; } = "all"; // "all", "burgers", "menus"
        public string SearchQuery { get; set; }
    }

    public class BurgerDetailViewModel
    {
        public Burger Burger { get; set; }
        public List<Complement> ComplementsDisponibles { get; set; } = new List<Complement>();
        public List<Burger> BurgersSimilaires { get; set; } = new List<Burger>();
    }

    public class MenuDetailViewModel
    {
        public Menu Menu { get; set; }
        public List<MenuBurger> Burgers { get; set; } = new List<MenuBurger>();
        public List<MenuComplement> Complements { get; set; } = new List<MenuComplement>();
        public decimal PrixTotal { get; set; }
    }

    public class PanierViewModel
    {
        public List<PanierItemViewModel> Items { get; set; } = new List<PanierItemViewModel>();
        public decimal SousTotal { get; set; }
        public decimal FraisLivraison { get; set; }
        public decimal Total { get; set; }
        public int NombreItems { get; set; }
    }

    public class PanierItemViewModel
    {
        public string Type { get; set; } // "Burger", "Menu", "Complement"
        public int ItemId { get; set; }
        public string Nom { get; set; }
        public string ImageUrl { get; set; }
        public decimal PrixUnitaire { get; set; }
        public int Quantite { get; set; }
        public decimal PrixTotal { get; set; }
        public List<ComplementPanierViewModel> Complements { get; set; } = new List<ComplementPanierViewModel>();
    }

    public class ComplementPanierViewModel
    {
        public int Id { get; set; }
        public string Nom { get; set; }
        public decimal Prix { get; set; }
        public int Quantite { get; set; }
    }
}