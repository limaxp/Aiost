package com.pm.aiost.entity;

import java.util.Map;
import java.util.Random;
import java.util.logging.Level;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftLivingEntity;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

import com.pm.aiost.entity.ai.CustomEntitySenses;
import com.pm.aiost.entity.ai.navigation.CustomNavigation;
import com.pm.aiost.entity.ai.navigation.CustomNavigationAbstract;

import net.minecraft.server.v1_15_R1.BlockPosition;
import net.minecraft.server.v1_15_R1.ControllerJump;
import net.minecraft.server.v1_15_R1.ControllerLook;
import net.minecraft.server.v1_15_R1.ControllerMove;
import net.minecraft.server.v1_15_R1.DamageSource;
import net.minecraft.server.v1_15_R1.DifficultyDamageScaler;
import net.minecraft.server.v1_15_R1.EnchantmentManager;
import net.minecraft.server.v1_15_R1.Entity;
import net.minecraft.server.v1_15_R1.EntityHuman;
import net.minecraft.server.v1_15_R1.EntityLiving;
import net.minecraft.server.v1_15_R1.EnumDifficulty;
import net.minecraft.server.v1_15_R1.EnumItemSlot;
import net.minecraft.server.v1_15_R1.EnumMainHand;
import net.minecraft.server.v1_15_R1.EnumMobSpawn;
import net.minecraft.server.v1_15_R1.GameProfilerFiller;
import net.minecraft.server.v1_15_R1.GeneratorAccess;
import net.minecraft.server.v1_15_R1.GenericAttributes;
import net.minecraft.server.v1_15_R1.GroupDataEntity;
import net.minecraft.server.v1_15_R1.Item;
import net.minecraft.server.v1_15_R1.ItemAxe;
import net.minecraft.server.v1_15_R1.ItemStack;
import net.minecraft.server.v1_15_R1.Items;
import net.minecraft.server.v1_15_R1.MathHelper;
import net.minecraft.server.v1_15_R1.MinecraftKey;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import net.minecraft.server.v1_15_R1.PathType;
import net.minecraft.server.v1_15_R1.PathfinderGoalSelector;
import net.minecraft.server.v1_15_R1.World;

public interface CustomInsentient extends AiostLiving {

	@Override
	public default EntityLiving getEntity() {
		return (EntityLiving) this;
	}

	public ControllerLook getControllerLook();

	public ControllerMove getControllerMove();

	public ControllerJump getControllerJump();

	public CustomNavigationAbstract getNavigation();

	public CustomEntitySenses getEntitySenses();

	public @Nullable EntityLiving getGoalTarget();

	public default void setGoalTarget(@Nullable EntityLiving entityliving) {
		setGoalTarget(entityliving, EntityTargetEvent.TargetReason.UNKNOWN, true);
	}

	public default boolean setGoalTarget(EntityLiving entityliving, EntityTargetEvent.TargetReason reason,
			boolean fireEvent) {
		if (getGoalTarget() == entityliving)
			return false;
		if (fireEvent) {
			if (reason == EntityTargetEvent.TargetReason.UNKNOWN && getGoalTarget() != null && entityliving == null) {
				reason = getGoalTarget().isAlive() ? EntityTargetEvent.TargetReason.FORGOT_TARGET
						: EntityTargetEvent.TargetReason.TARGET_DIED;
			}
			if (reason == EntityTargetEvent.TargetReason.UNKNOWN) {
				this.getWorld().getServer().getLogger().log(Level.WARNING,
						"Unknown target reason, please report on the issue tracker", new Exception());
			}
			CraftLivingEntity ctarget = null;
			if (entityliving != null) {
				ctarget = (CraftLivingEntity) entityliving.getBukkitEntity();
			}
			EntityTargetLivingEntityEvent event = new EntityTargetLivingEntityEvent(getBukkitEntity(), ctarget, reason);
			this.getWorld().getServer().getPluginManager().callEvent(event);
			if (event.isCancelled()) {
				return false;
			}

			if (event.getTarget() != null) {
				entityliving = ((CraftLivingEntity) event.getTarget()).getHandle();
			} else {
				entityliving = null;
			}
		}
		return true;
	}

	public default void blockEaten() {
	}

	public void entityBaseTick();

	public default void doSpawnEffect() {
	}

	public void tick();

	public default MinecraftKey getLootTable() {
		// TODO: implement
		return null;
	}

	public void movementTick();

	public boolean isTypeNotPersistent(double d0);

	public void checkDespawn();

