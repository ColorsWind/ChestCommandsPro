package com.blzeecraft.chestcommandsPro.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.blzeecraft.chestcommandsPro.ChestCommandsPro;
import com.blzeecraft.chestcommandsPro.utils.NBTUtils;
import com.blzeecraft.chestcommandsPro.utils.ReflectUtils;

import net.md_5.bungee.api.ChatColor;

public class CommandInfo {

	public CommandInfo(ChestCommandsPro pl, CommandHandler commandHandler) {

	}

	public void execute(Player p) {
		String[] m = execute(ReflectUtils.getItemInHand(p, true));
		execute(p, m);
		execute(Bukkit.getConsoleSender(), m);

	}
	
	public String[] execute(ItemStack item) {
		if (item == null || item.getType() == Material.AIR) {
			return new String[]{ChatColor.RED + "手上无物品"};
		}
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> messages = new ArrayList<>(5);
		messages.add(ChatColor.AQUA + " NAME: " + buildName(meta.getDisplayName()));
		List<String> lore = buildLore(meta.getLore());
		if (lore.isEmpty()) {
			messages.add(ChatColor.AQUA + " LORE: []" );
		} else {
			messages.add(ChatColor.AQUA + " LORE: " );
			messages.addAll(lore);
		}
		
		messages.add(ChatColor.AQUA + " ID: " + item.getType().name());
		messages.add(ChatColor.AQUA + " DATA-VALUE: " + item.getDurability());
		messages.add(ChatColor.AQUA + " AMOUNT: " + item.getAmount());
		messages.add(ChatColor.AQUA + " NBT: \"" + NBTUtils.itemNBTtoText(item).replace("\r", "\\r").replace("\n", "\\n")  + "\"");
		return messages.toArray(new String[0]);
	} 
	
	private List<String> buildLore(List<String> lore) {
		if (lore == null || lore.isEmpty()) {
			return Collections.emptyList();
		} else {
			ArrayList<String> list = new ArrayList<>(lore.size());
			for(String s : lore) {
				list.add(("   -\'" + s + "'").replace('§', '&'));
			}
		}
		return lore;
	}


	private String buildName(String displayName) {
		if (displayName == null) {
			return "\'\'";
		}
		return ("'" + displayName + "'").replace('§', '&');
	}


	public void execute(CommandSender sender, String[] message) {
		sender.sendMessage(message);
	}
}
