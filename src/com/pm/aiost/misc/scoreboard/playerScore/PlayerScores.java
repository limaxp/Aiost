package com.pm.aiost.misc.scoreboard.playerScore;

import org.bukkit.entity.Player;

import com.pm.aiost.misc.packet.PacketFactory;
import com.pm.aiost.misc.packet.PacketSender;

public class PlayerScores extends AbstractPlayerScore {

	protected String[] names;

	public PlayerScores(Player player, String objectiveName, String[] names) {
		super(player, objectiveName);
		setNames(names);
	}

	private void setNames(String[] names) {
		this.names = names;
		int i = names.length - 1;
		for (String name : names)
			PacketSender.send(player, PacketFactory.packetScoreboardScoreChange(objectiveName, name, i--));
	}

	public void setName(int score, String name) {
		remove(score);
		PacketSender.send(player, PacketFactory.packetScoreboardScoreChange(objectiveName, name, score));
		names[score] = name;
	}

	public void remove(int score) {
		PacketSender.send(player, PacketFactory.packetScoreboardScoreRemove(objectiveName, names[score]));
	}

	@Override
	public void remove() {
		for (String name : names)
			PacketSender.send(player, PacketFactory.packetScoreboardScoreRemove(objectiveName, name));
	}

	public String[] getNames() {
		return names;
	}

	public String getName(int index) {
		return names[index];
	}
}