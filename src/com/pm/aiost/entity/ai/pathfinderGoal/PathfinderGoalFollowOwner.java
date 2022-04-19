package com.pm.aiost.entity.ai.pathfinderGoal;

import java.util.EnumSet;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftEntity;
import org.bukkit.event.entity.EntityTeleportEvent;

import com.pm.aiost.entity.ownable.OwnableInsentient;

import net.minecraft.server.v1_15_R1.BlockLeaves;
import net.minecraft.server.v1_15_R1.BlockPosition;
import net.minecraft.server.v1_15_R1.EntityInsentient;
import net.minecraft.server.v1_15_R1.EntityLiving;
import net.minecraft.server.v1_15_R1.IBlockData;
import net.minecraft.server.v1_15_R1.IWorldReader;
import net.minecraft.server.v1_15_R1.Navigation;
import net.minecraft.server.v1_15_R1.NavigationAbstract;
import net.minecraft.server.v1_15_R1.NavigationFlying;
import net.minecraft.server.v1_15_R1.PathType;
import net.minecraft.server.v1_15_R1.PathfinderGoal;
import net.minecraft.server.v1_15_R1.PathfinderNormal;

public class PathfinderGoalFollowOwner extends PathfinderGoal {

	private final OwnableInsentient a;
	private final EntityInsentient a_;
	private EntityLiving b;
	private final IWorldReader c;
	private final double d;
	private final NavigationAbstract e;
	private int f;
	private final float g;
	private final float h;
	private float i;
	private final boolean j;

	public PathfinderGoalFollowOwner(OwnableInsentient ownableEntity, double d0, float f, float f1, boolean flag) {
		this.a = ownableEntity;
		this.a_ = ownableEntity.getEntity();
		this.c = a_.world;
		this.d = d0;
		this.e = ownableEntity.getNavigation();
		this.h = f;
		this.g = f1;
		this.j = flag;
		a(EnumSet.of(PathfinderGoal.Type.MOVE, PathfinderGoal.Type.LOOK));
		if (!(ownableEntity.getNavigation() instanceof Navigation)
				&& !(ownableEntity.getNavigation() instanceof NavigationFlying)) {
			throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
		}
	}

	public boolean a() {
		EntityLiving entityliving = this.a.getOwner();

		if (entityliving == null)
			return false;
		if (entityliving.isSpectator())
			return false;
		if (this.a.isSitting())
			return false;
		if (this.a_.h(entityliving) < (this.h * this.h)) {
			return false;
		}
		this.b = entityliving;
		return true;
	}

	public boolean b() {
		return this.e.m() ? false : (this.a.isSitting() ? false : ((this.a_.h(this.b) > (this.g * this.g))));
	}

	public void c() {
		this.f = 0;
		this.i = this.a_.a(PathType.WATER);
		this.a_.a(PathType.WATER, 0.0F);
	}

	public void d() {
		this.b = null;
		this.e.o();
		this.a_.a(PathType.WATER, this.i);
	}

	public void e() {
		this.a.getControllerLook().a(this.b, 10.0F, this.a_.dU());
		if (--this.f <= 0) {
			this.f = 10;
			if (!this.a_.isLeashed() && !this.a.isPassenger()) {
				if (this.a_.h(this.b) >= 144.0D) {
					g();
				} else {
					this.e.a(this.b, this.d);
				}
			}
		}
	}

	private void g() {
		BlockPosition blockposition = new BlockPosition(this.b);

		for (int i = 0; i < 10; i++) {
			int j = a(-3, 3);
			int k = a(-1, 1);
			int l = a(-3, 3);
			boolean flag = a(blockposition.getX() + j, blockposition.getY() + k, blockposition.getZ() + l);

			if (flag) {
				return;
			}
		}
	}

	private boolean a(int i, int j, int k) {
		if (Math.abs(i - this.b.locX()) < 2.0D && Math.abs(k - this.b.locZ()) < 2.0D)
			return false;
		if (!a(new BlockPosition(i, j, k))) {
			return false;
		}

		CraftEntity entity = this.a.getBukkitEntity();
		Location to = new Location(entity.getWorld(), (i + 0.5F), j, (k + 0.5F), this.a_.yaw, this.a_.pitch);
		EntityTeleportEvent event = new EntityTeleportEvent(entity, entity.getLocation(), to);
		this.a_.world.getServer().getPluginManager().callEvent(event);
		if (event.isCancelled()) {
			return false;
		}
		to = event.getTo();

		this.a_.setPositionRotation(to.getX(), to.getY(), to.getZ(), to.getYaw(), to.getPitch());

		this.e.o();
		return true;
	}

	private boolean a(BlockPosition blockposition) {
		PathType pathtype = PathfinderNormal.b(this.c, blockposition.getX(), blockposition.getY(),
				blockposition.getZ());

		if (pathtype != PathType.WALKABLE) {
			return false;
		}
		IBlockData iblockdata = this.c.getType(blockposition.down());

		if (!this.j && iblockdata.getBlock() instanceof BlockLeaves) {
			return false;
		}
		BlockPosition blockposition1 = blockposition.b(new BlockPosition(this.a_));

		return this.c.getCubes(this.a_, this.a.getBoundingBox().a(blockposition1));
	}

	private int a(int i, int j) {
		return this.a.getRandom().nextInt(j - i + 1) + i;
	}
}
