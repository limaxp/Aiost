package com.pm.aiost.misc.packet.object.objects;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.pm.aiost.collection.list.UnorderedIdentityArrayList;
import com.pm.aiost.entity.dataWatcher.EmptyDataWatcher;
import com.pm.aiost.misc.packet.PacketFactory;
import com.pm.aiost.misc.packet.PacketSender;
import com.pm.aiost.misc.packet.object.PacketObjectType;
import com.pm.aiost.misc.packet.object.PacketObjectTypes;
import com.pm.aiost.misc.utils.LocationHelper;
import com.pm.aiost.misc.utils.nbt.custom.INBTTagCompound;
import com.pm.aiost.misc.utils.nms.NMS;
import com.pm.aiost.server.world.ServerWorld;

public class TextDisplay extends Hologram {

	protected EmptyDataWatcher[] dataWatchers;
	private final List<Player> playerList = new UnorderedIdentityArrayList<Player>();

	public TextDisplay(ServerWorld world) {
		super(world);
	}

	public TextDisplay(ServerWorld world, String text) {
		super(world, text);
		dataWatchers = new EmptyDataWatcher[1];
	}

	public TextDisplay(ServerWorld world, String[] text) {
		super(world, text);
		dataWatchers = new EmptyDataWatcher[text.length];
	}

	public TextDisplay(ServerWorld world, Collection<String> text) {
		super(world, text);
		dataWatchers = new EmptyDataWatcher[text.size()];
	}

	@Override
	public void spawn() {
		for (Player player : world.getPlayer()) {
			Location loc = player.getLocation();
			if (LocationHelper.distance(x, z, loc.getBlockX(), loc.getBlockZ()) <= PACKET_OBJECT_VISIBILE_RANGE)
				playerList.add(player);
		}
		PacketSender.send_(playerList, spawnPackets = createSpawnPackets());
	}

	@Override
	public void show(Player player) {
		playerList.add(player);
		super.show(player);
	}

	@Override
	public void spawn(Player player) {
		playerList.add(player);
		super.spawn(player);
	}

	@Override
	public void remove() {
		world.removePacketObject(x, y, z);
		PacketSender.send_(playerList, createRemovePacket());
		playerList.clear();
	}

	@Override
	public void hide(Player player) {
		playerList.remove(player);
		super.hide(player);
	}

	@Override
	public void load(INBTTagCompound nbt) {
		super.load(nbt);
		dataWatchers = new EmptyDataWatcher[text.length];
	}

	@Override
	public Object createMetaDataPacket(int index, String text) {
		EmptyDataWatcher dataWatcher = createDataWatcher(text);
		dataWatchers[index] = dataWatcher;
		return super.createMetaDataPacket(index, text);
	}

	public void setText(int index, String text) {
		this.text[index] = text;
		dataWatchers[index].set(NAME_WATCHER, Optional.ofNullable(NMS.createChatMessage(text)));
		updateText(index);
	}

	public void setText(String[] text) {
		this.text = text;
		for (int i = 0; i < text.length; i++)
			dataWatchers[i].set(NAME_WATCHER, Optional.ofNullable(NMS.createChatMessage(text[i])));
		updateText();
	}

	public void setText(int index, String[] text) {
		this.text = text;
		int j = index;
		for (int i = 0; i < text.length; i++)
			dataWatchers[j++].set(NAME_WATCHER, Optional.ofNullable(NMS.createChatMessage(text[i])));
		updateFrom(index);
	}

	public void updateFrom(int index) {
		int length = dataWatchers.length;
		Object[] packets = new Object[length];
		for (int i = index; i < length; i++)
			packets[i] = PacketFactory.packetEntityMetadata(id + i, dataWatchers[i], true);
		PacketSender.send_(playerList, packets);
	}

	public void updateText() {
		updateFrom(0);
	}

	public void updateText(int index) {
		PacketSender.send(playerList, PacketFactory.packetEntityMetadata(id + index, dataWatchers[index], true));
	}

	@Override
	public PacketObjectType<?> getPacketObjectType() {
		return PacketObjectTypes.TEXT_DISPLAY;
	}
}

//public void addText(String text) {
//int index = spawnPackets.length;
//spawnPackets = Arrays.copyOf(spawnPackets, index + 1);
//removePackets = Arrays.copyOf(removePackets, index + 1);
//Packet<?> spawnPacket = createSpawnPacket(text, index);
//spawnPackets[index] = spawnPacket;
//removePackets[index] = PacketFactory.packetEntityDestroy(id + index);
//PacketSender.send(world.world, x, y, z, PACKET_OBJECT_VISIBILE_RANGE, spawnPacket);
//}
//
//public void addText(String[] text) {
//int length = text.length;
//int index = spawnPackets.length;
//spawnPackets = Arrays.copyOf(spawnPackets, index + length);
//removePackets = Arrays.copyOf(removePackets, index + length);
//
//Packet<?>[] newPackets = new Packet<?>[length];
//for (int i = 0; i < length; i++) {
//	int indexI = index + i;
//	Packet<?> spawnPacket = createSpawnPacket(text[i], indexI);
//	newPackets[i] = spawnPacket;
//	spawnPackets[indexI] = spawnPacket;
//	removePackets[indexI] = PacketFactory.packetEntityDestroy(id + indexI);
//}
//PacketSender.send(world.world, x, y, z, PACKET_OBJECT_VISIBILE_RANGE, newPackets);
//}
