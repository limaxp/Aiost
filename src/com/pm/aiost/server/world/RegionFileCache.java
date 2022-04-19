package com.pm.aiost.server.world;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

import org.bukkit.Chunk;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.objects.ObjectIterator;

import com.pm.aiost.misc.log.Logger;

import net.minecraft.server.v1_15_R1.ChunkCoordIntPair;
import net.minecraft.server.v1_15_R1.NBTCompressedStreamTools;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import net.minecraft.server.v1_15_R1.RegionFile;

public class RegionFileCache implements AutoCloseable {

	private final Long2ObjectLinkedOpenHashMap<RegionFile> cache;
	private final File regionFile;

	public RegionFileCache(File regionFile) {
		this.cache = new Long2ObjectLinkedOpenHashMap<RegionFile>();
		this.regionFile = regionFile;
		if (!regionFile.exists())
			regionFile.mkdir();
	}

	@Override
	public void close() throws IOException {
		ObjectIterator<RegionFile> objectiterator = this.cache.values().iterator();
		while (objectiterator.hasNext())
			objectiterator.next().close();
	}

	private RegionFile getFile(int regionX, int regionZ) throws IOException {
		long key = ChunkCoordIntPair.pair(regionX, regionZ);
		RegionFile regionfile = this.cache.getAndMoveToFirst(key);
		if (regionfile != null)
			return regionfile;

		if (cache.size() >= 256)
			cache.removeLast().close();

		File file = new File(regionFile, "r." + regionX + "." + regionZ + ".aia");
		RegionFile regionfile1 = new RegionFile(file, regionFile);
		cache.putAndMoveToFirst(key, regionfile1);
		return regionfile1;
	}

	public NBTTagCompound loadChunk(Chunk chunk) {
		int x = chunk.getX();
		int z = chunk.getZ();
		int regionX = getRegionIndex(x);
		int regionZ = getRegionIndex(z);
		try {
			RegionFile regionFile = getFile(regionX, regionZ);
			try (DataInputStream din = regionFile.a(new ChunkCoordIntPair(x, z))) {
				if (din != null)
					return NBTCompressedStreamTools.a(din);
//					return NBTTagCompound.a.b(din, 0, NBTReadLimiter.a);
			}
		} catch (IOException e) {
			Logger.err("RegionFileCache: Error on loading chunk at " + x + ", " + z, e);
		}
		return new NBTTagCompound();
	}

	public void saveChunk(Chunk chunk, NBTTagCompound nbt) {
		int x = chunk.getX();
		int z = chunk.getZ();
		int regionX = getRegionIndex(x);
		int regionZ = getRegionIndex(z);
		try {
			RegionFile regionFile = getFile(regionX, regionZ);
			try (DataOutputStream dou = regionFile.c(new ChunkCoordIntPair(x, z))) {
				NBTCompressedStreamTools.a(nbt, (DataOutput) dou);
//				nbt.write(dau);
			}
		} catch (IOException e) {
			Logger.err("RegionFileCache: Error on saving chunk at " + x + ", " + z, e);
		}
	}

	public static int getRegionIndex(int chunkCoordinate) {
		return chunkCoordinate < 0 ? ((chunkCoordinate + 1) / 512) - 1 : chunkCoordinate / 512;
	}
}