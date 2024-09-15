package com.pm.aiost.effect.collection;

import java.util.Collection;

import org.bukkit.inventory.EquipmentSlot;

import com.pm.aiost.effect.Effect;

public class EffectEntry extends EffectList {

	public static final EffectEntry EMPTY = new EffectEntry();

	private final Effect[] effects;

	public EffectEntry() {
		effects = EMPTY_EFFECTS;
	}

	public EffectEntry(Effect... effects) {
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
