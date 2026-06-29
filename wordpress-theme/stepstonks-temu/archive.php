<?php get_header(); ?>

<main id="main" class="wc-main">
    <div class="container" style="padding-top:16px;padding-bottom:40px">
        <?php if (have_posts()): ?>
            <h1 class="section-title" style="margin-bottom:16px"><?php the_archive_title(); ?></h1>
            <div class="products-grid">
                <?php while (have_posts()): the_post(); ?>
                    <?php get_template_part('template-parts/content', get_post_type()); ?>
                <?php endwhile; ?>
            </div>
            <?php the_posts_pagination(); ?>
        <?php else: ?>
            <p><?php esc_html_e('No posts found.', 'stepstonks-temu'); ?></p>
        <?php endif; ?>
    </div>
</main>

<?php get_footer(); ?>
