package com.pm.aiost.entity.vanilla.hostile;

import com.pm.aiost.entity.AiostEntityTypes;
import com.pm.aiost.entity.AiostInsentient;
import com.pm.aiost.entity.vanilla.NoCombustSkeleton;

import net.minecraft.server.v1_15_R1.DamageSource;
import net.minecraft.server.v1_15_R1.EntityHuman;
import net.minecraft.server.v1_15_R1.EntitySkeleton;
import net.minecraft.server.v1_15_R1.EntityTypes;
import net.minecraft.server.v1_15_R1.EntityVillager;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import net.minecraft.server.v1_15_R1.PathfinderGoalFloat;
import net.minecraft.server.v1_15_R1.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_15_R1.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_15_R1.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_15_R1.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_15_R1.PathfinderGoalRandomStrollLand;
import net.minecraft.server.v1_15_R1.SoundEffect;
import net.minecraft.server.v1_15_R1.SoundEffects;
import net.minecraft.server.v1_15_R1.World;

public class HostileVillagerTrader extends NoCombustSkeleton implements AiostInsentient {

	public HostileVillagerTrader(EntityTypes<? extends EntitySkeleton> entitytypes, World world) {
		super(EntityTypes.SKELETON, world);
	}

	public HostileVillagerTrader(World world) {
		super(world);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected void initPathfinder() {
		this.goalSelector.a(0, new PathfinderGoalFloat(this));
		this.goalSelector.a(4, new PathfinderGoalMeleeAttack(this, 1.0D, true));
		this.goalSelector.a(5, new PathfinderGoalRandomStrollLand(this, 1.0D));
		this.goalSelector.a(6, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 6.0F));
		this.goalSelector.a(7, new PathfinderGoalRandomLookaround(this));
		this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, true));
		this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityVillager.class, false));
	}

	@Override
	public EntityTypes<?> getEntityType() {
		return EntityTypes.WANDERING_TRADER;
	}

	protected SoundEffect getSoundAmbient() {
		return SoundEffects.ENTITY_WANDERING_TRADER_AMBIENT;
	}

	protected SoundEffect getSoundHurt(DamageSource damagesource) {
		return SoundEffects.ENTITY_WANDERING_TRADER_HURT;
	}

	protected SoundEffect getSoundDeath() {
		return SoundEffects.ENTITY_WANDERING_TRADER_DEATH;
	}

	@Override
	public NBTTagCompound save(NBTTagCompound nbttagcompound) {
		super.b(nbttagcompound);
		saveNBT(nbttagcompound);
		return nbttagcompound;
	}

	@Override
	public EntityTypes<?> getCustomEntityType() {
		return AiostEntityTypes.HOSTILE_WANDERING_TRADER;
	}
}
