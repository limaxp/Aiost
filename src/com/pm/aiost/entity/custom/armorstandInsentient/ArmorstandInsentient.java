package com.pm.aiost.entity.custom.armorstandInsentient;

import java.util.Map;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftArmorStand;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftEntity;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;

import com.google.common.collect.Maps;
import com.pm.aiost.entity.CustomInsentient;
import com.pm.aiost.entity.ai.CustomEntitySenses;
import com.pm.aiost.entity.ai.controller.ArmorstandControllerLook;
import com.pm.aiost.entity.ai.controller.CustomControllerJump;
import com.pm.aiost.entity.ai.controller.CustomControllerLook;
import com.pm.aiost.entity.ai.controller.CustomControllerMove;
import com.pm.aiost.entity.ai.controller.CustomEntityAIBodyControl;
import com.pm.aiost.entity.ai.navigation.CustomNavigationAbstract;
import com.pm.aiost.misc.utils.nms.NMS;

import net.minecraft.server.v1_15_R1.AttributeInstance;
import net.minecraft.server.v1_15_R1.BlockPosition;
import net.minecraft.server.v1_15_R1.ControllerJump;
import net.minecraft.server.v1_15_R1.ControllerLook;
import net.minecraft.server.v1_15_R1.ControllerMove;
import net.minecraft.server.v1_15_R1.DataWatcher;
import net.minecraft.server.v1_15_R1.DataWatcherObject;
import net.minecraft.server.v1_15_R1.DataWatcherRegistry;
import net.minecraft.server.v1_15_R1.Entity;
import net.minecraft.server.v1_15_R1.EntityArmorStand;
import net.minecraft.server.v1_15_R1.EntityHuman;
import net.minecraft.server.v1_15_R1.EntityLiving;
import net.minecraft.server.v1_15_R1.EntityTypes;
import net.minecraft.server.v1_15_R1.GameProfilerFiller;
import net.minecraft.server.v1_15_R1.GenericAttributes;
import net.minecraft.server.v1_15_R1.IWorldReader;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import net.minecraft.server.v1_15_R1.PathType;
import net.minecraft.server.v1_15_R1.PathfinderGoalSelector;
import net.minecraft.server.v1_15_R1.World;

public abstract class ArmorstandInsentient extends EntityArmorStand implements CustomInsentient {

	public static final DataWatcherObject<Byte> b = DataWatcher.a(ArmorstandInsentient.class, DataWatcherRegistry.a);

	private final Map<PathType, Float> bB = Maps.newEnumMap(PathType.class);
	protected CustomControllerLook lookController;
	protected CustomControllerMove moveController;
	protected CustomControllerJump jumpController;
	private CustomEntityAIBodyControl bodyControl;
	protected CustomNavigationAbstract navigation;
	public PathfinderGoalSelector goalSelector;
	public PathfinderGoalSelector targetSelector;
	private EntityLiving goalTarget;
	private CustomEntitySenses senses;
	public boolean persistent;
	private BlockPosition bH;
	private float bI;

	public ArmorstandInsentient(EntityTypes<? extends ArmorstandInsentient> entitytypes, World world) {
		super(EntityTypes.ARMOR_STAND, world);
		init();
	}

	public ArmorstandInsentient(World world, double d0, double d1, double d2) {
		super(world, d0, d1, d2);
		init();
	}

	protected void init() {
		this.bH = BlockPosition.ZERO;
		this.bI = -1.0F;
		GameProfilerFiller filler = (world != null && world.getMethodProfiler() != null) ? world.getMethodProfiler()
				: null;
		this.goalSelector = new PathfinderGoalSelector(filler);
		this.targetSelector = new PathfinderGoalSelector(filler);
		jumpController = new CustomControllerJump(this);
		lookController = new ArmorstandControllerLook(this);
		moveController = new CustomControllerMove(this);
		bodyControl = new CustomEntityAIBodyControl(this);
		navigation = createNavigation(world);
		senses = new CustomEntitySenses(this);

		if (world != null && !world.isClientSide)
			initPathfinder();

		this.persistent = !isTypeNotPersistent(0.0D);
		this.H = 1.0F; // step height!
	}

	@Override
	protected void initAttributes() {
		super.initAttributes();
		AttributeInstance followRange = getAttributeInstance(GenericAttributes.FOLLOW_RANGE);
		if (followRange == null)
			followRange = getAttributeMap().b(GenericAttributes.FOLLOW_RANGE);
		followRange.setValue(16.0);

		AttributeInstance attackKnockback = getAttributeInstance(GenericAttributes.ATTACK_KNOCKBACK);
		if (attackKnockback == null)
			getAttributeMap().b(GenericAttributes.ATTACK_KNOCKBACK);
	}

	protected void initPathfinder() {
	}

