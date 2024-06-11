package com.pm.aiost.world.chunk;

import java.util.List;

import com.pm.aiost.misc.nms.NBT.NBTType;
import com.pm.aiost.misc.packet.entity.PacketEntity;
import com.pm.aiost.misc.packet.entity.PacketEntityTypes;
import com.pm.aiost.misc.packet.object.PacketObject;
import com.pm.aiost.misc.packet.object.PacketObjectTypes;
import com.pm.aiost.world.object.tileObject.TileObject;
import com.pm.aiost.world.object.tileObject.TileObjectTypes;

import it.unimi.dsi.fastutil.ints.Int2IntMap.Entry;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

public class ChunkLoader {

	public static ServerChunk loadChunk(ServerChunk serverChunk, CompoundTag nbt) {
		ListTag sectionList = nbt.getList("Sections", NBTType.COMPOUND);
		ServerChunkSection[] sections = serverChunk.sections;
		if (sectionList.size() > 0) {
			for (byte i = 0; i < sectionList.size(); i++) {
				CompoundTag sectionTag = sectionList.getCompound(i);
				ServerChunkSection section = new ServerChunkSection(sectionTag.getByte("Y"));
				sections[i] = section;
			}
		} else {
			for (byte b = 0; b < 16; b++)
				sections[b] = new ServerChunkSection(b);
		}

		ListTag packetObjectList = nbt.getList("PacketObjects", NBTType.COMPOUND);
		if (packetObjectList.size() > 0) {
			for (int i = 0; i < packetObjectList.size(); i++) {
				CompoundTag packetObjectTag = packetObjectList.getCompound(i);
				PacketObjectTypes.spawn(packetObjectTag.getInt("id"), serverChunk, packetObjectTag);
			}
		}

		ListTag packetEntityList = nbt.getList("PacketEntities", NBTType.COMPOUND);
		if (packetEntityList.size() > 0) {
			for (int i = 0; i < packetEntityList.size(); i++) {
				CompoundTag packetEntityTag = packetEntityList.getCompound(i);
				PacketEntityTypes.spawn(packetEntityTag.getInt("id"), serverChunk, packetEntityTag);
			}
		}

		ListTag tileObjectList = nbt.getList("TileObjects", NBTType.COMPOUND);
		if (tileObjectList.size() > 0) {
			for (int i = 0; i < tileObjectList.size(); i++) {
				CompoundTag tileObjectTag = tileObjectList.getCompound(i);
				TileObjectTypes.spawn(tileObjectTag.getInt("id"), serverChunk, tileObjectTag);
			}
		}

		ListTag blockEffectList = nbt.getList("BlockEffects", NBTType.COMPOUND);
		if (blockEffectList.size() > 0) {
			for (int i = 0; i < blockEffectList.size(); i++) {
				CompoundTag blockEffectTag = blockEffectList.getCompound(i);
				serverChunk.blockEffects.put(blockEffectTag.getInt("loc"), blockEffectTag.getInt("id"));
			}
		}

		int[] regionIds = nbt.getIntArray("Regions");
		int regionIdLength = regionIds.length;
		if (regionIdLength > 0) {
			for (int i = 0; i < regionIdLength; i++)
				serverChunk.regions.add(regionIds[i]);
		}
		return serverChunk;
	}

	public static CompoundTag saveChunk(ServerChunk serverChunk) {
		CompoundTag nbt = new CompoundTag();
		ListTag sectionList = new ListTag();
		nbt.put("Sections", sectionList);

		for (ServerChunkSection section : serverChunk.sections) {
			CompoundTag sectionTag = new CompoundTag();
			sectionList.add(sectionTag);
			sectionTag.putByte("Y", section.yPos);
		}

		ObjectCollection<PacketObject> packetObjects = serverChunk.packetObjects.values();
		if (packetObjects.size() > 0) {
			ListTag packetObjectList = new ListTag();
			nbt.put("PacketObjects", packetObjectList);
			for (PacketObject packetObject : packetObjects)
				packetObjectList.add(packetObject.save(new CompoundTag()));
		}

		List<PacketEntity> packetEntities = serverChunk.packetEntities;
		if (packetEntities.size() > 0) {
			ListTag packetEntityList = new ListTag();
			nbt.put("PacketEntities", packetEntityList);
			for (PacketEntity packetEntity : packetEntities)
				packetEntityList.add(packetEntity.save(new CompoundTag()));
		}

		ObjectCollection<TileObject> tileObjects = serverChunk.tileObjects.values();
		if (tileObjects.size() > 0) {
			ListTag tileObjectList = new ListTag();
			nbt.put("TileObjects", tileObjectList);
			for (TileObject tileObject : tileObjects)
				tileObjectList.add(tileObject.save(new CompoundTag()));
		}

		ObjectSet<Entry> blockEffects = serverChunk.blockEffects.int2IntEntrySet();
		if (blockEffects.size() > 0) {
			ListTag blockEffectList = new ListTag();
			nbt.put("BlockEffects", blockEffectList);
			for (Entry entry : blockEffects) {
				CompoundTag blockEffectTag = new CompoundTag();
				blockEffectList.add(blockEffectTag);
				blockEffectTag.putLong("loc", entry.getIntKey());
				blockEffectTag.putInt("id", entry.getIntValue());
			}
		}

		IntList regions = serverChunk.regions;
		if (regions.size() > 0)
			nbt.putIntArray("Regions", regions.toIntArray());
		return nbt;
	}
}
