package com.appletalk.dailyPlugin;

import com.appletalk.dailyPlugin.commands.*;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class dailyShopCommandHandler implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        AbstractCommand cmd = new HelpCommand(sender);

        // No args, show help.
        if (args.length == 0) {
            cmd.execute(sender, command, label, args);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "도움말":
                cmd = new HelpCommand(sender);
                break;
            case "열기":
                cmd = new OpenCommand(sender);
                break;
            case "생성":
                cmd = new CreateCommand(sender);
                break;
            case "삭제":
                cmd = new DeleteCommand(sender);
                break;
            case "목록":
                cmd = new ListCommand(sender);
                break;
            case "설정":
                cmd = new SetCommand(sender);
                break;
        }

        cmd.execute(sender, command, label, args);
        return true;

    }

}