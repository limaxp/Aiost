package com.pm.aiost.event.effect.collection;

import java.util.Collection;

import org.bukkit.inventory.EquipmentSlot;

import com.pm.aiost.event.effect.Effect;

public class EffectEntryList extends EffectList {

	public static final EffectEntryList EMPTY = new EffectEntryList();

	private final Effect[] effects;

	public EffectEntryList() {
		effects = EMPTY_EFFECTS;
	}

	public EffectEntryList(Effect... effects) {
		this.effects = effects;
		for (int i = 0; i < effects.length; i++)
			super.add(effects[i]);
	}

	public Effect[] getEffects() {
		return effects;
	}

	@Override
	public boolean add(Effect effect) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addHand(EquipmentSlot slot, Effect effect) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void remove(Effect effect) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeHand(EquipmentSlot slot, Effect effect) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(Collection<? extends Effect> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addAll(Effect... effects) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}
}
