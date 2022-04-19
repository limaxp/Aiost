package com.pm.aiost.misc.packet;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.pm.aiost.misc.utils.LocationHelper;
import com.pm.aiost.misc.utils.nms.NMS;

import net.minecraft.server.v1_15_R1.EntityHuman;
import net.minecraft.server.v1_15_R1.EntityPlayer;
import net.minecraft.server.v1_15_R1.Packet;
import net.minecraft.server.v1_15_R1.PlayerConnection;
import net.minecraft.server.v1_15_R1.World;

public class PacketSender {

	public static final double NEARBY_DISTANCE = 64;

	public static void send(Player player, Packet<?> packet) {
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
	}

	public static void send_(Player player, Object packet) {
		((CraftPlayer) player).getHandle().playerConnection.sendPacket((Packet<?>) packet);
	}

	public static void sendNMS(EntityPlayer player, Packet<?> packet) {
		player.playerConnection.sendPacket(packet);
	}

	public static void sendNMS_(EntityPlayer player, Object packet) {
		player.playerConnection.sendPacket((Packet<?>) packet);
	}

	public static void sendReflected(Object player, Packet<?> packet) {
		((EntityPlayer) player).playerConnection.sendPacket(packet);
	}

	public static void sendReflected_(Object player, Object packet) {
		((EntityPlayer) player).playerConnection.sendPacket((Packet<?>) packet);
	}

	public static void send(Player player, Packet<?>... packets) {
		PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
		for (Packet<?> packet : packets)
			connection.sendPacket(packet);
	}

	public static void send_(Player player, Object... packets) {
		PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
		for (Object packet : packets)
			connection.sendPacket((Packet<?>) packet);
	}

	public static void sendNMS(EntityPlayer player, Packet<?>... packets) {
		PlayerConnection connection = player.playerConnection;
		for (Packet<?> packet : packets)
			connection.sendPacket(packet);
	}

	public static void sendNMS_(EntityPlayer player, Object... packets) {
		PlayerConnection connection = player.playerConnection;
		for (Object packet : packets)
			connection.sendPacket((Packet<?>) packet);
	}

	public static void sendReflected(Object player, Packet<?>... packets) {
		PlayerConnection connection = ((EntityPlayer) player).playerConnection;
		for (Packet<?> packet : packets)
			connection.sendPacket(packet);
	}

	public static void sendReflected_(Object player, Object... packets) {
		PlayerConnection connection = ((EntityPlayer) player).playerConnection;
		for (Object packet : packets)
			connection.sendPacket((Packet<?>) packet);
	}

	public static void send(Player[] player, Packet<?> packet) {
		for (Player p : player) {
			PlayerConnection connection = ((CraftPlayer) p).getHandle().playerConnection;
			connection.sendPacket(packet);
		}
	}

	public static void send_(Player[] player, Object packet) {
		for (Player p : player) {
			PlayerConnection connection = ((CraftPlayer) p).getHandle().playerConnection;
			connection.sendPacket((Packet<?>) packet);
		}
	}

	public static void sendNMS(EntityPlayer[] player, Packet<?> packet) {
		for (EntityPlayer p : player) {
			PlayerConnection connection = p.playerConnection;
			connection.sendPacket(packet);
		}
	}

	public static void sendNMS_(EntityPlayer[] player, Object packet) {
		for (EntityPlayer p : player) {
			PlayerConnection connection = p.playerConnection;
			connection.sendPacket((Packet<?>) packet);
		}
	}

	public static void sendReflected(Object[] player, Packet<?> packet) {
		for (Object p : player) {
			PlayerConnection connection = ((EntityPlayer) p).playerConnection;
			connection.sendPacket(packet);
		}
	}

	public static void sendReflected_(Object[] player, Object packet) {
		for (Object p : player) {
			PlayerConnection connection = ((EntityPlayer) p).playerConnection;
			connection.sendPacket((Packet<?>) packet);
		}
	}

	public static void send(Player player, Iterable<Packet<?>> packets) {
		PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
		for (Packet<?> packet : packets)
			connection.sendPacket(packet);
	}

	public static void send_(Player player, Iterable<Object> packets) {
		PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
		for (Object packet : packets)
			connection.sendPacket((Packet<?>) packet);
	}

