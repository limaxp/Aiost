package com.pm.aiost.misc.utils.worldEdit;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import com.pm.aiost.Aiost;
import com.pm.aiost.collection.list.UnorderedIdentityArrayList;
import com.pm.aiost.misc.utils.nms.NMS;

public class WorldEdit {

	public static final int MAX_STEPS = 128;
	public static final int DEFAULT_INTERVALL = 1;
	public static final int MAX_TASKS = 20;

	private static final List<WorldEditTask> ACTIVE_TASKS = new UnorderedIdentityArrayList<WorldEditTask>();

	public static List<WorldEditTask> getActiveTasks() {
		return Collections.unmodifiableList(ACTIVE_TASKS);
	}

	public static void breakBlock(Block block) {
		block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType());
		block.setType(Material.AIR);
	}

	public static void breakBlock(Block block, Material type) {
		if (block.getType() == type)
			breakBlock(block);
	}

	public static void breakBlockNaturally(Block block) {
		block.breakNaturally();
	}

	public static void breakBlockNaturally(Block block, Material type) {
		if (block.getType() == type)
			block.breakNaturally();
	}

	public static void setBlock(Block block, Material type) {
		block.setType(type);
	}

	public static void setBlock(Block block, BlockData blockData) {
		block.setBlockData(blockData);
	}

	public static void setEmptyBlock(Block block, Material type) {
		if (block.getType() == Material.AIR)
			block.setType(type);
	}

	public static void setEmptyBlock(Block block, BlockData blockData) {
		if (block.getType() == Material.AIR) {
			block.setBlockData(blockData);
		}
	}

	public static void replaceBlock(Block block, Material old, Material type) {
		if (block.getType() == old)
			block.setType(type);
	}

	public static void replaceBlock(Block block, Material old, BlockData blockData) {
		if (block.getType() == old) {
			block.setBlockData(blockData);
		}
	}

	public static void breakBlocks(Block block, int xRadius, int yRadius, int zRadius) {
		new BreakBlockTask(block, xRadius, yRadius, zRadius).runTaskTimer(Aiost.getPlugin(), 0, DEFAULT_INTERVALL);
	}

	public static void breakBlocks(Block block1, Block block2) {
		new BreakBlockTask(block1, block2).runTaskTimer(Aiost.getPlugin(), 0, DEFAULT_INTERVALL);
	}

	public static void breakBlocksNaturally(Block block, int xRadius, int yRadius, int zRadius) {
		new BreakBlockNaturallyTask(block, xRadius, yRadius, zRadius).runTaskTimer(Aiost.getPlugin(), 0,
				DEFAULT_INTERVALL);
	}

	public static void breakBlocksNaturally(Block block, Block block2) {
		new BreakBlockNaturallyTask(block, block2).runTaskTimer(Aiost.getPlugin(), 0, DEFAULT_INTERVALL);
	}

	public static void setBlocks(Block block, int xRadius, int yRadius, int zRadius, Material type) {
		new SetBlockTask(block, xRadius, yRadius, zRadius, type).runTaskTimer(Aiost.getPlugin(), 0, DEFAULT_INTERVALL);
	}

	public static void setBlocks(Block block, Block block2, Material type) {
		new SetBlockTask(block, block2, type).runTaskTimer(Aiost.getPlugin(), 0, DEFAULT_INTERVALL);
	}

	public static void setBlocks(Block block, int xRadius, int yRadius, int zRadius, BlockData data) {
		new SetBlockDataTask(block, xRadius, yRadius, zRadius, data).runTaskTimer(Aiost.getPlugin(), 0,
				DEFAULT_INTERVALL);
	}

	public static void setBlocks(Block block, Block block2, BlockData data) {
		new SetBlockDataTask(block, block2, data).runTaskTimer(Aiost.getPlugin(), 0, DEFAULT_INTERVALL);
	}

	public static void fillBlocks(Block block, int xRadius, int yRadius, int zRadius, Material type) {
		new FillBlockTask(block, xRadius, yRadius, zRadius, type).runTaskTimer(Aiost.getPlugin(), 0, DEFAULT_INTERVALL);
	}

	public static void fillBlocks(Block block, Block block2, Material type) {
		new FillBlockTask(block, block2, type).runTaskTimer(Aiost.getPlugin(), 0, DEFAULT_INTERVALL);
	}

	public static void fillBlocks(Block block, int xRadius, int yRadius, int zRadius, BlockData data) {
		new FillBlockDataTask(block, xRadius, yRadius, zRadius, data).runTaskTimer(Aiost.getPlugin(), 0,
				DEFAULT_INTERVALL);
	}

	public static void fillBlocks(Block block, Block block2, BlockData data) {
		new FillBlockDataTask(block, block2, data).runTaskTimer(Aiost.getPlugin(), 0, DEFAULT_INTERVALL);
	}

	public static void replaceBlocks(Block block, int xRadius, int yRadius, int zRadius, Material old, Material type) {
		new ReplaceBlockTask(block, xRadius, yRadius, zRadius, old, type).runTaskTimer(Aiost.getPlugin(), 0,
				DEFAULT_INTERVALL);
	}

	public static void replaceBlocks(Block block, Block block2, Material old, Material type) {
		new ReplaceBlockTask(block, block2, old, type).runTaskTimer(Aiost.getPlugin(), 0, DEFAULT_INTERVALL);
	}

	public static void replaceBlocks(Block block, int xRadius, int yRadius, int zRadius, Material old, BlockData data) {
		new ReplaceBlockDataTask(block, xRadius, yRadius, zRadius, old, data).runTaskTimer(Aiost.getPlugin(), 0,
				DEFAULT_INTERVALL);
	}

	public static void replaceBlocks(Block block, Block block2, Material old, BlockData data) {
		new ReplaceBlockDataTask(block, block2, old, data).runTaskTimer(Aiost.getPlugin(), 0, DEFAULT_INTERVALL);
	}

	public static void breakBall(Block block, int radius) {
		new BreakBallTask(block, radius).runTaskTimer(Aiost.getPlugin(), 0, DEFAULT_INTERVALL);
	}

	public static void breakBallNaturally(Block block, int radius) {
		new BreakBallNaturallyTask(block, radius).runTaskTimer(Aiost.getPlugin(), 0, DEFAULT_INTERVALL);
	}

	public static void setBall(Block block, int radius, Material type) {
		new SetBallTask(block, radius, type).runTaskTimer(Aiost.getPlugin(), 0, DEFAULT_INTERVALL);
	}

	public static void setBall(Block block, int radius, BlockData data) {
		new SetBallDataTask(block, radius, data).runTaskTimer(Aiost.getPlugin(), 0, DEFAULT_INTERVALL);
	}

	public static void fillBall(Block block, int radius, Material type) {
		new FillBallTask(block, radius, type).runTaskTimer(Aiost.getPlugin(), 0, DEFAULT_INTERVALL);
	}

	public static void fillBall(Block block, int radius, BlockData data) {
		new FillBallDataTask(block, radius, data).runTaskTimer(Aiost.getPlugin(), 0, DEFAULT_INTERVALL);
	}

	public static void fillBall(Block block, int radius, Material old, Material type) {
		new ReplaceBallTask(block, radius, old, type).runTaskTimer(Aiost.getPlugin(), 0, DEFAULT_INTERVALL);
	}

	public static void fillBall(Block block, int radius, Material old, BlockData data) {
		new ReplaceBallDataTask(block, radius, old, data).runTaskTimer(Aiost.getPlugin(), 0, DEFAULT_INTERVALL);
	}

	public static void breakHollowBall(Block block, int radius) {
		new BreakHollowBallTask(block, radius).runTaskTimer(Aiost.getPlugin(), 0, DEFAULT_INTERVALL);
	}

	public static void breakHollowBallNaturally(Block block, int radius) {
		new BreakHollowBallNaturallyTask(block, radius).runTaskTimer(Aiost.getPlugin(), 0, DEFAULT_INTERVALL);
	}

	public static void setHollowBall(Block block, int radius, Material type) {
		new SetHollowBallTask(block, radius, type).runTaskTimer(Aiost.getPlugin(), 0, DEFAULT_INTERVALL);
	}

	public static void setHollowBall(Block block, int radius, BlockData data) {
		new SetHollowBallDataTask(block, radius, data).runTaskTimer(Aiost.getPlugin(), 0, DEFAULT_INTERVALL);
	}

	public static void fillHollowBall(Block block, int radius, Material type) {
		new FillHollowBallTask(block, radius, type).runTaskTimer(Aiost.getPlugin(), 0, DEFAULT_INTERVALL);
	}

	public static void fillHollowBall(Block block, int radius, BlockData data) {
		new FillHollowBallDataTask(block, radius, data).runTaskTimer(Aiost.getPlugin(), 0, DEFAULT_INTERVALL);
	}

	public static void fillHollowBall(Block block, int radius, Material old, Material type) {
		new ReplaceHollowBallTask(block, radius, old, type).runTaskTimer(Aiost.getPlugin(), 0, DEFAULT_INTERVALL);
	}

	public static void fillHollowBall(Block block, int radius, Material old, BlockData data) {
		new ReplaceHollowBallDataTask(block, radius, old, data).runTaskTimer(Aiost.getPlugin(), 0, DEFAULT_INTERVALL);
	}

	public static void breakSphere(Block block, int xRadius, int yRadius, int zRadius) {
		new BreakSphereTask(block, xRadius, yRadius, zRadius).runTaskTimer(Aiost.getPlugin(), 0, DEFAULT_INTERVALL);
	}

	public static void breakSphere(Block block1, Block block2) {
		new BreakSphereTask(block1, block2).runTaskTimer(Aiost.getPlugin(), 0, DEFAULT_INTERVALL);
	}

	public static void breakSphereNaturally(Block block, int xRadius, int yRadius, int zRadius) {
		new BreakSphereNaturallyTask(block, xRadius, yRadius, zRadius).runTaskTimer(Aiost.getPlugin(), 0,
				DEFAULT_INTERVALL);
	}

	public static void breakSphereNaturally(Block block1, Block block2) {
		new BreakSphereNaturallyTask(block1, block2).runTaskTimer(Aiost.getPlugin(), 0, DEFAULT_INTERVALL);
	}

	public static void setSphere(Block block, int xRadius, int yRadius, int zRadius, Material type) {
		new SetSphereTask(block, xRadius, yRadius, zRadius, type).runTaskTimer(Aiost.getPlugin(), 0, DEFAULT_INTERVALL);
	}

	public static void setSphere(Block block1, Block block2, Material type) {
		new SetSphereTask(block1, block2, type).runTaskTimer(Aiost.getPlugin(), 0, DEFAULT_INTERVALL);
	}

	public static void setSphere(Block block, int xRadius, int yRadius, int zRadius, BlockData data) {
		new SetSphereDataTask(block, xRadius, yRadius, zRadius, data).runTaskTimer(Aiost.getPlugin(), 0,
				DEFAULT_INTERVALL);
	}

	public static void setSphere(Block block1, Block block2, BlockData data) {
		new SetSphereDataTask(block1, block2, data).runTaskTimer(Aiost.getPlugin(), 0, DEFAULT_INTERVALL);
	}

	public static void fillSphere(Block block, int xRadius, int yRadius, int zRadius, Material type) {
		new FillSphereTask(block, xRadius, yRadius, zRadius, type).runTaskTimer(Aiost.getPlugin(), 0,
				DEFAULT_INTERVALL);
	}

	public static void fillSphere(Block block1, Block block2, Material type) {
		new FillSphereTask(block1, block2, type).runTaskTimer(Aiost.getPlugin(), 0, DEFAULT_INTERVALL);
	}

	public static void fillSphere(Block block, int xRadius, int yRadius, int zRadius, BlockData data) {
		new FillSphereDataTask(block, xRadius, yRadius, zRadius, data).runTaskTimer(Aiost.getPlugin(), 0,
				DEFAULT_INTERVALL);
	}

	public static void fillSphere(Block block1, Block block2, BlockData data) {
		new FillSphereDataTask(block1, block2, data).runTaskTimer(Aiost.getPlugin(), 0, DEFAULT_INTERVALL);
	}

	public static void replaceSphere(Block block, int xRadius, int yRadius, int zRadius, Material old, Material type) {
		new ReplaceSphereTask(block, xRadius, yRadius, zRadius, old, type).runTaskTimer(Aiost.getPlugin(), 0,
				DEFAULT_INTERVALL);
	}

	public static void replaceSphere(Block block1, Block block2, Material old, Material type) {
		new ReplaceSphereTask(block1, block2, old, type).runTaskTimer(Aiost.getPlugin(), 0, DEFAULT_INTERVALL);
	}

	public static void replaceSphere(Block block, int xRadius, int yRadius, int zRadius, Material old, BlockData data) {
		new ReplaceSphereDataTask(block, xRadius, yRadius, zRadius, old, data).runTaskTimer(Aiost.getPlugin(), 0,
				DEFAULT_INTERVALL);
	}

	public static void replaceSphere(Block block1, Block block2, Material old, BlockData data) {
		new ReplaceSphereDataTask(block1, block2, old, data).runTaskTimer(Aiost.getPlugin(), 0, DEFAULT_INTERVALL);
	}

	public static void breakHollowSphere(Block block, int xRadius, int yRadius, int zRadius) {
		new BreakHollowSphereTask(block, xRadius, yRadius, zRadius).runTaskTimer(Aiost.getPlugin(), 0,
				DEFAULT_INTERVALL);
	}

	public static void breakHollowSphere(Block block1, Block block2) {
		new BreakHollowSphereTask(block1, block2).runTaskTimer(Aiost.getPlugin(), 0, DEFAULT_INTERVALL);
	}

	public static void breakHollowSphereNaturally(Block block, int xRadius, int yRadius, int zRadius) {
		new BreakHollowSphereNaturallyTask(block, xRadius, yRadius, zRadius).runTaskTimer(Aiost.getPlugin(), 0,
				DEFAULT_INTERVALL);
	}

	public static void breakHollowSphereNaturally(Block block1, Block block2) {
		new BreakHollowSphereNaturallyTask(block1, block2).runTaskTimer(Aiost.getPlugin(), 0, DEFAULT_INTERVALL);
	}

	public static void setHollowSphere(Block block, int xRadius, int yRadius, int zRadius, Material type) {
		new SetHollowSphereTask(block, xRadius, yRadius, zRadius, type).runTaskTimer(Aiost.getPlugin(), 0,
				DEFAULT_INTERVALL);
	}

	public static void setHollowSphere(Block block1, Block block2, Material type) {
		new SetHollowSphereTask(block1, block2, type).runTaskTimer(Aiost.getPlugin(), 0, DEFAULT_INTERVALL);
	}

	public static void setHollowSphere(Block block, int xRadius, int yRadius, int zRadius, BlockData data) {
		new SetHollowSphereDataTask(block, xRadius, yRadius, zRadius, data).runTaskTimer(Aiost.getPlugin(), 0,
				DEFAULT_INTERVALL);
	}

	public static void setHollowSphere(Block block1, Block block2, BlockData data) {
		new SetHollowSphereDataTask(block1, block2, data).runTaskTimer(Aiost.getPlugin(), 0, DEFAULT_INTERVALL);
	}

	public static void fillHollowSphere(Block block, int xRadius, int yRadius, int zRadius, Material type) {
		new FillHollowSphereTask(block, xRadius, yRadius, zRadius, type).runTaskTimer(Aiost.getPlugin(), 0,
				DEFAULT_INTERVALL);
	}

	public static void fillHollowSphere(Block block1, Block block2, Material type) {
		new FillHollowSphereTask(block1, block2, type).runTaskTimer(Aiost.getPlugin(), 0, DEFAULT_INTERVALL);
	}

	public static void fillHollowSphere(Block block, int xRadius, int yRadius, int zRadius, BlockData data) {
		new FillHollowSphereDataTask(block, xRadius, yRadius, zRadius, data).runTaskTimer(Aiost.getPlugin(), 0,
				DEFAULT_INTERVALL);
	}

	public static void fillHollowSphere(Block block1, Block block2, BlockData data) {
		new FillHollowSphereDataTask(block1, block2, data).runTaskTimer(Aiost.getPlugin(), 0, DEFAULT_INTERVALL);
	}

	public static void replaceHollowSphere(Block block, int xRadius, int yRadius, int zRadius, Material old,
			Material type) {
		new ReplaceHollowSphereTask(block, xRadius, yRadius, zRadius, old, type).runTaskTimer(Aiost.getPlugin(), 0,
				DEFAULT_INTERVALL);
	}

	public static void replaceHollowSphere(Block block1, Block block2, Material old, Material type) {
		new ReplaceHollowSphereTask(block1, block2, old, type).runTaskTimer(Aiost.getPlugin(), 0, DEFAULT_INTERVALL);
	}

	public static void replaceHollowSphere(Block block, int xRadius, int yRadius, int zRadius, Material old,
			BlockData data) {
		new ReplaceHollowSphereDataTask(block, xRadius, yRadius, zRadius, old, data).runTaskTimer(Aiost.getPlugin(), 0,
				DEFAULT_INTERVALL);
	}

	public static void replaceHollowSphere(Block block1, Block block2, Material old, BlockData data) {
		new ReplaceHollowSphereDataTask(block1, block2, old, data).runTaskTimer(Aiost.getPlugin(), 0,
				DEFAULT_INTERVALL);
	}

	public static void breakPlane(Entity entity, Block block, int xRadius, int yRadius, int zRadius) {
		breakPlane(block, xRadius, yRadius, zRadius, getBlockFace(entity));
	}

	public static void breakPlane(Block block, int xRadius, int yRadius, int zRadius, BlockFace face) {
		new BreakPlaneTask(block, xRadius, yRadius, zRadius, face).runTaskTimer(Aiost.getPlugin(), 0,
				DEFAULT_INTERVALL);
	}

	public static void breakPlane(Block block1, Block block2, BlockFace face) {
		new BreakPlaneTask(block1, block2, face).runTaskTimer(Aiost.getPlugin(), 0, DEFAULT_INTERVALL);
	}

	public static void breakPlaneNaturally(Entity entity, Block block, int xRadius, int yRadius, int zRadius) {
		breakPlaneNaturally(block, xRadius, yRadius, zRadius, getBlockFace(entity));
	}

	public static void breakPlaneNaturally(Block block, int xRadius, int yRadius, int zRadius, BlockFace face) {
		new BreakPlaneNaturallyTask(block, xRadius, yRadius, zRadius, face).runTaskTimer(Aiost.getPlugin(), 0,
				DEFAULT_INTERVALL);
	}

	public static void breakPlaneNaturally(Block block1, Block block2, BlockFace face) {
		new BreakPlaneNaturallyTask(block1, block2, face).runTaskTimer(Aiost.getPlugin(), 0, DEFAULT_INTERVALL);
	}

	public static void setPlane(Entity entity, Block block, int xRadius, int yRadius, int zRadius, Material type) {
		setPlane(block, xRadius, yRadius, zRadius, getBlockFace(entity), type);
	}

	public static void setPlane(Block block, int xRadius, int yRadius, int zRadius, BlockFace face, Material type) {
		new SetPlaneTask(block, xRadius, yRadius, zRadius, face, type).runTaskTimer(Aiost.getPlugin(), 0,
				DEFAULT_INTERVALL);
	}

	public static void setPlane(Block block1, Block block2, BlockFace face, Material type) {
		new SetPlaneTask(block1, block2, face, type).runTaskTimer(Aiost.getPlugin(), 0, DEFAULT_INTERVALL);
	}

	public static void setPlane(Entity entity, Block block, int xRadius, int yRadius, int zRadius, BlockData data) {
		setPlane(block, xRadius, yRadius, zRadius, getBlockFace(entity), data);
	}

	public static void setPlane(Block block, int xRadius, int yRadius, int zRadius, BlockFace face, BlockData data) {
		new SetPlaneDataTask(block, xRadius, yRadius, zRadius, face, data).runTaskTimer(Aiost.getPlugin(), 0,
				DEFAULT_INTERVALL);
	}

	public static void setPlane(Block block1, Block block2, BlockFace face, BlockData data) {
		new SetPlaneDataTask(block1, block2, face, data).runTaskTimer(Aiost.getPlugin(), 0, DEFAULT_INTERVALL);
	}

	public static void fillPlane(Entity entity, Block block, int xRadius, int yRadius, int zRadius, Material type) {
		fillPlane(block, xRadius, yRadius, zRadius, getBlockFace(entity), type);
	}

	public static void fillPlane(Block block, int xRadius, int yRadius, int zRadius, BlockFace face, Material type) {
		new FillPlaneTask(block, xRadius, yRadius, zRadius, face, type).runTaskTimer(Aiost.getPlugin(), 0,
				DEFAULT_INTERVALL);
	}

	public static void fillPlane(Block block1, Block block2, BlockFace face, Material type) {
		new FillPlaneTask(block1, block2, face, type).runTaskTimer(Aiost.getPlugin(), 0, DEFAULT_INTERVALL);
	}

	public static void fillPlane(Entity entity, Block block, int xRadius, int yRadius, int zRadius, BlockData data) {
		fillPlane(block, xRadius, yRadius, zRadius, getBlockFace(entity), data);
	}

	public static void fillPlane(Block block, int xRadius, int yRadius, int zRadius, BlockFace face, BlockData data) {
		new FillPlaneDataTask(block, xRadius, yRadius, zRadius, face, data).runTaskTimer(Aiost.getPlugin(), 0,
				DEFAULT_INTERVALL);
	}

	public static void fillPlane(Block block1, Block block2, BlockFace face, BlockData data) {
		new FillPlaneDataTask(block1, block2, face, data).runTaskTimer(Aiost.getPlugin(), 0, DEFAULT_INTERVALL);
	}

	public static void replacePlane(Entity entity, Block block, int xRadius, int yRadius, int zRadius, Material old,
			Material type) {
		replacePlane(block, xRadius, yRadius, zRadius, getBlockFace(entity), old, type);
	}

	public static void replacePlane(Block block, int xRadius, int yRadius, int zRadius, BlockFace face, Material old,
			Material type) {
		new ReplacePlaneTask(block, xRadius, yRadius, zRadius, face, old, type).runTaskTimer(Aiost.getPlugin(), 0,
				DEFAULT_INTERVALL);
	}

	public static void replacePlane(Block block1, Block block2, BlockFace face, Material old, Material type) {
		new ReplacePlaneTask(block1, block2, face, old, type).runTaskTimer(Aiost.getPlugin(), 0, DEFAULT_INTERVALL);
	}

	public static void replacePlane(Entity entity, Block block, int xRadius, int yRadius, int zRadius, Material old,
			BlockData data) {
		replacePlane(block, xRadius, yRadius, zRadius, getBlockFace(entity), old, data);
	}

	public static void replacePlane(Block block, int xRadius, int yRadius, int zRadius, BlockFace face, Material old,
			BlockData data) {
		new ReplacePlaneDataTask(block, xRadius, yRadius, zRadius, face, old, data).runTaskTimer(Aiost.getPlugin(), 0,
				DEFAULT_INTERVALL);
	}

	public static void replacePlane(Block block1, Block block2, BlockFace face, Material old, BlockData data) {
		new ReplacePlaneDataTask(block1, block2, face, old, data).runTaskTimer(Aiost.getPlugin(), 0, DEFAULT_INTERVALL);
	}

	public static void buildStaffBreakEffect(Entity entity, Block block, int xRadius, int yRadius, int zRadius) {
		buildStaffBreakEffect(block, xRadius, yRadius, zRadius, getBlockFace(entity));
	}

	public static void buildStaffBreakEffect(Block block, int xRadius, int yRadius, int zRadius, BlockFace face) {
		new BuildStaffBreakTask(block, xRadius, yRadius, zRadius, face).runTaskTimer(Aiost.getPlugin(), 0,
				DEFAULT_INTERVALL);
	}

	public static void buildStaffBreakEffect(Block block1, Block block2, BlockFace face, Material type) {
		new BuildStaffBreakTask(block1, block2, face, type).runTaskTimer(Aiost.getPlugin(), 0, DEFAULT_INTERVALL);
	}

	public static void buildStaffPlaceEffect(Entity entity, Block block, int xRadius, int yRadius, int zRadius) {
		buildStaffPlaceEffect(block, xRadius, yRadius, zRadius, getBlockFace(entity));
	}

	public static void buildStaffPlaceEffect(Block block, int xRadius, int yRadius, int zRadius, BlockFace face) {
		new BuildStaffPlaceTask(block, xRadius, yRadius, zRadius, face).runTaskTimer(Aiost.getPlugin(), 0,
				DEFAULT_INTERVALL);
	}

	public static void buildStaffPlaceEffect(Block block1, Block block2, BlockFace face, Material type,
			BlockData data) {
		new BuildStaffPlaceTask(block1, block2, face, type, data).runTaskTimer(Aiost.getPlugin(), 0, DEFAULT_INTERVALL);
	}

	public static BlockFace getBlockFace(Entity entity) {
		Vector entityDirection = entity.getLocation().getDirection();
		return NMS.notchToBlockFace(
				NMS.getEnumDirection(entityDirection.getX(), entityDirection.getY(), entityDirection.getZ()));
	}

	/**
	 * This makes a ray cast!
	 * 
	 * @param player
	 * @return
	 */
	public BlockFace getBlockFace(Player player) {
		List<Block> lastTwoTargetBlocks = player.getLastTwoTargetBlocks(null, 100);
		if (lastTwoTargetBlocks.size() != 2 || !lastTwoTargetBlocks.get(1).getType().isOccluding())
			return null;
		Block targetBlock = lastTwoTargetBlocks.get(1);
		Block adjacentBlock = lastTwoTargetBlocks.get(0);
		return targetBlock.getFace(adjacentBlock);
	}

	public static abstract class WorldEditTask extends BukkitRunnable {

		protected final World world;
		protected int x;
		protected int y;
		protected int z;
		protected final int bx;
		protected final int by;
		protected final int bz;
		protected int xMin;
		protected int yMin;
		protected int zMin;
		protected int xMax;
		protected int yMax;
		protected int zMax;

		protected WorldEditTask(Block block, int radius) {
			this(block, radius, radius, radius);
		}

		protected WorldEditTask(Block block, int xRadius, int yRadius, int zRadius) {
			world = block.getWorld();
			bx = block.getX();
			by = block.getY();
			bz = block.getZ();
			init(xRadius, yRadius, zRadius);
		}

		protected WorldEditTask(Block block1, Block block2) {
			world = block1.getWorld();
			init(block1.getX(), block1.getY(), block1.getZ(), block2.getX(), block2.getY(), block2.getZ());
			bx = xMin + ((xMax - xMin) / 2);
			by = yMin + ((yMax - yMin) / 2);
			bz = zMin + ((zMax - zMin) / 2);
		}

		private void init(int xRadius, int yRadius, int zRadius) {
			x = xMin = bx - xRadius;
			y = yMin = by - yRadius;
			z = zMin = bz - zRadius;
			xMax = bx + xRadius;
			yMax = by + yRadius;
			zMax = bz + zRadius;
			if (yMin < 0)
				y = yMin = 0;
			if (yMax > world.getMaxHeight())
				yMax = world.getMaxHeight();
		}

		private void init(int x1, int y1, int z1, int x2, int y2, int z2) {
			if (x1 < x2) {
				x = xMin = x1;
				xMax = x2;
			} else {
				x = xMin = x2;
				xMax = x1;
			}
			if (y1 < y2) {
				y = yMin = y1;
				yMax = y2;
			} else {
				y = yMin = y2;
				yMax = y1;
			}
			if (z1 < z2) {
				z = zMin = z1;
				zMax = z2;
			} else {
				z = zMin = z2;
				zMax = z1;
			}
		}

		@Override
		@Nullable
		public synchronized BukkitTask runTask(Plugin plugin) throws IllegalArgumentException, IllegalStateException {
			if (ACTIVE_TASKS.size() >= MAX_TASKS)
				return null;
			ACTIVE_TASKS.add(this);
			return super.runTask(plugin);
		}

		@Override
		@Nullable
		public synchronized BukkitTask runTaskAsynchronously(Plugin plugin)
				throws IllegalArgumentException, IllegalStateException {
			if (ACTIVE_TASKS.size() >= MAX_TASKS)
				return null;
			ACTIVE_TASKS.add(this);
			return super.runTaskAsynchronously(plugin);
		}

		@Override
		@Nullable
		public synchronized BukkitTask runTaskLater(Plugin plugin, long delay)
				throws IllegalArgumentException, IllegalStateException {
			if (ACTIVE_TASKS.size() >= MAX_TASKS)
				return null;
			ACTIVE_TASKS.add(this);
			return super.runTaskLater(plugin, delay);
		}

		@Override
		@Nullable
		public synchronized BukkitTask runTaskLaterAsynchronously(Plugin plugin, long delay)
				throws IllegalArgumentException, IllegalStateException {
			if (ACTIVE_TASKS.size() >= MAX_TASKS)
				return null;
			ACTIVE_TASKS.add(this);
			return super.runTaskLaterAsynchronously(plugin, delay);
		}

		@Override
		@Nullable
		public synchronized BukkitTask runTaskTimer(Plugin plugin, long delay, long period)
				throws IllegalArgumentException, IllegalStateException {
			if (ACTIVE_TASKS.size() >= MAX_TASKS)
				return null;
			ACTIVE_TASKS.add(this);
			return super.runTaskTimer(plugin, delay, period);
		}

		@Override
		@Nullable
		public synchronized BukkitTask runTaskTimerAsynchronously(Plugin plugin, long delay, long period)
				throws IllegalArgumentException, IllegalStateException {
			if (ACTIVE_TASKS.size() >= MAX_TASKS)
				return null;
			ACTIVE_TASKS.add(this);
			return super.runTaskTimerAsynchronously(plugin, delay, period);
		}

		@Override
		public synchronized void cancel() throws IllegalStateException {
			ACTIVE_TASKS.remove(this);
			super.cancel();
		}
	}

	public static abstract class BlockTask extends WorldEditTask {

		protected BlockTask(Block block, int xRadius, int yRadius, int zRadius) {
			super(block, xRadius, yRadius, zRadius);
		}

		protected BlockTask(Block block1, Block block2) {
			super(block1, block2);
		}

		@Override
		public void run() {
			int steps = 0;
			for (; y <= yMax; y++) {
				for (; x <= xMax; x++) {
					for (; z <= zMax; z++) {
						if (steps++ > MAX_STEPS)
							return;
						Block block = world.getBlockAt(x, y, z);
						if (!block.getChunk().isLoaded()) {
							cancel();
							return;
						}
						handle(block);
					}
					z = zMin;
				}
				x = xMin;
			}
			cancel();
		}

		protected abstract void handle(Block block);
	}

	private static class BreakBlockTask extends BlockTask {

		private BreakBlockTask(Block block, int xRadius, int yRadius, int zRadius) {
			super(block, xRadius, yRadius, zRadius);
		}

		private BreakBlockTask(Block block1, Block block2) {
			super(block1, block2);
		}

		@Override
		protected void handle(Block block) {
			breakBlock(block);
		}
	}

	private static class BreakBlockNaturallyTask extends BlockTask {

		private BreakBlockNaturallyTask(Block block, int xRadius, int yRadius, int zRadius) {
			super(block, xRadius, yRadius, zRadius);
		}

		private BreakBlockNaturallyTask(Block block1, Block block2) {
			super(block1, block2);
		}

		@Override
		protected void handle(Block block) {
			block.breakNaturally();
		}
	}

	private static class SetBlockTask extends BlockTask {

		protected final Material type;

		private SetBlockTask(Block block, int xRadius, int yRadius, int zRadius, Material type) {
			super(block, xRadius, yRadius, zRadius);
			this.type = type;
		}

		private SetBlockTask(Block block1, Block block2, Material type) {
			super(block1, block2);
			this.type = type;
		}

		@Override
		protected void handle(Block block) {
			setBlock(block, type);
		}
	}

	private static class SetBlockDataTask extends BlockTask {

		protected final BlockData data;

		private SetBlockDataTask(Block block, int xRadius, int yRadius, int zRadius, BlockData data) {
			super(block, xRadius, yRadius, zRadius);
			this.data = data;
		}

		private SetBlockDataTask(Block block1, Block block2, BlockData data) {
			super(block1, block2);
			this.data = data;
		}

		@Override
		protected void handle(Block block) {
			setBlock(block, data);
		}
	}

	private static class FillBlockTask extends BlockTask {

		protected final Material type;

		private FillBlockTask(Block block, int xRadius, int yRadius, int zRadius, Material type) {
			super(block, xRadius, yRadius, zRadius);
			this.type = type;
		}

		private FillBlockTask(Block block1, Block block2, Material type) {
			super(block1, block2);
			this.type = type;
		}

		@Override
		protected void handle(Block block) {
			setEmptyBlock(block, type);
		}
	}

	private static class FillBlockDataTask extends BlockTask {

		protected final BlockData data;

		private FillBlockDataTask(Block block, int xRadius, int yRadius, int zRadius, BlockData data) {
			super(block, xRadius, yRadius, zRadius);
			this.data = data;
		}

		private FillBlockDataTask(Block block1, Block block2, BlockData data) {
			super(block1, block2);
			this.data = data;
		}

		@Override
		protected void handle(Block block) {
			setEmptyBlock(block, data);
		}
	}

	private static class ReplaceBlockTask extends FillBlockTask {

		protected final Material old;

		private ReplaceBlockTask(Block block, int xRadius, int yRadius, int zRadius, Material old, Material type) {
			super(block, xRadius, yRadius, zRadius, type);
			this.old = old;
		}

		private ReplaceBlockTask(Block block1, Block block2, Material old, Material type) {
			super(block1, block2, type);
			this.old = old;
		}

		@Override
		protected void handle(Block block) {
			replaceBlock(block, old, type);
		}
	}

	private static class ReplaceBlockDataTask extends FillBlockDataTask {

		protected final Material old;

		private ReplaceBlockDataTask(Block block, int xRadius, int yRadius, int zRadius, Material old, BlockData data) {
			super(block, xRadius, yRadius, zRadius, data);
			this.old = old;
		}

		private ReplaceBlockDataTask(Block block1, Block block2, Material old, BlockData data) {
			super(block1, block2, data);
			this.old = old;
		}

		@Override
		protected void handle(Block block) {
			replaceBlock(block, old, data);
		}
	}

	public static abstract class BallTask extends WorldEditTask {

		protected final int radiusSqrt;

		protected BallTask(Block block, int radius) {
			super(block, radius);
			radiusSqrt = radius * radius;
		}

		@Override
		public void run() {
			int steps = 0;
			for (; y <= yMax; y++) {
				for (; x <= xMax; x++) {
					for (; z <= zMax; z++) {
						if (steps++ > MAX_STEPS)
							return;
						double distance = ((bx - x) * (bx - x) + ((bz - z) * (bz - z)) + ((by - y) * (by - y)));
						if (distance >= radiusSqrt)
							continue;
						Block block = world.getBlockAt(x, y, z);
						if (!block.getChunk().isLoaded()) {
							cancel();
							return;
						}
						handle(block);
					}
					z = zMin;
				}
				x = xMin;
			}
			cancel();
		}

		protected abstract void handle(Block block);
	}

	private static class BreakBallTask extends BallTask {

		private BreakBallTask(Block block, int radius) {
			super(block, radius);
		}

		@Override
		protected void handle(Block block) {
			breakBlock(block);
		}
	}

	private static class BreakBallNaturallyTask extends BallTask {

		private BreakBallNaturallyTask(Block block, int radius) {
			super(block, radius);
		}

		@Override
		protected void handle(Block block) {
			block.breakNaturally();
		}
	}

	private static class SetBallTask extends BallTask {

		protected final Material type;

		private SetBallTask(Block block, int radius, Material type) {
			super(block, radius);
			this.type = type;
		}

		@Override
		protected void handle(Block block) {
			setBlock(block, type);
		}
	}

	private static class SetBallDataTask extends BallTask {

		protected final BlockData data;

		private SetBallDataTask(Block block, int radius, BlockData data) {
			super(block, radius);
			this.data = data;
		}

		@Override
		protected void handle(Block block) {
			setBlock(block, data);
		}
	}

	private static class FillBallTask extends BallTask {

		protected final Material type;

		private FillBallTask(Block block, int radius, Material type) {
			super(block, radius);
			this.type = type;
		}

		@Override
		protected void handle(Block block) {
			setEmptyBlock(block, type);
		}
	}

	private static class FillBallDataTask extends BallTask {

		protected final BlockData data;

		private FillBallDataTask(Block block, int radius, BlockData data) {
			super(block, radius);
			this.data = data;
		}

		@Override
		protected void handle(Block block) {
			setEmptyBlock(block, data);
		}
	}

	private static class ReplaceBallTask extends FillBallTask {

		protected final Material old;

		private ReplaceBallTask(Block block, int radius, Material old, Material type) {
			super(block, radius, type);
			this.old = old;
		}

		@Override
		protected void handle(Block block) {
			replaceBlock(block, old, type);
		}
	}

	private static class ReplaceBallDataTask extends FillBallDataTask {

		protected final Material old;

		private ReplaceBallDataTask(Block block, int radius, Material old, BlockData data) {
			super(block, radius, data);
			this.old = old;
		}

		@Override
		protected void handle(Block block) {
			replaceBlock(block, old, data);
		}
	}

	public static abstract class HollowBallTask extends BallTask {

		protected final int radiusSqrtHollow;

		protected HollowBallTask(Block block, int radius) {
			super(block, radius);
			radiusSqrtHollow = (radius - 1) * (radius - 1);
		}

		@Override
		public void run() {
			int steps = 0;
			for (; y <= yMax; y++) {
				for (; x <= xMax; x++) {
					for (; z <= zMax; z++) {
						if (steps++ > MAX_STEPS)
							return;
						double distance = ((bx - x) * (bx - x) + ((bz - z) * (bz - z)) + ((by - y) * (by - y)));
						if (distance >= radiusSqrt || distance < radiusSqrtHollow)
							continue;
						Block block = world.getBlockAt(x, y, z);
						if (!block.getChunk().isLoaded()) {
							cancel();
							return;
						}
						handle(block);
					}
					z = zMin;
				}
				x = xMin;
			}
			cancel();
		}
	}

	private static class BreakHollowBallTask extends HollowBallTask {

		private BreakHollowBallTask(Block block, int radius) {
			super(block, radius);
		}

		@Override
		protected void handle(Block block) {
			breakBlock(block);
		}
	}

	private static class BreakHollowBallNaturallyTask extends HollowBallTask {

		private BreakHollowBallNaturallyTask(Block block, int radius) {
			super(block, radius);
		}

		@Override
		protected void handle(Block block) {
			block.breakNaturally();
		}
	}

	private static class SetHollowBallTask extends HollowBallTask {

		protected final Material type;

		private SetHollowBallTask(Block block, int radius, Material type) {
			super(block, radius);
			this.type = type;
		}

		@Override
		protected void handle(Block block) {
			setBlock(block, type);
		}
	}

	private static class SetHollowBallDataTask extends HollowBallTask {

		protected final BlockData data;

		private SetHollowBallDataTask(Block block, int radius, BlockData data) {
			super(block, radius);
			this.data = data;
		}

		@Override
		protected void handle(Block block) {
			setBlock(block, data);
		}
	}

	private static class FillHollowBallTask extends HollowBallTask {

		protected final Material type;

		private FillHollowBallTask(Block block, int radius, Material type) {
			super(block, radius);
			this.type = type;
		}

		@Override
		protected void handle(Block block) {
			setEmptyBlock(block, type);
		}
	}

	private static class FillHollowBallDataTask extends HollowBallTask {

		protected final BlockData data;

		private FillHollowBallDataTask(Block block, int radius, BlockData data) {
			super(block, radius);
			this.data = data;
		}

		@Override
		protected void handle(Block block) {
			setEmptyBlock(block, data);
		}
	}

	private static class ReplaceHollowBallTask extends FillHollowBallTask {

		protected final Material old;

		private ReplaceHollowBallTask(Block block, int radius, Material old, Material type) {
			super(block, radius, type);
			this.old = old;
		}

		@Override
		protected void handle(Block block) {
			replaceBlock(block, old, type);
		}
	}

	private static class ReplaceHollowBallDataTask extends FillBallDataTask {

		protected final Material old;

		private ReplaceHollowBallDataTask(Block block, int radius, Material old, BlockData data) {
			super(block, radius, data);
			this.old = old;
		}

		@Override
		protected void handle(Block block) {
			replaceBlock(block, old, data);
		}
	}

	public static abstract class SphereTask extends WorldEditTask {

		protected final int xRadiusSqrt;
		protected final int yRadiusSqrt;
		protected final int zRadiusSqrt;

		protected SphereTask(Block block, int xRadius, int yRadius, int zRadius) {
			super(block, xRadius, yRadius, zRadius);
			xRadiusSqrt = xRadius * xRadius;
			yRadiusSqrt = yRadius * yRadius;
			zRadiusSqrt = zRadius * zRadius;
		}

		protected SphereTask(Block block1, Block block2) {
			super(block1, block2);
			int xRadius = xMax - bx;
			int yRadius = yMax - by;
			int zRadius = zMax - bz;
			xRadiusSqrt = xRadius * xRadius;
			yRadiusSqrt = yRadius * yRadius;
			zRadiusSqrt = zRadius * zRadius;
		}

		@Override
		public void run() {
			int steps = 0;
			for (; y <= yMax; y++) {
				for (; x <= xMax; x++) {
					for (; z <= zMax; z++) {
						if (steps++ > MAX_STEPS)
							return;
						double distance = ((bx - x) * (bx - x) + ((bz - z) * (bz - z)) + ((by - y) * (by - y)));
						if (distance >= yRadiusSqrt && distance >= xRadiusSqrt && distance >= zRadiusSqrt)
							continue;
						Block block = world.getBlockAt(x, y, z);
						if (!block.getChunk().isLoaded()) {
							cancel();
							return;
						}
						handle(block);
					}
					z = zMin;
				}
				x = xMin;
			}
			cancel();
		}

		protected abstract void handle(Block block);
	}

	private static class BreakSphereTask extends SphereTask {

		private BreakSphereTask(Block block, int xRadius, int yRadius, int zRadius) {
			super(block, xRadius, yRadius, zRadius);
		}

		private BreakSphereTask(Block block1, Block block2) {
			super(block1, block2);
		}

		@Override
		protected void handle(Block block) {
			breakBlock(block);
		}
	}

	private static class BreakSphereNaturallyTask extends SphereTask {

		private BreakSphereNaturallyTask(Block block, int xRadius, int yRadius, int zRadius) {
			super(block, xRadius, yRadius, zRadius);
		}

		private BreakSphereNaturallyTask(Block block1, Block block2) {
			super(block1, block2);
		}

		@Override
		protected void handle(Block block) {
			block.breakNaturally();
		}
	}

	private static class SetSphereTask extends SphereTask {

		protected final Material type;

		private SetSphereTask(Block block, int xRadius, int yRadius, int zRadius, Material type) {
			super(block, xRadius, yRadius, zRadius);
			this.type = type;
		}

		private SetSphereTask(Block block1, Block block2, Material type) {
			super(block1, block2);
			this.type = type;
		}

		@Override
		protected void handle(Block block) {
			setBlock(block, type);
		}
	}

	private static class SetSphereDataTask extends SphereTask {

		protected final BlockData data;

		private SetSphereDataTask(Block block, int xRadius, int yRadius, int zRadius, BlockData data) {
			super(block, xRadius, yRadius, zRadius);
			this.data = data;
		}

		private SetSphereDataTask(Block block1, Block block2, BlockData data) {
			super(block1, block2);
			this.data = data;
		}

		@Override
		protected void handle(Block block) {
			setBlock(block, data);
		}
	}

	private static class FillSphereTask extends SphereTask {

		protected final Material type;

		private FillSphereTask(Block block, int xRadius, int yRadius, int zRadius, Material type) {
			super(block, xRadius, yRadius, zRadius);
			this.type = type;
		}

		private FillSphereTask(Block block1, Block block2, Material type) {
			super(block1, block2);
			this.type = type;
		}

		@Override
		protected void handle(Block block) {
			setEmptyBlock(block, type);
		}
	}

	private static class FillSphereDataTask extends SphereTask {

		protected final BlockData data;

		private FillSphereDataTask(Block block, int xRadius, int yRadius, int zRadius, BlockData data) {
			super(block, xRadius, yRadius, zRadius);
			this.data = data;
		}

		private FillSphereDataTask(Block block1, Block block2, BlockData data) {
			super(block1, block2);
			this.data = data;
		}

		@Override
		protected void handle(Block block) {
			setEmptyBlock(block, data);
		}
	}

	private static class ReplaceSphereTask extends FillSphereTask {

		protected final Material old;

		private ReplaceSphereTask(Block block, int xRadius, int yRadius, int zRadius, Material old, Material type) {
			super(block, xRadius, yRadius, zRadius, type);
			this.old = old;
		}

		private ReplaceSphereTask(Block block1, Block block2, Material old, Material type) {
			super(block1, block2, type);
			this.old = old;
		}

		@Override
		protected void handle(Block block) {
			replaceBlock(block, old, type);
		}
	}

	private static class ReplaceSphereDataTask extends FillSphereDataTask {

		protected final Material old;

		private ReplaceSphereDataTask(Block block, int xRadius, int yRadius, int zRadius, Material old,
				BlockData data) {
			super(block, xRadius, yRadius, zRadius, data);
			this.old = old;
		}

		private ReplaceSphereDataTask(Block block1, Block block2, Material old, BlockData data) {
			super(block1, block2, data);
			this.old = old;
		}

		@Override
		protected void handle(Block block) {
			replaceBlock(block, old, data);
		}
	}

	public static abstract class HollowSphereTask extends WorldEditTask {

		protected final int xRadiusSqrt;
		protected final int yRadiusSqrt;
		protected final int zRadiusSqrt;
		protected final int xRadiusSqrtHollow;
		protected final int yRadiusSqrtHollow;
		protected final int zRadiusSqrtHollow;

		protected HollowSphereTask(Block block, int xRadius, int yRadius, int zRadius) {
			super(block, xRadius, yRadius, zRadius);
			xRadiusSqrt = xRadius * xRadius;
			yRadiusSqrt = yRadius * yRadius;
			zRadiusSqrt = zRadius * zRadius;
			xRadiusSqrtHollow = (xRadius - 1) * (xRadius - 1);
			yRadiusSqrtHollow = (yRadius - 1) * (yRadius - 1);
			zRadiusSqrtHollow = (zRadius - 1) * (zRadius - 1);
		}

		protected HollowSphereTask(Block block1, Block block2) {
			super(block1, block2);
			int xRadius = xMax - bx;
			int yRadius = yMax - by;
			int zRadius = zMax - bz;
			xRadiusSqrt = xRadius * xRadius;
			yRadiusSqrt = yRadius * yRadius;
			zRadiusSqrt = zRadius * zRadius;
			xRadiusSqrtHollow = (xRadius - 1) * (xRadius - 1);
			yRadiusSqrtHollow = (yRadius - 1) * (yRadius - 1);
			zRadiusSqrtHollow = (zRadius - 1) * (zRadius - 1);
		}

		@Override
		public void run() {
			int steps = 0;
			for (; y <= yMax; y++) {
				for (; x <= xMax; x++) {
					for (; z <= zMax; z++) {
						if (steps++ > MAX_STEPS)
							return;
						double distance = ((bx - x) * (bx - x) + ((bz - z) * (bz - z)) + ((by - y) * (by - y)));
						if (distance >= yRadiusSqrt && distance >= xRadiusSqrt && distance >= zRadiusSqrt)
							continue;
						if (distance < xRadiusSqrtHollow && distance < yRadiusSqrtHollow
								&& distance < zRadiusSqrtHollow)
							continue;
						Block block = world.getBlockAt(x, y, z);
						if (!block.getChunk().isLoaded()) {
							cancel();
							return;
						}
						handle(block);
					}
					z = zMin;
				}
				x = xMin;
			}
			cancel();
		}

		protected abstract void handle(Block block);
	}

	private static class BreakHollowSphereTask extends HollowSphereTask {

		private BreakHollowSphereTask(Block block, int xRadius, int yRadius, int zRadius) {
			super(block, xRadius, yRadius, zRadius);
		}

		private BreakHollowSphereTask(Block block1, Block block2) {
			super(block1, block2);
		}

		@Override
		protected void handle(Block block) {
			breakBlock(block);
		}
	}

	private static class BreakHollowSphereNaturallyTask extends HollowSphereTask {

		private BreakHollowSphereNaturallyTask(Block block, int xRadius, int yRadius, int zRadius) {
			super(block, xRadius, yRadius, zRadius);
		}

		private BreakHollowSphereNaturallyTask(Block block1, Block block2) {
			super(block1, block2);
		}

		@Override
		protected void handle(Block block) {
			block.breakNaturally();
		}
	}

	private static class SetHollowSphereTask extends HollowSphereTask {

		protected final Material type;

		private SetHollowSphereTask(Block block, int xRadius, int yRadius, int zRadius, Material type) {
			super(block, xRadius, yRadius, zRadius);
			this.type = type;
		}

		private SetHollowSphereTask(Block block1, Block block2, Material type) {
			super(block1, block2);
			this.type = type;
		}

		@Override
		protected void handle(Block block) {
			setBlock(block, type);
		}
	}

	private static class SetHollowSphereDataTask extends HollowSphereTask {

		protected final BlockData data;

		private SetHollowSphereDataTask(Block block, int xRadius, int yRadius, int zRadius, BlockData data) {
			super(block, xRadius, yRadius, zRadius);
			this.data = data;
		}

		private SetHollowSphereDataTask(Block block1, Block block2, BlockData data) {
			super(block1, block2);
			this.data = data;
		}

		@Override
		protected void handle(Block block) {
			setBlock(block, data);
		}
	}

	private static class FillHollowSphereTask extends HollowSphereTask {

		protected final Material type;

		private FillHollowSphereTask(Block block, int xRadius, int yRadius, int zRadius, Material type) {
			super(block, xRadius, yRadius, zRadius);
			this.type = type;
		}

		private FillHollowSphereTask(Block block1, Block block2, Material type) {
			super(block1, block2);
			this.type = type;
		}

		@Override
		protected void handle(Block block) {
			setEmptyBlock(block, type);
		}
	}

	private static class FillHollowSphereDataTask extends HollowSphereTask {

		protected final BlockData data;

		private FillHollowSphereDataTask(Block block, int xRadius, int yRadius, int zRadius, BlockData data) {
			super(block, xRadius, yRadius, zRadius);
			this.data = data;
		}

		private FillHollowSphereDataTask(Block block1, Block block2, BlockData data) {
			super(block1, block2);
			this.data = data;
		}

		@Override
		protected void handle(Block block) {
			setEmptyBlock(block, data);
		}
	}

	private static class ReplaceHollowSphereTask extends FillHollowSphereTask {

		protected final Material old;

		private ReplaceHollowSphereTask(Block block, int xRadius, int yRadius, int zRadius, Material old,
				Material type) {
			super(block, xRadius, yRadius, zRadius, type);
			this.old = old;
		}

		private ReplaceHollowSphereTask(Block block1, Block block2, Material old, Material type) {
			super(block1, block2, type);
			this.old = old;
		}

		@Override
		protected void handle(Block block) {
			replaceBlock(block, old, type);
		}
	}

	private static class ReplaceHollowSphereDataTask extends FillHollowSphereDataTask {

		protected final Material old;

		private ReplaceHollowSphereDataTask(Block block, int xRadius, int yRadius, int zRadius, Material old,
				BlockData data) {
			super(block, xRadius, yRadius, zRadius, data);
			this.old = old;
		}

		private ReplaceHollowSphereDataTask(Block block1, Block block2, Material old, BlockData data) {
			super(block1, block2, data);
			this.old = old;
		}

		@Override
		protected void handle(Block block) {
			replaceBlock(block, old, data);
		}
	}

	public static abstract class PlaneTask extends WorldEditTask {

		protected final BlockFace face;

		protected PlaneTask(Block block, int xRadius, int yRadius, int zRadius, BlockFace face) {
			super(block, xRadius, yRadius, zRadius);
			this.face = face;
		}

		protected PlaneTask(Block block1, Block block2, BlockFace face) {
			super(block1, block2);
			this.face = face;
		}

		@Override
		public void run() {
			int steps = 0;
			if (face == BlockFace.DOWN || face == BlockFace.UP) {
				for (; x <= xMax; x++) {
					for (; z <= zMax; z++) {
						if (steps++ > MAX_STEPS)
							return;
						Block block = world.getBlockAt(x, by, z);
						if (!block.getChunk().isLoaded()) {
							cancel();
							return;
						}
						handle(block);
					}
					z = zMin;
				}
			} else if (face == BlockFace.EAST || face == BlockFace.WEST) {
				for (; y <= yMax; y++) {
					for (; z <= zMax; z++) {
						if (steps++ > MAX_STEPS)
							return;
						Block block = world.getBlockAt(bx, y, z);
						if (!block.getChunk().isLoaded()) {
							cancel();
							return;
						}
						handle(block);
					}
					z = zMin;
				}
			} else if (face == BlockFace.NORTH || face == BlockFace.SOUTH) {
				for (; y <= yMax; y++) {
					for (; x <= xMax; x++) {
						if (steps++ > MAX_STEPS)
							return;
						Block block = world.getBlockAt(x, y, bz);
						if (!block.getChunk().isLoaded()) {
							cancel();
							return;
						}
						handle(block);
					}
					x = xMin;
				}
			}
			cancel();
		}

		protected abstract void handle(Block block);
	}

	private static class BreakPlaneTask extends PlaneTask {

		private BreakPlaneTask(Block block, int xRadius, int yRadius, int zRadius, BlockFace face) {
			super(block, xRadius, yRadius, zRadius, face);
		}

		private BreakPlaneTask(Block block1, Block block2, BlockFace face) {
			super(block1, block2, face);
		}

		@Override
		protected void handle(Block block) {
			breakBlock(block);
		}
	}

	private static class BreakPlaneNaturallyTask extends PlaneTask {

		private BreakPlaneNaturallyTask(Block block, int xRadius, int yRadius, int zRadius, BlockFace face) {
			super(block, xRadius, yRadius, zRadius, face);
		}

		private BreakPlaneNaturallyTask(Block block1, Block block2, BlockFace face) {
			super(block1, block2, face);
		}

		@Override
		protected void handle(Block block) {
			block.breakNaturally();
		}
	}

	private static class SetPlaneTask extends PlaneTask {

		protected final Material type;

		private SetPlaneTask(Block block, int xRadius, int yRadius, int zRadius, BlockFace face, Material type) {
			super(block, xRadius, yRadius, zRadius, face);
			this.type = type;
		}

		private SetPlaneTask(Block block1, Block block2, BlockFace face, Material type) {
			super(block1, block2, face);
			this.type = type;
		}

		@Override
		protected void handle(Block block) {
			setBlock(block, type);
		}
	}

	private static class SetPlaneDataTask extends PlaneTask {

		protected final BlockData data;

		private SetPlaneDataTask(Block block, int xRadius, int yRadius, int zRadius, BlockFace face, BlockData data) {
			super(block, xRadius, yRadius, zRadius, face);
			this.data = data;
		}

		private SetPlaneDataTask(Block block1, Block block2, BlockFace face, BlockData data) {
			super(block1, block2, face);
			this.data = data;
		}

		@Override
		protected void handle(Block block) {
			setBlock(block, data);
		}
	}

	private static class FillPlaneTask extends PlaneTask {

		protected final Material type;

		private FillPlaneTask(Block block, int xRadius, int yRadius, int zRadius, BlockFace face, Material type) {
			super(block, xRadius, yRadius, zRadius, face);
			this.type = type;
		}

		private FillPlaneTask(Block block1, Block block2, BlockFace face, Material type) {
			super(block1, block2, face);
			this.type = type;
		}

		@Override
		protected void handle(Block block) {
			setEmptyBlock(block, type);
		}
	}

	private static class FillPlaneDataTask extends PlaneTask {

		protected final BlockData data;

		private FillPlaneDataTask(Block block, int xRadius, int yRadius, int zRadius, BlockFace face, BlockData data) {
			super(block, xRadius, yRadius, zRadius, face);
			this.data = data;
		}

		private FillPlaneDataTask(Block block1, Block block2, BlockFace face, BlockData data) {
			super(block1, block2, face);
			this.data = data;
		}

		@Override
		protected void handle(Block block) {
			setEmptyBlock(block, data);
		}
	}

	private static class ReplacePlaneTask extends FillPlaneTask {

		protected final Material old;

		private ReplacePlaneTask(Block block, int xRadius, int yRadius, int zRadius, BlockFace face, Material old,
				Material type) {
			super(block, xRadius, yRadius, zRadius, face, type);
			this.old = old;
		}

		private ReplacePlaneTask(Block block1, Block block2, BlockFace face, Material old, Material type) {
			super(block1, block2, face, type);
			this.old = old;
		}

		@Override
		protected void handle(Block block) {
			replaceBlock(block, old, type);
		}
	}

	private static class ReplacePlaneDataTask extends FillPlaneDataTask {

		protected final Material old;

		private ReplacePlaneDataTask(Block block, int xRadius, int yRadius, int zRadius, BlockFace face, Material old,
				BlockData data) {
			super(block, xRadius, yRadius, zRadius, face, data);
			this.old = old;
		}

		private ReplacePlaneDataTask(Block block1, Block block2, BlockFace face, Material old, BlockData data) {
			super(block1, block2, face, data);
			this.old = old;
		}

		@Override
		protected void handle(Block block) {
			replaceBlock(block, old, data);
		}
	}

	public static class BuildStaffBreakTask extends WorldEditTask {

		protected final BlockFace face;
		protected final Material type;

		protected BuildStaffBreakTask(Block block, int xRadius, int yRadius, int zRadius, BlockFace face) {
			super(block, xRadius, yRadius, zRadius);
			this.face = face;
			type = block.getType();
		}

		protected BuildStaffBreakTask(Block block1, Block block2, BlockFace face, Material type) {
			super(block1, block2);
			this.face = face;
			this.type = type;
		}

		@Override
		public void run() {
			int steps = 0;
			if (face == BlockFace.UP) {
				for (; x <= xMax; x++) {
					for (; z <= zMax; z++) {
						if (steps++ > MAX_STEPS)
							return;
						if (world.getBlockAt(x, by + 1, z).getType() != Material.AIR)
							continue;
						Block block = world.getBlockAt(x, by, z);
						if (!block.getChunk().isLoaded()) {
							cancel();
							return;
						}
						breakBlockNaturally(block, type);
					}
					z = zMin;
				}
			} else if (face == BlockFace.DOWN) {
				for (; x <= xMax; x++) {
					for (; z <= zMax; z++) {
						if (steps++ > MAX_STEPS)
							return;
						if (world.getBlockAt(x, by - 1, z).getType() != Material.AIR)
							continue;
						Block block = world.getBlockAt(x, by, z);
						if (!block.getChunk().isLoaded()) {
							cancel();
							return;
						}
						breakBlockNaturally(block, type);
					}
					z = zMin;
				}
			} else if (face == BlockFace.EAST) {
				for (; y <= yMax; y++) {
					for (; z <= zMax; z++) {
						if (steps++ > MAX_STEPS)
							return;
						if (world.getBlockAt(bx + 1, y, z).getType() != Material.AIR)
							continue;
						Block block = world.getBlockAt(bx, y, z);
						if (!block.getChunk().isLoaded()) {
							cancel();
							return;
						}
						breakBlockNaturally(block, type);
					}
					z = zMin;
				}
			} else if (face == BlockFace.WEST) {
				for (; y <= yMax; y++) {
					for (; z <= zMax; z++) {
						if (steps++ > MAX_STEPS)
							return;
						if (world.getBlockAt(bx - 1, y, z).getType() != Material.AIR)
							continue;
						Block block = world.getBlockAt(bx, y, z);
						if (!block.getChunk().isLoaded()) {
							cancel();
							return;
						}
						breakBlockNaturally(block, type);
					}
					z = zMin;
				}
			} else if (face == BlockFace.NORTH) {
				for (; y <= yMax; y++) {
					for (; x <= xMax; x++) {
						if (steps++ > MAX_STEPS)
							return;
						if (world.getBlockAt(x, y, bz - 1).getType() != Material.AIR)
							continue;
						Block block = world.getBlockAt(x, y, bz);
						if (!block.getChunk().isLoaded()) {
							cancel();
							return;
						}
						breakBlockNaturally(block, type);
					}
					x = xMin;
				}
			} else if (face == BlockFace.SOUTH) {
				for (; y <= yMax; y++) {
					for (; x <= xMax; x++) {
						if (steps++ > MAX_STEPS)
							return;
						if (world.getBlockAt(x, y, bz + 1).getType() != Material.AIR)
							continue;
						Block block = world.getBlockAt(x, y, bz);
						if (!block.getChunk().isLoaded()) {
							cancel();
							return;
						}
						breakBlockNaturally(block, type);
					}
					x = xMin;
				}
			}
			cancel();
		}
	}

	public static class BuildStaffPlaceTask extends WorldEditTask {

		protected final BlockFace face;
		protected final Material type;
		protected final BlockData data;

		protected BuildStaffPlaceTask(Block block, int xRadius, int yRadius, int zRadius, BlockFace face) {
			super(block, xRadius, yRadius, zRadius);
			this.face = face;
			type = block.getType();
			data = block.getBlockData();
		}

		protected BuildStaffPlaceTask(Block block1, Block block2, BlockFace face, Material type, BlockData data) {
			super(block1, block2);
			this.face = face;
			this.type = type;
			this.data = data;
		}

		@Override
		public void run() {
			int steps = 0;
			if (face == BlockFace.UP) {
				for (; x <= xMax; x++) {
					for (; z <= zMax; z++) {
						if (steps++ > MAX_STEPS)
							return;
						if (world.getBlockAt(x, by, z).getType() != type)
							continue;
						Block block = world.getBlockAt(x, by + 1, z);
						if (!block.getChunk().isLoaded()) {
							cancel();
							return;
						}
						setEmptyBlock(block, data);
					}
					z = zMin;
				}
			} else if (face == BlockFace.DOWN) {
				for (; x <= xMax; x++) {
					for (; z <= zMax; z++) {
						if (steps++ > MAX_STEPS)
							return;
						if (world.getBlockAt(x, by, z).getType() != type)
							continue;
						Block block = world.getBlockAt(x, by - 1, z);
						if (!block.getChunk().isLoaded()) {
							cancel();
							return;
						}
						setEmptyBlock(block, data);
					}
					z = zMin;
				}
			} else if (face == BlockFace.EAST) {
				for (; y <= yMax; y++) {
					for (; z <= zMax; z++) {
						if (steps++ > MAX_STEPS)
							return;
						if (world.getBlockAt(bx, y, z).getType() != type)
							continue;
						Block block = world.getBlockAt(bx + 1, y, z);
						if (!block.getChunk().isLoaded()) {
							cancel();
							return;
						}
						setEmptyBlock(block, data);
					}
					z = zMin;
				}
			} else if (face == BlockFace.WEST) {
				for (; y <= yMax; y++) {
					for (; z <= zMax; z++) {
						if (steps++ > MAX_STEPS)
							return;
						if (world.getBlockAt(bx, y, z).getType() != type)
							continue;
						Block block = world.getBlockAt(bx - 1, y, z);
						if (!block.getChunk().isLoaded()) {
							cancel();
							return;
						}
						setEmptyBlock(block, data);
					}
					z = zMin;
				}
			} else if (face == BlockFace.NORTH) {
				for (; y <= yMax; y++) {
					for (; x <= xMax; x++) {
						if (steps++ > MAX_STEPS)
							return;
						if (world.getBlockAt(x, y, bz).getType() != type)
							continue;
						Block block = world.getBlockAt(x, y, bz - 1);
						if (!block.getChunk().isLoaded()) {
							cancel();
							return;
						}
						setEmptyBlock(block, data);
					}
					x = xMin;
				}
			} else if (face == BlockFace.SOUTH) {
				for (; y <= yMax; y++) {
					for (; x <= xMax; x++) {
						if (steps++ > MAX_STEPS)
							return;
						if (world.getBlockAt(x, y, bz).getType() != type)
							continue;
						Block block = world.getBlockAt(x, y, bz + 1);
						if (!block.getChunk().isLoaded()) {
							cancel();
							return;
						}
						setEmptyBlock(block, data);
					}
					x = xMin;
				}
			}
			cancel();
		}
	}
}
