package com.blzeecraft.chestcommandsPro.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.Inventory;

import com.allatori.annotations.DoNotRename;
import com.gmail.filoghost.chestcommands.api.IconMenu;

/**
 * 刷新菜单物品
 */
@DoNotRename
public class MenuRefreshedEvent extends PlayerEvent {
	private static final HandlerList handlers = new HandlerList();
	private final IconMenu menu;
	private final Inventory inventory;


	public MenuRefreshedEvent(Player who, IconMenu menu, Inventory inventory) {
		super(who);
		this.menu = menu;
		this.inventory = inventory;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	@DoNotRename
	public static HandlerList getHandlerList() {
		return handlers;
	}


	/**
	 * 获得玩家打开的背包
	 * @return 玩家打开的背包
	 */
	@DoNotRename
	public Inventory getInventory() {
		return inventory;
	}

	/**
	 * 获得玩家打开的菜单
	 * @return 玩家打开的菜单
	 */
	@DoNotRename
	public IconMenu getMenu() {
		return menu;
	}

}
