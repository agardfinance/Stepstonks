<?php
defined('ABSPATH') || exit;
if (!$notices) return;
?>
<ul class="woocommerce-message" role="status" style="background:#f0fff4;border-left:4px solid var(--color-success);border-radius:var(--radius-md);padding:12px 16px;margin-bottom:16px">
    <?php foreach ($notices as $notice): ?>
        <li <?php echo wc_get_notice_data_attr($notice); ?>>
            <?php echo wp_kses_post($notice['notice']); ?>
        </li>
    <?php endforeach; ?>
</ul>
