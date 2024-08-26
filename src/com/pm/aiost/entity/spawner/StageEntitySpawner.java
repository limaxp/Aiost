package com.pm.aiost.entity.spawner;

import org.bukkit.configuration.ConfigurationSection;

public class StageEntitySpawner extends EntitySpawner {

	private int toSpawn;
	private int currentlySpawned;
	private int maxSpawnSize;
	private int minSpawnSize;

	public StageEntitySpawner() {
		maxSpawnSize = 40;
		minSpawnSize = 4;
	}

	@Override
	public void spawn() {
		int allowedMaxSpawnSize = maxSpawnSize - currentlySpawned;
		if (toSpawn > 0 && allowedMaxSpawnSize > 0) {
			int possibleMaxSpawnSize = Math.min(toSpawn, allowedMaxSpawnSize);
			int size = Math.min(minSpawnSize + random.nextInt(possibleMaxSpawnSize), possibleMaxSpawnSize);
			spawn(size);
			toSpawn -= size;
			currentlySpawned += size;
		}
	}

	public void respawn() {
		spawn(currentlySpawned);
	}

	@Override
	public void setSpawnSize(int spawnSize) {
		super.setSpawnSize(spawnSize);
		toSpawn = spawnSize;
	}

	public void resetSpawnSize(int spawnSize) {
		toSpawn = spawnSize;
	}

	public void setMaxSpawnSize(int maxSpawnSize) {
		this.maxSpawnSize = maxSpawnSize;
	}

	public int getMaxSpawnSize() {
		return maxSpawnSize;
	}

	public void setMinSpawnSize(int minSpawnSize) {
		this.minSpawnSize = minSpawnSize;
	}

	public int getMinSpawnSize() {
		return minSpawnSize;
	}

	public void onEntityDeath() {
		currentlySpawned--;
	}

	public boolean allSpawned() {
		return toSpawn < 1;
	}

	public boolean allDeath() {
		return currentlySpawned < 1;
	}

	public boolean allSpawnedAndDeath() {
		return toSpawn < 1 && currentlySpawned < 1;
	}

	public int getCurrentEntitySize() {
		return toSpawn + currentlySpawned;
	}

	public int getCurrentlySpawnedEntitySize() {
		return currentlySpawned;
	}

	@Override
	public void load(ConfigurationSection section) {
		super.load(section);
		toSpawn = section.getInt("toSpawn");
		currentlySpawned = section.getInt("currentlySpawned");
		maxSpawnSize = section.getInt("maxSpawnSize");
		minSpawnSize = section.getInt("minSpawnSize");
	}

	@Override
	public void save(ConfigurationSection section) {
		super.save(section);
		section.set("toSpawn", toSpawn);
		section.set("currentlySpawned", currentlySpawned);
		section.set("maxSpawnSize", maxSpawnSize);
		section.set("minSpawnSize", minSpawnSize);
	}
}