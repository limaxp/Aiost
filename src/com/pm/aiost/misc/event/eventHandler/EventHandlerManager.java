package com.pm.aiost.misc.event.eventHandler;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import com.pm.aiost.collection.list.IdentityArrayList;
import com.pm.aiost.game.GameLobby;
import com.pm.aiost.misc.event.eventHandler.handler.CancelEventHandler;
import com.pm.aiost.misc.event.eventHandler.handler.DuelEventHandler;
import com.pm.aiost.misc.event.eventHandler.handler.DuelRegionEventHandler;
import com.pm.aiost.misc.event.eventHandler.handler.LobbyEventHandler;
import com.pm.aiost.misc.event.eventHandler.handler.PlayerRegionEventHandler;
import com.pm.aiost.misc.event.eventHandler.handler.PlayerWorldEventHandler;
import com.pm.aiost.misc.event.eventHandler.handler.ProjectileEventHandler;
import com.pm.aiost.misc.event.eventHandler.handler.ReleasedWorldEventHandler;
import com.pm.aiost.misc.event.eventHandler.handler.SpectatorEventHandler;
import com.pm.aiost.misc.event.eventHandler.handler.SurvivalEventHandler;
import com.pm.aiost.misc.registry.AiostRegistry;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.world.ServerWorld;

public class EventHandlerManager {

	private static final Map<Entity, EventHandler> ENTITY_MAP = new IdentityHashMap<Entity, EventHandler>(100);
	private static final List<Entity> TICKABLE_ENTITIES = new IdentityArrayList<Entity>(100);
	private static final List<TickableHandler> TICKABLE_ENTITY_HANDLER = new IdentityArrayList<TickableHandler>(100);

	private static EventHandler defaultHandler = EventHandler.NULL;

	static {
		register(CancelEventHandler::getInstance);
		register(LobbyEventHandler::getInstance);
		register(SurvivalEventHandler::getInstance);
		register(PlayerRegionEventHandler::new);
		register(PlayerWorldEventHandler::new);
		register(ReleasedWorldEventHandler::new);
		register(GameLobby::new);
		register(SpectatorEventHandler::getInstance);
		register(DuelEventHandler::new);
		register(DuelRegionEventHandler::new);
		register(ProjectileEventHandler::new);
	}

	public static void init() {
	}

	public static void init(EventHandler defaultHandler) {
		if (EventHandlerManager.defaultHandler != EventHandler.NULL)
			return;
		EventHandlerManager.defaultHandler = defaultHandler;
	}

	public static void update() {
		int size = TICKABLE_ENTITIES.size();
		for (int i = 0; i < size; i++)
			TICKABLE_ENTITY_HANDLER.get(i).onTick(TICKABLE_ENTITIES.get(i));
	}

	public static synchronized void registerEntities(World world) {
		for (Entity entity : world.getEntities())
			if (entity instanceof LivingEntity)
				ENTITY_MAP.put(entity, get(entity.getLocation()));
	}

	public static synchronized void registerEntities(Chunk chunk) {
		for (Entity entity : chunk.getEntities())
			if (entity instanceof LivingEntity)
				ENTITY_MAP.put(entity, get(entity.getLocation()));
	}

	public static synchronized void unregisterEntities(Chunk chunk) {
		for (Entity entity : chunk.getEntities())
			if (entity instanceof LivingEntity)
				ENTITY_MAP.remove(entity);
	}

	public static synchronized void setEntityHandler(Entity entity, EventHandler eventHandler) {
		ENTITY_MAP.put(entity, eventHandler);
		if (eventHandler instanceof TickableHandler) {
			TICKABLE_ENTITIES.add(entity);
			TICKABLE_ENTITY_HANDLER.add((TickableHandler) eventHandler);
		}
	}

	public static synchronized EventHandler removeEntityHandler(Entity entity) {
		EventHandler eventHandler = ENTITY_MAP.remove(entity);
		if (eventHandler instanceof TickableHandler) {
			int index = TICKABLE_ENTITIES.lastIndexOf(entity);
			TICKABLE_ENTITIES.remove(index);
			TICKABLE_ENTITY_HANDLER.remove(index);
		}

		else if (eventHandler == null)
			return EventHandler.NULL;
		return eventHandler;
	}

	public static EventHandler get(Location loc) {
		return ServerWorld.getByWorld(loc.getWorld()).getRegion(loc).getEventHandler();
	}

	public static EventHandler get(ServerWorld serverWorld, Location loc) {
		return serverWorld.getRegion(loc).getEventHandler();
	}

	public static EventHandler get(ServerPlayer serverPlayer) {
		return serverPlayer.getEventHandler();
	}

	public static @Nullable EventHandler get(Entity entity) {
		return ENTITY_MAP.get(entity);
	}

	public static EventHandler getOrDefault(Entity entity, EventHandler eventHandler) {
		return ENTITY_MAP.getOrDefault(entity, eventHandler);
	}

	public static EventHandler getOrEmpty(Entity entity) {
		return ENTITY_MAP.getOrDefault(entity, EventHandler.NULL);
	}

	public static EventHandler getDefault() {
		return defaultHandler;
	}

	public static void register(Supplier<EventHandler> supplier) {
		AiostRegistry.EVENT_HANDLER.register(supplier.get().getEventHandlerName(), supplier);
	}

	@SuppressWarnings("unchecked")
	public static Supplier<EventHandler>[] getRegionEventHandler() {
		return new Supplier[] { EventHandler.get("Lobby") };
	}
}
