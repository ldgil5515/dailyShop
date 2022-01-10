package com.appletalk.dailyPlugin.messaging;

import org.bukkit.ChatColor;

import java.util.List;

public final class MessageFormatter {

    private final MessageFile messageFile;
    public MessageFormatter() {
        this.messageFile = new MessageFile("messages.yml");
    }

    public String format(String key, Object... args) {
        return format(true, key, args);
    }

    /**
     * Returns the formatted version of the message.
     *
     * @param prefix whether to prepend with the plugin's prefix
     * @param key    the key
     * @param args   the args to replace
     * @return the formatted String
     */
    public String format(boolean prefix, String key, Object... args) {
        String message = prefix ? messageFile.get("prefix") + messageFile.get(key) : messageFile.get(key);
        for (int i = 0; i < args.length; i++)
            message = message.replace("{" + i + "}", String.valueOf(args[i]));
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    /**
     * Add the prefix to a message.
     *
     * @param msg the message.
     * @return the message with the prefix.
     */
    public static String prefix(String msg) {
        MessageFormatter messageformatter = new MessageFormatter();
        return ChatColor.translateAlternateColorCodes('&', messageformatter.messageFile.get("prefix") + msg);
    }

    public static String hasBase(String msg, String firstValue, String secondValue) {
        char lastName = msg.charAt(msg.length() - 1);

        if (lastName < 0xAC00 || lastName > 0xD7A3) {
            return firstValue;
        }

        String seletedValue = (lastName - 0xAC00) % 28 > 0 ? firstValue : secondValue;
        return seletedValue;
    }

    /**
     * Returns the message configuration.
     *
     * @return the message configuration.
     */
    public MessageFile getMessageFile() {
        return messageFile;
    }
}
