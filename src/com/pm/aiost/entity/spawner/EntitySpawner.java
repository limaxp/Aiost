package com.pm.aiost.entity.spawner;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scheduler.BukkitRunnable;

import com.pm.aiost.Aiost;
import com.pm.aiost.collection.list.IdentityArrayList;
import com.pm.aiost.entity.EntityConfig;
import com.pm.aiost.misc.utils.LocationHelper;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

public abstract class EntitySpawner {

	private static final Consumer<Entity> NULL_CALLBACK = new Consumer<Entity>() {
		@Override
		public void accept(Entity entity) {
		}
	};

	public final Random random;
	private List<EntityConfig> entityTypes;
	private Consumer<Entity> spawnCallback;
	private BukkitRunnable scheduler;
	private int intervallTime;
	private int time;
	private int spawnSize;
	private Location[] locations;
	private int locationVaraety;

	public EntitySpawner() {
		this.random = new Random();
		this.entityTypes = new IdentityArrayList<EntityConfig>(8);
		this.spawnCallback = NULL_CALLBACK;
		this.intervallTime = 20;
		this.spawnSize = 4;
		locationVaraety = 1;
	}

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
		int spawnSize = size / locationVaraety;
		if (spawnSize > 0)
			for (int i = 0; i < locationVaraety; i++)
				spawnRandom(spawnSize);
		spawnRandom(size % locationVaraety);
	}

	private void spawnRandom(int size) {
		Location loc = getLocation();
		while (size > 0) {
			int groupSize = Math.min(1 + random.nextInt(size), size);
			size -= groupSize;
			EntityConfig type = entityTypes.get(random.nextInt(entityTypes.size()));
			for (int i = 0; i < groupSize; i++)
				spawnCallback.accept(type.spawn(loc));
		}
	}

	public void setLocations(Location[] locations) {
		this.locations = locations;
		locationVaraety = locations.length;
	}

	public Location[] getLocations() {
		return locations;
	}

	public void setLocation(int index, Location location) {
		this.locations[index] = location;
	}

	public void setLocation(Location location) {
		this.locations = new Location[] { location };
		locationVaraety = 1;
	}

	public Location getLocation(int index) {
		return locations[index];
	}

	public Location getLocation() {
		return locations[random.nextInt(locations.length)];
	}

	public void setLocationVaraety(int locationVaraety) {
		this.locationVaraety = locationVaraety;
	}

	public int getLocationVaraety() {
		return locationVaraety;
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

	public void setEntityTypes(List<EntityType<?>> entityTypes) {
		this.entityTypes.clear();
		int size = entityTypes.size();
		for (int i = 0; i < size; i++)
			addEntity(entityTypes.get(i));
	}

	public void setEntities(List<EntityConfig> entityTypes) {
		this.entityTypes = entityTypes;
	}

	public List<EntityConfig> getEntities() {
		return entityTypes;
	}

	public void addEntity(EntityType<?> type) {
		entityTypes.add(EntityConfig.wrap(type));
	}

	public void addEntity(EntityConfig type) {
		entityTypes.add(type);
	}

	public void removeEntity(EntityType<?> type) {
		entityTypes.remove(EntityConfig.wrap(type));
	}

	public void removeEntity(EntityConfig type) {
		entityTypes.remove(type);
	}

	public void addEntities(EntityType<?>... types) {
		int length = types.length;
		for (int i = 0; i < length; i++)
			entityTypes.add(EntityConfig.wrap(types[i]));
	}

	public void addEntities(EntityConfig... types) {
		int length = types.length;
		for (int i = 0; i < length; i++)
			entityTypes.add(types[i]);
	}

	public void removeEntities(EntityType<?>... types) {
		int length = types.length;
		for (int i = 0; i < length; i++)
			entityTypes.remove(EntityConfig.wrap(types[i]));
	}

	public void removeEntities(EntityConfig... types) {
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
		this.entityTypes = EntityConfig.load(section, "entityTypes");
		ConfigurationSection locationsSection = section.getConfigurationSection("locations");
		List<Location> locations = new ArrayList<Location>();
		for (String key : locationsSection.getKeys(false))
			locations.add(LocationHelper.load(locationsSection.getConfigurationSection(key)));
		this.locations = locations.toArray(new Location[locations.size()]);
		locationVaraety = section.getInt("locationVaraety");
	}

	public void save(ConfigurationSection section) {
		section.set("intervallTime", intervallTime);
		section.set("time", time);
		section.set("spawnSize", spawnSize);
		EntityConfig.save(section, "entityTypes", entityTypes);
		ConfigurationSection locationsSection = section.createSection("locations");
		int size = locations.length;
		for (int i = 0; i < size; i++)
			LocationHelper.save(locations[i], locationsSection.createSection(Integer.toString(i)));
		section.set("locationVaraety", locationVaraety);
	}
}
