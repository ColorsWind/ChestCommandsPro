package com.blzeecraft.chestcommandsPro.menus;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.TreeSet;
import java.util.regex.Pattern;

import com.blzeecraft.chestcommandsPro.utils.Fallback;
import com.gmail.filoghost.chestcommands.ChestCommands;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.blzeecraft.chestcommandsPro.ChestCommandsPro;
import com.blzeecraft.chestcommandsPro.event.IClickHandler;
import com.blzeecraft.chestcommandsPro.utils.NBTUtils;
import com.gmail.filoghost.chestcommands.api.ClickHandler;
import com.gmail.filoghost.chestcommands.api.Icon;
import com.gmail.filoghost.chestcommands.internal.CommandsClickHandler;
import com.gmail.filoghost.chestcommands.internal.icon.ExtendedIcon;
import com.gmail.filoghost.chestcommands.internal.icon.IconCommand;
import com.gmail.filoghost.chestcommands.serializer.CommandSerializer;

import lombok.ToString;
import me.clip.placeholderapi.PlaceholderAPI;


@ToString
public class ExtendIconPro extends ExtendedIcon implements IClickHandler {
	private final EnumMap<ClickType, CommandsClickHandler> clickHandlers;
	private final TreeSet<OverrideIcon> overrides;
	private boolean usePlaceHolder;
	private String nbt;

	/**
	 * note 如果row过多，会抛出npe
	 * 
	 * @param icon
	 */
	public ExtendIconPro(ExtendedIcon icon) {
		this.clickHandlers = new EnumMap<>(ClickType.class);
		this.overrides = new TreeSet<>();
		try {
			this.apply(icon);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean closeOnClick() {
		return this.closeOnClick;
	}

	@Override
	public ItemStack createItemstack(Player pov) {
		OverrideIcon o = this.getOverrideIcon(pov);
		ItemStack item;
		if (o == null) {
			item = super.createItemstack(pov);
		} else {
			item = o.createItemstack(pov);
		}
		if (NBTUtils.isVaidNBT(nbt)) {
			String name = item.getItemMeta().getDisplayName();
			List<String> lore = item.getItemMeta().getLore();
			item = NBTUtils.setItemNBT(item, nbt);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(name);
			meta.setLore(lore);
			item.setItemMeta(meta);
		}
		return item;
	}

	public OverrideIcon getOverrideIcon(Player player) {
		for (OverrideIcon o : this.overrides.descendingSet()) {
			if (o.match(player)) {
				return o;
			}
		}
		return null;
	}

	@Override
	public String calculateName(Player pov) {
		String name = super.calculateName(pov);
		if (usePlaceHolder && name != null) {
			return PlaceholderAPI.setPlaceholders(pov, name);
		}
		return name;
	}

	@Override
	public List<String> calculateLore(Player pov) {
		List<String> lore = super.calculateLore(pov);
		if (usePlaceHolder && lore != null) {
			return PlaceholderAPI.setPlaceholders(pov, lore);
		}
		return lore;

	}

	private void apply(ExtendedIcon icon) throws Exception {
		for (Field f : Icon.class.getDeclaredFields()) {
			f.setAccessible(true);
			f.set(this, f.get(icon));
		}
		for (Field f : ExtendedIcon.class.getDeclaredFields()) {
			f.setAccessible(true);
			f.set(this, f.get(icon));
		}
	}

	// 读取本插件增加的内容
	public void readExtend(ConfigurationSection section) {
		readCommands(section);
		readNBT(section);
		if (section.isSet("OVERRIDE")) {
			readOverride(section.getConfigurationSection("OVERRIDE"));
		}
		readPapi(section);

	}

	private void readOverride(ConfigurationSection section) {
		int i = -1;
		for (String path : section.getKeys(false)) {
			try {
				i = Integer.parseInt(path);
			} catch (NumberFormatException e) {
				ChestCommandsPro.getInstance().getLogger().warning(path + "is not a invalid number");
				e.printStackTrace();
			}
			overrides.add(new OverrideIcon(this, i, section.getConfigurationSection(path)));
		}

	}

	private void readNBT(ConfigurationSection section) {
		this.nbt = section.getString("NBT");

	}

	private void readCommands(ConfigurationSection section) {
		for (ClickType type : ClickType.values()) {
			String path = type.name();
			ArrayList<IconCommand> commands = new ArrayList<>();
			if (section.contains(path)) {
				if (section.isList(path)) {
					for (String command : section.getStringList(path)) {
						if (!command.isEmpty()) {
							commands.add(CommandSerializer.matchCommand(command));
						}
					}
				} else {
					commands.addAll(Fallback.readCommands(section.getString(path)));
				}
				this.clickHandlers.put(type, new CommandsClickHandler(commands, closeOnClick));
			}
		}
	}



	
	private void readPapi(ConfigurationSection section) {
		usePlaceHolder = section.getBoolean("PLACEHOLDERAPI")
				&& Bukkit.getPluginManager().isPluginEnabled("PlaceHolderAPI");
	}

	public ClickHandler getHandler() {
		return this.getClickHandler();
	}

	@Override
	public ClickHandler getHandler(ClickType type) {
		return clickHandlers.get(type);
	}

}
