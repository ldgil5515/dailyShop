package com.appletalk.dailyPlugin.api;

import com.appletalk.dailyPlugin.dailyShopPlugin;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class NBTHandler {

     public static void removeNBT(ItemStack itemStack, String key) {
        if(itemStack != null){
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.getPersistentDataContainer().remove(new NamespacedKey(dailyShopPlugin.getInstance(), key));
            itemStack.setItemMeta(itemMeta);
        }
    }

    public static void removeListNBT(List<ItemStack> itemStack, String key) {
        for (ItemStack stack : itemStack) {
            removeNBT(stack, key);
        }
    }
}
