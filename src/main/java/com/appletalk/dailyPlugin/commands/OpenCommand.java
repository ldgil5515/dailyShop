package com.appletalk.dailyPlugin.commands;

import com.appletalk.dailyPlugin.api.LoreHandler;
import com.appletalk.dailyPlugin.api.NBTHandler;
import com.appletalk.dailyPlugin.dailyShopPlugin;
import com.appletalk.dailyPlugin.messaging.MessageFormatter;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.appletalk.dailyPlugin.dailyLangUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.appletalk.dailyPlugin.api.LoreHandler.getPrice;

/**
 * Command that displays the help.
 */
public class OpenCommand extends AbstractCommand {
    dailyLangUtils translater = new dailyLangUtils();
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

        String menuTexture = "§f" + dailyShopPlugin.getInstance().shopMap.get(args[1]).get("gui");

        Player p = (Player) sender;
        final ChestGui gui = new ChestGui((Integer) dailyShopPlugin.getInstance().shopMap.get(args[1]).get("row"), menuTexture);
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
            event.setCancelled(true);
            ItemStack itemStack = event.getCurrentItem().clone();
            if(event.isLeftClick()) {
                if(getPrice(itemStack,true) != 0){
                    if(event.isShiftClick()) itemStack.setAmount(itemStack.getAmount()*64);
                    int buyAmount = itemStack.getAmount();
                    int payPrice = getPrice(event.getCurrentItem(),false) * buyAmount;

                    if(dailyShopPlugin.getInstance().econ.getBalance(p.getName())>=payPrice){
                        LoreHandler.removeShopLore(itemStack);
                        NBTHandler.removeNBT(itemStack, "if-uuid");
                        if(p.getInventory().addItem(itemStack).size() != 0){
                            p.sendMessage(MessageFormatter.prefix("인벤토리에 공간이 부족합니다."));
                        }
                        else {
                            dailyShopPlugin.getInstance().econ.withdrawPlayer(p.getName(), payPrice);
                            p.sendMessage(MessageFormatter.prefix("&6" + translater.getItemDisplayName(itemStack, p) + "&f를 &6" + payPrice + "&f원에 구매하였습니다."));
                        }
                    } else {
                        p.sendMessage(MessageFormatter.prefix("돈이 부족합니다."));
                    }
                }
            }
            else if(event.isRightClick()) {
                LoreHandler.removeShopLore(itemStack);
                NBTHandler.removeNBT(itemStack, "if-uuid");
                if(event.isShiftClick()) itemStack.setAmount(itemStack.getAmount()*64);

                int amountLack;
                Map<Integer, ItemStack> returnValue = new HashMap<>();
                returnValue = p.getInventory().removeItem(itemStack.clone());
                if(returnValue.size() == 0){
                    amountLack = 0;
                }
                else amountLack = returnValue.get(0).getAmount();

                if(amountLack == 1){
                    p.sendMessage(MessageFormatter.prefix("아이템을 가지고 있지 않습니다."));
                }
                else {
                    int sellAmount = itemStack.getAmount()-amountLack;
                    int payPrice = getPrice(event.getCurrentItem(),false) * sellAmount;

                    dailyShopPlugin.getInstance().econ.depositPlayer(p.getName(), payPrice);
                    p.sendMessage(MessageFormatter.prefix(payPrice + "원을 획득하였습니다."));
                }
            }
        });

        gui.show(p);
    }
}
