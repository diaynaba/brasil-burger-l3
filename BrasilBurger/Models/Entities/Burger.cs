using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace BrasilBurger.Web.Models.Entities
{
    [Table("burger")]
    public class Burger
    {
        [Key]
        [Column("id")]
        public int Id { get; set; }

        [Required]
        [StringLength(100)]
        [Column("nom")]
        public string Nom { get; set; } = string.Empty;

        [Required]
        [Column("prix")]
        public decimal Prix { get; set; }

        // ✅ colonne réelle en base
        [Column("image")]
        public string? Image { get; set; }

        // ✅ colonne réelle en base
        [Column("est_archive")]
        public bool EstArchive { get; set; }

        [Column("date_creation")]
        public DateTime DateCreation { get; set; }

        /* ============================
           PROPRIÉTÉS COMPATIBILITÉ
           (utilisées par controllers/views)
           ============================ */

        [NotMapped]
        public bool IsArchived => EstArchive;

        [NotMapped]
        public bool IsDisponible => !EstArchive;

        [NotMapped]
        public string ImageUrl => Image ?? "/images/default-burger.png";

        [NotMapped]
        public string Description => "Burger savoureux fait maison 🍔";

        // Relations
        public virtual ICollection<MenuBurger> MenuBurgers { get; set; } = new List<MenuBurger>();
        public virtual ICollection<CommandeItem> CommandeItems { get; set; } = new List<CommandeItem>();
    }
}
