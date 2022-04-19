package com.pm.aiost.event.effect.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.inventory.EquipmentSlot;

import com.pm.aiost.event.effect.Effect;
import com.pm.aiost.event.effect.EffectAction;

public class EffectList extends AbstractEffectCollection implements EffectCollection {

	protected List<Effect>[] effects;

	@SuppressWarnings("unchecked")
	public EffectList() {
		this.effects = new List[EffectAction.size()];
	}

	@Override
	public boolean add(Effect effect) {
		for (byte action : effect.getActions())
			add(action, effect);
		return true;
	}

	public boolean addHand(EquipmentSlot slot, Effect effect) {
		for (byte action : effect.getActions())
			add(EffectAction.getHandAction(action, slot), effect);
		return true;
	}

	private void add(byte action, Effect effect) {
		List<Effect> list = effects[action];
		if (list == null) {
			list = new ArrayList<Effect>();
			effects[action] = list;
		}
		list.add(effect);
	}

	@Override
	public boolean remove(Object o) {
		remove((Effect) o);
		return true;
	}

	public void remove(Effect effect) {
		for (byte action : effect.getActions())
			remove(action, effect);
	}

	public void removeHand(EquipmentSlot slot, Effect effect) {
		for (byte action : effect.getActions())
			remove(EffectAction.getHandAction(action, slot), effect);
	}

	private void remove(byte action, Effect effect) {
		effects[action].remove(effect);
	}

	public List<Effect> get(byte action) {
		return effects[action];
	}

	public List<Effect> getOrEmpty(byte action) {
		return getOrDefault(action, EMPTY_EFFECT_LIST);
	}

	private List<Effect> getOrDefault(byte action, List<Effect> defaultValue) {
		List<Effect> value = effects[action];
		return value != null ? value : defaultValue;
	}

	@Override
	public String toString() {
		String s = "EffectList[";
		boolean found = false;
		for (int i = 0; i < effects.length; i++) {
			if (effects[i] != null) {
				if (found == false) {
					s += toString(0);
					found = true;
				} else
					s += ", " + toString(i);
			}
		}
		return s + "]";
	}

	public String toString(int index) {
		return EffectCollection.toString((byte) index, this.effects[index]);
	}

	@Override
	public boolean addAll(Collection<? extends Effect> c) {
		for (Effect effect : c)
			add(effect);
		return true;
	}

	public void addAll(Effect... effects) {
		for (int i = 0; i < effects.length; i++)
			add(effects[i]);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		for (Object obj : c)
			remove(obj);
		return true;
	}

	public boolean removeAll(Effect... effects) {
		for (int i = 0; i < effects.length; i++)
			remove(effects[i]);
		return true;
	}

	@Override
	public void clear() {
		for (int i = 0; i < effects.length; i++)
			effects[i] = null;
	}
}