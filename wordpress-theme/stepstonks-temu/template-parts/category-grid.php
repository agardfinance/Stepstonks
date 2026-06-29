<?php
/**
 * Category grid template part
 */
$categories = get_terms([
    'taxonomy'   => 'product_cat',
    'orderby'    => 'count',
    'order'      => 'DESC',
    'number'     => 10,
    'hide_empty' => true,
]);

if (is_wp_error($categories) || empty($categories)) return;

$icons = ['👗', '📱', '💻', '🏠', '🎮', '🏋️', '💄', '🎒', '🍳', '🌱', '🧸', '👟'];
?>

<section class="category-grid-section" aria-label="<?php esc_attr_e('Shop by Category', 'stepstonks-temu'); ?>">
    <div class="section-header">
        <div class="section-title-wrap">
            <div class="section-icon" aria-hidden="true">🗂️</div>
            <div>
                <div class="section-title"><?php esc_html_e('Shop by Category', 'stepstonks-temu'); ?></div>
                <div class="section-subtitle"><?php esc_html_e('Browse all departments', 'stepstonks-temu'); ?></div>
            </div>
        </div>
        <a href="<?php echo esc_url(get_permalink(wc_get_page_id('shop'))); ?>" class="section-see-all">
            <?php esc_html_e('All Categories', 'stepstonks-temu'); ?> →
        </a>
    </div>

    <div class="category-grid" role="list">
        <?php foreach ($categories as $i => $cat):
            $icon     = $icons[$i] ?? '🛍️';
            $link     = get_term_link($cat);
            $thumb_id = get_woocommerce_term_meta($cat->term_id, 'thumbnail_id', true);
        ?>
            <a href="<?php echo esc_url($link); ?>" class="category-grid-item" role="listitem">
                <div class="category-grid-icon" aria-hidden="true">
                    <?php if ($thumb_id): ?>
                        <?php echo wp_get_attachment_image($thumb_id, [48, 48]); ?>
                    <?php else: ?>
                        <?php echo $icon; ?>
                    <?php endif; ?>
                </div>
                <span class="category-grid-name"><?php echo esc_html($cat->name); ?></span>
                <span class="category-grid-count"><?php printf(_n('%d item', '%d items', $cat->count, 'stepstonks-temu'), $cat->count); ?></span>
            </a>
        <?php endforeach; ?>
    </div>
</section>
