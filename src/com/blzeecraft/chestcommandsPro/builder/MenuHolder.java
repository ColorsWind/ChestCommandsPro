package com.blzeecraft.chestcommandsPro.builder;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import lombok.Data;

@Data
public class MenuHolder implements InventoryHolder {
	private final String name;

	@Override
	public Inventory getInventory() {
		return null;
	}

	public MenuHolder() {
		this(null);
	}

	public MenuHolder(String name) {
		super();
		this.name = name;
	}
	
	

}
