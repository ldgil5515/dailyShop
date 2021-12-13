package com.appletalk.dailyPlugin.commands;

import com.appletalk.dailyPlugin.dailyShopPlugin;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Command that displays the help.
 */
public class OpenCommand extends AbstractCommand {

    private FileConfiguration config = null;
    /**
     * The name of the command.
     */
    public static final String NAME = "Open";

    /**
     * The description of the command.
     */
    public static final String DESCRIPTION = "Open a shop";

    /**
     * The main permission of the command.
     */
    public static final String PERMISSION = "dailyShop.open";

    /**
     * The proper usage of the command.
     */
    public static final String USAGE = "/dailyShop open";

    /**
     * The sub permissions of the command.
     */
    public static final String[] SUB_PERMISSIONS = {""};

    /**
     * Construct out object.
     *
     * @param sender the command sender
     */
    public OpenCommand(CommandSender sender) {
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
        final ChestGui gui = new ChestGui(6, args[1]);
        final StaticPane pane = new StaticPane(9, 6);

        List<ItemStack> itemlist = (List<ItemStack>) dailyShopPlugin.getInstance().shopMap.get(args[1]).get("item");

        int index = 0;
        for(ItemStack s : itemlist.toArray(new ItemStack[0])){
            if(s != null){
                GuiItem newItem = new GuiItem(s);
                pane.addItem(newItem, index%9, index/9);
            }
            index++;
        }

        gui.addPane(pane);
        gui.setOnGlobalClick(event -> {
            if(event.isLeftClick()) {
                ItemStack itemStack = event.getCurrentItem();
                ItemMeta itemMeta = itemStack.getItemMeta();

                event.getClick().isShiftClick();
                NamespacedKey key = new NamespacedKey(dailyShopPlugin.getInstance(), "if-uuid");
                itemMeta.getPersistentDataContainer().remove(key);
                itemStack.setItemMeta(itemMeta);

                p.getInventory().addItem(itemStack);
                event.setCancelled(true);
            }
            if(event.isRightClick()) event.setCancelled(true);
        });

        gui.show(p);
    }
}
