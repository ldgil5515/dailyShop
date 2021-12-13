package com.appletalk.dailyPlugin.api;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class LoreHandler {
    public static ItemStack addBuyLore(ItemStack itemStack, Integer price) {
        if(itemStack != null){
            ItemStack dummyItem = itemStack;
            List<String> itemLore = new ArrayList<>();
            if(itemStack.getItemMeta().hasLore()){
                itemLore = itemStack.getItemMeta().getLore();
            }

            if(isListContain(itemLore, " §6§l> §f우클릭 시 아이템을 구매합니다.")){
                int index = 0;
                for (String s : itemLore) {
                    if(s.contains("아이템 구매가격:")){
                        break;
                    }
                    index++;
                }
                itemLore.set(index, ChatColor.translateAlternateColorCodes('&', " &8&l> &r&f아이템 구매가격: &6" + price + "&f원 &a(64개 " + price*64 + ")"));
            }
            else {
                if(isListContain(itemLore, " §6§l> §f좌클릭 시 아이템을 판매합니다.")){
                    itemLore.add(itemLore.size()-4, ChatColor.translateAlternateColorCodes('&', " &8&l> &r&f아이템 구매가격: &6" + price + "&f원 &a(64개 " + price*64 + ")"));
                    itemLore.add(itemLore.size()-1, ChatColor.translateAlternateColorCodes('&', " &6&l> &r&f우클릭 시 아이템을 구매합니다."));
                    itemLore.add(itemLore.size()-1, ChatColor.translateAlternateColorCodes('&', " &6&l   └ &r&f시프트 + 우클릭 시 아이템을 64개 구매합니다."));
                }
                else {
                    itemLore.add("");
                    itemLore.add(ChatColor.translateAlternateColorCodes('&', " &8&l> &r&f아이템 구매가격: &6" + price + "&f원 &a(64개 " + price*64 + ")"));
                    itemLore.add("");
                    itemLore.add(ChatColor.translateAlternateColorCodes('&', " &6&l> &r&f우클릭 시 아이템을 구매합니다."));
                    itemLore.add(ChatColor.translateAlternateColorCodes('&', " &6&l   └ &r&f시프트 + 우클릭 시 아이템을 64개 구매합니다."));
                    itemLore.add("");
                }
            }

            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setLore(itemLore);
            dummyItem.setItemMeta(itemMeta);
            return dummyItem;
        }
        return itemStack;
    };

    public static ItemStack addSellLore(ItemStack itemStack, Integer price) {
        if(itemStack != null){
            ItemStack dummyItem = itemStack;
            List<String> itemLore = new ArrayList<>();
            if(itemStack.getItemMeta().hasLore()){
                itemLore = itemStack.getItemMeta().getLore();
            }

            if(isListContain(itemLore, " §6§l> §f좌클릭 시 아이템을 판매합니다.")){
                int index = 0;
                for (String s : itemLore) {
                    if(s.contains("아이템 판매가격:")){
                        break;
                    }
                    index++;
                }
                itemLore.set(index, ChatColor.translateAlternateColorCodes('&', " &8&l> &r&f아이템 판매가격: &6" + price + "&f원 &a(64개 " + price*64 + ")"));
            }
            else {
                if(isListContain(itemLore, " §6§l> §f우클릭 시 아이템을 구매합니다.")){
                    itemLore.add(itemLore.size()-5, ChatColor.translateAlternateColorCodes('&', " &8&l> &r&f아이템 판매가격: &6" + price + "&f원 &a(64개 " + price*64 + ")"));
                    itemLore.add(itemLore.size()-3, ChatColor.translateAlternateColorCodes('&', " &6&l> &r&f좌클릭 시 아이템을 판매합니다."));
                    itemLore.add(itemLore.size()-3, ChatColor.translateAlternateColorCodes('&', " &6&l   └ &r&f시프트 + 좌클릭 시 아이템을 64개 판매합니다."));
                }
                else {
                    itemLore.add("");
                    itemLore.add(ChatColor.translateAlternateColorCodes('&', " &8&l> &r&f아이템 판매가격: &6" + price + "&f원 &a(64개 " + price*64 + ")"));
                    itemLore.add("");
                    itemLore.add(ChatColor.translateAlternateColorCodes('&', " &6&l> &r&f좌클릭 시 아이템을 판매합니다."));
                    itemLore.add(ChatColor.translateAlternateColorCodes('&', " &6&l   └ &r&f시프트 + 좌클릭 시 아이템을 64개 판매합니다."));
                    itemLore.add("");
                }
            }

            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setLore(itemLore);
            dummyItem.setItemMeta(itemMeta);
            return dummyItem;
        }
        return itemStack;
    };

    private static boolean isListContain(List<String> itemLore, String compareString){
        boolean isContain = false;

        for (String s : itemLore) {
            if(s.contains(compareString)) {
                isContain = true;
                break;
            }
            else isContain = false;
        }
        return isContain;
    }
}