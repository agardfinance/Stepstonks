<?php
/**
 * WooCommerce single-product.php override
 */
defined('ABSPATH') || exit;

get_header('shop');

while (have_posts()):
    the_post();
    global $product;

    $id           = $product->get_id();
    $name         = $product->get_name();
    $price        = (float) $product->get_price();
    $reg_price    = (float) $product->get_regular_price();
    $sale_price   = (float) $product->get_sale_price();
    $description  = $product->get_description();
    $short_desc   = $product->get_short_description();
    $avg_rating   = $product->get_average_rating();
    $review_count = $product->get_review_count();
    $sold         = stepstonks_get_sold_count($id);
    $in_stock     = $product->is_in_stock();
    $stock_qty    = $product->get_stock_quantity();
    $sku          = $product->get_sku();

    $discount_pct = ($reg_price && $sale_price && $reg_price > 0)
        ? round((($reg_price - $sale_price) / $reg_price) * 100)
        : 0;

    // Gallery images
    $gallery_ids = $product->get_gallery_image_ids();
    $main_img_id = $product->get_image_id();
    $main_img    = $main_img_id ? wp_get_attachment_image_url($main_img_id, 'stepstonks-product-wide') : wc_placeholder_img_src();

    // Related products
    $related_ids = wc_get_related_products($id, 8);

    // Star markup
    $stars = '';
    for ($i = 1; $i <= 5; $i++) {
        $stars .= $i <= round($avg_rating) ? '★' : '☆';
    }
?>

