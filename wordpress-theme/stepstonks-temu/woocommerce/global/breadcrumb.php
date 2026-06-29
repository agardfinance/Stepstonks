<?php
/**
 * Breadcrumb – styled for Temu theme
 */
defined('ABSPATH') || exit;

if (empty($breadcrumb)) return;
?>
<nav class="woocommerce-breadcrumb" aria-label="<?php esc_attr_e('Breadcrumb', 'stepstonks-temu'); ?>">
    <?php foreach ($breadcrumb as $key => $crumb):
        $is_last = ($key === array_key_last($breadcrumb));
    ?>
        <?php if (!empty($crumb[1]) && !$is_last): ?>
            <a href="<?php echo esc_url($crumb[1]); ?>"><?php echo esc_html($crumb[0]); ?></a>
        <?php else: ?>
            <span aria-current="page"><?php echo esc_html($crumb[0]); ?></span>
        <?php endif; ?>
        <?php if (!$is_last): ?>
            <span aria-hidden="true" style="margin:0 5px;color:var(--color-border)">›</span>
        <?php endif; ?>
    <?php endforeach; ?>
</nav>
