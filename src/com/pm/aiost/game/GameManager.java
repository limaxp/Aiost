package com.pm.aiost.game;

import java.util.List;
import java.util.Random;

import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

import com.pm.aiost.collection.list.UnorderedIdentityArrayList;
import com.pm.aiost.misc.registry.AiostRegistry;

@SuppressWarnings("unchecked")
public final class GameManager {

	private static final List<Game>[] GAME_LISTS;

	private static final Long2ObjectMap<Game> ID_MAP = new Long2ObjectOpenHashMap<Game>();

	static {
		int size = AiostRegistry.GAMES.size();
		GAME_LISTS = new List[size];
		for (int i = 0; i < size; i++)
			GAME_LISTS[i] = new UnorderedIdentityArrayList<Game>();
	}

	static synchronized void addGame(Game game) {
		GameType<?> type = game.getType();
		GAME_LISTS[type.index].add(game);
		int id = game.getId();
		if (id == 0)
			game.setId(id = generateId(type.getId()));
		ID_MAP.put(getId(type.getId(), id), game);
	}

	static synchronized void removeGame(Game game) {
		GameType<?> type = game.getType();
		GAME_LISTS[type.index].remove(game);
		ID_MAP.remove(getId(type.getId(), game.getId()));
	}

	public static List<Game> getGames(GameType<?> type) {
		return GAME_LISTS[type.index];
	}

	public static Game getGame(int type, int id) {
		return ID_MAP.get(getId(type, id));
	}

	private static long getId(int type, int id) {
		return (((long) type) << 32) | (id & 0xffffffffL);
	}

	private static int generateId(int type) {
		int id;
		do {
			id = new Random().nextInt();
		} while (ID_MAP.containsKey(getId(type, id)));
		return id;
	}
}
