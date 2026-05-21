package com.colerorel.guisell;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class WorthCommand implements CommandExecutor, TabCompleter {

    private final GuiSellPlugin plugin;

    public WorthCommand(GuiSellPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) return true;

        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "list" -> {
                    plugin.getSellListener().openWorthList(player, 0);
                    return true;
                }
                case "remove" -> {
                    if (!player.hasPermission("guisell.admin")) return true;
                    handleRemove(player, args);
                    return true;
                }
                case "restore" -> {
                    if (!player.hasPermission("guisell.admin")) return true;
                    handleRestore(player, args);
                    return true;
                }
            }
        }

        Material material = (args.length == 0)
            ? player.getInventory().getItemInMainHand().getType()
            : Material.matchMaterial(args[0].toUpperCase());

        if (material == null || material == Material.AIR || !plugin.getSellPrices().containsKey(material)) {
            player.sendMessage(plugin.getPrefix() + ChatColor.RED + "That item has no value!");
            return true;
        }

        showWorth(player, material);
        return true;
    }

    private void handleRemove(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Usage: /worth remove [item] OR /worth remove list");
            return;
        }

        if (args[1].equalsIgnoreCase("list")) {
            plugin.getSellListener().openRemoveList(player);
        } else {
            Material mat = Material.matchMaterial(args[1].toUpperCase());
            if (mat != null && plugin.getSellPrices().containsKey(mat)) {
                plugin.saveWorth(mat, null);
                player.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Removed " + mat.name() + " from prices.");
            } else {
                player.sendMessage(plugin.getPrefix() + ChatColor.RED + "Item not found in price list.");
            }
        }
    }

    private void handleRestore(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Usage: /worth restore [itemname]");
            return;
        }

        Material mat = Material.matchMaterial(args[1].toUpperCase());

        if (mat != null) {
            plugin.saveWorth(mat, 1.0);
            player.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Restored " + mat.name() + " to $1.00.");
        } else {
            player.sendMessage(plugin.getPrefix() + ChatColor.RED + "Invalid item name.");
        }
    }

    private void showWorth(Player player, Material material) {
        double price = plugin.getSellPrices().get(material);

        player.sendMessage("");
        player.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "❄ ITEM WORTH ❄");
        player.sendMessage(ChatColor.DARK_GRAY + " » " + ChatColor.GRAY + "Item: " + ChatColor.WHITE + material.name().toLowerCase().replace("_", " "));
        player.sendMessage(ChatColor.DARK_GRAY + " » " + ChatColor.GRAY + "Price: " + ChatColor.GREEN + "$" + String.format("%.2f", price));

        ItemStack hand = player.getInventory().getItemInMainHand();

        if (hand.getType() == material && hand.getAmount() > 1) {
            player.sendMessage(ChatColor.DARK_GRAY + " » " + ChatColor.GRAY + "Stack Value: " + ChatColor.AQUA + "$" + String.format("%.2f", price * hand.getAmount()));
        }

        player.sendMessage("");
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1.0f, 2.0f);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return List.of("list", "remove", "restore");
        }

        if (args.length == 2 && (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("restore"))) {
            if (args[0].equalsIgnoreCase("remove")) return List.of("list");
        }

        return null;
    }
}