	public default int getMaxSpawnGroup() {
		return 4;
	}

	public Iterable<ItemStack> getArmorItems();

	public ItemStack getEquipment(EnumItemSlot enumitemslot);

	public void setSlot(EnumItemSlot enumitemslot, ItemStack itemstack);

	@Nullable
	public default GroupDataEntity prepare(GeneratorAccess generatoraccess,
			DifficultyDamageScaler difficultydamagescaler, EnumMobSpawn enummobspawn,
			@Nullable GroupDataEntity groupdataentity, @Nullable NBTTagCompound nbttagcompound) {
		return groupdataentity;
	}

	public void setPersistent();

	public default boolean canPickupLoot() {
		// TODO: implement
		return false;
	}

	public default void setCanPickupLoot(boolean flag) {
		// TODO: implement
	}

	public boolean isPersistent();

	public default void unleash(boolean flag, boolean flag1) {
		// TODO: implement
	}

	public default boolean isLeashed() {
		// TODO: implement
		return false;
	}

	@Nullable
	public default Entity getLeashHolder() {
		// TODO: implement
		return null;
	}

	public default void setLeashHolder(Entity entity, boolean flag) {
		// TODO: implement
	}

	public boolean doAITick();

	public default void setNoAI(boolean flag) {
		// TODO: implement
	}

	public default boolean isNoAI() {
		// TODO: implement
		return false;
	}

	public default boolean isLeftHanded() {
		// TODO: implement
		return false;
	}

	public EnumMainHand getMainHand();

//	custom stuff from here!!

	public boolean canDespawn();

	public void mobTick();

	public PathfinderGoalSelector getGoalSelector();

	public PathfinderGoalSelector getTargetSelector();

	public Map<PathType, Float> getbB();

	public boolean a(BlockPosition blockposition);

	public float f_(BlockPosition blockposition);

	public void r(float f);

	public void t(float f);

	public void o(float f);

	public BlockPosition ed();

	public float ee();

	public boolean eg();

	public void q(boolean flag);

	public default CustomNavigationAbstract createNavigation(World world) {
		return new CustomNavigation(this, world);
	}

	public default int bD() {
		if (getGoalTarget() == null) {
			return 3;
		}
		int i = (int) (getHealth() - getMaxHealth() * 0.33F);

		i -= (3 - this.getWorld().getDifficulty().a()) * 4;
		if (i < 0) {
			i = 0;
		}

		return i + 3;
	}

	public default int dU_() {
		return 40;
	}

	public default int dV_() {
		return 75;
	}

	public default int dW_() {
		return 10;
	}

	public default float a(PathType pathtype) {
		return getbB().containsKey(pathtype) ? getbB().get(pathtype).floatValue() : pathtype.a();
	}

	public default void a(PathType pathtype, float f) {
		getbB().put(pathtype, Float.valueOf(f));
	}

	public default void doTick(GameProfilerFiller filler) {
		filler.enter("checkDespawn");
		checkDespawn();
		filler.exit();
		filler.enter("sensing");
		getEntitySenses().a();
		filler.exit();
		filler.enter("targetSelector");
		getTargetSelector().doTick();
		filler.exit();
		filler.enter("goalSelector");
		getGoalSelector().doTick();
		filler.exit();
		filler.enter("navigation");
		getNavigation().c();
		filler.exit();
		filler.enter("mob tick");
		mobTick();
		filler.exit();
		filler.enter("controls");
		filler.enter("move");
		getControllerMove().a();
		filler.exitEnter("look");
		getControllerLook().a();
		filler.exitEnter("jump");
		getControllerJump().b();
		filler.exit();
		filler.exit();
	}

	public default boolean B(Entity entity) {
		EntityLiving thisEntity = getEntity();
		float f = (float) getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).getValue();
		float f1 = (float) getAttributeInstance(GenericAttributes.ATTACK_KNOCKBACK).getValue();

		if (entity instanceof EntityLiving) {
			f += EnchantmentManager.a(getItemInMainHand(), ((EntityLiving) entity).getMonsterType());
			f1 += EnchantmentManager.b(thisEntity);
		}

		int i = EnchantmentManager.getFireAspectEnchantmentLevel(thisEntity);

		if (i > 0) {

			EntityCombustByEntityEvent combustEvent = new EntityCombustByEntityEvent(getBukkitEntity(),
					entity.getBukkitEntity(), i * 4);
			Bukkit.getPluginManager().callEvent(combustEvent);

			if (!combustEvent.isCancelled()) {
				entity.setOnFire(combustEvent.getDuration(), false);
			}
		}

