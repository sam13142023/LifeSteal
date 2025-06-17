package com.example.lifesteal.listeners;

import com.example.lifesteal.LifeStealPlugin;
import com.example.lifesteal.enchantments.LifeStealEnchantment;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class LifeStealListener implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        // 检查攻击者是否为玩家
        if (!(event.getDamager() instanceof Player)) {
            return;
        }
        
        Player player = (Player) event.getDamager();
        
        // 检查玩家是否在生存模式
        if (player.getGameMode() != GameMode.SURVIVAL && player.getGameMode() != GameMode.ADVENTURE) {
            return;
        }
        
        // 获取玩家手中的物品
        ItemStack item = player.getInventory().getItemInMainHand();
        
        // 获取附魔实例
        LifeStealEnchantment enchantment = LifeStealPlugin.getInstance().getLifeStealEnchantment();
        
        // 检查物品是否有生命汲取附魔
        if (!item.hasItemMeta() || !item.getItemMeta().hasEnchant(enchantment)) {
            return;
        }
        
        // 获取附魔等级
        int level = item.getItemMeta().getEnchantLevel(enchantment);
        
        // 获取造成的伤害
        double damage = event.getFinalDamage();
        
        // 计算恢复的生命值
        double healAmount = damage * enchantment.getLifeStealPercentage(level);
        
        // 应用生命回复
        double maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        double newHealth = Math.min(player.getHealth() + healAmount, maxHealth);
        player.setHealth(newHealth);
        
        // 播放吸血效果音效
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5f, 1.5f);
        
        // 如果受害者是生物实体，可以添加粒子效果
        if (event.getEntity() instanceof LivingEntity) {
            LivingEntity victim = (LivingEntity) event.getEntity();
            // 给受害者添加短暂的虚弱效果，增强视觉反馈
            victim.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20, 0, false, true));
        }
    }
}
