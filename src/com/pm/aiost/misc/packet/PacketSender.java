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

public class PacketSender {

	public static final double NEARBY_DISTANCE = 64;

	public static void send(Player player, Packet<?> packet) {
		NMS.to(player).connection.sendPacket(packet);
	}

	public static void send_(Player player, Object packet) {
		NMS.to(player).connection.sendPacket((Packet<?>) packet);
	}

	public static void sendNMS(ServerPlayer player, Packet<?> packet) {
		player.connection.sendPacket(packet);
	}

	public static void sendNMS_(ServerPlayer player, Object packet) {
		player.connection.sendPacket((Packet<?>) packet);
	}

	public static void sendReflected(Object player, Packet<?> packet) {
		NMS.to((Player) player).connection.sendPacket(packet);
	}

	public static void sendReflected_(Object player, Object packet) {
		NMS.to((Player) player).connection.sendPacket((Packet<?>) packet);
	}

	public static void send(Player player, Packet<?>... packets) {
		ServerGamePacketListenerImpl connection = NMS.to(player).connection;
		for (Packet<?> packet : packets)
			connection.sendPacket(packet);
	}

	public static void send_(Player player, Object... packets) {
		ServerGamePacketListenerImpl connection = NMS.to(player).connection;
		for (Object packet : packets)
			connection.sendPacket((Packet<?>) packet);
	}

	public static void sendNMS(ServerPlayer player, Packet<?>... packets) {
		ServerGamePacketListenerImpl connection = player.connection;
		for (Packet<?> packet : packets)
			connection.sendPacket(packet);
	}

	public static void sendNMS_(ServerPlayer player, Object... packets) {
		ServerGamePacketListenerImpl connection = player.connection;
		for (Object packet : packets)
			connection.sendPacket((Packet<?>) packet);
	}

	public static void sendReflected(Object player, Packet<?>... packets) {
		ServerGamePacketListenerImpl connection = ((ServerPlayer) player).connection;
		for (Packet<?> packet : packets)
			connection.sendPacket(packet);
	}

	public static void sendReflected_(Object player, Object... packets) {
		ServerGamePacketListenerImpl connection = ((ServerPlayer) player).connection;
		for (Object packet : packets)
			connection.sendPacket((Packet<?>) packet);
	}

	public static void send(Player[] player, Packet<?> packet) {
		for (Player p : player)
			NMS.to(p).connection.sendPacket(packet);
	}

	public static void send_(Player[] player, Object packet) {
		for (Player p : player)
			NMS.to(p).connection.sendPacket((Packet<?>) packet);
	}

	public static void sendNMS(ServerPlayer[] player, Packet<?> packet) {
		for (ServerPlayer p : player)
			p.connection.sendPacket(packet);
	}

	public static void sendNMS_(ServerPlayer[] player, Object packet) {
		for (ServerPlayer p : player)
			p.connection.sendPacket((Packet<?>) packet);
	}

	public static void sendReflected(Object[] player, Packet<?> packet) {
		for (Object p : player)
			((ServerPlayer) p).connection.sendPacket(packet);
	}

	public static void sendReflected_(Object[] player, Object packet) {
		for (Object p : player)
			((ServerPlayer) p).connection.sendPacket((Packet<?>) packet);
	}

	public static void send(Player player, Iterable<Packet<?>> packets) {
		ServerGamePacketListenerImpl connection = NMS.to(player).connection;
		for (Packet<?> packet : packets)
			connection.sendPacket(packet);
	}

	public static void send_(Player player, Iterable<Object> packets) {
		ServerGamePacketListenerImpl connection = NMS.to(player).connection;
		for (Object packet : packets)
			connection.sendPacket((Packet<?>) packet);
	}

	public static void sendNMS(ServerPlayer player, Iterable<Packet<?>> packets) {
		ServerGamePacketListenerImpl connection = player.connection;
		for (Packet<?> packet : packets)
			connection.sendPacket(packet);
	}

