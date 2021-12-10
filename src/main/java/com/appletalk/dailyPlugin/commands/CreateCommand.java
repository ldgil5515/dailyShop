package com.appletalk.dailyPlugin.commands;

import com.appletalk.dailyPlugin.dailyShopPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

/**
 * Command that displays the help.
 */
public class CreateCommand extends AbstractCommand {

    /**
     * The name of the command.
     */
    public static final String NAME = "Create";

    /**
     * The description of the command.
     */
    public static final String DESCRIPTION = "Create a shop";

    /**
     * The main permission of the command.
     */
    public static final String PERMISSION = "dailyShop.create";

    /**
     * The proper usage of the command.
     */
    public static final String USAGE = "/dailyShop create";

    /**
     * The sub permissions of the command.
     */
    public static final String[] SUB_PERMISSIONS = {""};

    /**
     * Construct out object.
     *
     * @param sender the command sender
     */
    public CreateCommand(CommandSender sender) {
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

        YamlConfiguration shopConfig = new YamlConfiguration();
        shopConfig.set("shop.row", 6);
        shopConfig.set("shop.gui", "default");
        try {
            shopConfig.save(new File(dailyShopPlugin.getInstance().getDataFolder() + "/" + "shopConfig", args[0] + ".yml"));
            p.sendMessage("생성 성공");

        } catch (Exception var4) {
            p.sendMessage("생성 실패");
        }
    }
}
