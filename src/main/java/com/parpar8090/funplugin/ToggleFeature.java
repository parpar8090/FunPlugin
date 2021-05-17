package com.parpar8090.funplugin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public abstract class ToggleFeature implements CommandExecutor {

boolean isEnabled = false;

@Override
public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
	if(!isEnabled){
		this.isEnabled = true;
		sender.sendMessage(ChatColor.GREEN + "Enabled Un-Dismountable Entities!");
	} else {
		this.isEnabled = false;
		sender.sendMessage(ChatColor.RED + "Disabled Un-Dismountable Entities!");
	}
	return true;
}

}
