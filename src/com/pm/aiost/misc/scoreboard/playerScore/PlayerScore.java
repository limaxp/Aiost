package com.pm.aiost.misc.scoreboard.playerScore;

import org.bukkit.entity.Player;

import com.pm.aiost.misc.packet.PacketFactory;
import com.pm.aiost.misc.packet.PacketSender;

public class PlayerScore extends AbstractPlayerScore {

	protected String name;
	protected int score;

	public PlayerScore(Player player, String objectiveName, String name, int score) {
		super(player, objectiveName);
		this.name = name;
		setScore(score);
	}

	public void setScore(int score) {
		this.score = score;
		PacketSender.send(player, PacketFactory.packetScoreboardScoreChange(objectiveName, name, score));
	}

	public void setName(String name) {
		remove();
		PacketSender.send(player, PacketFactory.packetScoreboardScoreChange(objectiveName, name, score));
		this.name = name;
	}

	@Override
	public void remove() {
		PacketSender.send(player, PacketFactory.packetScoreboardScoreRemove(objectiveName, name));
	}

	public String getName() {
		return name;
	}

	public int getScore() {
		return score;
	}
}