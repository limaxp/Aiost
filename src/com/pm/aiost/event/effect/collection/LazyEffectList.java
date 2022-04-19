package com.pm.aiost.event.effect.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.bukkit.inventory.EquipmentSlot;

import com.pm.aiost.event.effect.Effect;
import com.pm.aiost.event.effect.EffectAction;

public class LazyEffectList extends AbstractEffectCollection implements EffectCollection {

	private static final int DEFAULT_SIZE = 5;

	protected byte[] actions;
	protected List<Effect>[] effects;
	protected int filledAmount;

	public LazyEffectList() {
		this(DEFAULT_SIZE);
	}

	@SuppressWarnings("unchecked")
	public LazyEffectList(int size) {
		this.actions = new byte[size];
		this.effects = new ArrayList[size];
		filledAmount = 0;
	}

	@Override
	public boolean add(Effect effect) {
		for (byte action : effect.getActions())
			add(action, effect);
		return true;
	}

	public void addHand(Effect effect, EquipmentSlot slot) {
		for (byte action : effect.getActions())
			add(EffectAction.getHandAction(action, slot), effect);
	}

	public void add(byte action, Effect effect) {
		for (int i = 0; i < filledAmount; i++) {
			if (actions[i] == action) {
				effects[i].add(effect);
				return;
			}
		}

		if (filledAmount >= actions.length)
			growArrays();
		actions[filledAmount] = action;
		List<Effect> newList = new ArrayList<Effect>();
		newList.add(effect);
		effects[filledAmount] = newList;
		filledAmount++;
	}

	public void remove(Effect effect) {
		for (byte action : effect.getActions())
			remove(action, effect);
	}

	public void removeHand(Effect effect, EquipmentSlot slot) {
		for (byte action : effect.getActions())
			remove(EffectAction.getHandAction(action, slot), effect);
	}

	public void remove(byte action, Effect effect) {
		for (int i = 0; i < filledAmount; i++) {
			if (actions[i] == action) {
				effects[i].remove(effect);
				return;
			}
		}
	}

	public List<Effect> get(byte action) {
		return getOrDefault(action, null);
	}

	public List<Effect> getOrEmpty(byte action) {
		return getOrDefault(action, EMPTY_EFFECT_LIST);
	}

	public List<Effect> getOrDefault(byte action, List<Effect> defaultValue) {
		for (int i = 0; i < filledAmount; i++) // TODO: this could be faster with ordered data!
			if (actions[i] == action)
				return effects[i];
		return defaultValue;
	}

	protected void growArrays() {
		int newSize = actions.length + DEFAULT_SIZE;
		actions = Arrays.copyOf(actions, newSize);
		effects = Arrays.copyOf(effects, newSize);
	}

	@Override
	public String toString() {
		String s = "EffectList[";
		if (filledAmount > 0) {
			s += toString(0);
			for (int i = 1; i < filledAmount; i++)
				s += ", " + toString(i);
		}
		return s + "]";
	}

	public String toString(int index) {
		return EffectCollection.toString(actions[index], this.effects[index]);
	}

	@Override
	public boolean addAll(Collection<? extends Effect> c) {
		for (Effect effect : c)
			add(effect);
		return true;
	}

	public void addAll(Effect[] arr) {
		for (Effect effect : arr)
			add(effect);
	}

	@Override
	public void clear() {
		for (int i = 0; i < filledAmount; i++) {
			actions[i] = 0;
			effects[i] = null;
		}
		filledAmount = 0;
	}

	@Override
	public boolean isEmpty() {
		return filledAmount == 0;
	}

	@Override
	public boolean remove(Object o) {
		if (o instanceof Effect)
			remove((Effect) o);
		return false;
	}

	public EffectArray toEffectArray() {
		Effect[][] effectArr = new Effect[filledAmount][];
		for (int i = 0; i < filledAmount; i++) {
			List<Effect> effectList = effects[i];
			effectArr[i] = effectList.toArray(new Effect[effectList.size()]);
		}
		return new EffectArray(actions, effectArr);
	}
}