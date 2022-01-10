package com.appletalk.dailyPlugin.commands;

import com.appletalk.dailyPlugin.dailyShopPlugin;
import com.appletalk.dailyPlugin.messaging.MessageFormatter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

/**
 * Command that displays the help.
 */
public class DeleteCommand extends AbstractCommand {

    /**
     * The name of the command.
     */
    public static final String NAME = "Delete";

    /**
     * The description of the command.
     */
    public static final String DESCRIPTION = "Delete a shop";

    /**
     * The main permission of the command.
     */
    public static final String PERMISSION = "dailyShop.delete";

    /**
     * The proper usage of the command.
     */
    public static final String USAGE = "/dailyShop delete";

    /**
     * The sub permissions of the command.
     */
    public static final String[] SUB_PERMISSIONS = {""};

    /**
     * Construct out object.
     *
     * @param sender the command sender
     */
    public DeleteCommand(CommandSender sender) {
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

        if(!dailyShopPlugin.getInstance().shopMap.containsKey(args[1])) p.sendMessage(MessageFormatter.prefix("존재하지 않는 상점입니다."));
        else {
            File deleteFile = new File(dailyShopPlugin.getInstance().getDataFolder() + "/" + "shopConfig", args[1] + ".yml");

            dailyShopPlugin.getInstance().shopMap.remove(args[1]);
            try {
                deleteFile.delete();
                p.sendMessage(MessageFormatter.prefix("상점이 성공적으로 삭제되었습니다."));
            } catch (Exception var4) {
                p.sendMessage(MessageFormatter.prefix("상점 삭제를 실패하였습니다."));
            }
        }
    }
}
