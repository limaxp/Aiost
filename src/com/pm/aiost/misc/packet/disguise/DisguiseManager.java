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
import com.pm.aiost.misc.packet.disguise.disguises.DisguiseEntity;
import com.pm.aiost.misc.packet.disguise.disguises.DisguiseEntityLiving;
import com.pm.aiost.misc.packet.disguise.disguises.DisguiseFurniture;
import com.pm.aiost.misc.packet.disguise.disguises.DisguisePlayer;
import com.pm.aiost.misc.utils.meta.MetaData;

import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ChunkMap.TrackedEntity;
import net.minecraft.server.network.ServerPlayerConnection;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class DisguiseManager {

	public final static String KEY = "aiostguise";

	private static final Map<String, Supplier<Disguise>> NAME_MAP = new HashMap<String, Supplier<Disguise>>();

	static {
		register("entity_", DisguiseEntity::new);
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

	public static void setDisguise(LivingEntity entity, Disguise disguise) {
		List<Object> packets = new ArrayList<Object>();
		packets.add(PacketFactory.packetEntityDestroy(entity.getEntityId()));
		disguise.addPackets(entity, packets);
		TrackedEntity tracker = NMS.getEntityTracker(entity);
		if (tracker != null)
			for (ServerPlayerConnection con : tracker.seenBy)
				for (Object packet : packets)
					PacketSender.send(con, (Packet<?>) packet);
		MetaData.set(entity, KEY, disguise);
	}

	public static void removeDisguise(LivingEntity entity) {
		Disguise disguise = getDisguise(entity);
		if (disguise == null)
			return;

		List<Object> packets = new ArrayList<Object>();
		net.minecraft.world.entity.LivingEntity entityNMS = NMS.to(entity);
		packets.add(PacketFactory.packetEntityDestroy(entityNMS.getId()));
		packets.add(PacketFactory.packetEntitySpawn(entityNMS));
		DisguiseManager.addEntityStatePackets(entityNMS, packets);
		TrackedEntity tracker = NMS.getEntityTracker(entity);
		if (tracker != null)
			for (ServerPlayerConnection con : tracker.seenBy)
				for (Object packet : packets)
					PacketSender.send(con, (Packet<?>) packet);
		MetaData.remove(entity, KEY);
	}

	public static @Nullable Disguise getDisguise(LivingEntity entity) {
		return (Disguise) MetaData.get(entity, KEY);
	}

	public static void addEntityStatePackets(net.minecraft.world.entity.LivingEntity entity, List<Object> packets) {
		packets.add(PacketFactory.packetEntityMetadata(entity.getId(), entity.getEntityData().getNonDefaultValues()));
		for (EquipmentSlot slot : EquipmentSlot.values()) {
			ItemStack equimentItem = entity.getItemBySlot(slot);
			if (equimentItem.getItem() != Items.AIR)
				packets.add(PacketFactory.packetEntityEquipment(entity.getId(), slot, equimentItem));
		}
	}
}
