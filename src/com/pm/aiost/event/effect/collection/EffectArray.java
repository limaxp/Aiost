package com.pm.aiost.event.effect.collection;

import java.util.Collection;

import com.pm.aiost.event.effect.Effect;

public class EffectArray extends AbstractEffectCollection implements EffectCollection {

	protected byte[] actions;
	protected Effect[][] effects;

	public EffectArray() {
	}

	public EffectArray(int size) {
		this.actions = new byte[size];
		this.effects = new Effect[size][];
	}

	public EffectArray(byte[] actions, Collection<Effect>[] effects) {
		int size = effects.length;
		Effect[][] effectArr = new Effect[size][];
		for (int i = 0; i < size; i++) {
			Collection<Effect> effectList = effects[i];
			effectArr[i] = effectList.toArray(new Effect[effectList.size()]);
		}
		this.actions = actions;
		this.effects = effectArr;
	}

	public EffectArray(byte[] actions, Effect[][] effects) {
		this.actions = actions;
		this.effects = effects;
	}

	public void set(byte action, Effect[] effects) {
		int size = actions.length;
		for (int i = 0; i < size; i++)
			if (actions[i] == action)
				this.effects[i] = effects;
	}

	public Effect[] get(byte action) {
		return getOrDefault(action, null);
	}

	public Effect[] getOrEmpty(byte action) {
		return getOrDefault(action, EMPTY_EFFECTS);
	}

	public Effect[] getOrDefault(byte action, Effect[] defaultValue) {
		int size = actions.length;
		for (int i = 0; i < size; i++)
			if (actions[i] == action)
				return effects[i];
		return defaultValue;
	}

	@Override
	public String toString() {
		String s = "EffectArray[";
		int size = effects.length;
		if (size > 0) {
			s += toString(0);
			for (int i = 1; i < size; i++)
				s += ", " + toString(i);
		}
		return s + "]";
	}

	public String toString(int index) {
		return EffectCollection.toString(actions[index], this.effects[index]);
	}

	@Override
	public void clear() {
		int size = effects.length;
		for (int i = 0; i < size; i++) {
			actions[i] = 0;
			effects[i] = null;
		}
	}

	@Override
	public boolean isEmpty() {
		return effects.length == 0;
	}
}
