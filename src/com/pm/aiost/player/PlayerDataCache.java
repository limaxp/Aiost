package com.pm.aiost.player;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.RED;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.BitSet;

import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import com.pm.aiost.misc.dataAccess.DataAccess;
import com.pm.aiost.misc.log.Logger;

public class PlayerDataCache {

	// TODO player worlds(update this cache on delete), released games

	private final ServerPlayer serverPlayer;
	private final Int2ObjectMap<BitSet> unlockableMap = new Int2ObjectOpenHashMap<BitSet>();
//	private PlayerWorldData[] playerWorlds = new PlayerWorldData[28];

	PlayerDataCache(ServerPlayer serverPlayer) {
		this.serverPlayer = serverPlayer;
	}

//	public PlayerWorldData getPlayerWorld(int index) {
//		if (playerWorlds.length <= index)
//			loadPlayerWorlds(index - (index % 28));
//		return playerWorlds[index];
//	}
//
//	private void loadPlayerWorlds(int index) {
//		ensurePlayerWorldCapacity(index + 28);
//		ResultSet resultSet = null;
//		try {
//			resultSet = DataAccess.getAccess().getPlayerWorlds(serverPlayer.getDatabaseID(), 28, index);
//			while (resultSet.next()) {
//				playerWorlds[index++] = createPlayerWorldData(resultSet);
//			}
//		} catch (SQLException e) {
//			Logger.err("PlayerWorldMenu: Could not load worlds for player '" + serverPlayer.player.getName() + "'", e);
//		} finally {
//			DataAccess.getAccess().closeResult(resultSet);
//		}
//	}
//
//	private void ensurePlayerWorldCapacity(int minCapacity) {
//		int length = playerWorlds.length;
//		if (minCapacity > length)
//			resizePlayerWorlds(Math.max(length * 2, minCapacity));
//	}
//
//	private void resizePlayerWorlds(int size) {
//		PlayerWorldData[] newArray = new PlayerWorldData[size];
//		System.arraycopy(playerWorlds, 0, newArray, 0, playerWorlds.length);
//		playerWorlds = newArray;
//	}
//
//	public static PlayerWorldData createPlayerWorldData(ResultSet resultSet) throws SQLException {
//		PlayerWorldData data = new PlayerWorldData();
//		data.uuid = UUID.fromString(resultSet.getString(1));
//		data.locationId = resultSet.getInt(2);
//		data.name = resultSet.getString(3);
//		data.environment = Environment.values()[resultSet.getByte(4)];
//		data.worldType = AiostWorldType.get(resultSet.getInt(5));
//		data.generateStructures = resultSet.getBoolean(6);
//		data.saveDate = resultSet.getDate(7);
//		data.saveTime = resultSet.getTime(7);
//		return data;
//	}

	public boolean addUnlockable(int type, short id) {
		try {
			DataAccess.getAccess().addUnlockable(serverPlayer.databaseID, type, id);
		} catch (SQLException e) {
			Logger.err("PlayerDataCache: Error! Could not add unlockable of type '" + type + "' with id '" + id
					+ "' for player '" + serverPlayer.name + "'", e);
			return false;
		}
		getUnlockables(type).set(id);
		return true;
	}

	public boolean buyUnlockable(int type, short id, int price) {
		if (!serverPlayer.hasCredits(price)) {
			serverPlayer.player.sendMessage(RED + BOLD + "You dont have enough credits to buy this!");
			return false;
		}
		try {
			DataAccess.getAccess().buyUnlockable(serverPlayer.databaseID, type, id, price);
		} catch (SQLException e) {
			Logger.err("PlayerDataCache: Error! Could not buy unlockable of type '" + type + "' with id '" + id
					+ "' for player '" + serverPlayer.name + "'", e);
			return false;
		}
		serverPlayer.removeCredits(price);
		getUnlockables(type).set(id);
		return true;
	}

	public boolean hasUnlockable(int type, short id) {
		return getUnlockables(type).get(id);
	}

	public BitSet getUnlockables(int type) {
		BitSet typeBitSet = unlockableMap.get(type);
		if (typeBitSet == null)
			unlockableMap.put(type, typeBitSet = loadUnlockables(type));
		return typeBitSet;
	}

	private BitSet loadUnlockables(int type) {
		BitSet bitSet = new BitSet();
		ResultSet resultSet = null;
		try {
			resultSet = DataAccess.getAccess().getUnlockables(serverPlayer.databaseID, type);
			while (resultSet.next()) {
				bitSet.set(resultSet.getShort(1));
			}
		} catch (SQLException e) {
			Logger.err("PlayerDataCache: Error! Could not load unlockables of type '" + type + "' for player '"
					+ serverPlayer.name + "'", e);
		} finally {
			DataAccess.getAccess().closeResult(resultSet);
		}
		return bitSet;
	}
}
