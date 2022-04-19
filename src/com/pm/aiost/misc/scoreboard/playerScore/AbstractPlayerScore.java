package com.pm.aiost.misc.scoreboard.playerScore;

import org.bukkit.entity.Player;

public abstract class AbstractPlayerScore {

	protected Player player;
	protected String objectiveName;

	public AbstractPlayerScore(Player player, String objectiveName) {
		this.player = player;
		this.objectiveName = objectiveName;
	}

	public abstract void remove();

	public String getObjectiveName() {
		return objectiveName;
	}
}