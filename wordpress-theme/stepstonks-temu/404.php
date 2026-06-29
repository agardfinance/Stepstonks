<?php get_header(); ?>

<main id="main" class="wc-main">
    <div class="container" style="text-align:center;padding:80px 12px">
        <div style="font-size:5rem;margin-bottom:16px" aria-hidden="true">🔍</div>
        <h1 style="font-size:2rem;font-weight:900;color:var(--color-primary);margin-bottom:8px">
            <?php esc_html_e("Oops! Page not found", 'stepstonks-temu'); ?>
        </h1>
        <p style="color:var(--color-text-muted);margin-bottom:24px;font-size:1rem">
            <?php esc_html_e("The page you're looking for doesn't exist or has been moved.", 'stepstonks-temu'); ?>
        </p>
        <a href="<?php echo esc_url(home_url('/')); ?>" class="btn-cart primary" style="display:inline-flex;text-decoration:none;padding:14px 32px">
            <?php esc_html_e('Back to Home', 'stepstonks-temu'); ?>
        </a>
    </div>
</main>

<?php get_footer(); ?>
