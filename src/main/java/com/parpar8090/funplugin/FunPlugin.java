package com.parpar8090.funplugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.plugin.java.JavaPlugin;

public class FunPlugin extends JavaPlugin {

	private static FunPlugin instance;
private ProtocolManager protocolManager;

@Override
public void onEnable() {
	setInstance(this);
	protocolManager = ProtocolLibrary.getProtocolManager();
	DirtPortal dirtPortalClass = new DirtPortal();
	DismountEntity dismClass = new DismountEntity();
	System.out.println("FunPlugin Enabled!");
	getCommand("toggledirtportal").setExecutor(dirtPortalClass);
	getServer().getPluginManager().registerEvents(dirtPortalClass, this);
	getCommand("dismount").setExecutor(dismClass);
	getServer().getPluginManager().registerEvents(dismClass, this);
}

@Override
public void onDisable() {
	System.out.println("FunPlugin Disabled!");
}

public static FunPlugin getInstance(){return instance;}

private static void setInstance(FunPlugin instance){FunPlugin.instance = instance;}

public ProtocolManager getProtocolManager(){return protocolManager;}

}
