package com.pm.aiost.server.world.region;

import java.io.File;
import java.util.Random;

import javax.annotation.Nullable;

import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;

import com.pm.aiost.server.world.ServerWorld;

public class WorldRegions extends RegionLoader {

	private static final int BUFFER_SIZE = 256;

	private final Int2ObjectLinkedOpenHashMap<Region> regions;

	public WorldRegions(File effectsFile) {
		super(effectsFile);
		regions = new Int2ObjectLinkedOpenHashMap<Region>();
	}

	public void add(ServerWorld serverWorld, Region region) {
		int id = generateId();
		region.setServerWorld(serverWorld);
		region.setId(id);
		addSynchronized(id, region);
		save(region);
		region.registerWorld();
	}

	public void remove(Region region) {
		int id = region.getId();
		removeSynchronized(id);
		delete(id);
		region.unregisterWorld();
	}

	protected synchronized void addSynchronized(int id, Region region) {
		if (regions.size() >= BUFFER_SIZE)
			regions.removeLast();
		regions.putAndMoveToFirst(id, region);
	}

	protected synchronized void removeSynchronized(int id) {
		regions.remove(id);
	}

	public void update(Region region) {
		addSynchronized(region.getId(), region);
		save(region);
	}

	public @Nullable Region get(ServerWorld serverWorld, int id) {
		Region region = regions.get(id);
		if (region == null) {
			region = new Region();
			region.setServerWorld(serverWorld);
			region.setId(id);
			addSynchronized(id, region);
			load(region);
		}
		return region;
	}

	private int generateId() {
		int id;
		Random random = new Random();
		do {
			id = random.nextInt(Integer.MAX_VALUE);
		} while (fileExists(id));
		return id;
	}
}
