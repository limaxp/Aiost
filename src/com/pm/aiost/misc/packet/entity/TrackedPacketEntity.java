package com.pm.aiost.misc.packet.entity;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.pm.aiost.collection.list.UnorderedIdentityArrayList;
import com.pm.aiost.misc.utils.LocationHelper;
import com.pm.aiost.server.world.ServerWorld;

public abstract class TrackedPacketEntity extends PacketEntity {

	private final List<Player> trackedPlayer;

	public TrackedPacketEntity(ServerWorld world) {
		super(world);
		trackedPlayer = new UnorderedIdentityArrayList<Player>();
	}

	@Override
	public void spawn() {
		for (Player player : world.getPlayer()) {
			Location loc = player.getLocation();
			if (LocationHelper.distance(x, z, loc.getX(), loc.getZ()) <= PACKET_OBJECT_VISIBILE_RANGE)
				trackedPlayer.add(player);
		}
	}

	@Override
	public void spawn(Player player) {
		trackedPlayer.add(player);
	}

	@Override
	public void remove() {
		trackedPlayer.clear();
	}

	@Override
	public void hide(Player player) {
		trackedPlayer.remove(player);
	}

	@Override
	public Object createSpawnPacket() {
		throw new UnsupportedOperationException();
	}

	public List<Player> getTrackedPlayer() {
		return trackedPlayer;
	}
}