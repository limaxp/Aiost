package com.pm.aiost.entity.vanilla;

import com.pm.aiost.entity.AiostEntityTypes;
import com.pm.aiost.entity.AiostInsentient;

import net.minecraft.server.v1_15_R1.EntityHuman;
import net.minecraft.server.v1_15_R1.EntityIronGolem;
import net.minecraft.server.v1_15_R1.EntityLiving;
import net.minecraft.server.v1_15_R1.EntitySpider;
import net.minecraft.server.v1_15_R1.EntityTypes;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import net.minecraft.server.v1_15_R1.PathfinderGoalFloat;
import net.minecraft.server.v1_15_R1.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_15_R1.PathfinderGoalLeapAtTarget;
import net.minecraft.server.v1_15_R1.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_15_R1.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_15_R1.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_15_R1.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_15_R1.PathfinderGoalRandomStrollLand;
import net.minecraft.server.v1_15_R1.World;

public class AlwayAttackSpider extends EntitySpider implements AiostInsentient {

	public AlwayAttackSpider(EntityTypes<? extends EntitySpider> entitytypes, World world) {
		super(EntityTypes.SPIDER, world);
	}

	public AlwayAttackSpider(World world) {
		super(EntityTypes.SPIDER, world);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected void initPathfinder() {
		this.goalSelector.a(1, new PathfinderGoalFloat(this));
		this.goalSelector.a(3, new PathfinderGoalLeapAtTarget(this, 0.4F));
		this.goalSelector.a(4, new PathfinderGoalAlwaysAttackSpiderMeleeAttack(this));
		this.goalSelector.a(5, new PathfinderGoalRandomStrollLand(this, 0.8D));
		this.goalSelector.a(6, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
		this.goalSelector.a(6, new PathfinderGoalRandomLookaround(this));
		this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, new Class[0]));
		this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, true));
		this.targetSelector.a(3, new PathfinderGoalNearestAttackableTarget(this, EntityIronGolem.class, true));
	}

	@Override
	public NBTTagCompound save(NBTTagCompound nbttagcompound) {
		super.b(nbttagcompound);
		saveNBT(nbttagcompound);
		return nbttagcompound;
	}

	@Override
	public EntityTypes<?> getCustomEntityType() {
		return AiostEntityTypes.ALWAYS_ATTACK_SPIDER;
	}

	public static class PathfinderGoalAlwaysAttackSpiderMeleeAttack extends PathfinderGoalMeleeAttack {
		public PathfinderGoalAlwaysAttackSpiderMeleeAttack(EntitySpider entityspider) {
			super(entityspider, 1.0D, true);
		}

		public boolean a() {
			return (super.a() && !this.a.isVehicle());
		}

		public boolean b() {
			if (this.a.getRandom().nextInt(100) == 0) {
				this.a.setGoalTarget(null);
				return false;
			}
			return super.b();
		}

		protected double a(EntityLiving entityliving) {
			return (4.0F + entityliving.getWidth());
		}
	}
}
