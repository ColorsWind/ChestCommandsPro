package com.blzeecraft.chestcommandsPro.injector;

import com.blzeecraft.chestcommandsPro.ChestCommandsPro;
import com.blzeecraft.chestcommandsPro.Settings;
import com.blzeecraft.chestcommandsPro.event.IClickHandler;
import com.blzeecraft.chestcommandsPro.menus.ExtendCommandTaskPro;
import com.blzeecraft.chestcommandsPro.menus.ExtendIconPro;
import com.blzeecraft.chestcommandsPro.menus.OverrideIcon;
import com.gmail.filoghost.chestcommands.ChestCommands;
import com.gmail.filoghost.chestcommands.api.ClickHandler;
import com.gmail.filoghost.chestcommands.api.Icon;
import com.gmail.filoghost.chestcommands.api.IconMenu;
import com.gmail.filoghost.chestcommands.internal.CommandsClickHandler;
import com.gmail.filoghost.chestcommands.internal.MenuInventoryHolder;
import com.gmail.filoghost.chestcommands.listener.InventoryListener;

import gnu.trove.map.TObjectLongMap;
import gnu.trove.map.hash.TObjectLongHashMap;

import java.lang.reflect.Field;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.InventoryHolder;

public class InventoryHandler implements Listener {
	private final TObjectLongMap<Player> antiSpam;
	private final InventoryListener listener;
	private Field f;

	public InventoryHandler(ChestCommandsPro pro, InventoryListener listener) {
		antiSpam = new TObjectLongHashMap<>();
		this.listener = listener;
		try {
			Class<?> clazz = Class.forName("com.gmail.filoghost.chestcommands.internal.CommandsClickHandler");
			f = clazz.getDeclaredField("commands");
			f.setAccessible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void handle(PlayerInteractEvent e) {
		listener.onInteract(e);
	}


	@EventHandler(priority = EventPriority.HIGHEST)
	public void handle(InventoryClickEvent e) {
		InventoryHolder holder = e.getInventory().getHolder();
		if (holder != null && holder instanceof MenuInventoryHolder) {
			e.setCancelled(true);
			int slot = e.getRawSlot();
			if (slot >=0 && slot == e.getSlot() && e.getInventory().getItem(slot) != null) {
				Player clicker = (Player) e.getWhoClicked();
				if (!isSpam(clicker)) {
					IconMenu menu = ((MenuInventoryHolder) holder).getIconMenu();
					//可能返回ExtendIconPro，OverrideIcon，etc
					Icon icon = menu.getIconRaw(slot);
					if (icon instanceof ExtendIconPro) {
						OverrideIcon o = ((ExtendIconPro) icon).getOverrideIcon(clicker);
						if (o != null) {
							icon = o;
						}
					}
					ClickHandler handler = null;
					if (icon instanceof IClickHandler) {
						//传递参数，ExtendCommandTaskPro 会处理null，自带的command，etc
						handler = ((IClickHandler) icon).getHandler(e.getClick());
					} else {
						handler = null;
					}
					Bukkit.getScheduler().scheduleSyncDelayedTask(ChestCommands.getInstance(),
							new ExtendCommandTaskPro(clicker, icon, handler));

				}
			}
		}

	}


	@Deprecated
	public boolean isRun(ClickHandler handler) {
		if (handler instanceof CommandsClickHandler) {
			try {
				List<?> commands = (List<?>) f.get(handler);
				if (commands != null && ! commands.isEmpty()) {
					return true;
				}
			} catch (Exception e) {
			}
		}
		return true;
	}

	private boolean isSpam(Player clicker) {
		long current = System.currentTimeMillis();
		if (antiSpam.get(clicker) - current >= Settings.antiSpam) {
			antiSpam.put(clicker, current);
			return true;
		}
		return false;
	}

	@EventHandler
	public void handle(PlayerQuitEvent e) {
		antiSpam.remove(e.getPlayer());
	}
}
