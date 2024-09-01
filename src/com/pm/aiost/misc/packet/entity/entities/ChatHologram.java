package com.pm.aiost.misc.packet.entity.entities;

import java.util.Collection;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.pm.aiost.misc.packet.PacketFactory;
import com.pm.aiost.misc.packet.PacketSender;
import com.pm.aiost.world.ServerWorld;

import net.minecraft.network.protocol.Packet;

public class ChatHologram extends Hologram {

	protected final List<Player> player;

	public ChatHologram(ServerWorld world, List<Player> player) {
		super(world);
		this.player = player;
	}

	public ChatHologram(ServerWorld world, String text, List<Player> player) {
		super(world, text);
		this.player = player;
	}

	public ChatHologram(ServerWorld world, String[] text, List<Player> player) {
		super(world, text);
		this.player = player;
	}

	public ChatHologram(ServerWorld world, Collection<String> text, List<Player> player) {
		super(world, text);
		this.player = player;
	}

	@Override
	public void spawn() {
		PacketSender.send(player, spawnPackets = createSpawnPackets());
	}

	@Override
	public void remove() {
		PacketSender.send(player, createRemovePacket());
	}

	public void teleport(Location loc, boolean onGround) {
		teleport(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch(), onGround);
	}

	public void teleport(double x, double y, double z, float yaw, float pitch, boolean onGround) {
		setPositionRotation(x, y, z, yaw, pitch);
		int length = text.length;
		Packet<?>[] teleportPackets = new Packet[length];
		for (int i = 0; i < length; i++)
			teleportPackets[i] = PacketFactory.packetEntityTeleport(id + i, x, y - (Hologram.ABS * i), z, yaw, pitch,
					onGround);
		PacketSender.send(player, teleportPackets);
	}
}
