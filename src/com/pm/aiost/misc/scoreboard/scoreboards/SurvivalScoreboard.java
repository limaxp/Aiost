package com.pm.aiost.misc.scoreboard.scoreboards;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.RED;
import static com.pm.aiost.misc.utils.ChatColor.YELLOW;

import org.bukkit.scoreboard.Team;

import com.pm.aiost.Aiost;
import com.pm.aiost.misc.scoreboard.ObjectiveType;
import com.pm.aiost.misc.scoreboard.Scoreboard;
import com.pm.aiost.player.ServerPlayer;

public class SurvivalScoreboard {

	private static Scoreboard scoreboard;
	private static Team[] rankTeams;

	static {
		scoreboard = initScoreboard();
		rankTeams = LobbyScoreboard.initRankTeams(scoreboard);
	}

	private static Scoreboard initScoreboard() {
		Scoreboard scoreboard = new Scoreboard();
		scoreboard.createBelowNameObjective(ObjectiveType.HEALTH, "Player Health", RED + BOLD + "❤");
		return scoreboard;
	}

	public static void addPlayer(ServerPlayer serverPlayer) {
		scoreboard.addPlayer(serverPlayer.player);
		addPlayerToTeam(serverPlayer);
		serverPlayer.player.setPlayerListHeaderFooter(null, YELLOW + Aiost.getUrl());
	}

	public static void removePlayer(ServerPlayer serverPlayer) {
		scoreboard.removePlayer(serverPlayer.player);
		removePlayerFromTeam(serverPlayer);
	}

	private static void addPlayerToTeam(ServerPlayer serverPlayer) {
		rankTeams[serverPlayer.getRank().id].addEntry(serverPlayer.name);
	}

	private static void removePlayerFromTeam(ServerPlayer serverPlayer) {
		rankTeams[serverPlayer.getRank().id].removeEntry(serverPlayer.name);
	}

	public static Scoreboard getScoreboard() {
		return scoreboard;
	}
}
