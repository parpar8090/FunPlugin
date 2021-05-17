package com.parpar8090.funplugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;
import io.netty.channel.Channel;
import net.minecraft.server.v1_14_R1.PacketPlayInEntityAction;
import net.minecraft.server.v1_14_R1.PacketPlayInSteerVehicle;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DismountEntity implements CommandExecutor, Listener {

	public static FunPlugin plugin = FunPlugin.getInstance();

	Material[] boats = new Material[]{
					Material.BIRCH_BOAT,
					Material.ACACIA_BOAT,
					Material.DARK_OAK_BOAT,
					Material.OAK_BOAT,
					Material.JUNGLE_BOAT,
					Material.SPRUCE_BOAT,
	};

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

@EventHandler
public void onDismount(EntityDismountEvent e){
	Entity dism = e.getDismounted();
	if(isEnabled && e.getEntity() instanceof Player && dism.isValid()){
		Player p = (Player)e.getEntity();

		if(dism instanceof Horse || dism instanceof Mule || dism instanceof Donkey) {
			AbstractHorse horse = (AbstractHorse) dism;

			if(dism instanceof Horse){
				if(horse.getInventory().getSaddle().getType() == Material.SADDLE) {
					if(horse.getInventory().getSaddle().getEnchantments().containsKey(Enchantment.BINDING_CURSE)){
						p.sendMessage("Has binding");
					}
				}
			}
			else if(dism instanceof Mule){
				if(horse.getInventory().getSaddle().getEnchantments().containsKey(Enchantment.BINDING_CURSE)) {
				}
			}
			else if(dism instanceof Donkey){
				if(horse.getInventory().getSaddle().getEnchantments().containsKey(Enchantment.BINDING_CURSE)) {
				}
			}
		}

		Vehicle v = (Vehicle) dism;
		if(dism instanceof Boat){
			if(v.getPersistentDataContainer().has(new NamespacedKey(plugin, "HasBinding"), PersistentDataType.INTEGER) &&
							v.getPersistentDataContainer().get(new NamespacedKey(plugin, "HasBinding"), PersistentDataType.INTEGER) == 1) {
			}
		}
		else if(dism instanceof Pig){
			if(v.getPersistentDataContainer().has(new NamespacedKey(plugin, "HasBinding"), PersistentDataType.INTEGER) &&
							v.getPersistentDataContainer().get(new NamespacedKey(plugin, "HasBinding"), PersistentDataType.INTEGER) == 1) {
			}
		}
	}
}

public void cancelPacket(){
	plugin.getProtocolManager().addPacketListener(new PacketAdapter(plugin, ListenerPriority.LOWEST, PacketType.Play.Client.STEER_VEHICLE) {
		@Override
		public void onPacketReceiving(PacketEvent e) {
			if (e.getPacketType() == PacketType.Play.Client.STEER_VEHICLE) {

				if (e.getPacket().getHandle() instanceof PacketPlayInSteerVehicle) {

					Field f = null;
					try {
						f = PacketPlayInSteerVehicle.class.getDeclaredField("d");
						f.setAccessible(true);
						f.set(e.getPacket().getHandle(), false);
						f.setAccessible(false);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}

			}
		}
	});
}

//Event for pigs and non-inventory entities
@EventHandler
public void onSaddle(PlayerInteractEntityEvent e){
	if(isEnabled){
		if(e.getRightClicked() instanceof Pig){
			Entity en = e.getRightClicked();
			Player p = e.getPlayer();

			p.sendMessage("Clicked pig");

			ItemStack item;
			if(p.getInventory().getItemInMainHand().getType() == Material.AIR) item = p.getInventory().getItemInOffHand();
			else item = p.getInventory().getItemInMainHand();

			p.sendMessage(item.getType().name());

			if(item.getType() == Material.SADDLE && item.getEnchantments().containsKey(Enchantment.BINDING_CURSE)){
				en.getPersistentDataContainer().set(new NamespacedKey(plugin, "HasBinding"), PersistentDataType.INTEGER, 1);
				p.sendMessage("Added key2");
			}
		}
	}
}

//Event only for boats
@EventHandler
public void onPlaceBoat(PlayerInteractEvent e){
	Player p = e.getPlayer();
	ItemStack item;
	if(p.getInventory().getItemInMainHand().getType() == Material.AIR) item = p.getInventory().getItemInOffHand();
	else item = p.getInventory().getItemInMainHand();
	List<Material> list = new ArrayList<>(Arrays.asList(boats));
	if(list.contains(item.getType()) && e.getAction() == Action.RIGHT_CLICK_BLOCK){
		if(item.getItemMeta().getEnchants().containsKey(Enchantment.BINDING_CURSE)){
			Boat boat = (Boat)p.getWorld().spawnEntity(
							p.getTargetBlock(null, 5).getLocation().add(0,1,0).
											setDirection(p.getLocation().getDirection()),
							EntityType.BOAT
			);

			switch(item.getType()){
				case OAK_BOAT: boat.setWoodType(TreeSpecies.GENERIC); //oak
				break;
				case ACACIA_BOAT: boat.setWoodType(TreeSpecies.ACACIA);
				break;
				case BIRCH_BOAT: boat.setWoodType(TreeSpecies.BIRCH);
				break;
				case DARK_OAK_BOAT: boat.setWoodType(TreeSpecies.DARK_OAK);
				break;
				case JUNGLE_BOAT: boat.setWoodType(TreeSpecies.JUNGLE);
				break;
				case SPRUCE_BOAT: boat.setWoodType(TreeSpecies.REDWOOD);
				break;
			}

			if(p.getGameMode() != GameMode.CREATIVE) item.setAmount(item.getAmount()-1); //Removes unwanted duplication
			boat.getPersistentDataContainer().set(new NamespacedKey(plugin, "HasBinding"), PersistentDataType.INTEGER, 1); //Set custom data
			e.setCancelled(true); //Cancels vanilla method of spawning boats
		}
	}
}

}
