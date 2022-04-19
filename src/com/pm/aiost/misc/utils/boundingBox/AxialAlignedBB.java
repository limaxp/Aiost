package com.pm.aiost.misc.utils.boundingBox;

import org.bukkit.Location;

public class AxialAlignedBB {

	public int xMin;
	public int xMax;
	public int yMin;
	public int yMax;
	public int zMin;
	public int zMax;

	public AxialAlignedBB(int xMin, int xMax, int yMin, int yMax, int zMin, int zMax) {
		this.xMin = xMin;
		this.xMax = xMax;
		this.yMin = yMin;
		this.yMax = yMax;
		this.zMin = zMin;
		this.xMax = zMax;
	}

	public boolean contains(Location loc) {
		return contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
	}

	public boolean contains(int x, int y, int z) {
		return xMin <= x && xMax >= x && yMin <= y && yMax >= y && zMin <= z && zMax >= z;
	}
}
