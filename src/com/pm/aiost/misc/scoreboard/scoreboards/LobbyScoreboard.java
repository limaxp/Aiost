package com.pm.aiost.misc.scoreboard.scoreboards;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.GREEN;
import static com.pm.aiost.misc.utils.ChatColor.YELLOW;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Team;
import org.bukkit.scoreboard.Team.Option;
import org.bukkit.scoreboard.Team.OptionStatus;

import com.pm.aiost.Aiost;
import com.pm.aiost.misc.rank.Rank;
import com.pm.aiost.misc.rank.Ranks;
import com.pm.aiost.misc.scoreboard.Scoreboard;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.server.ServerManager;
import com.pm.aiost.server.request.ServerRequest;

public class LobbyScoreboard {

	private static Scoreboard scoreboard;
	private static Team[] rankTeams;
	private static Score playerSizeScore;

	static {
		scoreboard = initScoreboard();
		rankTeams = initRankTeams(scoreboard);
	}

	private static Scoreboard initScoreboard() {
		Scoreboard scoreboard = new Scoreboard();
		Objective sidebar = scoreboard.createSidebar("Sidebar", YELLOW + BOLD + "Lobby");
		Score[] scores = Scoreboard.setScores(sidebar, "",
				"Nr: " + GREEN + "#" + (ServerManager.getServer().getId() + 1), "Online: ", "§1",
				"World name: " + GREEN + Bukkit.getWorlds().get(0).getName(), "§2", YELLOW + Aiost.getUrl());
		playerSizeScore = scores[2];
		return scoreboard;
	}

	public static Team[] initRankTeams(Scoreboard scoreboard) {
		int size = Ranks.size();
		Team[] rankTeams = new Team[size];
		List<Rank> ranks = Ranks.values();
		for (int i = 0; i < size; i++)
			rankTeams[i] = createRankTeam(scoreboard, ranks.get(i));
		return rankTeams;
	}

	public static Team createRankTeam(Scoreboard scoreboard, Rank rank) {
		Team team = scoreboard.addTeam(rank.name);
		team.setPrefix(rank.prefix);
		team.setColor(ChatColor.values()[rank.color]);
		team.setOption(Option.DEATH_MESSAGE_VISIBILITY, OptionStatus.NEVER);
		team.setOption(Option.COLLISION_RULE, OptionStatus.NEVER);
		return team;
	}

	public static void addPlayer(ServerPlayer serverPlayer) {
		scoreboard.addPlayer(serverPlayer.player);
		addPlayerToTeam(serverPlayer);
		serverPlayer.player.setPlayerListHeaderFooter(YELLOW + BOLD + "Lobby", YELLOW + Aiost.getUrl());
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

	public static void updatePlayerSize() {
		playerSizeScore = Scoreboard.setScoreName(playerSizeScore,
				"Online: " + GREEN + ServerRequest.getHandler().getPlayerSize());
	}
}
