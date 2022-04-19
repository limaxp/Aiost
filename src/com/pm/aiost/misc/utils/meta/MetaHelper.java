package com.pm.aiost.misc.utils.meta;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.pm.aiost.misc.utils.nbt.NBTHelper;

import net.minecraft.server.v1_15_R1.NBTTagCompound;

public class MetaHelper {

	public static ItemStack set(ItemStack is, String name) {
		return CraftItemStack.asCraftMirror(set(CraftItemStack.asNMSCopy(is), name));
	}

	public static net.minecraft.server.v1_15_R1.ItemStack set(net.minecraft.server.v1_15_R1.ItemStack is, String name) {
		NBTTagCompound nbt = is.hasTag() ? is.getTag() : new NBTTagCompound();
		NBTTagCompound display = NBTHelper.getOrAddDisplay(nbt);
		NBTHelper.setDisplayName(display, name);
		is.setTag(nbt);
		return is;
	}

	public static ItemStack set(ItemStack is, int durability, String name) {
		return CraftItemStack.asCraftMirror(set(CraftItemStack.asNMSCopy(is), durability, name));
	}

	public static net.minecraft.server.v1_15_R1.ItemStack set(net.minecraft.server.v1_15_R1.ItemStack is,
			int durability, String name) {
		NBTTagCompound nbt = is.hasTag() ? is.getTag() : new NBTTagCompound();
		NBTHelper.setDurability(nbt, (short) durability);
		NBTTagCompound display = NBTHelper.getOrAddDisplay(nbt);
		NBTHelper.setDisplayName(display, name);
		is.setTag(nbt);
		return is;
	}

	public static ItemStack set(ItemStack is, List<String> lore) {
		return CraftItemStack.asCraftMirror(set(CraftItemStack.asNMSCopy(is), lore));
	}

	public static net.minecraft.server.v1_15_R1.ItemStack set(net.minecraft.server.v1_15_R1.ItemStack is,
			List<String> lore) {
		NBTTagCompound nbt = is.hasTag() ? is.getTag() : new NBTTagCompound();
		NBTTagCompound display = NBTHelper.getOrAddDisplay(nbt);
		NBTHelper.setLore(display, lore);
		is.setTag(nbt);
		return is;
	}

	public static ItemStack set(ItemStack is, int durability, List<String> lore) {
		return CraftItemStack.asCraftMirror(set(CraftItemStack.asNMSCopy(is), durability, lore));
	}

	public static net.minecraft.server.v1_15_R1.ItemStack set(net.minecraft.server.v1_15_R1.ItemStack is,
			int durability, List<String> lore) {
		NBTTagCompound nbt = is.hasTag() ? is.getTag() : new NBTTagCompound();
		NBTHelper.setDurability(nbt, (short) durability);
		NBTTagCompound display = NBTHelper.getOrAddDisplay(nbt);
		NBTHelper.setLore(display, lore);
		is.setTag(nbt);
		return is;
	}

	public static ItemStack set(ItemStack is, String name, List<String> lore) {
		return CraftItemStack.asCraftMirror(set(CraftItemStack.asNMSCopy(is), name, lore));
	}

	public static net.minecraft.server.v1_15_R1.ItemStack set(net.minecraft.server.v1_15_R1.ItemStack is, String name,
			List<String> lore) {
		NBTTagCompound nbt = is.hasTag() ? is.getTag() : new NBTTagCompound();
		NBTTagCompound display = NBTHelper.getOrAddDisplay(nbt);
		NBTHelper.setDisplayName(display, name);
		NBTHelper.setLore(display, lore);
		is.setTag(nbt);
		return is;
	}

	public static ItemStack set(ItemStack is, int durability, String name, List<String> lore) {
		return CraftItemStack.asCraftMirror(set(CraftItemStack.asNMSCopy(is), durability, name, lore));
	}

	public static net.minecraft.server.v1_15_R1.ItemStack set(net.minecraft.server.v1_15_R1.ItemStack is,
			int durability, String name, List<String> lore) {
		NBTTagCompound nbt = is.hasTag() ? is.getTag() : new NBTTagCompound();
		NBTHelper.setDurability(nbt, (short) durability);
		NBTTagCompound display = NBTHelper.getOrAddDisplay(nbt);
		NBTHelper.setDisplayName(display, name);
		NBTHelper.setLore(display, lore);
		is.setTag(nbt);
		return is;
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
		im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		is.setItemMeta(im);
		return is;
	}

	public static ItemStack hidePotionEffects(ItemStack is, String name) {
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(name);
		im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		is.setItemMeta(im);
		return is;
	}

	public static ItemStack hidePotionEffects(ItemStack is, List<String> lore) {
		ItemMeta im = is.getItemMeta();
		im.setLore(lore);
		im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		is.setItemMeta(im);
		return is;
	}

	public static ItemStack hidePotionEffects(ItemStack is, String name, List<String> lore) {
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(name);
		im.setLore(lore);
		im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
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
		im.addEnchant(Enchantment.DIG_SPEED, 1, false);
		im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		is.setItemMeta(im);
		return is;
	}

	public static ItemStack removeGlow(ItemStack is) {
		ItemMeta im = is.getItemMeta();
		im.removeEnchant(Enchantment.DIG_SPEED);
		im.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
		is.setItemMeta(im);
		return is;
	}
}
