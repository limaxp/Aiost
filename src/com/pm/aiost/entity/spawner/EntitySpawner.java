package com.pm.aiost.entity.spawner;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scheduler.BukkitRunnable;

import com.pm.aiost.Aiost;
import com.pm.aiost.entity.AiostEntityTypes;

import net.minecraft.server.v1_15_R1.Entity;
import net.minecraft.server.v1_15_R1.EntityTypes;

public abstract class EntitySpawner {

	private static final Consumer<Entity> NULL_CALLBACK = new Consumer<Entity>() {
		@Override
		public void accept(Entity entity) {
		}
	};

	private int intervallTime;
	private int time;
	private int spawnSize;
	private List<EntityTypes<?>> entityTypes;
	public final Random random;
	private Consumer<Entity> spawnCallback;
	private BukkitRunnable scheduler;

	public EntitySpawner(Random random) {
		this.random = random;
		spawnCallback = NULL_CALLBACK;
	}

	public EntitySpawner(int intervallTime, int spawnSize) {
		this(intervallTime, spawnSize, new ArrayList<EntityTypes<?>>(), new Random());
	}

	public EntitySpawner(int intervallTime, int spawnSize, Random random) {
		this(intervallTime, spawnSize, new ArrayList<EntityTypes<?>>(), random);
	}

	public EntitySpawner(int intervallTime, int spawnSize, List<EntityTypes<?>> entityTypes) {
		this(intervallTime, spawnSize, entityTypes, new Random());
	}

	public EntitySpawner(int intervallTime, int spawnSize, List<EntityTypes<?>> entityTypes, Random random) {
		this(random);
		this.intervallTime = intervallTime;
		this.spawnSize = spawnSize;
		this.entityTypes = entityTypes;
	}

	public abstract Location getLocation();

	public final BukkitRunnable startScheduler() {
		scheduler = new BukkitRunnable() {
			@Override
			public void run() {
				tick();
			}
		};
		scheduler.runTaskTimer(Aiost.getPlugin(), 0, 20);
		return scheduler;
	}

	public final void stopScheduler() {
		if (scheduler != null && !scheduler.isCancelled())
			scheduler.cancel();
	}

	public void tick() {
		if (time <= 0) {
			spawn();
			time = intervallTime;
		} else
			time--;
	}

	public void spawn() {
		spawn(spawnSize);
	}

	public void spawn(int size) {
		Location loc = getLocation();
		while (size > 0) {
			int groupSize = Math.min(1 + random.nextInt(size), size);
			size -= groupSize;
			EntityTypes<?> type = getRandomEntityType();
			for (int i = 0; i < groupSize; i++)
				spawnCallback.accept(AiostEntityTypes.spawnEntity(type, loc));
		}
	}

	public void setTime(int time) {
		this.time = time;
	}

	public void resetTime() {
		time = intervallTime;
	}

	public void finishTime() {
		time = 0;
	}

	public void setIntervallTime(int intervallTime) {
		this.intervallTime = intervallTime;
	}

	public int getIntervallTime() {
		return intervallTime;
	}

	public void setSpawnSize(int spawnSize) {
		this.spawnSize = spawnSize;
	}

	public int getSpawnSize() {
		return spawnSize;
	}

	public void setEntityTypes(List<EntityTypes<?>> entityTypes) {
		this.entityTypes = entityTypes;
	}

	public List<EntityTypes<?>> getEntityTypes() {
		return entityTypes;
	}

	public EntityTypes<?> getRandomEntityType() {
		return entityTypes.get(random.nextInt(entityTypes.size()));
	}

	public void addEntityType(EntityTypes<?> type) {
		entityTypes.add(type);
	}

	public void addEntityTypes(EntityTypes<?>... types) {
		int length = types.length;
		for (int i = 0; i < length; i++)
			entityTypes.add(types[i]);
	}

	public void removeEntityType(EntityTypes<?> type) {
		entityTypes.remove(type);
	}

	public void removeEntityTypes(EntityTypes<?>... types) {
		int length = types.length;
		for (int i = 0; i < length; i++)
			entityTypes.remove(types[i]);
	}

	public void setSpawnCallback(Consumer<Entity> spawnCallback) {
		this.spawnCallback = spawnCallback;
	}

	public Consumer<Entity> getSpawnCallback() {
		return spawnCallback;
	}

	public void load(ConfigurationSection section) {
		intervallTime = section.getInt("intervallTime");
		time = section.getInt("time");
		spawnSize = section.getInt("spawnSize");
		this.entityTypes = AiostEntityTypes.load(section, "entityTypes");
	}

	public void save(ConfigurationSection section) {
		section.set("intervallTime", intervallTime);
		section.set("time", time);
		section.set("spawnSize", spawnSize);
		AiostEntityTypes.save(section, "entityTypes", entityTypes);
	}
}
