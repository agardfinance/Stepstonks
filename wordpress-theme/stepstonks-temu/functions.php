<?php
/**
 * Stepstonks Temu Theme – functions.php
 */

defined('ABSPATH') || exit;

/* =====================================================
   Theme Setup
===================================================== */
function stepstonks_setup() {
    load_theme_textdomain('stepstonks-temu', get_template_directory() . '/languages');

    add_theme_support('title-tag');
    add_theme_support('post-thumbnails');
    add_theme_support('html5', ['search-form', 'comment-form', 'comment-list', 'gallery', 'caption', 'style', 'script']);
    add_theme_support('custom-logo', [
        'height'      => 60,
        'width'       => 200,
        'flex-height' => true,
        'flex-width'  => true,
    ]);
    add_theme_support('woocommerce', [
        'thumbnail_image_width' => 400,
        'gallery_thumbnail_image_width' => 100,
        'single_image_width'    => 600,
    ]);
    add_theme_support('wc-product-gallery-zoom');
    add_theme_support('wc-product-gallery-lightbox');
    add_theme_support('wc-product-gallery-slider');
    add_theme_support('automatic-feed-links');

    register_nav_menus([
        'primary'   => __('Primary Menu', 'stepstonks-temu'),
        'category'  => __('Category Nav', 'stepstonks-temu'),
        'footer'    => __('Footer Menu', 'stepstonks-temu'),
    ]);

    // Thumbnail sizes
    add_image_size('stepstonks-product-card', 400, 400, true);
    add_image_size('stepstonks-product-wide', 800, 600, true);
    add_image_size('stepstonks-hero', 1280, 480, true);
}
add_action('after_setup_theme', 'stepstonks_setup');

/* =====================================================
   Enqueue Scripts & Styles
===================================================== */
function stepstonks_enqueue_assets() {
    $ver = wp_get_theme()->get('Version');

    wp_enqueue_style('stepstonks-variables', get_template_directory_uri() . '/style.css', [], $ver);
    wp_enqueue_style('stepstonks-main',      get_template_directory_uri() . '/assets/css/main.css', ['stepstonks-variables'], $ver);

    wp_enqueue_script('stepstonks-main', get_template_directory_uri() . '/assets/js/main.js', [], $ver, true);

    wp_localize_script('stepstonks-main', 'stepstonks_ajax', [
        'ajax_url'  => admin_url('admin-ajax.php'),
        'nonce'     => wp_create_nonce('stepstonks_nonce'),
        'cart_url'  => wc_get_cart_url(),
        'shop_url'  => get_permalink(wc_get_page_id('shop')),
    ]);

    if (is_singular() && comments_open() && get_option('thread_comments')) {
        wp_enqueue_script('comment-reply');
    }
}
add_action('wp_enqueue_scripts', 'stepstonks_enqueue_assets');

/* =====================================================
   Widget Areas
===================================================== */
function stepstonks_register_sidebars() {
    $shared = [
        'before_widget' => '<div id="%1$s" class="widget %2$s">',
        'after_widget'  => '</div>',
        'before_title'  => '<h3 class="widget-title">',
        'after_title'   => '</h3>',
    ];

    register_sidebar(array_merge($shared, [
        'name' => __('Primary Sidebar', 'stepstonks-temu'),
        'id'   => 'sidebar-1',
    ]));

    register_sidebar(array_merge($shared, [
        'name' => __('Footer Column 1', 'stepstonks-temu'),
        'id'   => 'footer-1',
    ]));

    register_sidebar(array_merge($shared, [
        'name' => __('Footer Column 2', 'stepstonks-temu'),
        'id'   => 'footer-2',
    ]));

    register_sidebar(array_merge($shared, [
        'name' => __('Homepage – Above Products', 'stepstonks-temu'),
        'id'   => 'homepage-top',
    ]));
}
add_action('widgets_init', 'stepstonks_register_sidebars');

/* =====================================================
   Include WooCommerce helpers
===================================================== */
require_once get_template_directory() . '/inc/woocommerce.php';

/* =====================================================
   WooCommerce Customisations
===================================================== */

// Remove default WC wrappers and use our own layout
remove_action('woocommerce_before_main_content', 'woocommerce_output_content_wrapper', 10);
remove_action('woocommerce_after_main_content',  'woocommerce_output_content_wrapper_end', 10);

add_action('woocommerce_before_main_content', function () {
    echo '<main id="main" class="wc-main"><div class="container">';
}, 10);

add_action('woocommerce_after_main_content', function () {
    echo '</div></main>';
}, 10);

// Remove default WC breadcrumbs sidebar
remove_action('woocommerce_sidebar', 'woocommerce_get_sidebar', 10);

// Products per page
add_filter('loop_shop_per_page', function () { return 24; }, 20);

// Product columns
add_filter('loop_shop_columns', function () { return 4; });

// Remove default WC styles we replace
add_filter('woocommerce_enqueue_styles', function ($styles) {
    // Keep only block editor styles; we handle the rest
    unset($styles['woocommerce-layout']);
    unset($styles['woocommerce-smallscreen']);
    return $styles;
});

/* =====================================================
   AJAX: Add to Cart
===================================================== */
function stepstonks_ajax_add_to_cart() {
    check_ajax_referer('stepstonks_nonce', 'nonce');

    $product_id = absint($_POST['product_id'] ?? 0);
    $qty        = absint($_POST['qty'] ?? 1);

    if (!$product_id) {
        wp_send_json_error(['message' => 'Invalid product']);
    }

    $added = WC()->cart->add_to_cart($product_id, $qty);

    if ($added) {
        wp_send_json_success([
            'message'    => 'Product added',
            'cart_count' => WC()->cart->get_cart_contents_count(),
            'cart_total' => WC()->cart->get_cart_total(),
        ]);
    } else {
        wp_send_json_error(['message' => 'Could not add to cart']);
    }
}
add_action('wp_ajax_stepstonks_add_to_cart',        'stepstonks_ajax_add_to_cart');
add_action('wp_ajax_nopriv_stepstonks_add_to_cart', 'stepstonks_ajax_add_to_cart');

