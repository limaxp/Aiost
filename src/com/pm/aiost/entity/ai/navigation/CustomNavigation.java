package com.pm.aiost.entity.ai.navigation;

import com.pm.aiost.entity.CustomInsentient;
import com.pm.aiost.entity.ai.navigation.pathfinder.CustomPathfinder;
import com.pm.aiost.entity.ai.navigation.pathfinder.CustomPathfinderNormal;

import net.minecraft.server.v1_15_R1.Block;
import net.minecraft.server.v1_15_R1.BlockPosition;
import net.minecraft.server.v1_15_R1.Blocks;
import net.minecraft.server.v1_15_R1.Entity;
import net.minecraft.server.v1_15_R1.MathHelper;
import net.minecraft.server.v1_15_R1.PathEntity;
import net.minecraft.server.v1_15_R1.PathMode;
import net.minecraft.server.v1_15_R1.PathPoint;
import net.minecraft.server.v1_15_R1.PathType;
import net.minecraft.server.v1_15_R1.Vec3D;
import net.minecraft.server.v1_15_R1.World;

public class CustomNavigation extends CustomNavigationAbstract {

	private boolean p;

	public CustomNavigation(CustomInsentient var0, World var1) {
		super(var0, var1);
	}

	protected CustomPathfinder a(int var0) {
		this.o = new CustomPathfinderNormal();
		this.o.a(true);
		return new CustomPathfinder(this.o, var0);
	}

	protected boolean a() {
		return (this.a_.onGround || p() || this.a.isPassenger());
	}

	protected Vec3D b() {
		return new Vec3D(this.a.locX(), t(), this.a.locZ());
	}

	public PathEntity a(BlockPosition var0, int var1) {
		if (this.b.getType(var0).isAir()) {
			BlockPosition var2 = var0.down();
			while (var2.getY() > 0 && this.b.getType(var2).isAir()) {
				var2 = var2.down();
			}

			if (var2.getY() > 0) {
				return super.a(var2.up(), var1);
			}

			while (var2.getY() < this.b.getBuildHeight() && this.b.getType(var2).isAir()) {
				var2 = var2.up();
			}
			var0 = var2;
		}

		if (this.b.getType(var0).getMaterial().isBuildable()) {
			BlockPosition var2 = var0.up();
			while (var2.getY() < this.b.getBuildHeight() && this.b.getType(var2).getMaterial().isBuildable()) {
				var2 = var2.up();
			}
			return super.a(var2, var1);
		}

		return super.a(var0, var1);
	}

	public PathEntity a(Entity var0, int var1) {
		return a(new BlockPosition(var0), var1);
	}

	private int t() {
		if (!this.a.isInWater() || !r()) {
			return MathHelper.floor(this.a.locY() + 0.5D);
		}

		int var0 = MathHelper.floor(this.a.locY());
		Block var1 = this.b.getType(new BlockPosition(this.a.locX(), var0, this.a.locZ())).getBlock();
		int var2 = 0;
		while (var1 == Blocks.WATER) {
			var0++;
			var1 = this.b.getType(new BlockPosition(this.a.locX(), var0, this.a.locZ())).getBlock();
			if (++var2 > 16) {
				return MathHelper.floor(this.a.locY());
			}
		}
		return var0;
	}

	protected void F_() {
		super.F_();

		if (this.p) {
			if (this.b.f(new BlockPosition(this.a.locX(), this.a.locY() + 0.5D, this.a.locZ()))) {
				return;
			}

			for (int var0 = 0; var0 < this.c.e(); var0++) {
				PathPoint var1 = this.c.a(var0);
				if (this.b.f(new BlockPosition(var1.a, var1.b, var1.c))) {
					this.c.b(var0);
					return;
				}
			}
		}
	}

