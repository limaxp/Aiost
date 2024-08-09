package com.pm.aiost.misc.packet.disguise;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;

import com.google.common.base.Supplier;
import com.pm.aiost.misc.nms.NMS;
import com.pm.aiost.misc.packet.PacketFactory;
import com.pm.aiost.misc.packet.PacketSender;
import com.pm.aiost.misc.packet.disguise.disguises.DisguiseBlock;
import com.pm.aiost.misc.packet.disguise.disguises.DisguiseEntityLiving;
import com.pm.aiost.misc.packet.disguise.disguises.DisguiseFurniture;
import com.pm.aiost.misc.packet.disguise.disguises.DisguisePlayer;
import com.pm.aiost.player.ServerPlayer;

import net.minecraft.server.network.ServerPlayerConnection;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class DisguiseManager {

	private static final Map<String, Supplier<Disguise>> NAME_MAP = new HashMap<String, Supplier<Disguise>>();

	static {
		register("entity_living", DisguiseEntityLiving::new);
		register("falling_block", DisguiseBlock::new);
		register("furniture", DisguiseFurniture::new);
		register("player", DisguisePlayer::new);
	}

	public static void register(String name, Supplier<Disguise> constructor) {
		NAME_MAP.put(name.toUpperCase(), constructor);
	}

	public static Disguise create(String name) {
		return NAME_MAP.get(name.toUpperCase()).get();
	}

	public static Disguise create(ConfigurationSection section) {
		Disguise disguise = create(section.getString("type"));
		disguise.load(section);
		return disguise;
	}

	public static void setDisguise(ServerPlayer player, Disguise disguise) {
		player.setDisguise(disguise);
	}

	public static void setDisguise(LivingEntity entity, Disguise disguise) {
		setDisguise(entity, disguise, getDisguise(entity));
		// TODO save disguise!
	}

	public static void setDisguise(LivingEntity entity, Disguise disguise, Disguise prevDisguise) {
		List<Object> packets = new ArrayList<Object>();
		packets.add(PacketFactory.packetEntityDestroy(entity.getEntityId()));
		if (prevDisguise != null)
			prevDisguise.removePackets(entity, packets);
		disguise.addPackets(entity, packets);
		for (ServerPlayerConnection con : NMS.getTrackedPlayers(entity))
			PacketSender.send(con, packets);
	}

	public static void removeDisguise(ServerPlayer player) {
		player.removeDisguise();
	}

	public static void removeDisguise(LivingEntity entity) {
		removeDisguise(entity, getDisguise(entity), null);
		// TODO save disguise!
	}

	public static void removeDisguise(LivingEntity entity, Disguise disguise, Disguise defaultDisguise) {
		if (disguise == null)
			return;

		List<Object> packets = new ArrayList<Object>();
		net.minecraft.world.entity.LivingEntity entityNMS = NMS.to(entity);
		packets.add(PacketFactory.packetEntityDestroy(entityNMS.getId()));
		disguise.removePackets(entity, packets);
		if (defaultDisguise != null)
			defaultDisguise.addPackets(entity, packets);
		else {
			packets.add(PacketFactory.packetEntitySpawn(entityNMS));
			DisguiseManager.addEntityStatePackets(entityNMS, packets);
		}
		for (ServerPlayerConnection con : NMS.getTrackedPlayers(entityNMS))
			PacketSender.send(con, packets);
	}

	public @Nullable Disguise getDisguise(ServerPlayer player) {
		return player.getDisguise();
	}

	public static @Nullable Disguise getDisguise(LivingEntity entity) {
		// TODO
		return null;
	}

	public static void addEntityStatePackets(net.minecraft.world.entity.LivingEntity entity, List<Object> packets) {
		int id = entity.getId();
		packets.add(PacketFactory.packetEntityMetadata(id, entity.getEntityData().getNonDefaultValues()));
		for (EquipmentSlot slot : EquipmentSlot.values()) {
			ItemStack equimentItem = entity.getItemBySlot(slot);
			if (equimentItem.getItem() != Items.AIR)
				packets.add(PacketFactory.packetEntityEquipment(id, slot, equimentItem));
		}
	}
}