<main id="main" class="wc-main" style="background:var(--color-bg);padding-bottom:80px">

    <!-- Breadcrumb -->
    <div class="container" style="padding-top:12px;font-size:.8rem;color:var(--color-text-muted)">
        <?php woocommerce_breadcrumb(); ?>
    </div>

    <!-- Product Layout -->
    <div class="single-product-layout">

        <!-- Gallery -->
        <div class="product-gallery-wrap">
            <div class="product-gallery-main">
                <img id="main-product-img" src="<?php echo esc_url($main_img); ?>" alt="<?php echo esc_attr($name); ?>" loading="eager">
            </div>

            <?php if (!empty($gallery_ids) || $main_img_id): ?>
                <div class="product-gallery-thumbs" role="list" aria-label="<?php esc_attr_e('Product images', 'stepstonks-temu'); ?>">
                    <?php
                    $all_thumbs = $main_img_id ? array_merge([$main_img_id], $gallery_ids) : $gallery_ids;
                    foreach ($all_thumbs as $i => $thumb_id):
                        $thumb_url = wp_get_attachment_image_url($thumb_id, 'gallery_thumbnail');
                    ?>
                        <div class="gallery-thumb <?php echo $i === 0 ? 'active' : ''; ?>" role="listitem">
                            <img src="<?php echo esc_url($thumb_url); ?>" alt="" loading="lazy">
                        </div>
                    <?php endforeach; ?>
                </div>
            <?php endif; ?>
        </div>

        <!-- Product Info -->
        <div class="product-info-wrap">

            <!-- Main Info Card -->
            <div class="product-info-card">

                <?php if ($product->is_on_sale()): ?>
                    <div style="margin-bottom:8px">
                        <span class="product-badge badge-sale" style="position:static;display:inline-block"><?php esc_html_e('SALE', 'stepstonks-temu'); ?></span>
                    </div>
                <?php endif; ?>

                <h1 class="product-title"><?php echo esc_html($name); ?></h1>

                <!-- Rating Row -->
                <div class="product-rating-row">
                    <span class="stars" aria-label="<?php printf(esc_attr__('Rated %s out of 5', 'stepstonks-temu'), number_format($avg_rating, 1)); ?>">
                        <?php echo $stars; ?>
                    </span>
                    <span class="rating-count"><?php echo number_format($avg_rating, 1); ?> (<?php echo esc_html(number_format($review_count)); ?> <?php esc_html_e('reviews', 'stepstonks-temu'); ?>)</span>
                    <span class="sold-count"><?php echo esc_html(number_format($sold)); ?> <?php esc_html_e('sold', 'stepstonks-temu'); ?></span>
                </div>

                <!-- Price -->
                <div class="product-price-section">
                    <div class="product-current-price"><?php echo wp_kses_post(wc_price($price)); ?></div>
                    <div class="product-price-meta">
                        <?php if ($reg_price && $reg_price != $price): ?>
                            <span class="product-original-price"><?php echo wp_kses_post(wc_price($reg_price)); ?></span>
                        <?php endif; ?>
                        <?php if ($discount_pct > 0): ?>
                            <span class="product-discount-pct"><?php echo esc_html($discount_pct); ?>% <?php esc_html_e('OFF', 'stepstonks-temu'); ?></span>
                        <?php endif; ?>
                    </div>
                    <div class="product-shipping-note">
                        ✓ <?php esc_html_e('Free shipping on orders over $25', 'stepstonks-temu'); ?>
                    </div>
                </div>

                <!-- Short Description -->
                <?php if ($short_desc): ?>
                    <div style="font-size:.85rem;color:var(--color-text-muted);line-height:1.6;margin-bottom:14px">
                        <?php echo wp_kses_post($short_desc); ?>
                    </div>
                <?php endif; ?>

                <!-- Variations -->
                <?php if ($product->is_type('variable')):
                    $attributes = $product->get_variation_attributes();
                    foreach ($attributes as $attr_name => $options):
                        $label = wc_attribute_label($attr_name);
                ?>
                    <div class="variant-section">
                        <div class="variant-label"><?php echo esc_html($label); ?>: <span><?php echo esc_html($options[0] ?? ''); ?></span></div>
                        <div class="variant-options" data-attribute="<?php echo esc_attr($attr_name); ?>">
                            <?php if (strtolower($label) === 'color'): ?>
                                <?php foreach ($options as $opt): ?>
                                    <button class="color-btn" style="background:<?php echo esc_attr(strtolower($opt)); ?>" title="<?php echo esc_attr($opt); ?>"></button>
                                <?php endforeach; ?>
                            <?php else: ?>
                                <?php foreach ($options as $i => $opt): ?>
                                    <button class="variant-btn <?php echo $i === 0 ? 'selected' : ''; ?>">
                                        <?php echo esc_html($opt); ?>
                                    </button>
                                <?php endforeach; ?>
                            <?php endif; ?>
                        </div>
                    </div>
                <?php endforeach; endif; ?>

                <!-- Quantity -->
                <div class="qty-section">
                    <span class="qty-label"><?php esc_html_e('Quantity', 'stepstonks-temu'); ?></span>
                    <div class="qty-control">
                        <button class="qty-btn qty-minus" aria-label="<?php esc_attr_e('Decrease quantity', 'stepstonks-temu'); ?>">−</button>
                        <input class="qty-input" type="number" min="1" max="<?php echo esc_attr($stock_qty ?: 99); ?>" value="1" aria-label="<?php esc_attr_e('Quantity', 'stepstonks-temu'); ?>">
                        <button class="qty-btn qty-plus" aria-label="<?php esc_attr_e('Increase quantity', 'stepstonks-temu'); ?>">+</button>
                    </div>
                    <?php if ($stock_qty): ?>
                        <span style="font-size:.78rem;color:var(--color-text-muted)">
                            <?php printf(esc_html__('%d in stock', 'stepstonks-temu'), $stock_qty); ?>
                        </span>
                    <?php endif; ?>
                </div>

                <!-- Add to Cart -->
                <?php if ($in_stock): ?>
                    <div class="add-to-cart-area">
                        <button class="btn-cart primary" data-product-id="<?php echo esc_attr($id); ?>">
                            <svg viewBox="0 0 24 24" width="20" height="20" fill="none" stroke="currentColor" stroke-width="2" aria-hidden="true">
                                <circle cx="9" cy="21" r="1"/><circle cx="20" cy="21" r="1"/>
                                <path d="M1 1h4l2.68 13.39a2 2 0 0 0 2 1.61h9.72a2 2 0 0 0 2-1.61L23 6H6"/>
                            </svg>
                            <?php esc_html_e('Add to Cart', 'stepstonks-temu'); ?>
                        </button>
                        <a href="<?php echo esc_url(wc_get_checkout_url()); ?>" class="btn-cart secondary">
                            ⚡ <?php esc_html_e('Buy Now', 'stepstonks-temu'); ?>
                        </a>
                        <button class="btn-cart outline product-wishlist-btn" style="position:static;width:auto;height:auto;border-radius:28px;padding:14px">
                            <svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2" aria-hidden="true">
                                <path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z"/>
                            </svg>
                            <?php esc_html_e('Save to Wishlist', 'stepstonks-temu'); ?>
                        </button>
                    </div>
                <?php else: ?>
                    <div style="padding:14px;background:#fff3f3;border-radius:var(--radius-md);text-align:center;color:var(--color-danger);font-weight:700">
                        <?php esc_html_e('Out of Stock', 'stepstonks-temu'); ?>
                    </div>
                <?php endif; ?>

                <!-- Meta -->
                <div style="margin-top:14px;font-size:.78rem;color:var(--color-text-muted)">
                    <?php if ($sku): ?>
                        <div style="margin-bottom:3px"><?php esc_html_e('SKU:', 'stepstonks-temu'); ?> <?php echo esc_html($sku); ?></div>
                    <?php endif; ?>
                    <?php echo wc_get_product_category_list($id, ', ', '<div>' . esc_html__('Category:', 'stepstonks-temu') . ' ', '</div>'); ?>
                </div>

            </div><!-- .product-info-card -->

            <!-- Delivery Info Card -->
            <div class="product-info-card" style="padding:16px 20px">
                <div style="display:flex;flex-direction:column;gap:10px">
                    <div style="display:flex;align-items:center;gap:10px;font-size:.85rem">
                        <span style="font-size:1.2rem" aria-hidden="true">🚚</span>
                        <div>
                            <strong><?php esc_html_e('Free Shipping', 'stepstonks-temu'); ?></strong><br>
                            <span style="color:var(--color-text-muted)"><?php esc_html_e('Estimated 7–15 business days', 'stepstonks-temu'); ?></span>
                        </div>
                    </div>
                    <div style="display:flex;align-items:center;gap:10px;font-size:.85rem">
                        <span style="font-size:1.2rem" aria-hidden="true">↩️</span>
                        <div>
                            <strong><?php esc_html_e('Free Returns', 'stepstonks-temu'); ?></strong><br>
                            <span style="color:var(--color-text-muted)"><?php esc_html_e('30-day hassle-free return policy', 'stepstonks-temu'); ?></span>
                        </div>
                    </div>
                    <div style="display:flex;align-items:center;gap:10px;font-size:.85rem">
                        <span style="font-size:1.2rem" aria-hidden="true">🔒</span>
                        <div>
                            <strong><?php esc_html_e('Secure Checkout', 'stepstonks-temu'); ?></strong><br>
                            <span style="color:var(--color-text-muted)"><?php esc_html_e('Encrypted & buyer-protected payment', 'stepstonks-temu'); ?></span>
                        </div>
                    </div>
                </div>
            </div>

        </div><!-- .product-info-wrap -->

    </div><!-- .single-product-layout -->

    <!-- Product Detail Tabs -->
    <div class="container">
        <div class="product-tabs">
            <div class="tab-list" role="tablist">
                <button class="tab-btn active" role="tab" data-tab="tab-desc" aria-selected="true">
                    <?php esc_html_e('Description', 'stepstonks-temu'); ?>
                </button>
                <button class="tab-btn" role="tab" data-tab="tab-specs" aria-selected="false">
                    <?php esc_html_e('Details', 'stepstonks-temu'); ?>
                </button>
                <button class="tab-btn" role="tab" data-tab="tab-reviews" aria-selected="false">
                    <?php printf(esc_html__('Reviews (%d)', 'stepstonks-temu'), $review_count); ?>
                </button>
                <button class="tab-btn" role="tab" data-tab="tab-shipping" aria-selected="false">
                    <?php esc_html_e('Shipping', 'stepstonks-temu'); ?>
                </button>
            </div>

            <div id="tab-desc" class="tab-panel active" role="tabpanel">
                <?php if ($description): ?>
                    <div style="line-height:1.7;font-size:.9rem"><?php echo wp_kses_post($description); ?></div>
                <?php else: ?>
                    <p style="color:var(--color-text-muted)"><?php esc_html_e('No description available.', 'stepstonks-temu'); ?></p>
                <?php endif; ?>
            </div>

            <div id="tab-specs" class="tab-panel" role="tabpanel">
                <?php
                $attributes = $product->get_attributes();
                if ($attributes):
                ?>
                    <table style="width:100%;border-collapse:collapse;font-size:.88rem">
                        <?php foreach ($attributes as $attribute):
                            if (!$attribute->get_visible()) continue;
                        ?>
                            <tr style="border-bottom:1px solid var(--color-border)">
                                <th style="padding:10px 14px;text-align:left;font-weight:700;width:35%;color:var(--color-text-muted)">
                                    <?php echo esc_html(wc_attribute_label($attribute->get_name())); ?>
                                </th>
                                <td style="padding:10px 14px">
                                    <?php echo wp_kses_post(wc_get_product_attribute_table($attribute, $product)); ?>
                                </td>
                            </tr>
                        <?php endforeach; ?>
                    </table>
                <?php else: ?>
                    <p style="color:var(--color-text-muted)"><?php esc_html_e('No specifications listed.', 'stepstonks-temu'); ?></p>
                <?php endif; ?>
            </div>

            <div id="tab-reviews" class="tab-panel" role="tabpanel">
                <?php comments_template(); ?>
            </div>

            <div id="tab-shipping" class="tab-panel" role="tabpanel">
                <div style="font-size:.9rem;line-height:1.7">
                    <h3 style="font-weight:700;margin-bottom:10px"><?php esc_html_e('Shipping Information', 'stepstonks-temu'); ?></h3>
                    <ul style="list-style:disc;padding-left:18px;color:var(--color-text-muted)">
                        <li><?php esc_html_e('Free standard shipping on orders over $25', 'stepstonks-temu'); ?></li>
                        <li><?php esc_html_e('Estimated delivery: 7–15 business days', 'stepstonks-temu'); ?></li>
                        <li><?php esc_html_e('Express shipping available at checkout', 'stepstonks-temu'); ?></li>
                        <li><?php esc_html_e('Order tracking provided via email', 'stepstonks-temu'); ?></li>
                    </ul>
                    <h3 style="font-weight:700;margin:14px 0 10px"><?php esc_html_e('Returns', 'stepstonks-temu'); ?></h3>
                    <ul style="list-style:disc;padding-left:18px;color:var(--color-text-muted)">
                        <li><?php esc_html_e('30-day return window', 'stepstonks-temu'); ?></li>
                        <li><?php esc_html_e('Item must be unused and in original packaging', 'stepstonks-temu'); ?></li>
                        <li><?php esc_html_e('Refund processed within 5 business days', 'stepstonks-temu'); ?></li>
                    </ul>
                </div>
            </div>
        </div><!-- .product-tabs -->

        <!-- Related Products -->
        <?php if (!empty($related_ids)):
            $related_products = array_map('wc_get_product', $related_ids);
            $related_products = array_filter($related_products);
        ?>
        <section style="margin-top:20px" aria-label="<?php esc_attr_e('You May Also Like', 'stepstonks-temu'); ?>">
            <div class="flash-deals-section">
                <div class="section-header">
                    <div class="section-title-wrap">
                        <div class="section-icon" aria-hidden="true">👀</div>
                        <div class="section-title"><?php esc_html_e('You May Also Like', 'stepstonks-temu'); ?></div>
                    </div>
                </div>
                <div class="products-scroll">
                    <div class="products-scroll-inner">
                        <?php foreach ($related_products as $rel):
                            $rid        = $rel->get_id();
                            $rname      = $rel->get_name();
                            $rprice     = $rel->get_price();
                            $rreg       = $rel->get_regular_price();
                            $rsale      = $rel->get_sale_price();
                            $rimgid     = $rel->get_image_id();
                            $rimg       = $rimgid ? wp_get_attachment_image_url($rimgid, 'stepstonks-product-card') : wc_placeholder_img_src();
                            $rdiscount  = ($rreg && $rsale && $rreg > 0) ? round((($rreg - $rsale) / $rreg) * 100) : 0;
                            $rsold      = stepstonks_get_sold_count($rid);
                        ?>
                            <article class="product-card">
                                <div class="product-card-img">
                                    <a href="<?php echo esc_url(get_permalink($rid)); ?>" class="product-link" aria-label="<?php echo esc_attr($rname); ?>">
                                        <img src="<?php echo esc_url($rimg); ?>" alt="<?php echo esc_attr($rname); ?>" loading="lazy" width="400" height="400">
                                    </a>
                                    <?php if ($rdiscount > 0): ?>
                                        <div class="product-discount-badge">-<?php echo esc_html($rdiscount); ?>%</div>
                                    <?php endif; ?>
                                </div>
                                <div class="product-card-body">
                                    <h3 class="product-card-name"><?php echo esc_html($rname); ?></h3>
                                    <div class="product-price-row">
                                        <span class="product-price"><?php echo wp_kses_post(wc_price($rprice)); ?></span>
                                        <?php if ($rreg && $rreg != $rprice): ?>
                                            <span class="product-price-original"><?php echo wp_kses_post(wc_price($rreg)); ?></span>
                                        <?php endif; ?>
                                    </div>
                                    <div class="sold-bar-label" style="margin-top:4px"><span><?php echo esc_html(number_format($rsold)); ?></span> <?php esc_html_e('sold', 'stepstonks-temu'); ?></div>
                                </div>
                            </article>
                        <?php endforeach; ?>
                    </div>
                </div>
            </div>
        </section>
        <?php endif; ?>

    </div><!-- .container -->

</main>

<!-- Sticky ATC (mobile) -->
<?php if (get_theme_mod('sticky_atc', true) && $in_stock): ?>
<div class="sticky-atc" aria-label="<?php esc_attr_e('Quick add to cart', 'stepstonks-temu'); ?>">
    <div>
        <div class="sticky-atc-price"><?php echo wp_kses_post(wc_price($price)); ?></div>
        <?php if ($discount_pct > 0): ?>
            <div style="font-size:.7rem;color:var(--color-text-muted);text-decoration:line-through"><?php echo wp_kses_post(wc_price($reg_price)); ?></div>
        <?php endif; ?>
    </div>
    <button class="sticky-atc-btn primary" data-product-id="<?php echo esc_attr($id); ?>">
        <?php esc_html_e('Add to Cart', 'stepstonks-temu'); ?>
    </button>
    <a href="<?php echo esc_url(wc_get_checkout_url()); ?>" class="sticky-atc-btn secondary">
        <?php esc_html_e('Buy Now', 'stepstonks-temu'); ?>
    </a>
</div>
<?php endif; ?>

<?php endwhile; ?>

<?php get_footer('shop'); ?>
