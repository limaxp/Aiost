package com.pm.aiost.entity;

import java.util.Collection;
import java.util.Optional;

import javax.annotation.Nullable;

import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;

import net.minecraft.server.v1_15_R1.AttributeInstance;
import net.minecraft.server.v1_15_R1.AttributeMapBase;
import net.minecraft.server.v1_15_R1.BlockPosition;
import net.minecraft.server.v1_15_R1.CombatTracker;
import net.minecraft.server.v1_15_R1.DamageSource;
import net.minecraft.server.v1_15_R1.Entity;
import net.minecraft.server.v1_15_R1.EntityLiving;
import net.minecraft.server.v1_15_R1.EnumHand;
import net.minecraft.server.v1_15_R1.EnumItemSlot;
import net.minecraft.server.v1_15_R1.EnumMainHand;
import net.minecraft.server.v1_15_R1.EnumMonsterType;
import net.minecraft.server.v1_15_R1.IAttribute;
import net.minecraft.server.v1_15_R1.ItemStack;
import net.minecraft.server.v1_15_R1.MobEffect;
import net.minecraft.server.v1_15_R1.MobEffectList;

public interface AiostLiving extends AiostEntity {

	@Override
	default EntityLiving getEntity() {
		return (EntityLiving) this;
	}

	public boolean removeAllEffects();

	public boolean removeAllEffects(EntityPotionEffectEvent.Cause cause);

	public Collection<MobEffect> getEffects();

	public boolean hasEffect(MobEffectList mobeffectlist);

	@Nullable
	public MobEffect getEffect(MobEffectList mobeffectlist);

	public boolean addEffect(MobEffect mobeffect);

	public boolean addEffect(MobEffect mobeffect, EntityPotionEffectEvent.Cause cause);

	public boolean removeEffect(MobEffectList mobeffectlist);

	public boolean removeEffect(MobEffectList mobeffectlist, EntityPotionEffectEvent.Cause cause);

	public void heal(float f);

	public void heal(float f, EntityRegainHealthEvent.RegainReason regainReason);

	public float getHealth();

	public void setHealth(float health);

	public boolean damageEntity(DamageSource damagesource, float f);

	public void die(DamageSource damagesource);

	public int getExpReward();

	public boolean isClimbing();

	public boolean isAlive();

	public int getArmorStrength();

	public CombatTracker getCombatTracker();

	public EntityLiving getKillingEntity();

	public float getMaxHealth();

	public int getArrowCount();

	public void setArrowCount(int i);

	public AttributeInstance getAttributeInstance(IAttribute iattribute);

	public AttributeMapBase getAttributeMap();

	public EnumMonsterType getMonsterType();

	public ItemStack getItemInMainHand();

	public ItemStack getItemInOffHand();

	public void setSprinting(boolean flag);

	public void collide(Entity entity);

	public void tick();

	public void movementTick();

	public boolean isRiptiding();

	public void stopRiding();

	public void passengerTick();

	public void setJumping(boolean jumping);

	public void receive(Entity entity, int i);

	public boolean hasLineOfSight(Entity entity);

	public boolean doAITick();

	public boolean isInteractable();

	public boolean isCollidable();

	public float getHeadRotation();

	public void setHeadRotation(float f);

	public float getAbsorptionHearts();

	public void setAbsorptionHearts(float f);

	public void enterCombat();

	public void exitCombat();

	public boolean isHandRaised();

	public EnumHand getRaisedHand();

	public void clearActiveItem();

	public boolean isBlocking();

	public boolean isGliding();

	public Optional<BlockPosition> getBedPosition();

	public boolean isSleeping();

	public void entitySleep(BlockPosition blockposition);

	public void entityWakeup();

	public boolean inBlock();

	public void broadcastItemBreak(EnumItemSlot enumitemslot);

	public void broadcastItemBreak(EnumHand enumhand);

	public Iterable<ItemStack> getArmorItems();

	public ItemStack getEquipment(EnumItemSlot slot);

	public void setSlot(EnumItemSlot slot, ItemStack itemStack);

	public EnumMainHand getMainHand();
}
