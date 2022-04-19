package com.pm.aiost.misc.utils.worldEdit;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import com.pm.aiost.misc.utils.worldEdit.Brush.BrushMode;

public class BrushData {

	public static final int DEFAULT_MAX_RADIUS = 32;

	public BrushMode mode = BrushMode.SET;
	public Block block;
	public int xRadius = 1;
	public int yRadius = 1;
	public int zRadius = 1;
	public Material type = Material.STONE;
	public Material type2 = Material.AIR;
	public BlockFace face = BlockFace.DOWN;
	public int maxRadius = DEFAULT_MAX_RADIUS;

	public void set(Block block1, Block block2) {
		set(block1.getWorld(), block1.getX(), block1.getY(), block1.getZ(), block2.getX(), block2.getY(),
				block2.getZ());
	}

	public void set(World world, int x1, int y1, int z1, int x2, int y2, int z2) {
		int xMax, xMin;
		int yMax, yMin;
		int zMax, zMin;
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
		setRadius((xMax - xMin) / 2, (yMax - yMin) / 2, (zMax - zMin) / 2);
		block = world.getBlockAt(xMin + xRadius, yMin + yRadius, zMin + zRadius);
	}

	public void setRadius(int radius) {
		if (radius > maxRadius)
			radius = maxRadius;
		xRadius = radius;
		yRadius = radius;
		zRadius = radius;
	}

	public void setRadius(int xRadius, int yRadius, int zRadius) {
		if (xRadius > maxRadius)
			xRadius = maxRadius;
		if (yRadius > maxRadius)
			yRadius = maxRadius;
		if (zRadius > maxRadius)
			zRadius = maxRadius;
		this.xRadius = xRadius;
		this.yRadius = yRadius;
		this.zRadius = zRadius;
	}
}