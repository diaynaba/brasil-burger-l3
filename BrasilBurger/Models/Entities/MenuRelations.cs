using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace BrasilBurger.Web.Models.Entities
{
    [Table("menu_burger")]
    public class MenuBurger
    {
        [Key]
        [Column("id")]
        public int Id { get; set; }

        [Required]
        [Column("menu_id")]
        public int MenuId { get; set; }

        [Required]
        [Column("burger_id")]
        public int BurgerId { get; set; }

        [Column("quantite")]
        public int Quantite { get; set; } = 1;

        [ForeignKey("MenuId")]
        public virtual Menu? Menu { get; set; }

        [ForeignKey("BurgerId")]
        public virtual Burger? Burger { get; set; }
    }

    [Table("menu_complement")]
    public class MenuComplement
    {
        [Key]
        [Column("id")]
        public int Id { get; set; }

        [Required]
        [Column("menu_id")]
        public int MenuId { get; set; }

        [Required]
        [Column("complement_id")]
        public int ComplementId { get; set; }

        [Column("quantite")]
        public int Quantite { get; set; } = 1;

        [ForeignKey("MenuId")]
        public virtual Menu? Menu { get; set; }

        [ForeignKey("ComplementId")]
        public virtual Complement? Complement { get; set; }
    }
}