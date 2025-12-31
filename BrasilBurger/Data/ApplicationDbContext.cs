using Microsoft.EntityFrameworkCore;
using BrasilBurger.Web.Models.Entities;

namespace BrasilBurger.Web.Data
{
    public class ApplicationDbContext : DbContext
    {
        public ApplicationDbContext(DbContextOptions<ApplicationDbContext> options)
            : base(options)
        {
        }

        public DbSet<Client> Clients { get; set; }
        public DbSet<Burger> Burgers { get; set; }
        public DbSet<Complement> Complements { get; set; }
        public DbSet<Menu> Menus { get; set; }
        public DbSet<MenuBurger> MenuBurgers { get; set; }
        public DbSet<MenuComplement> MenuComplements { get; set; }
        public DbSet<Commande> Commandes { get; set; }
        public DbSet<CommandeItem> CommandeItems { get; set; }
        public DbSet<CommandeComplement> CommandeComplements { get; set; }
        public DbSet<Paiement> Paiements { get; set; }
        public DbSet<Zone> Zones { get; set; }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            base.OnModelCreating(modelBuilder);

            // Configuration des relations
            modelBuilder.Entity<MenuBurger>()
                .HasOne(mb => mb.Menu)
                .WithMany(m => m.MenuBurgers)
                .HasForeignKey(mb => mb.MenuId)
                .OnDelete(DeleteBehavior.Cascade);

            modelBuilder.Entity<MenuBurger>()
                .HasOne(mb => mb.Burger)
                .WithMany(b => b.MenuBurgers)
                .HasForeignKey(mb => mb.BurgerId)
                .OnDelete(DeleteBehavior.Cascade);

            modelBuilder.Entity<MenuComplement>()
                .HasOne(mc => mc.Menu)
                .WithMany(m => m.MenuComplements)
                .HasForeignKey(mc => mc.MenuId)
                .OnDelete(DeleteBehavior.Cascade);

            modelBuilder.Entity<MenuComplement>()
                .HasOne(mc => mc.Complement)
                .WithMany(c => c.MenuComplements)
                .HasForeignKey(mc => mc.ComplementId)
                .OnDelete(DeleteBehavior.Cascade);

            modelBuilder.Entity<Commande>()
                .HasOne(c => c.Client)
                .WithMany(cl => cl.Commandes)
                .HasForeignKey(c => c.ClientId)
                .OnDelete(DeleteBehavior.Restrict);

            modelBuilder.Entity<CommandeItem>()
                .HasOne(ci => ci.Commande)
                .WithMany(c => c.CommandeItems)
                .HasForeignKey(ci => ci.CommandeId)
                .OnDelete(DeleteBehavior.Cascade);

            modelBuilder.Entity<CommandeComplement>()
                .HasOne(cc => cc.Commande)
                .WithMany(c => c.CommandeComplements)
                .HasForeignKey(cc => cc.CommandeId)
                .OnDelete(DeleteBehavior.Cascade);

            modelBuilder.Entity<Paiement>()
                .HasOne(p => p.Commande)
                .WithOne(c => c.Paiement)
                .HasForeignKey<Paiement>(p => p.CommandeId)
                .OnDelete(DeleteBehavior.Cascade);

            // Configuration des types décimaux pour PostgreSQL
            modelBuilder.Entity<Burger>()
                .Property(b => b.Prix)
                .HasPrecision(10, 2);

            modelBuilder.Entity<Complement>()
                .Property(c => c.Prix)
                .HasPrecision(10, 2);

            modelBuilder.Entity<Commande>()
                .Property(c => c.MontantTotal)
                .HasPrecision(10, 2);

            modelBuilder.Entity<CommandeItem>()
                .Property(ci => ci.PrixUnitaire)
                .HasPrecision(10, 2);

            modelBuilder.Entity<CommandeItem>()
                .Property(ci => ci.PrixTotal)
                .HasPrecision(10, 2);

            modelBuilder.Entity<CommandeComplement>()
                .Property(cc => cc.PrixUnitaire)
                .HasPrecision(10, 2);

            modelBuilder.Entity<CommandeComplement>()
                .Property(cc => cc.PrixTotal)
                .HasPrecision(10, 2);

            modelBuilder.Entity<Paiement>()
                .Property(p => p.Montant)
                .HasPrecision(10, 2);

            modelBuilder.Entity<Zone>()
                .Property(z => z.PrixLivraison)
                .HasPrecision(10, 2);
        }
    }
}