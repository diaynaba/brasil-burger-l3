using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace BrasilBurger.Web.Models.Entities
{
    [Table("commande")]
    public class Commande
    {
        [Key]
        [Column("id")]
        public int Id { get; set; }

        [Required]
        [Column("client_id")]
        public int ClientId { get; set; }

        [Required]
        [StringLength(50)]
        [Column("numero_commande")]
        public string NumeroCommande { get; set; } = string.Empty;

        [Required]
        [StringLength(50)]
        [Column("type_service")]
        public string TypeService { get; set; } = string.Empty;

        [Required]
        [StringLength(50)]
        [Column("etat")]
        public string Etat { get; set; } = "En attente";

        [Column("montant_total")]
        public decimal MontantTotal { get; set; }

        [StringLength(500)]
        [Column("adresse_livraison")]
        public string? AdresseLivraison { get; set; }

        [Column("zone_id")]
        public int? ZoneId { get; set; }

        [Column("livreur_id")]
        public int? LivreurId { get; set; }

        [Column("date_commande")]
        public DateTime DateCommande { get; set; } = DateTime.Now;

        [Column("date_livraison")]
        public DateTime? DateLivraison { get; set; }

        [StringLength(1000)]
        [Column("note")]
        public string? Note { get; set; }

        [ForeignKey("ClientId")]
        public virtual Client? Client { get; set; }

        [ForeignKey("ZoneId")]
        public virtual Zone? Zone { get; set; }

        public virtual ICollection<CommandeItem> CommandeItems { get; set; } = new List<CommandeItem>();
        public virtual ICollection<CommandeComplement> CommandeComplements { get; set; } = new List<CommandeComplement>();
        public virtual Paiement? Paiement { get; set; }

        [NotMapped]
        public bool IsPaye => Paiement != null;

        [NotMapped]
        public string EtatBadgeClass
        {
            get
            {
                return Etat switch
                {
                    "En attente" => "badge bg-warning",
                    "Validé" => "badge bg-info",
                    "En cours" => "badge bg-primary",
                    "Terminé" => "badge bg-success",
                    "Annulé" => "badge bg-danger",
                    _ => "badge bg-secondary"
                };
            }
        }
    }
}