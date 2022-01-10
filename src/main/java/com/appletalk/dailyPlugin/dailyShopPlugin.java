package com.appletalk.dailyPlugin;

import net.milkbowl.vault.economy.Economy;
import com.appletalk.dailyPlugin.listener.enterPrice;
import com.appletalk.dailyPlugin.messaging.MessageFormatter;
import org.bukkit.configuration.file.YamlConfiguration;

import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class dailyShopPlugin extends JavaPlugin {
    private static dailyShopPlugin instance;
    private MessageFormatter messageFormatter = null;
    public Map<String, YamlConfiguration> shopMap = new HashMap<>();
    public Economy econ = null;

    public static dailyShopPlugin getInstance() {
        return instance;
    }
    @Override
    public void onEnable() {
        instance = this;
        messageFormatter = new MessageFormatter();
        this.getCommand("상점").setExecutor(new dailyShopCommandHandler());
        this.getCommand("상점").setTabCompleter(new dailyShopTabCompleter());
        getServer().getPluginManager().registerEvents(enterPrice.INSTANCE, this);
        initConfig();
        loadShopConfig();
        if(!setupEconomy()){
            System.out.println("Failed load Vault!");
        }
    }

    @Override
    public void onDisable() {
        saveShopConfig();
    }

    public void initConfig() {
        this.saveDefaultConfig();
    }

    public void loadShopConfig() {
        File folder = new File(instance.getDataFolder() + "/shopConfig");
        File[] files = folder.listFiles();
        if (files != null) {
            Arrays.stream(files).forEach(s -> {
                shopMap.put(s.getName().replace(".yml", ""), YamlConfiguration.loadConfiguration(s));
            });
        }
    }

    public void saveShopConfig() {
        shopMap.keySet().forEach(s -> {
            try {
                shopMap.get(s).save(new File(dailyShopPlugin.getInstance().getDataFolder() + "/" + "shopConfig", s + ".yml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public MessageFormatter getMessageFormatter() {
        return messageFormatter;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return true;
    }
}