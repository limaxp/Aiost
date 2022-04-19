package com.pm.aiost.entity.ai;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.server.v1_15_R1.Entity;
import net.minecraft.server.v1_15_R1.EntityLiving;

public class CustomEntitySenses {

	private final EntityLiving entity;
	private final List<Entity> seenList;
	private final List<Entity> unseenList;

	public CustomEntitySenses(EntityLiving entity) {
		seenList = Lists.newArrayList();
		unseenList = Lists.newArrayList();
		this.entity = entity;
	}

	public void a() {
		seenList.clear();
		unseenList.clear();
	}

	public boolean a(Entity entity) {
		if (seenList.contains(entity)) {
			return true;
		}
		if (unseenList.contains(entity)) {
			return false;
		}

		this.entity.world.getMethodProfiler().enter("canSee");
		boolean hasLineOfSight = this.entity.hasLineOfSight(entity);
		this.entity.world.getMethodProfiler().exit();
		if (hasLineOfSight) {
			seenList.add(entity);
		} else {
			unseenList.add(entity);
		}
		return hasLineOfSight;
	}
}
