package com.pm.aiost.misc.packet.object.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bukkit.entity.Player;

import com.pm.aiost.misc.nms.NMS;
import com.pm.aiost.misc.packet.PacketFactory;
import com.pm.aiost.misc.packet.object.PacketObjectType;
import com.pm.aiost.misc.packet.object.PacketObjectTypes;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.server.world.ServerWorld;

import net.minecraft.network.syncher.SynchedEntityData.DataValue;

public class Marker extends SimpleText {

	public Marker(ServerWorld world) {
		super(world);
	}

	public Marker(ServerWorld world, String name, int index) {
		super(world);
		this.text = name + " " + index;
	}

	@Override
	public void spawn() {
		if (world.getEventHandler().markerVisible())
			super.spawn();
	}

	@Override
	public void spawn(Player player) {
		if (world.getEventHandler().markerVisible())
			super.spawn(player);
	}

	@Override
	public void onPlayerAttack(ServerPlayer serverPlayer) {
		int lastSpaceIndex = text.lastIndexOf(" ");
		world.removeMarker(text.substring(0, lastSpaceIndex), Integer.parseInt(text.substring(lastSpaceIndex + 1)));
		remove();
	}

	@Override
	public String getName() {
		return text.substring(0, text.lastIndexOf(" "));
	}

	public int getIndex() {
		return Integer.parseInt(text.substring(text.lastIndexOf(" ") + 1));
	}

	@Override
	public Object createMetaDataPacket() {
		return PacketFactory.packetEntityMetadata(id, createDataWatcher(text));
	}

	public static List<DataValue<?>> createDataWatcher(String text) {
		List<DataValue<?>> dataWatcher = new ArrayList<DataValue<?>>();
		dataWatcher.add(DataValue.create(Hologram.FLAG_WATCHER, (byte) (0x20 + 0x40)));
		dataWatcher.add(DataValue.create(Hologram.NAME_WATCHER, Optional.ofNullable(NMS.createChatComponent(text))));
		dataWatcher.add(DataValue.create(Hologram.NAME_VISIBLE_WATCHER, true));
		return dataWatcher;
	}

	@Override
	public PacketObjectType<?> getPacketObjectType() {
		return PacketObjectTypes.MARKER;
	}
}
