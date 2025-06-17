package com.example.lifesteal.enchantments;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

public class LifeStealEnchantment extends Enchantment {

    public LifeStealEnchantment(NamespacedKey key) {
        super(key);
    }
    
    @Override
    public String getName() {
        return "生命汲取";
    }
    
    @Override
    public int getMaxLevel() {
        return 3;
    }
    
    @Override
    public int getStartLevel() {
        return 1;
    }
    
    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.WEAPON;  // 只应用于武器
    }
    
    @Override
    public boolean isTreasure() {
        return false;
    }
    
    @Override
    public boolean isCursed() {
        return false;
    }
    
    @Override
    public boolean conflictsWith(Enchantment other) {
        return false;  // 与其他附魔不冲突
    }
    
    @Override
    public boolean canEnchantItem(ItemStack item) {
        return item.getType().name().contains("SWORD") ||
               item.getType().name().contains("AXE");  // 可以附魔剑和斧
    }
    
    /**
     * 根据附魔等级获取生命汲取比例
     * @param level 附魔等级
     * @return 生命汲取比例（0-1之间的小数）
     */
    public double getLifeStealPercentage(int level) {
        switch (level) {
            case 1:
                return 0.15;  // 15%
            case 2:
                return 0.30;  // 30%
            case 3:
                return 0.50;  // 50%
            default:
                return 0;
        }
    }
    
    /**
     * 获取用于显示的附魔名称
     * @param level 附魔等级
     * @return 格式化的附魔名称
     */
    public String getDisplayName(int level) {
        return ChatColor.GRAY + "生命汲取 " + getRomanLevel(level);
    }
    
    /**
     * 将数字转换为罗马数字
     * @param number 要转换的数字
     * @return 罗马数字形式
     */
    private String getRomanLevel(int number) {
        switch (number) {
            case 1:
                return "I";
            case 2:
                return "II";
            case 3:
                return "III";
            default:
                return String.valueOf(number);
        }
    }
}
