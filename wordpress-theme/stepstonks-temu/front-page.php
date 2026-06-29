<?php
/**
 * Front page template – Temu-style homepage
 */
get_header();
?>

<?php if (class_exists('WooCommerce')): ?>

    <!-- Hero Banner -->
    <?php get_template_part('template-parts/hero-banner'); ?>

    <!-- Flash Deals -->
    <?php get_template_part('template-parts/flash-deals'); ?>

    <!-- Category Grid -->
    <?php get_template_part('template-parts/category-grid'); ?>

    <!-- Promo Banners Row -->
    <?php get_template_part('template-parts/promo-banners'); ?>

    <!-- Best Sellers -->
    <?php
    $bestsellers = wc_get_products([
        'status'  => 'publish',
        'limit'   => 12,
        'orderby' => 'popularity',
        'order'   => 'DESC',
    ]);

    if ($bestsellers):
    ?>
    <section class="flash-deals-section" aria-label="<?php esc_attr_e('Best Sellers', 'stepstonks-temu'); ?>">
        <div class="section-header">
            <div class="section-title-wrap">
                <div class="section-icon" aria-hidden="true">🏆</div>
                <div>
                    <div class="section-title"><?php esc_html_e('Best Sellers', 'stepstonks-temu'); ?></div>
                    <div class="section-subtitle"><?php esc_html_e('Most popular right now', 'stepstonks-temu'); ?></div>
                </div>
            </div>
            <a href="<?php echo esc_url(add_query_arg('orderby', 'popularity', get_permalink(wc_get_page_id('shop')))); ?>" class="section-see-all">
                <?php esc_html_e('See All', 'stepstonks-temu'); ?> →
            </a>
        </div>

        <div class="products-scroll" role="list">
            <div class="products-scroll-inner">
                <?php foreach ($bestsellers as $product):
                    $id         = $product->get_id();
                    $name       = $product->get_name();
                    $price      = $product->get_price();
                    $reg_price  = $product->get_regular_price();
                    $sale_price = $product->get_sale_price();
                    $permalink  = get_permalink($id);
                    $img_id     = $product->get_image_id();
                    $img_url    = $img_id ? wp_get_attachment_image_url($img_id, 'stepstonks-product-card') : wc_placeholder_img_src('stepstonks-product-card');
                    $discount   = ($reg_price && $sale_price && $reg_price > 0) ? round((($reg_price - $sale_price) / $reg_price) * 100) : 0;
                    $sold       = stepstonks_get_sold_count($id);
                    $stock      = $product->get_stock_quantity() ?: rand(30, 200);
                    $total      = $sold + $stock;
                    $pct        = $total > 0 ? min(100, round(($sold / $total) * 100)) : 60;
                ?>
                    <article class="product-card" role="listitem">
                        <div class="product-card-img">
                            <a href="<?php echo esc_url($permalink); ?>" class="product-link" aria-label="<?php echo esc_attr($name); ?>">
                                <img src="<?php echo esc_url($img_url); ?>" alt="<?php echo esc_attr($name); ?>" loading="lazy" width="400" height="400">
                            </a>
                            <span class="product-badge badge-best"><?php esc_html_e('BEST', 'stepstonks-temu'); ?></span>
                            <?php if ($discount > 0): ?>
                                <div class="product-discount-badge">-<?php echo esc_html($discount); ?>%</div>
                            <?php endif; ?>
                        </div>
                        <div class="product-card-body">
                            <h3 class="product-card-name"><?php echo esc_html($name); ?></h3>
                            <div class="product-price-row">
                                <span class="product-price"><?php echo wp_kses_post(wc_price($price)); ?></span>
                                <?php if ($reg_price && $reg_price != $price): ?>
                                    <span class="product-price-original"><?php echo wp_kses_post(wc_price($reg_price)); ?></span>
                                <?php endif; ?>
                            </div>
                            <div class="product-sold-bar">
                                <div class="sold-bar-track">
                                    <div class="sold-bar-fill" style="width:<?php echo esc_attr($pct); ?>%"></div>
                                </div>
                                <div class="sold-bar-label"><span><?php echo esc_html(number_format($sold)); ?></span> <?php esc_html_e('sold', 'stepstonks-temu'); ?></div>
                            </div>
                        </div>
                        <button class="product-wishlist-btn" aria-label="<?php printf(esc_attr__('Add %s to wishlist', 'stepstonks-temu'), esc_attr($name)); ?>">
                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" aria-hidden="true">
                                <path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z"/>
                            </svg>
                        </button>
                    </article>
                <?php endforeach; ?>
            </div>
        </div>
    </section>
    <?php endif; ?>

    <!-- New Arrivals Grid -->
    <?php
    $new_arrivals = wc_get_products([
        'status'  => 'publish',
        'limit'   => 20,
        'orderby' => 'date',
        'order'   => 'DESC',
    ]);

    if ($new_arrivals):
    ?>
    <section class="products-grid-section" aria-label="<?php esc_attr_e('New Arrivals', 'stepstonks-temu'); ?>">
        <div style="background:var(--color-bg-white);border-radius:var(--radius-lg);margin-bottom:12px;box-shadow:var(--shadow-sm);">
            <div class="section-header">
                <div class="section-title-wrap">
                    <div class="section-icon" aria-hidden="true">✨</div>
                    <div>
                        <div class="section-title"><?php esc_html_e('New Arrivals', 'stepstonks-temu'); ?></div>
                        <div class="section-subtitle"><?php esc_html_e('Just dropped', 'stepstonks-temu'); ?></div>
                    </div>
                </div>
                <a href="<?php echo esc_url(add_query_arg('orderby', 'date', get_permalink(wc_get_page_id('shop')))); ?>" class="section-see-all">
                    <?php esc_html_e('See All', 'stepstonks-temu'); ?> →
                </a>
            </div>
            <div style="padding:12px 16px 20px;">
                <div class="products-grid" role="list">
                    <?php foreach ($new_arrivals as $product):
                        $id         = $product->get_id();
                        $name       = $product->get_name();
                        $price      = $product->get_price();
                        $reg_price  = $product->get_regular_price();
                        $sale_price = $product->get_sale_price();
                        $permalink  = get_permalink($id);
                        $img_id     = $product->get_image_id();
                        $img_url    = $img_id ? wp_get_attachment_image_url($img_id, 'stepstonks-product-card') : wc_placeholder_img_src();
                        $discount   = ($reg_price && $sale_price && $reg_price > 0) ? round((($reg_price - $sale_price) / $reg_price) * 100) : 0;
                        $sold       = stepstonks_get_sold_count($id);
                    ?>
                        <article class="product-card" role="listitem">
                            <div class="product-card-img">
                                <a href="<?php echo esc_url($permalink); ?>" class="product-link" aria-label="<?php echo esc_attr($name); ?>">
                                    <img src="<?php echo esc_url($img_url); ?>" alt="<?php echo esc_attr($name); ?>" loading="lazy" width="400" height="400">
                                </a>
                                <span class="product-badge badge-new"><?php esc_html_e('NEW', 'stepstonks-temu'); ?></span>
                                <?php if ($discount > 0): ?>
                                    <div class="product-discount-badge">-<?php echo esc_html($discount); ?>%</div>
                                <?php endif; ?>
                            </div>
                            <div class="product-card-body">
                                <h3 class="product-card-name"><?php echo esc_html($name); ?></h3>
                                <div class="product-price-row">
                                    <span class="product-price"><?php echo wp_kses_post(wc_price($price)); ?></span>
                                    <?php if ($reg_price && $reg_price != $price): ?>
                                        <span class="product-price-original"><?php echo wp_kses_post(wc_price($reg_price)); ?></span>
                                    <?php endif; ?>
                                </div>
                                <div class="sold-bar-label" style="margin-top:4px"><span><?php echo esc_html(number_format($sold)); ?></span> <?php esc_html_e('sold', 'stepstonks-temu'); ?></div>
                            </div>
                            <button class="product-wishlist-btn" aria-label="<?php printf(esc_attr__('Add %s to wishlist', 'stepstonks-temu'), esc_attr($name)); ?>">
                                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" aria-hidden="true">
                                    <path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z"/>
                                </svg>
                            </button>
                        </article>
                    <?php endforeach; ?>
                </div>
            </div>
        </div>
    </section>
    <?php endif; ?>

<?php else: ?>
    <!-- WooCommerce not active fallback -->
    <div class="container" style="padding:60px 12px;text-align:center">
        <h2 style="color:var(--color-primary)">Welcome to <?php bloginfo('name'); ?></h2>
        <p style="color:var(--color-text-muted);margin-top:10px">Please activate WooCommerce to start selling.</p>
    </div>
<?php endif; ?>

<?php get_footer(); ?>
