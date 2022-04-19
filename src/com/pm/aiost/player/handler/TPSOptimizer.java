package com.pm.aiost.player.handler;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.pm.aiost.misc.utils.nms.NMS;
import com.pm.aiost.player.ServerPlayer;

import net.minecraft.server.v1_15_R1.EntityPlayer;
import net.minecraft.server.v1_15_R1.MathHelper;
import net.minecraft.server.v1_15_R1.PlayerChunk;

public class TPSOptimizer {

	// TODO make something out of this
	// player.setViewDistance() is only useable in PaperSpigot

	public static final double maxIndice = 0.75;
	public static final double tpsLimit = 19.5;
	public static final double tpsChange = 0.01;
	public static final int loginViewRange = 4;
	public static final int loginViewDelay = 10;

	private static double reductionIndice = 0;

	public static double get1minTPS() {
		return NMS.getMinecraftServer().recentTps[0];
	}

	public static void update() {
		final double TPS = get1minTPS();
		if (TPS > tpsLimit && TPS < 20) { // If tps > tps limit
			reductionIndice = reductionIndice - tpsChange; // Decrease indice
		} else if (TPS < tpsLimit) { // If tps < tps limit
			reductionIndice = reductionIndice + tpsChange; // Increase indice
		}
		reductionIndice = Math.max(0.0, Math.min(reductionIndice, maxIndice));
	}

	public static double getReductionIndice() {
		return reductionIndice;
	}

	public static void onPlayerJoin(final ServerPlayer serverPlayer) {
		final Player player = serverPlayer.player;
//		System.out.println(NMS.getNMS(player).clientViewDistance);
//		System.out.println(player.getWorld().getViewDistance());
//		System.out.println(Bukkit.getViewDistance());
//		data.setViewDistanceATBS(loginViewRange, onLoginDelay);
	}

//	public static void onPlayerMove(final PlayerMoveEvent e) {
//		if (e.getPlayer().hasMetadata("NPC")) {
//			return;
//		}
//		// Unset Afk with Async Method...
//		Bukkit.getScheduler().runTaskAsynchronously(BestViewDistance.plugin, new UnsetAfk(e));
//	}
//
//	public static void onPlayerTeleport(final PlayerTeleportEvent e) {
//		final Player p = e.getPlayer();
//		if (p.hasMetadata("NPC")) {
//			return;
//		}
//
//		final BVDPlayer player = onlinePlayers.get(p);
//		if (!e.getCause().equals(CHORUS_FRUIT) && !e.getCause().equals(UNKNOWN) && !e.getCause().equals(ENDER_PEARL)) {
//			if (player.isViewBypass() && permissionsBypassTeleport) {
//				if (!player.isWaitingForTpUnset()) { // If he's not waiting for tp unset
//					player.setViewDistance(onTeleportView); // Set on teleport view
//				}
//				Bukkit.getScheduler().runTaskAsynchronously(BestViewDistance.plugin, new TeleportData(p)); // Process
//																											// teleport
//																											// data with
//																											// async
//																											// method
//			} else {
//				if (!player.isWaitingForTpUnset()) { // If he's not waiting for tp unset
//					player.setViewDistance(onTeleportView); // Set on teleport view
//				}
//				Bukkit.getScheduler().runTaskAsynchronously(BestViewDistance.plugin, new TeleportData(p)); // Process
//																											// teleport
//																											// data with
//																											// async
//																											// method
//			}
//		}
//	}

	public class SetViewDistance implements Runnable {
		private final int viewDistance;
		private final Player player;

		public SetViewDistance(final Player player, final int viewDistance) {
			this.player = player;
			this.viewDistance = viewDistance;
		}

		@Override
		public void run() {
			NMS.getNMS(player).clientViewDistance = viewDistance;
//			player.setViewDistance(viewDistance); PaperSpigot
		}
	}

//	public void setViewDistance(EntityPlayer entityplayer, int i, boolean markSort) {
//		i = MathHelper.clamp(i, 3, 32);
//		int oldViewDistance = entityplayer.getViewDistance();
//		if (i != oldViewDistance) {
//			int j = i - oldViewDistance;
//
//			int k = (int) entityplayer.locX >> 4;
//			int l = (int) entityplayer.locZ >> 4;
//			int i1;
//			int j1;
//
//			if (j > 0) {
//				for (i1 = k - i; i1 <= k + i; ++i1) {
//					for (j1 = l - i; j1 <= l + i; ++j1) {
//						PlayerChunk playerchunk = this.c(i1, j1);
//
//						if (!playerchunk.d(entityplayer)) {
//							playerchunk.a(entityplayer);
//						}
//					}
//				}
//			} else {
//				for (i1 = k - oldViewDistance; i1 <= k + oldViewDistance; ++i1) {
//					for (j1 = l - oldViewDistance; j1 <= l + oldViewDistance; ++j1) {
//						if (!this.a(i1, j1, k, l, i)) {
//							this.c(i1, j1).b(entityplayer);
//						}
//					}
//				}
//				if (markSort) {
//					this.e();
//				}
//			}
//		}
//	}
}
