<?php
/**
 * WooCommerce integration helpers
 */
defined('ABSPATH') || exit;

/* ---- Attribute table helper (used in single product) ---- */
if (!function_exists('wc_get_product_attribute_table')) {
    function wc_get_product_attribute_table(WC_Product_Attribute $attribute, WC_Product $product): string {
        if ($attribute->is_taxonomy()) {
            $terms = wp_get_post_terms($product->get_id(), $attribute->get_name(), ['fields' => 'names']);
            return implode(', ', $terms);
        }
        return implode(', ', $attribute->get_options());
    }
}

/* ---- Remove WC default title on shop ---- */
add_filter('woocommerce_show_page_title', '__return_false');

/* ---- Move WC notices above content ---- */
remove_action('woocommerce_before_main_content', 'woocommerce_breadcrumb', 20);

/* ---- Disable quantity fields for grouped products ---- */
add_filter('woocommerce_quantity_input_args', function ($args, $product) {
    if ($product->is_type('grouped')) {
        $args['min_value'] = 1;
    }
    return $args;
}, 10, 2);
