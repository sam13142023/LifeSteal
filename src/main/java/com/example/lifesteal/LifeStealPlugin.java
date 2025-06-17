package com.example.lifesteal;

import com.example.lifesteal.commands.LifeStealCommand;
import com.example.lifesteal.enchantments.LifeStealEnchantment;
import com.example.lifesteal.listeners.LifeStealListener;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.logging.Level;

public class LifeStealPlugin extends JavaPlugin {

    private static LifeStealPlugin instance;
    private LifeStealEnchantment lifeStealEnchantment;
    private NamespacedKey lifeStealKey;

    @Override
    public void onEnable() {
        instance = this;

        // 创建命名空间键
        lifeStealKey = new NamespacedKey(this, "life_steal");

        // 注册自定义附魔
        registerEnchantment();

        // 注册事件监听器
        getServer().getPluginManager().registerEvents(new LifeStealListener(), this);
        
        // 注册命令
        this.getCommand("lifesteal").setExecutor(new LifeStealCommand());
        
        getLogger().info("LifeSteal 插件已启用！");
    }

    @Override
    public void onDisable() {
        // 取消注册附魔
        try {
            unregisterEnchantment();
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "取消注册附魔时发生错误", e);
        }
        
        getLogger().info("LifeSteal 插件已禁用！");
    }

    private void registerEnchantment() {
        try {
            // 检查是否已经注册
            if (Enchantment.getByKey(lifeStealKey) != null) {
                getLogger().warning("生命汲取附魔已注册过，跳过注册。");
                lifeStealEnchantment = (LifeStealEnchantment) Enchantment.getByKey(lifeStealKey);
                return;
            }
            
            // 创建附魔实例
            lifeStealEnchantment = new LifeStealEnchantment(lifeStealKey);
            
            // 使用反射绕过Bukkit的限制来注册自定义附魔
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
            
            // 注册附魔
            Enchantment.registerEnchantment(lifeStealEnchantment);
            
            getLogger().info("生命汲取附魔已成功注册！");
            
        } catch (IllegalArgumentException e) {
            getLogger().warning("生命汲取附魔已注册过，跳过注册。");
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "注册附魔时发生错误", e);
        }
    }
    
    private void unregisterEnchantment() throws Exception {
        try {
            Field keyField = Enchantment.class.getDeclaredField("byKey");
            keyField.setAccessible(true);
            @SuppressWarnings("unchecked")
            HashMap<NamespacedKey, Enchantment> byKey = (HashMap<NamespacedKey, Enchantment>) keyField.get(null);
            
            if (byKey.containsKey(lifeStealKey)) {
                byKey.remove(lifeStealKey);
            }
            
            Field nameField = Enchantment.class.getDeclaredField("byName");
            nameField.setAccessible(true);
            @SuppressWarnings("unchecked")
            HashMap<String, Enchantment> byName = (HashMap<String, Enchantment>) nameField.get(null);
            
            if (byName.containsKey("生命汲取")) {
                byName.remove("生命汲取");
            }
            
        } catch (Exception e) {
            throw e;
        }
    }

    public static LifeStealPlugin getInstance() {
        return instance;
    }

    public LifeStealEnchantment getLifeStealEnchantment() {
        return lifeStealEnchantment;
    }
}
