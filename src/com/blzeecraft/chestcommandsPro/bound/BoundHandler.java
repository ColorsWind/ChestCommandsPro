package com.blzeecraft.chestcommandsPro.bound;

import java.util.Map;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.blzeecraft.chestcommandsPro.Settings;
import com.gmail.filoghost.chestcommands.api.ChestCommandsAPI;

public class BoundHandler implements Listener {
	
	@EventHandler
	private void handle(PlayerDropItemEvent e)  {
		Map<Action, String> map = BoundConfig.get(e.getItemDrop().getItemStack().getType());
		if (map != null) {
			String s = map.get(Action.fromEvent(e));
			if (s != null) {
				if (! ChestCommandsAPI.openPluginMenu(e.getPlayer(), s)) {
					Settings.sendMessage(e.getPlayer(), "无法为您打开 " + s  + " 请联系管理员");
				}
				e.setCancelled(true);
			}
		}
	}  
	
	@EventHandler
	private void handle(PlayerInteractEvent e)  {
		Map<Action, String> map = BoundConfig.get(e.getMaterial());
		if (map != null) {
			String s = map.get(Action.fromEvent(e));
			if (s != null) {
				if (! ChestCommandsAPI.openPluginMenu(e.getPlayer(), s)) {
					Settings.sendMessage(e.getPlayer(), "无法为您打开 " + s  + " 请联系管理员");
				}
				e.setCancelled(true);
			}
		}
	}

}