	public static void sendNMS_(ServerPlayer player, Iterable<Object> packets) {
		ServerGamePacketListenerImpl connection = player.connection;
		for (Object packet : packets)
			connection.sendPacket((Packet<?>) packet);
	}

	public static void sendReflected(Object player, Iterable<Packet<?>> packets) {
		ServerGamePacketListenerImpl connection = ((ServerPlayer) player).connection;
		for (Packet<?> packet : packets)
			connection.sendPacket(packet);
	}

	public static void sendReflected_(Object player, Iterable<Object> packets) {
		ServerGamePacketListenerImpl connection = ((ServerPlayer) player).connection;
		for (Object packet : packets)
			connection.sendPacket((Packet<?>) packet);
	}

	public static void send(Iterable<Player> player, Packet<?> packet) {
		for (Player p : player)
			((CraftPlayer) p).getHandle().connection.sendPacket(packet);
	}

	public static void send_(Iterable<Player> player, Object packet) {
		for (Player p : player)
			((CraftPlayer) p).getHandle().connection.sendPacket((Packet<?>) packet);
	}

	public static void sendNMS(Iterable<ServerPlayer> player, Packet<?> packet) {
		for (ServerPlayer p : player)
			p.connection.sendPacket(packet);
	}

	public static void sendNMS_(Iterable<ServerPlayer> player, Object packet) {
		for (ServerPlayer p : player)
			p.connection.sendPacket((Packet<?>) packet);
	}

	public static void sendReflected(Iterable<Object> player, Packet<?> packet) {
		for (Object p : player) {
			ServerGamePacketListenerImpl connection = ((ServerPlayer) p).connection;
			connection.sendPacket(packet);
		}
	}

	public static void sendReflected_(Iterable<Object> player, Object packet) {
		for (Object p : player) {
			ServerGamePacketListenerImpl connection = ((ServerPlayer) p).connection;
			connection.sendPacket((Packet<?>) packet);
		}
	}

	public static void send(Player[] player, Packet<?>... packets) {
		for (Player p : player) {
			ServerGamePacketListenerImpl connection = ((CraftPlayer) p).getHandle().connection;
			for (Packet<?> packet : packets)
				connection.sendPacket(packet);
		}
	}

	public static void send_(Player[] player, Object... packets) {
		for (Player p : player) {
			ServerGamePacketListenerImpl connection = ((CraftPlayer) p).getHandle().connection;
			for (Object packet : packets)
				connection.sendPacket((Packet<?>) packet);
		}
	}

	public static void sendNMS(ServerPlayer[] player, Packet<?>... packets) {
		for (ServerPlayer p : player) {
			ServerGamePacketListenerImpl connection = p.connection;
			for (Packet<?> packet : packets)
				connection.sendPacket(packet);
		}
	}

	public static void sendNMS_(ServerPlayer[] player, Object... packets) {
		for (ServerPlayer p : player) {
			ServerGamePacketListenerImpl connection = p.connection;
			for (Object packet : packets)
				connection.sendPacket((Packet<?>) packet);
		}
	}

	public static void sendReflected(Object[] player, Packet<?>... packets) {
		for (Object p : player) {
			ServerGamePacketListenerImpl connection = ((ServerPlayer) p).connection;
			for (Packet<?> packet : packets)
				connection.sendPacket(packet);
		}
	}

	public static void sendReflected_(Object[] player, Object... packets) {
		for (Object p : player) {
			ServerGamePacketListenerImpl connection = ((ServerPlayer) p).connection;
			for (Object packet : packets)
				connection.sendPacket((Packet<?>) packet);
		}
	}

	public static void send(Iterable<Player> player, Packet<?>... packets) {
		for (Player p : player)
			for (Packet<?> packet : packets)
				((CraftPlayer) p).getHandle().connection.sendPacket(packet);
	}

	public static void send_(Iterable<Player> player, Object... packets) {
		for (Player p : player)
			for (Object packet : packets)
				((CraftPlayer) p).getHandle().connection.sendPacket((Packet<?>) packet);
	}

