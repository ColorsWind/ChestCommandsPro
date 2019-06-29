package com.blzeecraft.chestcommandsPro.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.blzeecraft.chestcommandsPro.ChestCommandsPro;
import com.blzeecraft.chestcommandsPro.Settings;

import lombok.Getter;

@Getter
public class CommandHandler implements CommandExecutor {
	private final CommandInfo cmdInfo;
	private final CommandReload cmdReload;
	private final CommandBuilder cmdBuild;

	public CommandHandler(ChestCommandsPro pl) {
		cmdInfo = new CommandInfo(pl, this);
		cmdReload = new CommandReload(pl, this);
		cmdBuild = new CommandBuilder(pl, this);

	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender.hasPermission("chestcommandspro.admin")) {
			if (args.length == 0) {
				sendHelp(sender);
			} else {
				if ("reload".equalsIgnoreCase(args[0])) {
					cmdReload.execute(sender);
				} else if (sender instanceof Player) {
					switch (args[0].toLowerCase()) {
					case "info":
						cmdInfo.execute((Player) sender);
						break;
					case "builder":
						if (args.length == 1) {
							cmdBuild.execute((Player) sender);
						} else {
							cmdBuild.execute((Player) sender, args[1]);
						}
						break;
					default:
						sendHelp(sender);
						break;
					}
				} else {
					sendHelp(sender);
				}
			}
		} else {
			Settings.sendMessage(sender, "权限不足，拒绝");
		}

		return true;
	}

	private void sendHelp(CommandSender sender) {
		Settings.sendMessage(sender, "~~~~ChestCommandsPro命令列表~~~");
		Settings.sendMessage(sender, "/ccp reload 重载插件");
		Settings.sendMessage(sender, "/ccp info 查看手上物品的属性");
		Settings.sendMessage(sender, "/ccp builder (name) 编辑(指定)菜单");
		Settings.sendMessage(sender, "~上古之石出品");
	}

}
