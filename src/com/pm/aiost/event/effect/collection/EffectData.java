package com.pm.aiost.event.effect.collection;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.scheduler.BukkitRunnable;

import com.pm.aiost.Aiost;
import com.pm.aiost.event.effect.Effect;
import com.pm.aiost.event.effect.EffectCondition;
import com.pm.aiost.event.effect.EffectHandler;
import com.pm.aiost.event.events.PlayerEquipHandItemEvent;
import com.pm.aiost.event.events.PlayerEquipItemEvent;
import com.pm.aiost.item.ItemEffects;
import com.pm.aiost.item.custom.Slot;
import com.pm.aiost.misc.utils.nbt.NBTHelper;
import com.pm.aiost.misc.utils.nms.NMS;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.server.world.ServerWorld;
import com.pm.aiost.server.world.effects.WorldEffects;

import net.minecraft.server.v1_15_R1.NBTTagCompound;

public class EffectData extends EffectList {

	private int[] slotIds;
	private int[] worldIds;
	private Effect[][] worldEffects;
	private Effect[] aboveBlockEffect;
	private Effect[] belowBlockEffect;
	private List<Effect> uniqueEffects;
	private List<Effect> blockedUniqueEffects;

	public EffectData() {
		slotIds = new int[Slot.SLOT_SIZE];
		worldIds = new int[Slot.SLOT_SIZE];
		worldEffects = new Effect[Slot.SLOT_SIZE][];
		uniqueEffects = new ArrayList<Effect>();
		blockedUniqueEffects = new ArrayList<Effect>();
	}

	public void setEffect(PlayerEquipItemEvent event) {
		net.minecraft.server.v1_15_R1.ItemStack is = NMS.getNMS(event.getItemStack());
		if (is.hasTag()) {
			NBTTagCompound nbtTag = is.getTag();
			int effectID = NBTHelper.getItemEffect(nbtTag);
			if (effectID != 0) {
				slotIds[event.getSlot().ordinal()] = effectID;
				add(ItemEffects.get(effectID));
				EffectHandler.itemEquipRunEffects(effectID, event);
			} else if ((effectID = NBTHelper.getWorldEffect(nbtTag)) != 0) {
				int slot = event.getSlot().ordinal();
				worldIds[slot] = effectID;
				Effect effect[] = worldEffects[slot] = event.getServerPlayer().getServerWorld().getEffect(effectID);
				add(effect);
				EffectHandler.itemEquipRunWorldEffects(effectID, event);
			}
		}
	}

	public void setHandEffect(PlayerEquipHandItemEvent event) {
		net.minecraft.server.v1_15_R1.ItemStack is = NMS.getNMS(event.getItemStack());
		if (is.hasTag()) {
			NBTTagCompound nbtTag = is.getTag();
			int effectID = NBTHelper.getItemEffect(nbtTag);
			if (effectID != 0) {
				EquipmentSlot slot = event.getSlot();
				slotIds[slot.ordinal()] = effectID;
				addHand(slot, ItemEffects.get(effectID));
				EffectHandler.itemEquipRunEffects(effectID, event);
			} else if ((effectID = NBTHelper.getWorldEffect(nbtTag)) != 0) {
				EquipmentSlot slot = event.getSlot();
				int slotIndex = slot.ordinal();
				worldIds[slotIndex] = effectID;
				Effect effect[] = worldEffects[slotIndex] = event.getServerPlayer().getServerWorld()
						.getEffect(effectID);
				addHand(slot, effect);
				EffectHandler.itemEquipRunWorldEffects(effectID, event);
			}
		}
	}

	public void removeEffect(PlayerEquipItemEvent event) {
		EquipmentSlot slot = event.getSlot();
		int effectID = slotIds[slot.ordinal()];
		if (effectID != 0) {
			slotIds[slot.ordinal()] = 0;
			remove(ItemEffects.get(effectID));
			EffectHandler.itemUnequipRunEffects(effectID, event);
		} else if ((effectID = worldIds[slot.ordinal()]) != 0) {
			int slotIndex = slot.ordinal();
			worldIds[slotIndex] = 0;
			remove(worldEffects[slotIndex]);
			worldEffects[slotIndex] = null;
			EffectHandler.itemUnequipRunWorldEffects(effectID, event);
		}
	}