	protected boolean a(Vec3D var0, Vec3D var1, int var2, int var3, int var4) {
		int var5 = MathHelper.floor(var0.x);
		int var6 = MathHelper.floor(var0.z);

		double var7 = var1.x - var0.x;
		double var9 = var1.z - var0.z;
		double var11 = var7 * var7 + var9 * var9;
		if (var11 < 1.0E-8D) {
			return false;
		}

		double var13 = 1.0D / Math.sqrt(var11);
		var7 *= var13;
		var9 *= var13;

		var2 += 2;
		var4 += 2;
		if (!a(var5, MathHelper.floor(var0.y), var6, var2, var3, var4, var0, var7, var9)) {
			return false;
		}
		var2 -= 2;
		var4 -= 2;

		double var15 = 1.0D / Math.abs(var7);
		double var17 = 1.0D / Math.abs(var9);

		double var19 = var5 - var0.x;
		double var21 = var6 - var0.z;
		if (var7 >= 0.0D) {
			var19++;
		}
		if (var9 >= 0.0D) {
			var21++;
		}
		var19 /= var7;
		var21 /= var9;

		int var23 = (var7 < 0.0D) ? -1 : 1;
		int var24 = (var9 < 0.0D) ? -1 : 1;
		int var25 = MathHelper.floor(var1.x);
		int var26 = MathHelper.floor(var1.z);
		int var27 = var25 - var5;
		int var28 = var26 - var6;
		while (var27 * var23 > 0 || var28 * var24 > 0) {
			if (var19 < var21) {
				var19 += var15;
				var5 += var23;
				var27 = var25 - var5;
			} else {
				var21 += var17;
				var6 += var24;
				var28 = var26 - var6;
			}

			if (!a(var5, MathHelper.floor(var0.y), var6, var2, var3, var4, var0, var7, var9)) {
				return false;
			}
		}
		return true;
	}

	private boolean a(int var0, int var1, int var2, int var3, int var4, int var5, Vec3D var6, double var7,
			double var9) {
		int var11 = var0 - var3 / 2;
		int var12 = var2 - var5 / 2;

		if (!b(var11, var1, var12, var3, var4, var5, var6, var7, var9)) {
			return false;
		}

		for (int var13 = var11; var13 < var11 + var3; var13++) {
			for (int var14 = var12; var14 < var12 + var5; var14++) {
				double var15 = var13 + 0.5D - var6.x;
				double var17 = var14 + 0.5D - var6.z;
				if (var15 * var7 + var17 * var9 >= 0.0D) {

					PathType var19 = this.o.a(this.b, var13, var1 - 1, var14, this.a, var3, var4, var5, true, true);

					if (var19 == PathType.WATER) {
						return false;
					}

					if (var19 == PathType.LAVA) {
						return false;
					}

					if (var19 == PathType.OPEN) {
						return false;
					}

					var19 = this.o.a(this.b, var13, var1, var14, this.a, var3, var4, var5, true, true);
					float var20 = this.a.a(var19);

					if (var20 < 0.0F || var20 >= 8.0F) {
						return false;
					}

					if (var19 == PathType.DAMAGE_FIRE || var19 == PathType.DANGER_FIRE
							|| var19 == PathType.DAMAGE_OTHER) {
						return false;
					}
				}
			}
		}
		return true;
	}

	private boolean b(int var0, int var1, int var2, int var3, int var4, int var5, Vec3D var6, double var7,
			double var9) {
		for (BlockPosition var12 : BlockPosition.a(new BlockPosition(var0, var1, var2),
				new BlockPosition(var0 + var3 - 1, var1 + var4 - 1, var2 + var5 - 1))) {
			double var13 = var12.getX() + 0.5D - var6.x;
			double var15 = var12.getZ() + 0.5D - var6.z;
			if (var13 * var7 + var15 * var9 < 0.0D) {
				continue;
			}
			if (!this.b.getType(var12).a(this.b, var12, PathMode.LAND)) {
				return false;
			}
		}
		return true;
	}

	public void a(boolean var0) {
		this.o.b(var0);
	}

	public boolean f() {
		return this.o.c();
	}

	public void c(boolean var0) {
		this.p = var0;
	}
}