	@Override
	protected void initDatawatcher() {
		super.initDatawatcher();
		this.datawatcher.register(b, Byte.valueOf((byte) 0));
//		setInvisible(true); // TODO: setting it invisible does not work!
	}

	@Override
	public NBTTagCompound save(NBTTagCompound nbttagcompound) {
		super.b(nbttagcompound);
		saveNBT(nbttagcompound);
		return nbttagcompound;
	}

	@Override
	public boolean isTypeNotPersistent(double d0) {
		return true;
	}

	@Override
	protected final void doTick() {
		this.ticksFarFromPlayer++;
		doTick(this.world.getMethodProfiler());
	}

	@Override
	public boolean canDespawn() {
		return false;
	}

	@Override
	public void checkDespawn() {
		if (!isPersistent() && !canDespawn()) {
			EntityHuman entityhuman = this.world.findNearbyPlayer(this, -1.0D);

			if (entityhuman != null) {
				double d0 = entityhuman.h(this);

				if (d0 > 16384.0D) {
					die();
				}

				if (this.ticksFarFromPlayer > 600 && this.random.nextInt(800) == 0 && d0 > 1024.0D) {
					die();
				} else if (d0 < 1024.0D) {
					this.ticksFarFromPlayer = 0;
				}
			}
		} else {

			this.ticksFarFromPlayer = 0;
		}
	}

	@Override
	public boolean isPersistent() {
		return persistent;
	}

	@Override
	public void setPersistent() {
		persistent = true;
	}

	@Override
	public boolean isCollidable() {
		return true;
	}

	@Override
	public void mobTick() {
	}

	@Override
	public CustomNavigationAbstract getNavigation() {
		return navigation;
	}

	@Override
	public ControllerMove getControllerMove() {
		return moveController;
	}

	@Override
	public ControllerJump getControllerJump() {
		return jumpController;
	}

	@Override
	public ControllerLook getControllerLook() {
		return lookController;
	}

	@Override
	public PathfinderGoalSelector getGoalSelector() {
		return goalSelector;
	}

	@Override
	public PathfinderGoalSelector getTargetSelector() {
		return targetSelector;
	}

	@Override
	public @Nullable EntityLiving getGoalTarget() {
		return this.goalTarget;
	}

	@Override
	public CustomEntitySenses getEntitySenses() {
		return senses;
	}

	@Override
	public Map<PathType, Float> getbB() {
		return bB;
	}

	@Override
	public float f_(BlockPosition blockposition) {
		return a(blockposition, this.world);
	}

	public float a(BlockPosition blockposition, IWorldReader iworldreader) {
		return 0.0F;
	}

	@Override
	public boolean a(BlockPosition blockposition) {
		return (this.bI == -1.0F) ? true : ((this.bH.m(blockposition) < (this.bI * this.bI)));
	}

	@Override
	public void r(float f) {
		this.bb = f;
	}

	@Override
	public void t(float f) {
		this.aZ = f;
	}

	@Override
	public void o(float f) {
		super.o(f);
		r(f * 0.2F);
	}

	@Override
	public BlockPosition ed() {
		return this.bH;
	}

	@Override
	public float ee() {
		return this.bI;
	}

	@Override
	public boolean eg() {
		return (this.bI != -1.0F);
	}

	@Override
	protected float f(float f, float f1) {
		bodyControl.a();
		return super.f(f, f1);
	}

	@Override
	public boolean setGoalTarget(EntityLiving entityliving, TargetReason reason, boolean fireEvent) {
		if (CustomInsentient.super.setGoalTarget(entityliving, reason, fireEvent)) {
			this.goalTarget = entityliving;
			return true;
		}
		return false;
	}

	@Override
	public void q(boolean flag) {
		byte b0 = ((Byte) this.getDataWatcher().get(b)).byteValue();

		this.getDataWatcher().set(b, Byte.valueOf(flag ? (byte) (b0 | 0x4) : (byte) (b0 & 0xFFFFFFFB)));
	}

	@Override
	public boolean B(Entity entity) {
		return CustomInsentient.super.B(entity);
	}

	@Override
	public void a(EntityLiving entityliving, Entity entity) {
		super.a(entityliving, entity);
	}

	@Override
	public CraftEntity getBukkitEntity() {
		CraftEntity bukkitEntity = NMS.getBukkitEntity(this);
		if (bukkitEntity == null)
			NMS.setBukkitEntity(this, bukkitEntity = new ArmorstandEntity(this));
		return bukkitEntity;
	}

	public static class ArmorstandEntity extends CraftArmorStand {

		protected ArmorstandEntity(ArmorstandInsentient entity) {
			super((CraftServer) Bukkit.getServer(), entity);
		}

		@Override
		public ArmorstandInsentient getHandle() {
			return (ArmorstandInsentient) this.entity;
		}
	}
}
