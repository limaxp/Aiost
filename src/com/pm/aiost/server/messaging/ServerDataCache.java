package com.pm.aiost.server.messaging;

import java.util.List;

import com.pm.aiost.collection.list.FastArrayList;
import com.pm.aiost.game.GameType;
import com.pm.aiost.game.data.RemoteGameData;
import com.pm.aiost.misc.registry.AiostRegistry;
import com.pm.aiost.server.Server;
import com.pm.aiost.server.ServerType;

@SuppressWarnings("unchecked")
public class ServerDataCache {

	private static final int START_SIZE = 10;

	static int playerSize;

	static final List<Server>[] SERVER_TYPE_LISTS;

	static final List<RemoteGameData>[] GAME_TYPE_LISTS;

	static {
		int serverTypeSize = ServerType.size();
		SERVER_TYPE_LISTS = new List[serverTypeSize];
		for (int i = 0; i < serverTypeSize; i++)
			SERVER_TYPE_LISTS[i] = new FastArrayList<Server>(START_SIZE);

		int gameTypeSize = AiostRegistry.GAMES.size();
		GAME_TYPE_LISTS = new List[gameTypeSize];
		for (int i = 0; i < gameTypeSize; i++)
			GAME_TYPE_LISTS[i] = new FastArrayList<RemoteGameData>(START_SIZE);
	}

	public static int getPlayerSize() {
		return playerSize;
	}

	public static Server getServer(ServerType type, int id) {
		return SERVER_TYPE_LISTS[type.index].get(id);
	}

	public static List<Server> getServerList(ServerType type) {
		return SERVER_TYPE_LISTS[type.index];
	}

	public static RemoteGameData getGame(GameType<?> type, int id) {
		return GAME_TYPE_LISTS[type.index].get(id);
	}

	public static List<RemoteGameData> getGameList(GameType<?> type) {
		return GAME_TYPE_LISTS[type.index];
	}
}
