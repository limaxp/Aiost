package com.pm.aiost.world.chunk;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.pm.aiost.misc.packet.entity.PacketEntity;
import com.pm.aiost.misc.packet.object.PacketObject;
import com.pm.aiost.misc.utils.scheduler.AiostScheduler;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.world.ServerWorld;

public class ChunkWatcher {

	public static final int VIEW_DISTANCE = 5;
	public static final int LOAD_DELAY = 10;
	public static final int JOIN_DELAY = 60;

	public static void join(ServerPlayer serverPlayer) {
		AiostScheduler.runTaskLater(JOIN_DELAY, () -> new ShowChunkTask(serverPlayer.player,
				serverPlayer.getServerWorld(), serverPlayer.player.getLocation()).run());
	}

	public static void disable(ServerPlayer serverPlayer) {
		disable(serverPlayer.player, serverPlayer.getServerWorld(), serverPlayer.player.getLocation());
	}

	private static void disable(Player player, ServerWorld serverWorld, Location loc) {
		Chunk chunk = loc.getChunk();
		int chunkX = chunk.getX();
		int chunkZ = chunk.getZ();
		for (int x = chunkX - VIEW_DISTANCE; x <= chunkX + VIEW_DISTANCE; x++) {
			for (int z = chunkZ - VIEW_DISTANCE; z <= chunkZ + VIEW_DISTANCE; z++) {
				ServerChunk serverChunk = serverWorld.getChunk(x, z);
				if (serverChunk != null)
					hide(player, serverChunk);
			}
		}
	}

	public static void changeWorld(Player player, ServerWorld serverWorld) {
		AiostScheduler.runTaskLater(JOIN_DELAY,
				() -> new ShowChunkTask(player, serverWorld, player.getLocation()).run());
	}

	public static void teleport(ServerPlayer serverPlayer, Location fromLocation, Location toLocation) {
		teleport(serverPlayer.player, serverPlayer.getServerWorld(), fromLocation, toLocation);
	}

	public static void teleport(Player player, ServerWorld serverWorld, Location fromLocation, Location toLocation) {
		// TODO: check for overlapping chunks
		// remove not overlapping ones from the previous location
		// add only overlapping ones from next location
		disable(player, serverWorld, fromLocation);
		new ShowChunkTask(player, serverWorld, toLocation).run();
	}

	public static void move(ServerPlayer serverPlayer, Location fromLocation, Location toLocation) {
		move(serverPlayer.player, serverPlayer.getServerWorld(), fromLocation, toLocation);
	}

	public static void move(Player player, ServerWorld serverWorld, Location fromLoc, Location toLoc) {
		Chunk fromChunk = fromLoc.getChunk();
		int fromChunkX = fromChunk.getX();
		int fromChunkZ = fromChunk.getZ();
		Chunk toChunk = toLoc.getChunk();
		int toChunkX = toChunk.getX();
		int toChunkZ = toChunk.getZ();
		if (fromChunkX != toChunkX) {
			if (fromChunkX < toChunkX) // East (positive x)
				new MoveEastChunkTask(player, serverWorld, fromChunkX, fromChunkZ, toChunkX, toChunkZ).run();
			else // West (negative x)
				new MoveWestChunkTask(player, serverWorld, fromChunkX, fromChunkZ, toChunkX, toChunkZ).run();
		}
		if (fromChunkZ != toChunkZ) {
			if (fromChunkZ < toChunkZ) // South (positive z)
				new MoveSouthChunkTask(player, serverWorld, fromChunkX, fromChunkZ, toChunkX, toChunkZ).run();
			else // North (negative z)
				new MoveNorthChunkTask(player, serverWorld, fromChunkX, fromChunkZ, toChunkX, toChunkZ).run();
		}
	}

	private static void show(Player player, ServerChunk chunk) {
		for (PacketObject packetObject : chunk.packetObjects.values())
			packetObject.show(player);
		for (PacketEntity packetEntity : chunk.packetEntities)
			packetEntity.show(player);
	}

	private static void hide(Player player, ServerChunk chunk) {
		for (PacketObject packetObject : chunk.packetObjects.values())
			packetObject.hide(player);
		for (PacketEntity packetEntity : chunk.packetEntities)
			packetEntity.hide(player);
	}

	private static class ShowChunkTask implements Runnable {

		private final Player player;
		private final ServerWorld serverWorld;
		private final int chunkX;
		private final int chunkZ;
		private int x;
		private int z;

		private ShowChunkTask(Player player, ServerWorld serverWorld, Location loc) {
			this.player = player;
			this.serverWorld = serverWorld;
			Chunk chunk = loc.getChunk();
			chunkX = chunk.getX();
			chunkZ = chunk.getZ();
			x = chunkX - VIEW_DISTANCE;
			z = chunkZ - VIEW_DISTANCE;
		}

