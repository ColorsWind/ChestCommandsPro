package com.blzeecraft.chestcommandsPro.commands;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.blzeecraft.chestcommandsPro.ChestCommandsPro;
import com.blzeecraft.chestcommandsPro.builder.MenuBuilder;

public class CommandBuilder {
	private final MenuBuilder builder;

	public CommandBuilder(ChestCommandsPro pl, CommandHandler commandHandler) {
		builder = pl.getBuilder();
	}

	public void execute(Player p) {
		Inventory inv = builder.createInventory(p);
		p.closeInventory();
		p.openInventory(inv);
		
	}
	
	public void execute(Player p, String name) {
		Inventory inv = builder.createInventory(p, name);
		p.closeInventory();
		p.openInventory(inv);
		
	}

}
