package com.pm.aiost.misc.dataAccess;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.pm.aiost.game.Game;
import com.pm.aiost.game.GamePlayer;
import com.pm.aiost.game.GameTeam;
import com.pm.aiost.misc.other.DataManager;
import com.pm.aiost.misc.rank.Ranks;
import com.pm.aiost.misc.utils.nms.NMS;
import com.pm.aiost.player.ServerPlayer;

@SuppressWarnings("unchecked")
public class SpigotFileAccess extends FileAccess implements SpigotDataAccess {

	@SuppressWarnings("deprecation")
	@Override
	public void getPlayer(ServerPlayer serverPlayer, boolean loadInventory) throws SQLException {
		JSONObject playerJson = DataManager.getPlayer(serverPlayer.getDatabaseID());
		Player player = serverPlayer.player;
		serverPlayer.setRank(Ranks.get((int) (long) playerJson.get("rank_ID")));
		serverPlayer.setScore((int) (long) playerJson.get("score"));
		serverPlayer.setCredits((int) (long) playerJson.get("credits"));
		serverPlayer.setLevel((int) (long) playerJson.get("level"));
		serverPlayer.setExperience((int) (long) playerJson.get("experience"));
		player.setHealth((double) playerJson.get("health"));
		player.setMaxHealth((double) playerJson.get("max_Health"));
		serverPlayer.setMana((double) playerJson.get("mana"));
		serverPlayer.setMaxMana((double) playerJson.get("max_Mana"));
		player.setFoodLevel((int) (long) playerJson.get("hunger"));
		serverPlayer.setThirst((int) (long) playerJson.get("thirst"));
		player.setLevel((int) (long) playerJson.get("mc_Level"));
		player.setExp((float) (double) playerJson.get("mc_Experience"));

		JSONArray permissionsJson = DataManager.getPermissions(playerJson);
		for (int i = 0; i < permissionsJson.size(); i++)
			serverPlayer.getPermissions().add((int) (long) permissionsJson.get(i));

		JSONArray settingsJson = DataManager.getSettings(playerJson);
		JSONArray settingValuesJson = DataManager.getSettingValues(playerJson);
		for (int i = 0; i < settingsJson.size(); i++)
			serverPlayer.getSettings().put((int) (long) settingsJson.get(i), (short) (long) settingValuesJson.get(i));

		String inventory = (String) playerJson.get("player_Inventory");
		if (inventory != null && !inventory.isEmpty())
			NMS.loadPlayerInventoryFromNBTString(serverPlayer, inventory);

	}

	@SuppressWarnings("deprecation")
	@Override
	public void updatePlayer(ServerPlayer serverPlayer, String inventory) throws SQLException {
		JSONObject playerJson = DataManager.getPlayer(serverPlayer.getDatabaseID());
		Player player = serverPlayer.player;
		if (serverPlayer.hasChangedSettings()) {
			JSONArray settingsJson = DataManager.getSettings(playerJson);
			JSONArray settingValuesJson = DataManager.getSettingValues(playerJson);
			for (int settingID : serverPlayer.getChangedSettings())
				changeSetting(settingsJson, settingValuesJson, settingID, serverPlayer.getSetting(settingID));
		}

		if (serverPlayer.hasNewPermissions()) {
			JSONArray permissionsJson = DataManager.getPermissions(playerJson);
			for (int permissionID : serverPlayer.getNewPermissions())
				permissionsJson.add((long) permissionID);
		}

		playerJson.put("health", player.getHealth());
		playerJson.put("max_Health", player.getMaxHealth());
		playerJson.put("mana", serverPlayer.getMana());
		playerJson.put("max_Mana", serverPlayer.getMaxMana());
		playerJson.put("hunger", (long) player.getFoodLevel());
		playerJson.put("thirst", (long) serverPlayer.getThirst());
		playerJson.put("mc_Level", (long) player.getLevel());
		playerJson.put("mc_Experience", (double) player.getExp());
		playerJson.put("player_Inventory", inventory);
	}

	private void changeSetting(JSONArray settingsJson, JSONArray settingValuesJson, int settingID, short settingValue) {
		for (int i = 0; i < settingsJson.size(); i++) {
			if (settingID == (int) (long) settingsJson.get(i)) {
				settingValuesJson.set(i, (long) settingValue);
				return;
			}
		}
		settingsJson.add((long) settingID);
		settingValuesJson.add((long) settingValue);
	}

	@Override
	public void gameFinished(Game game) throws SQLException {
		List<GamePlayer> gamePlayerList = game.getPlayerData();
		StringBuilder playerData = new StringBuilder();
		int rateAmount = 0;
		int rateValue = 0;
		for (GamePlayer gamePlayer : gamePlayerList) {
			if (gamePlayer.hasRate()) {
				rateAmount++;
				rateValue += gamePlayer.getRateValue();
			}
			gamePlayer.databaseSave(playerData);
			playerData.append(';');
			addCredits(gamePlayer.getDatabaseID(), gamePlayer.getCreditsEarned());
		}

		UUID uuid = game.getUniqueId();
		if (rateAmount > 0)
			updateGameRate(uuid, rateAmount, rateValue);

		// TODO save games

		List<GameTeam> teamList = game.getTeams();
		StringBuilder teamData = new StringBuilder();
		for (GameTeam gameTeam : teamList) {
			gameTeam.databaseSave(teamData);
			playerData.append(';');
		}

		// TODO save teams
	}
}
