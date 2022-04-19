package com.pm.aiost.misc.registry;

import java.util.Collection;
import java.util.Iterator;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.pm.aiost.collection.list.IdentityArrayList;
import com.pm.aiost.misc.log.Logger;

public class GenIdRegistry<T> extends AiostRegistry<T> {

	protected final IdentityArrayList<T> values;
	protected final BiMap<String, T> keyMap;

	public GenIdRegistry(String name) {
		super(name);
		values = new IdentityArrayList<T>();
		keyMap = HashBiMap.create();
	}

	@Override
	public int register(String key, T value) {
		if (keyMap.containsKey(key)) {
			Logger.log(name + " Registry: key collision! key = " + key + ", value = " + value);
			return -1;
		}
		int id = values.insert(value);
		keyMap.put(key, value);
		return id;
	}

	@Override
	public boolean register(int id, String key, T value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public T get(String key) {
		return keyMap.get(key);
	}

	@Override
	public T getOrDefault(String key, T defaultValue) {
		return keyMap.getOrDefault(key, defaultValue);
	}

	@Override
	public T get(int id) {
		return values.get(id);
	}

	@Override
	public T getOrDefault(int id, T defaultValue) {
		T value = values.get(id);
		return value != null ? value : null;
	}

	@Override
	public T getByIndex(int index) {
		return values.get(index);
	}

	@Override
	public String getKey(T value) {
		return keyMap.inverse().get(value);
	}

	@Override
	public Collection<T> values() {
		return values;
	}

	@Override
	public Iterator<T> iterator() {
		return values.iterator();
	}

	@Override
	public int size() {
		return values.size();
	}
}