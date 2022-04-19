package com.pm.aiost.entity;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nullable;

import org.bukkit.craftbukkit.v1_15_R1.entity.CraftEntity;

import net.minecraft.server.v1_15_R1.AxisAlignedBB;
import net.minecraft.server.v1_15_R1.BlockPosition;
import net.minecraft.server.v1_15_R1.CrashReportSystemDetails;
import net.minecraft.server.v1_15_R1.DamageSource;
import net.minecraft.server.v1_15_R1.DataWatcher;
import net.minecraft.server.v1_15_R1.DimensionManager;
import net.minecraft.server.v1_15_R1.Entity;
import net.minecraft.server.v1_15_R1.EntityHuman;
import net.minecraft.server.v1_15_R1.EntityLightning;
import net.minecraft.server.v1_15_R1.EntityPose;
import net.minecraft.server.v1_15_R1.EntityTypes;
import net.minecraft.server.v1_15_R1.EnumDirection;
import net.minecraft.server.v1_15_R1.EnumItemSlot;
import net.minecraft.server.v1_15_R1.EnumMoveType;
import net.minecraft.server.v1_15_R1.EnumPistonReaction;
import net.minecraft.server.v1_15_R1.IChatBaseComponent;
import net.minecraft.server.v1_15_R1.ItemStack;
import net.minecraft.server.v1_15_R1.MinecraftServer;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import net.minecraft.server.v1_15_R1.ScoreboardTeamBase;
import net.minecraft.server.v1_15_R1.SoundCategory;
import net.minecraft.server.v1_15_R1.Vec3D;
import net.minecraft.server.v1_15_R1.World;

public interface AiostEntity {

	public default Entity getEntity() {
		return (Entity) this;
	}

	public default void saveNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setString("id", EntityTypes.getName(getCustomEntityType()).toString());
	}

	public EntityTypes<?> getCustomEntityType();

	public Random getRandom();

	public CraftEntity getBukkitEntity();

	public void inactiveTick();

	public float getBukkitYaw();

	public boolean isChunkLoaded();

	public boolean isSpectator();

	public void decouple();

	public EntityTypes<?> getEntityType();

	public int getId();

	public Set<String> getScoreboardTags();

	public boolean addScoreboardTag(String s);

	public boolean removeScoreboardTag(String s);

	public void killEntity();

	public DataWatcher getDataWatcher();

	public boolean equals(Object object);

	public int hashCode();

	public void die();

	public EntityPose getPose();

	public void setPosition(double locX, double locY, double locZ);

	public void tick();

	public void postTick();

	public void entityBaseTick();

	public void setOnFire(int i);

	public void setOnFire(int i, boolean callEvent);

	public void extinguish();

	public void move(EnumMoveType enummovetype, Vec3D vec3d);

	public void recalcPosition();

	public boolean isSilent();

	public void setSilent(boolean flag);

	public boolean isNoGravity();

	public void setNoGravity(boolean noGravity);

	public boolean isFireProof();

	public boolean isInWater();

	public boolean isInWaterOrRain();

	public void spawnIn(World world);

	public void setLocation(double d0, double d1, double d2, float f, float f1);

	public void setPositionRotation(BlockPosition blockposition, float f, float f1);

	public void setPositionRotation(double d0, double d1, double d2, float f, float f1);

	public void pickup(EntityHuman entityhuman);

	public void collide(Entity entity);

	public boolean damageEntity(DamageSource damagesource, float f);

	public boolean isInteractable();

	public boolean isCollidable();

	public NBTTagCompound save(NBTTagCompound nbttagcompound);

	@Nullable
	public String getSaveID();

	public boolean isAlive();

	public boolean inBlock();

	public void passengerTick();

	public boolean startRiding(Entity entity);

	public void ejectPassengers();

	public void stopRiding();

	public Vec3D getLookDirection();

	public Iterable<ItemStack> getArmorItems();

	public void setEquipment(EnumItemSlot slot, ItemStack itemStack);

	public boolean isBurning();

	public boolean isPassenger();

	public boolean isVehicle();

	public void setSneaking(boolean flag);

	public boolean isSneaking();

	public boolean isSprinting();

	public void setSprinting(boolean flag);

	public boolean isSwimming();

	public void setSwimming(boolean flag);

	public boolean isInvisible();

	@Nullable
	public ScoreboardTeamBase getScoreboardTeam();

	public void setInvisible(boolean flag);

	public boolean getFlag(int i);

	public void setFlag(int i, boolean flag);

	public int getAirTicks();

	public void setAirTicks(int i);

	public void onLightningStrike(EntityLightning entitylightning);

	public IChatBaseComponent getDisplayName();

	public float getHeadRotation();

	public void setHeadRotation(float f);

	public boolean isInvulnerable(DamageSource damagesource);

	public boolean isInvulnerable();

	public void setInvulnerable(boolean value);

	@Nullable
	public Entity teleportTo(DimensionManager dimensionmanager, BlockPosition location);

	public boolean canPortal();

	public Vec3D getPortalOffset();

	public EnumDirection getPortalDirection();

	public boolean isIgnoreBlockTrigger();

	public void appendEntityCrashDetails(CrashReportSystemDetails crashreportsystemdetails);

	public UUID getUniqueID();

	public String getUniqueIDString();

	public String getName();

	public IChatBaseComponent getScoreboardDisplayName();

	public void setCustomName(@Nullable IChatBaseComponent ichatbasecomponent);

	@Nullable
	public IChatBaseComponent getCustomName();

	public boolean hasCustomName();

	public void setCustomNameVisible(boolean visible);

	public boolean getCustomNameVisible();

	public void enderTeleportAndLoad(double d0, double d1, double d2);

	public void enderTeleportTo(double d0, double d1, double d2);

	public void updateSize();

	public EnumDirection getDirection();

	public EnumDirection getAdjustedDirection();

	public AxisAlignedBB getBoundingBox();

	public float getHeadHeight();

	public void sendMessage(IChatBaseComponent ichatbasecomponent);

	public BlockPosition getChunkCoordinates();

	public World getWorld();

	public MinecraftServer getMinecraftServer();

	public Entity getRidingPassenger();

	public List<Entity> getPassengers();

	public boolean hasSinglePlayerPassenger();

	public Entity getRootVehicle();

	public boolean isSameVehicle(Entity entity);

	public Entity getVehicle();

	public EnumPistonReaction getPushReaction();

	public SoundCategory getSoundCategory();

	public int getMaxFireTicks();

	public boolean shouldSendSuccess();

	public boolean shouldSendFailure();

	public boolean shouldBroadcastCommands();

	public float getWidth();

	public float getHeight();

	public Vec3D getPositionVector();

	public Vec3D getMot();

	public void setMot(Vec3D vec3d);

	public void setMot(double x, double y, double z);

	public double locX();

	public double locY();

	public double getHeadY();

	public double locZ();

	public void setPositionRaw(double d0, double d1, double d2);

	public void checkDespawn();

	public void teleportAndSync(double d0, double d1, double d2);
}
