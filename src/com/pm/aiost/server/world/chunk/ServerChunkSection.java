package com.pm.aiost.server.world.chunk;

import org.bukkit.Location;
import org.bukkit.block.Block;

public class ServerChunkSection {

	public final byte yPos;

	ServerChunkSection(byte yPos) {
		this.yPos = yPos;
	}

	public static int getKey(Block block) {
		return getKey(block.getX(), block.getY(), block.getZ());
	}

	public static int getKey(Location loc) {
		return getKey(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
	}

	public static int getKey(int x, int y, int z) {
		return (y & 0xF) << 8 | (z & 0xF) << 4 | (x & 0xF);
	}
}
