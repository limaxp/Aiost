package com.pm.aiost.misc.other;

import java.lang.invoke.MethodHandle;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.pm.aiost.collection.list.FastArrayList;
import com.pm.aiost.misc.log.Logger;
import com.pm.aiost.misc.utils.nms.NMS;
import com.pm.aiost.misc.utils.reflection.Reflection;

public class PlayerHead {

	private static final List<String> HEAD_NAMES = new FastArrayList<String>(new String[] { "MHF_Alex", "MHF_Blaze",
			"MHF_CaveSpider", "MHF_Chicken", "MHF_Cow", "MHF_Creeper", "MHF_EnderDragon", "MHF_Enderman",
			"MHF_Endermite", "MHF_Ghast", "MHF_Golem", "MHF_Guardian", "MHF_Herobrine", "MHF_LavaSlime",
			"MHF_MushroomCow", "MHF_Ocelot", "MHF_Pig", "MHF_PigZombie", "MHF_Sheep", "MHF_Shulker", "MHF_Silverfish",
			"MHF_Skeleton", "MHF_Slime", "MHF_SnowGolem", "MHF_spider", "MHF_Squid", "MHF_Steve", "MHF_Witch",
			"MHF_Wither", "MHF_Wolf", "MHF_WSkeleton", "MHF_Villager", "MHF_Zombie", "MHF_Cactus", "MHF_Cake",
			"MHF_Chest", "MHF_CoconutB", "MHF_CoconutG", "MHF_Melon", "MHF_OakLog", "MHF_Present1", "MHF_Present2",
			"MHF_Pumkin", "MHF_TNT", "MHF_TNT2", "MHF_ArrowUp", "MHF_ArrowDown", "MHF_ArrowLeft", "MHF_ArrowRight",
			"MHF_Exclamation", "MHF_Question" });

	private static final MethodHandle CRAFTSKULLMETA_SET_PROFILE = Reflection
			.unreflectSetter(NMS.CRAFT_META_SKULL_CLASS, "profile");;

	public static void addHeadName(String name) {
		HEAD_NAMES.add(name);
	}

	public static void removeHeadName(String name) {
		HEAD_NAMES.add(name);
	}

	public static List<String> getHeadNames() {
		return HEAD_NAMES;
	}

	@SuppressWarnings("deprecation")
	public static ItemStack create(String playerName) {
		return set(new ItemStack(Material.PLAYER_HEAD), Bukkit.getOfflinePlayer(playerName));
	}

	@SuppressWarnings("deprecation")
	public static ItemStack create(String playerName, String name) {
		return set(new ItemStack(Material.PLAYER_HEAD), Bukkit.getOfflinePlayer(playerName), name);
	}

	@SuppressWarnings("deprecation")
	public static ItemStack create(String playerName, String name, List<String> lore) {
		return set(new ItemStack(Material.PLAYER_HEAD), Bukkit.getOfflinePlayer(playerName), name, lore);
	}

	public static ItemStack create(UUID uuid) {
		return set(new ItemStack(Material.PLAYER_HEAD), Bukkit.getOfflinePlayer(uuid));
	}

	public static ItemStack create(UUID uuid, String name) {
		return set(new ItemStack(Material.PLAYER_HEAD), Bukkit.getOfflinePlayer(uuid), name);
	}

	public static ItemStack create(UUID uuid, String name, List<String> lore) {
		return set(new ItemStack(Material.PLAYER_HEAD), Bukkit.getOfflinePlayer(uuid), name, lore);
	}

	public static ItemStack create(OfflinePlayer player) {
		return set(new ItemStack(Material.PLAYER_HEAD), player);
	}

	public static ItemStack create(OfflinePlayer player, String name) {
		return set(new ItemStack(Material.PLAYER_HEAD), player, name);
	}

	public static ItemStack create(OfflinePlayer player, String name, List<String> lore) {
		return set(new ItemStack(Material.PLAYER_HEAD), player, name, lore);
	}

