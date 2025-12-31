using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace BrasilBurger.Web.Models.Entities
{
    [Table("client")]
    public class Client
    {
        [Key]
        [Column("id")]
        public int Id { get; set; }

        [Required]
        [StringLength(100)]
        [Column("nom")]
        public string Nom { get; set; } = string.Empty;

        [Required]
        [StringLength(100)]
        [Column("prenom")]
        public string Prenom { get; set; } = string.Empty;

        [Required]
        [StringLength(20)]
        [Column("telephone")]
        public string Telephone { get; set; } = string.Empty;

        [Required]
        [EmailAddress]
        [StringLength(150)]
        [Column("email")]
        public string Email { get; set; } = string.Empty;

        [Required]
        [StringLength(255)]
        [Column("password")]
        public string Password { get; set; } = string.Empty;

        [StringLength(255)]
        [Column("adresse")]
        public string? Adresse { get; set; }

        [Column("date_creation")]
        public DateTime DateCreation { get; set; } = DateTime.Now;

        [Column("is_active")]
        public bool IsActive { get; set; } = true;

        public virtual ICollection<Commande> Commandes { get; set; } = new List<Commande>();

        [NotMapped]
        public string NomComplet => $"{Prenom} {Nom}";
    }
}