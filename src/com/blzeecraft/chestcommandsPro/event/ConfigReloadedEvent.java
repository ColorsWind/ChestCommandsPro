package com.blzeecraft.chestcommandsPro.event;

import org.bukkit.event.HandlerList;
import org.bukkit.event.server.PluginEvent;
import org.bukkit.plugin.Plugin;


/**
 * 重载插件
 */
public class ConfigReloadedEvent extends PluginEvent {
	private static final HandlerList handlers = new HandlerList();

	public ConfigReloadedEvent(Plugin plugin) {
		super(plugin);
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}


}
