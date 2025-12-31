using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System.Linq;
using System.Threading.Tasks;
using BrasilBurger.Web.Models.ViewModels;
using BrasilBurger.Web.Repositories;
using BrasilBurger.Web.Data;

namespace BrasilBurger.Web.Controllers
{
    public class CatalogueController : Controller
    {
        private readonly IUnitOfWork _unitOfWork;
        private readonly ApplicationDbContext _context;

        public CatalogueController(IUnitOfWork unitOfWork, ApplicationDbContext context)
        {
            _unitOfWork = unitOfWork;
            _context = context;
        }

        public async Task<IActionResult> Index(string filter = "all", string search = null)
        {
            var burgersQuery = _context.Burgers
                .Where(b => !b.IsArchived && b.IsDisponible);

            var menusQuery = _context.Menus
                .Include(m => m.MenuBurgers)
                    .ThenInclude(mb => mb.Burger)
                .Include(m => m.MenuComplements)
                    .ThenInclude(mc => mc.Complement)
                .Where(m => !m.IsArchived && m.IsDisponible);

            if (!string.IsNullOrEmpty(search))
            {
                burgersQuery = burgersQuery.Where(b => 
                    b.Nom.ToLower().Contains(search.ToLower()) ||
                    b.Description.ToLower().Contains(search.ToLower()));

                menusQuery = menusQuery.Where(m => 
                    m.Nom.ToLower().Contains(search.ToLower()) ||
                    m.Description.ToLower().Contains(search.ToLower()));
            }

            var model = new CatalogueViewModel
            {
                FilterType = filter,
                SearchQuery = search
            };

            if (filter == "all" || filter == "burgers")
            {
                model.Burgers = await burgersQuery.ToListAsync();
            }

            if (filter == "all" || filter == "menus")
            {
                model.Menus = await menusQuery.ToListAsync();
            }

            if (filter == "all")
            {
                var complements = await _unitOfWork.Complements
                    .FindAsync(c => !c.IsArchived && c.IsDisponible);
                model.Complements = complements.ToList();
            }

            return View(model);
        }

        public async Task<IActionResult> BurgerDetail(int id)
        {
            var burger = await _context.Burgers
                .FirstOrDefaultAsync(b => b.Id == id && !b.IsArchived);

            if (burger == null)
            {
                return NotFound();
            }

            var complements = await _unitOfWork.Complements
                .FindAsync(c => !c.IsArchived && c.IsDisponible);

            var burgersSimilaires = await _context.Burgers
                .Where(b => b.Id != id && !b.IsArchived && b.IsDisponible)
                .Take(3)
                .ToListAsync();

            var model = new BurgerDetailViewModel
            {
                Burger = burger,
                ComplementsDisponibles = complements.ToList(),
                BurgersSimilaires = burgersSimilaires
            };

            return View(model);
        }

        public async Task<IActionResult> MenuDetail(int id)
        {
            var menu = await _context.Menus
                .Include(m => m.MenuBurgers)
                    .ThenInclude(mb => mb.Burger)
                .Include(m => m.MenuComplements)
                    .ThenInclude(mc => mc.Complement)
                .FirstOrDefaultAsync(m => m.Id == id && !m.IsArchived);

            if (menu == null)
            {
                return NotFound();
            }

            var model = new MenuDetailViewModel
            {
                Menu = menu,
                Burgers = menu.MenuBurgers.ToList(),
                Complements = menu.MenuComplements.ToList(),
                PrixTotal = menu.Prix
            };

            return View(model);
        }
    }
}