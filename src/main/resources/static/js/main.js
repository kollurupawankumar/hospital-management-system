// Main JavaScript file for Hospital Management System

document.addEventListener('DOMContentLoaded', function() {
    // Initialize tooltips
    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });

    // Initialize popovers
    var popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'));
    var popoverList = popoverTriggerList.map(function (popoverTriggerEl) {
        return new bootstrap.Popover(popoverTriggerEl);
    });

    // Auto-hide alerts after 5 seconds
    setTimeout(function() {
        var alerts = document.querySelectorAll('.alert:not(.alert-permanent)');
        alerts.forEach(function(alert) {
            var bsAlert = new bootstrap.Alert(alert);
            bsAlert.close();
        });
    }, 5000);

    // Toggle sidebar on mobile
    var sidebarToggle = document.getElementById('sidebarToggle');
    if (sidebarToggle) {
        sidebarToggle.addEventListener('click', function() {
            document.body.classList.toggle('sidebar-toggled');
            document.querySelector('.sidebar').classList.toggle('toggled');
            document.querySelector('.sidebar').classList.toggle('d-none');
            document.querySelector('.sidebar').classList.toggle('d-block');
        });
    }
    
    // Close sidebar when clicking outside on mobile
    document.addEventListener('click', function(event) {
        var sidebar = document.querySelector('.sidebar');
        var sidebarToggle = document.getElementById('sidebarToggle');
        
        if (window.innerWidth <= 768 && 
            sidebar && 
            sidebar.classList.contains('toggled') && 
            !sidebar.contains(event.target) && 
            event.target !== sidebarToggle) {
            sidebar.classList.remove('toggled');
            sidebar.classList.add('d-none');
            sidebar.classList.remove('d-block');
            document.body.classList.remove('sidebar-toggled');
        }
    });

    // Add active class to current nav item
    var currentLocation = window.location.pathname;
    var navLinks = document.querySelectorAll('.sidebar .nav-link');
    navLinks.forEach(function(link) {
        if (link.getAttribute('href') === currentLocation) {
            link.classList.add('active');
            
            // If this link is inside a collapse, make sure the collapse is open
            var collapseElement = link.closest('.collapse');
            if (collapseElement) {
                collapseElement.classList.add('show');
                var collapseToggle = document.querySelector('[data-bs-toggle="collapse"][href="#' + collapseElement.id + '"]');
                if (collapseToggle) {
                    collapseToggle.setAttribute('aria-expanded', 'true');
                }
            }
        }
    });
    
    // Make sure the admin submenu is open on the admin dashboard page
    if (currentLocation.includes('/admin/dashboard')) {
        var adminSubmenu = document.getElementById('adminSubmenu');
        if (adminSubmenu) {
            adminSubmenu.classList.add('show');
            var adminToggle = document.querySelector('[data-bs-toggle="collapse"][href="#adminSubmenu"]');
            if (adminToggle) {
                adminToggle.setAttribute('aria-expanded', 'true');
            }
        }
    }
});