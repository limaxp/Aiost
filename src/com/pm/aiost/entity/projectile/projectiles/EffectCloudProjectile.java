package com.pm.aiost.entity.projectile.projectiles;

import java.util.Iterator;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_15_R1.CraftParticle;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftAreaEffectCloud;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.projectiles.ProjectileSource;

import com.pm.aiost.entity.AiostEntityTypes;
import com.pm.aiost.entity.projectile.CustomProjectile;
import com.pm.aiost.event.AiostEventFactory;
import com.pm.aiost.event.effect.Effect;
import com.pm.aiost.misc.utils.nms.NMS;

import net.minecraft.server.v1_15_R1.AxisAlignedBB;
import net.minecraft.server.v1_15_R1.Blocks;
import net.minecraft.server.v1_15_R1.DataWatcher;
import net.minecraft.server.v1_15_R1.DataWatcherObject;
import net.minecraft.server.v1_15_R1.DataWatcherRegistry;
import net.minecraft.server.v1_15_R1.Entity;
import net.minecraft.server.v1_15_R1.EntityAreaEffectCloud;
import net.minecraft.server.v1_15_R1.EntityLiving;
import net.minecraft.server.v1_15_R1.EntityTypes;
import net.minecraft.server.v1_15_R1.MovingObjectPosition;
import net.minecraft.server.v1_15_R1.MovingObjectPositionBlock;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import net.minecraft.server.v1_15_R1.ParticleParam;
import net.minecraft.server.v1_15_R1.ProjectileHelper;
import net.minecraft.server.v1_15_R1.RayTrace;
import net.minecraft.server.v1_15_R1.World;

public class EffectCloudProjectile extends EntityAreaEffectCloud implements CustomProjectile {

	private static final DataWatcherObject<Byte> FLAGS_WATCHER = DataWatcher.a(EffectCloudProjectile.class,
			DataWatcherRegistry.a);

	private static final DataWatcherObject<Float> DAMAGE_WATCHER = DataWatcher.a(EffectCloudProjectile.class,
			DataWatcherRegistry.c);

	private static final DataWatcherObject<Float> KNOCKBACK_WATCHER = DataWatcher.a(EffectCloudProjectile.class,
			DataWatcherRegistry.c);

	private static final int DEFAULT_DURATION = 40;
	private static final float DEFAULT_RADIUS = 0.6F;

	protected float damage;
	protected float knockback = 1F;
	protected Effect effect = Effect.EMPTY;
	protected Entity as;
	protected int at;

	public EffectCloudProjectile(EntityTypes<? extends EffectCloudProjectile> entitytypes, World world) {
		super(entitytypes, world);
	}

	public EffectCloudProjectile(LivingEntity source, Particle particle) {
		this(((CraftLivingEntity) source).getHandle(), CraftParticle.toNMS(particle));
	}

	public EffectCloudProjectile(LivingEntity source, ParticleParam particle) {
		this(((CraftLivingEntity) source).getHandle(), particle);
	}

	public EffectCloudProjectile(EntityLiving source, Particle particle) {
		this(source.world, source, CraftParticle.toNMS(particle));
	}

	public EffectCloudProjectile(EntityLiving source, ParticleParam particle) {
		this(source.world, source, particle);
	}

	public EffectCloudProjectile(World world, EntityLiving source, Particle particle) {
		this(world, source, CraftParticle.toNMS(particle));
	}

	public EffectCloudProjectile(World world, EntityLiving source, ParticleParam particle) {
		this(world, source.locX(), source.getHeadY() - 0.10000000149011612D, source.locZ(), particle);
		setSource(source);
		this.projectileSource = (LivingEntity) source.getBukkitEntity();
	}

	public EffectCloudProjectile(World world, double x, double y, double z, Particle particle) {
		this(world, x, y, z, CraftParticle.toNMS(particle));
	}

	public EffectCloudProjectile(World world, double x, double y, double z, ParticleParam particle) {
		super(world, x, y, z);

		setParticle(particle);
		setDuration(DEFAULT_DURATION);
		setRadius(DEFAULT_RADIUS);
	}

	@Override
	protected void initDatawatcher() {
		super.initDatawatcher();
		this.datawatcher.register(FLAGS_WATCHER, (byte) 0);
		this.datawatcher.register(DAMAGE_WATCHER, damage);
		this.datawatcher.register(KNOCKBACK_WATCHER, knockback);
	}