		@Override
		public void run() {
			if (!player.isOnline())
				return;
			for (; x <= chunkX + VIEW_DISTANCE; x++) {
				for (; z <= chunkZ + VIEW_DISTANCE; z++) {
					ServerChunk serverChunk = serverWorld.getChunk(x, z);
					if (serverChunk != null)
						show(player, serverChunk);
					else {
						AiostScheduler.runTaskLater(LOAD_DELAY, this);
						return;
					}
				}
				z = chunkZ - VIEW_DISTANCE;
			}
		}
	}

	private static abstract class MoveChunkTask implements Runnable {

		protected final Player player;
		protected final ServerWorld serverWorld;
		protected final int fromChunkX;
		protected final int fromChunkZ;
		protected final int toChunkX;
		protected final int toChunkZ;
		protected int i;

		private MoveChunkTask(Player player, ServerWorld serverWorld, int fromChunkX, int fromChunkZ, int toChunkX,
				int toChunkZ) {
			this.player = player;
			this.serverWorld = serverWorld;
			this.fromChunkX = fromChunkX;
			this.fromChunkZ = fromChunkZ;
			this.toChunkX = toChunkX;
			this.toChunkZ = toChunkZ;
		}

		protected static void move(Player player, ServerWorld serverWorld, int x1, int y1, int x2, int y2,
				Runnable task) {
			ServerChunk showChunk = serverWorld.getChunk(x1, y1);
			if (showChunk != null)
				show(player, showChunk);
			else {
				AiostScheduler.runTaskLater(LOAD_DELAY, task);
				return;
			}
			ServerChunk hideChunk = serverWorld.getChunk(x2, y2);
			if (hideChunk != null)
				hide(player, hideChunk);
		}
	}

	private static class MoveEastChunkTask extends MoveChunkTask {

		private MoveEastChunkTask(Player player, ServerWorld serverWorld, int fromChunkX, int fromChunkZ, int toChunkX,
				int toChunkZ) {
			super(player, serverWorld, fromChunkX, fromChunkZ, toChunkX, toChunkZ);
			i = toChunkZ - VIEW_DISTANCE;
		}

		@Override
		public void run() {
			if (!player.isOnline())
				return;
			for (; i <= toChunkZ + VIEW_DISTANCE; i++)
				move(player, serverWorld, toChunkX + VIEW_DISTANCE, i, fromChunkX - VIEW_DISTANCE, i, this);
		}
	}

	private static class MoveWestChunkTask extends MoveChunkTask {

		private MoveWestChunkTask(Player player, ServerWorld serverWorld, int fromChunkX, int fromChunkZ, int toChunkX,
				int toChunkZ) {
			super(player, serverWorld, fromChunkX, fromChunkZ, toChunkX, toChunkZ);
			i = toChunkZ - VIEW_DISTANCE;
		}

		@Override
		public void run() {
			if (!player.isOnline())
				return;
			for (; i <= toChunkZ + VIEW_DISTANCE; i++)
				move(player, serverWorld, toChunkX - VIEW_DISTANCE, i, fromChunkX + VIEW_DISTANCE, i, this);
		}
	}

	private static class MoveSouthChunkTask extends MoveChunkTask {

		private MoveSouthChunkTask(Player player, ServerWorld serverWorld, int fromChunkX, int fromChunkZ, int toChunkX,
				int toChunkZ) {
			super(player, serverWorld, fromChunkX, fromChunkZ, toChunkX, toChunkZ);
			i = toChunkX - VIEW_DISTANCE;
		}

		@Override
		public void run() {
			if (!player.isOnline())
				return;
			for (; i <= toChunkX + VIEW_DISTANCE; i++)
				move(player, serverWorld, i, toChunkZ + VIEW_DISTANCE, i, fromChunkZ - VIEW_DISTANCE, this);
		}
	}

	private static class MoveNorthChunkTask extends MoveChunkTask {

		private MoveNorthChunkTask(Player player, ServerWorld serverWorld, int fromChunkX, int fromChunkZ, int toChunkX,
				int toChunkZ) {
			super(player, serverWorld, fromChunkX, fromChunkZ, toChunkX, toChunkZ);
			i = toChunkX - VIEW_DISTANCE;
		}

		@Override
		public void run() {
			if (!player.isOnline())
				return;
			for (; i <= toChunkX + VIEW_DISTANCE; i++)
				move(player, serverWorld, i, toChunkZ - VIEW_DISTANCE, i, fromChunkZ + VIEW_DISTANCE, this);
		}
	}
}
