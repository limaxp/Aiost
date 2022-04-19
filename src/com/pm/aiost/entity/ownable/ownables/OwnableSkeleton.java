package com.pm.aiost.entity.ownable.ownables;

import java.util.UUID;

import com.pm.aiost.entity.AiostEntityTypes;
import com.pm.aiost.entity.ai.pathfinderGoal.PathfinderGoalFollowOwner;
import com.pm.aiost.entity.ai.pathfinderGoal.PathfinderGoalNearestAttackableTargetExceptTeammates;
import com.pm.aiost.entity.ai.pathfinderGoal.PathfinderGoalOwnerHurtByTarget;
import com.pm.aiost.entity.ai.pathfinderGoal.PathfinderGoalOwnerHurtTarget;
import com.pm.aiost.entity.ownable.OwnableInsentient;
import com.pm.aiost.entity.vanilla.NoCombustSkeleton;

import net.minecraft.server.v1_15_R1.EntityHuman;
import net.minecraft.server.v1_15_R1.EntitySkeleton;
import net.minecraft.server.v1_15_R1.EntityTypes;
import net.minecraft.server.v1_15_R1.EnumHand;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import net.minecraft.server.v1_15_R1.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_15_R1.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_15_R1.Vec3D;
import net.minecraft.server.v1_15_R1.World;

public class OwnableSkeleton extends NoCombustSkeleton implements OwnableInsentient {

	private UUID ownerId;
	private int boundTime = -1;

	public OwnableSkeleton(EntityTypes<? extends EntitySkeleton> entitytypes, World world) {
		super(world);
	}

	public OwnableSkeleton(World world) {
		super(world);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void initPathfinder() {
		this.goalSelector.a(2, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
		this.goalSelector.a(2, new PathfinderGoalRandomLookaround(this));
		this.goalSelector.a(1, new PathfinderGoalFollowOwner(this, 1.0D, 10.0F, 2.0F, false));
		this.targetSelector.a(1, new PathfinderGoalOwnerHurtByTarget(this));
		this.targetSelector.a(2, new PathfinderGoalOwnerHurtTarget(this));
		this.targetSelector.a(3,
				new PathfinderGoalNearestAttackableTargetExceptTeammates(this, EntityHuman.class, true));
	}

	@Override
	public void tick() {
		super.tick();
		ownableEntityTick();
	}

	@Override
	public NBTTagCompound save(NBTTagCompound nbttagcompound) {
		super.b(nbttagcompound);
		saveNBT(nbttagcompound);
		return nbttagcompound;
	}

	@Override
	public void f(NBTTagCompound nbttagcompound) {
		super.f(nbttagcompound);
		loadNBT(nbttagcompound);
	}

	@Override
	public boolean a(EntityHuman entityhuman, EnumHand enumhand) {
		boolean a = super.a(entityhuman, enumhand);
		rightClicked(entityhuman, enumhand);
		return a;
	}

	@Override
	public void e(Vec3D vec3d) {
		riding(vec3d);
	}

	@Override
	public void super_e(Vec3D vec3d) {
		super.e(vec3d);
	}

	@Override
	public void super_setYawPitch(float yaw, float pitch) {
		setYawPitch(yaw, pitch);
	}

	@Override
	public boolean dY() { // this overrides riding check
		return true;
	}

	@Override
	public void setOwnerUUID(UUID id) {
		this.ownerId = id;
	}

	@Override
	public UUID getOwnerUUID() {
		return ownerId;
	}

	@Override
	public void setBoundTime(int time) {
		boundTime = time;
	}

	@Override
	public int getBoundTime() {
		return boundTime;
	}

	@Override
	public EntityTypes<?> getCustomEntityType() {
		return AiostEntityTypes.OWNABLE_SKELETON;
	}
}
