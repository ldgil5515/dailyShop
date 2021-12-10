package com.appletalk.dailyPlugin.commands;

import com.appletalk.dailyPlugin.dailyShopPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

/**
 * Command that displays the help.
 */
public class HelpCommand extends AbstractCommand {

    /**
     * The name of the command.
     */
    public static final String NAME = "Help";

    /**
     * The description of the command.
     */
    public static final String DESCRIPTION = "Displays help information.";

    /**
     * The main permission of the command.
     */
    public static final String PERMISSION = "dailyShop.help";

    /**
     * The proper usage of the command.
     */
    public static final String USAGE = "/dailyShop help";

    /**
     * The sub permissions of the command.
     */
    public static final String[] SUB_PERMISSIONS = {""};

    /**
     * Construct out object.
     *
     * @param sender the command sender
     */
    public HelpCommand(CommandSender sender) {
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

        ConfigurationSection help = dailyShopPlugin.getInstance().getMessageFormatter().getMessageFile().getConfig().getConfigurationSection("help");

        for (String s : help.getKeys(false)) {
            if (sender.hasPermission("dailyShop." + s) || s.equalsIgnoreCase("header"))
                sender.sendMessage(dailyShopPlugin.getInstance().getMessageFormatter().format(false, help.getName() + "." + s));
        }

    }
}
