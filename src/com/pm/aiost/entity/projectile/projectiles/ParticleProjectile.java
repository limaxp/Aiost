package com.pm.aiost.entity.projectile.projectiles;

import java.util.Iterator;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftLivingEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.projectiles.ProjectileSource;

import com.pm.aiost.entity.AiostEntityTypes;
import com.pm.aiost.entity.projectile.CustomProjectile;
import com.pm.aiost.event.AiostEventFactory;
import com.pm.aiost.event.effect.Effect;
import com.pm.aiost.misc.particleEffect.particle.IParticle;
import com.pm.aiost.misc.utils.nbt.NBTType;
import com.pm.aiost.misc.utils.nms.EntityPlayerIterator;
import com.pm.aiost.misc.utils.nms.NMS;

import net.minecraft.server.v1_15_R1.AxisAlignedBB;
import net.minecraft.server.v1_15_R1.Blocks;
import net.minecraft.server.v1_15_R1.DataWatcher;
import net.minecraft.server.v1_15_R1.DataWatcherObject;
import net.minecraft.server.v1_15_R1.DataWatcherRegistry;
import net.minecraft.server.v1_15_R1.Entity;
import net.minecraft.server.v1_15_R1.EntityLiving;
import net.minecraft.server.v1_15_R1.EntityTypes;
import net.minecraft.server.v1_15_R1.GameProfileSerializer;
import net.minecraft.server.v1_15_R1.MovingObjectPosition;
import net.minecraft.server.v1_15_R1.MovingObjectPositionBlock;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import net.minecraft.server.v1_15_R1.Packet;
import net.minecraft.server.v1_15_R1.PacketPlayOutSpawnEntity;
import net.minecraft.server.v1_15_R1.ProjectileHelper;
import net.minecraft.server.v1_15_R1.RayTrace;
import net.minecraft.server.v1_15_R1.World;
import net.minecraft.server.v1_15_R1.WorldServer;

public class ParticleProjectile extends Entity implements CustomProjectile {

	private static final DataWatcherObject<Byte> FLAGS_WATCHER = DataWatcher.a(ParticleProjectile.class,
			DataWatcherRegistry.a);

	private static final DataWatcherObject<Float> DAMAGE_WATCHER = DataWatcher.a(ParticleProjectile.class,
			DataWatcherRegistry.c);

	private static final DataWatcherObject<Float> KNOCKBACK_WATCHER = DataWatcher.a(ParticleProjectile.class,
			DataWatcherRegistry.c);

	private static final int DEFAULT_DURATION = 40;

	protected IParticle particle;
	protected EntityLiving source;
	protected UUID sourceId;
	protected float damage;
	protected float knockback = 1F;
	protected Effect effect = Effect.EMPTY;
	protected int duration = DEFAULT_DURATION;
	protected Entity as;
	protected int at;

	public ParticleProjectile(EntityTypes<? extends ParticleProjectile> entitytypes, World world) {
		super(entitytypes, world);
	}

	public ParticleProjectile(LivingEntity source, IParticle particle) {
		this(((CraftLivingEntity) source).getHandle(), particle);
	}

	public ParticleProjectile(EntityLiving source, IParticle particle) {
		this(source.world, source, particle);
	}

	public ParticleProjectile(World world, EntityLiving source, IParticle particle) {
		this(world, source.locX(), source.getHeadY() - 0.10000000149011612D, source.locZ(), particle);
		this.source = source;
		this.sourceId = source.getUniqueID();
		this.projectileSource = (LivingEntity) source.getBukkitEntity();
	}

	public ParticleProjectile(World world, double x, double y, double z, IParticle particle) {
		super(AiostEntityTypes.PARTICLE_PROJECTILE, world);
		setPosition(x, y, z);
		this.particle = particle;
	}

	@Override
	protected void initDatawatcher() {
		this.datawatcher.register(FLAGS_WATCHER, (byte) 0);
		this.datawatcher.register(DAMAGE_WATCHER, damage);
		this.datawatcher.register(KNOCKBACK_WATCHER, knockback);
	}

	@Override
	public void tick() {
		if (isAlive()) {
			if (this.duration-- <= 0)
				die();
		}
		particle.spawn(locX(), locY(), locZ(), new EntityPlayerIterator(NMS.getTrackedPlayers(this)));

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
			if (this.source != null && this.ticksLived < 2 && this.as == null && this.source == entity) {
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
		saveNBT(nbttagcompound);
		nbttagcompound.setFloat("damage", damage);
		nbttagcompound.setFloat("knockback", knockback);
		if (this.sourceId != null)
			nbttagcompound.set("owner", GameProfileSerializer.a(this.sourceId));
		nbttagcompound.set("effect", Effect.saveNBT(effect, new NBTTagCompound()));
	}

	@Override
	public void a(NBTTagCompound nbttagcompound) {
		setDamage(nbttagcompound.getFloat("damage"));
		setKnockback(nbttagcompound.getFloat("knockback"));
		if (nbttagcompound.hasKeyOfType("owner", NBTType.COMPOUND))
			sourceId = GameProfileSerializer.b(nbttagcompound.getCompound("owner"));
		effect = Effect.loadNBT(nbttagcompound.getCompound("effect"));
	}

	public Packet<?> L() {
		return new PacketPlayOutSpawnEntity(this);
	}

	@Override
	public EntityLiving getSource() {
		if (this.source == null && this.sourceId != null && this.world instanceof WorldServer) {
			Entity entity = ((WorldServer) this.world).getEntity(this.sourceId);

			if (entity instanceof EntityLiving)
				this.source = (EntityLiving) entity;
			else
				this.sourceId = null;
		}
		return source;
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

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getDuration() {
		return duration;
	}

	@Override
	public Random getRandom() {
		return random;
	}

	@Override
	public EntityTypes<?> getCustomEntityType() {
		return AiostEntityTypes.PARTICLE_PROJECTILE;
	}

	@Override
	public CraftEntity getBukkitEntity() {
		CraftEntity bukkitEntity = NMS.getBukkitEntity(this);
		if (bukkitEntity == null)
			NMS.setBukkitEntity(this, bukkitEntity = new ProjectileParticle(this));
		return bukkitEntity;
	}

	public static class ProjectileParticle extends CraftEntity implements Projectile {

		public ProjectileParticle(ParticleProjectile entity) {
			super((CraftServer) Bukkit.getServer(), entity);
		}

		@Override
		public ParticleProjectile getHandle() {
			return (ParticleProjectile) this.entity;
		}

		@Override
		public EntityType getType() {
			return EntityType.AREA_EFFECT_CLOUD;
		}

		@Override
		public void setShooter(ProjectileSource shooter) {
			if (shooter instanceof CraftLivingEntity) {
				getHandle().source = (EntityLiving) ((CraftEntity) shooter).getHandle();
				getHandle().sourceId = ((CraftLivingEntity) shooter).getUniqueId();
			} else {
				getHandle().source = null;
				getHandle().sourceId = null;
			}
			entity.projectileSource = shooter;
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
