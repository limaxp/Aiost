package com.pm.aiost.server.messaging;

import java.util.List;
import java.util.UUID;

import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.IntArrayList;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.IntList;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.pm.aiost.collection.list.FastArrayList;
import com.pm.aiost.game.GameType;
import com.pm.aiost.game.data.RemoteGameData;
import com.pm.aiost.misc.menu.menus.GameJoinMenu;
import com.pm.aiost.misc.menu.menus.LobbyMenu;
import com.pm.aiost.misc.registry.AiostRegistry;
import com.pm.aiost.server.Server;
import com.pm.aiost.server.ServerState;
import com.pm.aiost.server.ServerType;

public class ServerDataRequester {

	private static final IntList SERVER_TYPE_LIST = new IntArrayList();
	private static final IntList GAME_TYPE_LIST = new IntArrayList();

	public static void registerServerType(ServerType type) {
		SERVER_TYPE_LIST.add(type.index);
	}

	public static void unregisterServerType(ServerType type) {
		SERVER_TYPE_LIST.removeInt(SERVER_TYPE_LIST.indexOf(type.index));
	}

	public static void registerGameType(GameType<?> type) {
		GAME_TYPE_LIST.add(type.getId());
	}

	public static void unregisterGameType(GameType<?> type) {
		GAME_TYPE_LIST.removeInt(GAME_TYPE_LIST.indexOf(type.getId()));
	}

	public static void requestData() {
		int serverSize = SERVER_TYPE_LIST.size();
		int gameSize = GAME_TYPE_LIST.size();
		if (serverSize + gameSize == 0)
			return;

		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("requestData");
		out.writeInt(serverSize);
		for (int i = 0; i < serverSize; i++)
			out.writeInt(SERVER_TYPE_LIST.getInt(i));
		out.writeInt(gameSize);
		for (int i = 0; i < gameSize; i++)
			out.writeInt(GAME_TYPE_LIST.getInt(i));
		PluginMessage.send(out);
	}

	public static void recieveData(ByteArrayDataInput in) {
		ServerDataCache.playerSize = in.readInt();
		loadServerLists(in);
		loadGameLists(in);
		LobbyMenu.updateMenu();
		GameJoinMenu.updateMenus();
	}

	private static void loadServerLists(ByteArrayDataInput in) {
		int size = in.readInt();
		for (int i = 0; i < size; i++)
			loadServerList(in);
	}

	private static void loadServerList(ByteArrayDataInput in) {
		int type = in.readInt();
		ServerType serverType = ServerType.get(type);
		int size = in.readInt();
		List<Server> list = new FastArrayList<Server>(size);
		for (int i = 0; i < size; i++) {
			Server server = new Server(in.readInt(), in.readUTF(), serverType, ServerState.values()[in.readInt()]);
			list.add(server);
		}
		ServerDataCache.SERVER_TYPE_LISTS[type] = list;
	}

	private static void loadGameLists(ByteArrayDataInput in) {
		int size = in.readInt();
		for (int i = 0; i < size; i++)
			loadGameList(in);
	}

	private static void loadGameList(ByteArrayDataInput in) {
		int type = in.readInt();
		GameType<?> gameType = AiostRegistry.GAMES.get(type);
		int size = in.readInt();
		List<RemoteGameData> list = new FastArrayList<RemoteGameData>(size);
		for (int i = 0; i < size; i++) {
			RemoteGameData game = new RemoteGameData(in.readInt(), UUID.fromString(in.readUTF()), in.readUTF(),
					in.readUTF(), gameType, in.readInt(), in.readInt(), in.readInt(), in.readUTF());
			list.add(game);
		}
		ServerDataCache.GAME_TYPE_LISTS[type] = list;
	}
}
