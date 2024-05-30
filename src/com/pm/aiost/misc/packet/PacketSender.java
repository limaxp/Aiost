package com.pm.aiost.misc.packet;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.pm.aiost.misc.nms.NMS;
import com.pm.aiost.misc.utils.LocationHelper;

import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.network.ServerPlayerConnection;

public class PacketSender {

	public static final double NEARBY_DISTANCE = 64;

	public static void send(Player player, Packet<?> packet) {
		NMS.to(player).connection.sendPacket(packet);
	}

	public static void send(ServerPlayer player, Packet<?> packet) {
		player.connection.sendPacket(packet);
	}

	public static void send(ServerPlayerConnection player, Packet<?> packet) {
		player.send(packet);
	}

	public static void send(Player player, Packet<?>... packets) {
		ServerGamePacketListenerImpl connection = NMS.to(player).connection;
		for (Packet<?> packet : packets)
			connection.sendPacket(packet);
	}

	public static void send(ServerPlayer player, Packet<?>... packets) {
		ServerGamePacketListenerImpl connection = player.connection;
		for (Packet<?> packet : packets)
			connection.sendPacket(packet);
	}

	public static void send(ServerPlayerConnection player, Packet<?>... packets) {
		for (Packet<?> packet : packets)
			player.send(packet);
	}

	public static void send(Player[] player, Packet<?> packet) {
		for (Player p : player)
			NMS.to(p).connection.sendPacket(packet);
	}

	public static void send(ServerPlayer[] player, Packet<?> packet) {
		for (ServerPlayer p : player)
			p.connection.sendPacket(packet);
	}

	public static void send(ServerPlayerConnection[] player, Packet<?> packet) {
		for (ServerPlayerConnection p : player)
			p.send(packet);
	}

	public static void send(Player player, Iterable<Packet<?>> packets) {
		ServerGamePacketListenerImpl connection = NMS.to(player).connection;
		for (Packet<?> packet : packets)
			connection.sendPacket(packet);
	}

	public static void send(ServerPlayer player, Iterable<Packet<?>> packets) {
		ServerGamePacketListenerImpl connection = player.connection;
		for (Packet<?> packet : packets)
			connection.sendPacket(packet);
	}

	public static void send(ServerPlayerConnection player, Iterable<Packet<?>> packets) {
		for (Packet<?> packet : packets)
			player.send(packet);
	}

	public static void send(Iterable<Player> player, Packet<?> packet) {
		for (Player p : player)
			((CraftPlayer) p).getHandle().connection.sendPacket(packet);
	}

	public static void send(Player[] player, Packet<?>... packets) {
		for (Player p : player) {
			ServerGamePacketListenerImpl connection = ((CraftPlayer) p).getHandle().connection;
			for (Packet<?> packet : packets)
				connection.sendPacket(packet);
		}
	}

	public static void send(ServerPlayer[] player, Packet<?>... packets) {
		for (ServerPlayer p : player) {
			ServerGamePacketListenerImpl connection = p.connection;
			for (Packet<?> packet : packets)
				connection.sendPacket(packet);
		}
	}

	public static void send(ServerPlayerConnection[] player, Packet<?>... packets) {
		for (ServerPlayerConnection p : player) {
			for (Packet<?> packet : packets)
				p.send(packet);
		}
	}

	public static void send(Iterable<Player> player, Packet<?>... packets) {
		for (Player p : player)
			for (Packet<?> packet : packets)
				((CraftPlayer) p).getHandle().connection.sendPacket(packet);
	}

	public static void send(Iterable<Player> player, Iterable<Packet<?>> packets) {
		for (Player p : player)
			for (Packet<?> packet : packets)
				((CraftPlayer) p).getHandle().connection.sendPacket(packet);
	}

	public static void sendAll(Packet<?> packet) {
		for (ServerPlayer player : NMS.getMinecraftServer().getPlayerList().players)
			player.connection.sendPacket(packet);
	}

