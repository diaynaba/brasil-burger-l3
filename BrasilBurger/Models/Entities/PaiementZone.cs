using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace BrasilBurger.Web.Models.Entities
{
    [Table("paiement")]
    public class Paiement
    {
        [Key]
        [Column("id")]
        public int Id { get; set; }

        [Required]
        [Column("commande_id")]
        public int CommandeId { get; set; }

        [Required]
        [StringLength(50)]
        [Column("methode")]
        public string Methode { get; set; } = string.Empty;

        [Required]
        [Column("montant")]
        public decimal Montant { get; set; }

        [Required]
        [StringLength(100)]
        [Column("reference")]
        public string Reference { get; set; } = string.Empty;

        [Column("date_paiement")]
        public DateTime DatePaiement { get; set; } = DateTime.Now;

        [StringLength(50)]
        [Column("statut")]
        public string Statut { get; set; } = "Validé";

        [ForeignKey("CommandeId")]
        public virtual Commande? Commande { get; set; }
    }

    [Table("zone")]
    public class Zone
    {
        [Key]
        [Column("id")]
        public int Id { get; set; }

        [Required]
        [StringLength(100)]
        [Column("nom")]
        public string Nom { get; set; } = string.Empty;

        [Required]
        [Column("prix_livraison")]
        public decimal PrixLivraison { get; set; }

        [StringLength(500)]
        [Column("quartiers")]
        public string? Quartiers { get; set; }

        [Column("is_active")]
        public bool IsActive { get; set; } = true;

        public virtual ICollection<Commande> Commandes { get; set; } = new List<Commande>();
    }
}