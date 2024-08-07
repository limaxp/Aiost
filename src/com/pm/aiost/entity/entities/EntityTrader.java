package com.pm.aiost.entity.entities;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R4.CraftServer;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftVillager;

import com.pm.aiost.entity.AiostEntityTypes;
import com.pm.aiost.misc.nms.NMS;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.Level;

public class EntityTrader extends Villager {

	public EntityTrader(EntityType<? extends Villager> entitytypes, Level world) {
		super(EntityType.VILLAGER, world);
	}

	public EntityTrader(Level world) {
		super(EntityType.VILLAGER, world);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new FloatGoal(this));
	}

	@Override
	public void addAdditionalSaveData(CompoundTag nbttagcompound) {
		super.addAdditionalSaveData(nbttagcompound);
		AiostEntityTypes.saveId(nbttagcompound, AiostEntityTypes.TRADER);
	}

	@Override
	public CraftEntity getBukkitEntity() {
		CraftEntity bukkitEntity = NMS.getBukkitEntity(this);
		if (bukkitEntity == null)
			NMS.setBukkitEntity(this, bukkitEntity = new Trader(this));
		return bukkitEntity;
	}

	public static class Trader extends CraftVillager {

		public Trader(EntityTrader entity) {
			super((CraftServer) Bukkit.getServer(), entity);
		}

		@Override
		public EntityTrader getHandle() {
			return (EntityTrader) this.entity;
		}
	}
}
