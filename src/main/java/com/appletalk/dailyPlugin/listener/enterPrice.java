package com.appletalk.dailyPlugin.listener;

import com.appletalk.dailyPlugin.api.LoreHandler;
import com.appletalk.dailyPlugin.commands.SetCommand;
import com.appletalk.dailyPlugin.dailyShopPlugin;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class enterPrice implements Listener {
    public static final enterPrice INSTANCE = new enterPrice();
    public Map<String, ClickTarget> enterPriceFlag = new HashMap<>();

    @EventHandler
    private void enterPrice(PlayerChatEvent e){
        Player p = e.getPlayer();

        if(enterPriceFlag.containsKey(p.getName())){
            e.setCancelled(true);
            try {
                int price = Integer.parseInt(e.getMessage());

                List<ItemStack> itemlist = (List<ItemStack>) dailyShopPlugin.getInstance().shopMap.get(enterPriceFlag.get(p.getName()).targetShop).get("item");
                ItemStack itemStack = itemlist.get(itemlist.indexOf(enterPriceFlag.get(p.getName()).targetItem));
                if(enterPriceFlag.get(p.getName()).isBuy){
                    LoreHandler.addBuyLore(itemStack, price);
                }
                else {
                    LoreHandler.addSellLore(itemStack, price);
                }
                dailyShopPlugin.getInstance().shopMap.get(enterPriceFlag.get(p.getName()).targetShop).set("item", itemlist);
                enterPriceFlag.remove(p.getName());
            } catch (NumberFormatException r){
                p.sendMessage("숫자만 입력해주세요.");
            }
        }
        return;
    }
}

