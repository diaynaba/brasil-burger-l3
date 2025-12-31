using System.Security.Claims;
using Microsoft.AspNetCore.Http;
using BrasilBurger.Web.Models.Entities;

namespace BrasilBurger.Web.Helpers
{
    public static class SessionHelper
    {
        public static int? GetClientId(this HttpContext context)
        {
            var clientIdClaim = context.User.FindFirst(ClaimTypes.NameIdentifier);
            if (clientIdClaim != null && int.TryParse(clientIdClaim.Value, out int clientId))
            {
                return clientId;
            }
            return null;
        }

        public static string GetClientName(this HttpContext context)
        {
            return context.User.FindFirst(ClaimTypes.Name)?.Value;
        }

        public static string GetClientEmail(this HttpContext context)
        {
            return context.User.FindFirst(ClaimTypes.Email)?.Value;
        }

        public static bool IsAuthenticated(this HttpContext context)
        {
            return context.User.Identity?.IsAuthenticated ?? false;
        }
    }

    public static class NumeroCommandeGenerator
    {
        public static string Generate()
        {
            var timestamp = System.DateTime.Now.ToString("yyyyMMddHHmmss");
            var random = new System.Random().Next(1000, 9999);
            return $"CMD{timestamp}{random}";
        }
    }

    public static class ReferenceGenerator
    {
        public static string GenerateReference(string methode)
        {
            var prefix = methode == "Wave" ? "WV" : "OM";
            var timestamp = System.DateTime.Now.ToString("yyyyMMddHHmmss");
            var random = new System.Random().Next(100000, 999999);
            return $"{prefix}{timestamp}{random}";
        }
    }
}