	@Override
	public void tick() {
		if (isAlive()) {
			int duration = getDuration() - 1;
			if (duration <= 0)
				die();
			else
				setDuration(duration);
		}

		EntityLiving shooter = this.getSource();
		AxisAlignedBB axisalignedbb = getBoundingBox().a(getMot()).g(1.0D);
		Iterator<Entity> iterator = this.world
				.getEntities(this, axisalignedbb, entity -> (!entity.isSpectator() && entity.isInteractable()))
				.iterator();
		while (iterator.hasNext()) {
			Entity entity = (Entity) iterator.next();
			if (entity == this.as) {
				this.at++;
				break;
			}
			if (shooter != null && this.ticksLived < 2 && this.as == null && shooter == entity) {
				this.as = entity;
				this.at = 3;
				break;
			}
		}
		MovingObjectPosition movingobjectposition = ProjectileHelper.a(this, axisalignedbb,
				entity1 -> (!entity1.isSpectator() && entity1.isInteractable() && entity1 != this.as),
				RayTrace.BlockCollisionOption.OUTLINE, true);

		if (this.as != null && this.at-- <= 0)
			this.as = null;

		if (movingobjectposition.getType() != MovingObjectPosition.EnumMovingObjectType.MISS) {
			if (movingobjectposition.getType() == MovingObjectPosition.EnumMovingObjectType.BLOCK
					&& this.world.getType(((MovingObjectPositionBlock) movingobjectposition).getBlockPosition())
							.getBlock() == Blocks.NETHER_PORTAL) {
				c(((MovingObjectPositionBlock) movingobjectposition).getBlockPosition());
			} else {
				projectileHit(movingobjectposition);

				if (this.dead)
					effect.onProjectileHit(AiostEventFactory.callProjectileHitEvent(this, movingobjectposition));
			}
		}

		defaultUpdateLocation();
	}

	@Override
	public void projectileHit(MovingObjectPosition movingObjectPosition) {
		CustomProjectile.super.projectileHit(movingObjectPosition);
		if (!this.world.isClientSide)
			die();
	}

	@Override
	public void b(NBTTagCompound nbttagcompound) {
		super.b(nbttagcompound);
		saveNBT(nbttagcompound);
		nbttagcompound.setFloat("damage", damage);
		nbttagcompound.setFloat("knockback", knockback);
		nbttagcompound.set("effect", Effect.saveNBT(effect, new NBTTagCompound()));
	}

	@Override
	public void a(NBTTagCompound nbttagcompound) {
		super.a(nbttagcompound);
		setDamage(nbttagcompound.getFloat("damage"));
		setKnockback(nbttagcompound.getFloat("knockback"));
		effect = Effect.loadNBT(nbttagcompound.getCompound("effect"));
	}

	private void setProjectileFlag(int i, boolean flag) {
		byte b0 = ((Byte) this.datawatcher.get(FLAGS_WATCHER)).byteValue();
		if (flag)
			this.datawatcher.set(FLAGS_WATCHER, Byte.valueOf((byte) (b0 | i)));
		else
			this.datawatcher.set(FLAGS_WATCHER, Byte.valueOf((byte) (b0 & (i ^ 0xFFFFFFFF))));
	}

	@Override
	public void setNoclip(boolean noclip) {
		this.noclip = noclip;
		setProjectileFlag(2, noclip);
	}

	@Override
	public boolean noclip() {
		return !this.world.isClientSide ? this.noclip
				: (((this.datawatcher.get(FLAGS_WATCHER).byteValue() & 0x2) != 0) ? true : false);
	}

	@Override
	public void setDamage(float damage) {
		this.damage = damage;
		getDataWatcher().set(DAMAGE_WATCHER, damage);
	}

	@Override
	public float getDamage() {
		return !this.world.isClientSide ? this.damage : this.datawatcher.get(DAMAGE_WATCHER);
	}

	@Override
	public void setKnockback(float knockback) {
		this.knockback = knockback;
		getDataWatcher().set(KNOCKBACK_WATCHER, damage);
	}

	@Override
	public float getKnockback() {
		return !this.world.isClientSide ? this.knockback : this.datawatcher.get(KNOCKBACK_WATCHER);
	}

	@Override
	public void setEffect(Effect effect) {
		this.effect = effect;
	}

	@Override
	public Effect getEffect() {
		return effect;
	}

	@Override
	public Random getRandom() {
		return random;
	}

	@Override
	public EntityTypes<?> getCustomEntityType() {
		return AiostEntityTypes.EFFECT_CLOUD_PROJECTILE;
	}

	@Override
	public CraftEntity getBukkitEntity() {
		CraftEntity bukkitEntity = NMS.getBukkitEntity(this);
		if (bukkitEntity == null)
			NMS.setBukkitEntity(this, bukkitEntity = new ProjectileEffectCloud(this));
		return bukkitEntity;
	}

	public static class ProjectileEffectCloud extends CraftAreaEffectCloud implements Projectile {

		public ProjectileEffectCloud(EffectCloudProjectile entity) {
			super((CraftServer) Bukkit.getServer(), entity);
		}

		@Override
		public EffectCloudProjectile getHandle() {
			return (EffectCloudProjectile) this.entity;
		}

		@Override
		public void setShooter(ProjectileSource shooter) {
			throw new UnsupportedOperationException();
		}

		@Override
		public ProjectileSource getShooter() {
			return entity.projectileSource;
		}

		@Override
		public void setBounce(boolean doesBounce) {
		}

		@Override
		public boolean doesBounce() {
			return false;
		}
	}
}
