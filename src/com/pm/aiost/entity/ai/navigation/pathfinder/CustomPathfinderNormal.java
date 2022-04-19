package com.pm.aiost.entity.ai.navigation.pathfinder;

import java.util.EnumSet;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.collect.Sets;
import com.pm.aiost.entity.CustomInsentient;

import net.minecraft.server.v1_15_R1.AxisAlignedBB;
import net.minecraft.server.v1_15_R1.Block;
import net.minecraft.server.v1_15_R1.BlockDoor;
import net.minecraft.server.v1_15_R1.BlockFenceGate;
import net.minecraft.server.v1_15_R1.BlockLeaves;
import net.minecraft.server.v1_15_R1.BlockMinecartTrackAbstract;
import net.minecraft.server.v1_15_R1.BlockPosition;
import net.minecraft.server.v1_15_R1.BlockPosition.PooledBlockPosition;
import net.minecraft.server.v1_15_R1.Blocks;
import net.minecraft.server.v1_15_R1.ChunkCache;
import net.minecraft.server.v1_15_R1.EnumDirection;
import net.minecraft.server.v1_15_R1.Fluid;
import net.minecraft.server.v1_15_R1.FluidTypes;
import net.minecraft.server.v1_15_R1.IBlockAccess;
import net.minecraft.server.v1_15_R1.IBlockData;
import net.minecraft.server.v1_15_R1.Material;
import net.minecraft.server.v1_15_R1.MathHelper;
import net.minecraft.server.v1_15_R1.PathDestination;
import net.minecraft.server.v1_15_R1.PathMode;
import net.minecraft.server.v1_15_R1.PathPoint;
import net.minecraft.server.v1_15_R1.PathType;
import net.minecraft.server.v1_15_R1.TagsBlock;
import net.minecraft.server.v1_15_R1.TagsFluid;
import net.minecraft.server.v1_15_R1.VoxelShape;

public class CustomPathfinderNormal extends CustomPathfinderAbstract {

	protected float j;

	public void a(ChunkCache var0, CustomInsentient var1) {
		super.a(var0, var1);
		this.j = var1.a(PathType.WATER);
	}

	public void a() {
		this.b.a(PathType.WATER, this.j);
		super.a();
	}

	public PathPoint b() {
		int var0;
		if (e() && this.b.isInWater()) {
			var0 = MathHelper.floor(this.b.locY());
			BlockPosition.MutableBlockPosition var1 = new BlockPosition.MutableBlockPosition(this.b.locX(), var0,
					this.b.locZ());
			IBlockData var2 = this.a.getType(var1);
			while (var2.getBlock() == Blocks.WATER || var2.getFluid() == FluidTypes.WATER.a(false)) {
				var0++;
				var1.c(this.b.locX(), var0, this.b.locZ());
				var2 = this.a.getType(var1);
			}
			var0--;
		} else if (this.b_.onGround) {
			var0 = MathHelper.floor(this.b.locY() + 0.5D);
		} else {
			BlockPosition var1 = new BlockPosition(this.b_);
			while ((this.a.getType(var1).isAir() || this.a.getType(var1).a(this.a, var1, PathMode.LAND))
					&& var1.getY() > 0) {
				var1 = var1.down();
			}
			var0 = var1.up().getY();
		}

		BlockPosition var1 = new BlockPosition(this.b_);
		PathType var2 = a(this.b, var1.getX(), var0, var1.getZ());

		if (this.b.a(var2) < 0.0F) {
			Set<BlockPosition> var3 = Sets.newHashSet();
			var3.add(new BlockPosition((this.b.getBoundingBox()).minX, var0, (this.b.getBoundingBox()).minZ));
			var3.add(new BlockPosition((this.b.getBoundingBox()).minX, var0, (this.b.getBoundingBox()).maxZ));
			var3.add(new BlockPosition((this.b.getBoundingBox()).maxX, var0, (this.b.getBoundingBox()).minZ));
			var3.add(new BlockPosition((this.b.getBoundingBox()).maxX, var0, (this.b.getBoundingBox()).maxZ));

			for (BlockPosition var5 : var3) {
				PathType var6 = a(this.b, var5);
				if (this.b.a(var6) >= 0.0F) {
					return a(var5.getX(), var5.getY(), var5.getZ());
				}
			}
		}

		return a(var1.getX(), var0, var1.getZ());
	}

