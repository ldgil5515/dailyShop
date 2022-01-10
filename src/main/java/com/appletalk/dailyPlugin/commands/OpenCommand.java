package com.appletalk.dailyPlugin.commands;

import com.appletalk.dailyPlugin.api.LoreHandler;
import com.appletalk.dailyPlugin.api.NBTHandler;
import com.appletalk.dailyPlugin.dailyLangUtils;
import com.appletalk.dailyPlugin.dailyShopPlugin;
import com.appletalk.dailyPlugin.messaging.MessageFormatter;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
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
        if(args.length < 2) {
            sender.sendMessage(MessageFormatter.prefix("상점 이름을 입력해주세요."));
            return;
        }
        if(dailyShopPlugin.getInstance().shopMap.get(args[1]) == null) {
            sender.sendMessage(MessageFormatter.prefix("존재하지 않는 메뉴입니다."));
            return;
        }

        Player p = (Player) sender;
        String menuTexture = "§f" + dailyShopPlugin.getInstance().shopMap.get(args[1]).get("gui");
        final ChestGui gui = new ChestGui(dailyShopPlugin.getInstance().shopMap.get(args[1]).getInt("row"), menuTexture);
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
            event.setCancelled(true);
            if(event.getSlot() == event.getRawSlot() && event.getCurrentItem() != null){
                ItemStack itemStack = event.getCurrentItem().clone();
                LoreHandler.removeShopLore(itemStack);
                NBTHandler.removeNBT(itemStack, "if-uuid");
                if(event.isShiftClick()) itemStack.setAmount(itemStack.getAmount()*64);
                int price = getPrice(event.getCurrentItem(),event.isRightClick());

                if(event.isRightClick() && getPrice(event.getCurrentItem(),true) != 0) {
                    if(dailyShopPlugin.getInstance().econ.getBalance(p.getName())<price) p.sendMessage(MessageFormatter.prefix("돈이 부족합니다."));
                    else {
                        ItemStack giveResult = p.getInventory().addItem(itemStack.clone()).get(0);
                        int giveAmount = itemStack.getAmount() - (giveResult == null?0:giveResult.getAmount());

                        if(giveAmount == 0) p.sendMessage(MessageFormatter.prefix("인벤토리에 공간이 부족합니다."));
                        else {
                            int canBuyAmount;
                            for(canBuyAmount=1; canBuyAmount<=giveAmount; canBuyAmount++){
                                if(dailyShopPlugin.getInstance().econ.getBalance(p.getName())<getPrice(event.getCurrentItem(),event.isRightClick()) * canBuyAmount) {
                                    break;
                                }
                            }
                            canBuyAmount -= 1;
                            price = getPrice(event.getCurrentItem(),event.isRightClick()) * canBuyAmount;
                            ItemStack removeItem = itemStack.clone();
                            if(giveAmount>canBuyAmount){
                                removeItem.setAmount(giveAmount-canBuyAmount);
                                p.getInventory().removeItem(removeItem.clone());
                            }
                            dailyShopPlugin.getInstance().econ.withdrawPlayer(p.getName(), price);
                            p.sendMessage(MessageFormatter.prefix("&e&l" + translater.getItemDisplayName(itemStack, p) + MessageFormatter.hasBase(translater.getItemDisplayName(itemStack, p), "&r&f을", "&r&f를") + " &a&l" + price + "&r&f원에 구매하였습니다."));
                        }
                    }
                }
                else if(event.isLeftClick() && getPrice(event.getCurrentItem(),false) != 0) {
                    ItemStack sellResult = p.getInventory().removeItem(itemStack.clone()).get(0);
                    int sellAmount = itemStack.getAmount() - (sellResult == null?0:sellResult.getAmount());
                    if(sellAmount == 0) p.sendMessage(MessageFormatter.prefix("아이템을 가지고 있지 않습니다."));
                    else {
                        price = getPrice(event.getCurrentItem(),false) * sellAmount;
                        dailyShopPlugin.getInstance().econ.depositPlayer(p.getName(), price);
                        p.sendMessage(MessageFormatter.prefix("&e&l" + translater.getItemDisplayName(itemStack, p) + MessageFormatter.hasBase(translater.getItemDisplayName(itemStack, p), "&r&f을", "&r&f를") + " 판매하여 &a&l" + price + "&r&f원을 획득하였습니다."));
                    }
                }
            }
        });

        gui.show(p);
    }
}
