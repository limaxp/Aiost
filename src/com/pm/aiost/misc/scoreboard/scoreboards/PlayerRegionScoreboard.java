package com.pm.aiost.misc.scoreboard.scoreboards;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.GREEN;
import static com.pm.aiost.misc.utils.ChatColor.YELLOW;

import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Team;

import com.pm.aiost.Aiost;
import com.pm.aiost.misc.scoreboard.Scoreboard;

public class PlayerRegionScoreboard extends Scoreboard {

	private Team builderTeam;
	private Team guestTeam;
	protected Score playerSizeScore;

	public PlayerRegionScoreboard(String ownerName, String regionName) {
		initScoreboard(ownerName, regionName);
		initTeams();
	}

	protected void initScoreboard(String ownerName, String regionName) {
		Objective sidebar = createSidebar("Sidebar", YELLOW + BOLD + "Region");
		Score[] scores = setScores(sidebar, "", "Region name: " + GREEN + regionName, "Owner: " + GREEN + ownerName,
				"§1", "Player: ", "§2", YELLOW + Aiost.getUrl());
		playerSizeScore = scores[4];
	}

	private void initTeams() {
		guestTeam = addTeam("Guest");
		guestTeam.setPrefix("[Guest] ");
		guestTeam.setColor(ChatColor.GRAY);

		builderTeam = addTeam("Builder");
		builderTeam.setPrefix("[Builder] ");
		builderTeam.setColor(ChatColor.BLUE);
	}

	public Team getBuilderTeam() {
		return builderTeam;
	}

	public Team getGuestTeam() {
		return guestTeam;
	}

	public void setPlayerSize(int amount) {
		playerSizeScore = setScoreName(playerSizeScore, "Player: " + GREEN + amount);
	}
}
