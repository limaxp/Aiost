package com.pm.aiost.misc.utils.wrapper;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntitySnapshot;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Pose;
import org.bukkit.entity.SpawnCategory;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

public class EntityWrapper implements Entity {

	protected Entity entity;

	public EntityWrapper(Entity entity) {
		this.entity = entity;
	}

	@Override
	public Location getLocation() {
		return entity.getLocation();
	}

	@Override
	public Location getLocation(Location loc) {
		return entity.getLocation(loc);
	}

	@Override
	public void setVelocity(Vector velocity) {
		entity.setVelocity(velocity);
	}

	@Override
	public Vector getVelocity() {
		return entity.getVelocity();
	}

	@Override
	public double getHeight() {
		return entity.getHeight();
	}

	@Override
	public double getWidth() {
		return entity.getWidth();
	}

	@Override
	public BoundingBox getBoundingBox() {
		return entity.getBoundingBox();
	}

	@Override
	public boolean isOnGround() {
		return entity.isOnGround();
	}

	@Override
	public boolean isInWater() {
		return entity.isInWater();
	}

	@Override
	public World getWorld() {
		return entity.getWorld();
	}

	@Override
	public void setRotation(float yaw, float pitch) {
		entity.setRotation(yaw, pitch);
	}

	@Override
	public boolean teleport(Location location) {
		return entity.teleport(location);
	}

	@Override
	public boolean teleport(Location location, TeleportCause cause) {
		return entity.teleport(location, cause);
	}

	@Override
	public boolean teleport(Entity destination) {
		return entity.teleport(destination);
	}

	@Override
	public boolean teleport(Entity destination, TeleportCause cause) {
		return entity.teleport(destination, cause);
	}

	@Override
	public List<Entity> getNearbyEntities(double x, double y, double z) {
		return entity.getNearbyEntities(x, y, z);
	}

	@Override
	public int getEntityId() {
		return entity.getEntityId();
	}

	@Override
	public int getFireTicks() {
		return entity.getFireTicks();
	}

	@Override
	public int getMaxFireTicks() {
		return entity.getMaxFireTicks();
	}

	@Override
	public void setFireTicks(int ticks) {
		entity.setFireTicks(ticks);
	}

	@Override
	public void setVisualFire(boolean fire) {
		entity.setVisualFire(fire);
	}

	@Override
	public boolean isVisualFire() {
		return entity.isVisualFire();
	}

	@Override
	public int getFreezeTicks() {
		return entity.getFreezeTicks();
	}

	@Override
	public int getMaxFreezeTicks() {
		return entity.getMaxFreezeTicks();
	}

	@Override
	public void setFreezeTicks(int ticks) {
		entity.setFreezeTicks(ticks);
	}

	@Override
	public boolean isFrozen() {
		return entity.isFrozen();
	}

	@Override
	public void remove() {
		entity.remove();
	}

	@Override
	public boolean isDead() {
		return entity.isDead();
	}

	@Override
	public boolean isValid() {
		return entity.isValid();
	}

	@Override
	public Server getServer() {
		return entity.getServer();
	}

	@Override
	public boolean isPersistent() {
		return entity.isPersistent();
	}

	@Override
	public void setPersistent(boolean persistent) {
		entity.setPersistent(persistent);
	}

	@Override
	public Entity getPassenger() {
		return entity.getPassenger();
	}

	@Override
	public boolean setPassenger(Entity passenger) {
		return entity.setPassenger(passenger);
	}

	@Override
	public List<Entity> getPassengers() {
		return entity.getPassengers();
	}

	@Override
	public boolean addPassenger(Entity passenger) {
		return entity.addPassenger(passenger);
	}

	@Override
	public boolean removePassenger(Entity passenger) {
		return entity.removePassenger(passenger);
	}

	@Override
	public boolean isEmpty() {
		return entity.isEmpty();
	}

	@Override
	public boolean eject() {
		return entity.eject();
	}

	@Override
	public float getFallDistance() {
		return entity.getFallDistance();
	}

	@Override
	public void setFallDistance(float distance) {
		entity.setFallDistance(distance);
	}

	@Override
	public void setLastDamageCause(EntityDamageEvent event) {
		entity.setLastDamageCause(event);
	}

	@Override
	public EntityDamageEvent getLastDamageCause() {
		return entity.getLastDamageCause();
	}

	@Override
	public UUID getUniqueId() {
		return entity.getUniqueId();
	}

	@Override
	public int getTicksLived() {
		return entity.getTicksLived();
	}

	@Override
	public void setTicksLived(int value) {
		entity.setTicksLived(value);
	}

	@Override
	public void playEffect(EntityEffect type) {
		entity.playEffect(type);
	}

	@Override
	public EntityType getType() {
		return entity.getType();
	}

	@Override
	public Sound getSwimSound() {
		return entity.getSwimSound();
	}

	@Override
	public Sound getSwimSplashSound() {
		return entity.getSwimSplashSound();
	}

	@Override
	public Sound getSwimHighSpeedSplashSound() {
		return entity.getSwimHighSpeedSplashSound();
	}

	@Override
	public boolean isInsideVehicle() {
		return entity.isInsideVehicle();
	}

	@Override
	public boolean leaveVehicle() {
		return entity.leaveVehicle();
	}

