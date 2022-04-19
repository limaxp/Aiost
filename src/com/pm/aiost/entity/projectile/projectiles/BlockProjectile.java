package com.pm.aiost.entity.projectile.projectiles;

import java.util.Iterator;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftFallingBlock;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.projectiles.ProjectileSource;

import com.pm.aiost.entity.AiostEntityTypes;
import com.pm.aiost.entity.projectile.CustomProjectile;
import com.pm.aiost.event.AiostEventFactory;
import com.pm.aiost.event.effect.Effect;
import com.pm.aiost.misc.utils.nbt.NBTType;
import com.pm.aiost.misc.utils.nms.NMS;

import net.minecraft.server.v1_15_R1.AxisAlignedBB;
import net.minecraft.server.v1_15_R1.Block;
import net.minecraft.server.v1_15_R1.Blocks;
import net.minecraft.server.v1_15_R1.DataWatcher;
import net.minecraft.server.v1_15_R1.DataWatcherObject;
import net.minecraft.server.v1_15_R1.DataWatcherRegistry;
import net.minecraft.server.v1_15_R1.Entity;
import net.minecraft.server.v1_15_R1.EntityFallingBlock;
import net.minecraft.server.v1_15_R1.EntityLiving;
import net.minecraft.server.v1_15_R1.EntityTypes;
import net.minecraft.server.v1_15_R1.GameProfileSerializer;
import net.minecraft.server.v1_15_R1.IBlockData;
import net.minecraft.server.v1_15_R1.MovingObjectPosition;
import net.minecraft.server.v1_15_R1.MovingObjectPositionBlock;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import net.minecraft.server.v1_15_R1.ProjectileHelper;
import net.minecraft.server.v1_15_R1.RayTrace;
import net.minecraft.server.v1_15_R1.World;
import net.minecraft.server.v1_15_R1.WorldServer;

public class BlockProjectile extends EntityFallingBlock implements CustomProjectile {

	private static final DataWatcherObject<Byte> FLAGS_WATCHER = DataWatcher.a(BlockProjectile.class,
			DataWatcherRegistry.a);

	private static final DataWatcherObject<Float> DAMAGE_WATCHER = DataWatcher.a(BlockProjectile.class,
			DataWatcherRegistry.c);

	private static final DataWatcherObject<Float> KNOCKBACK_WATCHER = DataWatcher.a(BlockProjectile.class,
			DataWatcherRegistry.c);

	protected EntityLiving source;
	protected UUID sourceId;
	protected float damage;
	protected float knockback = 1F;
	protected Effect effect = Effect.EMPTY;
	protected boolean isProjectile = true;
	protected Entity as;
	protected int at;

	public BlockProjectile(EntityTypes<? extends BlockProjectile> entitytypes, World world) {
		super(entitytypes, world);
	}

	public BlockProjectile(EntityLiving source, Block block) {
		this(source.world, source, block.getBlockData());
	}

	public BlockProjectile(EntityLiving source, IBlockData blockData) {
		this(source.world, source, blockData);
	}

	public BlockProjectile(World world, EntityLiving source, Block block) {
		this(world, source, block.getBlockData());
	}

	public BlockProjectile(World world, EntityLiving source, IBlockData blockData) {
		super(world, source.locX(), source.getHeadY() - 0.10000000149011612D, source.locZ(), blockData);
		this.source = source;
		this.sourceId = source.getUniqueID();
		this.projectileSource = (LivingEntity) source.getBukkitEntity();
	}

	public BlockProjectile(World world, double x, double y, double z, Block block) {
		this(world, x, y, z, block.getBlockData());
	}

	public BlockProjectile(World world, double x, double y, double z, IBlockData blockData) {
		super(world, x, y, z, blockData);
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
		if (!isProjectile) {
			super.tick();
			return;
		} else {
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

					if (!this.isProjectile)
						effect.onProjectileHit(AiostEventFactory.callProjectileHitEvent(this, movingobjectposition));
				}
			}

			defaultUpdateLocation();
		}
	}

	@Override
	public void projectileHit(MovingObjectPosition movingObjectPosition) {
		CustomProjectile.super.projectileHit(movingObjectPosition);
		isProjectile = false;
	}

	@Override
	public void b(NBTTagCompound nbttagcompound) {
		super.b(nbttagcompound);
		saveNBT(nbttagcompound);
		nbttagcompound.setFloat("damage", damage);
		nbttagcompound.setFloat("knockback", knockback);
		nbttagcompound.setBoolean("isProjectile", isProjectile);
		if (this.sourceId != null)
			nbttagcompound.set("owner", GameProfileSerializer.a(this.sourceId));
		nbttagcompound.set("effect", Effect.saveNBT(effect, new NBTTagCompound()));
	}

	@Override
	public void a(NBTTagCompound nbttagcompound) {
		super.a(nbttagcompound);
		setDamage(nbttagcompound.getFloat("damage"));
		setKnockback(nbttagcompound.getFloat("knockback"));
		isProjectile = nbttagcompound.getBoolean("isProjectile");
		if (nbttagcompound.hasKeyOfType("owner", NBTType.COMPOUND))
			sourceId = GameProfileSerializer.b(nbttagcompound.getCompound("owner"));
		effect = Effect.loadNBT(nbttagcompound.getCompound("effect"));
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

	@Override
	public Random getRandom() {
		return random;
	}

	@Override
	public EntityTypes<?> getCustomEntityType() {
		return AiostEntityTypes.BLOCK_PROJECTILE;
	}

	@Override
	public CraftEntity getBukkitEntity() {
		CraftEntity bukkitEntity = NMS.getBukkitEntity(this);
		if (bukkitEntity == null)
			NMS.setBukkitEntity(this, bukkitEntity = new ProjectileBlock(this));
		return bukkitEntity;
	}

	public static class ProjectileBlock extends CraftFallingBlock implements Projectile {

		public ProjectileBlock(BlockProjectile entity) {
			super((CraftServer) Bukkit.getServer(), entity);
		}

		@Override
		public BlockProjectile getHandle() {
			return (BlockProjectile) this.entity;
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
		public void setBounce(boolean arg0) {
		}

		@Override
		public boolean doesBounce() {
			return false;
		}
	}
}
