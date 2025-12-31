using CloudinaryDotNet;
using CloudinaryDotNet.Actions;
using Microsoft.Extensions.Configuration;
using System;
using System.IO;
using System.Threading.Tasks;

namespace BrasilBurger.Web.Services
{
    public interface ICloudinaryService
    {
        Task<string> UploadImageAsync(Stream imageStream, string fileName);
        Task<bool> DeleteImageAsync(string publicId);
    }

    public class CloudinaryService : ICloudinaryService
    {
        private readonly Cloudinary _cloudinary;

        public CloudinaryService(IConfiguration configuration)
        {
            var account = new Account(
                configuration["Cloudinary:CloudName"],
                configuration["Cloudinary:ApiKey"],
                configuration["Cloudinary:ApiSecret"]
            );
            _cloudinary = new Cloudinary(account);
        }

        public async Task<string> UploadImageAsync(Stream imageStream, string fileName)
        {
            try
            {
                var uploadParams = new ImageUploadParams
                {
                    File = new FileDescription(fileName, imageStream),
                    Folder = "brasilburger",
                    Transformation = new Transformation()
                        .Width(800)
                        .Height(600)
                        .Crop("fill")
                        .Quality("auto")
                };

                var uploadResult = await _cloudinary.UploadAsync(uploadParams);

                if (uploadResult.StatusCode == System.Net.HttpStatusCode.OK)
                {
                    return uploadResult.SecureUrl.ToString();
                }

                throw new Exception($"Upload failed: {uploadResult.Error?.Message}");
            }
            catch (Exception ex)
            {
                throw new Exception($"Error uploading image: {ex.Message}", ex);
            }
        }

        public async Task<bool> DeleteImageAsync(string publicId)
        {
            try
            {
                var deleteParams = new DeletionParams(publicId);
                var result = await _cloudinary.DestroyAsync(deleteParams);
                return result.Result == "ok";
            }
            catch
            {
                return false;
            }
        }
    }
}