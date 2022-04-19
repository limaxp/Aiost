package com.pm.aiost.misc.scoreboard;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_15_R1.scoreboard.CraftScoreboard;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.RenderType;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import com.pm.aiost.misc.packet.PacketFactory;
import com.pm.aiost.misc.packet.PacketScoreboardDisplaySlot;
import com.pm.aiost.misc.packet.PacketScoreboardObjectiveAction;
import com.pm.aiost.misc.packet.PacketSender;
import com.pm.aiost.misc.scoreboard.playerScore.PlayerScore;
import com.pm.aiost.misc.scoreboard.playerScore.PlayerScores;
import com.pm.aiost.player.ServerPlayer;

import net.minecraft.server.v1_15_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_15_R1.IScoreboardCriteria;
import net.minecraft.server.v1_15_R1.IScoreboardCriteria.EnumScoreboardHealthDisplay;
import net.minecraft.server.v1_15_R1.ScoreboardObjective;
import net.minecraft.server.v1_15_R1.ScoreboardTeam;

public class Scoreboard {

	public static final int MAX_SCORES = 16;
	private static final ScoreboardManager SCOREBOARD_MANAGER;
	private static final org.bukkit.scoreboard.Scoreboard MAIN_SCOREBOARD;

	static {
		SCOREBOARD_MANAGER = Bukkit.getScoreboardManager();
		MAIN_SCOREBOARD = SCOREBOARD_MANAGER.getMainScoreboard();
	}

	public final org.bukkit.scoreboard.Scoreboard scoreboard;
	public final net.minecraft.server.v1_15_R1.Scoreboard nmsScoreboard;

	public Scoreboard() {
		scoreboard = SCOREBOARD_MANAGER.getNewScoreboard();
		nmsScoreboard = ((CraftScoreboard) scoreboard).getHandle();
	}

	public Objective createObjective(String type, String name, String displayName) {
		return scoreboard.registerNewObjective(name, type, displayName);
	}

	public Objective createObjective(String type, String name, String displayName, RenderType renderType) {
		return scoreboard.registerNewObjective(name, type, displayName, renderType);
	}

	public Objective createSidebar(String name, String displayName) {
		Objective objective = scoreboard.registerNewObjective(name, ObjectiveType.DUMMY, displayName);
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		return objective;
	}

	public void removeSidebar() {
		scoreboard.getObjective(DisplaySlot.SIDEBAR).unregister();
	}

	public Objective createTeamSidebar(String name, String displayName, Team team) {
		return createTeamSidebar(name, displayName, team.getColor().ordinal());
	}

	public Objective createTeamSidebar(String name, String displayName, ChatColor color) {
		return createTeamSidebar(name, displayName, color.ordinal());
	}

	public Objective createTeamSidebar(String name, String displayName, int color) {
		ScoreboardObjective objective = nmsScoreboard.registerObjective(name,
				new IScoreboardCriteria(ObjectiveType.DUMMY), ChatSerializer.a("{\"text\": \"" + displayName + "\"}"),
				EnumScoreboardHealthDisplay.INTEGER);
		nmsScoreboard.setDisplaySlot(3 + color, objective);
		return scoreboard.getObjective(name);
	}

	public void removeTeamSidebar(Team team) {
		removeTeamSidebar(team.getColor().ordinal());
	}

	public void removeTeamSidebar(ChatColor color) {
		removeTeamSidebar(color.ordinal());
	}

	public void removeTeamSidebar(int color) {
		nmsScoreboard.unregisterObjective(nmsScoreboard.getObjectiveForSlot(3 + color));
	}

	public void removeScore(Score score) {
		scoreboard.resetScores(score.getEntry());
	}

	public void removeScore(String score) {
		scoreboard.resetScores(score);
	}

	public void removeScore(String... scores) {
		for (String score : scores)
			scoreboard.resetScores(score);
	}

