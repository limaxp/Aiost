package com.pm.aiost.entity.ai.pathfinderGoal.custom;

import java.util.EnumSet;
import java.util.function.Predicate;

import org.bukkit.craftbukkit.libs.jline.internal.Nullable;
import org.bukkit.event.entity.EntityTargetEvent;

import com.pm.aiost.entity.CustomInsentient;

import net.minecraft.server.v1_15_R1.AxisAlignedBB;
import net.minecraft.server.v1_15_R1.EntityHuman;
import net.minecraft.server.v1_15_R1.EntityLiving;
import net.minecraft.server.v1_15_R1.EntityPlayer;
import net.minecraft.server.v1_15_R1.PathfinderGoal;
import net.minecraft.server.v1_15_R1.PathfinderTargetCondition;

public class CustomPathfinderGoalNearestAttackableTarget<T extends EntityLiving> extends CustomPathfinderGoalTarget {

	protected final Class<T> a;
	protected final int b;
	protected EntityLiving c;
	protected PathfinderTargetCondition d;

	public CustomPathfinderGoalNearestAttackableTarget(CustomInsentient entityinsentient, Class<T> oclass, boolean flag) {
		this(entityinsentient, oclass, flag, false);
	}

	public CustomPathfinderGoalNearestAttackableTarget(CustomInsentient entityinsentient, Class<T> oclass, boolean flag,
			boolean flag1) {
		this(entityinsentient, oclass, 10, flag, flag1, null);
	}

	public CustomPathfinderGoalNearestAttackableTarget(CustomInsentient entityinsentient, Class<T> oclass, int i,
			boolean flag, boolean flag1, @Nullable Predicate<EntityLiving> predicate) {
		super(entityinsentient, flag, flag1);
		this.a = oclass;
		this.b = i;
		a(EnumSet.of(PathfinderGoal.Type.TARGET));
		this.d = (new PathfinderTargetCondition()).a(k()).a(predicate);
	}

	public boolean a() {
		if (this.b > 0 && this.e.getRandom().nextInt(this.b) != 0) {
			return false;
		}
		g();
		return (this.c != null);
	}

	protected AxisAlignedBB a(double d0) {
		return this.e.getBoundingBox().grow(d0, 4.0D, d0);
	}

	protected void g() {
		if (this.a != EntityHuman.class && this.a != EntityPlayer.class) {
			this.c = this.e.getWorld().b(this.a, this.d, this.e.getEntity(), this.e.locX(), this.e.getHeadY(),
					this.e.locZ(), a(k()));
		} else {
			this.c = this.e.getWorld().a(this.d, this.e.getEntity(), this.e.locX(), this.e.getHeadY(), this.e.locZ());
		}
	}

	public void c() {
		this.e.setGoalTarget(this.c, (this.c instanceof EntityPlayer) ? EntityTargetEvent.TargetReason.CLOSEST_PLAYER
				: EntityTargetEvent.TargetReason.CLOSEST_ENTITY, true);
		super.c();
	}
}
