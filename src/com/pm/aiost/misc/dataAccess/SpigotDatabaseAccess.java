package com.pm.aiost.misc.dataAccess;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.bukkit.entity.Player;

import com.pm.aiost.game.Game;
import com.pm.aiost.game.GamePlayer;
import com.pm.aiost.game.GameTeam;
import com.pm.aiost.misc.database.Database;
import com.pm.aiost.misc.database.DatabaseManager;
import com.pm.aiost.misc.rank.Ranks;
import com.pm.aiost.misc.utils.nms.NMS;
import com.pm.aiost.player.ServerPlayer;

public class SpigotDatabaseAccess extends DatabaseAccess implements SpigotDataAccess {

	@SuppressWarnings("deprecation")
	@Override
	public void getPlayer(ServerPlayer serverPlayer, boolean loadInventory) throws SQLException {
		ResultSet resultSet = null;
		try {
			resultSet = getPlayer(serverPlayer.getDatabaseID(), loadInventory);
			if (resultSet.next()) {
				Player player = serverPlayer.player;
				serverPlayer.setRank(Ranks.get(resultSet.getInt(1)));
				serverPlayer.setScore(resultSet.getInt(2));
				serverPlayer.setCredits(resultSet.getInt(3));
				serverPlayer.setLevel(resultSet.getInt(4));
				serverPlayer.setExperience(resultSet.getInt(5));
				player.setHealth(resultSet.getDouble(6));
				player.setMaxHealth(resultSet.getDouble(7));
				serverPlayer.setMana(resultSet.getDouble(8));
				serverPlayer.setMaxMana(resultSet.getDouble(9));
				player.setFoodLevel(resultSet.getInt(10));
				serverPlayer.setThirst(resultSet.getInt(11));
				player.setLevel(resultSet.getInt(12));
				player.setExp(resultSet.getFloat(13));

				String permissions = resultSet.getString(14);
				if (permissions != null && !permissions.isEmpty()) {
					if (permissions.contains(",")) {
						for (String permission : permissions.split(","))
							serverPlayer.getPermissions().add(Integer.parseInt(permission));
					} else
						serverPlayer.getPermissions().add(Integer.parseInt(permissions));
				}

				String settings = resultSet.getString(15);
				String settingValues = resultSet.getString(16);
				if (settings != null && !settings.isEmpty() && settingValues != null && !settingValues.isEmpty()) {
					if (settings.contains(",")) {
						String[] settingsSplit = settings.split(",");
						String[] settingValuesSplit = settingValues.split(",");
						int length = settingsSplit.length;
						for (int i = 0; i < length; i++)
							serverPlayer.getSettings().put(Integer.parseInt(settingsSplit[i]),
									Short.parseShort(settingValuesSplit[i]));
					} else
						serverPlayer.getSettings().put(Integer.parseInt(settings), Short.parseShort(settingValues));
				}

				String inventory = resultSet.getString(17);
				if (inventory != null && !inventory.isEmpty())
					NMS.loadPlayerInventoryFromNBTString(serverPlayer, inventory);
			}
		} finally {
			DataAccess.getAccess().closeResult(resultSet);
		}
	}

	private ResultSet getPlayer(long ID, boolean loadInventory) throws SQLException {
		Database database = DatabaseManager.getDatabase();
		Connection connection = database.getConnection();
		CallableStatement statement = database.prepareCall(connection, "getPlayer(?, ?)");
		statement.setLong(1, ID);
		statement.setBoolean(2, loadInventory);
		return statement.executeQuery();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void updatePlayer(ServerPlayer serverPlayer, String inventory) throws SQLException {
		Player player = serverPlayer.player;
		String changedSettings;
		String changedSettingValues;
		if (serverPlayer.hasChangedSettings()) {
			StringBuilder changedSettingsBuilder = new StringBuilder();
			StringBuilder changedSettingValuesBuilder = new StringBuilder();
			for (int settingID : serverPlayer.getChangedSettings()) {
				changedSettingsBuilder.append(settingID).append(',');
				changedSettingValuesBuilder.append(serverPlayer.getSetting(settingID)).append(',');
			}
			changedSettingsBuilder.deleteCharAt(changedSettingsBuilder.length() - 1);
			changedSettingValuesBuilder.deleteCharAt(changedSettingValuesBuilder.length() - 1);
			changedSettings = changedSettingsBuilder.toString();
			changedSettingValues = changedSettingValuesBuilder.toString();
		} else {
			changedSettings = null;
			changedSettingValues = null;
		}

		String newPermissions;
		if (serverPlayer.hasNewPermissions()) {
			StringBuilder newPermissionsBuilder = new StringBuilder();
			for (int permissionID : serverPlayer.getNewPermissions())
				newPermissionsBuilder.append(permissionID).append(',');
			newPermissionsBuilder.deleteCharAt(newPermissionsBuilder.length() - 1);
			newPermissions = newPermissionsBuilder.toString();
		} else
			newPermissions = null;

		Database database = DatabaseManager.getDatabase();
		try (Connection connection = database.getConnection();
				CallableStatement statement = database.prepareCall(connection,
						"updatePlayer(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
			statement.setLong(1, serverPlayer.getDatabaseID());
			statement.setDouble(2, player.getHealth());
			statement.setDouble(3, player.getMaxHealth());
			statement.setDouble(4, serverPlayer.getMana());
			statement.setDouble(5, serverPlayer.getMaxMana());
			statement.setInt(6, player.getFoodLevel());
			statement.setInt(7, (int) serverPlayer.getThirst());
			statement.setInt(8, player.getLevel());
			statement.setFloat(9, player.getExp());
			statement.setString(10, newPermissions);
			statement.setString(11, changedSettings);
			statement.setString(12, changedSettingValues);
			statement.setString(13, inventory);
			statement.execute();
		}
	}

	@Override
	public void gameFinished(Game game) throws SQLException {
		List<GamePlayer> playerList = game.getPlayerData();
		StringBuilder playerData = new StringBuilder();
		int rateAmount = 0;
		int rateValue = 0;
		for (GamePlayer gamePlayer : playerList) {
			if (gamePlayer.hasRate()) {
				rateAmount++;
				rateValue += gamePlayer.getRateValue();
			}
			gamePlayer.databaseSave(playerData);
			playerData.append(';');
		}

		// TODO add teamData in Database Statement
		List<GameTeam> teamList = game.getTeams();
		StringBuilder teamData = new StringBuilder();
		for (GameTeam gameTeam : teamList) {
			gameTeam.databaseSave(teamData);
			playerData.append(';');
		}

		Database database = DatabaseManager.getDatabase();
		try (Connection connection = database.getConnection();
				CallableStatement statement = database.prepareCall(connection,
						"saveGameStats(?, ?, ?, ?, ?, ?, ?, ?)")) {
			statement.setInt(1, game.getType().getId());
			statement.setString(2, game.getUniqueId().toString());
			statement.setInt(3, game.getTime());
			statement.setInt(4, playerList.size());
			statement.setString(5, game.getWinCondition().getWinnerName(game));
			statement.setInt(6, rateAmount);
			statement.setInt(7, rateValue);
			statement.setString(8, playerData.toString());
			statement.execute();
		}
	}
}