	@Override
	public Entity getVehicle() {
		return entity.getVehicle();
	}

	@Override
	public void setCustomNameVisible(boolean flag) {
		entity.setCustomNameVisible(flag);
	}

	@Override
	public boolean isCustomNameVisible() {
		return entity.isCustomNameVisible();
	}

	@Override
	public void setVisibleByDefault(boolean visible) {
		entity.setVisibleByDefault(visible);
	}

	@Override
	public boolean isVisibleByDefault() {
		return entity.isVisibleByDefault();
	}

	@Override
	public Set<Player> getTrackedBy() {
		return entity.getTrackedBy();
	}

	@Override
	public void setGlowing(boolean flag) {
		entity.setGlowing(flag);
	}

	@Override
	public boolean isGlowing() {
		return entity.isGlowing();
	}

	@Override
	public void setInvulnerable(boolean flag) {
		entity.setInvulnerable(flag);
	}

	@Override
	public boolean isInvulnerable() {
		return entity.isInvulnerable();
	}

	@Override
	public boolean isSilent() {
		return entity.isSilent();
	}

	@Override
	public void setSilent(boolean flag) {
		entity.setSilent(flag);
	}

	@Override
	public boolean hasGravity() {
		return entity.hasGravity();
	}

	@Override
	public void setGravity(boolean gravity) {
		entity.setGravity(gravity);
	}

	@Override
	public int getPortalCooldown() {
		return entity.getPortalCooldown();
	}

	@Override
	public void setPortalCooldown(int cooldown) {
		entity.setPortalCooldown(cooldown);
	}

	@Override
	public Set<String> getScoreboardTags() {
		return entity.getScoreboardTags();
	}

	@Override
	public boolean addScoreboardTag(String tag) {
		return entity.addScoreboardTag(tag);
	}

	@Override
	public boolean removeScoreboardTag(String tag) {
		return entity.removeScoreboardTag(tag);
	}

	@Override
	public PistonMoveReaction getPistonMoveReaction() {
		return entity.getPistonMoveReaction();
	}

	@Override
	public BlockFace getFacing() {
		return entity.getFacing();
	}

	@Override
	public Pose getPose() {
		return entity.getPose();
	}

	@Override
	public SpawnCategory getSpawnCategory() {
		return entity.getSpawnCategory();
	}

	@Override
	public boolean isInWorld() {
		return entity.isInWorld();
	}

	@Override
	public String getAsString() {
		return entity.getAsString();
	}

	@Override
	public EntitySnapshot createSnapshot() {
		return entity.createSnapshot();
	}

	@Override
	public Entity copy() {
		return entity.copy();
	}

	@Override
	public Entity copy(Location to) {
		return entity.copy(to);
	}

	@Override
	public Spigot spigot() {
		return entity.spigot();
	}

	@Override
	public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {
		entity.setMetadata(metadataKey, newMetadataValue);
	}

	@Override
	public List<MetadataValue> getMetadata(String metadataKey) {
		return entity.getMetadata(metadataKey);
	}

	@Override
	public boolean hasMetadata(String metadataKey) {
		return entity.hasMetadata(metadataKey);
	}

	@Override
	public void removeMetadata(String metadataKey, Plugin owningPlugin) {
		entity.removeMetadata(metadataKey, owningPlugin);
	}

	@Override
	public void sendMessage(String message) {
		entity.sendMessage(message);
	}

	@Override
	public void sendMessage(String... messages) {
		entity.sendMessage(messages);
	}

	@Override
	public void sendMessage(UUID sender, String message) {
		entity.sendMessage(sender, message);
	}

	@Override
	public void sendMessage(UUID sender, String... messages) {
		entity.sendMessage(sender, messages);
	}

	@Override
	public String getName() {
		return entity.getName();
	}

	@Override
	public boolean isPermissionSet(String name) {
		return entity.isPermissionSet(name);
	}

	@Override
	public boolean isPermissionSet(Permission perm) {
		return entity.isPermissionSet(perm);
	}

	@Override
	public boolean hasPermission(String name) {
		return entity.hasPermission(name);
	}

	@Override
	public boolean hasPermission(Permission perm) {
		return entity.hasPermission(perm);
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
		return entity.addAttachment(plugin, name, value);
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin) {
		return entity.addAttachment(plugin);
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
		return entity.addAttachment(plugin, name, value, ticks);
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
		return entity.addAttachment(plugin, ticks);
	}

	@Override
	public void removeAttachment(PermissionAttachment attachment) {
		entity.removeAttachment(attachment);
	}

	@Override
	public void recalculatePermissions() {
		entity.recalculatePermissions();
	}

	@Override
	public Set<PermissionAttachmentInfo> getEffectivePermissions() {
		return entity.getEffectivePermissions();
	}

	@Override
	public boolean isOp() {
		return entity.isOp();
	}

	@Override
	public void setOp(boolean value) {
		entity.setOp(value);
	}

	@Override
	public String getCustomName() {
		return entity.getCustomName();
	}

	@Override
	public void setCustomName(String name) {
		entity.setCustomName(name);
	}

	@Override
	public PersistentDataContainer getPersistentDataContainer() {
		return entity.getPersistentDataContainer();
	}
}
