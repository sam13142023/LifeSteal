package com.example.lifesteal.enchantments;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
// Removed: import org.bukkit.inventory.meta.ItemMeta; // Not used directly here

public class LifeStealEnchantment extends Enchantment {

    public LifeStealEnchantment(NamespacedKey key) {
        super(key); // This is the line that was causing NoSuchMethodError if API mismatches
    }

    @Override
    public String getName() {
        // This is more of a display name. The NamespacedKey is the true unique identifier.
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
        // Set to true if you want it to be a treasure enchantment (e.g., only from loot/villagers)
        return false;
    }

    @Override
    public boolean isCursed() {
        // Set to true if it's a cursed enchantment
        return false;
    }

    @Override
    public boolean conflictsWith(Enchantment other) {
        // Define any enchantments this should conflict with
        // For example, if it shouldn't go with Sharpness:
        // if (other.equals(Enchantment.DAMAGE_ALL)) {
        //     return true;
        // }
        return false; // No conflicts by default
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
        if (item == null) return false;
        String typeName = item.getType().name();
        // More robust check for swords and axes, works with different material types (WOOD_SWORD, DIAMOND_AXE etc.)
        return typeName.endsWith("_SWORD") || typeName.endsWith("_AXE");
    }

    // Custom method, not an override
    public double getLifeStealPercentage(int level) {
        switch (level) {
            case 1: return 0.15; // 15%
            case 2: return 0.30; // 30%
            case 3: return 0.50; // 50%
            default: return 0.0; // Return a double
        }
    }
    
    // Custom method for display name with level, not an override
    public String getDisplayName(int level) {
        return ChatColor.GRAY + this.getName() + " " + getRomanLevel(level);
    }
    
    private String getRomanLevel(int number) {
        switch (number) {
            case 1: return "I";
            case 2: return "II";
            case 3: return "III";
            default: return String.valueOf(number);
        }
    }

    // Note: getKey() is inherited from Enchantment and will return the NamespacedKey passed to the constructor.
    // No need to override it unless you have a specific reason.
}