	public void createPlayerSidebar(Player player, String name, String displayName) {
		PacketSender.send(player,
				PacketFactory.packetScoreboardObjective(name, ChatSerializer.a("{\"text\": \"" + displayName + "\"}"),
						EnumScoreboardHealthDisplay.INTEGER, PacketScoreboardObjectiveAction.CREATE));

		PacketSender.send(player,
				PacketFactory.packetScoreboardDisplayObjective(PacketScoreboardDisplaySlot.SIDEBAR, name));
	}

	public void removePlayerSidebar(Player player, String name, String displayName) {
		PacketSender.send(player,
				PacketFactory.packetScoreboardObjective(name, ChatSerializer.a("{\"text\": \"" + displayName + "\"}"),
						EnumScoreboardHealthDisplay.INTEGER, PacketScoreboardObjectiveAction.REMOVE));
	}

	public PlayerScore addPlayerScore(Player player, String objectiveName, String scoreName, int score) {
		return new PlayerScore(player, objectiveName, scoreName, score);
	}

	public PlayerScores addPlayerScores(Player player, String objectiveName, String... scores) {
		return new PlayerScores(player, objectiveName, scores);
	}

	public void removePlayerSidebarScore(PlayerScore score) {
		score.remove();
	}

	public Objective createBelowNameObjective(String type, String name, String displayName) {
		Objective objective = scoreboard.registerNewObjective(name, type, displayName);
		objective.setDisplaySlot(DisplaySlot.BELOW_NAME);
		return objective;
	}

	public void removeBelowNameObjective() {
		scoreboard.getObjective(DisplaySlot.BELOW_NAME).unregister();
	}

	public void addPlayer(Player player) {
		player.setScoreboard(scoreboard);
	}

	public void removePlayer(ServerPlayer serverPlayer) {
		serverPlayer.player.setScoreboard(MAIN_SCOREBOARD);
	}

	public void removePlayer(Player player) {
		player.setScoreboard(MAIN_SCOREBOARD);
	}

	public Objective getSidebar() {
		return scoreboard.getObjective(DisplaySlot.SIDEBAR);
	}

	public Objective getBelowNameObjective() {
		return scoreboard.getObjective(DisplaySlot.BELOW_NAME);
	}

	public Team addTeam(String name) {
		return scoreboard.registerNewTeam(name);
	}

	public void removeTeam(String name) {
		scoreboard.getTeam(name).unregister();
	}

	public void removeTeam(Team team) {
		team.unregister();
	}

	public Team getTeam(String name) {
		return scoreboard.getTeam(name);
	}

	public Set<Team> getTeams() {
		return scoreboard.getTeams();
	}

	public Team getEntryTeam(String name) {
		return scoreboard.getEntryTeam(name);
	}

	public ScoreboardTeam getNMSTeam(String name) {
		return nmsScoreboard.getTeam(name);
	}

	public static Score setScore(Objective objective, String name, int value) {
		Score score = objective.getScore(name);
		score.setScore(value);
		return score;
	}

	public static Score setScoreName(Score score, String name) {
		Objective objective = score.getObjective();
		int value = score.getScore();
		score.getScoreboard().resetScores(score.getEntry());
		return setScore(objective, name, value);
	}

	public static Score setScoreName(Objective objective, String oldName, String name) {
		Score score = objective.getScore(oldName);
		int value = score.getScore();
		objective.getScoreboard().resetScores(oldName);
		return setScore(objective, name, value);
	}

	public static Score setScoreName(Objective objective, String oldName, String name, int value) {
		objective.getScoreboard().resetScores(oldName);
		return setScore(objective, name, value);
	}

	public static Score[] setScores(Objective objective, String... scores) {
		int length = scores.length;
		Score[] scoreArray = new Score[length--];
		for (int i = 0; i <= length; i++) {
			Score score = objective.getScore(scores[i]);
			score.setScore(length - i);
			scoreArray[i] = score;
		}
		return scoreArray;
	}

	public static void removeFromTeam(Player player) {
		String name = player.getName();
		player.getScoreboard().getEntryTeam(name).removeEntry(name);
	}
}