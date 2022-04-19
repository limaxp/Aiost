package com.pm.aiost.misc.utils.worldEdit;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.scheduler.BukkitRunnable;

import com.pm.aiost.Aiost;
import com.pm.aiost.misc.utils.nbt.NBTType;
import com.pm.aiost.misc.utils.nbt.custom.INBTTagCompound;
import com.pm.aiost.misc.utils.nbt.custom.NBTCompound;
import com.pm.aiost.misc.utils.nbt.custom.NBTList;
import com.pm.aiost.misc.utils.worldEdit.WorldEdit.WorldEditTask;

import net.minecraft.server.v1_15_R1.NBTTagCompound;
import net.minecraft.server.v1_15_R1.NBTTagList;

public class Schematic {

	private BlockData[][][] data;

	public Schematic() {
	}

	public Schematic(Block block, int xRadius, int yRadius, int zRadius) {
		new LoadSchematicTask(block, xRadius, yRadius, zRadius).runTaskTimer(Aiost.getPlugin(), 0,
				WorldEdit.DEFAULT_INTERVALL);
	}

	public Schematic(Block block1, Block block2) {
		new LoadSchematicTask(block1, block2).runTaskTimer(Aiost.getPlugin(), 0, WorldEdit.DEFAULT_INTERVALL);
	}

	public void spawn(Location loc) {
		spawn(loc.getBlock());
	}

	public void spawn(Block block) {
		new PlaceSchematicTask(block).runTaskTimer(Aiost.getPlugin(), 0, WorldEdit.DEFAULT_INTERVALL);
	}

	public void load(INBTTagCompound nbt) {
		NBTTagList yList = nbt.getList("schematic", NBTType.LIST);
		for (int y = 0; y < yList.size(); y++) {
			NBTTagList xList = (NBTTagList) yList.get(y);
			for (int x = 0; x < xList.size(); x++) {
				NBTTagList zList = (NBTTagList) xList.get(x);
				for (int z = 0; z < zList.size(); z++) {
					NBTTagCompound nbtData = zList.getCompound(z);
					System.out.println(nbtData.getString("data"));
					BlockData data = null;
					this.data[x][y][z] = data;
				}
			}
		}
	}

	public void save(INBTTagCompound nbt) {
		NBTList yList = new NBTList();
		nbt.set("schematic", yList);
		int yLast = data.length;
		for (int y = 0; y < yLast; y++) {
			NBTList xList = new NBTList();
			yList.add(xList);
			int xLast = data[y].length;
			for (int x = 0; x < xLast; x++) {
				NBTList zList = new NBTList();
				xList.add(zList);
				int zLast = data[y][x].length;
				for (int z = 0; z < zLast; z++) {
					BlockData data = this.data[x][y][z];
					NBTCompound nbtData = new NBTCompound();
					nbtData.setString("data", data.getAsString());
					zList.add(nbtData);
				}
			}
		}
	}

	private class LoadSchematicTask extends WorldEditTask {

		private LoadSchematicTask(Block block, int xRadius, int yRadius, int zRadius) {
			super(block, xRadius, yRadius, zRadius);
			data = new BlockData[1 + yMax - yMin][1 + xMax - xMin][1 + zMax - zMin];
		}

		private LoadSchematicTask(Block block1, Block block2) {
			super(block1, block2);
			data = new BlockData[1 + yMax - yMin][1 + xMax - xMin][1 + zMax - zMin];
		}

		@Override
		public void run() {
			int steps = 0;
			for (; y <= yMax; y++) {
				for (; x <= xMax; x++) {
					for (; z <= zMax; z++) {
						if (steps++ > WorldEdit.MAX_STEPS)
							return;
						Block block = world.getBlockAt(x, y, z);
						if (!block.getChunk().isLoaded()) {
							cancel();
							return;
						}
						data[y - yMin][x - xMin][z - zMin] = block.getBlockData();
					}
					z = 0;
				}
				x = 0;
			}
			cancel();
		}
	}

	private class PlaceSchematicTask extends BukkitRunnable {

		protected final World world;
		protected final int xPos;
		protected final int yPos;
		protected final int zPos;
		protected int x;
		protected int y;
		protected int z;

		private PlaceSchematicTask(Block block) {
			world = block.getWorld();
			xPos = block.getX();
			yPos = block.getY();
			zPos = block.getZ();
			x = 0;
			y = 0;
			z = 0;
		}

		@Override
		public void run() {
			int steps = 0;
			int yLast = data.length;
			for (; y <= yLast; y++) {
				int xLast = data[y].length;
				for (; x <= xLast; x++) {
					int zLast = data[y][x].length;
					for (; z <= zLast; z++) {
						if (steps++ > WorldEdit.MAX_STEPS)
							return;
						Block block = world.getBlockAt(xPos + x, yPos + y, zPos + z);
						if (!block.getChunk().isLoaded()) {
							cancel();
							return;
						}
						block.setBlockData(data[y][x][z]);
					}
					z = 0;
				}
				x = 0;
			}
			cancel();
		}
	}
}