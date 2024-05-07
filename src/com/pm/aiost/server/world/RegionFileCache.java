package com.pm.aiost.server.world;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import org.bukkit.Chunk;

import com.pm.aiost.misc.log.Logger;
import com.pm.aiost.misc.nms.NMS;

import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.storage.RegionFile;
import net.minecraft.world.level.chunk.storage.RegionStorageInfo;

public class RegionFileCache implements AutoCloseable {

	private final Long2ObjectLinkedOpenHashMap<RegionFile> cache;
	private final File regionFile;
	private final ServerWorld serverWorld;

	public RegionFileCache(ServerWorld serverWorld, File regionFile) {
		this.serverWorld = serverWorld;
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
		long key = ChunkPos.asLong(regionX, regionZ);
		RegionFile regionfile = this.cache.getAndMoveToFirst(key);
		if (regionfile != null)
			return regionfile;

		if (cache.size() >= 256)
			cache.removeLast().close();

		Path path = new File(regionFile, "r." + regionX + "." + regionZ + ".aia").toPath();
		ServerLevel level = NMS.getNMS(serverWorld.world);
		RegionStorageInfo info = new RegionStorageInfo(serverWorld.getName(), level.dimension(),
				level.getTypeKey().registry().getNamespace());
		RegionFile regionfile1 = new RegionFile(info, path, regionFile.toPath(), true);
		cache.putAndMoveToFirst(key, regionfile1);
		return regionfile1;
	}

	public CompoundTag loadChunk(Chunk chunk) {
		int x = chunk.getX();
		int z = chunk.getZ();
		int regionX = getRegionIndex(x);
		int regionZ = getRegionIndex(z);
		try {
			RegionFile regionFile = getFile(regionX, regionZ);
			try (DataInputStream din = regionFile.getChunkDataInputStream(new ChunkPos(x, z))) {
				if (din != null)
//					return NBTTagCompound.a.b(din, 0, NBTReadLimiter.a);
//					return NBTCompressedStreamTools.a(din);
					return NbtIo.readCompressed(din, NbtAccounter.unlimitedHeap());
			}
		} catch (IOException e) {
			Logger.err("RegionFileCache: Error on loading chunk at " + x + ", " + z, e);
		}
		return new CompoundTag();
	}

	public void saveChunk(Chunk chunk, CompoundTag nbt) {
		int x = chunk.getX();
		int z = chunk.getZ();
		int regionX = getRegionIndex(x);
		int regionZ = getRegionIndex(z);
		try {
			RegionFile regionFile = getFile(regionX, regionZ);
			try (DataOutputStream dou = regionFile.getChunkDataOutputStream(new ChunkPos(x, z))) {
//				NBTCompressedStreamTools.a(nbt, (DataOutput) dou);
//				nbt.write(dou);
				NbtIo.writeCompressed(nbt, dou);
			}
		} catch (IOException e) {
			Logger.err("RegionFileCache: Error on saving chunk at " + x + ", " + z, e);
		}
	}

	public static int getRegionIndex(int chunkCoordinate) {
		return chunkCoordinate < 0 ? ((chunkCoordinate + 1) / 512) - 1 : chunkCoordinate / 512;
	}
}