	public PathDestination a(double var0, double var2, double var4) {
		return new PathDestination(a(MathHelper.floor(var0), MathHelper.floor(var2), MathHelper.floor(var4)));
	}

	public int a(PathPoint[] var0, PathPoint var1) {
		int var2 = 0;

		int var3 = 0;
		PathType var4 = a(this.b, var1.a, var1.b + 1, var1.c);
		if (this.b.a(var4) >= 0.0F) {
			PathType var5 = a(this.b, var1.a, var1.b, var1.c);
			if (var5 == PathType.STICKY_HONEY) {
				var3 = 0;
			} else {
				var3 = MathHelper.d(Math.max(1.0F, this.b_.H));
			}
		}

		double var5 = a(this.a, new BlockPosition(var1.a, var1.b, var1.c));

		PathPoint var7 = a(var1.a, var1.b, var1.c + 1, var3, var5, EnumDirection.SOUTH);
		if (var7 != null && !var7.i && var7.k >= 0.0F) {
			var0[var2++] = var7;
		}

		PathPoint var8 = a(var1.a - 1, var1.b, var1.c, var3, var5, EnumDirection.WEST);
		if (var8 != null && !var8.i && var8.k >= 0.0F) {
			var0[var2++] = var8;
		}

		PathPoint var9 = a(var1.a + 1, var1.b, var1.c, var3, var5, EnumDirection.EAST);
		if (var9 != null && !var9.i && var9.k >= 0.0F) {
			var0[var2++] = var9;
		}

		PathPoint var10 = a(var1.a, var1.b, var1.c - 1, var3, var5, EnumDirection.NORTH);
		if (var10 != null && !var10.i && var10.k >= 0.0F) {
			var0[var2++] = var10;
		}

		PathPoint var11 = a(var1.a - 1, var1.b, var1.c - 1, var3, var5, EnumDirection.NORTH);
		if (a(var1, var8, var10, var11)) {
			var0[var2++] = var11;
		}

		PathPoint var12 = a(var1.a + 1, var1.b, var1.c - 1, var3, var5, EnumDirection.NORTH);
		if (a(var1, var9, var10, var12)) {
			var0[var2++] = var12;
		}

		PathPoint var13 = a(var1.a - 1, var1.b, var1.c + 1, var3, var5, EnumDirection.SOUTH);
		if (a(var1, var8, var7, var13)) {
			var0[var2++] = var13;
		}

		PathPoint var14 = a(var1.a + 1, var1.b, var1.c + 1, var3, var5, EnumDirection.SOUTH);
		if (a(var1, var9, var7, var14)) {
			var0[var2++] = var14;
		}

		return var2;
	}

	private boolean a(PathPoint var0, @Nullable PathPoint var1, @Nullable PathPoint var2, @Nullable PathPoint var3) {
		if (var3 == null || var2 == null || var1 == null) {
			return false;
		}

		if (var3.i) {
			return false;
		}

		if (var2.b > var0.b || var1.b > var0.b) {
			return false;
		}

		return (var3.k >= 0.0F && (var2.b < var0.b || var2.k >= 0.0F) && (var1.b < var0.b || var1.k >= 0.0F));
	}

	public static double a(IBlockAccess var0, BlockPosition var1) {
		BlockPosition var2 = var1.down();
		VoxelShape var3 = var0.getType(var2).getCollisionShape(var0, var2);
		return var2.getY() + (var3.isEmpty() ? 0.0D : var3.c(EnumDirection.EnumAxis.Y));
	}