	public static void sendNMS(EntityPlayer player, Iterable<Packet<?>> packets) {
		PlayerConnection connection = player.playerConnection;
		for (Packet<?> packet : packets)
			connection.sendPacket(packet);
	}

	public static void sendNMS_(EntityPlayer player, Iterable<Object> packets) {
		PlayerConnection connection = player.playerConnection;
		for (Object packet : packets)
			connection.sendPacket((Packet<?>) packet);
	}

	public static void sendReflected(Object player, Iterable<Packet<?>> packets) {
		PlayerConnection connection = ((EntityPlayer) player).playerConnection;
		for (Packet<?> packet : packets)
			connection.sendPacket(packet);
	}

	public static void sendReflected_(Object player, Iterable<Object> packets) {
		PlayerConnection connection = ((EntityPlayer) player).playerConnection;
		for (Object packet : packets)
			connection.sendPacket((Packet<?>) packet);
	}

	public static void send(Iterable<Player> player, Packet<?> packet) {
		for (Player p : player)
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
	}

	public static void send_(Iterable<Player> player, Object packet) {
		for (Player p : player)
			((CraftPlayer) p).getHandle().playerConnection.sendPacket((Packet<?>) packet);
	}

	public static void sendNMS(Iterable<EntityPlayer> player, Packet<?> packet) {
		for (EntityPlayer p : player)
			p.playerConnection.sendPacket(packet);
	}

	public static void sendNMS_(Iterable<EntityPlayer> player, Object packet) {
		for (EntityPlayer p : player)
			p.playerConnection.sendPacket((Packet<?>) packet);
	}

	public static void sendReflected(Iterable<Object> player, Packet<?> packet) {
		for (Object p : player) {
			PlayerConnection connection = ((EntityPlayer) p).playerConnection;
			connection.sendPacket(packet);
		}
	}

	public static void sendReflected_(Iterable<Object> player, Object packet) {
		for (Object p : player) {
			PlayerConnection connection = ((EntityPlayer) p).playerConnection;
			connection.sendPacket((Packet<?>) packet);
		}
	}

	public static void send(Player[] player, Packet<?>... packets) {
		for (Player p : player) {
			PlayerConnection connection = ((CraftPlayer) p).getHandle().playerConnection;
			for (Packet<?> packet : packets)
				connection.sendPacket(packet);
		}
	}

	public static void send_(Player[] player, Object... packets) {
		for (Player p : player) {
			PlayerConnection connection = ((CraftPlayer) p).getHandle().playerConnection;
			for (Object packet : packets)
				connection.sendPacket((Packet<?>) packet);
		}
	}

	public static void sendNMS(EntityPlayer[] player, Packet<?>... packets) {
		for (EntityPlayer p : player) {
			PlayerConnection connection = p.playerConnection;
			for (Packet<?> packet : packets)
				connection.sendPacket(packet);
		}
	}

	public static void sendNMS_(EntityPlayer[] player, Object... packets) {
		for (EntityPlayer p : player) {
			PlayerConnection connection = p.playerConnection;
			for (Object packet : packets)
				connection.sendPacket((Packet<?>) packet);
		}
	}

	public static void sendReflected(Object[] player, Packet<?>... packets) {
		for (Object p : player) {
			PlayerConnection connection = ((EntityPlayer) p).playerConnection;
			for (Packet<?> packet : packets)
				connection.sendPacket(packet);
		}
	}

	public static void sendReflected_(Object[] player, Object... packets) {
		for (Object p : player) {
			PlayerConnection connection = ((EntityPlayer) p).playerConnection;
			for (Object packet : packets)
				connection.sendPacket((Packet<?>) packet);
		}
	}

