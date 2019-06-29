package com.blzeecraft.chestcommandsPro;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import net.md_5.bungee.api.ChatColor;

public class Settings {

	public static boolean commandInjector = true;

	public static boolean listenerInjector = true;

	public static long antiSpam = 200L;

	public static boolean reloadChestCommand = true;

	public static boolean supportNbt = true;
	
	public static boolean configEvent = true;
	
	public static boolean openEvent = true;
	
	public static boolean refreshEvent = true;

	public static void read(FileConfiguration config) {
		commandInjector = config.getBoolean("CommandInjector", commandInjector);
		listenerInjector = config.getBoolean("ListenerInjector", listenerInjector);
		antiSpam = config.getLong("AntiSpam", antiSpam);
		reloadChestCommand = config.getBoolean("ReloadChestCommand", reloadChestCommand);
		supportNbt = config.getBoolean("SupportNBT", supportNbt)
				&& Bukkit.getPluginManager().isPluginEnabled("ProtocolLib");
		configEvent = config.getBoolean("PluginEvent.ConfigEvent", configEvent);
		openEvent = config.getBoolean("PluginEvent.ConfigEvent", openEvent);
		refreshEvent = config.getBoolean("PluginEvent.ConfigEvent", refreshEvent);
	}

	public static final String PREFIX = ChatColor.AQUA + "[" + ChatColor.LIGHT_PURPLE + "ChestCommandsPro" + ChatColor.AQUA
			+ "] ";

	public static void sendMessage(CommandSender sender, String message) {
		sender.sendMessage(new StringBuilder(PREFIX).append(message).toString());
	}
	
}
