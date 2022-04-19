package com.pm.aiost.event.effect.collection;

import java.util.List;

import com.pm.aiost.collection.list.FastArrayList;
import com.pm.aiost.event.effect.Effect;
import com.pm.aiost.event.effect.EffectCondition;
import com.pm.aiost.item.ItemEffects;
import com.pm.aiost.server.world.ServerWorld;
import com.pm.aiost.server.world.effects.WorldEffects;

public class EffectEntryBuilder extends AbstractEffectCollection implements EffectCollection {

	private List<Effect> effects;
	private List<Effect> selfEffects;

	public EffectEntryBuilder() {
		effects = new FastArrayList<Effect>();
		selfEffects = new FastArrayList<Effect>();
	}

	public EffectEntryBuilder(int size, int selfSize) {
		effects = new FastArrayList<Effect>(size);
		selfEffects = new FastArrayList<Effect>(selfSize);
	}

	@Override
	public boolean add(Effect effect) {
		if (effect.getCondition() == EffectCondition.SELF)
			selfEffects.add(effect);
		else
			effects.add(effect);
		return true;
	}

	public void remove(Effect effect) {
		if (effect.getCondition() == EffectCondition.SELF)
			selfEffects.remove(effect);
		else
			effects.remove(effect);
	}

	public void addAll(Effect... effects) {
		for (int i = 0; i < effects.length; i++)
			add(effects[i]);
	}

	public void addAll(List<Effect> effects) {
		for (int i = 0; i < effects.size(); i++)
			add(effects.get(i));
	}

	public Effect get(int index) {
		return effects.get(index);
	}

	public Effect getSelf(int index) {
		return selfEffects.get(index);
	}

	@Override
	public void clear() {
		effects.clear();
		selfEffects.clear();
	}

	@Override
	public boolean isEmpty() {
		return effects.size() == 0 && selfEffects.isEmpty();
	}

	public boolean hasEffects() {
		return effects.size() > 0;
	}

	public boolean hasSelfEffects() {
		return selfEffects.size() > 0;
	}

	public Effect[] createEffectArray() {
		return effects.toArray(new Effect[effects.size()]);
	}

	public Effect[] createSelfEffectArray() {
		return selfEffects.toArray(new Effect[selfEffects.size()]);
	}

	public int createItemEntry(String name) {
		if (hasEffects())
			if (hasSelfEffects())
				return ItemEffects.add(name, createEffectArray(), createSelfEffectArray());
			else
				return ItemEffects.add(name, createEffectArray());
		else if (hasSelfEffects())
			return ItemEffects.addSelf(name, createSelfEffectArray());
		return -1;
	}

	public int createWorldEntry(ServerWorld serverWorld) {
		if (hasEffects())
			if (hasSelfEffects())
				return serverWorld.getWorldEffects().add(createEffectArray(), createSelfEffectArray());
			else
				return serverWorld.getWorldEffects().add(createEffectArray());
		else if (hasSelfEffects())
			return serverWorld.getWorldEffects().addSelf(createSelfEffectArray());
		return -1;
	}

	public void replaceWorldEntry(int id, WorldEffects worldEffects) {
		if (hasEffects())
			if (hasSelfEffects())
				worldEffects.set(id, createEffectArray(), createSelfEffectArray());
			else
				worldEffects.set(id, createEffectArray());
		else if (hasSelfEffects())
			worldEffects.setSelf(id, createSelfEffectArray());
	}
}