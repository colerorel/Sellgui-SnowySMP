package com.colerorel.guisell;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

public class GuiSellPlugin extends JavaPlugin {

    private static Economy econ = null;
    private final Map<Material, Double> sellPrices = new EnumMap<>(Material.class);
    private final String PREFIX = ChatColor.BLUE + "" + ChatColor.BOLD + "Snowy" + ChatColor.WHITE + "" + ChatColor.BOLD + "SMP " + ChatColor.DARK_GRAY + "» ";
    private SellListener sellListener;

    @Override
    public void onEnable() {
        if (!setupEconomy()) {
            getLogger().severe("Vault dependency not found! Disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        setupWorthFile();
        loadData();

        this.sellListener = new SellListener(this);
        WorthCommand worthCmd = new WorthCommand(this);

        if (getCommand("worth") != null) {
            getCommand("worth").setExecutor(worthCmd);
            getCommand("worth").setTabCompleter(worthCmd);
        }

        if (getCommand("guisell") != null) {
            getCommand("guisell").setExecutor(sellListener);
        }

        getServer().getPluginManager().registerEvents(sellListener, this);
        getLogger().info("SnowySMP Sell Plugin Enabled (1.21.1/Java 21)");
    }

    public SellListener getSellListener() { return sellListener; }

    public void loadData() {
        sellPrices.clear();
        File worthFile = new File(getDataFolder(), "worth.yml");
        if (!worthFile.exists()) saveResource("worth.yml", false);
        
        FileConfiguration worthConfig = YamlConfiguration.loadConfiguration(worthFile);
        if (worthConfig.getConfigurationSection("prices") != null) {
            for (String key : worthConfig.getConfigurationSection("prices").getKeys(false)) {
                Material mat = Material.getMaterial(key.toUpperCase());
                if (mat != null) {
                    sellPrices.put(mat, worthConfig.getDouble("prices." + key));
                }
            }
        }
    }

    public void saveWorth(Material mat, Double price) {
        File worthFile = new File(getDataFolder(), "worth.yml");
        FileConfiguration worthConfig = YamlConfiguration.loadConfiguration(worthFile);
        if (price == null) {
            sellPrices.remove(mat);
            worthConfig.set("prices." + mat.name(), null);
        } else {
            sellPrices.put(mat, price);
            worthConfig.set("prices." + mat.name(), price);
        }
        try {
            worthConfig.save(worthFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupWorthFile() {
        if (!getDataFolder().exists()) getDataFolder().mkdirs();
        File f = new File(getDataFolder(), "worth.yml");
        if (!f.exists()) saveResource("worth.yml", false);
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) return false;
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) return false;
        econ = rsp.getProvider();
        return (econ != null);
    }

    public Map<Material, Double> getSellPrices() { return sellPrices; }
    public String getPrefix() { return PREFIX; }
    public Economy getEcon() { return econ; }
}