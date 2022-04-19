package com.pm.aiost.game.data;

import java.util.UUID;

import com.pm.aiost.game.GameType;
import com.pm.aiost.server.world.region.IRegion;

public interface IGameData {

	public int getId();

	public String getName();

	public GameType<?> getType();

	public UUID getUniqueId();

	public String getAuthorName();

	public int getPlayerSize();

	public default IRegion getRegion() {
		throw new UnsupportedOperationException();
	}

	public int getMinPlayer();

	public int getMaxPlayer();

	public boolean hasPassword();

	public boolean checkPassword(String password);
}