	public void removeHandEffect(PlayerEquipHandItemEvent event) {
		EquipmentSlot slot = event.getSlot();
		int effectID = slotIds[slot.ordinal()];
		if (effectID != 0) {
			slotIds[slot.ordinal()] = 0;
			removeHand(slot, ItemEffects.get(effectID));
			EffectHandler.itemUnequipRunEffects(effectID, event);
		} else if ((effectID = worldIds[slot.ordinal()]) != 0) {
			int slotIndex = slot.ordinal();
			worldIds[slotIndex] = 0;
			removeHand(slot, worldEffects[slotIndex]);
			worldEffects[slotIndex] = null;
			EffectHandler.itemUnequipRunWorldEffects(effectID, event);
		}
	}

	public void onPlayerChangeBlockPosition(ServerWorld serverWorld, Location futureLocation) {
		int x = futureLocation.getBlockX();
		int y = futureLocation.getBlockY();
		int z = futureLocation.getBlockZ();
		int aboveEffect = serverWorld.getEffect(x, y + 1, z);
		int belowEffect = serverWorld.getEffect(x, y - 1, z);

		removeAboveBlockEffect();
		if (aboveEffect != 0)
			setAboveBlockEffect(serverWorld.getWorldEffects(), aboveEffect);

		removeBelowBlockEffect();
		if (belowEffect != 0)
			setBelowBlockEffect(serverWorld.getWorldEffects(), belowEffect);
	}

	private void setAboveBlockEffect(WorldEffects worldEffects, int effectID) {
		add(aboveBlockEffect = worldEffects.get(effectID));
	}

	private void setBelowBlockEffect(WorldEffects worldEffects, int effectID) {
		add(belowBlockEffect = worldEffects.get(effectID));
	}

	private void removeAboveBlockEffect() {
		if (aboveBlockEffect != null) {
			remove(aboveBlockEffect);
			aboveBlockEffect = null;
		}
	}

	private void removeBelowBlockEffect() {
		if (belowBlockEffect != null) {
			remove(belowBlockEffect);
			belowBlockEffect = null;
		}
	}

	public void add(Effect... effects) {
		for (int i = 0; i < effects.length; i++)
			add(effects[i]);
	}

	@Override
	public boolean add(Effect effect) {
		if (checkCondition(effect)) {
			super.add(effect);
			return true;
		}
		return false;
	}

	public void addHand(EquipmentSlot slot, Effect... effects) {
		for (int i = 0; i < effects.length; i++)
			addHand(slot, effects[i]);
	}

	@Override
	public boolean addHand(EquipmentSlot slot, Effect effect) {
		if (effect.getCondition() != getForbiddenCondition(slot) && checkCondition(effect)) {
			super.addHand(slot, effect);
			return true;
		}
		return false;
	}

	public boolean add(int time, Effect... effects) {
		for (int i = 0; i < effects.length; i++) {
			Effect effect = effects[i];
			if (checkTimeCondition(effect))
				super.add(effect);
			else
				return false;
		}
		registerTimeEffect(time, effects);
		return true;
	}

	public boolean add(int time, Effect effect) {
		if (checkTimeCondition(effect)) {
			super.add(effect);
			registerTimeEffect(time, effect);
			return true;
		}
		return false;
	}

	private void registerTimeEffect(int time, Effect... effects) {
		new RemoveEffectTask(this, effects).runTaskLater(Aiost.getPlugin(), time);
	}

	private boolean checkCondition(Effect effect) {
		byte condition = effect.getCondition();
		if (condition == EffectCondition.SELF)
			return false;
		else if (condition == EffectCondition.UNIQUE)
			return checkUnique(effect);
		return true;
	}

	private boolean checkTimeCondition(Effect effect) {
		byte condition = effect.getCondition();
		if (condition == EffectCondition.SELF)
			return false;
		else if (condition == EffectCondition.UNIQUE)
			return checkTimeUnique(effect);
		return true;
	}

	private boolean checkUnique(Effect effect) {
		if (Effect.containsSimilar(uniqueEffects, effect)) {
			blockedUniqueEffects.add(effect);
			return false;
		}
		uniqueEffects.add(effect);
		return true;
	}

	private boolean checkTimeUnique(Effect effect) {
		if (Effect.containsSimilar(uniqueEffects, effect))
			// doesn't get saved in blockedUniqueEffects and id ignored!
			return false;
		uniqueEffects.add(effect);
		return true;
	}

