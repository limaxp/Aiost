package com.pm.aiost.player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.pm.aiost.misc.dataAccess.DataAccess;
import com.pm.aiost.misc.log.Logger;
import com.pm.aiost.misc.utils.nms.NMS;
import com.pm.aiost.server.ServerManager;
import com.pm.aiost.server.ServerType;
import com.pm.aiost.server.messaging.PluginMessage;

public class PlayerDataLoader {

	public static void askForBungeePlayerData(Player player) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("requestPlayerData");
		out.writeUTF(player.getUniqueId().toString());
		PluginMessage.send(player, out);
	}

	public static void recieveBungeePlayerData(ByteArrayDataInput in) {
		Player player = Bukkit.getPlayer(UUID.fromString(in.readUTF()));
		if (player != null) {
			ServerPlayer serverPlayer = PlayerManager.getPlayer(player);
			serverPlayer.bungeeID = in.readInt();
			serverPlayer.databaseID = in.readLong();
			PlayerManager.onPlayerRecieveBungeeData(serverPlayer);
		}
	}

	public static void loadDataWithoutBungee(ServerPlayer player) {
		ResultSet resultSet = null;
		try {
			resultSet = DataAccess.getAccess().getPlayerCore(player.player.getUniqueId(), player.player.getName());
			if (resultSet.next())
				player.databaseID = resultSet.getLong(1);
		} catch (SQLException e) {
			Logger.err("PlayerManager: Error! Could not load data for player '" + player.player.getName() + "'", e);
		} finally {
			DataAccess.getAccess().closeResult(resultSet);
		}
		PlayerManager.onPlayerRecieveData(player);
	}

	public static void loadData(ServerPlayer serverPlayer) {
		try {
			DataAccess.getAccess().getPlayer(serverPlayer, ServerManager.getServer().getType() == ServerType.SURVIVAL);
		} catch (SQLException e) {
			Logger.err("PlayerManager: Error! Could not load data for player '" + serverPlayer.player.getName() + "'",
					e);
		}
	}

	public static void updateData(ServerPlayer serverPlayer) {
		String inventory;
		if (ServerManager.getServer().getType() == ServerType.SURVIVAL)
			inventory = NMS.savePlayerInventoryToNBTString(serverPlayer);
		else
			inventory = null;

		try {
			DataAccess.getAccess().updatePlayer(serverPlayer, inventory);
		} catch (SQLException e) {
			Logger.err("PlayerManager: Error! Could not update data for player '" + serverPlayer.player.getName() + "'",
					e);
		}
	}
}
