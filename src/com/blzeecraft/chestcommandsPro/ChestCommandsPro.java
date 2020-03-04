package com.blzeecraft.chestcommandsPro;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration; 
import org.bukkit.plugin.java.JavaPlugin;

import com.blzeecraft.chestcommandsPro.bound.BoundConfig;
import com.blzeecraft.chestcommandsPro.bound.BoundHandler;
import com.blzeecraft.chestcommandsPro.builder.MenuBuilder;
import com.blzeecraft.chestcommandsPro.commands.CommandHandler;
import com.blzeecraft.chestcommandsPro.injector.Injector;
import com.blzeecraft.chestcommandsPro.menus.MenuManager;
import com.gmail.filoghost.chestcommands.ChestCommands;

import lombok.Getter;

@Getter
public class ChestCommandsPro extends JavaPlugin {


	@Getter
	private static ChestCommandsPro instance = null;
	
	
	private ChestCommands chestCommands;
	private MenuManager menuManager;
	private CommandHandler executor;
	private Injector injector;
	private MenuBuilder builder;
	private BoundHandler bHandler;
	private Metrics metrics;


	@Override
	public void onEnable() {
		instance = this;
		if (PlayerPointsBridge.setupPlugin()) {
			this.getLogger().info("找到 PlayerPoints.");
		}
		chestCommands = JavaPlugin.getPlugin(ChestCommands.class);
		initConfig();
		menuManager = new MenuManager(this, chestCommands);
		bHandler = new BoundHandler();
		builder = new MenuBuilder(this);
		executor = new CommandHandler(this);
		menuManager.refreshMenus(); //must load before inject
		injector = new Injector(this, chestCommands);
		if (Settings.listenerInjector) {
			injector.inject();
		}
		this.metrics = new Metrics(this);
		this.getServer().getPluginManager().registerEvents(bHandler, this);
		this.getCommand("chestcommandspro").setExecutor(executor);
		loadExample();
	}
	
	
	private void initConfig() {
		try {
			File file = new File(getDataFolder(), "config.yml");
			if (!file.exists()) {
				this.saveDefaultConfig();
			} else {
				Settings.read(YamlConfiguration.loadConfiguration(file));
			}
			file = new File(getDataFolder(), "bound.yml");
			if (!file.exists()) {
				this.saveResource("bound.yml", false);
			} else {
				BoundConfig.read(YamlConfiguration.loadConfiguration(file));
			} 
		} catch (Exception e) {
			this.getLogger().warning("An error occur while reading config, please type /ccp reload after you fix it");
			e.printStackTrace();
		}
	}
	
	public void loadExample() {
		File file = new File(getDataFolder(), "example.yml");
		if (! file.exists()) {
			this.saveResource("example.yml", false);
			File file2 = new File("./plugins/ChestCommands/menu/example_CCP.yml");
			if (! file2.exists()) {
				file.renameTo(file2);
				Settings.sendMessage(Bukkit.getConsoleSender(), "Generate example into ./ChestCommands/menu");
				this.executor.getCmdReload().execute(Bukkit.getConsoleSender());
			}
		}
	}

	@Override
	public void onDisable() {
		this.builder.closeAll();
	}

	public void reload() {
		initConfig();
		this.menuManager.refreshMenus();
		this.builder.closeAll();
		if (Settings.configEvent) {
			Bukkit.getPluginManager().callEvent(new com.blzeecraft.chestcommandsPro.event.ConfigReloadedEvent(this));
		}
		
	}
	

}