	public static void sendNMS(Iterable<ServerPlayer> player, Packet<?>... packets) {
		for (ServerPlayer p : player) {
			ServerGamePacketListenerImpl connection = p.connection;
			for (Packet<?> packet : packets)
				connection.sendPacket(packet);
		}
	}

	public static void sendNMS_(Iterable<ServerPlayer> player, Object... packets) {
		for (ServerPlayer p : player) {
			ServerGamePacketListenerImpl connection = p.connection;
			for (Object packet : packets)
				connection.sendPacket((Packet<?>) packet);
		}
	}

	public static void sendReflected(Iterable<Object> player, Packet<?>... packets) {
		for (Object p : player) {
			ServerGamePacketListenerImpl connection = ((ServerPlayer) p).connection;
			for (Packet<?> packet : packets)
				connection.sendPacket(packet);
		}
	}

	public static void sendReflected_(Iterable<Object> player, Object... packets) {
		for (Object p : player) {
			ServerGamePacketListenerImpl connection = ((ServerPlayer) p).connection;
			for (Object packet : packets)
				connection.sendPacket((Packet<?>) packet);
		}
	}

	public static void send(Iterable<Player> player, Iterable<Packet<?>> packets) {
		for (Player p : player)
			for (Packet<?> packet : packets)
				((CraftPlayer) p).getHandle().connection.sendPacket(packet);
	}

	public static void send_(Iterable<Player> player, Iterable<Object> packets) {
		for (Player p : player)
			for (Object packet : packets)
				((CraftPlayer) p).getHandle().connection.sendPacket((Packet<?>) packet);
	}

	public static void sendNMS(Iterable<ServerPlayer> player, Iterable<Packet<?>> packets) {
		for (ServerPlayer p : player) {
			ServerGamePacketListenerImpl connection = p.connection;
			for (Packet<?> packet : packets)
				connection.sendPacket(packet);
		}
	}

	public static void sendNMS_(Iterable<ServerPlayer> player, Iterable<Object> packets) {
		for (ServerPlayer p : player) {
			ServerGamePacketListenerImpl connection = p.connection;
			for (Object packet : packets)
				connection.sendPacket((Packet<?>) packet);
		}
	}

	public static void sendReflected(Iterable<Object> player, Iterable<Packet<?>> packets) {
		for (Object p : player) {
			ServerGamePacketListenerImpl connection = ((ServerPlayer) p).connection;
			for (Packet<?> packet : packets)
				connection.sendPacket(packet);
		}
	}

	public static void sendReflected_(Iterable<Object> player, Iterable<Object> packets) {
		for (Object p : player) {
			ServerGamePacketListenerImpl connection = ((ServerPlayer) p).connection;
			for (Object packet : packets)
				connection.sendPacket((Packet<?>) packet);
		}
	}

	public static void sendAll(Packet<?> packet) {
		for (ServerPlayer player : NMS.getMinecraftServer().getPlayerList().players)
			player.connection.sendPacket(packet);
	}

	public static void sendAll_(Object packet) {
		for (ServerPlayer player : NMS.getMinecraftServer().getPlayerList().players)
			player.connection.sendPacket((Packet<?>) packet);
	}

	public static void sendAll(Packet<?>... packets) {
		for (ServerPlayer player : NMS.getMinecraftServer().getPlayerList().players)
			for (Packet<?> packet : packets)
				player.connection.sendPacket(packet);
	}

	public static void sendAll_(Object... packets) {
		for (ServerPlayer player : NMS.getMinecraftServer().getPlayerList().players)
			for (Object packet : packets)
				player.connection.sendPacket((Packet<?>) packet);
	}

	public static void sendWorld(org.bukkit.World world, Packet<?> packet) {
		sendNMSWorld(NMS.to(world), packet);
	}

	public static void sendWorld_(org.bukkit.World world, Object packet) {
		sendNMSWorld_(NMS.to(world), packet);
	}

