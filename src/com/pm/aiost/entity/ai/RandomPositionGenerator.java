package com.pm.aiost.entity.ai;

import java.util.Random;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;

import javax.annotation.Nullable;

import com.pm.aiost.entity.CustomInsentient;
import com.pm.aiost.entity.ai.navigation.CustomNavigationAbstract;

import net.minecraft.server.v1_15_R1.BlockPosition;
import net.minecraft.server.v1_15_R1.EntityLiving;
import net.minecraft.server.v1_15_R1.MathHelper;
import net.minecraft.server.v1_15_R1.PathType;
import net.minecraft.server.v1_15_R1.PathfinderNormal;
import net.minecraft.server.v1_15_R1.TagsFluid;
import net.minecraft.server.v1_15_R1.Vec3D;

public class RandomPositionGenerator {

	@Nullable
	public static Vec3D a(CustomInsentient var0, int var1, int var2) {
		return a(var0, var1, var2, 0, null, true, 1.5707963705062866D, var0::f_, false, 0, 0, true);
	}

	@Nullable
	public static Vec3D a(CustomInsentient var0, int var1, int var2, int var3, @Nullable Vec3D var4, double var5) {
		return a(var0, var1, var2, var3, var4, true, var5, var0::f_, true, 0, 0, false);
	}

	@Nullable
	public static Vec3D b(CustomInsentient var0, int var1, int var2) {
		return a(var0, var1, var2, var0::f_);
	}

	@Nullable
	public static Vec3D a(CustomInsentient var0, int var1, int var2, ToDoubleFunction<BlockPosition> var3) {
		return a(var0, var1, var2, 0, null, false, 0.0D, var3, true, 0, 0, true);
	}

	@Nullable
	public static Vec3D a(CustomInsentient var0, int var1, int var2, Vec3D var3, float var4, int var5, int var6) {
		return a(var0, var1, var2, 0, var3, false, var4, var0::f_, true, var5, var6, true);
	}

	@Nullable
	public static Vec3D a(CustomInsentient var0, int var1, int var2, Vec3D var3) {
		Vec3D var4 = var3.a(var0.locX(), var0.locY(), var0.locZ());
		return a(var0, var1, var2, 0, var4, true, 1.5707963705062866D, var0::f_, false, 0, 0, true);
	}

	@Nullable
	public static Vec3D a(CustomInsentient var0, int var1, int var2, Vec3D var3, double var4) {
		Vec3D var6 = var3.a(var0.locX(), var0.locY(), var0.locZ());
		return a(var0, var1, var2, 0, var6, true, var4, var0::f_, false, 0, 0, true);
	}

	@Nullable
	public static Vec3D b(CustomInsentient var0, int var1, int var2, int var3, Vec3D var4, double var5) {
		Vec3D var7 = var4.a(var0.locX(), var0.locY(), var0.locZ());

		return a(var0, var1, var2, var3, var7, false, var5, var0::f_, true, 0, 0, false);
	}

	@Nullable
	public static Vec3D b(CustomInsentient var0, int var1, int var2, Vec3D var3) {
		Vec3D var4 = var0.getEntity().getPositionVector().d(var3);
		return a(var0, var1, var2, 0, var4, true, 1.5707963705062866D, var0::f_, false, 0, 0, true);
	}

	@Nullable
	public static Vec3D c(CustomInsentient var0, int var1, int var2, Vec3D var3) {
		Vec3D var4 = var0.getEntity().getPositionVector().d(var3);
		return a(var0, var1, var2, 0, var4, false, 1.5707963705062866D, var0::f_, true, 0, 0, true);
	}

