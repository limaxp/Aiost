package com.pm.aiost.server.world.chunk;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.pm.aiost.misc.packet.entity.PacketEntity;
import com.pm.aiost.misc.packet.object.PacketObject;
import com.pm.aiost.misc.utils.scheduler.AiostScheduler;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.server.world.ServerWorld;

public class ChunkWatcher {

	public static final int VIEW_DISTANCE = 5;

	public static void join(ServerPlayer serverPlayer) {
		new ShowChunkTask(serverPlayer.player, serverPlayer.getServerWorld(), serverPlayer.player.getLocation()).run();
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
		AiostScheduler.runTaskLater(1, () -> new ShowChunkTask(player, serverWorld, player.getLocation()).run());
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
		MoveChunkTask.doTask(player, serverWorld, fromLoc, toLoc);
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
						AiostScheduler.runTaskLater(10, this);
						return;
					}
				}
				z = chunkZ - VIEW_DISTANCE;
			}
		}
	}

	private static abstract class MoveChunkTask implements Runnable {

		private static void doTask(Player player, ServerWorld serverWorld, Location fromLoc, Location toLoc) {
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

		private static class MoveEastChunkTask extends MoveChunkTask {

			private MoveEastChunkTask(Player player, ServerWorld serverWorld, int fromChunkX, int fromChunkZ,
					int toChunkX, int toChunkZ) {
				super(player, serverWorld, fromChunkX, fromChunkZ, toChunkX, toChunkZ);
				i = toChunkZ - VIEW_DISTANCE;
			}

			@Override
			public void run() {
				if (!player.isOnline())
					return;
				for (; i <= toChunkZ + VIEW_DISTANCE; i++) {
					ServerChunk showChunk = serverWorld.getChunk(toChunkX + VIEW_DISTANCE, i);
					if (showChunk != null)
						show(player, showChunk);
					else {
						AiostScheduler.runTaskLater(10, this);
						return;
					}
					ServerChunk hideChunk = serverWorld.getChunk(fromChunkX - VIEW_DISTANCE, i);
					if (hideChunk != null)
						hide(player, hideChunk);
				}
			}
		}

		private static class MoveWestChunkTask extends MoveChunkTask {

			private MoveWestChunkTask(Player player, ServerWorld serverWorld, int fromChunkX, int fromChunkZ,
					int toChunkX, int toChunkZ) {
				super(player, serverWorld, fromChunkX, fromChunkZ, toChunkX, toChunkZ);
				i = toChunkZ - VIEW_DISTANCE;
			}

			@Override
			public void run() {
				if (!player.isOnline())
					return;
				for (; i <= toChunkZ + VIEW_DISTANCE; i++) {
					ServerChunk showChunk = serverWorld.getChunk(toChunkX - VIEW_DISTANCE, i);
					if (showChunk != null)
						show(player, showChunk);
					else {
						AiostScheduler.runTaskLater(10, this);
						return;
					}
					ServerChunk hideChunk = serverWorld.getChunk(fromChunkX + VIEW_DISTANCE, i);
					if (hideChunk != null)
						hide(player, hideChunk);
				}
			}
		}

		private static class MoveSouthChunkTask extends MoveChunkTask {

			private MoveSouthChunkTask(Player player, ServerWorld serverWorld, int fromChunkX, int fromChunkZ,
					int toChunkX, int toChunkZ) {
				super(player, serverWorld, fromChunkX, fromChunkZ, toChunkX, toChunkZ);
				i = toChunkX - VIEW_DISTANCE;
			}

			@Override
			public void run() {
				if (!player.isOnline())
					return;
				for (; i <= toChunkX + VIEW_DISTANCE; i++) {
					ServerChunk showChunk = serverWorld.getChunk(i, toChunkZ + VIEW_DISTANCE);
					if (showChunk != null)
						show(player, showChunk);
					else {
						AiostScheduler.runTaskLater(10, this);
						return;
					}
					ServerChunk hideChunk = serverWorld.getChunk(i, fromChunkZ - VIEW_DISTANCE);
					if (hideChunk != null)
						hide(player, hideChunk);
				}
			}
		}

		private static class MoveNorthChunkTask extends MoveChunkTask {

			private MoveNorthChunkTask(Player player, ServerWorld serverWorld, int fromChunkX, int fromChunkZ,
					int toChunkX, int toChunkZ) {
				super(player, serverWorld, fromChunkX, fromChunkZ, toChunkX, toChunkZ);
				i = toChunkX - VIEW_DISTANCE;
			}

			@Override
			public void run() {
				if (!player.isOnline())
					return;
				for (; i <= toChunkX + VIEW_DISTANCE; i++) {
					ServerChunk showChunk = serverWorld.getChunk(i, toChunkZ - VIEW_DISTANCE);
					if (showChunk != null)
						show(player, showChunk);
					else {
						AiostScheduler.runTaskLater(10, this);
						return;
					}
					ServerChunk hideChunk = serverWorld.getChunk(i, fromChunkZ + VIEW_DISTANCE);
					if (hideChunk != null)
						hide(player, hideChunk);
				}
			}
		}
	}

//	OLD CODE FOR SAFETY!
//	
//	public static void move(Player player, ServerWorld serverWorld, Location fromLoc, Location toLoc) {
//		Chunk fromChunk = fromLoc.getChunk();
//		int fromX = fromChunk.getX();
//		int fromZ = fromChunk.getZ();
//		Chunk toChunk = toLoc.getChunk();
//		int toX = toChunk.getX();
//		int toZ = toChunk.getZ();
//
//		if (fromX != toX) {
//			if (fromX < toX) { // East (positive x)
//				for (int z = toZ - VIEW_DISTANCE; z <= toZ + VIEW_DISTANCE; z++) {
//					hide(player, serverWorld.getChunk(fromX - VIEW_DISTANCE, z));
//					show(player, serverWorld.getChunk(toX + VIEW_DISTANCE, z));
//				}
//			} else { // West (negative x)
//				for (int z = toZ - VIEW_DISTANCE; z <= toZ + VIEW_DISTANCE; z++) {
//					hide(player, serverWorld.getChunk(fromX + VIEW_DISTANCE, z));
//					show(player, serverWorld.getChunk(toX - VIEW_DISTANCE, z));
//				}
//			}
//		}
//		if (fromZ != toZ) {
//			if (fromZ < toZ) { // South (positive z)
//				for (int x = toX - VIEW_DISTANCE; x <= toX + VIEW_DISTANCE; x++) {
//					hide(player, serverWorld.getChunk(x, fromZ - VIEW_DISTANCE));
//					show(player, serverWorld.getChunk(x, toZ + VIEW_DISTANCE));
//				}
//			} else { // North (negative z)
//				for (int x = toX - VIEW_DISTANCE; x <= toX + VIEW_DISTANCE; x++) {
//					hide(player, serverWorld.getChunk(x, fromZ + VIEW_DISTANCE));
//					show(player, serverWorld.getChunk(x, toZ - VIEW_DISTANCE));
//				}
//			}
//		}
//	}
}
