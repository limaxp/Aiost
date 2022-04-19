package com.pm.aiost.game;

import java.util.UUID;

import javax.annotation.Nullable;

import org.bukkit.configuration.ConfigurationSection;

import com.pm.aiost.player.ServerPlayer;

public class GamePlayer {

	public static final GamePlayer NULL_GAME_PLAYER = new GamePlayer();

	public final Game game;
	private ServerPlayer serverPlayer;
	private UUID uuid;
	private long databaseID;
	private GameKit kit;
	private int kitIndex;
	private int lives;
	private int rateValue;
	private GameTeam team;
	private int points;

	private GamePlayer() {
		this.game = null;
		kit = GameKit.EMPTY;
		this.lives = 0;
	}

	public GamePlayer(Game game) {
		this.game = game;
		kit = GameKit.EMPTY;
		this.lives = game.getStartLives();
	}

	final void setServerPlayer(ServerPlayer serverPlayer) {
		this.serverPlayer = serverPlayer;
		uuid = serverPlayer.player.getUniqueId();
		databaseID = serverPlayer.getDatabaseID();
	}

	@Nullable
	public final ServerPlayer getServerPlayer() {
		return serverPlayer;
	}

	final void setUniqueId(UUID uuid) {
		this.uuid = uuid;
	}

	public UUID getUniqueId() {
		return uuid;
	}

	public long getDatabaseID() {
		return databaseID;
	}

	public final void setTeam(@Nullable GameTeam team) {
		if (this.team != null)
			this.team.getTeam().removeEntry(serverPlayer.name);
		if (team != null)
			team.getTeam().addEntry(serverPlayer.name);
		this.team = team;
	}

	public final GameTeam getTeam() {
		return team;
	}

	private final void setKit(int index) {
		this.kit = game.getKits()[index];
		kitIndex = index;
	}

	public final void equipDefaultKit() {
		equipKit(0);
	}

	public final void equipKit(int index) {
		kit.unequip(serverPlayer);
		setKit(index);
		kit.equip(serverPlayer);
	}

	public final void unequipKit() {
		kit.unequip(serverPlayer);
	}

	public final void applyKit() {
		kit.apply(serverPlayer);
	}

	public final void deapplyKit() {
		kit.deapply(serverPlayer);
	}

	public final boolean hasKit(int index) {
		return kitIndex == index;
	}

	public final boolean hasKit(GameKit kit) {
		return this.kit == kit;
	}

	public final GameKit getKit() {
		return kit;
	}

	public final int getKitIndex() {
		return kitIndex;
	}

	public final int addLive() {
		return addLives(1);
	}

	public final int addLives(int lives) {
		return setLives(this.lives + lives);
	}

	public final int removeLive() {
		return removeLives(1);
	}

	public final int removeLives(int lives) {
		return setLives(this.lives - lives);
	}

	public int setLives(int lives) {
		return this.lives = lives;
	}

	public final int getLives() {
		return lives;
	}

	public final boolean isAlive() {
		return lives > 0;
	}

	public final boolean isDeath() {
		return lives < 1;
	}

	public final void setRateValue(int rateValue) {
		this.rateValue = rateValue;
	}

	public final int getRateValue() {
		return rateValue;
	}

	public boolean hasRate() {
		return this.rateValue != 0;
	}

	public final int addPoints(int points) {
		this.points += points;
		if (game.getWinCondition().onPlayerGetPoints(game, serverPlayer, this.points))
			game.end();
		return this.points;
	}

	public final int removePoints(int points) {
		return this.points -= points;
	}

	public final int getPoints() {
		return points;
	}

	public int getCreditsEarned() {
		return points / 1000;
	}

	public boolean isHost() {
		return game.isHost(serverPlayer);
	}

	public void load(ConfigurationSection section) {
		databaseID = section.getLong("databaseID");
		setKit(section.getInt("kitIndex"));
		lives = section.getInt("lives");
		rateValue = section.getInt("rateValue");
		team = game.getTeam(section.getInt("team"));
		points = section.getInt("points");
	}

	public void save(ConfigurationSection section) {
		section.set("databaseID", databaseID);
		section.set("kitIndex", kitIndex);
		section.set("lives", lives);
		section.set("rateValue", rateValue);
		section.set("team", team.getId());
		section.set("points", points);
	}

	public void databaseSave(StringBuilder builder) {
		builder.append(databaseID);
		builder.append(',');
		int earnedCredits = getCreditsEarned();
		if (serverPlayer != null)
			serverPlayer.addCredits(earnedCredits);
		builder.append(earnedCredits);
		builder.append(',');
		builder.append(team.getName());
		builder.append(',');
		builder.append(kitIndex);
		builder.append(',');
		builder.append(points);
	}
}
