package com.blzeecraft.chestcommandsPro.builder;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.blzeecraft.chestcommandsPro.ChestCommandsPro;
import com.blzeecraft.chestcommandsPro.Settings;
import com.blzeecraft.chestcommandsPro.menus.ExtendIconPro;
import com.blzeecraft.chestcommandsPro.utils.NBTUtils;
import com.blzeecraft.chestcommandsPro.utils.ReflectUtils;

import lombok.Getter;

public class MenuBuilder {
	// private final ChestCommandsPro pl;
	@Getter
	private final File dir;

	@Getter
	private final MenuHandler handler;

	public MenuBuilder(ChestCommandsPro pl) {
		// this.pl = pl;
		this.handler = new MenuHandler(this);
		this.dir = new File(pl.getDataFolder(), "menus");
		checkDir();
		Bukkit.getPluginManager().registerEvents(handler, pl);

	}

	public void checkDir() {
		if (dir.exists()) {
			dir.mkdir();
		}
	}

	public Inventory createInventory(Player p) {
		return this.createInventory(p, new MenuHolder());
	}

	public Inventory createInventory(Player p, MenuHolder holder) {
		Inventory inv = Bukkit.createInventory(holder, 54, "Editor");
		return inv;
	}

	public Inventory createInventory(Player p, String name) {
		Inventory inv = this.createInventory(p, new MenuHolder(name));
		name = fillName(name);
		File f = new File(dir, name);
		if (f.exists()) {
			FileConfiguration config = YamlConfiguration.loadConfiguration(f);
			for (String path : config.getKeys(false)) {
				if (! "menu-settings".equalsIgnoreCase(path)) {
					ConfigurationSection section = config.getConfigurationSection(path);
					com.gmail.filoghost.chestcommands.internal.icon.ExtendedIcon icon = (com.gmail.filoghost.chestcommands.internal.icon.ExtendedIcon) com.gmail.filoghost.chestcommands.serializer.IconSerializer
							.loadIconFromSection(section, null, null, null);
					// qaq+
					ExtendIconPro pro = new ExtendIconPro(icon);
					pro.readExtend(section);
					int x = section.getInt("POSITION-X");
					int y = section.getInt("POSITION-Y");
					int slot = ((y - 1) * 9) + x - 1;
					inv.setItem(slot, pro.createItemstack(p));
				}
			}
		}
		return inv;
	}

	public String saveInventory(Inventory inv, String name) {
		name = fillName(name);
		File f = new File(dir, name);
		FileConfiguration cs = YamlConfiguration.loadConfiguration(dir);
		ItemStack[] contents = inv.getContents();
		for (int i = 0; i < contents.length; i++) {
			if (contents[i] != null) {
				int y = (i / 9) + 1;
				int x = (i % 9) + 1;
				ConfigurationSection sub = cs.createSection("X" + x + "Y" + y);
				ItemStack item = contents[i];
				ItemMeta meta = item.getItemMeta();
				sub.set("ID", item.getType().name());
				sub.set("DATA-VALUE", item.getDurability());
				sub.set("AMOUNT", item.getAmount());
				if (meta != null) {
					if (meta.hasDisplayName()) {
						sub.set("NAME", meta.getDisplayName());
					}
					if (meta.hasLore()) {
						sub.set("LORE", meta.getLore());
					}
				}
				if (Settings.supportNbt) {
					sub.set("NBT", NBTUtils.itemNBTtoText(item));
				}
				sub.set("POSITION-X", x);
				sub.set("POSITION-Y", y);
				sub.set("KEEP-OPEN", true);
			}
		}
		saveMenus(cs, f);
		return name;
	}

	public void closeAll() {
		for (Player p : ReflectUtils.getPlayerOnline()) {
			Inventory inv = p.getOpenInventory().getTopInventory();
			if (inv != null) {
				InventoryHolder holder = inv.getHolder();
				if (holder != null && (holder instanceof com.gmail.filoghost.chestcommands.internal.MenuInventoryHolder
						|| holder instanceof MenuHolder)) {
					p.closeInventory();
				}
			}
		}
	}

	private String fillName(String name) {
		if (name == null || name.isEmpty()) {
			name = String.valueOf(System.currentTimeMillis());
		}
		if (!name.toLowerCase().endsWith(".yml")) {
			name = name + ".yml";
		}
		return name;
	}

	private void saveMenus(FileConfiguration cs, File f) {
		try {
			cs.save(f);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

}
