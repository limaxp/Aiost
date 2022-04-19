package com.pm.aiost.game.games.soccer;

import static com.pm.aiost.misc.utils.ChatColor.BLUE;
import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;
import static com.pm.aiost.misc.utils.ChatColor.GREEN;
import static com.pm.aiost.misc.utils.ChatColor.RED;
import static com.pm.aiost.misc.utils.ChatColor.YELLOW;

import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

import com.pm.aiost.Aiost;
import com.pm.aiost.game.Game;
import com.pm.aiost.misc.scoreboard.scoreboards.GameScoreboard;

public class SoccerScoreboard extends GameScoreboard {

	protected Score pointsScore;

	@Override
	public void init(Game game) {
	}

	@Override
	public void create(Game game) {
		Objective sidebar = createSidebar("Sidebar", YELLOW + BOLD + "Soccer");
		Score[] scores = setScores(sidebar, "", "ID: " + game.getUniqueId().toString().substring(0, 18),
				"Name: " + GREEN + game.getName(), "Author: " + GREEN + game.getAuthorName(), "§1",
				BLUE + '0' + GRAY + " : " + RED + '0', "Time: ", "§2", YELLOW + Aiost.getUrl());
		pointsScore = scores[5];
		timeScore = scores[6];
	}

	public void setTime(String time) {
		timeScore = setScoreName(timeScore, "Time: " + time);
	}

	public void setPoints(int blue, int red) {
		pointsScore = setScoreName(pointsScore, BLUE + blue + GRAY + " : " + RED + red);
	}
}