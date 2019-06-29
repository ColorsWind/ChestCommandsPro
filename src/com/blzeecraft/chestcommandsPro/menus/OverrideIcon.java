package com.blzeecraft.chestcommandsPro.menus;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.EnumMap;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import com.blzeecraft.chestcommandsPro.ChestCommandsPro;
import com.blzeecraft.chestcommandsPro.event.IClickHandler;

import com.gmail.filoghost.chestcommands.api.ClickHandler;
import com.gmail.filoghost.chestcommands.bridge.EconomyBridge;
import com.gmail.filoghost.chestcommands.bridge.PlayerPointsBridge;
import com.gmail.filoghost.chestcommands.config.AsciiPlaceholders;
import com.gmail.filoghost.chestcommands.exception.FormatException;
import com.gmail.filoghost.chestcommands.internal.CommandsClickHandler;
import com.gmail.filoghost.chestcommands.internal.icon.ExtendedIcon;
import com.gmail.filoghost.chestcommands.internal.icon.IconCommand;
import com.gmail.filoghost.chestcommands.serializer.CommandSerializer;
import com.gmail.filoghost.chestcommands.util.ItemStackReader;
import com.gmail.filoghost.chestcommands.util.Utils;
import com.gmail.filoghost.chestcommands.util.Validate;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;

@Data
@EqualsAndHashCode(callSuper = false)
public class OverrideIcon extends ExtendedIcon implements Comparable<OverrideIcon>, IClickHandler {

	private final EnumMap<ClickType, CommandsClickHandler> clickHandlers;
	private final ExtendIconPro icon;
	private final int order;
	private int vMoney;
	private int vPoint;
	private String vPerm;
	private String nbt;

	public OverrideIcon(ExtendIconPro icon, int order, ConfigurationSection section) {
		this.icon = icon;
		this.clickHandlers = new EnumMap<>(ClickType.class);
		this.order = order;
		apply(section);
	}

	private void apply(ConfigurationSection section) {
		Validate.notNull(section, "ConfigurationSection cannot be null");
		if (section.isSet("ID")) {
			this.readBasic(section);
			this.readCommands(section);
		}
		
	}

	private void readBasic(ConfigurationSection section) {
		String id = section.getString("ID");
		try {
			ItemStackReader reader = new ItemStackReader(id, true);
			this.setMaterial(reader.getMaterial());
			this.setDataValue(reader.getDataValue());
			this.setAmount(reader.getAmount());
		} catch (FormatException arg13) {
			ChestCommandsPro.getInstance().getLogger().warning("Invalid ID" + id);
		}
		if (section.isSet("DATA-VALUE")) {
			this.setDataValue((short) section.getInt("DATA-VALUE"));
		}

		if (section.isSet("AMOUNT")) {
			this.setAmount(section.getInt("AMOUNT"));
		} else {
			this.setAmount(1);
		}
		if (section.isSet("NBT")) {
			this.nbt = section.getString("NBT");
		}
		// 待更新
		this.setCloseOnClick(section.isSet("KEEP-OPEN") ? !section.getBoolean("KEEP-OPEN") : this.icon.closeOnClick());
		this.setName(section.isSet("NAME") ? AsciiPlaceholders.placeholdersToSymbols(Utils.colorizeName(section.getString("NAME"))) : this.getName());
		this.setLore(section.isSet("LORE") ? AsciiPlaceholders.placeholdersToSymbols(Utils.colorizeLore(section.getStringList("LORE"))) : this.getLore());
		this.vMoney = section.getInt("VIEW-MONEY");
		this.vPoint = section.getInt("VIEW-POINT");
		this.vPerm = section.getString("VIEW-PERMISSION");
		this.setMoneyPrice(section.getInt("MONEY"));
		this.setPlayerPointsPrice(section.getInt("POINT"));
		this.setPermission(section.getString("PERMISSION"));
		this.setPermissionMessage(section.getString("PERMISSION-MESSAGE"));
		
	}

	@SneakyThrows
	private String getName() {
		Class<?> clazz = Class.forName("com.gmail.filoghost.chestcommands.api.Icon");
		Field f = clazz.getDeclaredField("name");
		f.setAccessible(true);
		return (String) f.get(icon);
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
					commands.addAll(CommandSerializer.readCommands(section.getString(path)));
				}
				clickHandlers.put(type, new CommandsClickHandler(commands, closeOnClick));
			}
		}
		String path = "COMMAND";
		ArrayList<IconCommand> commands = new ArrayList<>();
		if (section.contains(path)) {
			if (section.isList(path)) {
				for (String command : section.getStringList(path)) {
					if (!command.isEmpty()) {
						commands.add(CommandSerializer.matchCommand(command));
					}
				}
			} else {
				commands.addAll(CommandSerializer.readCommands(section.getString(path)));
			}
			this.setClickHandler(new CommandsClickHandler(commands, closeOnClick));
		}

	}

	@Override
	public int compareTo(OverrideIcon o) {
		return Integer.compare(this.order, o.order);
	}

	public boolean match(Player player) {
		if (! this.canViewIcon(player)) {
			return false;
		}
		if (vMoney > 0 && !EconomyBridge.hasMoney(player, vMoney)) {
			return false;
		}
		if (vPoint > 0 && !PlayerPointsBridge.hasPoints(player, vPoint)) {
			return false;
		}
		return true;
	}

	@Override
	public ClickHandler getHandler(ClickType type) {
		return this.clickHandlers.get(type);
	}

}