	public static void send(Iterable<Player> player, Packet<?>... packets) {
		for (Player p : player)
			for (Packet<?> packet : packets)
				((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
	}

	public static void send_(Iterable<Player> player, Object... packets) {
		for (Player p : player)
			for (Object packet : packets)
				((CraftPlayer) p).getHandle().playerConnection.sendPacket((Packet<?>) packet);
	}

	public static void sendNMS(Iterable<EntityPlayer> player, Packet<?>... packets) {
		for (EntityPlayer p : player) {
			PlayerConnection connection = p.playerConnection;
			for (Packet<?> packet : packets)
				connection.sendPacket(packet);
		}
	}

	public static void sendNMS_(Iterable<EntityPlayer> player, Object... packets) {
		for (EntityPlayer p : player) {
			PlayerConnection connection = p.playerConnection;
			for (Object packet : packets)
				connection.sendPacket((Packet<?>) packet);
		}
	}

	public static void sendReflected(Iterable<Object> player, Packet<?>... packets) {
		for (Object p : player) {
			PlayerConnection connection = ((EntityPlayer) p).playerConnection;
			for (Packet<?> packet : packets)
				connection.sendPacket(packet);
		}
	}

	public static void sendReflected_(Iterable<Object> player, Object... packets) {
		for (Object p : player) {
			PlayerConnection connection = ((EntityPlayer) p).playerConnection;
			for (Object packet : packets)
				connection.sendPacket((Packet<?>) packet);
		}
	}

	public static void send(Iterable<Player> player, Iterable<Packet<?>> packets) {
		for (Player p : player)
			for (Packet<?> packet : packets)
				((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
	}

	public static void send_(Iterable<Player> player, Iterable<Object> packets) {
		for (Player p : player)
			for (Object packet : packets)
				((CraftPlayer) p).getHandle().playerConnection.sendPacket((Packet<?>) packet);
	}

	public static void sendNMS(Iterable<EntityPlayer> player, Iterable<Packet<?>> packets) {
		for (EntityPlayer p : player) {
			PlayerConnection connection = p.playerConnection;
			for (Packet<?> packet : packets)
				connection.sendPacket(packet);
		}
	}

	public static void sendNMS_(Iterable<EntityPlayer> player, Iterable<Object> packets) {
		for (EntityPlayer p : player) {
			PlayerConnection connection = p.playerConnection;
			for (Object packet : packets)
				connection.sendPacket((Packet<?>) packet);
		}
	}

	public static void sendReflected(Iterable<Object> player, Iterable<Packet<?>> packets) {
		for (Object p : player) {
			PlayerConnection connection = ((EntityPlayer) p).playerConnection;
			for (Packet<?> packet : packets)
				connection.sendPacket(packet);
		}
	}

	public static void sendReflected_(Iterable<Object> player, Iterable<Object> packets) {
		for (Object p : player) {
			PlayerConnection connection = ((EntityPlayer) p).playerConnection;
			for (Object packet : packets)
				connection.sendPacket((Packet<?>) packet);
		}
	}

	public static void sendAll(Packet<?> packet) {
		for (EntityPlayer player : NMS.getMinecraftServer().getPlayerList().players)
			player.playerConnection.sendPacket(packet);
	}

	public static void sendAll_(Object packet) {
		for (EntityPlayer player : NMS.getMinecraftServer().getPlayerList().players)
			player.playerConnection.sendPacket((Packet<?>) packet);
	}

	public static void sendAll(Packet<?>... packets) {
		for (EntityPlayer player : NMS.getMinecraftServer().getPlayerList().players)
			for (Packet<?> packet : packets)
				player.playerConnection.sendPacket(packet);
	}

	public static void sendAll_(Object... packets) {
		for (EntityPlayer player : NMS.getMinecraftServer().getPlayerList().players)
			for (Object packet : packets)
				player.playerConnection.sendPacket((Packet<?>) packet);
	}

	public static void sendWorld(org.bukkit.World world, Packet<?> packet) {
		sendNMSWorld(((CraftWorld) world).getHandle(), packet);
	}

	public static void sendWorld_(org.bukkit.World world, Object packet) {
		sendNMSWorld_(((CraftWorld) world).getHandle(), packet);
	}

	public static void sendNMSWorld(World world, Packet<?> packet) {
		for (EntityHuman player : world.getPlayers())
			((EntityPlayer) player).playerConnection.sendPacket(packet);
	}

	public static void sendNMSWorld_(World world, Object packet) {
		for (EntityHuman player : world.getPlayers())
			((EntityPlayer) player).playerConnection.sendPacket((Packet<?>) packet);
	}

	public static void sendWorld(org.bukkit.World world, Player except, Packet<?> packet) {
		sendNMSWorld(((CraftWorld) world).getHandle(), ((CraftPlayer) except).getHandle(), packet);
	}

	public static void sendWorld_(org.bukkit.World world, Player except, Object packet) {
		sendNMSWorld_(((CraftWorld) world).getHandle(), ((CraftPlayer) except).getHandle(), packet);
	}

	public static void sendNMSWorld(World world, EntityPlayer except, Packet<?> packet) {
		for (EntityHuman player : world.getPlayers())
			if (player != except)
				((EntityPlayer) player).playerConnection.sendPacket(packet);
	}

	public static void sendNMSWorld_(World world, EntityPlayer except, Object packet) {
		for (EntityHuman player : world.getPlayers())
			if (player != except)
				((EntityPlayer) player).playerConnection.sendPacket((Packet<?>) packet);
	}

	public static void sendWorld(org.bukkit.World world, Packet<?>... packets) {
		sendNMSWorld(((CraftWorld) world).getHandle(), packets);
	}

	public static void sendWorld_(org.bukkit.World world, Object... packets) {
		sendNMSWorld_(((CraftWorld) world).getHandle(), packets);
	}

	public static void sendWorld(org.bukkit.World world, Iterable<Packet<?>> packets) {
		sendNMSWorld(((CraftWorld) world).getHandle(), packets);
	}

	public static void sendWorld_(org.bukkit.World world, Iterable<Object> packets) {
		sendNMSWorld_(((CraftWorld) world).getHandle(), packets);
	}

	public static void sendNMSWorld(World world, Packet<?>... packets) {
		for (EntityHuman player : world.getPlayers())
			for (Packet<?> packet : packets)
				((EntityPlayer) player).playerConnection.sendPacket(packet);
	}

	public static void sendNMSWorld_(World world, Object... packets) {
		for (EntityHuman player : world.getPlayers())
			for (Object packet : packets)
				((EntityPlayer) player).playerConnection.sendPacket((Packet<?>) packet);
	}

	public static void sendNMSWorld(World world, Iterable<Packet<?>> packets) {
		for (EntityHuman player : world.getPlayers())
			for (Packet<?> packet : packets)
				((EntityPlayer) player).playerConnection.sendPacket(packet);
	}

	public static void sendNMSWorld_(World world, Iterable<Object> packets) {
		for (EntityHuman player : world.getPlayers())
			for (Object packet : packets)
				((EntityPlayer) player).playerConnection.sendPacket((Packet<?>) packet);
	}

	public static void sendWorld(org.bukkit.World world, Player except, Packet<?>... packets) {
		sendNMSWorld(((CraftWorld) world).getHandle(), ((CraftPlayer) except).getHandle(), packets);
	}

	public static void sendWorld_(org.bukkit.World world, Player except, Object... packets) {
		sendNMSWorld_(((CraftWorld) world).getHandle(), ((CraftPlayer) except).getHandle(), packets);
	}

	public static void sendWorld(org.bukkit.World world, Player except, Iterable<Packet<?>> packets) {
		sendNMSWorld(((CraftWorld) world).getHandle(), ((CraftPlayer) except).getHandle(), packets);
	}

	public static void sendWorld_(org.bukkit.World world, Player except, Iterable<Object> packets) {
		sendNMSWorld_(((CraftWorld) world).getHandle(), ((CraftPlayer) except).getHandle(), packets);
	}

	public static void sendNMSWorld(World world, EntityPlayer except, Packet<?>... packets) {
		for (EntityHuman player : world.getPlayers())
			if (player != except)
				for (Packet<?> packet : packets)
					((EntityPlayer) player).playerConnection.sendPacket(packet);
	}

	public static void sendNMSWorld_(World world, EntityPlayer except, Object... packets) {
		for (EntityHuman player : world.getPlayers())
			if (player != except)
				for (Object packet : packets)
					((EntityPlayer) player).playerConnection.sendPacket((Packet<?>) packet);
	}

	public static void sendNMSWorld(World world, EntityPlayer except, Iterable<Packet<?>> packets) {
		for (EntityHuman player : world.getPlayers())
			if (player != except)
				for (Packet<?> packet : packets)
					((EntityPlayer) player).playerConnection.sendPacket(packet);
	}

	public static void sendNMSWorld_(World world, EntityPlayer except, Iterable<Object> packets) {
		for (EntityHuman player : world.getPlayers())
			if (player != except)
				for (Object packet : packets)
					((EntityPlayer) player).playerConnection.sendPacket((Packet<?>) packet);
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
		sendNMSNear(((CraftWorld) world).getHandle(), x, y, z, distance, packet);
	}

	public static void sendNear_(org.bukkit.World world, int x, int y, int z, int distance, Object packet) {
		sendNMSNear_(((CraftWorld) world).getHandle(), x, y, z, distance, packet);
	}

	public static void sendNMSNear(World world, int x, int y, int z, int distance, Packet<?> packet) {
		for (EntityHuman player : world.getPlayers()) {
			if (LocationHelper.distance(x, z, player.locX(), player.locZ()) <= distance)
				((EntityPlayer) player).playerConnection.sendPacket(packet);
		}
	}

	public static void sendNMSNear_(World world, int x, int y, int z, int distance, Object packet) {
		for (EntityHuman player : world.getPlayers()) {
			if (LocationHelper.distance(x, z, player.locX(), player.locZ()) <= distance)
				((EntityPlayer) player).playerConnection.sendPacket((Packet<?>) packet);
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
		sendNMSNear(((CraftWorld) world).getHandle(), x, y, z, distance, packets);
	}

	public static void sendNear_(org.bukkit.World world, int x, int y, int z, int distance, Object... packets) {
		sendNMSNear_(((CraftWorld) world).getHandle(), x, y, z, distance, packets);
	}

	public static void sendNMSNear(World world, int x, int y, int z, int distance, Packet<?>... packets) {
		for (EntityHuman player : world.getPlayers()) {
			if (LocationHelper.distance(x, z, player.locX(), player.locZ()) <= distance)
				for (Packet<?> packet : packets)
					((EntityPlayer) player).playerConnection.sendPacket(packet);
		}
	}

	public static void sendNMSNear_(World world, int x, int y, int z, int distance, Object... packets) {
		for (EntityHuman player : world.getPlayers()) {
			if (LocationHelper.distance(x, z, player.locX(), player.locZ()) <= distance)
				for (Object packet : packets)
					((EntityPlayer) player).playerConnection.sendPacket((Packet<?>) packet);
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
		sendNMSNear(((CraftWorld) world).getHandle(), x, y, z, distance, packet);
	}

	public static void sendNear_(org.bukkit.World world, double x, double y, double z, double distance, Object packet) {
		sendNMSNear_(((CraftWorld) world).getHandle(), x, y, z, distance, packet);
	}

	public static void sendNMSNear(World world, double x, double y, double z, double distance, Packet<?> packet) {
		for (EntityHuman player : world.getPlayers()) {
			if (LocationHelper.distance(x, z, player.locX(), player.locZ()) <= distance)
				((EntityPlayer) player).playerConnection.sendPacket(packet);
		}
	}

	public static void sendNMSNear_(World world, double x, double y, double z, double distance, Object packet) {
		for (EntityHuman player : world.getPlayers()) {
			if (LocationHelper.distance(x, z, player.locX(), player.locZ()) <= distance)
				((EntityPlayer) player).playerConnection.sendPacket((Packet<?>) packet);
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
		sendNMSNear(((CraftWorld) world).getHandle(), x, y, z, distance, packets);
	}

	public static void sendNear_(org.bukkit.World world, double x, double y, double z, double distance,
			Object... packets) {
		sendNMSNear_(((CraftWorld) world).getHandle(), x, y, z, distance, packets);
	}

	public static void sendNMSNear(World world, double x, double y, double z, double distance, Packet<?>... packets) {
		for (EntityHuman player : world.getPlayers()) {
			if (LocationHelper.distance(x, z, player.locX(), player.locZ()) <= distance)
				for (Packet<?> packet : packets)
					((EntityPlayer) player).playerConnection.sendPacket(packet);
		}
	}

	public static void sendNMSNear_(World world, double x, double y, double z, double distance, Object... packets) {
		for (EntityHuman player : world.getPlayers()) {
			if (LocationHelper.distance(x, z, player.locX(), player.locZ()) <= distance)
				for (Object packet : packets)
					((EntityPlayer) player).playerConnection.sendPacket((Packet<?>) packet);
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
		sendNMSNearby(((CraftWorld) world).getHandle(), x, y, z, packet);
	}

	public static void sendNearby_(org.bukkit.World world, int x, int y, int z, Object packet) {
		sendNMSNearby_(((CraftWorld) world).getHandle(), x, y, z, packet);
	}

	public static void sendNMSNearby(World world, int x, int y, int z, Packet<?> packet) {
		for (EntityHuman player : world.getPlayers()) {
			if (LocationHelper.distance(x, z, player.locX(), player.locZ()) <= NEARBY_DISTANCE)
				((EntityPlayer) player).playerConnection.sendPacket(packet);
		}
	}

	public static void sendNMSNearby_(World world, int x, int y, int z, Object packet) {
		for (EntityHuman player : world.getPlayers()) {
			if (LocationHelper.distance(x, z, player.locX(), player.locZ()) <= NEARBY_DISTANCE)
				((EntityPlayer) player).playerConnection.sendPacket((Packet<?>) packet);
		}
	}

	public static void sendNearby(org.bukkit.World world, int x, int y, int z, Packet<?>... packets) {
		sendNMSNearby(((CraftWorld) world).getHandle(), x, y, z, packets);
	}

	public static void sendNearby_(org.bukkit.World world, int x, int y, int z, Object... packets) {
		sendNMSNearby_(((CraftWorld) world).getHandle(), x, y, z, packets);
	}

	public static void sendNMSNearby(World world, int x, int y, int z, Packet<?>... packets) {
		for (EntityHuman player : world.getPlayers()) {
			if (LocationHelper.distance(x, z, player.locX(), player.locZ()) <= NEARBY_DISTANCE)
				for (Packet<?> packet : packets)
					((EntityPlayer) player).playerConnection.sendPacket(packet);
		}
	}

	public static void sendNMSNearby_(World world, int x, int y, int z, Object... packets) {
		for (EntityHuman player : world.getPlayers()) {
			if (LocationHelper.distance(x, z, player.locX(), player.locZ()) <= NEARBY_DISTANCE)
				for (Object packet : packets)
					((EntityPlayer) player).playerConnection.sendPacket((Packet<?>) packet);
		}
	}

	public static void sendNearby(org.bukkit.World world, double x, double y, double z, Packet<?> packet) {
		sendNMSNearby(((CraftWorld) world).getHandle(), x, y, z, packet);
	}

	public static void sendNearby_(org.bukkit.World world, double x, double y, double z, Object packet) {
		sendNMSNearby_(((CraftWorld) world).getHandle(), x, y, z, packet);
	}

	public static void sendNMSNearby(World world, double x, double y, double z, Packet<?> packet) {
		for (EntityHuman player : world.getPlayers()) {
			if (LocationHelper.distance(x, z, player.locX(), player.locZ()) <= NEARBY_DISTANCE)
				((EntityPlayer) player).playerConnection.sendPacket(packet);
		}
	}

	public static void sendNMSNearby_(World world, double x, double y, double z, Object packet) {
		for (EntityHuman player : world.getPlayers()) {
			if (LocationHelper.distance(x, z, player.locX(), player.locZ()) <= NEARBY_DISTANCE)
				((EntityPlayer) player).playerConnection.sendPacket((Packet<?>) packet);
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
		sendNMSNearby(((CraftWorld) world).getHandle(), x, y, z, packets);
	}

	public static void sendNearby_(org.bukkit.World world, double x, double y, double z, Object... packets) {
		sendNMSNearby_(((CraftWorld) world).getHandle(), x, y, z, packets);
	}

	public static void sendNMSNearby(World world, double x, double y, double z, Packet<?>... packets) {
		for (EntityHuman player : world.getPlayers()) {
			if (LocationHelper.distance(x, z, player.locX(), player.locZ()) <= NEARBY_DISTANCE)
				for (Packet<?> packet : packets)
					((EntityPlayer) player).playerConnection.sendPacket(packet);
		}
	}

	public static void sendNMSNearby_(World world, double x, double y, double z, Object... packets) {
		for (EntityHuman player : world.getPlayers()) {
			if (LocationHelper.distance(x, z, player.locX(), player.locZ()) <= NEARBY_DISTANCE)
				for (Object packet : packets)
					((EntityPlayer) player).playerConnection.sendPacket((Packet<?>) packet);
		}
	}
}
