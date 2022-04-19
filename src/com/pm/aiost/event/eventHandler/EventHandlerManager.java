package com.pm.aiost.event.eventHandler;

import java.util.IdentityHashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import com.pm.aiost.server.world.ServerWorld;

public class EventHandlerManager {

	private static final Map<Entity, EventHandler> ENTITY_MAP = new IdentityHashMap<Entity, EventHandler>();
	private static EventHandler defaultHandler = EventHandler.NULL;

	public static void init(@Nonnull EventHandler defaultHandler) {
		if (EventHandlerManager.defaultHandler != EventHandler.NULL)
			return;
		EventHandlerManager.defaultHandler = defaultHandler;
	}

	public static synchronized void registerEntities(@Nonnull World world) {
		for (Entity entity : world.getEntities())
			if (entity instanceof LivingEntity)
				ENTITY_MAP.put(entity, get(entity.getLocation()));
	}

	public static synchronized void registerEntities(@Nonnull Chunk chunk) {
		for (Entity entity : chunk.getEntities())
			if (entity instanceof LivingEntity)
				ENTITY_MAP.put(entity, get(entity.getLocation()));
	}

	public static synchronized void unregisterEntities(@Nonnull Chunk chunk) {
		for (Entity entity : chunk.getEntities())
			if (entity instanceof LivingEntity)
				ENTITY_MAP.remove(entity);
	}

	public static synchronized void setEntityHandler(@Nonnull Entity entity, @Nonnull EventHandler eventHandler) {
		ENTITY_MAP.put(entity, eventHandler);
	}

	public static synchronized @Nullable EventHandler removeEntityHandler(@Nonnull Entity entity) {
		return ENTITY_MAP.remove(entity);
	}

	public static @Nonnull EventHandler get(@Nonnull Location loc) {
		return ServerWorld.getByWorld(loc.getWorld()).getRegion(loc).getEventHandler();
	}

	public static @Nonnull EventHandler get(@Nonnull ServerWorld serverWorld, @Nonnull Location loc) {
		return serverWorld.getRegion(loc).getEventHandler();
	}

	public static @Nullable EventHandler get(@Nonnull Entity entity) {
		return ENTITY_MAP.get(entity);
	}

	public static @Nullable EventHandler getOrDefault(@Nonnull Entity entity, EventHandler eventHandler) {
		return ENTITY_MAP.getOrDefault(entity, eventHandler);
	}

	public static @Nullable EventHandler getOrEmpty(@Nonnull Entity entity) {
		return ENTITY_MAP.getOrDefault(entity, EventHandler.NULL);
	}

	public static @Nonnull EventHandler getDefault() {
		return defaultHandler;
	}
}