/* =====================================================
   Flash Deals helper
===================================================== */
function stepstonks_get_flash_deals($limit = 10) {
    return wc_get_products([
        'status'   => 'publish',
        'limit'    => $limit,
        'orderby'  => 'date',
        'order'    => 'DESC',
        'meta_key' => '_sale_price',
        'meta_compare' => '!=',
        'meta_value'   => '',
    ]);
}

/* =====================================================
   Custom Excerpt Length
===================================================== */
add_filter('excerpt_length', function () { return 18; });

/* =====================================================
   Customizer: theme options
===================================================== */
function stepstonks_customizer(WP_Customize_Manager $wp_customize) {
    $wp_customize->add_section('stepstonks_options', [
        'title'    => __('Stepstonks Temu Options', 'stepstonks-temu'),
        'priority' => 30,
    ]);

    // Primary colour
    $wp_customize->add_setting('primary_color', ['default' => '#ff6900', 'sanitize_callback' => 'sanitize_hex_color']);
    $wp_customize->add_control(new WP_Customize_Color_Control($wp_customize, 'primary_color', [
        'label'   => __('Primary Color', 'stepstonks-temu'),
        'section' => 'stepstonks_options',
    ]));

    // Secondary colour
    $wp_customize->add_setting('secondary_color', ['default' => '#ff3d00', 'sanitize_callback' => 'sanitize_hex_color']);
    $wp_customize->add_control(new WP_Customize_Color_Control($wp_customize, 'secondary_color', [
        'label'   => __('Secondary / Sale Color', 'stepstonks-temu'),
        'section' => 'stepstonks_options',
    ]));

    // Hero headline
    $wp_customize->add_setting('hero_headline', ['default' => 'Up to 90% Off!', 'sanitize_callback' => 'sanitize_text_field']);
    $wp_customize->add_control('hero_headline', [
        'label'   => __('Hero Headline', 'stepstonks-temu'),
        'section' => 'stepstonks_options',
        'type'    => 'text',
    ]);

    // Flash deals end time
    $wp_customize->add_setting('flash_deals_end', ['default' => '', 'sanitize_callback' => 'sanitize_text_field']);
    $wp_customize->add_control('flash_deals_end', [
        'label'       => __('Flash Deals End (ISO 8601)', 'stepstonks-temu'),
        'description' => '2025-12-31T23:59:59',
        'section'     => 'stepstonks_options',
        'type'        => 'text',
    ]);

    // Enable sticky ATC on mobile
    $wp_customize->add_setting('sticky_atc', ['default' => true, 'sanitize_callback' => 'wp_validate_boolean']);
    $wp_customize->add_control('sticky_atc', [
        'label'   => __('Sticky Add-to-Cart on Mobile', 'stepstonks-temu'),
        'section' => 'stepstonks_options',
        'type'    => 'checkbox',
    ]);
}
add_action('customize_register', 'stepstonks_customizer');

/* Apply customizer colors inline */
function stepstonks_customizer_inline_css() {
    $primary   = get_theme_mod('primary_color',   '#ff6900');
    $secondary = get_theme_mod('secondary_color', '#ff3d00');

    $css = ":root{--color-primary:{$primary};--color-secondary:{$secondary};--color-primary-dark:" . stepstonks_darken_hex($primary, 15) . ";}";
    wp_add_inline_style('stepstonks-variables', $css);
}
add_action('wp_enqueue_scripts', 'stepstonks_customizer_inline_css', 20);

function stepstonks_darken_hex(string $hex, int $pct): string {
    $hex = ltrim($hex, '#');
    $r = hexdec(substr($hex, 0, 2));
    $g = hexdec(substr($hex, 2, 2));
    $b = hexdec(substr($hex, 4, 2));
    $factor = 1 - $pct / 100;
    return sprintf('#%02x%02x%02x', (int)($r * $factor), (int)($g * $factor), (int)($b * $factor));
}

/* =====================================================
   Product Sold Count (fake/real meta)
===================================================== */
function stepstonks_increment_sold_count(int $order_id): void {
    $order = wc_get_order($order_id);
    if (!$order) return;
    foreach ($order->get_items() as $item) {
        $product_id = $item->get_product_id();
        $sold = (int) get_post_meta($product_id, '_stepstonks_sold_count', true);
        update_post_meta($product_id, '_stepstonks_sold_count', $sold + (int) $item->get_quantity());
    }
}
add_action('woocommerce_order_status_completed', 'stepstonks_increment_sold_count');

function stepstonks_get_sold_count(int $product_id): int {
    $real = (int) get_post_meta($product_id, '_stepstonks_sold_count', true);
    // Seed with a display offset so new products don't show "0 sold"
    $seed = (int) get_post_meta($product_id, '_stepstonks_sold_seed', true);
    if (!$seed) {
        $seed = rand(10, 250);
        update_post_meta($product_id, '_stepstonks_sold_seed', $seed);
    }
    return $real + $seed;
}

/* =====================================================
   Body Classes
===================================================== */
add_filter('body_class', function ($classes) {
    if (is_woocommerce()) $classes[] = 'woocommerce-page';
    return $classes;
});

/* =====================================================
   Disable WC default wrappers for category / shop
===================================================== */
add_action('woocommerce_before_shop_loop', function () {
    woocommerce_result_count();
    woocommerce_catalog_ordering();
}, 30);