	public static void sendAll(Packet<?>... packets) {
		for (ServerPlayer player : NMS.getMinecraftServer().getPlayerList().players)
			for (Packet<?> packet : packets)
				player.connection.sendPacket(packet);
	}

	public static void sendWorld(org.bukkit.World world, Packet<?> packet) {
		sendWorld(NMS.to(world), packet);
	}

	public static void sendWorld(ServerLevel world, Packet<?> packet) {
		for (ServerPlayer player : world.players())
			((ServerPlayer) player).connection.sendPacket(packet);
	}

	public static void sendWorld(org.bukkit.World world, Player except, Packet<?> packet) {
		sendWorld(NMS.to(world), ((CraftPlayer) except).getHandle(), packet);
	}

	public static void sendWorld(ServerLevel world, ServerPlayer except, Packet<?> packet) {
		for (ServerPlayer player : world.players())
			if (player != except)
				((ServerPlayer) player).connection.sendPacket(packet);
	}

	public static void sendWorld(org.bukkit.World world, Packet<?>... packets) {
		sendWorld(NMS.to(world), packets);
	}

	public static void sendWorld(org.bukkit.World world, Iterable<Packet<?>> packets) {
		sendWorld(NMS.to(world), packets);
	}

	public static void sendWorld(ServerLevel world, Packet<?>... packets) {
		for (ServerPlayer player : world.players())
			for (Packet<?> packet : packets)
				((ServerPlayer) player).connection.sendPacket(packet);
	}

	public static void sendWorld(ServerLevel world, Iterable<Packet<?>> packets) {
		for (ServerPlayer player : world.players())
			for (Packet<?> packet : packets)
				((ServerPlayer) player).connection.sendPacket(packet);
	}

	public static void sendWorld(org.bukkit.World world, Player except, Packet<?>... packets) {
		sendWorld(NMS.to(world), ((CraftPlayer) except).getHandle(), packets);
	}

	public static void sendWorld(org.bukkit.World world, Player except, Iterable<Packet<?>> packets) {
		sendWorld(NMS.to(world), ((CraftPlayer) except).getHandle(), packets);
	}

	public static void sendWorld(ServerLevel world, ServerPlayer except, Packet<?>... packets) {
		for (ServerPlayer player : world.players())
			if (player != except)
				for (Packet<?> packet : packets)
					((ServerPlayer) player).connection.sendPacket(packet);
	}

	public static void sendWorld(ServerLevel world, ServerPlayer except, Iterable<Packet<?>> packets) {
		for (ServerPlayer player : world.players())
			if (player != except)
				for (Packet<?> packet : packets)
					((ServerPlayer) player).connection.sendPacket(packet);
	}

