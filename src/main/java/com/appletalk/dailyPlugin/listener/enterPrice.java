package com.appletalk.dailyPlugin.listener;

import com.appletalk.dailyPlugin.api.LoreHandler;
import com.appletalk.dailyPlugin.dailyShopPlugin;
import com.appletalk.dailyPlugin.messaging.MessageFormatter;
import com.appletalk.dailyPlugin.utils.ClickTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.bukkit.Bukkit.getServer;


public class enterPrice implements Listener {
    public static final enterPrice INSTANCE = new enterPrice();
    public Map<String, ClickTarget> enterPriceFlag = new HashMap<>();

    @EventHandler
    private void onPlayerChat(AsyncPlayerChatEvent e){
        Player p = e.getPlayer();

        if(enterPriceFlag.containsKey(p.getName())){
            e.setCancelled(true);
            try {
                int price = Integer.parseInt(e.getMessage());

                List<?> itemlist = dailyShopPlugin.getInstance().shopMap.get(enterPriceFlag.get(p.getName()).targetShop).getList("item");
                assert itemlist != null;
                ItemStack itemStack = (ItemStack) itemlist.get(itemlist.indexOf(enterPriceFlag.get(p.getName()).targetItem));
                LoreHandler.addLore(itemStack, price, enterPriceFlag.get(p.getName()).isBuy);
                dailyShopPlugin.getInstance().shopMap.get(enterPriceFlag.get(p.getName()).targetShop).set("item", itemlist);
                getServer().getScheduler().runTask(dailyShopPlugin.getInstance(), () -> {
                    p.chat("/상점 설정 " + enterPriceFlag.get(p.getName()).targetShop + " 아이템");
                    enterPriceFlag.remove(p.getName());
                });
            } catch (NumberFormatException r){
                p.sendMessage(MessageFormatter.prefix("가격이 올바르지 않습니다."));
            }
        }
    }
}

