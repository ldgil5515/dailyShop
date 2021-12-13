package com.appletalk.dailyPlugin;

import com.appletalk.dailyPlugin.commands.SetCommand;
import com.appletalk.dailyPlugin.listener.enterPrice;
import com.appletalk.dailyPlugin.messaging.MessageFormatter;
import org.bukkit.configuration.file.YamlConfiguration;

import org.bukkit.plugin.java.JavaPlugin;


import java.io.File;
import java.io.IOException;
import java.util.*;

public class dailyShopPlugin extends JavaPlugin {
    private static dailyShopPlugin instance;
    private MessageFormatter messageFormatter = null;
    public Map<String, YamlConfiguration> shopMap = new HashMap<>();


    public static dailyShopPlugin getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        messageFormatter = new MessageFormatter();

        this.getCommand("dailyShop").setExecutor(new dailyShopCommandHandler());
        getServer().getPluginManager().registerEvents(enterPrice.INSTANCE, this);

        initConfig();
        loadShopConfig();
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
}