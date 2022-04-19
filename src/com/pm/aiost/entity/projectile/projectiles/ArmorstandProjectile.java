package com.pm.aiost.entity.projectile.projectiles;

import java.util.Iterator;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftArmorStand;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftEntity;
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
import net.minecraft.server.v1_15_R1.Blocks;
import net.minecraft.server.v1_15_R1.DataWatcher;
import net.minecraft.server.v1_15_R1.DataWatcherObject;
import net.minecraft.server.v1_15_R1.DataWatcherRegistry;
import net.minecraft.server.v1_15_R1.Entity;
import net.minecraft.server.v1_15_R1.EntityArmorStand;
import net.minecraft.server.v1_15_R1.EntityHuman;
import net.minecraft.server.v1_15_R1.EntityLiving;
import net.minecraft.server.v1_15_R1.EntityTypes;
import net.minecraft.server.v1_15_R1.EnumHand;
import net.minecraft.server.v1_15_R1.EnumInteractionResult;
import net.minecraft.server.v1_15_R1.EnumItemSlot;
import net.minecraft.server.v1_15_R1.GameProfileSerializer;
import net.minecraft.server.v1_15_R1.ItemStack;
import net.minecraft.server.v1_15_R1.MathHelper;
import net.minecraft.server.v1_15_R1.MovingObjectPosition;
import net.minecraft.server.v1_15_R1.MovingObjectPositionBlock;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import net.minecraft.server.v1_15_R1.ProjectileHelper;
import net.minecraft.server.v1_15_R1.RayTrace;
import net.minecraft.server.v1_15_R1.Vec3D;
import net.minecraft.server.v1_15_R1.Vector3f;
import net.minecraft.server.v1_15_R1.World;
import net.minecraft.server.v1_15_R1.WorldServer;

public class ArmorstandProjectile extends EntityArmorStand implements CustomProjectile {

	private static final DataWatcherObject<Byte> FLAGS_WATCHER = DataWatcher.a(ArmorstandProjectile.class,
			DataWatcherRegistry.a);

	private static final DataWatcherObject<Float> DAMAGE_WATCHER = DataWatcher.a(ArmorstandProjectile.class,
			DataWatcherRegistry.c);

	private static final DataWatcherObject<Float> KNOCKBACK_WATCHER = DataWatcher.a(ArmorstandProjectile.class,
			DataWatcherRegistry.c);

	private static final Vector3f HAND_POSE = new Vector3f(0, 0, -30);

	protected EntityLiving source;
	protected UUID sourceId;
	protected float damage;
	protected float knockback = 1F;
	protected Effect effect = Effect.EMPTY;
	protected boolean isProjectile = true;
	protected int age;
	protected Entity as;
	protected int at;

	public ArmorstandProjectile(EntityTypes<? extends ArmorstandProjectile> entitytypes, World world) {
		super(entitytypes, world);
	}

	public ArmorstandProjectile(EntityLiving source, ItemStack is) {
		this(source.world, source, is);
	}

	public ArmorstandProjectile(World world, EntityLiving source, ItemStack is) {
		this(world, source.locX(), source.getHeadY() - 0.10000000149011612D, source.locZ(), source.yaw, source.pitch,
				is);
		this.source = source;
		this.sourceId = source.getUniqueID();
		this.projectileSource = (LivingEntity) source.getBukkitEntity();
	}

	public ArmorstandProjectile(World world, double x, double y, double z, float yaw, float pitch, ItemStack is) {
		super(world, x, y, z);
		this.yaw = yaw;
		setItemStack(is);
		setInvisible(true);
		setRightArmPose(HAND_POSE);
		setNoclip(true); // maybe not useful!
	}

	@Override
	public void shoot(double motX, double motY, double motZ, float power, float accuracity) {
		Entity entity = getEntity();
		Random random = getRandom();
		float f2 = MathHelper.sqrt(motX * motX + motY * motY + motZ * motZ);
		motX /= f2;
		motY /= f2;
		motZ /= f2;
		motX += random.nextGaussian() * 0.007499999832361937D * accuracity;
		motY += random.nextGaussian() * 0.007499999832361937D * accuracity;
		motZ += random.nextGaussian() * 0.007499999832361937D * accuracity;
		motX *= power;
		motY *= power;
		motZ *= power;
		entity.setMot(motX, motY, motZ);
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
			if (isAlive()) {
				if (this.age++ >= this.world.spigotConfig.itemDespawnRate)
					die();
			}
			return;
		}

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
					AiostEventFactory.callProjectileHitEvent(this, movingobjectposition);
			}
		}

		defaultUpdateLocation();
	}

	@Override
	public void projectileHit(MovingObjectPosition movingObjectPosition) {
		CustomProjectile.super.projectileHit(movingObjectPosition);
		isProjectile = false;
	}

	@Override
	public EnumInteractionResult a(EntityHuman entityhuman, Vec3D vec3d, EnumHand enumhand) {
		if (!isProjectile) {
			die();
			if (!entityhuman.abilities.canInstantlyBuild)
				return super.a(entityhuman, vec3d, enumhand);
		}
		return EnumInteractionResult.FAIL;
	}

	@Override
	public void b(NBTTagCompound nbttagcompound) {
		super.b(nbttagcompound);
		saveNBT(nbttagcompound);
		nbttagcompound.setFloat("damage", damage);
		nbttagcompound.setFloat("knockback", knockback);
		nbttagcompound.setInt("age", age);
		if (this.sourceId != null)
			nbttagcompound.set("owner", GameProfileSerializer.a(this.sourceId));
		nbttagcompound.set("effect", Effect.saveNBT(effect, new NBTTagCompound()));
	}

	@Override
	public void a(NBTTagCompound nbttagcompound) {
		super.a(nbttagcompound);
		setDamage(nbttagcompound.getFloat("damage"));
		setKnockback(nbttagcompound.getFloat("knockback"));
		age = nbttagcompound.getInt("age");
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

	public void setItemStack(ItemStack is) {
		setSlot(EnumItemSlot.MAINHAND, is);
	}

	public ItemStack getItemStack() {
		return getEquipment(EnumItemSlot.MAINHAND);
	}

	@Override
	public EntityTypes<?> getCustomEntityType() {
		return AiostEntityTypes.ARMORSTAND_PROJECTILE;
	}

	@Override
	public CraftEntity getBukkitEntity() {
		CraftEntity bukkitEntity = NMS.getBukkitEntity(this);
		if (bukkitEntity == null)
			NMS.setBukkitEntity(this, bukkitEntity = new ProjectileArmorstand(this));
		return bukkitEntity;
	}

	public static class ProjectileArmorstand extends CraftArmorStand implements Projectile {

		public ProjectileArmorstand(ArmorstandProjectile entity) {
			super((CraftServer) Bukkit.getServer(), entity);
		}

		@Override
		public ArmorstandProjectile getHandle() {
			return (ArmorstandProjectile) this.entity;
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
