package com.pm.aiost.game.games.tntThrow;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;
import static com.pm.aiost.misc.utils.ChatColor.GREEN;
import static com.pm.aiost.misc.utils.ChatColor.YELLOW;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

import com.pm.aiost.Aiost;
import com.pm.aiost.game.Game;
import com.pm.aiost.misc.scoreboard.scoreboards.GameScoreboard;

public class TntThrowScoreboard extends GameScoreboard {

	protected int startPlayerSize;

	@Override
	public void init(Game game) {
		startPlayerSize = game.getPlayer().size();
	}

	@Override
	public void create(Game game) {
		Objective sidebar = createSidebar("Sidebar", YELLOW + BOLD + "Tnt throw");
		Score[] scores = setScores(sidebar, "", "ID: " + game.getUniqueId().toString().substring(0, 18),
				"Name: " + GREEN + game.getName(), "Author: " + GREEN + game.getAuthorName(), "§1", "Player: ",
				"Time: ", "§2", YELLOW + Aiost.getUrl());
		playerSizeScore = scores[5];
		timeScore = scores[6];
	}

	public void setPlayerSize(int playerSize) {
		playerSizeScore = setScoreName(playerSizeScore,
				"Player: " + GREEN + playerSize + GRAY + "/" + GREEN + startPlayerSize);
	}

	public void setTime(String time) {
		timeScore = setScoreName(timeScore, "Time: " + time);
	}

	@Override
	public void save(ConfigurationSection section) {
		section.set("startPlayerSize", startPlayerSize);
	}

	@Override
	public void load(ConfigurationSection section) {
		startPlayerSize = section.getInt("startPlayerSize");
	}
}