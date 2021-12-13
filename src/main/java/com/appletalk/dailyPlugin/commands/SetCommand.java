package com.appletalk.dailyPlugin.commands;

import com.appletalk.dailyPlugin.api.LoreHandler;
import com.appletalk.dailyPlugin.api.NBTHandler;
import com.appletalk.dailyPlugin.dailyShopPlugin;
import com.appletalk.dailyPlugin.listener.ClickTarget;
import com.appletalk.dailyPlugin.listener.enterPrice;
import com.appletalk.dailyPlugin.messaging.MessageFormatter;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static org.bukkit.Bukkit.getServer;

/**
 * Command that displays the help.
 */
public class SetCommand extends AbstractCommand {
    /**
     * The name of the command.
     */
    public static final String NAME = "Set";

    /**
     * The description of the command.
     */
    public static final String DESCRIPTION = "Set a shop";

    /**
     * The main permission of the command.
     */
    public static final String PERMISSION = "dailyShop.set";

    /**
     * The proper usage of the command.
     */
    public static final String USAGE = "/dailyShop set";

    /**
     * The sub permissions of the command.
     */
    public static final String[] SUB_PERMISSIONS = {""};

    /**
     * Construct out object.
     *
     * @param sender the command sender
     */
    public SetCommand(CommandSender sender) {
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

        switch (args[1].toLowerCase()) {
            case "item":
                itemSet(sender, args);
                break;
            case "row":
                rowSet(sender, args);
                break;
            case "gui":
                guiSet(sender, args);
                break;
        }
    }

    private void itemSet(CommandSender sender, String[] args){
        Player p = (Player) sender;

        final ChestGui gui = new ChestGui(6, args[2]);
        final StaticPane pane = new StaticPane(9, 6);

        List<ItemStack> itemlist = (List<ItemStack>) dailyShopPlugin.getInstance().shopMap.get(args[2]).get("item");

        int index = 0;
        if (itemlist != null){
            for(ItemStack s : itemlist.toArray(new ItemStack[0])){
                if(s != null){
                    GuiItem newItem = new GuiItem(s);
                    pane.addItem(newItem, index%9, index/9);
                }
                index++;
            }
        }

        gui.addPane(pane);
        gui.setOnGlobalClick(event -> {
            if(event.isShiftClick()) {
                event.setCancelled(true);
                ClickTarget clickTarget = new ClickTarget();
                clickTarget.targetShop = args[2];
                clickTarget.targetItem = event.getCurrentItem();
                clickTarget.isBuy = event.isRightClick();

                enterPrice.INSTANCE.enterPriceFlag.put(p.getName(), clickTarget);
                getServer().getScheduler().runTask(dailyShopPlugin.getInstance(), () -> {
                    p.closeInventory();
                });
            }
        });
        gui.setOnClose(event -> {
            List<ItemStack> saveItemStack = Arrays.asList(gui.getInventory().getContents());
            saveItemStack = NBTHandler.removeListNBT(saveItemStack, "if-uuid");
            dailyShopPlugin.getInstance().shopMap.get(args[2]).set("item", saveItemStack);
            p.sendMessage(MessageFormatter.prefix("상점 아이템 설정이 완료되었습니다."));
        });

        gui.show(p);
    }

    private void rowSet(CommandSender sender, String[] args){
        Player p = (Player) sender;
        dailyShopPlugin.getInstance().shopMap.get(args[2]).set("row", args[3]);
        dailyShopPlugin.getInstance().saveShopConfig();
        p.sendMessage(MessageFormatter.prefix(args[2] + "상점의 줄이 " + args[3] + "으로 변경되었습니다."));
    }

    private void guiSet(CommandSender sender, String[] args){
        Player p = (Player) sender;
        dailyShopPlugin.getInstance().shopMap.get(args[2]).set("gui", args[3]);
        dailyShopPlugin.getInstance().saveShopConfig();
        p.sendMessage(MessageFormatter.prefix(args[2] + "상점의 GUI가 " + args[3] + "로 변경되었습니다."));
    }
}
