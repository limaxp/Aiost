package com.pm.aiost.player.handler;

import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.pm.aiost.Aiost;
import com.pm.aiost.collection.list.UnorderedIdentityArrayList;
import com.pm.aiost.misc.packet.entity.entities.ChatHologram;
import com.pm.aiost.misc.utils.LocationHelper;
import com.pm.aiost.player.ServerPlayer;

public class ChatHologramHandler {

	public static final int MAX_ROW_LENGTH = 30;
	public static final int SHOW_TIME = 160;
	public static final int CHAT_HOLOGRAM_VISIBILE_RANGE = 3 * 16;
	public static final double Y_OFFSET = 0.5;

	public static void show(ServerPlayer serverPlayer, String msg, Set<Player> recipients) {
		Location loc = serverPlayer.player.getLocation();
		double x = loc.getX();
		double z = loc.getZ();
		List<Player> nearRecipents = new UnorderedIdentityArrayList<Player>();
		for (Player recipient : recipients) {
			Location recipentLoc = recipient.getLocation();
			if (LocationHelper.distance(x, z, recipentLoc.getX(), recipentLoc.getZ()) <= CHAT_HOLOGRAM_VISIBILE_RANGE)
				nearRecipents.add(recipient);
		}
		if (nearRecipents.size() > 0) {
			ChatHologram chatHologram = new ChatHologram(serverPlayer.getServerWorld(), convert(msg), nearRecipents);
			chatHologram.setPositionRotation(x, loc.getY() + Y_OFFSET, z, 0.0F, 0.0F);
			chatHologram.spawn();
			serverPlayer.addChatHologram(chatHologram);
			Bukkit.getScheduler().runTaskLater(Aiost.getPlugin(), serverPlayer::removeChatHologram, SHOW_TIME);
		}
	}

	private static String[] convert(String msg) {
		int length = msg.length();
		String[] arr = new String[length / MAX_ROW_LENGTH + 1];
		StringBuilder builder = new StringBuilder();
		int added = 0;
		int nextStop = MAX_ROW_LENGTH;
		for (int i = 0; i < length; i++) {
			char c = msg.charAt(i);
			if (i < nextStop)
				builder.append(c);
			else {
				arr[added++] = builder.toString();
				builder = new StringBuilder();
				nextStop = MAX_ROW_LENGTH * (added + 1);
			}
		}
		arr[added] = builder.toString();
		return arr;
	}
}
