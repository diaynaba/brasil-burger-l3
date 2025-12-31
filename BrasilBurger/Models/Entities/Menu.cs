using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;

namespace BrasilBurger.Web.Models.Entities
{
    [Table("menu")]
    public class Menu
    {
        [Key]
        [Column("id")]
        public int Id { get; set; }

        [Required]
        [StringLength(100)]
        [Column("nom")]
        public string Nom { get; set; } = string.Empty;

        [StringLength(500)]
        [Column("description")]
        public string? Description { get; set; }

        [StringLength(255)]
        [Column("image_url")]
        public string? ImageUrl { get; set; }

        [Column("is_disponible")]
        public bool IsDisponible { get; set; } = true;

        [Column("is_archived")]
        public bool IsArchived { get; set; } = false;

        [Column("date_creation")]
        public DateTime DateCreation { get; set; } = DateTime.Now;

        public virtual ICollection<MenuBurger> MenuBurgers { get; set; } = new List<MenuBurger>();
        public virtual ICollection<MenuComplement> MenuComplements { get; set; } = new List<MenuComplement>();
        public virtual ICollection<CommandeItem> CommandeItems { get; set; } = new List<CommandeItem>();

        [NotMapped]
        public decimal Prix
        {
            get
            {
                decimal total = 0;
                if (MenuBurgers != null && MenuBurgers.Any())
                {
                    total += MenuBurgers.Sum(mb => mb.Burger?.Prix ?? 0);
                }
                if (MenuComplements != null && MenuComplements.Any())
                {
                    total += MenuComplements.Sum(mc => mc.Complement?.Prix ?? 0);
                }
                return total;
            }
        }
    }
}