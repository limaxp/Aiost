package com.pm.aiost.misc.dataAccess;

import java.sql.SQLException;

import com.pm.aiost.game.Game;
import com.pm.aiost.player.ServerPlayer;

public interface SpigotDataAccess extends IDataAccess {

	public void getPlayer(ServerPlayer serverPlayer, boolean loadInventory) throws SQLException;

	public void updatePlayer(ServerPlayer serverPlayer, String inventory) throws SQLException;

	public void gameFinished(Game game) throws SQLException;
}
