/**
 * Stepstonks Temu Theme – Main JS
 */
(function () {
    'use strict';

    /* ---- Countdown Timer ---- */
    function initCountdowns() {
        document.querySelectorAll('[data-countdown]').forEach(function (el) {
            var end = new Date(el.dataset.countdown).getTime();
            function tick() {
                var diff = end - Date.now();
                if (diff <= 0) { el.innerHTML = '<span class="countdown-expired">Ended</span>'; return; }
                var h = Math.floor(diff / 3600000);
                var m = Math.floor((diff % 3600000) / 60000);
                var s = Math.floor((diff % 60000) / 1000);
                var hEl = el.querySelector('.cd-h');
                var mEl = el.querySelector('.cd-m');
                var sEl = el.querySelector('.cd-s');
                if (hEl) hEl.textContent = String(h).padStart(2, '0');
                if (mEl) mEl.textContent = String(m).padStart(2, '0');
                if (sEl) sEl.textContent = String(s).padStart(2, '0');
            }
            tick();
            setInterval(tick, 1000);
        });
    }

    /* ---- Gallery Thumbs ---- */
    function initGallery() {
        var mains = document.querySelectorAll('.product-gallery-main img');
        document.querySelectorAll('.gallery-thumb').forEach(function (thumb) {
            thumb.addEventListener('click', function () {
                var src = this.querySelector('img').src;
                var gallery = this.closest('.product-gallery-wrap');
                var mainImg = gallery.querySelector('.product-gallery-main img');
                if (mainImg) mainImg.src = src;
                gallery.querySelectorAll('.gallery-thumb').forEach(function (t) { t.classList.remove('active'); });
                this.classList.add('active');
            });
        });
    }

    /* ---- Tabs ---- */
    function initTabs() {
        document.querySelectorAll('.tab-list').forEach(function (list) {
            list.querySelectorAll('.tab-btn').forEach(function (btn) {
                btn.addEventListener('click', function () {
                    var target = this.dataset.tab;
                    var wrap = this.closest('.product-tabs');
                    wrap.querySelectorAll('.tab-btn').forEach(function (b) { b.classList.remove('active'); });
                    wrap.querySelectorAll('.tab-panel').forEach(function (p) { p.classList.remove('active'); });
                    this.classList.add('active');
                    var panel = wrap.querySelector('#' + target);
                    if (panel) panel.classList.add('active');
                });
            });
        });
    }

    /* ---- Variant Buttons ---- */
    function initVariants() {
        document.querySelectorAll('.variant-options').forEach(function (opts) {
            opts.querySelectorAll('.variant-btn, .color-btn').forEach(function (btn) {
                btn.addEventListener('click', function () {
                    opts.querySelectorAll('.variant-btn, .color-btn').forEach(function (b) { b.classList.remove('selected'); });
                    this.classList.add('selected');
                });
            });
        });
    }

    /* ---- Quantity Control ---- */
    function initQtyControls() {
        document.querySelectorAll('.qty-control').forEach(function (ctrl) {
            var input = ctrl.querySelector('.qty-input');
            ctrl.querySelector('.qty-minus').addEventListener('click', function () {
                var val = parseInt(input.value, 10) || 1;
                if (val > 1) input.value = val - 1;
                input.dispatchEvent(new Event('change'));
            });
            ctrl.querySelector('.qty-plus').addEventListener('click', function () {
                var val = parseInt(input.value, 10) || 1;
                var max = parseInt(input.max, 10) || 9999;
                if (val < max) input.value = val + 1;
                input.dispatchEvent(new Event('change'));
            });
        });
    }

    /* ---- Mini Cart ---- */
    function initMiniCart() {
        var overlay = document.getElementById('mini-cart-overlay');
        if (!overlay) return;
        var openBtns = document.querySelectorAll('[data-open-cart]');
        var closeBtn = document.getElementById('mini-cart-close');

        function open() { overlay.classList.add('open'); document.body.style.overflow = 'hidden'; }
        function close() { overlay.classList.remove('open'); document.body.style.overflow = ''; }

        openBtns.forEach(function (btn) { btn.addEventListener('click', open); });
        if (closeBtn) closeBtn.addEventListener('click', close);
        overlay.addEventListener('click', function (e) { if (e.target === overlay) close(); });

        document.addEventListener('keydown', function (e) { if (e.key === 'Escape') close(); });
    }

    /* ---- Wishlist Toggle ---- */
    function initWishlist() {
        document.querySelectorAll('.product-wishlist-btn').forEach(function (btn) {
            btn.addEventListener('click', function (e) {
                e.preventDefault();
                e.stopPropagation();
                this.classList.toggle('active');
                var added = this.classList.contains('active');
                showToast(added ? 'Added to Wishlist ❤️' : 'Removed from Wishlist', added ? 'success' : '');
            });
        });
    }

    /* ---- Toast ---- */
    function showToast(msg, type) {
        var container = document.getElementById('toast-container');
        if (!container) {
            container = document.createElement('div');
            container.id = 'toast-container';
            container.className = 'toast-container';
            document.body.appendChild(container);
        }
        var toast = document.createElement('div');
        toast.className = 'toast ' + (type || '');
        toast.textContent = msg;
        container.appendChild(toast);
        requestAnimationFrame(function () {
            requestAnimationFrame(function () { toast.classList.add('show'); });
        });
        setTimeout(function () {
            toast.classList.remove('show');
            setTimeout(function () { toast.remove(); }, 400);
        }, 3000);
    }

    /* ---- Back to Top ---- */
    function initBackToTop() {
        var btn = document.getElementById('back-to-top');
        if (!btn) return;
        window.addEventListener('scroll', function () {
            btn.classList.toggle('visible', window.scrollY > 400);
        }, { passive: true });
        btn.addEventListener('click', function () { window.scrollTo({ top: 0, behavior: 'smooth' }); });
    }

    /* ---- Sticky ATC (single product) ---- */
    function initStickyAtc() {
        var trigger = document.querySelector('.add-to-cart-area');
        var sticky = document.querySelector('.sticky-atc');
        if (!trigger || !sticky) return;
        var obs = new IntersectionObserver(function (entries) {
            entries.forEach(function (e) {
                sticky.style.display = e.isIntersecting ? 'none' : 'flex';
            });
        }, { rootMargin: '0px' });
        obs.observe(trigger);
    }

    /* ---- Lazy Image Loading ---- */
    function initLazyImages() {
        if (!('IntersectionObserver' in window)) return;
        var obs = new IntersectionObserver(function (entries) {
            entries.forEach(function (e) {
                if (e.isIntersecting) {
                    var img = e.target;
                    if (img.dataset.src) {
                        img.src = img.dataset.src;
                        img.removeAttribute('data-src');
                    }
                    obs.unobserve(img);
                }
            });
        }, { rootMargin: '200px' });
        document.querySelectorAll('img[data-src]').forEach(function (img) { obs.observe(img); });
    }

    /* ---- Smooth Hover on Product Cards ---- */
    function initProductCards() {
        document.querySelectorAll('.product-card').forEach(function (card) {
            card.addEventListener('click', function (e) {
                if (e.target.closest('.product-wishlist-btn')) return;
                var link = this.querySelector('a.product-link');
                if (link) link.click();
            });
        });
    }

    /* ---- Add to Cart AJAX ---- */
    function initAjaxCart() {
        document.querySelectorAll('.btn-cart[data-product-id]').forEach(function (btn) {
            btn.addEventListener('click', function (e) {
                e.preventDefault();
                var id = this.dataset.productId;
                var nonce = window.stepstonks_ajax ? window.stepstonks_ajax.nonce : '';
                var url = window.stepstonks_ajax ? window.stepstonks_ajax.ajax_url : '/wp-admin/admin-ajax.php';
                var self = this;
                self.disabled = true;
                self.textContent = 'Adding...';

                fetch(url, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                    body: new URLSearchParams({ action: 'stepstonks_add_to_cart', product_id: id, nonce: nonce, qty: 1 })
                })
                .then(function (r) { return r.json(); })
                .then(function (data) {
                    if (data.success) {
                        showToast('Added to cart!', 'cart');
                        var countEl = document.querySelector('.cart-count');
                        if (countEl && data.data && data.data.cart_count) {
                            countEl.textContent = data.data.cart_count;
                        }
                    } else {
                        showToast('Could not add to cart', 'error');
                    }
                })
                .catch(function () { showToast('Network error', 'error'); })
                .finally(function () {
                    self.disabled = false;
                    self.textContent = 'Add to Cart';
                });
            });
        });
    }

    /* ---- Filter/Sort ---- */
    function initFilters() {
        document.querySelectorAll('.filter-btn[data-sort]').forEach(function (btn) {
            btn.addEventListener('click', function () {
                document.querySelectorAll('.filter-btn[data-sort]').forEach(function (b) { b.classList.remove('active'); });
                this.classList.add('active');
                var url = new URL(window.location.href);
                url.searchParams.set('orderby', this.dataset.sort);
                window.location.href = url.toString();
            });
        });
    }

    /* ---- Mobile Menu ---- */
    function initMobileMenu() {
        var toggle = document.getElementById('mobile-menu-toggle');
        var menu = document.getElementById('mobile-menu');
        if (!toggle || !menu) return;
        toggle.addEventListener('click', function () {
            var open = menu.classList.toggle('open');
            document.body.style.overflow = open ? 'hidden' : '';
        });
    }

    /* ---- Search Suggestions (simple) ---- */
    function initSearchSuggestions() {
        var input = document.querySelector('.header-search input');
        if (!input) return;
        var timeout;
        input.addEventListener('input', function () {
            clearTimeout(timeout);
            timeout = setTimeout(function () {
                /* Placeholder: real implementation would hit WP REST API */
            }, 300);
        });
    }

    /* ---- Init ---- */
    document.addEventListener('DOMContentLoaded', function () {
        initCountdowns();
        initGallery();
        initTabs();
        initVariants();
        initQtyControls();
        initMiniCart();
        initWishlist();
        initBackToTop();
        initStickyAtc();
        initLazyImages();
        initProductCards();
        initAjaxCart();
        initFilters();
        initMobileMenu();
        initSearchSuggestions();
    });

    /* ---- Expose globally ---- */
    window.stepstonksTheme = { showToast: showToast };
})();
