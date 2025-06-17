package com.example.lifesteal.commands;

import com.example.lifesteal.LifeStealPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class LifeStealCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "只有玩家可以执行此命令！");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("lifesteal.give")) {
            player.sendMessage(ChatColor.RED + "你没有权限使用此命令！");
            return true;
        }

        int level = 1;
        boolean isBook = false;

        if (args.length > 0) {
            try {
                level = Integer.parseInt(args[0]);
                if (level < 1 || level > 3) {
                    level = 1;
                }
            } catch (NumberFormatException e) {
                player.sendMessage(ChatColor.RED + "请输入有效的等级（1-3）！");
                return false;
            }
        }

        if (args.length > 1 && args[1].equalsIgnoreCase("book")) {
            isBook = true;
        }

        Enchantment enchantment = LifeStealPlugin.getInstance().getLifeStealEnchantment();
        if (enchantment == null) {
            player.sendMessage(ChatColor.RED + "生命汲取附魔未正确注册！");
            return true;
        }

        ItemStack item;

        if (isBook) {
            item = new ItemStack(Material.ENCHANTED_BOOK);
            ItemMeta meta = item.getItemMeta();
            if (meta instanceof EnchantmentStorageMeta) {
                ((EnchantmentStorageMeta) meta).addStoredEnchant(enchantment, level, true);
                item.setItemMeta(meta);
                player.getInventory().addItem(item);
                player.sendMessage(ChatColor.GREEN + "已给予你一本生命汲取 " + level + " 附魔书！");
            } else {
                player.sendMessage(ChatColor.RED + "无法生成附魔书！");
            }
        } else {
            item = player.getInventory().getItemInMainHand();
            if (item.getType() == Material.AIR) {
                player.sendMessage(ChatColor.RED + "请手持一件可附魔的物品！");
                return true;
            }
            if (!enchantment.canEnchantItem(item)) {
                player.sendMessage(ChatColor.RED + "此物品不能添加生命汲取附魔！");
                return true;
            }
            ItemMeta meta = item.getItemMeta();
            meta.addEnchant(enchantment, level, true);
            item.setItemMeta(meta);
            player.sendMessage(ChatColor.GREEN + "已为你的物品添加生命汲取 " + level + " 附魔！");
        }

        return true;
    }
}
