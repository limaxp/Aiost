package com.pm.aiost.entity.ai.navigation.pathfinder;

import java.util.EnumSet;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.collect.Sets;
import com.pm.aiost.entity.CustomInsentient;

import net.minecraft.server.v1_15_R1.Block;
import net.minecraft.server.v1_15_R1.BlockPosition;
import net.minecraft.server.v1_15_R1.Blocks;
import net.minecraft.server.v1_15_R1.ChunkCache;
import net.minecraft.server.v1_15_R1.EntityInsentient;
import net.minecraft.server.v1_15_R1.IBlockAccess;
import net.minecraft.server.v1_15_R1.MathHelper;
import net.minecraft.server.v1_15_R1.PathDestination;
import net.minecraft.server.v1_15_R1.PathPoint;
import net.minecraft.server.v1_15_R1.PathType;

public class CustomPathfinderFlying extends CustomPathfinderNormal {

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
			Block var2 = this.a.getType(var1).getBlock();
			while (var2 == Blocks.WATER) {
				var0++;
				var1.c(this.b.locX(), var0, this.b.locZ());
				var2 = this.a.getType(var1).getBlock();
			}
		} else {
			var0 = MathHelper.floor(this.b.locY() + 0.5D);
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
					return super.a(var5.getX(), var5.getY(), var5.getZ());
				}
			}
		}

		return super.a(var1.getX(), var0, var1.getZ());
	}

	public PathDestination a(double var0, double var2, double var4) {
		return new PathDestination(super.a(MathHelper.floor(var0), MathHelper.floor(var2), MathHelper.floor(var4)));
	}

	public int a(PathPoint[] var0, PathPoint var1) {
		int var2 = 0;

		PathPoint var3 = a(var1.a, var1.b, var1.c + 1);
		if (b(var3)) {
			var0[var2++] = var3;
		}

		PathPoint var4 = a(var1.a - 1, var1.b, var1.c);
		if (b(var4)) {
			var0[var2++] = var4;
		}

		PathPoint var5 = a(var1.a + 1, var1.b, var1.c);
		if (b(var5)) {
			var0[var2++] = var5;
		}

		PathPoint var6 = a(var1.a, var1.b, var1.c - 1);
		if (b(var6)) {
			var0[var2++] = var6;
		}

		PathPoint var7 = a(var1.a, var1.b + 1, var1.c);
		if (b(var7)) {
			var0[var2++] = var7;
		}

		PathPoint var8 = a(var1.a, var1.b - 1, var1.c);
		if (b(var8)) {
			var0[var2++] = var8;
		}

		PathPoint var9 = a(var1.a, var1.b + 1, var1.c + 1);
		if (b(var9) && a(var3) && a(var7)) {
			var0[var2++] = var9;
		}

		PathPoint var10 = a(var1.a - 1, var1.b + 1, var1.c);
		if (b(var10) && a(var4) && a(var7)) {
			var0[var2++] = var10;
		}

		PathPoint var11 = a(var1.a + 1, var1.b + 1, var1.c);
		if (b(var11) && a(var5) && a(var7)) {
			var0[var2++] = var11;
		}

		PathPoint var12 = a(var1.a, var1.b + 1, var1.c - 1);
		if (b(var12) && a(var6) && a(var7)) {
			var0[var2++] = var12;
		}

		PathPoint var13 = a(var1.a, var1.b - 1, var1.c + 1);
		if (b(var13) && a(var3) && a(var8)) {
			var0[var2++] = var13;
		}

		PathPoint var14 = a(var1.a - 1, var1.b - 1, var1.c);
		if (b(var14) && a(var4) && a(var8)) {
			var0[var2++] = var14;
		}

		PathPoint var15 = a(var1.a + 1, var1.b - 1, var1.c);
		if (b(var15) && a(var5) && a(var8)) {
			var0[var2++] = var15;
		}

		PathPoint var16 = a(var1.a, var1.b - 1, var1.c - 1);
		if (b(var16) && a(var6) && a(var8)) {
			var0[var2++] = var16;
		}

		PathPoint var17 = a(var1.a + 1, var1.b, var1.c - 1);
		if (b(var17) && a(var6) && a(var5)) {
			var0[var2++] = var17;
		}

		PathPoint var18 = a(var1.a + 1, var1.b, var1.c + 1);
		if (b(var18) && a(var3) && a(var5)) {
			var0[var2++] = var18;
		}

		PathPoint var19 = a(var1.a - 1, var1.b, var1.c - 1);
		if (b(var19) && a(var6) && a(var4)) {
			var0[var2++] = var19;
		}

		PathPoint var20 = a(var1.a - 1, var1.b, var1.c + 1);
		if (b(var20) && a(var3) && a(var4)) {
			var0[var2++] = var20;
		}

		PathPoint var21 = a(var1.a + 1, var1.b + 1, var1.c - 1);
		if (b(var21) && a(var17) && a(var12) && a(var11)) {
			var0[var2++] = var21;
		}

		PathPoint var22 = a(var1.a + 1, var1.b + 1, var1.c + 1);
		if (b(var22) && a(var18) && a(var9) && a(var11)) {
			var0[var2++] = var22;
		}

		PathPoint var23 = a(var1.a - 1, var1.b + 1, var1.c - 1);
		if (b(var23) && a(var19) && a(var12) && a(var10)) {
			var0[var2++] = var23;
		}

		PathPoint var24 = a(var1.a - 1, var1.b + 1, var1.c + 1);
		if (b(var24) && a(var20) && a(var9) && a(var10)) {
			var0[var2++] = var24;
		}

		PathPoint var25 = a(var1.a + 1, var1.b - 1, var1.c - 1);
		if (b(var25) && a(var17) && a(var16) && a(var15)) {
			var0[var2++] = var25;
		}

		PathPoint var26 = a(var1.a + 1, var1.b - 1, var1.c + 1);
		if (b(var26) && a(var18) && a(var13) && a(var15)) {
			var0[var2++] = var26;
		}

		PathPoint var27 = a(var1.a - 1, var1.b - 1, var1.c - 1);
		if (b(var27) && a(var19) && a(var16) && a(var14)) {
			var0[var2++] = var27;
		}

		PathPoint var28 = a(var1.a - 1, var1.b - 1, var1.c + 1);
		if (b(var28) && a(var20) && a(var13) && a(var14)) {
			var0[var2++] = var28;
		}

		return var2;
	}

	private boolean a(@Nullable PathPoint var0) {
		return (var0 != null && var0.k >= 0.0F);
	}

	private boolean b(@Nullable PathPoint var0) {
		return (var0 != null && !var0.i);
	}

	@Nullable
	protected PathPoint a(int var0, int var1, int var2) {
		PathPoint var3 = null;

		PathType var4 = a(this.b, var0, var1, var2);

		float var5 = this.b.a(var4);

		if (var5 >= 0.0F) {
			var3 = super.a(var0, var1, var2);
			var3.l = var4;
			var3.k = Math.max(var3.k, var5);

			if (var4 == PathType.WALKABLE) {
				var3.k++;
			}
		}

		if (var4 == PathType.OPEN || var4 == PathType.WALKABLE) {
			return var3;
		}

		return var3;
	}

	public PathType a(IBlockAccess var0, int var1, int var2, int var3, EntityInsentient var4, int var5, int var6,
			int var7, boolean var8, boolean var9) {
		EnumSet<PathType> var10 = EnumSet.noneOf(PathType.class);
		PathType var11 = PathType.BLOCKED;

		BlockPosition var12 = new BlockPosition(var4);

		var11 = a(var0, var1, var2, var3, var5, var6, var7, var8, var9, var10, var11, var12);

		if (var10.contains(PathType.FENCE)) {
			return PathType.FENCE;
		}

		PathType var13 = PathType.BLOCKED;
		for (PathType var15 : var10) {

			if (var4.a(var15) < 0.0F) {
				return var15;
			}

			if (var4.a(var15) >= var4.a(var13)) {
				var13 = var15;
			}
		}

		if (var11 == PathType.OPEN && var4.a(var13) == 0.0F) {
			return PathType.OPEN;
		}

		return var13;
	}

	public PathType a(IBlockAccess var0, int var1, int var2, int var3) {
		PathType var4 = c(var0, var1, var2, var3);

		if (var4 == PathType.OPEN && var2 >= 1) {
			Block var5 = var0.getType(new BlockPosition(var1, var2 - 1, var3)).getBlock();
			PathType var6 = c(var0, var1, var2 - 1, var3);

			if (var6 == PathType.DAMAGE_FIRE || var5 == Blocks.MAGMA_BLOCK || var6 == PathType.LAVA
					|| var5 == Blocks.CAMPFIRE) {
				var4 = PathType.DAMAGE_FIRE;
			} else if (var6 == PathType.DAMAGE_CACTUS) {
				var4 = PathType.DAMAGE_CACTUS;
			} else if (var6 == PathType.DAMAGE_OTHER) {
				var4 = PathType.DAMAGE_OTHER;
			} else if (var6 == PathType.COCOA) {
				var4 = PathType.COCOA;
			} else if (var6 == PathType.FENCE) {
				var4 = PathType.FENCE;
			} else {
				var4 = (var6 == PathType.WALKABLE || var6 == PathType.OPEN || var6 == PathType.WATER) ? PathType.OPEN
						: PathType.WALKABLE;
			}
		}

		if (var4 == PathType.WALKABLE || var4 == PathType.OPEN) {
			var4 = a(var0, var1, var2, var3, var4);
		}

		return var4;
	}

	private PathType a(CustomInsentient var0, BlockPosition var1) {
		return a(var0, var1.getX(), var1.getY(), var1.getZ());
	}

	private PathType a(CustomInsentient var0, int var1, int var2, int var3) {
		return a(this.a, var1, var2, var3, var0, this.d, this.e, this.f, d(), c());
	}
}
