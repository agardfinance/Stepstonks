<?php
/**
 * Flash Deals section template part
 * @var WC_Product[] $products
 * @var string $end_time  ISO 8601 datetime for countdown
 */
$end_time = $end_time ?? get_theme_mod('flash_deals_end', date('Y-m-d\T23:59:59'));
$products = $products ?? stepstonks_get_flash_deals(12);

if (empty($products)) return;
?>

<section class="flash-deals-section" aria-label="<?php esc_attr_e('Flash Deals', 'stepstonks-temu'); ?>">

    <div class="section-header">
        <div class="section-title-wrap">
            <div class="section-icon" aria-hidden="true">⚡</div>
            <div>
                <div class="section-title"><?php esc_html_e('Flash Deals', 'stepstonks-temu'); ?></div>
                <div class="section-subtitle"><?php esc_html_e('Limited time – grab them before they\'re gone!', 'stepstonks-temu'); ?></div>
            </div>
        </div>

        <div class="countdown-timer" data-countdown="<?php echo esc_attr($end_time); ?>" aria-live="polite">
            <span class="countdown-label"><?php esc_html_e('Ends in', 'stepstonks-temu'); ?></span>
            <span class="countdown-part"><span class="cd-h">00</span></span>
            <span class="countdown-sep">:</span>
            <span class="countdown-part"><span class="cd-m">00</span></span>
            <span class="countdown-sep">:</span>
            <span class="countdown-part"><span class="cd-s">00</span></span>
        </div>

        <a href="<?php echo esc_url(get_permalink(wc_get_page_id('shop'))); ?>" class="section-see-all">
            <?php esc_html_e('See All', 'stepstonks-temu'); ?> →
        </a>
    </div>

    <div class="products-scroll" role="list">
        <div class="products-scroll-inner">
            <?php foreach ($products as $product):
                $id           = $product->get_id();
                $name         = $product->get_name();
                $price        = $product->get_price();
                $reg_price    = $product->get_regular_price();
                $sale_price   = $product->get_sale_price();
                $permalink    = get_permalink($id);
                $img_id       = $product->get_image_id();
                $img_url      = $img_id ? wp_get_attachment_image_url($img_id, 'stepstonks-product-card') : wc_placeholder_img_src('stepstonks-product-card');

                $discount_pct = ($reg_price && $sale_price && $reg_price > 0)
                    ? round((($reg_price - $sale_price) / $reg_price) * 100)
                    : 0;

                $sold  = stepstonks_get_sold_count($id);
                $stock = $product->get_stock_quantity() ?: rand(30, 200);
                $total = $sold + $stock;
                $pct   = $total > 0 ? min(100, round(($sold / $total) * 100)) : 50;
            ?>
                <article class="product-card" role="listitem">
                    <div class="product-card-img">
                        <a href="<?php echo esc_url($permalink); ?>" class="product-link" aria-label="<?php echo esc_attr($name); ?>">
                            <img
                                src="<?php echo esc_url($img_url); ?>"
                                alt="<?php echo esc_attr($name); ?>"
                                loading="lazy"
                                width="400" height="400"
                            >
                        </a>

                        <?php if ($product->is_on_sale()): ?>
                            <span class="product-badge badge-sale"><?php esc_html_e('SALE', 'stepstonks-temu'); ?></span>
                        <?php elseif ($product->is_featured()): ?>
                            <span class="product-badge badge-hot"><?php esc_html_e('HOT', 'stepstonks-temu'); ?></span>
                        <?php endif; ?>

                        <?php if ($discount_pct > 0): ?>
                            <div class="product-discount-badge" aria-label="<?php printf(esc_attr__('%d%% off', 'stepstonks-temu'), $discount_pct); ?>">
                                -<?php echo esc_html($discount_pct); ?>%
                            </div>
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
                                <div class="sold-bar-fill" style="width:<?php echo esc_attr($pct); ?>%" role="progressbar" aria-valuenow="<?php echo esc_attr($pct); ?>" aria-valuemin="0" aria-valuemax="100"></div>
                            </div>
                            <div class="sold-bar-label">
                                <span><?php echo esc_html(number_format($sold)); ?></span> <?php esc_html_e('sold', 'stepstonks-temu'); ?>
                            </div>
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
            <?php endforeach; ?>
        </div>
    </div>

</section>
