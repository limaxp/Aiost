package com.pm.aiost.game.data;

import java.util.UUID;

import com.pm.aiost.game.GameType;

public class RemoteGameData implements IGameData {

	private final int id;
	private final String name;
	private final GameType<?> type;
	private final UUID uuid;
	private final String ownerName;
	private final int playerSize;
	private final int minPlayer;
	private final int maxPlayer;
	private final String password;

	public RemoteGameData(int id, UUID uuid, String name, String ownerName, GameType<?> type, int playerSize,
			int minPlayer, int maxPlayer, String password) {
		this.id = id;
		this.uuid = uuid;
		this.name = name;
		this.ownerName = ownerName;
		this.type = type;
		this.playerSize = playerSize;
		this.minPlayer = minPlayer;
		this.maxPlayer = maxPlayer;
		this.password = password;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public GameType<?> getType() {
		return type;
	}

	@Override
	public UUID getUniqueId() {
		return uuid;
	}

	@Override
	public String getAuthorName() {
		return ownerName;
	}

	@Override
	public int getPlayerSize() {
		return playerSize;
	}

	@Override
	public int getMinPlayer() {
		return minPlayer;
	}

	@Override
	public int getMaxPlayer() {
		return maxPlayer;
	}

	@Override
	public boolean hasPassword() {
		return password != null;
	}

	@Override
	public boolean checkPassword(String password) {
		if (password == null)
			return true;
		return this.password.equals(password);
	}
}