	@Nullable
	private PathPoint a(int var0, int var1, int var2, int var3, double var4, EnumDirection var6) {
		PathPoint var7 = null;

		BlockPosition var8 = new BlockPosition(var0, var1, var2);
		double var9 = a(this.a, var8);

		if (var9 - var4 > 1.125D) {
			return null;
		}

		PathType var11 = a(this.b, var0, var1, var2);

		float var12 = this.b.a(var11);
		double var13 = this.b.getWidth() / 2.0D;

		if (var12 >= 0.0F) {
			var7 = a(var0, var1, var2);
			var7.l = var11;
			var7.k = Math.max(var7.k, var12);
		}

		if (var11 == PathType.WALKABLE) {
			return var7;
		}

		if ((var7 == null || var7.k < 0.0F) && var3 > 0 && var11 != PathType.FENCE && var11 != PathType.TRAPDOOR) {
			var7 = a(var0, var1 + 1, var2, var3 - 1, var4, var6);

			if (var7 != null && (var7.l == PathType.OPEN || var7.l == PathType.WALKABLE) && this.b.getWidth() < 1.0F) {
				double var15 = (var0 - var6.getAdjacentX()) + 0.5D;
				double var17 = (var2 - var6.getAdjacentZ()) + 0.5D;

				AxisAlignedBB var19 = new AxisAlignedBB(var15 - var13,
						a(this.a, new BlockPosition(var15, (var1 + 1), var17)) + 0.001D, var17 - var13, var15 + var13,
						this.b.getHeight() + a(this.a, new BlockPosition(var7.a, var7.b, var7.c)) - 0.002D,
						var17 + var13);

				if (!this.a.getCubes(this.b_, var19)) {
					var7 = null;
				}
			}
		}

		if (var11 == PathType.WATER && !e()) {
			if (a(this.b, var0, var1 - 1, var2) != PathType.WATER) {
				return var7;
			}

			while (var1 > 0) {
				var1--;

				var11 = a(this.b, var0, var1, var2);

				if (var11 == PathType.WATER) {
					var7 = a(var0, var1, var2);
					var7.l = var11;
					var7.k = Math.max(var7.k, this.b.a(var11));
					continue;
				}
				return var7;
			}
		}

		if (var11 == PathType.OPEN) {

			AxisAlignedBB var15 = new AxisAlignedBB(var0 - var13 + 0.5D, var1 + 0.001D, var2 - var13 + 0.5D,
					var0 + var13 + 0.5D, (var1 + this.b.getHeight()), var2 + var13 + 0.5D);
			if (!this.a.getCubes(this.b_, var15)) {
				return null;
			}

			if (this.b.getWidth() >= 1.0F) {
				PathType var16 = a(this.b, var0, var1 - 1, var2);
				if (var16 == PathType.BLOCKED) {
					var7 = a(var0, var1, var2);
					var7.l = PathType.WALKABLE;
					var7.k = Math.max(var7.k, var12);
					return var7;
				}
			}

			int var16 = 0;
			int var17 = var1;
			while (var11 == PathType.OPEN) {
				var1--;

				if (var1 < 0) {
					PathPoint var18 = a(var0, var17, var2);
					var18.l = PathType.BLOCKED;
					var18.k = -1.0F;
					return var18;
				}

				PathPoint var18 = a(var0, var1, var2);

				if (var16++ >= this.b.bD()) {
					var18.l = PathType.BLOCKED;
					var18.k = -1.0F;
					return var18;
				}

				var11 = a(this.b, var0, var1, var2);
				var12 = this.b.a(var11);

				if (var11 != PathType.OPEN && var12 >= 0.0F) {
					var7 = var18;
					var7.l = var11;
					var7.k = Math.max(var7.k, var12);
					break;
				}
				if (var12 < 0.0F) {
					var18.l = PathType.BLOCKED;
					var18.k = -1.0F;
					return var18;
				}
			}
		}
		return var7;
	}

