package com.appletalk.dailyPlugin.commands;

import com.appletalk.dailyPlugin.api.NBTHandler;
import com.appletalk.dailyPlugin.dailyShopPlugin;
import com.appletalk.dailyPlugin.utils.ClickTarget;
import com.appletalk.dailyPlugin.listener.enterPrice;
import com.appletalk.dailyPlugin.messaging.MessageFormatter;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

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

        if(args.length < 2){
            sender.sendMessage(MessageFormatter.prefix("&3&l/?????? ?????? <????????????> <?????????/???/gui> &r- ????????? ???????????????."));
            return;
        }

        switch (args[2].toLowerCase()) {
            case "?????????" -> itemSet(sender, args);
            case "???" -> rowSet(sender, args);
            case "gui" -> guiSet(sender, args);
        }
    }

    private void itemSet(CommandSender sender, String[] args){
        Player p = (Player) sender;

        final ChestGui gui = new ChestGui(dailyShopPlugin.getInstance().shopMap.get(args[1]).getInt("row"), args[2]);
        final StaticPane pane = new StaticPane(9, 6);

        List<?> itemlist = dailyShopPlugin.getInstance().shopMap.get(args[1]).getList("item");

        if(itemlist != null){
            int index = 0;
            for(Object s : itemlist){
                if(s instanceof ItemStack){
                    GuiItem newItem = new GuiItem((ItemStack) s);
                    pane.addItem(newItem, index%9, index/9);
                }
                index++;
            }
        }

        gui.addPane(pane);
        gui.setOnGlobalClick(event -> {
            if(event.isShiftClick()) {
                event.setCancelled(true);
                if(event.getSlot() == event.getRawSlot()){
                    ClickTarget clickTarget = new ClickTarget();
                    clickTarget.targetShop = args[1];
                    clickTarget.targetItem = event.getCurrentItem();
                    clickTarget.isBuy = event.isRightClick();

                    enterPrice.INSTANCE.enterPriceFlag.put(p.getName(), clickTarget);
                    p.sendMessage(MessageFormatter.prefix(event.isRightClick()?"??????????????? ??????????????????.":"??????????????? ??????????????????."));
                    getServer().getScheduler().runTask(dailyShopPlugin.getInstance(), p::closeInventory);
                }
            }
        });
        gui.setOnClose(event -> {
            List<ItemStack> saveItemStack = Arrays.asList(gui.getInventory().getContents());
            NBTHandler.removeListNBT(saveItemStack, "if-uuid");
            saveItemStack.forEach(s-> {
                if(s!=null) s.setAmount(1);
            });
            dailyShopPlugin.getInstance().shopMap.get(args[1]).set("item", saveItemStack);
            if(enterPrice.INSTANCE.enterPriceFlag.get(p.getName())==null) p.sendMessage(MessageFormatter.prefix("?????? ????????? ????????? ?????????????????????."));
        });

        gui.show(p);
    }

    private void rowSet(CommandSender sender, String[] args){
        Player p = (Player) sender;

        if(args.length != 4) p.sendMessage(MessageFormatter.prefix("?????? ??????????????????."));
        else if(args[3].matches("[+-]?\\d*(\\.\\d+)?") && Integer.parseInt(args[3])>=1 && Integer.parseInt(args[3])<=6){
            dailyShopPlugin.getInstance().shopMap.get(args[1]).set("row", Integer.parseInt(args[3]));
            dailyShopPlugin.getInstance().saveShopConfig();
            p.sendMessage(MessageFormatter.prefix(args[2] + "????????? ?????? " + args[3] + "?????? ?????????????????????."));
        }
        else p.sendMessage(MessageFormatter.prefix("1???~6??? ????????? ??????????????????."));
    }

    private void guiSet(CommandSender sender, String[] args){
        Player p = (Player) sender;
        if(args.length != 4) p.sendMessage(MessageFormatter.prefix("GUI ????????? ??????????????????."));
        else {
            dailyShopPlugin.getInstance().shopMap.get(args[1]).set("gui", args[3]);
            dailyShopPlugin.getInstance().saveShopConfig();
            p.sendMessage(MessageFormatter.prefix(args[2] + "????????? GUI??? " + args[3] + "??? ?????????????????????."));
        }
    }
}
