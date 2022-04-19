package com.pm.aiost.server.world.chunk;

import java.util.List;

import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.Int2IntMap.Entry;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.IntList;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.objects.ObjectCollection;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.objects.ObjectSet;

import com.pm.aiost.misc.packet.entity.PacketEntity;
import com.pm.aiost.misc.packet.entity.PacketEntityTypes;
import com.pm.aiost.misc.packet.object.PacketObject;
import com.pm.aiost.misc.packet.object.PacketObjectTypes;
import com.pm.aiost.misc.utils.nbt.NBTType;
import com.pm.aiost.misc.utils.nbt.custom.NBTCompound;
import com.pm.aiost.server.world.object.tileObject.TileObject;
import com.pm.aiost.server.world.object.tileObject.TileObjectTypes;

import net.minecraft.server.v1_15_R1.NBTTagCompound;
import net.minecraft.server.v1_15_R1.NBTTagList;

public class ChunkLoader {

	public static ServerChunk loadChunk(ServerChunk serverChunk, NBTTagCompound nbt) {
		NBTTagList sectionList = nbt.getList("Sections", NBTType.COMPOUND);
		ServerChunkSection[] sections = serverChunk.sections;
		if (sectionList.size() > 0) {
			for (byte i = 0; i < sectionList.size(); i++) {
				NBTTagCompound sectionTag = sectionList.getCompound(i);
				ServerChunkSection section = new ServerChunkSection(sectionTag.getByte("Y"));
				sections[i] = section;
			}
		} else {
			for (byte b = 0; b < 16; b++)
				sections[b] = new ServerChunkSection(b);
		}

		NBTTagList packetObjectList = nbt.getList("PacketObjects", NBTType.COMPOUND);
		if (packetObjectList.size() > 0) {
			for (int i = 0; i < packetObjectList.size(); i++) {
				NBTTagCompound packetObjectTag = packetObjectList.getCompound(i);
				PacketObjectTypes.spawn(packetObjectTag.getInt("id"), serverChunk, packetObjectTag);
			}
		}

		NBTTagList packetEntityList = nbt.getList("PacketEntities", NBTType.COMPOUND);
		if (packetEntityList.size() > 0) {
			for (int i = 0; i < packetEntityList.size(); i++) {
				NBTTagCompound packetEntityTag = packetEntityList.getCompound(i);
				PacketEntityTypes.spawn(packetEntityTag.getInt("id"), serverChunk, packetEntityTag);
			}
		}

		NBTTagList tileObjectList = nbt.getList("TileObjects", NBTType.COMPOUND);
		if (tileObjectList.size() > 0) {
			for (int i = 0; i < tileObjectList.size(); i++) {
				NBTTagCompound tileObjectTag = tileObjectList.getCompound(i);
				TileObjectTypes.spawn(tileObjectTag.getInt("id"), serverChunk, tileObjectTag);
			}
		}

		NBTTagList blockEffectList = nbt.getList("BlockEffects", NBTType.COMPOUND);
		if (blockEffectList.size() > 0) {
			for (int i = 0; i < blockEffectList.size(); i++) {
				NBTTagCompound blockEffectTag = blockEffectList.getCompound(i);
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

	public static NBTTagCompound saveChunk(ServerChunk serverChunk) {
		NBTTagCompound nbt = new NBTTagCompound();
		NBTTagList sectionList = new NBTTagList();
		nbt.set("Sections", sectionList);

		for (ServerChunkSection section : serverChunk.sections) {
			NBTTagCompound sectionTag = new NBTTagCompound();
			sectionList.add(sectionTag);
			sectionTag.setByte("Y", section.yPos);
		}

		ObjectCollection<PacketObject> packetObjects = serverChunk.packetObjects.values();
		if (packetObjects.size() > 0) {
			NBTTagList packetObjectList = new NBTTagList();
			nbt.set("PacketObjects", packetObjectList);
			for (PacketObject packetObject : packetObjects)
				packetObjectList.add(packetObject.save(new NBTCompound()));
		}

		List<PacketEntity> packetEntities = serverChunk.packetEntities;
		if (packetEntities.size() > 0) {
			NBTTagList packetEntityList = new NBTTagList();
			nbt.set("PacketEntities", packetEntityList);
			for (PacketEntity packetEntity : packetEntities)
				packetEntityList.add(packetEntity.save(new NBTCompound()));
		}

		ObjectCollection<TileObject> tileObjects = serverChunk.tileObjects.values();
		if (tileObjects.size() > 0) {
			NBTTagList tileObjectList = new NBTTagList();
			nbt.set("TileObjects", tileObjectList);
			for (TileObject tileObject : tileObjects)
				tileObjectList.add(tileObject.save(new NBTCompound()));
		}

		ObjectSet<Entry> blockEffects = serverChunk.blockEffects.int2IntEntrySet();
		if (blockEffects.size() > 0) {
			NBTTagList blockEffectList = new NBTTagList();
			nbt.set("BlockEffects", blockEffectList);
			for (Entry entry : blockEffects) {
				NBTTagCompound blockEffectTag = new NBTTagCompound();
				blockEffectList.add(blockEffectTag);
				blockEffectTag.setLong("loc", entry.getIntKey());
				blockEffectTag.setInt("id", entry.getIntValue());
			}
		}

		IntList regions = serverChunk.regions;
		if (regions.size() > 0)
			nbt.setIntArray("Regions", regions.toIntArray());
		return nbt;
	}
}
