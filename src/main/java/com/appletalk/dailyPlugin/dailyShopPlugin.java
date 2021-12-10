package com.appletalk.dailyPlugin;

import com.appletalk.dailyPlugin.messaging.MessageFormatter;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.util.*;

public class dailyShopPlugin extends JavaPlugin {
    private static dailyShopPlugin instacne;
    private MessageFormatter messageFormatter = null;
    public Map<String, YamlConfiguration> shopMap = new HashMap<>();


    public static dailyShopPlugin getInstance() {
        return instacne;
    }

    @Override
    public void onEnable() {
        instacne = this;

        messageFormatter = new MessageFormatter();

        this.getCommand("dailyShop").setExecutor(new dailyShopCommandHandler());

        initConfig();
        loadShopConfig();
    }

    @Override
    public void onDisable() {

    }

    public void initConfig() {
        this.saveDefaultConfig();
    }

    public void loadShopConfig() {
        File folder = new File(instacne.getDataFolder() + "/shopConfig");
        File[] files = folder.listFiles();
        if (files != null) {
            Arrays.stream(files).forEach(s -> {
                shopMap.put(s.getName().replace(".yml", ""), YamlConfiguration.loadConfiguration(s));
            });
        }
    }

    public MessageFormatter getMessageFormatter() {
        return messageFormatter;
    }
}