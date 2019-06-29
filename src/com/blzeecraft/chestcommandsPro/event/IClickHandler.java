package com.blzeecraft.chestcommandsPro.event;

import org.bukkit.event.inventory.ClickType;

import com.gmail.filoghost.chestcommands.api.ClickHandler;

public interface IClickHandler {
	
	
	public ClickHandler getHandler(ClickType type);
	

}