	@Nullable
	private static Vec3D a(CustomInsentient var0, int var1, int var2, int var3, @Nullable Vec3D var4, boolean var5,
			double var6, ToDoubleFunction<BlockPosition> var8, boolean var9, int var10, int var11, boolean var12) {
		boolean var15;
		EntityLiving var0_ = var0.getEntity();
		CustomNavigationAbstract var13 = var0.getNavigation();
		Random var14 = var0.getRandom();

		if (var0.eg()) {
			var15 = var0.ed().a(var0_.getPositionVector(), (var0.ee() + var1) + 1.0D);
		} else {
			var15 = false;
		}

		boolean var16 = false;
		double var17 = Double.NEGATIVE_INFINITY;

		BlockPosition var19 = new BlockPosition(var0_);

		for (int var20 = 0; var20 < 10; var20++) {
			BlockPosition var21 = a(var14, var1, var2, var3, var4, var6);
			if (var21 != null) {

				int var22 = var21.getX();
				int var23 = var21.getY();
				int var24 = var21.getZ();

				if (var0.eg() && var1 > 1) {
					BlockPosition var25 = var0.ed();
					if (var0.locX() > var25.getX()) {
						var22 -= var14.nextInt(var1 / 2);
					} else {
						var22 += var14.nextInt(var1 / 2);
					}
					if (var0.locZ() > var25.getZ()) {
						var24 -= var14.nextInt(var1 / 2);
					} else {
						var24 += var14.nextInt(var1 / 2);
					}
				}

				BlockPosition var25 = new BlockPosition(var22 + var0.locX(), var23 + var0.locY(), var24 + var0.locZ());
				if (var25.getY() >= 0 && var25.getY() <= var0_.world.getBuildHeight()) {

					if (!var15 || var0.a(var25)) {

						if (!var12 || var13.a(var25)) {

							if (var9) {
								var25 = a(var25, var14.nextInt(var10 + 1) + var11, var0_.world.getBuildHeight(),
										var1x -> var0_.world.getType(var1x).getMaterial().isBuildable());
							}

							if (var5 || !var0_.world.getFluid(var25).a(TagsFluid.WATER))

							{

								PathType var26 = PathfinderNormal.b(var0_.world, var25.getX(), var25.getY(),
										var25.getZ());
								if (var0.a(var26) == 0.0F)

								{

									double var27 = var8.applyAsDouble(var25);
									if (var27 > var17) {
										var17 = var27;
										var19 = var25;
										var16 = true;
									}
								}
							}
						}
					}
				}
			}
		}
		if (var16) {
			return new Vec3D(var19);
		}
		return null;
	}

	@Nullable
	private static BlockPosition a(Random var0, int var1, int var2, int var3, @Nullable Vec3D var4, double var5) {
		if (var4 == null || var5 >= Math.PI) {
			int var7 = var0.nextInt(2 * var1 + 1) - var1;
			int var8 = var0.nextInt(2 * var2 + 1) - var2 + var3;
			int var9 = var0.nextInt(2 * var1 + 1) - var1;
			return new BlockPosition(var7, var8, var9);
		}
		double var7 = MathHelper.d(var4.z, var4.x) - 1.5707963705062866D;
		double var9 = var7 + (2.0F * var0.nextFloat() - 1.0F) * var5;
		double var11 = Math.sqrt(var0.nextDouble()) * MathHelper.a * var1;
		double var13 = -var11 * Math.sin(var9);
		double var15 = var11 * Math.cos(var9);

		if (Math.abs(var13) > var1 || Math.abs(var15) > var1) {
			return null;
		}
		int var17 = var0.nextInt(2 * var2 + 1) - var2 + var3;
		return new BlockPosition(var13, var17, var15);
	}

	static BlockPosition a(BlockPosition var0, int var1, int var2, Predicate<BlockPosition> var3) {
		if (var1 < 0) {
			throw new IllegalArgumentException("aboveSolidAmount was " + var1 + ", expected >= 0");
		}
		if (var3.test(var0)) {

			BlockPosition var4 = var0.up();
			while (var4.getY() < var2 && var3.test(var4)) {
				var4 = var4.up();
			}

			BlockPosition var5 = var4;
			while (var5.getY() < var2 && var5.getY() - var4.getY() < var1) {
				BlockPosition var6 = var5.up();
				if (var3.test(var6)) {
					break;
				}
				var5 = var6;
			}
			return var5;
		}
		return var0;
	}
}