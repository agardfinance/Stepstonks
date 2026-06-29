<?php
/**
 * WooCommerce archive-product.php override – shop / category pages
 */
defined('ABSPATH') || exit;

get_header('shop');

$orderby       = isset($_GET['orderby']) ? sanitize_key($_GET['orderby']) : get_option('woocommerce_default_catalog_orderby');
$sort_options  = [
    'menu_order' => __('Default', 'stepstonks-temu'),
    'popularity' => __('Best Sellers', 'stepstonks-temu'),
    'rating'     => __('Top Rated', 'stepstonks-temu'),
    'date'       => __('Newest', 'stepstonks-temu'),
    'price'      => __('Price: Low → High', 'stepstonks-temu'),
    'price-desc' => __('Price: High → Low', 'stepstonks-temu'),
];
?>

<main id="main" class="wc-main" style="padding-bottom:40px">
    <div class="container" style="padding-top:12px">

        <!-- Page Title + Breadcrumb -->
        <div style="margin-bottom:12px">
            <?php woocommerce_breadcrumb(); ?>
            <h1 style="font-size:1.3rem;font-weight:900;color:var(--color-text);margin-top:8px">
                <?php woocommerce_page_title(); ?>
            </h1>
        </div>

        <!-- Filter / Sort Bar -->
        <div class="filter-sort-bar">
            <svg viewBox="0 0 24 24" width="16" height="16" fill="none" stroke="currentColor" stroke-width="2" style="flex-shrink:0;color:var(--color-text-muted)" aria-hidden="true">
                <polygon points="22 3 2 3 10 12.46 10 19 14 21 14 12.46 22 3"/>
            </svg>
            <?php foreach ($sort_options as $val => $label): ?>
                <button
                    class="filter-btn <?php echo ($orderby === $val) ? 'active' : ''; ?>"
                    data-sort="<?php echo esc_attr($val); ?>"
                >
                    <?php echo esc_html($label); ?>
                </button>
            <?php endforeach; ?>

            <button class="filter-btn" style="margin-left:auto;border-color:var(--color-primary);color:var(--color-primary)">
                <svg viewBox="0 0 24 24" width="14" height="14" fill="none" stroke="currentColor" stroke-width="2" aria-hidden="true">
                    <line x1="4" y1="6" x2="20" y2="6"/><line x1="8" y1="12" x2="16" y2="12"/><line x1="11" y1="18" x2="13" y2="18"/>
                </svg>
                <?php esc_html_e('Filter', 'stepstonks-temu'); ?>
            </button>
        </div>

        <!-- Result Count -->
        <div style="font-size:.82rem;color:var(--color-text-muted);margin-bottom:10px">
            <?php woocommerce_result_count(); ?>
        </div>

        <?php if (woocommerce_product_loop()): ?>

            <?php do_action('woocommerce_before_shop_loop'); ?>

            <div class="products-grid" role="list">
                <?php while (have_posts()): the_post();
                    global $product;
                    if (!$product) continue;

                    $id         = $product->get_id();
                    $name       = $product->get_name();
                    $price      = $product->get_price();
                    $reg_price  = $product->get_regular_price();
                    $sale_price = $product->get_sale_price();
                    $permalink  = get_permalink($id);
                    $img_id     = $product->get_image_id();
                    $img_url    = $img_id ? wp_get_attachment_image_url($img_id, 'stepstonks-product-card') : wc_placeholder_img_src();
                    $discount   = ($reg_price && $sale_price && $reg_price > 0) ? round((($reg_price - $sale_price) / $reg_price) * 100) : 0;
                    $avg_rating = $product->get_average_rating();
                    $sold       = stepstonks_get_sold_count($id);
                ?>
                    <article class="product-card" role="listitem">
                        <div class="product-card-img">
                            <a href="<?php echo esc_url($permalink); ?>" class="product-link" aria-label="<?php echo esc_attr($name); ?>">
                                <img src="<?php echo esc_url($img_url); ?>" alt="<?php echo esc_attr($name); ?>" loading="lazy" width="400" height="400">
                            </a>

                            <?php if ($product->is_on_sale()): ?>
                                <span class="product-badge badge-sale"><?php esc_html_e('SALE', 'stepstonks-temu'); ?></span>
                            <?php elseif ($product->is_featured()): ?>
                                <span class="product-badge badge-hot"><?php esc_html_e('HOT', 'stepstonks-temu'); ?></span>
                            <?php elseif ((time() - get_post_time('U')) < WEEK_IN_SECONDS * 2): ?>
                                <span class="product-badge badge-new"><?php esc_html_e('NEW', 'stepstonks-temu'); ?></span>
                            <?php endif; ?>

                            <?php if ($discount > 0): ?>
                                <div class="product-discount-badge">-<?php echo esc_html($discount); ?>%</div>
                            <?php endif; ?>
                        </div>

                        <div class="product-card-body">
                            <h2 class="product-card-name"><?php echo esc_html($name); ?></h2>

                            <?php if ($avg_rating > 0): ?>
                                <div style="font-size:.72rem;color:#757575;margin-bottom:4px">
                                    <span style="color:#ffd600">★</span>
                                    <?php echo number_format($avg_rating, 1); ?>
                                    (<?php echo esc_html($product->get_review_count()); ?>)
                                </div>
                            <?php endif; ?>

                            <div class="product-price-row">
                                <span class="product-price"><?php echo wp_kses_post(wc_price($price)); ?></span>
                                <?php if ($reg_price && $reg_price != $price): ?>
                                    <span class="product-price-original"><?php echo wp_kses_post(wc_price($reg_price)); ?></span>
                                <?php endif; ?>
                            </div>

                            <div class="sold-bar-label" style="margin-top:5px">
                                <span><?php echo esc_html(number_format($sold)); ?></span> <?php esc_html_e('sold', 'stepstonks-temu'); ?>
                            </div>
                        </div>

                        <button
                            class="product-wishlist-btn"
                            aria-label="<?php printf(esc_attr__('Add %s to wishlist', 'stepstonks-temu'), esc_attr($name)); ?>"
                        >
                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" aria-hidden="true">
                                <path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z"/>
                            </svg>
                        </button>
                    </article>
                <?php endwhile; ?>
            </div>

            <?php do_action('woocommerce_after_shop_loop'); ?>

        <?php else: ?>
            <?php do_action('woocommerce_no_products_found'); ?>
        <?php endif; ?>

        <!-- Pagination -->
        <div class="pagination-wrap">
            <?php woocommerce_pagination(); ?>
        </div>

    </div>
</main>

<?php get_footer('shop'); ?>
