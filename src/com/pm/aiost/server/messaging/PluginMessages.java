package com.pm.aiost.server.messaging;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.google.common.io.ByteArrayDataInput;
import com.pm.aiost.game.GameManager;
import com.pm.aiost.misc.command.commands.PlayerCommands.InvitePlayerCommand;
import com.pm.aiost.player.Party;
import com.pm.aiost.player.PartyManager;
import com.pm.aiost.player.PlayerDataLoader;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.player.handler.FriendHandler;
import com.pm.aiost.server.ServerManager;
import com.pm.aiost.server.ServerType;
import com.pm.aiost.server.world.creation.WorldLoader;

public class PluginMessages {

	public static final BiConsumer<Player, ByteArrayDataInput> NULL_CALLBACK = new BiConsumer<Player, ByteArrayDataInput>() {
		@Override
		public void accept(Player arg0, ByteArrayDataInput arg1) {
		}
	};

	private static final Map<String, BiConsumer<Player, ByteArrayDataInput>> CALLBACKS = new HashMap<String, BiConsumer<Player, ByteArrayDataInput>>();

	static {
		register("reload", (player, in) -> Bukkit.reload());

		register("stop", (player, in) -> Bukkit.shutdown());

		register("serverType", (player, in) -> ServerManager.setServerType(ServerType.get(in.readInt())));

		register("recievePlayerData", (player, in) -> PlayerDataLoader.recieveBungeePlayerData(in));

		register("recieveData", (player, in) -> ServerDataRequester.recieveData(in));

		register("sendFriendRequest", (player, in) -> FriendHandler.sendRequestMessage(player, in.readUTF()));

		register("startGame", (player, in) -> WorldLoader.loadGame(in));

		register("joinGame", (player, in) -> {
			ServerPlayer serverPlayer = ServerPlayer.getByBungeeID(in.readInt());
			if (serverPlayer != null)
				serverPlayer.player
						.teleport(GameManager.getGame(in.readInt(), in.readInt()).getRegion().getSpawnLocation());
		});

		register("partyRequest", (player, in) -> {
			ServerPlayer serverPlayer = ServerPlayer.getByBungeeID(in.readInt());
			if (serverPlayer != null)
				Party.sendRequest(serverPlayer, in.readUTF());
		});

		register("recievePartyData", (player, in) -> {
			ServerPlayer serverPlayer = ServerPlayer.getByBungeeID(in.readInt());
			if (serverPlayer != null)
				PartyManager.recievePartyRequest(serverPlayer, in);
		});

		register("invitePlayer", (player, in) -> InvitePlayerCommand
				.sendPlayerInviteMessage(ServerPlayer.getByPlayer(player), in.readUTF()));
	}

	public static void register(String cmd, BiConsumer<Player, ByteArrayDataInput> callback) {
		CALLBACKS.put(cmd, callback);
	}

	public static void unregister(String cmd) {
		CALLBACKS.remove(cmd);
	}

	public static BiConsumer<Player, ByteArrayDataInput> get(String cmd) {
		return CALLBACKS.getOrDefault(cmd, NULL_CALLBACK);
	}

	public static BiConsumer<Player, ByteArrayDataInput> getOrDefault(String cmd,
			BiConsumer<Player, ByteArrayDataInput> defaultValue) {
		return CALLBACKS.getOrDefault(cmd, defaultValue);
	}
}
