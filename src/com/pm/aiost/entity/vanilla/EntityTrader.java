package com.pm.aiost.entity.vanilla;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftVillager;

import com.pm.aiost.entity.AiostEntityTypes;
import com.pm.aiost.entity.AiostInsentient;
import com.pm.aiost.misc.utils.nms.NMS;

import net.minecraft.server.v1_15_R1.DamageSource;
import net.minecraft.server.v1_15_R1.EntityHuman;
import net.minecraft.server.v1_15_R1.EntityTypes;
import net.minecraft.server.v1_15_R1.EntityVillager;
import net.minecraft.server.v1_15_R1.EnumMoveType;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import net.minecraft.server.v1_15_R1.PathfinderGoalFloat;
import net.minecraft.server.v1_15_R1.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_15_R1.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_15_R1.Vec3D;
import net.minecraft.server.v1_15_R1.World;

public class EntityTrader extends EntityVillager implements AiostInsentient {

	public EntityTrader(EntityTypes<? extends EntityVillager> entitytypes, World world) {
		super(EntityTypes.VILLAGER, world);
	}

	public EntityTrader(World world) {
		super(EntityTypes.VILLAGER, world);
	}

	@Override
	protected void initPathfinder() {
		this.goalSelector.a(1, new PathfinderGoalFloat(this));
		this.goalSelector.a(6, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
		this.goalSelector.a(6, new PathfinderGoalRandomLookaround(this));
	}

	@Override
	public boolean damageEntity(DamageSource damagesource, float f) {
		return false;
	}

	@Override
	public void move(EnumMoveType enummovetype, Vec3D vec3d) {
		super.move(enummovetype, new Vec3D(0, vec3d.y, 0));
	}

	@Override
	public void setHealth(float f) {
		super.setHealth(20);
	}

	@Override
	public NBTTagCompound save(NBTTagCompound nbttagcompound) {
		super.b(nbttagcompound);
		saveNBT(nbttagcompound);
		return nbttagcompound;
	}

	@Override
	public EntityTypes<?> getCustomEntityType() {
		return AiostEntityTypes.TRADER;
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
