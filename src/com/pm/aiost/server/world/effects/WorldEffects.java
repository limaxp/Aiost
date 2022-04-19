package com.pm.aiost.server.world.effects;

import java.io.File;
import java.util.List;
import java.util.Random;

import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;

import com.pm.aiost.event.effect.Effect;
import com.pm.aiost.event.effect.collection.EffectEntryList;
import com.pm.aiost.event.effect.collection.EffectList;

public class WorldEffects extends WorldEffectLoader {

	private static final int CACHE_SIZE = 256;
	private static final int TEMP_ID_BOUND = 1000000;
	private static final int ID_BOUND = Integer.MAX_VALUE - TEMP_ID_BOUND;

	private final Int2ObjectLinkedOpenHashMap<Effect[]> effects;
	private final Int2ObjectLinkedOpenHashMap<EffectEntryList> selfEffects;

	public WorldEffects(File dir) {
		super(dir);
		effects = new Int2ObjectLinkedOpenHashMap<Effect[]>();
		selfEffects = new Int2ObjectLinkedOpenHashMap<EffectEntryList>();
	}

	public int add(Effect... effects) {
		int id = generateId();
		addSynchronized(id, effects);
		save(id, effects, EffectList.EMPTY_EFFECTS);
		return id;
	}

	public int addSelf(Effect... selfEffects) {
		int id = generateId();
		addSelfSynchronized(id, selfEffects);
		save(id, EffectList.EMPTY_EFFECTS, selfEffects);
		return id;
	}

	public int add(Effect[] effects, Effect[] selfEffects) {
		int id = generateId();
		addSynchronized(id, effects, selfEffects);
		save(id, effects, selfEffects);
		return id;
	}

	public int addTemp(Effect effect) {
		return addTemp(new Effect[] { effect });
	}

	public int addTemp(Effect... effects) {
		int id = generateTempId();
		addSynchronized(id, effects);
		return id;
	}

	public int addSelfTemp(Effect... selfEffects) {
		int id = generateTempId();
		addSelfSynchronized(id, selfEffects);
		return id;
	}

	public int addTemp(Effect[] effects, Effect[] selfEffects) {
		int id = generateTempId();
		addSynchronized(id, effects, selfEffects);
		return id;
	}

	protected synchronized void addSynchronized(int id, Effect... effects) {
		if (this.effects.size() >= CACHE_SIZE)
			this.effects.removeLast();
		this.effects.putAndMoveToFirst(id, effects);
	}

	protected synchronized void addSelfSynchronized(int id, Effect... selfEffects) {
		if (this.selfEffects.size() >= CACHE_SIZE)
			this.selfEffects.removeLast();
		this.selfEffects.putAndMoveToFirst(id, new EffectEntryList(selfEffects));
	}

	protected synchronized void addSynchronized(int id, Effect[] effects, Effect[] selfEffects) {
		addSynchronized(id, effects);
		addSelfSynchronized(id, selfEffects);
	}

	public void set(int id, Effect[] effects) {
		addSynchronized(id, effects);
		selfEffects.remove(id);
		save(id, effects, EffectList.EMPTY_EFFECTS);
	}

	public void setSelf(int id, Effect[] selfEffects) {
		addSelfSynchronized(id, selfEffects);
		effects.remove(id);
		save(id, EffectList.EMPTY_EFFECTS, selfEffects);
	}

	public void set(int id, Effect[] effects, Effect[] selfEffects) {
		addSynchronized(id, effects, selfEffects);
		save(id, effects, selfEffects);
	}

	public Effect[] get(int id) {
		Effect[] effects = this.effects.get(id);
		if (effects == null) {
			if (id >= ID_BOUND)
				return EffectList.EMPTY_EFFECTS;
			load(id, this);
			return this.effects.getOrDefault(id, EffectEntryList.EMPTY_EFFECTS);
		}
		return effects;
	}

	public EffectEntryList getSelf(int id) {
		EffectEntryList selfEffects = this.selfEffects.get(id);
		if (selfEffects == null) {
			if (id < ID_BOUND) {
				load(id, this);
				selfEffects = this.selfEffects.getOrDefault(id, EffectEntryList.EMPTY);
			} else
				selfEffects = EffectEntryList.EMPTY;
		}
		return selfEffects;
	}

	public List<Effect> getSelf(int id, byte action) {
		return getSelf(id).getOrEmpty(action);
	}

	public Effect[] getSelfArray(int id) {
		return getSelf(id).getEffects();
	}

	private int generateId() {
		int id;
		Random random = new Random();
		do {
			id = random.nextInt(ID_BOUND);
		} while (fileExists(id));
		return id;
	}

	private int generateTempId() {
		int id;
		Random random = new Random();
		do {
			id = ID_BOUND + random.nextInt(TEMP_ID_BOUND);
		} while (effects.containsKey(id) || selfEffects.containsKey(id));
		return id;
	}
}
