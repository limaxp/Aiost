package com.pm.aiost.entity.ai.pathfinderGoal.custom;

import com.pm.aiost.entity.CustomInsentient;

import net.minecraft.server.v1_15_R1.EntityHuman;
import net.minecraft.server.v1_15_R1.EntityLiving;

public class CustomPathfinderGoalLookAtPlayer extends net.minecraft.server.v1_15_R1.PathfinderGoalLookAtPlayer {

	protected final CustomInsentient a;
	private int g;

	public CustomPathfinderGoalLookAtPlayer(CustomInsentient entity, Class<? extends EntityLiving> var1, float var2) {
		this(entity, var1, var2, 0.02F);
	}

	public CustomPathfinderGoalLookAtPlayer(CustomInsentient entity, Class<? extends EntityLiving> var1, float var2,
			float var3) {
		super(null, var1, var2, var3);
		a = entity;
	}

	@Override
	public boolean a() {
		if (this.a.getRandom().nextFloat() >= this.d) {
			return false;
		}

		if (this.a.getGoalTarget() != null) {
			this.b = this.a.getGoalTarget();
		}
		if (this.e == EntityHuman.class) {
			this.b = this.a.getWorld().a(this.f, this.a.getEntity(), this.a.locX(), this.a.getHeadY(), this.a.locZ());
		} else {
			this.b = this.a.getWorld().b(this.e, this.f, this.a.getEntity(), this.a.locX(), this.a.getHeadY(),
					this.a.locZ(), this.a.getBoundingBox().grow(this.c, 3.0D, this.c));
		}

		return (this.b != null);
	}

	@Override
	public boolean b() {
		if (!this.b.isAlive()) {
			return false;
		}
		if (this.a.getEntity().h(this.b) > (this.c * this.c)) {
			return false;
		}
		return (this.g > 0);
	}

	@Override
	public void c() {
		this.g = 40 + this.a.getRandom().nextInt(40);
	}

	@Override
	public void e() {
		this.a.getControllerLook().a(this.b.locX(), this.b.getHeadY(), this.b.locZ());
		this.g--;
	}
}
