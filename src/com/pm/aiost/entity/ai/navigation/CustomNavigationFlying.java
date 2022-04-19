package com.pm.aiost.entity.ai.navigation;

import com.pm.aiost.entity.CustomInsentient;
import com.pm.aiost.entity.ai.navigation.pathfinder.CustomPathfinder;
import com.pm.aiost.entity.ai.navigation.pathfinder.CustomPathfinderFlying;

import net.minecraft.server.v1_15_R1.BlockPosition;
import net.minecraft.server.v1_15_R1.Entity;
import net.minecraft.server.v1_15_R1.IBlockAccess;
import net.minecraft.server.v1_15_R1.MathHelper;
import net.minecraft.server.v1_15_R1.PathEntity;
import net.minecraft.server.v1_15_R1.Vec3D;
import net.minecraft.server.v1_15_R1.World;

public class CustomNavigationFlying extends CustomNavigationAbstract {

	public CustomNavigationFlying(CustomInsentient var0, World var1) {
		super(var0, var1);
	}

	protected CustomPathfinder a(int var0) {
		this.o = new CustomPathfinderFlying();
		this.o.a(true);
		return new CustomPathfinder(this.o, var0);
	}

	protected boolean a() {
		return ((r() && p()) || !this.a.isPassenger());
	}

	protected Vec3D b() {
		return this.a_.getPositionVector();
	}

	public PathEntity a(Entity var0, int var1) {
		return a(new BlockPosition(var0), var1);
	}

	public void c() {
		this.e++;

		if (this.m) {
			j();
		}

		if (m()) {
			return;
		}

		if (a()) {
			l();
		} else if (this.c != null && this.c.f() < this.c.e()) {
			Vec3D var0 = this.c.a(this.a_, this.c.f());
			if (MathHelper.floor(this.a.locX()) == MathHelper.floor(var0.x)
					&& MathHelper.floor(this.a.locY()) == MathHelper.floor(var0.y)
					&& MathHelper.floor(this.a.locZ()) == MathHelper.floor(var0.z)) {
				this.c.c(this.c.f() + 1);
			}
		}

//		PacketDebug.a(this.b, this.a, this.c, this.l);

		if (m()) {
			return;
		}
		Vec3D var0 = this.c.a(this.a_);

		this.a.getControllerMove().a(var0.x, var0.y, var0.z, this.d);
	}

	protected boolean a(Vec3D var0, Vec3D var1, int var2, int var3, int var4) {
		int var5 = MathHelper.floor(var0.x);
		int var6 = MathHelper.floor(var0.y);
		int var7 = MathHelper.floor(var0.z);

		double var8 = var1.x - var0.x;
		double var10 = var1.y - var0.y;
		double var12 = var1.z - var0.z;
		double var14 = var8 * var8 + var10 * var10 + var12 * var12;
		if (var14 < 1.0E-8D) {
			return false;
		}

		double var16 = 1.0D / Math.sqrt(var14);
		var8 *= var16;
		var10 *= var16;
		var12 *= var16;

		double var18 = 1.0D / Math.abs(var8);
		double var20 = 1.0D / Math.abs(var10);
		double var22 = 1.0D / Math.abs(var12);

		double var24 = var5 - var0.x;
		double var26 = var6 - var0.y;
		double var28 = var7 - var0.z;
		if (var8 >= 0.0D) {
			var24++;
		}
		if (var10 >= 0.0D) {
			var26++;
		}
		if (var12 >= 0.0D) {
			var28++;
		}
		var24 /= var8;
		var26 /= var10;
		var28 /= var12;

		int var30 = (var8 < 0.0D) ? -1 : 1;
		int var31 = (var10 < 0.0D) ? -1 : 1;
		int var32 = (var12 < 0.0D) ? -1 : 1;
		int var33 = MathHelper.floor(var1.x);
		int var34 = MathHelper.floor(var1.y);
		int var35 = MathHelper.floor(var1.z);
		int var36 = var33 - var5;
		int var37 = var34 - var6;
		int var38 = var35 - var7;

		while (var36 * var30 > 0 || var37 * var31 > 0 || var38 * var32 > 0) {
			if (var24 < var28 && var24 <= var26) {
				var24 += var18;
				var5 += var30;
				var36 = var33 - var5;
				continue;
			}
			if (var26 < var24 && var26 <= var28) {
				var26 += var20;
				var6 += var31;
				var37 = var34 - var6;
				continue;
			}
			var28 += var22;
			var7 += var32;
			var38 = var35 - var7;
		}

		return true;
	}

	public void a(boolean var0) {
		this.o.b(var0);
	}

	public void b(boolean var0) {
		this.o.a(var0);
	}

	public boolean a(BlockPosition var0) {
		return this.b.getType(var0).a((IBlockAccess) this.b, var0, this.a_);
	}
}
