package com.blzeecraft.chestcommandsPro.menus;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.blzeecraft.chestcommandsPro.ChestCommandsPro;
import com.gmail.filoghost.chestcommands.ChestCommands;
import com.gmail.filoghost.chestcommands.internal.ExtendedIconMenu;
import com.gmail.filoghost.chestcommands.internal.icon.ExtendedIcon;

public class MenuManager {

	private final HashMap<String, FileConfiguration> nameToConfig;
	private final File menuFolder;

	public MenuManager(ChestCommandsPro instance, ChestCommands cc) {
		nameToConfig = new HashMap<>();
		menuFolder = new File(cc.getDataFolder(), "menu");
	}

	private void initFileIndex() {
		nameToConfig.clear();
		searchFile(menuFolder);
	}

	private void searchFile(File file) {
		if (file.isDirectory()) {
			for (File subFile : file.listFiles()) {
				searchFile(subFile);
			}
		} else {
			try {
				nameToConfig.put(file.getName(), YamlConfiguration.loadConfiguration(file));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void refreshMenus() {
		initFileIndex();
		Map<String, ExtendedIconMenu> fileToMenu = ChestCommands.getFileNameToMenuMap();
		for (Entry<String, ExtendedIconMenu> en : fileToMenu.entrySet()) {
			ExtendedIconMenu menu = en.getValue();
			en.setValue(new ExtendIconMenuPro(menu));
			refreshIcon(menu);
		}
	}

	public void refreshIcon(ExtendedIconMenu menu) {
		FileConfiguration config = nameToConfig.get(menu.getFileName());
		for (String path : config.getKeys(false)) {
			if ("menu-settings".equalsIgnoreCase(path)) {
				continue;
			}
			ConfigurationSection section = config.getConfigurationSection(path);
			int x = section.getInt("POSITION-X");
			int y = section.getInt("POSITION-Y");
			ExtendIconPro icon = new ExtendIconPro((ExtendedIcon) menu.getIcon(x, y));
			icon.readExtend(section);
			menu.setIcon(x, y, icon);
		}
	}

}