	public static ItemStack create(GameProfile profile) {
		return set(new ItemStack(Material.PLAYER_HEAD), profile);
	}

	public static ItemStack create(GameProfile profile, String name) {
		return set(new ItemStack(Material.PLAYER_HEAD), profile, name);
	}

	public static ItemStack create(GameProfile profile, String name, List<String> lore) {
		return set(new ItemStack(Material.PLAYER_HEAD), profile, name, lore);
	}

	public static ItemStack custom(String texture) {
		return set(new ItemStack(Material.PLAYER_HEAD), texture);
	}

	public static ItemStack custom(String texture, String name) {
		return set(new ItemStack(Material.PLAYER_HEAD), texture, name);
	}

	public static ItemStack custom(String texture, String name, List<String> lore) {
		return set(new ItemStack(Material.PLAYER_HEAD), texture, name, lore);
	}

	public static ItemStack set(ItemStack is, OfflinePlayer player) {
		SkullMeta skullMeta = (SkullMeta) is.getItemMeta();
		skullMeta.setOwningPlayer(player);
		is.setItemMeta(skullMeta);
		return is;
	}

	public static ItemStack set(ItemStack is, OfflinePlayer player, String name) {
		SkullMeta skullMeta = (SkullMeta) is.getItemMeta();
		skullMeta.setOwningPlayer(player);
		skullMeta.setDisplayName(name);
		is.setItemMeta(skullMeta);
		return is;
	}

	public static ItemStack set(ItemStack is, OfflinePlayer player, String name, List<String> lore) {
		SkullMeta skullMeta = (SkullMeta) is.getItemMeta();
		skullMeta.setOwningPlayer(player);
		skullMeta.setDisplayName(name);
		skullMeta.setLore(lore);
		is.setItemMeta(skullMeta);
		return is;
	}

	public static ItemStack set(ItemStack is, GameProfile profile) {
		return set(is, profile, null);
	}

	public static ItemStack set(ItemStack is, GameProfile profile, String name) {
		SkullMeta skullMeta = (SkullMeta) is.getItemMeta();
		set(skullMeta, profile, name);
		is.setItemMeta(skullMeta);
		return is;
	}

	public static ItemStack set(ItemStack is, GameProfile profile, String name, List<String> lore) {
		SkullMeta skullMeta = (SkullMeta) is.getItemMeta();
		set(skullMeta, profile, name);
		skullMeta.setLore(lore);
		is.setItemMeta(skullMeta);
		return is;
	}

	public static ItemStack set(ItemStack is, String texture) {
		return set(is, (String) null, texture);
	}

	public static ItemStack set(ItemStack is, String texture, String name) {
		SkullMeta skullMeta = (SkullMeta) is.getItemMeta();
		set(skullMeta, texture, name);
		is.setItemMeta(skullMeta);
		return is;
	}

	public static ItemStack set(ItemStack is, String texture, String name, List<String> lore) {
		SkullMeta skullMeta = (SkullMeta) is.getItemMeta();
		set(skullMeta, texture, name);
		skullMeta.setLore(lore);
		is.setItemMeta(skullMeta);
		return is;
	}

	public static void set(SkullMeta skullMeta, GameProfile profile) {
		try {
			CRAFTSKULLMETA_SET_PROFILE.invoke(skullMeta, profile);
		} catch (Throwable e) {
			Logger.err("PlayerHead: Error on setting CraftMetaSkull profile field!", e);
		}
	}

	public static void set(SkullMeta skullMeta, GameProfile profile, String name) {
		set(skullMeta, profile);
		skullMeta.setDisplayName(name);
	}

	public static void set(SkullMeta skullMeta, String texture) {
		set(skullMeta, texture, null);
	}

	public static void set(SkullMeta skullMeta, String texture, String name) {
		GameProfile profile = new GameProfile(UUID.randomUUID(), name);
		profile.getProperties().put("textures", new Property("textures", texture));
		set(skullMeta, profile);
	}
}