		boolean flag = entity.damageEntity(DamageSource.mobAttack(thisEntity), f);

		if (flag) {
			if (f1 > 0.0F && entity instanceof EntityLiving) {
				((EntityLiving) entity).a(thisEntity, f1 * 0.5F, MathHelper.sin(thisEntity.yaw * 0.017453292F),
						-MathHelper.cos(thisEntity.yaw * 0.017453292F));
				setMot(getMot().d(0.6D, 1.0D, 0.6D));
			}

			if (entity instanceof EntityHuman) {
				EntityHuman entityhuman = (EntityHuman) entity;
				ItemStack itemstack = getItemInMainHand();
				ItemStack itemstack1 = entityhuman.isHandRaised() ? entityhuman.dD() : ItemStack.a;

				if (!itemstack.isEmpty() && !itemstack1.isEmpty() && itemstack.getItem() instanceof ItemAxe
						&& itemstack1.getItem() == Items.SHIELD) {
					float f2 = 0.25F + EnchantmentManager.getDigSpeedEnchantmentLevel(thisEntity) * 0.05F;

					if (getRandom().nextFloat() < f2) {
						entityhuman.getCooldownTracker().setCooldown(Items.SHIELD, 100);
						thisEntity.world.broadcastEntityEffect(entityhuman, (byte) 30);
					}
				}
			}

			a(thisEntity, entity);
			thisEntity.z(entity);
		}

		return flag;
	}

	public void a(EntityLiving entityliving, Entity entity);

	public default void a(DifficultyDamageScaler difficultydamagescaler) {
		Random random = getRandom();
		if (random.nextFloat() < 0.15F * difficultydamagescaler.d()) {
			int i = random.nextInt(2);
			float f = (getWorld().getDifficulty() == EnumDifficulty.HARD) ? 0.1F : 0.25F;

			if (random.nextFloat() < 0.095F) {
				i++;
			}

			if (random.nextFloat() < 0.095F) {
				i++;
			}

			if (random.nextFloat() < 0.095F) {
				i++;
			}

			boolean flag = true;
			EnumItemSlot[] aenumitemslot = EnumItemSlot.values();
			int j = aenumitemslot.length;

			for (int k = 0; k < j; k++) {
				EnumItemSlot enumitemslot = aenumitemslot[k];

				if (enumitemslot.a() == EnumItemSlot.Function.ARMOR) {
					ItemStack itemstack = getEquipment(enumitemslot);

					if (!flag && random.nextFloat() < f) {
						break;
					}

					flag = false;
					if (itemstack.isEmpty()) {
						Item item = rollItem(enumitemslot, i);
						if (item != null) {
							setSlot(enumitemslot, new ItemStack(item));
						}
					}
				}
			}
		}
	}

	@Nullable
	public static Item rollItem(EnumItemSlot enumitemslot, int i) {
		switch (enumitemslot.ordinal()) {
		case 5:
			if (i == 0)
				return Items.LEATHER_HELMET;
			if (i == 1)
				return Items.GOLDEN_HELMET;
			if (i == 2)
				return Items.CHAINMAIL_HELMET;
			if (i == 3)
				return Items.IRON_HELMET;
			if (i == 4) {
				return Items.DIAMOND_HELMET;
			}
		case 4:
			if (i == 0)
				return Items.LEATHER_CHESTPLATE;
			if (i == 1)
				return Items.GOLDEN_CHESTPLATE;
			if (i == 2)
				return Items.CHAINMAIL_CHESTPLATE;
			if (i == 3)
				return Items.IRON_CHESTPLATE;
			if (i == 4) {
				return Items.DIAMOND_CHESTPLATE;
			}
		case 3:
			if (i == 0)
				return Items.LEATHER_LEGGINGS;
			if (i == 1)
				return Items.GOLDEN_LEGGINGS;
			if (i == 2)
				return Items.CHAINMAIL_LEGGINGS;
			if (i == 3)
				return Items.IRON_LEGGINGS;
			if (i == 4) {
				return Items.DIAMOND_LEGGINGS;
			}
		case 2:
			if (i == 0)
				return Items.LEATHER_BOOTS;
			if (i == 1)
				return Items.GOLDEN_BOOTS;
			if (i == 2)
				return Items.CHAINMAIL_BOOTS;
			if (i == 3)
				return Items.IRON_BOOTS;
			if (i == 4)
				return Items.DIAMOND_BOOTS;
			break;
		}
		return null;
	}
}
