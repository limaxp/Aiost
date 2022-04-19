package com.pm.aiost.misc.scoreboard.scoreboards;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;
import static com.pm.aiost.misc.utils.ChatColor.GREEN;
import static com.pm.aiost.misc.utils.ChatColor.RED;
import static com.pm.aiost.misc.utils.ChatColor.YELLOW;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

import com.pm.aiost.Aiost;
import com.pm.aiost.game.Game;
import com.pm.aiost.misc.scoreboard.Scoreboard;

public abstract class GameScoreboard extends Scoreboard {

	protected Score playerSizeScore;
	protected Score timeScore;

	public void init(Game game) {
	}

	public abstract void create(Game game);

	public void createLobbySidebar(Game game) {
		Objective sidebar = createSidebar("Sidebar", YELLOW + BOLD + "Game lobby");
		Score[] scores = setScores(sidebar, "", "Game: " + GREEN + game.getType().name,
				"ID: " + GREEN + game.getUniqueId().toString().substring(0, 18), "Name: " + GREEN + game.getName(),
				"Author: " + GREEN + game.getAuthorName(), "§1", "Player: ", "Starts in: ", "§2",
				YELLOW + Aiost.getUrl());
		playerSizeScore = scores[6];
		timeScore = scores[7];
	}

	public void setLobbyPlayerSize(int playerSize, int maxPlayer) {
		playerSizeScore = setScoreName(playerSizeScore,
				"Player: " + (playerSize >= maxPlayer ? GREEN : RED) + playerSize + GRAY + "/" + GREEN + maxPlayer);
	}

	public void setLobbyTime(String time) {
		timeScore = setScoreName(timeScore, "Starts in: " + time);
	}

	public void save(ConfigurationSection section) {
	}

	public void load(ConfigurationSection section) {
	}
}