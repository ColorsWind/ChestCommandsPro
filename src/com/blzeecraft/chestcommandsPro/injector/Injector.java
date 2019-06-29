package com.blzeecraft.chestcommandsPro.injector;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.RegisteredListener;

import com.blzeecraft.chestcommandsPro.ChestCommandsPro;
import com.gmail.filoghost.chestcommands.ChestCommands;
import com.gmail.filoghost.chestcommands.listener.InventoryListener;

import lombok.Getter;
import lombok.SneakyThrows;

public class Injector {
	
	public Injector(ChestCommandsPro pl, ChestCommands cc) {
		super();
		this.pl = pl;
		this.cc = cc;
	}

	private final ChestCommandsPro pl;
	private final ChestCommands cc;
	
	@Getter
	private InventoryHandler handler;
	
	
	@SneakyThrows
	public boolean inject() {
		ArrayList<RegisteredListener> list = HandlerList.getRegisteredListeners(cc);
		for(int i=0;i<list.size();i++) {
			if (list.get(i).getListener() instanceof InventoryListener) {
				 InventoryListener listener = (InventoryListener) list.get(i).getListener();
				 HandlerList.unregisterAll(listener);
				 handler = new InventoryHandler(pl, listener);
				 Bukkit.getPluginManager().registerEvents(handler, pl);
				 return true;
			}
		}
		return false;
	}
	

}
