package com.pm.aiost.server.request;

import java.io.File;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import org.bukkit.World;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.pm.aiost.game.GameType;
import com.pm.aiost.game.data.IGameData;
import com.pm.aiost.misc.menu.menus.DatabaseGameMenu.GameData;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.server.ServerType;
import com.pm.aiost.server.messaging.PluginMessage;
import com.pm.aiost.server.messaging.ServerDataCache;
import com.pm.aiost.server.world.creation.WorldFileLoader;

public class BungeecordServerRequestHandler implements ServerRequestHandler {

	@Override
	public boolean saveWorld(UUID uuid, World world) {
		// TODO
		return WorldFileLoader.saveWorld(uuid, world);
	}

	@Override
	public void loadWorld(UUID uuid, File dest) {
		// TODO
		WorldFileLoader.loadWorld(uuid.toString(), dest);
	}

	@Override
	public boolean deleteWorld(UUID uuid) {
		// TODO
		return WorldFileLoader.deleteWorld(uuid);
	}

	@Override
	public List<? extends IGameData> getGames(GameType<?> type) {
		return ServerDataCache.getGameList(type);
	}

	@Override
	public void hostGame(ServerPlayer serverPlayer, GameData game, int minPlayer, int maxPlayer,
			@Nullable String password) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("hostGame");
		out.writeInt(serverPlayer.getBungeeID());
		out.writeUTF(game.uuid.toString());
		out.writeUTF(game.name);
		out.writeUTF(game.authorName);
		out.writeInt(game.gameType.getId());
		out.writeInt(game.environment.ordinal());
		out.writeInt(game.worldType.id);
		out.writeBoolean(game.generateStructures);
		out.writeInt(minPlayer);
		out.writeInt(maxPlayer);
		out.writeUTF(password);
		PluginMessage.send(serverPlayer.player, out);
	}

	@Override
	public void joinGame(ServerPlayer serverPlayer, IGameData game) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("joinGame");
		out.writeInt(serverPlayer.getBungeeID());
		out.writeInt(game.getType().getId());
		out.writeInt(game.getId());
		PluginMessage.send(serverPlayer.player, out);
	}

	@Override
	public void sendFriendRequest(String name, ServerPlayer requestPlayer) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF(requestPlayer.name);
		PluginMessage.sendForwardToPlayer(requestPlayer.player, name, "sendFriendRequest", out);
	}

	@Override
	public void sendLobby(ServerPlayer serverPlayer) {
		PluginMessage.connectOpen(serverPlayer, ServerType.LOBBY);
	}

	@Override
	public void sendLobby(List<ServerPlayer> serverPlayer) {
		PluginMessage.connectAllOpen(serverPlayer, ServerType.LOBBY);
	}

	@Override
	public int getPlayerSize() {
		return ServerDataCache.getPlayerSize();
	}

	@Override
	public void inviteParty(ServerPlayer serverPlayer, String playerName) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("inviteParty");
		out.writeInt(serverPlayer.getBungeeID());
		out.writeUTF(playerName);
		PluginMessage.send(serverPlayer.player, out);
	}

	@Override
	public void joinParty(ServerPlayer serverPlayer, String requestPlayerName) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("joinParty");
		out.writeInt(serverPlayer.getBungeeID());
		out.writeUTF(requestPlayerName);
		PluginMessage.send(serverPlayer.player, out);
	}

	@Override
	public void leaveParty(ServerPlayer serverPlayer) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("leaveParty");
		out.writeInt(serverPlayer.getBungeeID());
		PluginMessage.send(serverPlayer.player, out);
	}

	@Override
	public void requestPartyData(ServerPlayer serverPlayer) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("requestPartyData");
		out.writeInt(serverPlayer.getBungeeID());
		PluginMessage.send(serverPlayer.player, out);
	}

	@Override
	public void removeFromParty(ServerPlayer serverPlayer, UUID uuid) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("removeFromParty");
		out.writeInt(serverPlayer.getBungeeID());
		out.writeUTF(uuid.toString());
		PluginMessage.send(serverPlayer.player, out);
	}

	@Override
	public void leavePartyOnQuit(ServerPlayer serverPlayer) {
		// DOES NOTHING!
	}

	@Override
	public void invitePlayer(ServerPlayer serverPlayer, String playerName) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF(serverPlayer.name);
		PluginMessage.sendForwardToPlayer(serverPlayer.player, playerName, "invitePlayer", out);
	}

	@Override
	public void sendToPlayer(ServerPlayer serverPlayer, String playerName) {
		PluginMessage.connectPerPlayer(serverPlayer, playerName);
	}
}
