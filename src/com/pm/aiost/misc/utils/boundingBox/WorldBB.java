package com.pm.aiost.misc.utils.boundingBox;

import java.util.function.Consumer;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import com.pm.aiost.server.world.ServerWorld;
import com.pm.aiost.server.world.chunk.ServerChunk;

public class WorldBB {

	protected int xMin;
	protected int xMax;
	protected int yMin;
	protected int yMax;
	protected int zMin;
	protected int zMax;

	public WorldBB() {
	}

	public WorldBB(Location first, Location second) {
		this(first.getBlockX(), first.getBlockY(), first.getBlockZ(), second.getBlockX(), second.getBlockY(),
				second.getBlockZ());
	}

	public WorldBB(int x1, int y1, int z1, int x2, int y2, int z2) {
		if (x1 < x2) {
			xMin = x1;
			xMax = x2;
		} else {
			xMin = x2;
			xMax = x1;
		}

		if (y1 < y2) {
			yMin = y1;
			yMax = y2;
		} else {
			yMin = y2;
			yMax = y1;
		}

		if (z1 < z2) {
			zMin = z1;
			zMax = z2;
		} else {
			zMin = z2;
			zMax = z1;
		}
	}

	public boolean contains(Location loc) {
		return contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
	}

	public boolean contains(int x, int y, int z) {
		return xMin <= x && xMax >= x && yMin <= y && yMax >= y && zMin <= z && zMax >= z;
	}

	public Location getCenter(World world) {
		return new Location(world, xMin + xMax - xMin, yMin + yMax - yMin, zMin + zMax - zMin, 0, 0);
	}

	public void forServerChunks(ServerWorld serverWorld, Consumer<ServerChunk> consumer) {
		int firstChunkX = xMin >> 4;
		int lastChunkX = xMax >> 4;
		int firstChunkZ = zMin >> 4;
		int lastChunkZ = zMax >> 4;

		for (int chunkX = firstChunkX; chunkX <= lastChunkX; chunkX++)
			for (int chunkZ = firstChunkZ; chunkZ <= lastChunkZ; chunkZ++)
				consumer.accept(serverWorld.getChunk(chunkX, chunkZ));
	}

	public void load(ConfigurationSection section) {
		xMin = section.getInt("xMin");
		xMax = section.getInt("xMax");
		yMin = section.getInt("yMin");
		yMax = section.getInt("yMax");
		zMin = section.getInt("zMin");
		zMax = section.getInt("zMax");
	}

	public void save(ConfigurationSection section) {
		section.set("xMin", xMin);
		section.set("xMax", xMax);
		section.set("yMin", yMin);
		section.set("yMax", yMax);
		section.set("zMin", zMin);
		section.set("zMax", zMax);
	}
}
