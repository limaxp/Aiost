package com.pm.aiost.server.request;

import java.io.File;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import org.bukkit.World;

import com.pm.aiost.game.GameType;
import com.pm.aiost.game.data.IGameData;
import com.pm.aiost.misc.menu.menus.DatabaseGameMenu.GameData;
import com.pm.aiost.player.ServerPlayer;

public interface ServerRequestHandler {

	public boolean saveWorld(UUID uuid, World world);

	public void loadWorld(UUID uuid, File dest);

	public boolean deleteWorld(UUID uuid);

	public List<? extends IGameData> getGames(GameType<?> type);

	public void hostGame(ServerPlayer serverPlayer, GameData game, int minPlayer, int maxPlayer,
			@Nullable String password);

	public void joinGame(ServerPlayer serverPlayer, IGameData game);

	public void sendFriendRequest(String name, ServerPlayer requestPlayer);

	public void sendLobby(ServerPlayer serverPlayer);

	public void sendLobby(List<ServerPlayer> serverPlayer);

	public int getPlayerSize();

	public void inviteParty(ServerPlayer serverPlayer, String playerName);

	public void joinParty(ServerPlayer serverPlayer, String requestPlayerName);

	public void leaveParty(ServerPlayer serverPlayer);

	public void requestPartyData(ServerPlayer serverPlayer);

	public void removeFromParty(ServerPlayer serverPlayer, UUID uuid);

	public void leavePartyOnQuit(ServerPlayer serverPlayer);

	public void invitePlayer(ServerPlayer serverPlayer, String playerName);

	public void sendToPlayer(ServerPlayer serverPlayer, String playerName);
}
