package com.pm.aiost.misc.scoreboard.scoreboards;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.GREEN;
import static com.pm.aiost.misc.utils.ChatColor.YELLOW;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

import com.pm.aiost.Aiost;

public class PlayerWorldScoreboard extends PlayerRegionScoreboard {

	public PlayerWorldScoreboard(String ownerName, String worldName) {
		super(ownerName, worldName);
	}

	@Override
	protected void initScoreboard(String ownerName, String worldName) {
		Objective sidebar = createSidebar("Sidebar", YELLOW + BOLD + "World creator");
		Score[] scores = setScores(sidebar, "", "World name: " + GREEN + worldName, "Owner: " + GREEN + ownerName, "§1",
				"Player: ", "§2", YELLOW + Aiost.getUrl());
		playerSizeScore = scores[4];
	}

	@Override
	public void addPlayer(Player player) {
		super.addPlayer(player);
		player.setPlayerListHeaderFooter(YELLOW + BOLD + "World creator", YELLOW + Aiost.getUrl());
	}
}