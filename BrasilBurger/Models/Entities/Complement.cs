using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace BrasilBurger.Web.Models.Entities
{
    [Table("complement")]
    public class Complement
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

        [Required]
        [StringLength(50)]
        [Column("type")]
        public string Type { get; set; } = string.Empty;

        [StringLength(255)]
        [Column("image_url")]
        public string? ImageUrl { get; set; }

        [Column("is_disponible")]
        public bool IsDisponible { get; set; } = true;

        [Column("is_archived")]
        public bool IsArchived { get; set; } = false;

        [Column("date_creation")]
        public DateTime DateCreation { get; set; } = DateTime.Now;

        public virtual ICollection<MenuComplement> MenuComplements { get; set; } = new List<MenuComplement>();
        public virtual ICollection<CommandeComplement> CommandeComplements { get; set; } = new List<CommandeComplement>();
    }
}