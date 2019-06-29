package com.blzeecraft.chestcommandsPro.bound;

import java.util.EnumMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class BoundConfig {
	
	public static Map<Material, Map<Action, String>> bounds;
	
	public static void read(FileConfiguration config) {
		bounds = new EnumMap<>(Material.class);
		for(String path : config.getKeys(false)) {
			ConfigurationSection cs = config.getConfigurationSection(path);
			EnumMap<Action, String> map = new EnumMap<>(Action.class);
			bounds.put(Material.valueOf(path), map);
			for(String sub : cs.getKeys(false)) {
				map.put(Action.valueOf(sub), cs.getString(sub) + ".yml");
			}
		}
	}
	
	public static String get(Material type, Action action) {
		Map<Action, String> map = bounds.get(type);
		if (map != null) {
			map.get(action);
		}
		return null;
	}
	
	public static Map<Action, String> get(Material m) {
		return bounds.get(m);
	}

}