	public PathType a(IBlockAccess var0, int var1, int var2, int var3, CustomInsentient var4, int var5, int var6,
			int var7, boolean var8, boolean var9) {
		EnumSet<PathType> var10 = EnumSet.noneOf(PathType.class);
		PathType var11 = PathType.BLOCKED;

		@SuppressWarnings("unused")
		double var12 = var4.getWidth() / 2.0D;
		BlockPosition var14 = new BlockPosition(var4.getEntity());

		var11 = a(var0, var1, var2, var3, var5, var6, var7, var8, var9, var10, var11, var14);

		if (var10.contains(PathType.FENCE)) {
			return PathType.FENCE;
		}

		PathType var15 = PathType.BLOCKED;
		for (PathType var17 : var10) {

			if (var4.a(var17) < 0.0F) {
				return var17;
			}

			if (var4.a(var17) >= var4.a(var15)) {
				var15 = var17;
			}
		}

		if (var11 == PathType.OPEN && var4.a(var15) == 0.0F) {
			return PathType.OPEN;
		}

		return var15;
	}

	public PathType a(IBlockAccess var0, int var1, int var2, int var3, int var4, int var5, int var6, boolean var7,
			boolean var8, EnumSet<PathType> var9, PathType var10, BlockPosition var11) {
		for (int var12 = 0; var12 < var4; var12++) {
			for (int var13 = 0; var13 < var5; var13++) {
				for (int var14 = 0; var14 < var6; var14++) {
					int var15 = var12 + var1;
					int var16 = var13 + var2;
					int var17 = var14 + var3;

					PathType var18 = a(var0, var15, var16, var17);

					var18 = a(var0, var7, var8, var11, var18);

					if (var12 == 0 && var13 == 0 && var14 == 0) {
						var10 = var18;
					}

					var9.add(var18);
				}
			}
		}
		return var10;
	}

	protected PathType a(IBlockAccess var0, boolean var1, boolean var2, BlockPosition var3, PathType var4) {
		if (var4 == PathType.DOOR_WOOD_CLOSED && var1 && var2) {
			var4 = PathType.WALKABLE;
		}
		if (var4 == PathType.DOOR_OPEN && !var2) {
			var4 = PathType.BLOCKED;
		}
		if (var4 == PathType.RAIL && !(var0.getType(var3).getBlock() instanceof BlockMinecartTrackAbstract)
				&& !(var0.getType(var3.down()).getBlock() instanceof BlockMinecartTrackAbstract)) {
			var4 = PathType.FENCE;
		}
		if (var4 == PathType.LEAVES) {
			var4 = PathType.BLOCKED;
		}
		return var4;
	}

	private PathType a(CustomInsentient var0, BlockPosition var1) {
		return a(var0, var1.getX(), var1.getY(), var1.getZ());
	}

	private PathType a(CustomInsentient var0, int var1, int var2, int var3) {
		return a(this.a, var1, var2, var3, var0, this.d, this.e, this.f, d(), c());
	}

	public PathType a(IBlockAccess var0, int var1, int var2, int var3) {
		return b(var0, var1, var2, var3);
	}

	public static PathType b(IBlockAccess var0, int var1, int var2, int var3) {
		PathType var4 = c(var0, var1, var2, var3);

		if (var4 == PathType.OPEN && var2 >= 1) {
			Block var5 = var0.getType(new BlockPosition(var1, var2 - 1, var3)).getBlock();
			PathType var6 = c(var0, var1, var2 - 1, var3);
			var4 = (var6 == PathType.WALKABLE || var6 == PathType.OPEN || var6 == PathType.WATER
					|| var6 == PathType.LAVA) ? PathType.OPEN : PathType.WALKABLE;

			if (var6 == PathType.DAMAGE_FIRE || var5 == Blocks.MAGMA_BLOCK || var5 == Blocks.CAMPFIRE) {
				var4 = PathType.DAMAGE_FIRE;
			}

			if (var6 == PathType.DAMAGE_CACTUS) {
				var4 = PathType.DAMAGE_CACTUS;
			}

			if (var6 == PathType.DAMAGE_OTHER) {
				var4 = PathType.DAMAGE_OTHER;
			}

			if (var6 == PathType.STICKY_HONEY) {
				var4 = PathType.STICKY_HONEY;
			}
		}

		if (var4 == PathType.WALKABLE) {
			var4 = a(var0, var1, var2, var3, var4);
		}
		return var4;
	}

