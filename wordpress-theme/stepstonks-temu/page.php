<?php get_header(); ?>

<main id="main" class="wc-main">
    <div class="container" style="padding:24px 12px 60px;max-width:860px;margin:0 auto">
        <?php while (have_posts()): the_post(); ?>
            <article id="post-<?php the_ID(); ?>" <?php post_class('page-content'); ?>>
                <h1 style="font-size:1.8rem;font-weight:900;margin-bottom:20px;color:var(--color-primary)"><?php the_title(); ?></h1>
                <div class="page-body" style="line-height:1.7">
                    <?php the_content(); ?>
                </div>
            </article>
        <?php endwhile; ?>
    </div>
</main>

<?php get_footer(); ?>
