using System.ComponentModel.DataAnnotations;

namespace BrasilBurger.Web.Models.ViewModels
{
    public class LoginViewModel
    {
        [Required(ErrorMessage = "L'email est requis")]
        [EmailAddress(ErrorMessage = "Email invalide")]
        public string Email { get; set; }

        [Required(ErrorMessage = "Le mot de passe est requis")]
        [DataType(DataType.Password)]
        public string Password { get; set; }

        public bool RememberMe { get; set; }
        public string ReturnUrl { get; set; }
    }

    public class RegisterViewModel
    {
        [Required(ErrorMessage = "Le nom est requis")]
        [StringLength(100, ErrorMessage = "Le nom ne doit pas dépasser 100 caractères")]
        public string Nom { get; set; }

        [Required(ErrorMessage = "Le prénom est requis")]
        [StringLength(100, ErrorMessage = "Le prénom ne doit pas dépasser 100 caractères")]
        public string Prenom { get; set; }

        [Required(ErrorMessage = "L'email est requis")]
        [EmailAddress(ErrorMessage = "Email invalide")]
        public string Email { get; set; }

        [Required(ErrorMessage = "Le téléphone est requis")]
        [Phone(ErrorMessage = "Numéro de téléphone invalide")]
        [StringLength(20)]
        public string Telephone { get; set; }

        [StringLength(255)]
        public string Adresse { get; set; }

        [Required(ErrorMessage = "Le mot de passe est requis")]
        [StringLength(100, MinimumLength = 6, ErrorMessage = "Le mot de passe doit contenir au moins 6 caractères")]
        [DataType(DataType.Password)]
        public string Password { get; set; }

        [Required(ErrorMessage = "Veuillez confirmer le mot de passe")]
        [DataType(DataType.Password)]
        [Compare("Password", ErrorMessage = "Les mots de passe ne correspondent pas")]
        public string ConfirmPassword { get; set; }
    }

    public class ProfilViewModel
    {
        public int Id { get; set; }

        [Required(ErrorMessage = "Le nom est requis")]
        [StringLength(100)]
        public string Nom { get; set; }

        [Required(ErrorMessage = "Le prénom est requis")]
        [StringLength(100)]
        public string Prenom { get; set; }

        [Required(ErrorMessage = "L'email est requis")]
        [EmailAddress(ErrorMessage = "Email invalide")]
        public string Email { get; set; }

        [Required(ErrorMessage = "Le téléphone est requis")]
        [Phone(ErrorMessage = "Numéro de téléphone invalide")]
        public string Telephone { get; set; }

        [StringLength(255)]
        public string Adresse { get; set; }
    }

    public class ChangePasswordViewModel
    {
        [Required(ErrorMessage = "Le mot de passe actuel est requis")]
        [DataType(DataType.Password)]
        public string CurrentPassword { get; set; }

        [Required(ErrorMessage = "Le nouveau mot de passe est requis")]
        [StringLength(100, MinimumLength = 6, ErrorMessage = "Le mot de passe doit contenir au moins 6 caractères")]
        [DataType(DataType.Password)]
        public string NewPassword { get; set; }

        [Required(ErrorMessage = "Veuillez confirmer le nouveau mot de passe")]
        [DataType(DataType.Password)]
        [Compare("NewPassword", ErrorMessage = "Les mots de passe ne correspondent pas")]
        public string ConfirmPassword { get; set; }
    }
}