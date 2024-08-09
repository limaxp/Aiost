package com.pm.aiost.entity.goal;

import java.util.EnumSet;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R4.event.CraftEventFactory;
import org.bukkit.event.entity.EntityTeleportEvent;

import com.pm.aiost.misc.event.eventHandler.handler.OwnableEventHandler;
import com.pm.aiost.misc.nms.NMS;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;

public class FollowOwnerGoal extends Goal {

	private final Mob tamable;
	private LivingEntity owner;
	private final LevelReader level;
	private final double speedModifier;
	private final PathNavigation navigation;
	private int timeToRecalcPath;
	private final float stopDistance;
	private final float startDistance;
	private float oldWaterCost;
	private final boolean canFly;
	protected OwnableEventHandler handler;

	public FollowOwnerGoal(Mob entitytameableanimal, OwnableEventHandler handler, double d0, float f, float f1,
			boolean flag) {
		this.tamable = entitytameableanimal;
		this.level = entitytameableanimal.level();
		this.speedModifier = d0;
		this.navigation = entitytameableanimal.getNavigation();
		this.startDistance = f;
		this.stopDistance = f1;
		this.canFly = flag;
		this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
		if (!(entitytameableanimal.getNavigation() instanceof GroundPathNavigation)
				&& !(entitytameableanimal.getNavigation() instanceof FlyingPathNavigation)) {
			throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
		}
		this.handler = handler;
	}

	public boolean canUse() {
		org.bukkit.entity.LivingEntity entityliving0 = this.handler.getOwner();
		if (entityliving0 == null) {
			return false;
		}
		LivingEntity entityliving = NMS.to(entityliving0);
		if (entityliving.isSpectator()) {
			return false;
		} else if (this.unableToMove()) {
			return false;
		} else if (this.tamable.distanceToSqr(entityliving) < (double) (this.startDistance * this.startDistance)) {
			return false;
		} else {
			this.owner = entityliving;
			return true;
		}
	}

	public boolean canContinueToUse() {
		return this.navigation.isDone() ? false
				: (this.unableToMove() ? false
						: this.tamable.distanceToSqr(this.owner) > (double) (this.stopDistance * this.stopDistance));
	}

	private boolean unableToMove() {
		return this.tamable.isPassenger() || this.tamable.mayBeLeashed();
	}

	public void start() {
		this.timeToRecalcPath = 0;
		this.oldWaterCost = this.tamable.getPathfindingMalus(PathType.WATER);
		this.tamable.setPathfindingMalus(PathType.WATER, 0.0F);
	}

	public void stop() {
		this.owner = null;
		this.navigation.stop();
		this.tamable.setPathfindingMalus(PathType.WATER, this.oldWaterCost);
	}

	public void tick() {
		this.tamable.getLookControl().setLookAt(this.owner, 10.0F, (float) this.tamable.getMaxHeadXRot());
		if (--this.timeToRecalcPath <= 0) {
			this.timeToRecalcPath = this.adjustedTickDelay(10);
			if (this.tamable.distanceToSqr(this.owner) >= 144.0) {
				this.teleportToOwner();
			} else {
				this.navigation.moveTo(this.owner, this.speedModifier);
			}
		}

	}

	private void teleportToOwner() {
		BlockPos blockposition = this.owner.blockPosition();

		for (int i = 0; i < 10; ++i) {
			int j = this.randomIntInclusive(-3, 3);
			int k = this.randomIntInclusive(-1, 1);
			int l = this.randomIntInclusive(-3, 3);
			boolean flag = this.maybeTeleportTo(blockposition.getX() + j, blockposition.getY() + k,
					blockposition.getZ() + l);
			if (flag) {
				return;
			}
		}

	}

	private boolean maybeTeleportTo(int i, int j, int k) {
		if (Math.abs((double) i - this.owner.getX()) < 2.0 && Math.abs((double) k - this.owner.getZ()) < 2.0) {
			return false;
		} else if (!this.canTeleportTo(new BlockPos(i, j, k))) {
			return false;
		} else {
			EntityTeleportEvent event = CraftEventFactory.callEntityTeleportEvent(this.tamable, (double) i + 0.5,
					(double) j, (double) k + 0.5);
			if (event.isCancelled()) {
				return false;
			} else {
				Location to = event.getTo();
				this.tamable.moveTo(to.getX(), to.getY(), to.getZ(), to.getYaw(), to.getPitch());
				this.navigation.stop();
				return true;
			}
		}
	}

	private boolean canTeleportTo(BlockPos blockposition) {
		PathType pathtype = WalkNodeEvaluator.getPathTypeStatic(this.tamable, blockposition);
		if (pathtype != PathType.WALKABLE) {
			return false;
		} else {
			BlockState iblockdata = this.level.getBlockState(blockposition.below());
			if (!this.canFly && iblockdata.getBlock() instanceof LeavesBlock) {
				return false;
			} else {
				BlockPos blockposition1 = blockposition.subtract(this.tamable.blockPosition());
				return this.level.noCollision(this.tamable, this.tamable.getBoundingBox().move(blockposition1));
			}
		}
	}

	private int randomIntInclusive(int i, int j) {
		return this.tamable.getRandom().nextInt(j - i + 1) + i;
	}
}
