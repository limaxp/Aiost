package com.pm.aiost.entity;

import javax.annotation.Nullable;

import org.bukkit.event.entity.EntityTargetEvent;

import net.minecraft.server.v1_15_R1.ControllerJump;
import net.minecraft.server.v1_15_R1.ControllerLook;
import net.minecraft.server.v1_15_R1.ControllerMove;
import net.minecraft.server.v1_15_R1.DifficultyDamageScaler;
import net.minecraft.server.v1_15_R1.Entity;
import net.minecraft.server.v1_15_R1.EntityInsentient;
import net.minecraft.server.v1_15_R1.EntityLiving;
import net.minecraft.server.v1_15_R1.EntitySenses;
import net.minecraft.server.v1_15_R1.EnumItemSlot;
import net.minecraft.server.v1_15_R1.EnumMainHand;
import net.minecraft.server.v1_15_R1.EnumMobSpawn;
import net.minecraft.server.v1_15_R1.GeneratorAccess;
import net.minecraft.server.v1_15_R1.GroupDataEntity;
import net.minecraft.server.v1_15_R1.ItemStack;
import net.minecraft.server.v1_15_R1.MinecraftKey;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import net.minecraft.server.v1_15_R1.NavigationAbstract;

public interface AiostInsentient extends AiostLiving {

	@Override
	public default EntityInsentient getEntity() {
		return (EntityInsentient) this;
	}

	public ControllerLook getControllerLook();

	public ControllerMove getControllerMove();

	public ControllerJump getControllerJump();

	public NavigationAbstract getNavigation();

	public EntitySenses getEntitySenses();

	@Nullable
	public EntityLiving getGoalTarget();

	public void setGoalTarget(@Nullable EntityLiving entityliving);

	public boolean setGoalTarget(EntityLiving entityliving, EntityTargetEvent.TargetReason reason, boolean fireEvent);

	public void blockEaten();

	public void entityBaseTick();

	public void doSpawnEffect();

	public void tick();

	public MinecraftKey getLootTable();

	public void movementTick();

	public boolean isTypeNotPersistent(double d0);

	public void checkDespawn();

	public int getMaxSpawnGroup();

	public Iterable<ItemStack> getArmorItems();

	public ItemStack getEquipment(EnumItemSlot enumitemslot);

	public void setSlot(EnumItemSlot enumitemslot, ItemStack itemstack);

	public GroupDataEntity prepare(GeneratorAccess generatoraccess, DifficultyDamageScaler difficultydamagescaler,
			EnumMobSpawn enummobspawn, @Nullable GroupDataEntity groupdataentity,
			@Nullable NBTTagCompound nbttagcompound);

	public void setPersistent();

	public boolean canPickupLoot();

	public void setCanPickupLoot(boolean flag);

	public boolean isPersistent();

	public void unleash(boolean flag, boolean flag1);

	public boolean isLeashed();

	@Nullable
	public Entity getLeashHolder();

	public void setLeashHolder(Entity entity, boolean flag);

	public boolean doAITick();

	public void setNoAI(boolean flag);

	public boolean isNoAI();

	public boolean isLeftHanded();

	public EnumMainHand getMainHand();
}
