package com.pm.aiost.server.request;

import static com.pm.aiost.misc.utils.ChatColor.RED;

import java.io.File;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.pm.aiost.game.GameLobby;
import com.pm.aiost.game.GameManager;
import com.pm.aiost.game.GameType;
import com.pm.aiost.game.data.IGameData;
import com.pm.aiost.misc.command.commands.PlayerCommands.InvitePlayerCommand;
import com.pm.aiost.misc.menu.menus.DatabaseGameMenu.GameData;
import com.pm.aiost.player.Party;
import com.pm.aiost.player.PartyManager;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.player.handler.FriendHandler;
import com.pm.aiost.server.world.creation.WorldFileLoader;
import com.pm.aiost.server.world.creation.WorldLoader;

public class StandaloneServerRequestHandler implements ServerRequestHandler {

	@Override
	public boolean saveWorld(UUID uuid, World world) {
		return WorldFileLoader.saveWorld(uuid, world);
	}

	@Override
	public void loadWorld(UUID uuid, File dest) {
		WorldFileLoader.loadWorld(uuid.toString(), dest);
	}

	@Override
	public boolean deleteWorld(UUID uuid) {
		return WorldFileLoader.deleteWorld(uuid);
	}

	@Override
	public List<? extends IGameData> getGames(GameType<?> type) {
		return GameManager.getGames(type);
	}

	@Override
	public void hostGame(ServerPlayer serverPlayer, GameData game, int minPlayer, int maxPlayer,
			@Nullable String password) {
		GameLobby gameLobby = WorldLoader.loadGame(serverPlayer, game, minPlayer, maxPlayer, password);
		if (gameLobby != null)
			joinGame(serverPlayer, gameLobby.getGame());
	}

	@Override
	public void joinGame(ServerPlayer serverPlayer, IGameData game) {
		Location loc = game.getRegion().getSpawnLocation();
		Party party;
		if (serverPlayer.hasLocalParty() && (party = serverPlayer.getLocalParty()).isOwner(serverPlayer)) {
			for (ServerPlayer partyPlayer : party.getMember()) {
				if (!partyPlayer.playsGame())
					partyPlayer.player.teleport(loc);
			}
		} else
			serverPlayer.player.teleport(loc);
	}

	@Override
	public void sendFriendRequest(String name, ServerPlayer requestPlayer) {
		Player player = Bukkit.getPlayer(name);
		if (player != null)
			FriendHandler.sendRequestMessage(player, requestPlayer.name);
	}

	@Override
	public void sendLobby(ServerPlayer serverPlayer) {
		serverPlayer.player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
	}

	@Override
	public void sendLobby(List<ServerPlayer> serverPlayer) {
		Location spawnLocation = Bukkit.getWorlds().get(0).getSpawnLocation();
		for (int i = serverPlayer.size() - 1; i >= 0; i--)
			serverPlayer.get(i).player.teleport(spawnLocation);
	}

	@Override
	public int getPlayerSize() {
		return Bukkit.getOnlinePlayers().size();
	}

	@Override
	public void inviteParty(ServerPlayer serverPlayer, String playerName) {
		PartyManager.inviteParty(serverPlayer, playerName);
	}

	@Override
	public void joinParty(ServerPlayer serverPlayer, String requestPlayerName) {
		PartyManager.joinParty(serverPlayer, requestPlayerName);
	}

	@Override
	public void leaveParty(ServerPlayer serverPlayer) {
		PartyManager.leaveParty(serverPlayer);
	}

	@Override
	public void requestPartyData(ServerPlayer serverPlayer) {
		PartyManager.requestPartyData(serverPlayer);
	}

	@Override
	public void removeFromParty(ServerPlayer serverPlayer, UUID uuid) {
		PartyManager.removeFromParty(serverPlayer, uuid);
	}

	@Override
	public void leavePartyOnQuit(ServerPlayer serverPlayer) {
		serverPlayer.leaveLocalParty();
	}

	@Override
	public void invitePlayer(ServerPlayer serverPlayer, String playerName) {
		ServerPlayer targetServerPlayer = ServerPlayer.getByName(playerName);
		if (targetServerPlayer == null) {
			serverPlayer.player.sendMessage(RED + "No player found for name '" + playerName + "'");
			return;
		}
		InvitePlayerCommand.sendPlayerInviteMessage(targetServerPlayer, serverPlayer.name);
	}

	@Override
	public void sendToPlayer(ServerPlayer serverPlayer, String playerName) {
		Player player = Bukkit.getPlayer(playerName);
		if (player == null) {
			serverPlayer.player.sendMessage(RED + "No player found for name '" + playerName + "'");
			return;
		}
		serverPlayer.player.teleport(player.getLocation());
	}
}
