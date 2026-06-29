<?php
/**
 * Hero banner template part
 */
$headline = get_theme_mod('hero_headline', __('Up to 90% Off!', 'stepstonks-temu'));
?>

<section class="hero-section" aria-label="<?php esc_attr_e('Promotions', 'stepstonks-temu'); ?>">
    <div class="hero-grid">

        <!-- Main Hero Card -->
        <a href="<?php echo esc_url(get_permalink(wc_get_page_id('shop'))); ?>" class="hero-main">
            <div class="hero-main-content">
                <div class="hero-label">🔥 <?php esc_html_e('LIMITED TIME OFFER', 'stepstonks-temu'); ?></div>
                <h1 class="hero-title"><?php echo esc_html($headline); ?></h1>
                <p class="hero-subtitle"><?php esc_html_e('Thousands of products, unbeatable prices every day.', 'stepstonks-temu'); ?></p>
                <span class="hero-cta">
                    <?php esc_html_e('Shop Now', 'stepstonks-temu'); ?>
                    <svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2.5" aria-hidden="true">
                        <path d="M5 12h14m-7-7 7 7-7 7"/>
                    </svg>
                </span>
            </div>
        </a>

        <!-- Side Cards -->
        <div class="hero-side">
            <a href="<?php echo esc_url(add_query_arg('orderby', 'date', get_permalink(wc_get_page_id('shop')))); ?>" class="hero-side-card card-1">
                <div class="hero-side-card-content">
                    <h3>🆕 <?php esc_html_e('New Arrivals', 'stepstonks-temu'); ?></h3>
                    <p><?php esc_html_e('Fresh drops daily', 'stepstonks-temu'); ?></p>
                    <div class="badge-pill"><?php esc_html_e('Just Launched', 'stepstonks-temu'); ?></div>
                </div>
            </a>
            <a href="<?php echo esc_url(add_query_arg('orderby', 'popularity', get_permalink(wc_get_page_id('shop')))); ?>" class="hero-side-card card-2">
                <div class="hero-side-card-content">
                    <h3>🏆 <?php esc_html_e('Best Sellers', 'stepstonks-temu'); ?></h3>
                    <p><?php esc_html_e('What everyone\'s buying', 'stepstonks-temu'); ?></p>
                    <div class="badge-pill"><?php esc_html_e('Top Picks', 'stepstonks-temu'); ?></div>
                </div>
            </a>
        </div>

    </div>
</section>
