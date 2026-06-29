<?php
/**
 * Index – fallback for all templates not explicitly defined
 */
get_header();
?>

<main id="main" class="wc-main">
    <div class="container" style="padding-top:20px;padding-bottom:40px">
        <?php if (have_posts()): ?>
            <div class="products-grid">
                <?php while (have_posts()): the_post(); ?>
                    <article id="post-<?php the_ID(); ?>" <?php post_class('product-card'); ?>>
                        <?php if (has_post_thumbnail()): ?>
                            <div class="product-card-img">
                                <a href="<?php the_permalink(); ?>" class="product-link">
                                    <?php the_post_thumbnail('stepstonks-product-card', ['loading' => 'lazy']); ?>
                                </a>
                            </div>
                        <?php endif; ?>
                        <div class="product-card-body">
                            <h2 class="product-card-name"><a href="<?php the_permalink(); ?>"><?php the_title(); ?></a></h2>
                            <div class="product-price-row" style="margin-top:4px;font-size:.78rem;color:var(--color-text-muted)">
                                <?php echo get_the_date(); ?>
                            </div>
                        </div>
                    </article>
                <?php endwhile; ?>
            </div>
            <?php the_posts_pagination(['prev_text' => '←', 'next_text' => '→']); ?>
        <?php else: ?>
            <p style="padding:60px;text-align:center;color:var(--color-text-muted)">
                <?php esc_html_e('Nothing found here yet.', 'stepstonks-temu'); ?>
            </p>
        <?php endif; ?>
    </div>
</main>

<?php get_footer(); ?>
