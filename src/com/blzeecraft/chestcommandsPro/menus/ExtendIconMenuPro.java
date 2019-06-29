package com.blzeecraft.chestcommandsPro.menus;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.blzeecraft.chestcommandsPro.Settings;
import com.gmail.filoghost.chestcommands.api.IconMenu;
import com.gmail.filoghost.chestcommands.internal.ExtendedIconMenu;

import lombok.ToString;


@ToString
public class ExtendIconMenuPro extends ExtendedIconMenu {

	public ExtendIconMenuPro(ExtendedIconMenu menu) {
		super(menu.getTitle(), menu.getRows(), menu.getFileName());
		try {
			this.apply(menu);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void apply(ExtendedIconMenu menu) throws Exception {
		for(Field f : IconMenu.class.getDeclaredFields()) {
			f.setAccessible(true);
			f.set(this, f.get(menu));
		}
		for(Field f : ExtendedIconMenu.class.getDeclaredFields()) {
			f.setAccessible(true);
			f.set(this, f.get(menu));
		}
		
	}

	@Override
	public void open(Player player) {
		super.open(player);
		if (Settings.openEvent) {
			Bukkit.getPluginManager().callEvent(new com.blzeecraft.chestcommandsPro.event.MenuOpenedEvent(player, this));
		}
	}

	@Override
	public void refresh(Player player, Inventory inventory) {
		super.refresh(player, inventory);
		if(Settings.refreshEvent) {
			Bukkit.getPluginManager().callEvent(new com.blzeecraft.chestcommandsPro.event.MenuRefreshedEvent(player, this, inventory));
			
		}
	}
}
