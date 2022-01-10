package com.appletalk.dailyPlugin.api;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class LoreHandler {
    public static void addLore(ItemStack itemStack, Integer price, boolean isBuy) {
        if (itemStack != null) {
            List<String> itemLore = new ArrayList<>();

            if(itemStack.hasItemMeta() && itemStack.getItemMeta().hasLore()){
                itemLore = itemStack.getItemMeta().getLore();
            }

            if(price==0) removeLore(itemLore, isBuy);
            else if (price!=0 && isListContain(itemLore, isBuy?" §6§l- §f우클릭 시 아이템을 구매합니다.":" §6§l- §f좌클릭 시 아이템을 판매합니다.")) {
                int index = 0;
                for (String s : itemLore) {
                    if (s.contains(isBuy?"아이템 구매가격:":"아이템 판매가격:")) break;
                    index++;
                }
                itemLore.set(index, ChatColor.translateAlternateColorCodes('&', isBuy?" &6&l■ &r&f아이템 구매가격: &e" + price + "&f원 &a(64개 " + price * 64 + ")":" &6&l■ &r&f아이템 판매가격: &e" + price + "&f원 &a(64개 " + price * 64 + ")"));
            }
            else {
                if (isListContain(itemLore, isBuy?" §6§l- §f좌클릭 시 아이템을 판매합니다.":" §6§l- §f우클릭 시 아이템을 구매합니다.")) {
                    itemLore.add(itemLore.size() - (isBuy?5:6), ChatColor.translateAlternateColorCodes('&', isBuy?" &6&l■ &r&f아이템 구매가격: &e" + price + "&f원 &a(64개 " + price * 64 + ")":" &6&l■ &r&f아이템 판매가격: &e" + price + "&f원 &a(64개 " + price * 64 + ")"));
                    itemLore.add(itemLore.size() - (isBuy?3:4), ChatColor.translateAlternateColorCodes('&', isBuy?" &6&l- &r&f우클릭 시 아이템을 구매합니다.":" &6&l- &r&f좌클릭 시 아이템을 판매합니다."));
                } else {
                    itemLore.add("");
                    itemLore.add(ChatColor.translateAlternateColorCodes('&', " &8&l---------------------------------"));
                    itemLore.add(ChatColor.translateAlternateColorCodes('&', isBuy?" &6&l■ &r&f아이템 구매가격: &e" + price + "&f원 &a(64개 " + price * 64 + ")":" &6&l■ &r&f아이템 판매가격: &e" + price + "&f원 &a(64개 " + price * 64 + ")"));
                    itemLore.add("");
                    itemLore.add(ChatColor.translateAlternateColorCodes('&', isBuy?" &6&l- &r&f우클릭 시 아이템을 구매합니다.":" &6&l- &r&f좌클릭 시 아이템을 판매합니다."));
                    itemLore.add(ChatColor.translateAlternateColorCodes('&', " &6&l- &r&fShift + 클릭 시 아이템을 64개 거래합니다."));
                    itemLore.add(ChatColor.translateAlternateColorCodes('&', " &8&l---------------------------------"));
                    itemLore.add("");
                }
            }

            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setLore(itemLore);
            itemStack.setItemMeta(itemMeta);
        }
    }

    public static Integer getPrice(ItemStack itemStack, boolean isBuy) {
        String parsingString = isBuy ? "아이템 구매가격:" : "아이템 판매가격:";

        if (itemStack.getItemMeta().hasLore()) {
            List<String> itemLore = itemStack.getItemMeta().getLore();
            for (String s : itemLore) {
                if (s.contains(parsingString)) {
                    return Integer.valueOf(s.split("§6")[1].split("§")[3].replace("e", ""));
                }
            }
        }

        return 0;
    }

    private static void removeLore(List<String> itemLore, boolean isBuy) {
        if(isListContain(itemLore, isBuy?" §6§l- §f우클릭 시 아이템을 구매합니다.":" §6§l- §f좌클릭 시 아이템을 판매합니다.")) {
            if(isListContain(itemLore, isBuy?" §6§l- §f좌클릭 시 아이템을 판매합니다.":" §6§l- §f우클릭 시 아이템을 구매합니다.")){
                itemLore.remove(itemLore.size() - (isBuy?7:8));
                itemLore.remove(itemLore.size() - (isBuy?4:5));
            }
            else {
                for(int i=0; i<8; i++) itemLore.remove(itemLore.size() -1);
            }
        }
    }

    public static void removeShopLore(ItemStack itemStack) {
        if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasLore()) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            List<String> itemLore = itemStack.getItemMeta().getLore();

            removeLore(itemLore, true);
            removeLore(itemLore, false);

            itemMeta.setLore(itemLore);
            itemStack.setItemMeta(itemMeta);
        }
    }

    private static boolean isListContain(List<String> itemLore, String compareString) {
        boolean isContain = false;

        for (String s : itemLore) {
            if (s.contains(compareString)) {
                isContain = true;
                break;
            } else isContain = false;
        }
        return isContain;
    }
}
