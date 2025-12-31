using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace BrasilBurger.Web.Models.Entities
{
    [Table("commande_item")]
    public class CommandeItem
    {
        [Key]
        [Column("id")]
        public int Id { get; set; }

        [Required]
        [Column("commande_id")]
        public int CommandeId { get; set; }

        [Column("burger_id")]
        public int? BurgerId { get; set; }

        [Column("menu_id")]
        public int? MenuId { get; set; }

        [Required]
        [Column("quantite")]
        public int Quantite { get; set; } = 1;

        [Required]
        [Column("prix_unitaire")]
        public decimal PrixUnitaire { get; set; }

        [Required]
        [Column("prix_total")]
        public decimal PrixTotal { get; set; }

        [ForeignKey("CommandeId")]
        public virtual Commande? Commande { get; set; }

        [ForeignKey("BurgerId")]
        public virtual Burger? Burger { get; set; }

        [ForeignKey("MenuId")]
        public virtual Menu? Menu { get; set; }
    }

    [Table("commande_complement")]
    public class CommandeComplement
    {
        [Key]
        [Column("id")]
        public int Id { get; set; }

        [Required]
        [Column("commande_id")]
        public int CommandeId { get; set; }

        [Required]
        [Column("complement_id")]
        public int ComplementId { get; set; }

        [Required]
        [Column("quantite")]
        public int Quantite { get; set; } = 1;

        [Required]
        [Column("prix_unitaire")]
        public decimal PrixUnitaire { get; set; }

        [Required]
        [Column("prix_total")]
        public decimal PrixTotal { get; set; }

        [ForeignKey("CommandeId")]
        public virtual Commande? Commande { get; set; }

        [ForeignKey("ComplementId")]
        public virtual Complement? Complement { get; set; }
    }
}