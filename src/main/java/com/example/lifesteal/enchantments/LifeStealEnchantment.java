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
        return EnchantmentTarget.WEAPON;
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
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
        return item.getType().name().contains("SWORD") || item.getType().name().contains("AXE");
    }

    public double getLifeStealPercentage(int level) {
        switch (level) {
            case 1: return 0.15;
            case 2: return 0.30;
            case 3: return 0.50;
            default: return 0;
        }
    }
    
    public String getDisplayName(int level) {
        return ChatColor.GRAY + "生命汲取 " + getRomanLevel(level);
    }
    
    private String getRomanLevel(int number) {
        switch (number) {
            case 1: return "I";
            case 2: return "II";
            case 3: return "III";
            default: return String.valueOf(number);
        }
    }
}
