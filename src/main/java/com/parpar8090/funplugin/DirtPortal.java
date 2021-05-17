package com.parpar8090.funplugin;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.EndPortalFrame;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DirtPortal implements Listener, CommandExecutor {
	boolean isEnabled = false;

@Override
public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
	if(!isEnabled){
		this.isEnabled = true;
		sender.sendMessage(ChatColor.GREEN + "Enabled Dirt Portals!");
	} else {
		this.isEnabled = false;
		sender.sendMessage(ChatColor.RED + "Disabled Dirt Portals!");
	}
	return true;
}

@EventHandler
public void onInteract(PlayerInteractEvent e){
	ItemStack held = e.getPlayer().getInventory().getItemInMainHand();
	Material[] hoes = new Material[]{
					Material.WOODEN_HOE,
					Material.STONE_HOE,
					Material.IRON_HOE,
					Material.GOLDEN_HOE,
					Material.DIAMOND_HOE,
	};
	List<Material> list = new ArrayList<>(Arrays.asList(hoes));

	if(isEnabled && e.getAction() == Action.RIGHT_CLICK_BLOCK && list.contains(held.getType())){
		Player p = e.getPlayer();
		Block b = p.getTargetBlock(null, 5);
		b.setType(Material.END_PORTAL_FRAME);
		EndPortalFrame portal = (EndPortalFrame) b.getBlockData();

		BlockFace face = p.getFacing().getOppositeFace();
		portal.setFacing(face);
		b.setBlockData(portal);

	}
}
}
