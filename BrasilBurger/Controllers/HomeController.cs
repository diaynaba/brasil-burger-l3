using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System.Linq;
using System.Threading.Tasks;
using BrasilBurger.Web.Models.ViewModels;
using BrasilBurger.Web.Repositories;

namespace BrasilBurger.Web.Controllers
{
    public class HomeController : Controller
    {
        private readonly IUnitOfWork _unitOfWork;

        public HomeController(IUnitOfWork unitOfWork)
        {
            _unitOfWork = unitOfWork;
        }

        public async Task<IActionResult> Index()
        {
            var burgers = await _unitOfWork.Burgers
                .FindAsync(b => !b.IsArchived && b.IsDisponible);
            
            var menus = await _unitOfWork.Menus
                .FindAsync(m => !m.IsArchived && m.IsDisponible);

            var model = new CatalogueViewModel
            {
                Burgers = burgers.Take(6).ToList(),
                Menus = menus.Take(4).ToList()
            };

            return View(model);
        }

        public IActionResult About()
        {
            return View();
        }

        public IActionResult Contact()
        {
            return View();
        }

        public IActionResult Privacy()
        {
            return View();
        }

        [ResponseCache(Duration = 0, Location = ResponseCacheLocation.None, NoStore = true)]
        public IActionResult Error()
        {
            return View();
        }
    }
}