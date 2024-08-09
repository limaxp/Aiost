package com.pm.aiost.entity.entities;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R4.CraftServer;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftHorse;

import com.pm.aiost.entity.AiostEntityTypes;
import com.pm.aiost.misc.nms.NMS;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityRideable extends Horse {

	private EntityType<?> rideableType = EntityType.HORSE;

	public EntityRideable(EntityType<? extends EntityRideable> entitytypes, Level level) {
		super(EntityType.HORSE, level);
	}

	@Override
	public EntityType<?> getType() {
		return rideableType;
	}

	@Override
	public InteractionResult interactAt(Player entityhuman, Vec3 vec3d, InteractionHand enumhand) {
		doPlayerRide(entityhuman);
		return super.interactAt(entityhuman, vec3d, enumhand);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag nbttagcompound) {
		AiostEntityTypes.saveId(nbttagcompound, AiostEntityTypes.RIDEABLE);
	}

	public void setRideableType(EntityType<?> rideableType) {
		this.rideableType = rideableType;
	}

	@Override
	public CraftEntity getBukkitEntity() {
		CraftEntity bukkitEntity = NMS.getBukkitEntity(this);
		if (bukkitEntity == null)
			NMS.setBukkitEntity(this, bukkitEntity = new RideableEntity(this));
		return bukkitEntity;
	}

	public static class RideableEntity extends CraftHorse {

		public RideableEntity(EntityRideable entity) {
			super((CraftServer) Bukkit.getServer(), entity);
		}

		@Override
		public EntityRideable getHandle() {
			return (EntityRideable) this.entity;
		}

		public void setRideableType(EntityType<?> rideableType) {
			getHandle().rideableType = rideableType;
		}
	}
}