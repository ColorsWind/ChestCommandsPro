package com.blzeecraft.chestcommandsPro.commands;

import org.bukkit.command.CommandSender;

import com.blzeecraft.chestcommandsPro.ChestCommandsPro;
import com.blzeecraft.chestcommandsPro.Settings;

import lombok.Getter;

@Getter
public class CommandReload {
	public CommandReload(ChestCommandsPro pl, CommandHandler commandHandler) {
		super();
		this.pl = pl;
		this.commandHandler = commandHandler;
	}



	private final ChestCommandsPro pl;
	private final CommandHandler commandHandler;

	

	public void execute(CommandSender sender) {
		if (Settings.reloadChestCommand) {
			pl.getChestCommands().getCommand("cc").execute(sender, "cc", new String[] {"reload"});
			//Bukkit.dispatchCommand(sender, "cc reload");
		}
		pl.reload();
		Settings.sendMessage(sender, "重载插件完成");
		Settings.sendMessage(sender, "监听注入器的改动需要重启后才能生效");
		
	}

}
