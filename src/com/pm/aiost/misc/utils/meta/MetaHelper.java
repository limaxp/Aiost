package com.pm.aiost.misc.utils.meta;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_20_R4.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.pm.aiost.misc.nms.NBT;
import com.pm.aiost.misc.nms.NMS;

import net.minecraft.nbt.CompoundTag;

public class MetaHelper {

	public static ItemStack set(ItemStack is, String name) {
		return CraftItemStack.asCraftMirror(set(CraftItemStack.asNMSCopy(is), name));
	}

	public static net.minecraft.world.item.ItemStack set(net.minecraft.world.item.ItemStack is, String name) {
		CompoundTag nbt = NBT.getNBT(is);
		CompoundTag display = NBT.getOrAddDisplay(nbt);
		NBT.setDisplayName(display, name);
		return NBT.loadNMSItem(nbt);
	}

	public static ItemStack set(ItemStack is, int durability, String name) {
		return NMS.from(set(NMS.to(is), durability, name));
	}

	public static net.minecraft.world.item.ItemStack set(net.minecraft.world.item.ItemStack is, int durability,
			String name) {
		CompoundTag nbt = NBT.getNBT(is);
		NBT.setDurability(nbt, (short) durability);
		CompoundTag display = NBT.getOrAddDisplay(nbt);
		NBT.setDisplayName(display, name);
		return NBT.loadNMSItem(nbt);
	}

	public static ItemStack set(ItemStack is, List<String> lore) {
		return NMS.from(set(NMS.to(is), lore));
	}

	public static net.minecraft.world.item.ItemStack set(net.minecraft.world.item.ItemStack is, List<String> lore) {
		CompoundTag nbt = NBT.getNBT(is);
		CompoundTag display = NBT.getOrAddDisplay(nbt);
		NBT.setLore(display, lore);
		return NBT.loadNMSItem(nbt);
	}

	public static ItemStack set(ItemStack is, int durability, List<String> lore) {
		return NMS.from(set(NMS.to(is), durability, lore));
	}

	public static net.minecraft.world.item.ItemStack set(net.minecraft.world.item.ItemStack is, int durability,
			List<String> lore) {
		CompoundTag nbt = NBT.getNBT(is);
		NBT.setDurability(nbt, (short) durability);
		CompoundTag display = NBT.getOrAddDisplay(nbt);
		NBT.setLore(display, lore);
		return NBT.loadNMSItem(nbt);
	}

	public static ItemStack set(ItemStack is, String name, List<String> lore) {
		return NMS.from(set(NMS.to(is), name, lore));
	}

	public static net.minecraft.world.item.ItemStack set(net.minecraft.world.item.ItemStack is, String name,
			List<String> lore) {
		CompoundTag nbt = NBT.getNBT(is);
		CompoundTag display = NBT.getOrAddDisplay(nbt);
		NBT.setDisplayName(display, name);
		NBT.setLore(display, lore);
		return NBT.loadNMSItem(nbt);
	}

	public static ItemStack set(ItemStack is, int durability, String name, List<String> lore) {
		return NMS.from(set(NMS.to(is), durability, name, lore));
	}

	public static net.minecraft.world.item.ItemStack set(net.minecraft.world.item.ItemStack is, int durability,
			String name, List<String> lore) {
		CompoundTag nbt = NBT.getNBT(is);
		NBT.setDurability(nbt, (short) durability);
		CompoundTag display = NBT.getOrAddDisplay(nbt);
		NBT.setDisplayName(display, name);
		NBT.setLore(display, lore);
		return NBT.loadNMSItem(nbt);
	}

	public static ItemStack setMeta(Material mat, String name) {
		ItemStack is = new ItemStack(mat);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(name);
		is.setItemMeta(im);
		return is;
	}

	public static ItemStack setMeta(ItemStack is, String name) {
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(name);
		is.setItemMeta(im);
		return is;
	}

	@SuppressWarnings("deprecation")
	public static ItemStack setMeta(Material mat, int durability, String name) {
		ItemStack is = new ItemStack(mat);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(name);
		is.setDurability((short) durability);
		is.setItemMeta(im);
		return is;
	}

	@SuppressWarnings("deprecation")
	public static ItemStack setMeta(ItemStack is, int durability, String name) {
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(name);
		is.setDurability((short) durability);
		is.setItemMeta(im);
		return is;
	}

