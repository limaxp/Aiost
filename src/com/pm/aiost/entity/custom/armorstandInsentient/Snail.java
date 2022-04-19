package com.pm.aiost.entity.custom.armorstandInsentient;

import com.pm.aiost.entity.AiostEntityTypes;
import com.pm.aiost.entity.ai.pathfinderGoal.custom.CustomPathfinderGoalLookAtPlayer;
import com.pm.aiost.entity.ai.pathfinderGoal.custom.CustomPathfinderGoalRandomLookaround;
import com.pm.aiost.entity.ai.pathfinderGoal.custom.CustomPathfinderGoalRandomStrollLand;
import com.pm.aiost.item.Items;
import com.pm.aiost.misc.utils.nms.NMS;

import net.minecraft.server.v1_15_R1.EntityHuman;
import net.minecraft.server.v1_15_R1.EntityTypes;
import net.minecraft.server.v1_15_R1.EnumItemSlot;
import net.minecraft.server.v1_15_R1.ItemStack;
import net.minecraft.server.v1_15_R1.World;

public class Snail extends ArmorstandInsentient {

	private static final ItemStack IDLE_ITEM = NMS.getNMS(Items.get("snail_brown"));
	private static final ItemStack WALKING_ITEM = NMS.getNMS(Items.get("snail_brown_walking"));

	public Snail(EntityTypes<? extends Snail> entitytypes, World world) {
		super(entitytypes, world);
	}

	public Snail(World world, double d0, double d1, double d2) {
		super(world, d0, d1, d2);
	}

	protected void init() {
		super.init();
		setSlot(EnumItemSlot.HEAD, WALKING_ITEM);
	}

	@Override
	protected void initAttributes() {
		super.initAttributes();

	}

	protected void initPathfinder() {
		this.goalSelector.a(2, new CustomPathfinderGoalRandomStrollLand(this, 1.0D));
		this.goalSelector.a(3, new CustomPathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
		this.goalSelector.a(3, new CustomPathfinderGoalRandomLookaround(this));
	}

// TODO: make it so snail is placed right!

//	@Override
//	public void setPosition(double d0, double d1, double d2) {
//		super.setPosition(d0, d1 - 1.188, d2);
//	}

	@Override
	public EntityTypes<?> getCustomEntityType() {
		return AiostEntityTypes.SNAIL;
	}
}