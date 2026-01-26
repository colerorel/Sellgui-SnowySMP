package com.colerorel.guisell;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.*;
import java.util.stream.Collectors;

public class SellListener implements Listener, CommandExecutor {

    private final GuiSellPlugin plugin;

    public SellListener(GuiSellPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) return true;
        openGUI(player);
        return true;
    }

    // Main Sell GUI
    public void openGUI(Player player) {
        Inventory gui = Bukkit.createInventory(new SellGUIHolder(), 54, ChatColor.BLUE + "Snowy" + ChatColor.WHITE + "SMP Sell");
        ItemStack glass = createItem(Material.BLUE_STAINED_GLASS_PANE, " ");
        for (int i = 45; i < 54; i++) gui.setItem(i, glass);
        ItemStack star = createItem(Material.NETHER_STAR, ChatColor.YELLOW + "☣ Inventory Sell ☣");
        gui.setItem(49, star);
        player.openInventory(gui);
    }

    // Standard Worth List GUI
    public void openWorthList(Player player, int page) {
        Inventory inv = Bukkit.createInventory(new WorthListHolder(), 54, ChatColor.BLUE + "Snowy" + ChatColor.WHITE + "Prices");
        List<Material> materials = plugin.getSellPrices().keySet().stream()
                .sorted((m1, m2) -> Double.compare(plugin.getSellPrices().get(m2), plugin.getSellPrices().get(m1)))
                .collect(Collectors.toList());

        int start = page * 45;
        for (int i = 0; i < 45 && (start + i) < materials.size(); i++) {
            Material mat = materials.get(start + i);
            ItemStack item = new ItemStack(mat);
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.setLore(List.of(ChatColor.GRAY + "Price: " + ChatColor.GREEN + "$" + String.format("%.2f", plugin.getSellPrices().get(mat))));
                item.setItemMeta(meta);
            }
            inv.setItem(i, item);
        }
        player.openInventory(inv);
    }

    // NEW: SnowySMP Removed Items GUI (Used by /worth remove list)
    public void openRemoveList(Player player) {
        Inventory inv = Bukkit.createInventory(new RemovedItemsHolder(), 54, ChatColor.BLUE + "Snowy" + ChatColor.WHITE + "SMP Removed Items");
        List<Material> materials = new ArrayList<>(plugin.getSellPrices().keySet());

        for (int i = 0; i < 54 && i < materials.size(); i++) {
            Material mat = materials.get(i);
            ItemStack item = new ItemStack(mat);
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.setDisplayName(ChatColor.BLUE + mat.name());
                meta.setLore(List.of(ChatColor.GRAY + "Currently in the price list."));
                item.setItemMeta(meta);
            }
            inv.setItem(i, item);
        }
        player.openInventory(inv);
    }

    private ItemStack createItem(Material mat, String name) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) { meta.setDisplayName(name); item.setItemMeta(meta); }
        return item;
    }

    private void processSale(Player player, Inventory inv) {
        double total = 0;
        for (int i = 0; i < 45; i++) {
            ItemStack item = inv.getItem(i);
            if (item != null && plugin.getSellPrices().containsKey(item.getType())) {
                total += plugin.getSellPrices().get(item.getType()) * item.getAmount();
                inv.setItem(i, null);
            }
        }
        if (total > 0) {
            plugin.getEcon().depositPlayer(player, total);
            player.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Sold for $" + String.format("%.2f", total));
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        InventoryHolder holder = e.getInventory().getHolder();
        if (holder instanceof SellGUIHolder) {
            if (e.getRawSlot() == 49) {
                e.setCancelled(true);
                processSale((Player)e.getWhoClicked(), e.getClickedInventory());
                e.getWhoClicked().closeInventory();
            }
        } else if (holder instanceof WorthListHolder || holder instanceof RemovedItemsHolder) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (e.getInventory().getHolder() instanceof SellGUIHolder) {
            processSale((Player) e.getPlayer(), e.getInventory());
        }
    }

    private static class SellGUIHolder implements InventoryHolder { @Override public Inventory getInventory() { return null; } }
    private static class WorthListHolder implements InventoryHolder { @Override public Inventory getInventory() { return null; } }
    private static class RemovedItemsHolder implements InventoryHolder { @Override public Inventory getInventory() { return null; } }
}