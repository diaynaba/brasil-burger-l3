using System;
using System.Threading.Tasks;
using BrasilBurger.Web.Data;
using BrasilBurger.Web.Models.Entities;

namespace BrasilBurger.Web.Repositories
{
    public interface IUnitOfWork : IDisposable
    {
        IGenericRepository<Client> Clients { get; }
        IGenericRepository<Burger> Burgers { get; }
        IGenericRepository<Complement> Complements { get; }
        IGenericRepository<Menu> Menus { get; }
        IGenericRepository<MenuBurger> MenuBurgers { get; }
        IGenericRepository<MenuComplement> MenuComplements { get; }
        IGenericRepository<Commande> Commandes { get; }
        IGenericRepository<CommandeItem> CommandeItems { get; }
        IGenericRepository<CommandeComplement> CommandeComplements { get; }
        IGenericRepository<Paiement> Paiements { get; }
        IGenericRepository<Zone> Zones { get; }
        
        Task<int> SaveAsync();
        int Save();
    }

    public class UnitOfWork : IUnitOfWork
    {
        private readonly ApplicationDbContext _context;
        
        private IGenericRepository<Client> _clients;
        private IGenericRepository<Burger> _burgers;
        private IGenericRepository<Complement> _complements;
        private IGenericRepository<Menu> _menus;
        private IGenericRepository<MenuBurger> _menuBurgers;
        private IGenericRepository<MenuComplement> _menuComplements;
        private IGenericRepository<Commande> _commandes;
        private IGenericRepository<CommandeItem> _commandeItems;
        private IGenericRepository<CommandeComplement> _commandeComplements;
        private IGenericRepository<Paiement> _paiements;
        private IGenericRepository<Zone> _zones;

        public UnitOfWork(ApplicationDbContext context)
        {
            _context = context;
        }

        public IGenericRepository<Client> Clients => 
            _clients ??= new GenericRepository<Client>(_context);

        public IGenericRepository<Burger> Burgers => 
            _burgers ??= new GenericRepository<Burger>(_context);

        public IGenericRepository<Complement> Complements => 
            _complements ??= new GenericRepository<Complement>(_context);

        public IGenericRepository<Menu> Menus => 
            _menus ??= new GenericRepository<Menu>(_context);

        public IGenericRepository<MenuBurger> MenuBurgers => 
            _menuBurgers ??= new GenericRepository<MenuBurger>(_context);

        public IGenericRepository<MenuComplement> MenuComplements => 
            _menuComplements ??= new GenericRepository<MenuComplement>(_context);

        public IGenericRepository<Commande> Commandes => 
            _commandes ??= new GenericRepository<Commande>(_context);

        public IGenericRepository<CommandeItem> CommandeItems => 
            _commandeItems ??= new GenericRepository<CommandeItem>(_context);

        public IGenericRepository<CommandeComplement> CommandeComplements => 
            _commandeComplements ??= new GenericRepository<CommandeComplement>(_context);

        public IGenericRepository<Paiement> Paiements => 
            _paiements ??= new GenericRepository<Paiement>(_context);

        public IGenericRepository<Zone> Zones => 
            _zones ??= new GenericRepository<Zone>(_context);

        public async Task<int> SaveAsync()
        {
            return await _context.SaveChangesAsync();
        }

        public int Save()
        {
            return _context.SaveChanges();
        }

        public void Dispose()
        {
            _context.Dispose();
        }
    }
}