	public static void sendNMSWorld(ServerLevel world, Packet<?> packet) {
		for (ServerPlayer player : world.players())
			((ServerPlayer) player).connection.sendPacket(packet);
	}

	public static void sendNMSWorld_(ServerLevel world, Object packet) {
		for (ServerPlayer player : world.players())
			((ServerPlayer) player).connection.sendPacket((Packet<?>) packet);
	}

	public static void sendWorld(org.bukkit.World world, Player except, Packet<?> packet) {
		sendNMSWorld(NMS.to(world), ((CraftPlayer) except).getHandle(), packet);
	}

	public static void sendWorld_(org.bukkit.World world, Player except, Object packet) {
		sendNMSWorld_(NMS.to(world), ((CraftPlayer) except).getHandle(), packet);
	}

	public static void sendNMSWorld(ServerLevel world, ServerPlayer except, Packet<?> packet) {
		for (ServerPlayer player : world.players())
			if (player != except)
				((ServerPlayer) player).connection.sendPacket(packet);
	}

	public static void sendNMSWorld_(ServerLevel world, ServerPlayer except, Object packet) {
		for (ServerPlayer player : world.players())
			if (player != except)
				((ServerPlayer) player).connection.sendPacket((Packet<?>) packet);
	}

	public static void sendWorld(org.bukkit.World world, Packet<?>... packets) {
		sendNMSWorld(NMS.to(world), packets);
	}

	public static void sendWorld_(org.bukkit.World world, Object... packets) {
		sendNMSWorld_(NMS.to(world), packets);
	}

	public static void sendWorld(org.bukkit.World world, Iterable<Packet<?>> packets) {
		sendNMSWorld(NMS.to(world), packets);
	}

	public static void sendWorld_(org.bukkit.World world, Iterable<Object> packets) {
		sendNMSWorld_(NMS.to(world), packets);
	}

	public static void sendNMSWorld(ServerLevel world, Packet<?>... packets) {
		for (ServerPlayer player : world.players())
			for (Packet<?> packet : packets)
				((ServerPlayer) player).connection.sendPacket(packet);
	}

	public static void sendNMSWorld_(ServerLevel world, Object... packets) {
		for (ServerPlayer player : world.players())
			for (Object packet : packets)
				((ServerPlayer) player).connection.sendPacket((Packet<?>) packet);
	}

	public static void sendNMSWorld(ServerLevel world, Iterable<Packet<?>> packets) {
		for (ServerPlayer player : world.players())
			for (Packet<?> packet : packets)
				((ServerPlayer) player).connection.sendPacket(packet);
	}

	public static void sendNMSWorld_(ServerLevel world, Iterable<Object> packets) {
		for (ServerPlayer player : world.players())
			for (Object packet : packets)
				((ServerPlayer) player).connection.sendPacket((Packet<?>) packet);
	}

	public static void sendWorld(org.bukkit.World world, Player except, Packet<?>... packets) {
		sendNMSWorld(NMS.to(world), ((CraftPlayer) except).getHandle(), packets);
	}

	public static void sendWorld_(org.bukkit.World world, Player except, Object... packets) {
		sendNMSWorld_(NMS.to(world), ((CraftPlayer) except).getHandle(), packets);
	}

	public static void sendWorld(org.bukkit.World world, Player except, Iterable<Packet<?>> packets) {
		sendNMSWorld(NMS.to(world), ((CraftPlayer) except).getHandle(), packets);
	}

	public static void sendWorld_(org.bukkit.World world, Player except, Iterable<Object> packets) {
		sendNMSWorld_(NMS.to(world), ((CraftPlayer) except).getHandle(), packets);
	}

	public static void sendNMSWorld(ServerLevel world, ServerPlayer except, Packet<?>... packets) {
		for (ServerPlayer player : world.players())
			if (player != except)
				for (Packet<?> packet : packets)
					((ServerPlayer) player).connection.sendPacket(packet);
	}

