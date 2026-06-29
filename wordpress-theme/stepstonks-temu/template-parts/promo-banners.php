<?php
/**
 * Promo banners row template part
 */
?>

<div class="promo-banners" aria-label="<?php esc_attr_e('Promotions', 'stepstonks-temu'); ?>">

    <a href="<?php echo esc_url(add_query_arg('on_sale', '1', get_permalink(wc_get_page_id('shop')))); ?>" class="promo-banner b1">
        <div class="promo-banner-content">
            <h3>⚡ <?php esc_html_e('Clearance Sale', 'stepstonks-temu'); ?></h3>
            <p><?php esc_html_e('Up to 70% off selected items', 'stepstonks-temu'); ?></p>
            <span class="btn-sm"><?php esc_html_e('Shop Now', 'stepstonks-temu'); ?></span>
        </div>
    </a>

    <a href="<?php echo esc_url(add_query_arg('orderby', 'date', get_permalink(wc_get_page_id('shop')))); ?>" class="promo-banner b2">
        <div class="promo-banner-content">
            <h3>✨ <?php esc_html_e('New This Week', 'stepstonks-temu'); ?></h3>
            <p><?php esc_html_e('Hundreds of new products added', 'stepstonks-temu'); ?></p>
            <span class="btn-sm"><?php esc_html_e('Explore', 'stepstonks-temu'); ?></span>
        </div>
    </a>

    <a href="<?php echo esc_url(add_query_arg('orderby', 'popularity', get_permalink(wc_get_page_id('shop')))); ?>" class="promo-banner b3">
        <div class="promo-banner-content">
            <h3>🏆 <?php esc_html_e('Top Sellers', 'stepstonks-temu'); ?></h3>
            <p><?php esc_html_e('Most loved by our shoppers', 'stepstonks-temu'); ?></p>
            <span class="btn-sm"><?php esc_html_e('See All', 'stepstonks-temu'); ?></span>
        </div>
    </a>

</div>