	public void remove(Effect... effects) {
		for (int i = 0; i < effects.length; i++)
			remove(effects[i]);
	}

	@Override
	public void remove(Effect effect) {
		if (removeCondition(effect, this::add))
			super.remove(effect);
	}

	public void removeHand(EquipmentSlot slot, Effect... effects) {
		for (int i = 0; i < effects.length; i++)
			removeHand(slot, effects[i]);
	}

	@Override
	public void removeHand(EquipmentSlot slot, Effect effect) {
		if (effect.getCondition() != getForbiddenCondition(slot) && removeCondition(effect, (e) -> addHand(slot, e)))
			super.removeHand(slot, effect);
	}

	private boolean removeCondition(Effect effect, Predicate<Effect> addFunction) {
		byte condition = effect.getCondition();
		if (condition == EffectCondition.SELF)
			return false;
		if (condition == EffectCondition.UNIQUE)
			return removeUnique(effect, addFunction);
		return true;
	}

	private boolean removeUnique(Effect effect, Predicate<Effect> addFunction) {
		if (Effect.containsSimilar(uniqueEffects, effect)) {
			uniqueEffects.remove(effect);
			for (Effect blocked : Effect.getSimilar(blockedUniqueEffects, effect)) {
				if (addFunction.test(blocked)) {
					blockedUniqueEffects.remove(blocked);
					uniqueEffects.add(blocked);
					break;
				}
			}
		} else
			blockedUniqueEffects.remove(effect);
		return true;
	}

	@Override
	public void clear() {
		super.clear();
		for (int i = 0; i < Slot.SLOT_SIZE; i++) {
			slotIds[i] = 0;
			worldIds[i] = 0;
			worldEffects[i] = null;
		}
		belowBlockEffect = null;
		belowBlockEffect = null;
		uniqueEffects.clear();
		blockedUniqueEffects.clear();
	}

	public void clearWorldData() {
		clearWorldSlot(Slot.HEAD_ID);
		clearWorldSlot(Slot.CHEST_ID);
		clearWorldSlot(Slot.LEGS_ID);
		clearWorldSlot(Slot.FEET_ID);
		clearWorldHandSlot(Slot.MAIN_HAND_ID);
		clearWorldHandSlot(Slot.OFF_HAND_ID);
		removeAboveBlockEffect();
		removeBelowBlockEffect();
	}

	private void clearWorldSlot(int slot) {
		Effect[] effect = worldEffects[slot];
		if (effect != null)
			remove(effect);
	}

	private void clearWorldHandSlot(int slot) {
		Effect[] effect = worldEffects[slot];
		if (effect != null)
			removeHand(EquipmentSlot.values()[slot], effect);
	}

	public static byte getForbiddenCondition(EquipmentSlot slot) {
		return slot == EquipmentSlot.HAND ? EffectCondition.OFF_HAND : EffectCondition.MAIN_HAND;
	}

	public <T extends Event> void run(byte action, T event, EventFunction<T> func) {
		List<Effect> activeEffects = get(action);
		if (activeEffects != null)
			for (int i = activeEffects.size() - 1; i >= 0; i--)
				func.accept(activeEffects.get(i), event);
	}

	public <T extends Event> void run(ServerPlayer serverPlayer, byte action, T event,
			ServerPlayerEventFunction<T> func) {
		List<Effect> activeEffects = get(action);
		if (activeEffects != null)
			for (int i = activeEffects.size() - 1; i >= 0; i--)
				func.accept(activeEffects.get(i), serverPlayer, event);
	}

	public void run(ServerPlayer serverPlayer, byte action, BiConsumer<Effect, ServerPlayer> func) {
		List<Effect> activeEffects = get(action);
		if (activeEffects != null)
			for (int i = activeEffects.size() - 1; i >= 0; i--)
				func.accept(activeEffects.get(i), serverPlayer);
	}

	private static class RemoveEffectTask extends BukkitRunnable {

		public final EffectData effectData;
		public final Effect[] effects;

		private RemoveEffectTask(EffectData effectData, Effect[] effects) {
			this.effectData = effectData;
			this.effects = effects;
		}

		@Override
		public void run() {
			effectData.remove(effects);
		}
	}

	public static interface EventFunction<T extends Event> {

		public void accept(Effect effect, T event);
	}

	public static interface ServerPlayerEventFunction<T extends Event> {

		public void accept(Effect effect, ServerPlayer serverPlayer, T event);
	}
}