package com.pm.aiost.entity.goal;

import org.bukkit.Location;

import com.pm.aiost.misc.utils.LocationHelper;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;

public class PathfinderGoalWalkToLocation extends Goal {

	public static final int MAX_TARGET_DISTANCE = 15;

	protected PathfinderMob entity;
	private Location loc;
	private double speed;
	private double x;
	private double y;
	private double z;
	private int timeBetweenMovement;

	public PathfinderGoalWalkToLocation(PathfinderMob entity, Location loc, double speed) {
		this(entity, loc, speed, 5);
	}

	public PathfinderGoalWalkToLocation(PathfinderMob entity, Location loc, double speed, int timeBetweenMovement) {
		this.entity = entity;
		this.loc = loc;
		this.speed = speed;
		this.timeBetweenMovement = timeBetweenMovement;
	}

	@Override
	public boolean canUse() {
		if (entity.isVehicle()) {
			return false;
		}
		if (entity.getRandom().nextInt(timeBetweenMovement) != 0) {
			return false;
		}
		if (LocationHelper.distance(entity, loc) > (MAX_TARGET_DISTANCE * MAX_TARGET_DISTANCE)) {
			return false;
		}

		this.x = loc.getX();
		this.y = loc.getY();
		this.z = loc.getZ();
		return true;
	}

	@Override
	public void start() {
		entity.getNavigation().moveTo(x, y, z, speed);
	}

	@Override
	public void stop() {
		entity.getNavigation().stop();
	}

	public void setTimeBetweenMovement(int time) {
		timeBetweenMovement = time;
	}
}
