package com.pm.aiost.event.effect.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.pm.aiost.event.effect.Effect;
import com.pm.aiost.event.effect.EffectAction;

public interface EffectCollection extends Collection<Effect> {

	public static final Effect[] EMPTY_EFFECTS = new Effect[0];
	public static final List<Effect> EMPTY_EFFECT_LIST = Collections.unmodifiableList(new ArrayList<Effect>());

	public static String toString(byte action, List<Effect> effects) {
		String s = EffectAction.getName(action) + "(";
		int size = effects.size();
		if (size > 0) {
			s += effects.get(0);
			if (size > 1)
				for (int i = 1; i < size; i++)
					s += ", " + effects.get(i);
		}
		return s + ")";
	}

	public static String toString(byte action, Effect[] effects) {
		String s = EffectAction.getName(action) + "(";
		int size = effects.length;
		if (size > 0) {
			s += effects[0];
			if (size > 1)
				for (int i = 1; i < size; i++)
					s += ", " + effects[i];
		}
		return s + ")";
	}
}