	public static void sendNearby(Location loc, int distance, Packet<?> packet) {
		sendNearby(((CraftWorld) loc.getWorld()).getHandle(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(),
				distance, packet);
	}

	public static void sendNearby(org.bukkit.World world, int x, int y, int z, int distance, Packet<?> packet) {
		sendNearby(NMS.to(world), x, y, z, distance, packet);
	}

	public static void sendNearby(ServerLevel world, int x, int y, int z, int distance, Packet<?> packet) {
		for (ServerPlayer player : world.players()) {
			if (LocationHelper.distance(x, z, player.getX(), player.getZ()) <= distance)
				((ServerPlayer) player).connection.sendPacket(packet);
		}
	}

	public static void sendNearby(Location loc, int distance, Packet<?>... packets) {
		sendNearby(((CraftWorld) loc.getWorld()).getHandle(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(),
				distance, packets);
	}

	public static void sendNearby(org.bukkit.World world, int x, int y, int z, int distance, Packet<?>... packets) {
		sendNearby(NMS.to(world), x, y, z, distance, packets);
	}

	public static void sendNearby(ServerLevel world, int x, int y, int z, int distance, Packet<?>... packets) {
		for (ServerPlayer player : world.players()) {
			if (LocationHelper.distance(x, z, player.getX(), player.getZ()) <= distance)
				for (Packet<?> packet : packets)
					((ServerPlayer) player).connection.sendPacket(packet);
		}
	}

	public static void sendNearby(Location loc, double distance, Packet<?> packet) {
		sendNearby(((CraftWorld) loc.getWorld()).getHandle(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(),
				distance, packet);
	}

	public static void sendNearby(org.bukkit.World world, double x, double y, double z, double distance,
			Packet<?> packet) {
		sendNearby(NMS.to(world), x, y, z, distance, packet);
	}

	public static void sendNearby(ServerLevel world, double x, double y, double z, double distance, Packet<?> packet) {
		for (ServerPlayer player : world.players()) {
			if (LocationHelper.distance(x, z, player.getX(), player.getZ()) <= distance)
				((ServerPlayer) player).connection.sendPacket(packet);
		}
	}

	public static void sendNearby(Location loc, double distance, Packet<?>... packets) {
		sendNearby(((CraftWorld) loc.getWorld()).getHandle(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(),
				distance, packets);
	}

	public static void sendNearby(org.bukkit.World world, double x, double y, double z, double distance,
			Packet<?>... packets) {
		sendNearby(NMS.to(world), x, y, z, distance, packets);
	}

	public static void sendNearby(ServerLevel world, double x, double y, double z, double distance,
			Packet<?>... packets) {
		for (ServerPlayer player : world.players()) {
			if (LocationHelper.distance(x, z, player.getX(), player.getZ()) <= distance)
				for (Packet<?> packet : packets)
					((ServerPlayer) player).connection.sendPacket(packet);
		}
	}

	public static void sendNearby(Location loc, Packet<?> packet) {
		sendNearby(((CraftWorld) loc.getWorld()).getHandle(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(),
				packet);
	}

	public static void sendNearby(org.bukkit.World world, int x, int y, int z, Packet<?> packet) {
		sendNearby(NMS.to(world), x, y, z, packet);
	}

	public static void sendNearby(ServerLevel world, int x, int y, int z, Packet<?> packet) {
		for (ServerPlayer player : world.players()) {
			if (LocationHelper.distance(x, z, player.getX(), player.getZ()) <= NEARBY_DISTANCE)
				((ServerPlayer) player).connection.sendPacket(packet);
		}
	}

	public static void sendNearby(org.bukkit.World world, int x, int y, int z, Packet<?>... packets) {
		sendNearby(NMS.to(world), x, y, z, packets);
	}

	public static void sendNearby(ServerLevel world, int x, int y, int z, Packet<?>... packets) {
		for (ServerPlayer player : world.players()) {
			if (LocationHelper.distance(x, z, player.getX(), player.getZ()) <= NEARBY_DISTANCE)
				for (Packet<?> packet : packets)
					((ServerPlayer) player).connection.sendPacket(packet);
		}
	}

	public static void sendNearby(org.bukkit.World world, double x, double y, double z, Packet<?> packet) {
		sendNearby(NMS.to(world), x, y, z, packet);
	}

	public static void sendNearby(ServerLevel world, double x, double y, double z, Packet<?> packet) {
		for (ServerPlayer player : world.players()) {
			if (LocationHelper.distance(x, z, player.getX(), player.getZ()) <= NEARBY_DISTANCE)
				((ServerPlayer) player).connection.sendPacket(packet);
		}
	}

	public static void sendNearby(Location loc, Packet<?>... packets) {
		sendNearby(((CraftWorld) loc.getWorld()).getHandle(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(),
				packets);
	}

	public static void sendNearby(org.bukkit.World world, double x, double y, double z, Packet<?>... packets) {
		sendNearby(NMS.to(world), x, y, z, packets);
	}

	public static void sendNearby(ServerLevel world, double x, double y, double z, Packet<?>... packets) {
		for (ServerPlayer player : world.players()) {
			if (LocationHelper.distance(x, z, player.getX(), player.getZ()) <= NEARBY_DISTANCE)
				for (Packet<?> packet : packets)
					((ServerPlayer) player).connection.sendPacket(packet);
		}
	}
}
