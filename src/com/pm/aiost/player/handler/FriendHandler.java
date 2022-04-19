package com.pm.aiost.player.handler;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.GREEN;
import static com.pm.aiost.misc.utils.ChatColor.RED;

import java.sql.SQLException;

import org.bukkit.entity.Player;

import com.pm.aiost.misc.dataAccess.DataAccess;
import com.pm.aiost.misc.log.Logger;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.server.request.ServerRequest;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class FriendHandler {

	private static final String[] REQUEST_ANSWER_CODES = new String[] {
			GREEN + BOLD + "Friend request successful send!", GREEN + BOLD + "You are friends now!",
			RED + BOLD + "No player found for name!",
			RED + BOLD + "You have already send a friend request to this player!",
			RED + BOLD + "You already are friends!" };

	public static void sendRequest(ServerPlayer serverPlayer, String name) {
		if (serverPlayer.name.equals(name)) {
			serverPlayer.player.sendMessage(RED + BOLD + "Cannot send friend request to yourself!");
			return;
		}
		byte answerCode;
		try {
			answerCode = DataAccess.getAccess().addFriendRequest(name, serverPlayer.getDatabaseID());
		} catch (SQLException e) {
			Logger.err("FriendHandler: Error! Could not send friend request with name " + name + " for player '"
					+ serverPlayer.player.getName() + "'", e);
			return;
		}
		serverPlayer.player.sendMessage(REQUEST_ANSWER_CODES[answerCode]);
		if (answerCode == 0) // == successful!
			ServerRequest.getHandler().sendFriendRequest(name, serverPlayer);
	}

	public static void sendRequest(ServerPlayer serverPlayer, long id) {
		if (serverPlayer.getDatabaseID() == id) {
			serverPlayer.player.sendMessage(RED + BOLD + "Cannot send friend request to yourself!");
			return;
		}
		byte answerCode;
		try {
			answerCode = DataAccess.getAccess().addFriendRequest(id, serverPlayer.getDatabaseID());
		} catch (SQLException e) {
			Logger.err("FriendHandler: Error! Could not send friend request with id '" + id + "' for player '"
					+ serverPlayer.player.getName() + "'", e);
			return;
		}
		serverPlayer.player.sendMessage(REQUEST_ANSWER_CODES[answerCode]);
	}

	public static void sendRequestMessage(Player player, String requestPlayerName) {
		TextComponent message = new TextComponent(requestPlayerName + " has send you a friend request!\n");
		message.setBold(true);
		TextComponent accept = new TextComponent("[Accept] ");
		accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friend " + requestPlayerName));
		accept.setColor(ChatColor.GREEN);
		accept.setBold(true);
		TextComponent decline = new TextComponent("[Decline]");
		decline.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friendDecline " + requestPlayerName));
		decline.setColor(ChatColor.RED);
		decline.setBold(true);
		message.addExtra(accept);
		message.addExtra(decline);
		player.spigot().sendMessage(message);
	}

	public static void removeRequest(long recieverId, ServerPlayer sender) {
		try {
			DataAccess.getAccess().removeFriendRequest(recieverId, sender.getDatabaseID());
		} catch (SQLException e) {
			Logger.err("FriendHandler: Error! Could not remove friend request with id '" + recieverId + "' for player '"
					+ sender.name + "'", e);
			return;
		}
	}

	public static void removeRequest(ServerPlayer reciever, long senderId) {
		try {
			DataAccess.getAccess().removeFriendRequest(reciever.getDatabaseID(), senderId);
		} catch (SQLException e) {
			Logger.err("FriendHandler: Error! Could not remove friend request with id '" + senderId + "' for player '"
					+ reciever.name + "'", e);
			return;
		}
	}

	public static void removeRequest(ServerPlayer reciever, String senderName) {
		try {
			DataAccess.getAccess().removeFriendRequest(reciever.getDatabaseID(), senderName);
		} catch (SQLException e) {
			Logger.err("FriendHandler: Error! Could not remove friend request with name '" + senderName
					+ "' for player '" + reciever.name + "'", e);
			return;
		}
	}

	public static void remove(ServerPlayer serverPlayer, long Id) {
		try {
			DataAccess.getAccess().removeFriend(serverPlayer.getDatabaseID(), Id);
		} catch (SQLException e) {
			Logger.err("FriendHandler: Error! Could not remove friend with id '" + Id + "' for player '"
					+ serverPlayer.player.getName() + "'", e);
			return;
		}
	}

	public static void remove(ServerPlayer serverPlayer, String name) {
		try {
			DataAccess.getAccess().removeFriend(serverPlayer.getDatabaseID(), name);
		} catch (SQLException e) {
			Logger.err("FriendHandler: Error! Could not remove friend with name '" + name + "' for player '"
					+ serverPlayer.player.getName() + "'", e);
			return;
		}
	}
}
