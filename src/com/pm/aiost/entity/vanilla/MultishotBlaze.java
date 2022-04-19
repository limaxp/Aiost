package com.pm.aiost.entity.vanilla;

import java.util.EnumSet;

import com.pm.aiost.entity.AiostEntityTypes;
import com.pm.aiost.entity.AiostInsentient;

import net.minecraft.server.v1_15_R1.BlockPosition;
import net.minecraft.server.v1_15_R1.EntityBlaze;
import net.minecraft.server.v1_15_R1.EntityHuman;
import net.minecraft.server.v1_15_R1.EntityLiving;
import net.minecraft.server.v1_15_R1.EntitySmallFireball;
import net.minecraft.server.v1_15_R1.EntityTypes;
import net.minecraft.server.v1_15_R1.GenericAttributes;
import net.minecraft.server.v1_15_R1.MathHelper;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import net.minecraft.server.v1_15_R1.PathfinderGoal;
import net.minecraft.server.v1_15_R1.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_15_R1.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_15_R1.PathfinderGoalMoveTowardsRestriction;
import net.minecraft.server.v1_15_R1.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_15_R1.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_15_R1.PathfinderGoalRandomStrollLand;
import net.minecraft.server.v1_15_R1.World;

public class MultishotBlaze extends EntityBlaze implements AiostInsentient {

	private static final int DEFAULT_SHOT_AMOUNT = 3;

	private int shotAmount = DEFAULT_SHOT_AMOUNT;

	public MultishotBlaze(EntityTypes<? extends EntityBlaze> entitytypes, World world) {
		super(EntityTypes.BLAZE, world);
	}

	public MultishotBlaze(World world) {
		super(EntityTypes.BLAZE, world);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected void initPathfinder() {
		this.goalSelector.a(4, new PathfinderGoalMultishotBlazeFireball(this));
		this.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(this, 1.0D));
		this.goalSelector.a(7, new PathfinderGoalRandomStrollLand(this, 1.0D, 0.0F));
		this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
		this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));

		this.targetSelector.a(1, (new PathfinderGoalHurtByTarget(this, new Class[0])).a(new Class[0]));
		this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, true));
	}

	@Override
	public NBTTagCompound save(NBTTagCompound nbttagcompound) {
		super.b(nbttagcompound);
		saveNBT(nbttagcompound);
		return nbttagcompound;
	}

	@Override
	public EntityTypes<?> getCustomEntityType() {
		return AiostEntityTypes.MULTISHOT_BLAZE;
	}

	public void setShotAmount(int shotAmount) {
		this.shotAmount = shotAmount;
	}

	public int getShotAmount() {
		return shotAmount;
	}

	static class PathfinderGoalMultishotBlazeFireball extends PathfinderGoal {

		private final EntityBlaze a;
		private int b;
		private int c;
		private int d;

		public PathfinderGoalMultishotBlazeFireball(EntityBlaze var0) {
			this.a = var0;

			a(EnumSet.of(PathfinderGoal.Type.MOVE, PathfinderGoal.Type.LOOK));
		}

		public boolean a() {
			EntityLiving var0 = this.a.getGoalTarget();
			return (var0 != null && var0.isAlive() && this.a.c(var0));
		}

		public void c() {
			this.b = 0;
		}

		public void d() {
//			MultishotBlaze.a(this.a, false);
			this.d = 0;
		}

		public void e() {
			this.c--;

			EntityLiving var0 = this.a.getGoalTarget();

			if (var0 == null) {
				return;
			}

			boolean var1 = this.a.getEntitySenses().a(var0);

			if (var1) {
				this.d = 0;
			} else {
				this.d++;
			}

			double var2 = this.a.h(var0);

			if (var2 < 4.0D) {
				if (!var1) {
					return;
				}

				if (this.c <= 0) {
					this.c = 20;
					this.a.B(var0);
				}
				this.a.getControllerMove().a(var0.locX(), var0.locY(), var0.locZ(), 1.0D);
			} else if (var2 < g() * g() && var1) {
				double var4 = var0.locX() - this.a.locX();
				double var6 = var0.e(0.5D) - this.a.e(0.5D);
				double var8 = var0.locZ() - this.a.locZ();

				if (this.c <= 0) {
					this.b++;
					if (this.b == 1) {
						this.c = 60;
//						MultishotBlaze.a(this.a, true);
					} else if (this.b <= 4) {
						this.c = 6;
					} else {
						this.c = 100;
						this.b = 0;
//						MultishotBlaze.a(this.a, false);
					}

					if (this.b > 1) {
						float var10 = MathHelper.c(MathHelper.sqrt(var2)) * 0.5F;

						this.a.world.a(null, 1018, new BlockPosition(this.a), 0);
						for (int var11 = 0; var11 < 1; var11++) {
							EntitySmallFireball var12 = new EntitySmallFireball(this.a.world, this.a,
									var4 + this.a.getRandom().nextGaussian() * var10, var6,
									var8 + this.a.getRandom().nextGaussian() * var10);
							var12.setPosition(var12.locX(), this.a.e(0.5D) + 0.5D, var12.locZ());
							this.a.world.addEntity(var12);
						}
					}
				}
				this.a.getControllerLook().a(var0, 10.0F, 10.0F);
			} else if (this.d < 5) {
				this.a.getControllerMove().a(var0.locX(), var0.locY(), var0.locZ(), 1.0D);
			}

			super.e();
		}

		private double g() {
			return this.a.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).getValue();
		}
	}
}
