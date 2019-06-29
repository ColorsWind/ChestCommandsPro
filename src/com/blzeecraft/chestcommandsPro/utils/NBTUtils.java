package com.blzeecraft.chestcommandsPro.utils;

import org.bukkit.inventory.ItemStack;

import com.blzeecraft.chestcommandsPro.Settings;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import com.comphenix.protocol.wrappers.nbt.NbtFactory;
import com.comphenix.protocol.wrappers.nbt.NbtWrapper;
import com.comphenix.protocol.wrappers.nbt.io.NbtTextSerializer;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NBTUtils {

	public static String itemNBTtoText(ItemStack item) {
		if (Settings.supportNbt) {
			NbtWrapper<?> wrapper = NbtFactory.fromItemTag(item);
			return NbtTextSerializer.DEFAULT.serialize(wrapper);
		} else {
			return "unsupport";
		}

	}
	
	public static ItemStack setItemNBT(ItemStack original, String text) {
		try {
			NbtWrapper<Object> wrapper = NbtTextSerializer.DEFAULT.deserialize(text);
			NbtCompound compound = NbtFactory.asCompound(wrapper);
			original = checkClass(original);
			NbtFactory.setItemTag(original, compound);
			return original;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return original;

	}
	
	public static ItemStack checkClass(ItemStack item) {
		if (item.getClass() == ItemStack.class) {
			return ReflectUtils.asNMSCopy(item);
		}
		return item;
	}
	
	public static boolean isVaidNBT(String text) {
		return Settings.supportNbt && text != null && !text.isEmpty() && !"unsupport".equalsIgnoreCase(text);
	}

}
