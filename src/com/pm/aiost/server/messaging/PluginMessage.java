package com.pm.aiost.server.messaging;

import java.util.Collection;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.pm.aiost.Aiost;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.server.Server;
import com.pm.aiost.server.ServerType;

public class PluginMessage {

	public static final String CHANNEL_KEY = "BungeeCord";
	private static final Plugin plugin = Aiost.getPlugin();

	public static void send(Player sender, byte[] data) {
		sender.sendPluginMessage(plugin, CHANNEL_KEY, data);
	}

	public static void send(Player sender, ByteArrayDataOutput data) {
		send(sender, data.toByteArray());
	}

	public static void send(byte[] data) {
		send(Iterables.getFirst(Bukkit.getOnlinePlayers(), null), data);
	}

	public static void send(ByteArrayDataOutput data) {
		send(Iterables.getFirst(Bukkit.getOnlinePlayers(), null), data.toByteArray());
	}

	public static void send(Player sender, String channelName) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF(channelName);
		send(sender, out);
	}

	public static void send(String channelName) {
		send(Iterables.getFirst(Bukkit.getOnlinePlayers(), null), channelName);
	}

	public static void send(Player sender, String channelName, byte[] data) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF(channelName);
		out.write(data);
		send(sender, out);
	}

	public static void send(Player sender, String channelName, ByteArrayDataOutput data) {
		send(sender, channelName, data.toByteArray());
	}

	public static void send(String channelName, byte[] data) {
		send(Iterables.getFirst(Bukkit.getOnlinePlayers(), null), channelName, data);
	}

	public static void send(String channelName, ByteArrayDataOutput data) {
		send(Iterables.getFirst(Bukkit.getOnlinePlayers(), null), channelName, data.toByteArray());
	}

	public static void sendForward(Player sender, String serverName, String channelName, byte[] data) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Forward");
		out.writeUTF(serverName);
		out.writeUTF(channelName); // The channel name to check if this your data
		out.writeShort(data.length);
		out.write(data);
		send(sender, out);
	}

	public static void sendForward(Player sender, String serverName, String channelName, ByteArrayDataOutput data) {
		sendForward(sender, serverName, channelName, data.toByteArray());
	}

	public static void sendForward(Player sender, String channelName, byte[] data) {
		sendForward(sender, "ALL", channelName, data);
	}

	public static void sendForward(Player sender, String channelName, ByteArrayDataOutput data) {
		sendForward(sender, "ALL", channelName, data.toByteArray());
	}

	public static void sendForward(String serverName, String channelName, byte[] data) {
		sendForward(Iterables.getFirst(Bukkit.getOnlinePlayers(), null), serverName, channelName, data);
	}

	public static void sendForward(String serverName, String channelName, ByteArrayDataOutput data) {
		sendForward(Iterables.getFirst(Bukkit.getOnlinePlayers(), null), serverName, channelName, data.toByteArray());
	}

	public static void sendForward(String channelName, byte[] data) {
		sendForward(Iterables.getFirst(Bukkit.getOnlinePlayers(), null), "ALL", channelName, data);
	}

	public static void sendForward(String channelName, ByteArrayDataOutput data) {
		sendForward(Iterables.getFirst(Bukkit.getOnlinePlayers(), null), "ALL", channelName, data.toByteArray());
	}

	public static void sendForwardToPlayer(Player sender, String targetPlayerName, String channelName, byte[] data) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("ForwardToPlayer");
		out.writeUTF(targetPlayerName);
		out.writeUTF(channelName); // The channel name to check if this your data
		out.writeShort(data.length);
		out.write(data);
		send(sender, out);
	}

	public static void sendForwardToPlayer(Player sender, String targetPlayerName, String channelName,
			ByteArrayDataOutput data) {
		sendForwardToPlayer(sender, targetPlayerName, channelName, data.toByteArray());
	}

	public static void sendForwardToPlayer(String targetPlayerName, String channelName, byte[] data) {
		sendForwardToPlayer(Iterables.getFirst(Bukkit.getOnlinePlayers(), null), targetPlayerName, channelName, data);
	}

	public static void sendForwardToPlayer(String targetPlayerName, String channelName, ByteArrayDataOutput data) {
		sendForwardToPlayer(Iterables.getFirst(Bukkit.getOnlinePlayers(), null), targetPlayerName, channelName,
				data.toByteArray());
	}

	public static void connect(ServerPlayer serverPlayer, Server server) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("connect");
		out.writeInt(serverPlayer.getBungeeID());
		out.writeInt(server.getType().index);
		out.writeInt(server.getId());
		send(serverPlayer.player, out);
	}

	public static void connectAll(Collection<ServerPlayer> serverPlayer, Server server) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("connectAll");
		out.writeInt(server.getType().index);
		out.writeInt(server.getId());
		out.writeInt(serverPlayer.size());
		ServerPlayer p = writePlayerList(out, serverPlayer);
		send(p.player, out);
	}

	public static void connectOpen(ServerPlayer serverPlayer, ServerType serverType) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("connectOpen");
		out.writeInt(serverPlayer.getBungeeID());
		out.writeInt(serverType.index);
		send(serverPlayer.player, out);
	}

	public static void connectAllOpen(Collection<ServerPlayer> serverPlayer, ServerType serverType) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("connectAllOpen");
		out.writeInt(serverType.index);
		out.writeInt(serverPlayer.size());
		ServerPlayer p = writePlayerList(out, serverPlayer);
		send(p.player, out);
	}

	public static void connectPerName(ServerPlayer serverPlayer, String serverName) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("connectPerName");
		out.writeInt(serverPlayer.getBungeeID());
		out.writeUTF(serverName);
		send(serverPlayer.player, out);
	}

	public static void connectAllPerName(Collection<ServerPlayer> serverPlayer, String serverName) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("connectAllPerName");
		out.writeUTF(serverName);
		out.writeInt(serverPlayer.size());
		ServerPlayer p = writePlayerList(out, serverPlayer);
		send(p.player, out);
	}

	public static void connectPerPlayer(ServerPlayer serverPlayer, String playerName) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("connectPerPlayer");
		out.writeInt(serverPlayer.getBungeeID());
		out.writeUTF(playerName);
		send(serverPlayer.player, out);
	}

	public static void connectAllPerPlayer(Collection<ServerPlayer> serverPlayer, String playerName) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("connectAllPerPlayer");
		out.writeUTF(playerName);
		out.writeInt(serverPlayer.size());
		ServerPlayer p = writePlayerList(out, serverPlayer);
		send(p.player, out);
	}

	public static void connect(Player sender, String serverName) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Connect");
		out.writeUTF(serverName);
		send(sender, out);
	}

	public static void getIP(Player sender) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("IP");
		send(sender, out);
	}

	public static void getPlayerAmount(String serverName) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("PlayerCount");
		out.writeUTF(serverName);
		send(out);
	}

	public static void getPlayerList(String serverName) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("PlayerList");
		out.writeUTF(serverName);
		send(out);
	}

	public static void getPlayerList() {
		getPlayerList("ALL");
	}

	public static void getServerIP(String serverName) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("ServerIP");
		out.writeUTF(serverName);
		send(out);
	}

	public static void kickPlayer(String name, String kickReason) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("KickPlayer");
		out.writeUTF(name);
		out.writeUTF(kickReason);
		send(out);
	}

	public static void sendMessage(String playerName, String msg) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Message");
		out.writeUTF(playerName);
		out.writeUTF(msg);
		send(out);
	}

	public static void getUUID(Player sender) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("UUID");
		send(sender, out);
	}

	public static void getUUID(String name) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("UUIDOther");
		out.writeUTF(name);
		send(out);
	}

	public static ServerPlayer writePlayerList(ByteArrayDataOutput out, Collection<ServerPlayer> serverPlayer) {
		Iterator<ServerPlayer> iter = serverPlayer.iterator();
		ServerPlayer p = iter.next();
		out.writeInt(p.getBungeeID());
		while (iter.hasNext())
			out.writeInt(iter.next().getBungeeID());
		return p;
	}
}
