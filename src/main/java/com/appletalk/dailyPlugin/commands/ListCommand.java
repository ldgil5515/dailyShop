package com.appletalk.dailyPlugin.commands;

import com.appletalk.dailyPlugin.dailyShopPlugin;
import com.appletalk.dailyPlugin.messaging.MessageFormatter;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

/**
 * Command that displays the help.
 */
public class ListCommand extends AbstractCommand {

    /**
     * The name of the command.
     */
    public static final String NAME = "List";

    /**
     * The description of the command.
     */
    public static final String DESCRIPTION = "Show list of shop";

    /**
     * The main permission of the command.
     */
    public static final String PERMISSION = "dailyShop.list";

    /**
     * The proper usage of the command.
     */
    public static final String USAGE = "/dailyShop list";

    /**
     * The sub permissions of the command.
     */
    public static final String[] SUB_PERMISSIONS = {""};

    /**
     * Construct out object.
     *
     * @param sender the command sender
     */
    public ListCommand(CommandSender sender) {
        super(sender, NAME, DESCRIPTION, PERMISSION, SUB_PERMISSIONS, USAGE);
    }

    /**
     * Execute the command.
     *
     * @param sender  the sender of the command
     * @param command the command being done
     * @param label   the name of the command
     * @param args    the arguments supplied
     */
    @Override
    public void execute(CommandSender sender, Command command, String label, String[] args) {

        if (!hasPermission()) {
            sender.sendMessage(dailyShopPlugin.getInstance().getMessageFormatter().format("error.no-permission"));
            return;
        }
        Player p = (Player) sender;
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "\n&r  &6&l●  &r&f상점 목록\n "));

        int index = 0;
        for(String i : dailyShopPlugin.getInstance().shopMap.keySet()){
            p.sendMessage(ChatColor.translateAlternateColorCodes('&',"  &3&l" + index + "&l&f - &r" + i));
            index++;
        }
        p.sendMessage("");
    }
}
