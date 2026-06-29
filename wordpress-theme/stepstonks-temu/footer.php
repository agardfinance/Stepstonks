<?php
/**
 * Footer template
 */
?>

<!-- ========== APP DOWNLOAD BANNER ========== -->
<section class="app-banner" aria-label="<?php esc_attr_e('Download our app', 'stepstonks-temu'); ?>">
    <div class="app-banner-content">
        <h2><?php esc_html_e('Shop on the Go!', 'stepstonks-temu'); ?></h2>
        <p><?php esc_html_e('Get exclusive app-only deals and track your orders anywhere.', 'stepstonks-temu'); ?></p>
        <div class="app-store-btns">
            <a href="#" class="app-store-btn">📱 App Store</a>
            <a href="#" class="app-store-btn">🤖 Google Play</a>
        </div>
    </div>
    <div class="app-banner-graphic" aria-hidden="true">📦</div>
</section>

<!-- ========== TRUST BADGES ========== -->
<div class="trust-badges" role="list" aria-label="<?php esc_attr_e('Trust & safety', 'stepstonks-temu'); ?>">
    <div class="trust-badge" role="listitem">
        <span class="trust-badge-icon" aria-hidden="true">🚚</span>
        <strong><?php esc_html_e('Free Shipping', 'stepstonks-temu'); ?></strong>
        <small><?php esc_html_e('On orders over $25', 'stepstonks-temu'); ?></small>
    </div>
    <div class="trust-badge" role="listitem">
        <span class="trust-badge-icon" aria-hidden="true">↩️</span>
        <strong><?php esc_html_e('Easy Returns', 'stepstonks-temu'); ?></strong>
        <small><?php esc_html_e('30-day hassle-free', 'stepstonks-temu'); ?></small>
    </div>
    <div class="trust-badge" role="listitem">
        <span class="trust-badge-icon" aria-hidden="true">🔒</span>
        <strong><?php esc_html_e('Secure Payment', 'stepstonks-temu'); ?></strong>
        <small><?php esc_html_e('256-bit SSL encrypted', 'stepstonks-temu'); ?></small>
    </div>
    <div class="trust-badge" role="listitem">
        <span class="trust-badge-icon" aria-hidden="true">⭐</span>
        <strong><?php esc_html_e('Top Rated', 'stepstonks-temu'); ?></strong>
        <small><?php esc_html_e('4.8 / 5 from 50k reviews', 'stepstonks-temu'); ?></small>
    </div>
    <div class="trust-badge" role="listitem">
        <span class="trust-badge-icon" aria-hidden="true">💬</span>
        <strong><?php esc_html_e('24/7 Support', 'stepstonks-temu'); ?></strong>
        <small><?php esc_html_e('Live chat always open', 'stepstonks-temu'); ?></small>
    </div>
</div>

<!-- ========== FOOTER ========== -->
<footer id="site-footer" role="contentinfo">
    <div class="footer-top">
        <div class="container">
            <div class="footer-grid">

                <!-- Brand -->
                <div class="footer-brand">
                    <h3><?php bloginfo('name'); ?></h3>
                    <p><?php bloginfo('description'); ?></p>
                    <div class="footer-social" aria-label="<?php esc_attr_e('Social media links', 'stepstonks-temu'); ?>">
                        <a href="#" class="social-btn" aria-label="Facebook">f</a>
                        <a href="#" class="social-btn" aria-label="Instagram">📸</a>
                        <a href="#" class="social-btn" aria-label="Twitter/X">𝕏</a>
                        <a href="#" class="social-btn" aria-label="TikTok">▶</a>
                    </div>
                </div>

                <!-- Shop -->
                <div class="footer-col">
                    <h4><?php esc_html_e('Shop', 'stepstonks-temu'); ?></h4>
                    <ul class="footer-links">
                        <?php if (class_exists('WooCommerce')): ?>
                            <li><a href="<?php echo esc_url(get_permalink(wc_get_page_id('shop'))); ?>"><?php esc_html_e('All Products', 'stepstonks-temu'); ?></a></li>
                            <li><a href="<?php echo esc_url(add_query_arg('orderby', 'popularity', get_permalink(wc_get_page_id('shop')))); ?>"><?php esc_html_e('Best Sellers', 'stepstonks-temu'); ?></a></li>
                            <li><a href="<?php echo esc_url(add_query_arg('orderby', 'date', get_permalink(wc_get_page_id('shop')))); ?>"><?php esc_html_e('New Arrivals', 'stepstonks-temu'); ?></a></li>
                            <li><a href="<?php echo esc_url(add_query_arg('on_sale', '1', get_permalink(wc_get_page_id('shop')))); ?>"><?php esc_html_e('Flash Deals', 'stepstonks-temu'); ?></a></li>
                        <?php endif; ?>
                    </ul>
                </div>

                <!-- Help -->
                <div class="footer-col">
                    <h4><?php esc_html_e('Help', 'stepstonks-temu'); ?></h4>
                    <ul class="footer-links">
                        <li><a href="#"><?php esc_html_e('Track My Order', 'stepstonks-temu'); ?></a></li>
                        <li><a href="#"><?php esc_html_e('Shipping Info', 'stepstonks-temu'); ?></a></li>
                        <li><a href="#"><?php esc_html_e('Returns & Refunds', 'stepstonks-temu'); ?></a></li>
                        <li><a href="#"><?php esc_html_e('FAQ', 'stepstonks-temu'); ?></a></li>
                        <li><a href="#"><?php esc_html_e('Contact Us', 'stepstonks-temu'); ?></a></li>
                    </ul>
                </div>

                <!-- Account -->
                <div class="footer-col">
                    <h4><?php esc_html_e('Account', 'stepstonks-temu'); ?></h4>
                    <ul class="footer-links">
                        <?php if (class_exists('WooCommerce')): ?>
                            <li><a href="<?php echo esc_url(get_permalink(wc_get_page_id('myaccount'))); ?>"><?php esc_html_e('My Account', 'stepstonks-temu'); ?></a></li>
                            <li><a href="<?php echo esc_url(get_permalink(wc_get_page_id('myaccount')) . 'orders/'); ?>"><?php esc_html_e('Orders', 'stepstonks-temu'); ?></a></li>
                            <li><a href="<?php echo esc_url(get_permalink(wc_get_page_id('myaccount')) . 'edit-address/'); ?>"><?php esc_html_e('Addresses', 'stepstonks-temu'); ?></a></li>
                        <?php endif; ?>
                        <li><a href="#"><?php esc_html_e('Wishlist', 'stepstonks-temu'); ?></a></li>
                        <li><a href="#"><?php esc_html_e('Become a Seller', 'stepstonks-temu'); ?></a></li>
                    </ul>
                </div>

            </div>
        </div>
    </div>

    <div class="container">
        <div class="footer-bottom">
            <p>&copy; <?php echo date('Y'); ?> <?php bloginfo('name'); ?>. <?php esc_html_e('All rights reserved.', 'stepstonks-temu'); ?></p>
            <div class="payment-icons" aria-label="<?php esc_attr_e('Accepted payments', 'stepstonks-temu'); ?>">
                <span class="payment-icon">VISA</span>
                <span class="payment-icon">MC</span>
                <span class="payment-icon">AMEX</span>
                <span class="payment-icon">PayPal</span>
                <span class="payment-icon">Apple Pay</span>
                <span class="payment-icon">GPay</span>
            </div>
        </div>
    </div>
</footer>

<?php wp_footer(); ?>
</body>
</html>
