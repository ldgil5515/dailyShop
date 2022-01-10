package com.appletalk.dailyPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;


public class dailyShopTabCompleter implements TabCompleter {
    /**
     * Valid command names.
     */
    private static final String[] COMMANDS = {"도움말", "열기", "목록", "생성", "삭제", "설정"};
    private static final String[] SETTINGS = {"아이템", "줄", "gui"};

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        if (sender.isOp() || sender.hasPermission("dailyShop.admin") || sender.hasPermission("dailyShop." + args[0])) {

            List<String> completions = new ArrayList<>();

            if (args.length == 1) {
                String partialCommand = args[0];
                List<String> commands = new ArrayList<>(Arrays.asList(COMMANDS));
                StringUtil.copyPartialMatches(partialCommand, commands, completions);
            }
            else if (args.length == 2){
                String partialCommand = args[1];
                if (args[0].equals("열기") || args[0].equals("삭제") || args[0].equals("설정")){
                    StringUtil.copyPartialMatches(partialCommand, dailyShopPlugin.getInstance().shopMap.keySet(), completions);
                }
            }
            else if (args.length == 3 && args[0].equals("설정")){
                String partialCommand = args[2];
                List<String> commands = new ArrayList<>(Arrays.asList(SETTINGS));
                StringUtil.copyPartialMatches(partialCommand, commands, completions);
            }

            Collections.sort(completions);
            return completions;
        }
        return null;
    }
}