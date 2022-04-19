package com.pm.aiost.game;

import static com.pm.aiost.misc.utils.ChatColor.DARK_RED;

import java.util.Iterator;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scoreboard.Team;

import com.pm.aiost.player.ServerPlayer;

public interface WinCondition {

	public static enum WinnerType {
		NONE, PLAYER, TEAM, CUSTOM;
	}

	public default boolean onTeamDefeat(Game game, GameTeam team) {
		return false;
	}

	public default boolean onPlayerDefeat(Game game, ServerPlayer serverPlayer, GameTeam team) {
		return false;
	}

	public default boolean onPlayerGetPoints(Game game, ServerPlayer serverPlayer, int points) {
		return false;
	}

	public default boolean onTeamGetPoints(Game game, GameTeam team, int points) {
		return false;
	}

	public WinnerType getWinnerType();

	public Object getWinner(Game game);

	public String getBroadcastMessage(Game game);

	public String getWinnerName(Game game);

	public default void load(ConfigurationSection section) {
	}

	public default void save(ConfigurationSection section) {
	}

	public static final WinCondition NONE = new WinCondition() {

		@Override
		public WinnerType getWinnerType() {
			return WinnerType.NONE;
		}

		@Override
		public Object getWinner(Game game) {
			return null;
		}

		@Override
		public String getBroadcastMessage(Game game) {
			return "";
		}

		@Override
		public String getWinnerName(Game game) {
			return "";
		}
	};

	public static final WinCondition LAST_TEAM = new WinCondition() {

		@Override
		public boolean onTeamDefeat(Game game, GameTeam team) {
			return game.getScoreboard().getTeams().size() < 3;
		}

		@Override
		public WinnerType getWinnerType() {
			return WinnerType.TEAM;
		}

		@Override
		public Team getWinner(Game game) {
			Iterator<Team> iter = game.getScoreboard().getTeams().iterator();
			iter.next();
			return iter.next();
		}

		@Override
		public String getBroadcastMessage(Game game) {
			return getWinner(game).getDisplayName() + " team has won!";
		}

		@Override
		public String getWinnerName(Game game) {
			return getWinner(game).getDisplayName();
		}
	};

	public static final WinCondition LAST_PLAYER = new WinCondition() {

		@Override
		public boolean onPlayerDefeat(Game game, ServerPlayer serverPlayer, GameTeam team) {
			return game.getPlayer().size() < 2;
		}

		@Override
		public WinnerType getWinnerType() {
			return WinnerType.PLAYER;
		}

		@Override
		public ServerPlayer getWinner(Game game) {
			return game.getPlayer().get(0);
		}

		@Override
		public String getBroadcastMessage(Game game) {
			return getWinner(game).name + " has won!";
		}

		@Override
		public String getWinnerName(Game game) {
			return getWinner(game).name;
		}
	};

	public static final WinCondition ALL_DEATH = new WinCondition() {

		@Override
		public boolean onPlayerDefeat(Game game, ServerPlayer serverPlayer, GameTeam team) {
			return game.getPlayer().size() < 1;
		}

		@Override
		public WinnerType getWinnerType() {
			return WinnerType.NONE;
		}

		@Override
		public Object getWinner(Game game) {
			return null;
		}

		@Override
		public String getBroadcastMessage(Game game) {
			return DARK_RED + "Game over!";
		}

		@Override
		public String getWinnerName(Game game) {
			return "";
		}
	};

	public static abstract class PlayerPointWinCondition implements WinCondition {

		private int points;
		private String winner;

		@Override
		public boolean onPlayerGetPoints(Game game, ServerPlayer serverPlayer, int points) {
			if (points >= this.points) {
				winner = serverPlayer.name;
				return true;
			}
			return false;
		}

		@Override
		public WinnerType getWinnerType() {
			return WinnerType.PLAYER;
		}

		@Override
		public String getWinner(Game game) {
			return winner;
		}

		@Override
		public String getBroadcastMessage(Game game) {
			return getWinner(game) + " has won!";
		}

		@Override
		public String getWinnerName(Game game) {
			return getWinner(game);
		}

		@Override
		public void load(ConfigurationSection section) {
			points = section.getInt("points");
			if (section.contains("winner"))
				winner = section.getString("winner");
		}

		@Override
		public void save(ConfigurationSection section) {
			section.set("points", points);
			if (winner != null)
				section.set("winner", winner);
		}

		public void setPoints(int points) {
			this.points = points;
		}

		public int getPoints() {
			return points;
		}
	};

	public static abstract class TeamPointWinCondition implements WinCondition {

		private int points;
		private String winner;

		@Override
		public boolean onTeamGetPoints(Game game, GameTeam team, int points) {
			if (points >= this.points) {
				winner = team.getName();
				return true;
			}
			return false;
		}

		@Override
		public WinnerType getWinnerType() {
			return WinnerType.TEAM;
		}

		@Override
		public String getWinner(Game game) {
			return winner;
		}

		@Override
		public String getBroadcastMessage(Game game) {
			return getWinner(game) + " team has won!";
		}

		@Override
		public String getWinnerName(Game game) {
			return getWinner(game);
		}

		@Override
		public void load(ConfigurationSection section) {
			points = section.getInt("points");
			if (section.contains("winner"))
				winner = section.getString("winner");
		}

		@Override
		public void save(ConfigurationSection section) {
			section.set("points", points);
			if (winner != null)
				section.set("winner", winner);
		}

		public void setPoints(int points) {
			this.points = points;
		}

		public int getPoints() {
			return points;
		}
	};
}
