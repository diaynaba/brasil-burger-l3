// Site JavaScript

$(document).ready(function() {
    // Update panier count on page load
    updatePanierCount();

    // Navbar scroll effect
    $(window).scroll(function() {
        if ($(this).scrollTop() > 50) {
            $('.navbar').addClass('scrolled');
        } else {
            $('.navbar').removeClass('scrolled');
        }
    });

    // Smooth scroll for anchor links
    $('a[href^="#"]').on('click', function(e) {
        e.preventDefault();
        var target = $(this.hash);
        if (target.length) {
            $('html, body').animate({
                scrollTop: target.offset().top - 70
            }, 800);
        }
    });

    // Auto-hide alerts after 5 seconds
    setTimeout(function() {
        $('.alert').fadeOut('slow');
    }, 5000);

    // Add fade-in animation to cards
    $('.card').each(function(index) {
        $(this).delay(100 * index).queue(function(next) {
            $(this).addClass('fade-in');
            next();
        });
    });

    // Price formatting
    $('.price-format').each(function() {
        var price = parseFloat($(this).text());
        $(this).text(price.toLocaleString('fr-FR', { minimumFractionDigits: 0 }));
    });
});

// Function to update panier count
function updatePanierCount() {
    $.ajax({
        url: '/Panier/GetNombreItems',
        type: 'GET',
        success: function(response) {
            $('#panier-count').text(response.nombreItems);
            if (response.nombreItems > 0) {
                $('#panier-count').show();
            } else {
                $('#panier-count').hide();
            }
        }
    });
}

// Add to cart with animation
function addToCartAnimation(button) {
    var originalText = button.html();
    button.html('<i class="fas fa-spinner fa-spin"></i> Ajout...');
    button.prop('disabled', true);

    setTimeout(function() {
        button.html('<i class="fas fa-check"></i> Ajouté !');
        button.removeClass('btn-danger').addClass('btn-success');

        setTimeout(function() {
            button.html(originalText);
            button.removeClass('btn-success').addClass('btn-danger');
            button.prop('disabled', false);
        }, 1500);
    }, 500);
}

// Format currency
function formatCurrency(amount) {
    return amount.toLocaleString('fr-FR', { minimumFractionDigits: 0 }) + ' FCFA';
}

// Show loading overlay
function showLoading() {
    $('body').append('<div class="loading-overlay"><div class="spinner-border text-danger" role="status"><span class="visually-hidden">Chargement...</span></div></div>');
}

// Hide loading overlay
function hideLoading() {
    $('.loading-overlay').remove();
}

// Confirm dialog
function confirmAction(message, callback) {
    if (confirm(message)) {
        callback();
    }
}

// Toast notification
function showToast(message, type = 'success') {
    var iconClass = type === 'success' ? 'fa-check-circle' : 'fa-exclamation-circle';
    var bgClass = type === 'success' ? 'bg-success' : 'bg-danger';

    var toast = $('<div class="toast-notification ' + bgClass + ' text-white">' +
        '<i class="fas ' + iconClass + '"></i> ' + message +
        '</div>');

    $('body').append(toast);

    setTimeout(function() {
        toast.addClass('show');
    }, 100);

    setTimeout(function() {
        toast.removeClass('show');
        setTimeout(function() {
            toast.remove();
        }, 300);
    }, 3000);
}

// Image lazy loading
document.addEventListener("DOMContentLoaded", function() {
    var lazyImages = [].slice.call(document.querySelectorAll("img.lazy"));

    if ("IntersectionObserver" in window) {
        let lazyImageObserver = new IntersectionObserver(function(entries, observer) {
            entries.forEach(function(entry) {
                if (entry.isIntersecting) {
                    let lazyImage = entry.target;
                    lazyImage.src = lazyImage.dataset.src;
                    lazyImage.classList.remove("lazy");
                    lazyImageObserver.unobserve(lazyImage);
                }
            });
        });

        lazyImages.forEach(function(lazyImage) {
            lazyImageObserver.observe(lazyImage);
        });
    }
});

// Form validation enhancement
(function() {
    'use strict';
    window.addEventListener('load', function() {
        var forms = document.getElementsByClassName('needs-validation');
        var validation = Array.prototype.filter.call(forms, function(form) {
            form.addEventListener('submit', function(event) {
                if (form.checkValidity() === false) {
                    event.preventDefault();
                    event.stopPropagation();
                }
                form.classList.add('was-validated');
            }, false);
        });
    }, false);
})();