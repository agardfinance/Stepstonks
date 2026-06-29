<?php get_header(); ?>

<main id="main" class="wc-main">
    <div class="container" style="max-width:860px;margin:0 auto;padding:24px 12px 60px">
        <?php while (have_posts()): the_post(); ?>
            <article id="post-<?php the_ID(); ?>" <?php post_class(); ?>>
                <h1 style="font-size:1.8rem;font-weight:900;margin-bottom:12px"><?php the_title(); ?></h1>
                <div style="font-size:.8rem;color:var(--color-text-muted);margin-bottom:20px">
                    <?php the_date(); ?> &middot; <?php the_author(); ?>
                </div>
                <?php if (has_post_thumbnail()): ?>
                    <div style="border-radius:var(--radius-lg);overflow:hidden;margin-bottom:20px">
                        <?php the_post_thumbnail('large', ['style' => 'width:100%;height:auto']); ?>
                    </div>
                <?php endif; ?>
                <div class="entry-content" style="line-height:1.8;font-size:.95rem">
                    <?php the_content(); ?>
                </div>
            </article>
            <?php comments_template(); ?>
        <?php endwhile; ?>
    </div>
</main>

<?php get_footer(); ?>
