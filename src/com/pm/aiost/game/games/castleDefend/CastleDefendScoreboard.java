package com.pm.aiost.game.games.castleDefend;

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
import com.pm.aiost.misc.scoreboard.ObjectiveType;
import com.pm.aiost.misc.scoreboard.scoreboards.GameScoreboard;

public class CastleDefendScoreboard extends GameScoreboard {

	protected int startPlayerSize;
	protected Score waveScore;
	protected Score enemySizeScore;
	protected Score killedScore;

	@Override
	public void init(Game game) {
		startPlayerSize = game.getPlayer().size();
	}

	@Override
	public void create(Game game) {
		Objective sidebar = createSidebar("Sidebar", YELLOW + BOLD + "Castle Defend");
		Score[] scores = setScores(sidebar, "", "ID: " + GREEN + game.getUniqueId().toString().substring(0, 18),
				"Name: " + GREEN + game.getName(), "Author: " + GREEN + game.getAuthorName(), "§1", "Player: ",
				"Time: ", "Wave: " + GREEN + ((CastleDefend) game).getWave(), "Enemies: ", "Killed: " + GREEN + "0",
				"§2", YELLOW + Aiost.getUrl());
		playerSizeScore = scores[5];
		timeScore = scores[6];
		waveScore = scores[7];
		enemySizeScore = scores[8];
		killedScore = scores[9];
		createBelowNameObjective(ObjectiveType.HEALTH, "Player Health", RED + BOLD + "❤");
	}

	public void setPlayerSize(int playerSize) {
		playerSizeScore = setScoreName(playerSizeScore,
				"Player: " + GREEN + playerSize + GRAY + "/" + GREEN + startPlayerSize);
	}

	public void setTime(String time) {
		timeScore = setScoreName(timeScore, "Time: " + time);
	}

	public void setWave(int wave) {
		waveScore = setScoreName(waveScore, "Wave: " + GREEN + wave);
	}

	public void setEnemySize(int enemySize) {
		enemySizeScore = setScoreName(enemySizeScore, "Enemies: " + GREEN + enemySize);
	}

	public void setKilled(int killed) {
		killedScore = setScoreName(killedScore, "Killed: " + GREEN + killed);
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