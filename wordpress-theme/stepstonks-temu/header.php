<!DOCTYPE html>
<html <?php language_attributes(); ?>>
<head>
    <meta charset="<?php bloginfo('charset'); ?>">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="profile" href="https://gmpg.org/xfn/11">
    <?php wp_head(); ?>
</head>
<body <?php body_class(); ?>>
<?php wp_body_open(); ?>

<!-- ========== SITE HEADER ========== -->
<header id="site-header" role="banner">
    <div class="header-inner container">

        <!-- Logo -->
        <div class="site-logo">
            <?php if (has_custom_logo()): ?>
                <?php the_custom_logo(); ?>
            <?php else: ?>
                <a href="<?php echo esc_url(home_url('/')); ?>" aria-label="<?php bloginfo('name'); ?>">
                    <svg viewBox="0 0 32 32" fill="none" xmlns="http://www.w3.org/2000/svg" aria-hidden="true">
                        <circle cx="16" cy="16" r="16" fill="rgba(255,255,255,.2)"/>
                        <path d="M10 22l6-12 6 12M12.5 18h7" stroke="#fff" stroke-width="2.2" stroke-linecap="round"/>
                    </svg>
                    <span><?php bloginfo('name'); ?></span>
                </a>
            <?php endif; ?>
        </div>

        <!-- Search -->
        <div class="header-search" role="search">
            <form method="get" action="<?php echo esc_url(home_url('/')); ?>">
                <input
                    type="search"
                    name="s"
                    placeholder="<?php esc_attr_e('Search for anything...', 'stepstonks-temu'); ?>"
                    value="<?php echo get_search_query(); ?>"
                    autocomplete="off"
                    aria-label="<?php esc_attr_e('Search', 'stepstonks-temu'); ?>"
                >
                <?php if (class_exists('WooCommerce')): ?>
                    <input type="hidden" name="post_type" value="product">
                <?php endif; ?>
                <button type="submit" aria-label="<?php esc_attr_e('Search', 'stepstonks-temu'); ?>">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" aria-hidden="true">
                        <circle cx="11" cy="11" r="8"/><path d="m21 21-4.35-4.35"/>
                    </svg>
                </button>
            </form>
        </div>

        <!-- Actions -->
        <nav class="header-actions" aria-label="<?php esc_attr_e('Account & Cart', 'stepstonks-temu'); ?>">

            <?php if (is_user_logged_in()): ?>
                <a href="<?php echo esc_url(get_permalink(get_option('woocommerce_myaccount_page_id'))); ?>" class="header-btn" aria-label="<?php esc_attr_e('My Account', 'stepstonks-temu'); ?>">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" aria-hidden="true">
                        <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/>
                    </svg>
                    <span><?php esc_html_e('Account', 'stepstonks-temu'); ?></span>
                </a>
            <?php else: ?>
                <a href="<?php echo esc_url(wp_login_url()); ?>" class="header-btn" aria-label="<?php esc_attr_e('Sign In', 'stepstonks-temu'); ?>">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" aria-hidden="true">
                        <path d="M15 3h4a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2h-4"/><polyline points="10 17 15 12 10 7"/><line x1="15" y1="12" x2="3" y2="12"/>
                    </svg>
                    <span><?php esc_html_e('Sign In', 'stepstonks-temu'); ?></span>
                </a>
            <?php endif; ?>

            <button class="header-btn" data-open-cart aria-label="<?php esc_attr_e('Open cart', 'stepstonks-temu'); ?>">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" aria-hidden="true">
                    <circle cx="9" cy="21" r="1"/><circle cx="20" cy="21" r="1"/>
                    <path d="M1 1h4l2.68 13.39a2 2 0 0 0 2 1.61h9.72a2 2 0 0 0 2-1.61L23 6H6"/>
                </svg>
                <?php if (class_exists('WooCommerce')): ?>
                    <span class="cart-count" aria-live="polite"><?php echo WC()->cart ? WC()->cart->get_cart_contents_count() : 0; ?></span>
                <?php endif; ?>
                <span><?php esc_html_e('Cart', 'stepstonks-temu'); ?></span>
            </button>

        </nav>

    </div>
</header>