	public static void sendNMSWorld_(ServerLevel world, ServerPlayer except, Object... packets) {
		for (ServerPlayer player : world.players())
			if (player != except)
				for (Object packet : packets)
					((ServerPlayer) player).connection.sendPacket((Packet<?>) packet);
	}

	public static void sendNMSWorld(ServerLevel world, ServerPlayer except, Iterable<Packet<?>> packets) {
		for (ServerPlayer player : world.players())
			if (player != except)
				for (Packet<?> packet : packets)
					((ServerPlayer) player).connection.sendPacket(packet);
	}

	public static void sendNMSWorld_(ServerLevel world, ServerPlayer except, Iterable<Object> packets) {
		for (ServerPlayer player : world.players())
			if (player != except)
				for (Object packet : packets)
					((ServerPlayer) player).connection.sendPacket((Packet<?>) packet);
	}

	public static void sendNear(Location loc, int distance, Packet<?> packet) {
		sendNMSNear(((CraftWorld) loc.getWorld()).getHandle(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(),
				distance, packet);
	}

	public static void sendNear_(Location loc, int distance, Object packet) {
		sendNMSNear_(((CraftWorld) loc.getWorld()).getHandle(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(),
				distance, packet);
	}

	public static void sendNear(org.bukkit.World world, int x, int y, int z, int distance, Packet<?> packet) {
		sendNMSNear(NMS.to(world), x, y, z, distance, packet);
	}

	public static void sendNear_(org.bukkit.World world, int x, int y, int z, int distance, Object packet) {
		sendNMSNear_(NMS.to(world), x, y, z, distance, packet);
	}

	public static void sendNMSNear(ServerLevel world, int x, int y, int z, int distance, Packet<?> packet) {
		for (ServerPlayer player : world.players()) {
			if (LocationHelper.distance(x, z, player.getX(), player.getZ()) <= distance)
				((ServerPlayer) player).connection.sendPacket(packet);
		}
	}

	public static void sendNMSNear_(ServerLevel world, int x, int y, int z, int distance, Object packet) {
		for (ServerPlayer player : world.players()) {
			if (LocationHelper.distance(x, z, player.getX(), player.getZ()) <= distance)
				((ServerPlayer) player).connection.sendPacket((Packet<?>) packet);
		}
	}

	public static void sendNear(Location loc, int distance, Packet<?>... packets) {
		sendNMSNear(((CraftWorld) loc.getWorld()).getHandle(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(),
				distance, packets);
	}

	public static void sendNear_(Location loc, int distance, Object... packets) {
		sendNMSNear_(((CraftWorld) loc.getWorld()).getHandle(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(),
				distance, packets);
	}

	public static void sendNear(org.bukkit.World world, int x, int y, int z, int distance, Packet<?>... packets) {
		sendNMSNear(NMS.to(world), x, y, z, distance, packets);
	}

	public static void sendNear_(org.bukkit.World world, int x, int y, int z, int distance, Object... packets) {
		sendNMSNear_(NMS.to(world), x, y, z, distance, packets);
	}

	public static void sendNMSNear(ServerLevel world, int x, int y, int z, int distance, Packet<?>... packets) {
		for (ServerPlayer player : world.players()) {
			if (LocationHelper.distance(x, z, player.getX(), player.getZ()) <= distance)
				for (Packet<?> packet : packets)
					((ServerPlayer) player).connection.sendPacket(packet);
		}
	}

	public static void sendNMSNear_(ServerLevel world, int x, int y, int z, int distance, Object... packets) {
		for (ServerPlayer player : world.players()) {
			if (LocationHelper.distance(x, z, player.getX(), player.getZ()) <= distance)
				for (Object packet : packets)
					((ServerPlayer) player).connection.sendPacket((Packet<?>) packet);
		}
	}

	public static void sendNear(Location loc, double distance, Packet<?> packet) {
		sendNMSNear(((CraftWorld) loc.getWorld()).getHandle(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(),
				distance, packet);
	}

	public static void sendNear_(Location loc, double distance, Object packet) {
		sendNMSNear_(((CraftWorld) loc.getWorld()).getHandle(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(),
				distance, packet);
	}

	public static void sendNear(org.bukkit.World world, double x, double y, double z, double distance,
			Packet<?> packet) {
		sendNMSNear(NMS.to(world), x, y, z, distance, packet);
	}

	public static void sendNear_(org.bukkit.World world, double x, double y, double z, double distance, Object packet) {
		sendNMSNear_(NMS.to(world), x, y, z, distance, packet);
	}

	public static void sendNMSNear(ServerLevel world, double x, double y, double z, double distance, Packet<?> packet) {
		for (ServerPlayer player : world.players()) {
			if (LocationHelper.distance(x, z, player.getX(), player.getZ()) <= distance)
				((ServerPlayer) player).connection.sendPacket(packet);
		}
	}

	public static void sendNMSNear_(ServerLevel world, double x, double y, double z, double distance, Object packet) {
		for (ServerPlayer player : world.players()) {
			if (LocationHelper.distance(x, z, player.getX(), player.getZ()) <= distance)
				((ServerPlayer) player).connection.sendPacket((Packet<?>) packet);
		}
	}

	public static void sendNear(Location loc, double distance, Packet<?>... packets) {
		sendNMSNear(((CraftWorld) loc.getWorld()).getHandle(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(),
				distance, packets);
	}

	public static void sendNear_(Location loc, double distance, Object... packets) {
		sendNMSNear_(((CraftWorld) loc.getWorld()).getHandle(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(),
				distance, packets);
	}

	public static void sendNear(org.bukkit.World world, double x, double y, double z, double distance,
			Packet<?>... packets) {
		sendNMSNear(NMS.to(world), x, y, z, distance, packets);
	}

	public static void sendNear_(org.bukkit.World world, double x, double y, double z, double distance,
			Object... packets) {
		sendNMSNear_(NMS.to(world), x, y, z, distance, packets);
	}

	public static void sendNMSNear(ServerLevel world, double x, double y, double z, double distance,
			Packet<?>... packets) {
		for (ServerPlayer player : world.players()) {
			if (LocationHelper.distance(x, z, player.getX(), player.getZ()) <= distance)
				for (Packet<?> packet : packets)
					((ServerPlayer) player).connection.sendPacket(packet);
		}
	}

	public static void sendNMSNear_(ServerLevel world, double x, double y, double z, double distance,
			Object... packets) {
		for (ServerPlayer player : world.players()) {
			if (LocationHelper.distance(x, z, player.getX(), player.getZ()) <= distance)
				for (Object packet : packets)
					((ServerPlayer) player).connection.sendPacket((Packet<?>) packet);
		}
	}

	public static void sendNearby(Location loc, Packet<?> packet) {
		sendNMSNearby(((CraftWorld) loc.getWorld()).getHandle(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(),
				packet);
	}

	public static void sendNearby_(Location loc, Object packet) {
		sendNMSNearby_(((CraftWorld) loc.getWorld()).getHandle(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(),
				packet);
	}

	public static void sendNearby(org.bukkit.World world, int x, int y, int z, Packet<?> packet) {
		sendNMSNearby(NMS.to(world), x, y, z, packet);
	}

	public static void sendNearby_(org.bukkit.World world, int x, int y, int z, Object packet) {
		sendNMSNearby_(NMS.to(world), x, y, z, packet);
	}

	public static void sendNMSNearby(ServerLevel world, int x, int y, int z, Packet<?> packet) {
		for (ServerPlayer player : world.players()) {
			if (LocationHelper.distance(x, z, player.getX(), player.getZ()) <= NEARBY_DISTANCE)
				((ServerPlayer) player).connection.sendPacket(packet);
		}
	}

	public static void sendNMSNearby_(ServerLevel world, int x, int y, int z, Object packet) {
		for (ServerPlayer player : world.players()) {
			if (LocationHelper.distance(x, z, player.getX(), player.getZ()) <= NEARBY_DISTANCE)
				((ServerPlayer) player).connection.sendPacket((Packet<?>) packet);
		}
	}

	public static void sendNearby(org.bukkit.World world, int x, int y, int z, Packet<?>... packets) {
		sendNMSNearby(NMS.to(world), x, y, z, packets);
	}

	public static void sendNearby_(org.bukkit.World world, int x, int y, int z, Object... packets) {
		sendNMSNearby_(NMS.to(world), x, y, z, packets);
	}

	public static void sendNMSNearby(ServerLevel world, int x, int y, int z, Packet<?>... packets) {
		for (ServerPlayer player : world.players()) {
			if (LocationHelper.distance(x, z, player.getX(), player.getZ()) <= NEARBY_DISTANCE)
				for (Packet<?> packet : packets)
					((ServerPlayer) player).connection.sendPacket(packet);
		}
	}

	public static void sendNMSNearby_(ServerLevel world, int x, int y, int z, Object... packets) {
		for (ServerPlayer player : world.players()) {
			if (LocationHelper.distance(x, z, player.getX(), player.getZ()) <= NEARBY_DISTANCE)
				for (Object packet : packets)
					((ServerPlayer) player).connection.sendPacket((Packet<?>) packet);
		}
	}

	public static void sendNearby(org.bukkit.World world, double x, double y, double z, Packet<?> packet) {
		sendNMSNearby(NMS.to(world), x, y, z, packet);
	}

	public static void sendNearby_(org.bukkit.World world, double x, double y, double z, Object packet) {
		sendNMSNearby_(NMS.to(world), x, y, z, packet);
	}

	public static void sendNMSNearby(ServerLevel world, double x, double y, double z, Packet<?> packet) {
		for (ServerPlayer player : world.players()) {
			if (LocationHelper.distance(x, z, player.getX(), player.getZ()) <= NEARBY_DISTANCE)
				((ServerPlayer) player).connection.sendPacket(packet);
		}
	}

	public static void sendNMSNearby_(ServerLevel world, double x, double y, double z, Object packet) {
		for (ServerPlayer player : world.players()) {
			if (LocationHelper.distance(x, z, player.getX(), player.getZ()) <= NEARBY_DISTANCE)
				((ServerPlayer) player).connection.sendPacket((Packet<?>) packet);
		}
	}

	public static void sendNearby(Location loc, Packet<?>... packets) {
		sendNMSNearby(((CraftWorld) loc.getWorld()).getHandle(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(),
				packets);
	}

	public static void sendNearby_(Location loc, Object... packets) {
		sendNMSNearby_(((CraftWorld) loc.getWorld()).getHandle(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(),
				packets);
	}

	public static void sendNearby(org.bukkit.World world, double x, double y, double z, Packet<?>... packets) {
		sendNMSNearby(NMS.to(world), x, y, z, packets);
	}

	public static void sendNearby_(org.bukkit.World world, double x, double y, double z, Object... packets) {
		sendNMSNearby_(NMS.to(world), x, y, z, packets);
	}

	public static void sendNMSNearby(ServerLevel world, double x, double y, double z, Packet<?>... packets) {
		for (ServerPlayer player : world.players()) {
			if (LocationHelper.distance(x, z, player.getX(), player.getZ()) <= NEARBY_DISTANCE)
				for (Packet<?> packet : packets)
					((ServerPlayer) player).connection.sendPacket(packet);
		}
	}

	public static void sendNMSNearby_(ServerLevel world, double x, double y, double z, Object... packets) {
		for (ServerPlayer player : world.players()) {
			if (LocationHelper.distance(x, z, player.getX(), player.getZ()) <= NEARBY_DISTANCE)
				for (Object packet : packets)
					((ServerPlayer) player).connection.sendPacket((Packet<?>) packet);
		}
	}
}
