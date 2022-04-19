package com.pm.aiost.entity.ai.pathfinderGoal;

import javax.annotation.Nullable;

import org.bukkit.Location;

import com.pm.aiost.misc.utils.LocationHelper;

import net.minecraft.server.v1_15_R1.EntityCreature;
import net.minecraft.server.v1_15_R1.PathfinderGoal;
import net.minecraft.server.v1_15_R1.RandomPositionGenerator;
import net.minecraft.server.v1_15_R1.Vec3D;

public class PathfinderGoalWalkToLocation extends PathfinderGoal {

	public static final int MAX_TARGET_DISTANCE = 15;

	protected EntityCreature entity;
	private Location loc;
	private double speed;
	private double x;
	private double y;
	private double z;
	private int timeBetweenMovement;
	private boolean g;

	public PathfinderGoalWalkToLocation(EntityCreature entity, Location loc, double speed) {
		this(entity, loc, speed, 5);
	}

	public PathfinderGoalWalkToLocation(EntityCreature entity, Location loc, double speed, int timeBetweenMovement) {
		this.entity = entity;
		this.loc = loc;
		this.speed = speed;
		this.timeBetweenMovement = timeBetweenMovement;
	}

	@Override
	public boolean a() { // shouldStart
		if (entity.isVehicle()) {
			return false;
		}
		if (!this.g) {
			if (entity.cL() >= 100) {
				return false;
			}
			if (LocationHelper.distance(entity, loc) > (MAX_TARGET_DISTANCE * MAX_TARGET_DISTANCE)) {
				return false;
			}
			if (entity.getRandom().nextInt(timeBetweenMovement) != 0) {
				return false;
			}
		}

		Vec3D var0 = g();

		if (var0 == null) {
			return false;
		}

		this.x = var0.x;
		this.y = var0.y;
		this.z = var0.z;
		this.g = false;
		return true;
	}

	@Nullable
	protected Vec3D g() {
		return RandomPositionGenerator.a(entity, 10, 7, new Vec3D(loc.getX(), loc.getY(), loc.getZ()));
	}

	@Override
	public boolean b() {
		return (!entity.getNavigation().m() && !entity.isVehicle());
	}

	@Override
	public void c() { // onStart
		entity.getNavigation().a(x, y, z, speed);
	}

	@Override
	public void d() {
		entity.getNavigation().o();
		super.d();
	}

	public void h() {
		this.g = true;
	}

	public void setTimeBetweenMovement(int time) {
		timeBetweenMovement = time;
	}

// @Override
// public boolean a() { // shouldStart
//		if (entity.isVehicle()) {
//			return false;
//		}
//		if (!this.g) {
//			if (entity.cL() >= 100) {
//				return false;
//			}
//			if (LocationHelper.distance(entity, loc) > (MAX_TARGET_DISTANCE * MAX_TARGET_DISTANCE)) {
//				return false;
//			}
//			if (entity.getRandom().nextInt(timeBetweenMovement) != 0) {
//				return false;
//			}
//		}
//
//		Random random = entity.getRandom();
//		if (entity.locX() > loc.getX())
//			x -= random.nextInt(MAX_TARGET_DISTANCE / 2);
//		else
//			x += random.nextInt(MAX_TARGET_DISTANCE / 2);
//		if (entity.locY() > loc.getY())
//			y -= random.nextInt(MAX_TARGET_DISTANCE / 2);
//		else
//			y += random.nextInt(MAX_TARGET_DISTANCE / 2);
//		if (entity.locZ() > loc.getZ())
//			z -= random.nextInt(MAX_TARGET_DISTANCE / 2);
//		else
//			z += random.nextInt(MAX_TARGET_DISTANCE / 2);
//
//		this.g = false;
//		return true;
// }
}
