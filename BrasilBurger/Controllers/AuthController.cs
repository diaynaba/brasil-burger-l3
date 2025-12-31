using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Authentication;
using Microsoft.AspNetCore.Authentication.Cookies;
using System.Security.Claims;
using System.Threading.Tasks;
using System.Collections.Generic;
using BCrypt.Net;
using BrasilBurger.Web.Models.ViewModels;
using BrasilBurger.Web.Models.Entities;
using BrasilBurger.Web.Repositories;

namespace BrasilBurger.Web.Controllers
{
    public class AuthController : Controller
    {
        private readonly IUnitOfWork _unitOfWork;

        public AuthController(IUnitOfWork unitOfWork)
        {
            _unitOfWork = unitOfWork;
        }

        [HttpGet]
        public IActionResult Login(string? returnUrl = null)
        {
            if (User?.Identity?.IsAuthenticated == true)
            {
                return RedirectToAction("Index", "Catalogue");
            }

            ViewData["ReturnUrl"] = returnUrl;
            return View();
        }

        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Login(LoginViewModel model, string? returnUrl = null)
        {
            ViewData["ReturnUrl"] = returnUrl;

            if (!ModelState.IsValid)
            {
                return View(model);
            }

            var client = await _unitOfWork.Clients
                .FirstOrDefaultAsync(c => c.Email == model.Email);

            if (client == null || !client.IsActive || !BCrypt.Net.BCrypt.Verify(model.Password, client.Password))
            {
                ModelState.AddModelError(string.Empty, "Email ou mot de passe incorrect.");
                return View(model);
            }

            var claims = new List<Claim>
            {
                new Claim(ClaimTypes.NameIdentifier, client.Id.ToString()),
                new Claim(ClaimTypes.Name, client.NomComplet),
                new Claim(ClaimTypes.Email, client.Email),
                new Claim("Telephone", client.Telephone)
            };

            var claimsIdentity = new ClaimsIdentity(claims, CookieAuthenticationDefaults.AuthenticationScheme);
            var authProperties = new AuthenticationProperties
            {
                IsPersistent = model.RememberMe,
                ExpiresUtc = model.RememberMe ? 
                    System.DateTimeOffset.UtcNow.AddDays(30) : 
                    System.DateTimeOffset.UtcNow.AddHours(2)
            };

            await HttpContext.SignInAsync(
                CookieAuthenticationDefaults.AuthenticationScheme,
                new ClaimsPrincipal(claimsIdentity),
                authProperties);

            if (!string.IsNullOrEmpty(returnUrl) && Url.IsLocalUrl(returnUrl))
            {
                return Redirect(returnUrl);
            }

            return RedirectToAction("Index", "Catalogue");
        }

        [HttpGet]
        public IActionResult Register()
        {
            if (User?.Identity?.IsAuthenticated == true)
            {
                return RedirectToAction("Index", "Catalogue");
            }

            return View();
        }

        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Register(RegisterViewModel model)
        {
            if (!ModelState.IsValid)
            {
                return View(model);
            }

            var existingClient = await _unitOfWork.Clients
                .FirstOrDefaultAsync(c => c.Email == model.Email);

            if (existingClient != null)
            {
                ModelState.AddModelError("Email", "Cet email est déjà utilisé.");
                return View(model);
            }

            var client = new Client
            {
                Nom = model.Nom,
                Prenom = model.Prenom,
                Email = model.Email,
                Telephone = model.Telephone,
                Adresse = model.Adresse,
                Password = BCrypt.Net.BCrypt.HashPassword(model.Password)
            };

            await _unitOfWork.Clients.AddAsync(client);
            await _unitOfWork.SaveAsync();

            TempData["SuccessMessage"] = "Inscription réussie ! Veuillez vous connecter.";
            return RedirectToAction(nameof(Login));
        }

        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Logout()
        {
            await HttpContext.SignOutAsync(CookieAuthenticationDefaults.AuthenticationScheme);
            return RedirectToAction("Index", "Home");
        }

        public IActionResult AccessDenied()
        {
            return View();
        }
    }
}