package com.blzeecraft.chestcommandsPro.menus;

import org.bukkit.entity.Player;

import com.gmail.filoghost.chestcommands.api.ClickHandler;
import com.gmail.filoghost.chestcommands.api.Icon;

public class ExtendCommandTaskPro implements Runnable {
	private final ClickHandler extraHandler;
	private final Player p;
	private final Icon icon;

	@Override
	public void run() {
		if (p.isOnline()) {
			
			boolean close = icon.onClick(p);
			if (extraHandler != null) {
				extraHandler.onClick(p);
			}
			if (close) {
				p.closeInventory();
			}
		}
	}
	
	public ExtendCommandTaskPro(Player p, Icon icon) {
		this(p, icon, null);
	}

	public ExtendCommandTaskPro(Player p, Icon icon, ClickHandler handler) {
		this.icon = icon;
		this.p = p;
		this.extraHandler = handler;
	}

}
