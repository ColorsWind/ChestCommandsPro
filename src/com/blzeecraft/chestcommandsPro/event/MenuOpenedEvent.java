package com.blzeecraft.chestcommandsPro.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import com.gmail.filoghost.chestcommands.api.IconMenu;


/**
 * 玩家打开菜单
 */
public class MenuOpenedEvent extends PlayerEvent {
	private static final HandlerList handlers = new HandlerList();
	private final IconMenu menu;


	public MenuOpenedEvent(Player who, IconMenu menu) {
		super(who);
		this.menu = menu;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
	


	/**
	 * 获得玩家打开的菜单
	 * @return 玩家打开的菜单
	 */
	public IconMenu getMenu() {
		return menu;
	}


}