	public static PathType a(IBlockAccess var0, int var1, int var2, int var3, PathType var4) {
		PooledBlockPosition var5 = BlockPosition.PooledBlockPosition.r();
		Throwable throwable = null;
		try {
			for (int var7 = -1; var7 <= 1; var7++) {
				for (int var8 = -1; var8 <= 1; var8++) {
					for (int var9 = -1; var9 <= 1; var9++) {
						if (var7 != 0 || var9 != 0) {
							Block var10 = var0.getType(var5.d(var7 + var1, var8 + var2, var9 + var3)).getBlock();

							if (var10 == Blocks.CACTUS) {
								var4 = PathType.DANGER_CACTUS;
							} else if (var10 == Blocks.FIRE || var10 == Blocks.LAVA) {
								var4 = PathType.DANGER_FIRE;
							} else if (var10 == Blocks.SWEET_BERRY_BUSH) {
								var4 = PathType.DANGER_OTHER;
							}
						}
					}
				}
			}
		} catch (Throwable throwable1) {
			throwable = throwable1 = null;
//			throw throwable1;
		} finally {
			if (var5 != null)
				if (throwable != null) {
					try {
						var5.close();
					} catch (Throwable throwable1) {
						throwable.addSuppressed(throwable1);
					}
				} else {
					var5.close();
				}
		}
		return var4;
	}

	protected static PathType c(IBlockAccess var0, int var1, int var2, int var3) {
		BlockPosition var4 = new BlockPosition(var1, var2, var3);
		IBlockData var5 = var0.getType(var4);
		Block var6 = var5.getBlock();
		Material var7 = var5.getMaterial();

		if (var5.isAir()) {
			return PathType.OPEN;
		}

		if (var6.a(TagsBlock.TRAPDOORS) || var6 == Blocks.LILY_PAD) {
			return PathType.TRAPDOOR;
		}

		if (var6 == Blocks.FIRE) {
			return PathType.DAMAGE_FIRE;
		}

		if (var6 == Blocks.CACTUS) {
			return PathType.DAMAGE_CACTUS;
		}

		if (var6 == Blocks.SWEET_BERRY_BUSH) {
			return PathType.DAMAGE_OTHER;
		}

		if (var6 == Blocks.HONEY_BLOCK) {
			return PathType.STICKY_HONEY;
		}

		if (var6 == Blocks.COCOA) {
			return PathType.COCOA;
		}

		if (var6 instanceof BlockDoor && var7 == Material.WOOD && !((Boolean) var5.get(BlockDoor.OPEN)).booleanValue())
			return PathType.DOOR_WOOD_CLOSED;
		if (var6 instanceof BlockDoor && var7 == Material.ORE && !((Boolean) var5.get(BlockDoor.OPEN)).booleanValue())
			return PathType.DOOR_IRON_CLOSED;
		if (var6 instanceof BlockDoor && ((Boolean) var5.get(BlockDoor.OPEN)).booleanValue()) {
			return PathType.DOOR_OPEN;
		}

		if (var6 instanceof BlockMinecartTrackAbstract) {
			return PathType.RAIL;
		}

		if (var6 instanceof BlockLeaves) {
			return PathType.LEAVES;
		}

		if (var6.a(TagsBlock.FENCES) || var6.a(TagsBlock.WALLS)
				|| (var6 instanceof BlockFenceGate && !((Boolean) var5.get(BlockFenceGate.OPEN)).booleanValue())) {
			return PathType.FENCE;
		}

		Fluid var8 = var0.getFluid(var4);
		if (var8.a(TagsFluid.WATER))
			return PathType.WATER;
		if (var8.a(TagsFluid.LAVA)) {
			return PathType.LAVA;
		}

		if (var5.a(var0, var4, PathMode.LAND)) {
			return PathType.OPEN;
		}

		return PathType.BLOCKED;
	}
}
