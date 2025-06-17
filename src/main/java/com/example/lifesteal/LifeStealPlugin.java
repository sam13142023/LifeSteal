package com.example.lifesteal;

import com.example.lifesteal.commands.LifeStealCommand; // Add this line
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
    
    @Override
    public void onEnable() {
        instance = this;
        
        // 注册附魔
        registerEnchantment();
        
        // 注册事件监听器
        getServer().getPluginManager().registerEvents(new LifeStealListener(), this);
        // 注册命令
        this.getCommand("lifesteal").setExecutor(new LifeStealCommand());
        getLogger().info("LifeSteal 插件已启用！");
    }
    
    @Override
    public void onDisable() {
        try {
            // 卸载附魔
            unregisterEnchantment();
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "卸载附魔时发生错误", e);
        }
        
        getLogger().info("LifeSteal 插件已禁用！");
    }
    
    private void registerEnchantment() {
        // 创建附魔键
        NamespacedKey key = new NamespacedKey(this, "life_steal");
        
        // 创建附魔实例
        lifeStealEnchantment = new LifeStealEnchantment(key);
        
        try {
            // 使用反射注册附魔
            Field field = Enchantment.class.getDeclaredField("acceptingNew");
            field.setAccessible(true);
            field.set(null, true);
            
            // 注册附魔
            Enchantment.registerEnchantment(lifeStealEnchantment);
            
            getLogger().info("生命汲取附魔已成功注册！");
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "注册附魔时发生错误", e);
        }
    }
    
    private void unregisterEnchantment() throws Exception {
        // 获取 Enchantment 类中的 byKey 和 byName 字段
        Field byKeyField = Enchantment.class.getDeclaredField("byKey");
        Field byNameField = Enchantment.class.getDeclaredField("byName");
        
        // 设置可访问
        byKeyField.setAccessible(true);
        byNameField.setAccessible(true);
        
        // 获取 Map 实例
        HashMap<NamespacedKey, Enchantment> byKey = (HashMap<NamespacedKey, Enchantment>) byKeyField.get(null);
        HashMap<String, Enchantment> byName = (HashMap<String, Enchantment>) byNameField.get(null);
        
        // 移除附魔
        byKey.remove(lifeStealEnchantment.getKey());
        byName.remove(lifeStealEnchantment.getName());
    }
    
    public static LifeStealPlugin getInstance() {
        return instance;
    }
    
    public LifeStealEnchantment getLifeStealEnchantment() {
        return lifeStealEnchantment;
    }
}
