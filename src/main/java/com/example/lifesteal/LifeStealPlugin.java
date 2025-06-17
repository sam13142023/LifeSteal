package com.example.lifesteal;

import com.example.lifesteal.commands.LifeStealCommand;
import com.example.lifesteal.enchantments.LifeStealEnchantment;
import com.example.lifesteal.listeners.LifeStealListener;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.logging.Level;

public class LifeStealPlugin extends JavaPlugin {

    private static LifeStealPlugin instance;
    private LifeStealEnchantment lifeStealEnchantment;
    private NamespacedKey lifeStealKey;

    @Override
    public void onEnable() {
        instance = this;
        lifeStealKey = new NamespacedKey(this, "life_steal");

        registerEnchantment();

        if (lifeStealEnchantment != null) {
            getServer().getPluginManager().registerEvents(new LifeStealListener(), this);
            if (this.getCommand("lifesteal") != null) {
                this.getCommand("lifesteal").setExecutor(new LifeStealCommand());
            } else {
                getLogger().severe("Command 'lifesteal' not found in plugin.yml!");
            }
            getLogger().info("LifeSteal 插件已启用！");
        } else {
            getLogger().severe("LifeSteal 附魔未能注册，插件功能将受限！");
            // Optionally disable the plugin entirely if the enchantment is critical
            // getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        // Unregistering enchantments reflectively is highly problematic and prone to breaking.
        // It's generally safer to let them remain registered.
        // If you unregister listeners or commands, do that here.
        getLogger().info("LifeSteal 插件已禁用！");
    }

    private void registerEnchantment() {
        try {
            Enchantment existingEnchant = Enchantment.getByKey(lifeStealKey);
            if (existingEnchant != null) {
                if (existingEnchant instanceof LifeStealEnchantment) {
                    lifeStealEnchantment = (LifeStealEnchantment) existingEnchant;
                    getLogger().info("生命汲取附魔已注册过 (可能是插件重载)，将使用现有实例。");
                } else {
                    getLogger().severe("命名空间键 " + lifeStealKey + " 已被另一个非 LifeSteal 附魔占用！生命汲取附魔无法注册。");
                    lifeStealEnchantment = null;
                }
                return;
            }
            
            lifeStealEnchantment = new LifeStealEnchantment(lifeStealKey);

            // This reflection is a common workaround to allow custom enchantment registration.
            Field acceptingNewField = Enchantment.class.getDeclaredField("acceptingNew");
            acceptingNewField.setAccessible(true);
            
            // Store original value to restore it later (optional, but good practice)
            // boolean wasAcceptingNew = acceptingNewField.getBoolean(null);
            
            acceptingNewField.set(null, true);
            
            Enchantment.registerEnchantment(lifeStealEnchantment);
            getLogger().info("生命汲取附魔已成功注册！");

            // Optional: Restore the original value of acceptingNew
            // acceptingNewField.set(null, wasAcceptingNew);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            getLogger().log(Level.SEVERE, "无法修改附魔注册状态 (acceptingNew 反射失败)，附魔可能无法注册。", e);
            lifeStealEnchantment = null;
        } catch (IllegalArgumentException e) {
            // This can happen if an enchantment with this key is already registered by Bukkit/Spigot/Paper itself,
            // or if there's some other conflict during registration.
            // The initial check for Enchantment.getByKey should catch most re-registration attempts.
            getLogger().log(Level.SEVERE, "注册生命汲取附魔时发生参数错误 (可能已存在或冲突): " + e.getMessage(), e);
            lifeStealEnchantment = null;
        } catch (Exception e) { // Catch any other unexpected errors
            getLogger().log(Level.SEVERE, "注册附魔时发生未知错误。", e);
            lifeStealEnchantment = null;
        }
    }

    public static LifeStealPlugin getInstance() {
        return instance;
    }

    public LifeStealEnchantment getLifeStealEnchantment() {
        return lifeStealEnchantment;
    }
}
