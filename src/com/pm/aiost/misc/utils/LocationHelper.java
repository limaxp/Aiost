package com.pm.aiost.misc.utils;

import static com.pm.aiost.misc.utils.ChatColor.DARK_GRAY;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

public class LocationHelper {

	public static double calculateYaw(Location loc1, Location loc2) {
		return Math.atan2(loc1.getZ() - loc2.getZ(), loc1.getX() - loc2.getX()) + 45;
	}

	public static double calculatePitch(Location loc1, Location loc2) {
		return Math.asin((loc1.getY() - loc2.getY()) / loc2.distance(loc1));
	}

	public static double distance(int x1, int z1, int x2, int z2) {
		return Math.sqrt((x2 - x1) * (x2 - x1) + (z2 - z1) * (z2 - z1));
	}

	public static double distance(double x1, double z1, double x2, double z2) {
		return Math.sqrt((x2 - x1) * (x2 - x1) + (z2 - z1) * (z2 - z1));
	}

	public static double distance(int x1, int y1, int z1, int x2, int y2, int z2) {
		return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1) + (z2 - z1) * (z2 - z1));
	}

	public static double distance(double x1, double y1, double z1, double x2, double y2, double z2) {
		return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1) + (z2 - z1) * (z2 - z1));
	}

	public static double distance(Entity entity, Location loc) {
		return entity.getLocation().distance(loc);
	}

	public static double distance(net.minecraft.world.entity.Entity entity, Location loc) {
		return distance(entity.getX(), entity.getY(), entity.getZ(), loc.getX(), loc.getY(), loc.getZ());
	}

	public static Location load(ConfigurationSection section) {
		return new Location(Bukkit.getWorld(section.getString("world")), section.getDouble("x"), section.getDouble("y"),
				section.getDouble("z"));
	}

	public static void save(Location loc, ConfigurationSection section) {
		section.set("world", loc.getWorld().getName());
		section.set("x", loc.getX());
		section.set("y", loc.getY());
		section.set("z", loc.getZ());
	}

	public static Location load(CompoundTag nbt) {
		return new Location(Bukkit.getWorld(nbt.getString("world")), nbt.getDouble("x"), nbt.getDouble("y"),
				nbt.getDouble("z"));
	}

	public static CompoundTag save(Location loc, CompoundTag nbt) {
		nbt.putString("world", loc.getWorld().getName());
		nbt.putDouble("x", loc.getX());
		nbt.putDouble("y", loc.getY());
		nbt.putDouble("z", loc.getZ());
		return nbt;
	}

	public static Location[] load(ListTag nbtList) {
		int size = nbtList.size();
		Location[] locations = new Location[size];
		for (int i = 0; i < size; i++)
			locations[i] = load(nbtList.getCompound(i));
		return locations;
	}

	public static ListTag save(Iterable<Location> locations, ListTag nbtList) {
		int i = 0;
		for (Location loc : locations)
			nbtList.set(i++, save(loc, new CompoundTag()));
		return nbtList;
	}

	public static void createDescription(List<String> list, Location loc) {
		list.add(GRAY + "World: " + DARK_GRAY + loc.getWorld().getName());
		list.add(GRAY + "x: " + DARK_GRAY + loc.getX());
		list.add(GRAY + "y: " + DARK_GRAY + loc.getY());
		list.add(GRAY + "z: " + DARK_GRAY + loc.getZ());
	}
}