	public static ItemStack setMeta(Material mat, List<String> lore) {
		ItemStack is = new ItemStack(mat);
		ItemMeta im = is.getItemMeta();
		im.setLore(lore);
		is.setItemMeta(im);
		return is;
	}

	public static ItemStack setMeta(ItemStack is, List<String> lore) {
		ItemMeta im = is.getItemMeta();
		im.setLore(lore);
		is.setItemMeta(im);
		return is;
	}

	@SuppressWarnings("deprecation")
	public static ItemStack setMeta(Material mat, int durability, List<String> lore) {
		ItemStack is = new ItemStack(mat);
		ItemMeta im = is.getItemMeta();
		im.setLore(lore);
		is.setDurability((short) durability);
		is.setItemMeta(im);
		return is;
	}

	@SuppressWarnings("deprecation")
	public static ItemStack setMeta(ItemStack is, int durability, List<String> lore) {
		ItemMeta im = is.getItemMeta();
		im.setLore(lore);
		is.setDurability((short) durability);
		is.setItemMeta(im);
		return is;
	}

	public static ItemStack setMeta(Material mat, String name, List<String> lore) {
		ItemStack is = new ItemStack(mat);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(name);
		im.setLore(lore);
		is.setItemMeta(im);
		return is;
	}

	public static ItemStack setMeta(ItemStack is, String name, List<String> lore) {
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(name);
		im.setLore(lore);
		is.setItemMeta(im);
		return is;
	}

	@SuppressWarnings("deprecation")
	public static ItemStack setMeta(Material mat, int durability, String name, List<String> lore) {
		ItemStack is = new ItemStack(mat);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(name);
		im.setLore(lore);
		is.setDurability((short) durability);
		is.setItemMeta(im);
		return is;
	}

	@SuppressWarnings("deprecation")
	public static ItemStack setMeta(ItemStack is, int durability, String name, List<String> lore) {
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(name);
		im.setLore(lore);
		is.setDurability((short) durability);
		is.setItemMeta(im);
		return is;
	}

	public static ItemStack hideAttributes(ItemStack is) {
		ItemMeta im = is.getItemMeta();
		im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		is.setItemMeta(im);
		return is;
	}

	public static ItemStack hideAttributes(ItemStack is, String name) {
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(name);
		im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		is.setItemMeta(im);
		return is;
	}

	public static ItemStack hideAttributes(ItemStack is, List<String> lore) {
		ItemMeta im = is.getItemMeta();
		im.setLore(lore);
		im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		is.setItemMeta(im);
		return is;
	}

	public static ItemStack hideAttributes(ItemStack is, String name, List<String> lore) {
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(name);
		im.setLore(lore);
		im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		is.setItemMeta(im);
		return is;
	}

	public static ItemStack hidePotionEffects(ItemStack is) {
		ItemMeta im = is.getItemMeta();
		im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		is.setItemMeta(im);
		return is;
	}

	public static ItemStack hidePotionEffects(ItemStack is, String name) {
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(name);
		im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		is.setItemMeta(im);
		return is;
	}

	public static ItemStack hidePotionEffects(ItemStack is, List<String> lore) {
		ItemMeta im = is.getItemMeta();
		im.setLore(lore);
		im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		is.setItemMeta(im);
		return is;
	}

	public static ItemStack hidePotionEffects(ItemStack is, String name, List<String> lore) {
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(name);
		im.setLore(lore);
		im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		is.setItemMeta(im);
		return is;
	}

	public static ItemStack addLore(ItemStack is, String name, List<String> lore) {
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(name);
		List<String> oldLore = im.getLore();
		oldLore.addAll(lore);
		im.setLore(oldLore);
		is.setItemMeta(im);
		return is;
	}

	public static ItemStack setGlow(ItemStack is) {
		ItemMeta im = is.getItemMeta();
		im.addEnchant(Enchantment.EFFICIENCY, 1, false);
		im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		is.setItemMeta(im);
		return is;
	}

	public static ItemStack removeGlow(ItemStack is) {
		ItemMeta im = is.getItemMeta();
		im.removeEnchant(Enchantment.EFFICIENCY);
		im.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
		is.setItemMeta(im);
		return is;
	}
}
