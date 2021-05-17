package com.parpar8090.funplugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public class BucketEntity implements Listener {

	@EventHandler
public void onInteract(PlayerInteractEntityEvent e){

		Player p = e.getPlayer();
		EntityType ent = e.getRightClicked().getType();

		PlayerInventory inv = p.getInventory();

		for(int i = 0; i < EntityType.values().length; i++){
			if(ent == EntityType.values()[i]) convertItem(p, ent);
		}
	}

	//Makes waterbucket with fish inside using custom model data
	public void convertItem(Player p, EntityType to){
		for(int i = 0; i < EntityType.values().length; i++){
			if(to == EntityType.values()[i]){

				PlayerInventory inv = p.getInventory();
				ItemStack item = new ItemStack(Material.WATER_BUCKET);
				if(inv.getItemInMainHand().getType() == Material.WATER_BUCKET && !inv.getItemInMainHand().getItemMeta().hasCustomModelData()){
					inv.setItem(inv.getHeldItemSlot(), item);
				} else if(inv.getItemInOffHand().getType() == Material.WATER_BUCKET && !inv.getItemInOffHand().getItemMeta().hasCustomModelData()) {
					inv.setItem(-106, item);
				} else return;

				ItemMeta meta = item.getItemMeta();
				meta.setCustomModelData(-1-i);
				item.setItemMeta(meta);

			}
		}
	}


	@EventHandler
	public void onPlaceBucket(PlayerInteractEvent e){
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK){
			BlockFace face = e.getBlockFace();
			Player p = e.getPlayer();

			Block rel = e.getClickedBlock().getRelative(face); //Get the block
			Location loc = rel.getLocation();

			ItemMeta item = p.getInventory().getItem(getPrioritySlot(p.getInventory())).getItemMeta();

			//rel.setType(Material.WATER); //TODO check if needs to be uncommented

			for(int i = 0; i < EntityType.values().length; i++){
				//Check if not match
				if(item.hasCustomModelData() ||
								item.getCustomModelData() == -1-i) return;

				//if do match, spawn the entity at loc
				loc.getWorld().spawnEntity(loc, EntityType.values()[i]).getType();
			}
		}
	}

	public int getPrioritySlot(PlayerInventory inv){
		if(inv.getItemInMainHand().getType() == Material.WATER_BUCKET && !inv.getItemInMainHand().getItemMeta().hasCustomModelData()){
			return inv.getHeldItemSlot();
		} else if(inv.getItemInOffHand().getType() == Material.WATER_BUCKET && !inv.getItemInOffHand().getItemMeta().hasCustomModelData()) {
			return -106;
		}
		return -1;
	}

}