<!-- Category Nav -->
<nav class="category-nav" aria-label="<?php esc_attr_e('Category Navigation', 'stepstonks-temu'); ?>">
    <div class="category-nav-inner">
        <?php
        $cats = get_terms([
            'taxonomy'   => 'product_cat',
            'orderby'    => 'count',
            'order'      => 'DESC',
            'number'     => 12,
            'hide_empty' => true,
        ]);

        if (!is_wp_error($cats) && !empty($cats)):
            $icons = ['🏠', '👗', '📱', '💻', '🎮', '🏋️', '🛋️', '🍳', '💄', '🎒', '🔧', '🌱'];
            foreach ($cats as $i => $cat):
                $icon = $icons[$i] ?? '🛍️';
                $active = (is_tax('product_cat', $cat) || (isset($cat->term_id) && get_queried_object_id() == $cat->term_id)) ? 'active' : '';
        ?>
            <a href="<?php echo esc_url(get_term_link($cat)); ?>" class="category-nav-item <?php echo esc_attr($active); ?>">
                <span aria-hidden="true"><?php echo $icon; ?></span>
                <?php echo esc_html($cat->name); ?>
            </a>
        <?php endforeach; endif; ?>

        <a href="<?php echo esc_url(get_permalink(wc_get_page_id('shop'))); ?>" class="category-nav-item">
            🔥 <?php esc_html_e('All Deals', 'stepstonks-temu'); ?>
        </a>
    </div>
</nav>

<!-- Mini Cart Overlay -->
<div id="mini-cart-overlay" class="mini-cart-overlay" role="dialog" aria-modal="true" aria-label="<?php esc_attr_e('Shopping Cart', 'stepstonks-temu'); ?>">
    <aside class="mini-cart">
        <div class="mini-cart-header">
            <h2>🛒 <?php esc_html_e('My Cart', 'stepstonks-temu'); ?></h2>
            <button id="mini-cart-close" class="mini-cart-close" aria-label="<?php esc_attr_e('Close cart', 'stepstonks-temu'); ?>">✕</button>
        </div>

        <div class="mini-cart-body">
            <?php if (class_exists('WooCommerce') && WC()->cart && !WC()->cart->is_empty()): ?>
                <?php foreach (WC()->cart->get_cart() as $key => $item):
                    $product = $item['data'];
                    $img_id  = $product->get_image_id();
                    $img_url = $img_id ? wp_get_attachment_image_url($img_id, 'thumbnail') : wc_placeholder_img_src();
                ?>
                    <div class="cart-item">
                        <div class="cart-item-img">
                            <img src="<?php echo esc_url($img_url); ?>" alt="<?php echo esc_attr($product->get_name()); ?>" loading="lazy">
                        </div>
                        <div class="cart-item-details">
                            <div class="cart-item-name"><?php echo esc_html($product->get_name()); ?></div>
                            <div class="cart-item-price"><?php echo wp_kses_post(WC()->cart->get_product_subtotal($product, $item['quantity'])); ?></div>
                            <div style="font-size:.75rem;color:var(--color-text-muted)">Qty: <?php echo esc_html($item['quantity']); ?></div>
                        </div>
                    </div>
                <?php endforeach; ?>
            <?php else: ?>
                <p style="text-align:center;padding:40px 20px;color:var(--color-text-muted)">
                    🛒 <?php esc_html_e('Your cart is empty', 'stepstonks-temu'); ?>
                </p>
            <?php endif; ?>
        </div>

        <?php if (class_exists('WooCommerce') && WC()->cart && !WC()->cart->is_empty()): ?>
        <div class="mini-cart-footer">
            <div class="cart-total-row">
                <span class="cart-total-label"><?php esc_html_e('Subtotal', 'stepstonks-temu'); ?></span>
                <span class="cart-total-val"><?php echo wp_kses_post(WC()->cart->get_cart_subtotal()); ?></span>
            </div>
            <a href="<?php echo esc_url(wc_get_checkout_url()); ?>" class="btn-checkout">
                <?php esc_html_e('Checkout', 'stepstonks-temu'); ?> →
            </a>
        </div>
        <?php endif; ?>
    </aside>
</div>

<button id="back-to-top" class="back-to-top" aria-label="<?php esc_attr_e('Back to top', 'stepstonks-temu'); ?>">↑</button>
