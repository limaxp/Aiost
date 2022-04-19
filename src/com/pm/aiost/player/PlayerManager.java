package com.pm.aiost.player;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.bukkit.entity.Player;

import com.pm.aiost.collection.list.UnorderedIdentityArrayList;
import com.pm.aiost.event.EquipmentListener;
import com.pm.aiost.event.eventHandler.EventHandler;
import com.pm.aiost.event.eventHandler.EventHandler.QuitReason;
import com.pm.aiost.misc.SpigotConfig;
import com.pm.aiost.misc.menu.Menu;
import com.pm.aiost.misc.packet.listen.PacketWatcher;
import com.pm.aiost.player.handler.TPSOptimizer;
import com.pm.aiost.player.handler.VisibilityManager;
import com.pm.aiost.player.unlockable.UnlockableTypes;
import com.pm.aiost.server.ServerManager;
import com.pm.aiost.server.http.HttpServer;
import com.pm.aiost.server.request.ServerRequest;
import com.pm.aiost.server.world.ServerWorld;
import com.pm.aiost.server.world.chunk.ChunkWatcher;
import com.pm.aiost.server.world.region.IRegion;

public class PlayerManager {

	private static final List<ServerPlayer> LIST;
	private static final List<ServerPlayer> LIST_VIEW;
	private static final Map<Player, ServerPlayer> PLAYER_MAP;
	private static final Int2ObjectMap<ServerPlayer> BUNGEE_MAP;

	private static final String RESOURCEPACK_LINK;
	private static final String DEFAULT_RESOURCEPACK_LINK;

	static {
		int maxPlayer = ServerManager.getServer().getType().maxPlayer;
		LIST = new UnorderedIdentityArrayList<ServerPlayer>(maxPlayer);
		LIST_VIEW = Collections.unmodifiableList(LIST);
		PLAYER_MAP = new IdentityHashMap<Player, ServerPlayer>(maxPlayer);
		BUNGEE_MAP = new Int2ObjectOpenHashMap<ServerPlayer>(maxPlayer);

		String address = HttpServer.getAddress().getHostAddress() + ':' + HttpServer.getPort();
		RESOURCEPACK_LINK = "http://" + address + "/ResourcePack/ResourcePack.zip";
		DEFAULT_RESOURCEPACK_LINK = "http://" + address + "/ResourcePack/DefaultPack.zip";
	}

	public static void registerOnlinePlayer() {
		for (Player player : Bukkit.getOnlinePlayers())
			registerPlayer(player);
	}

	public static void enableOnlinePlayer() {
		if (SpigotConfig.HAS_BUNGEE) {
			for (ServerPlayer serverPlayer : LIST) {
				onPlayerEnable(serverPlayer);
				PlayerDataLoader.askForBungeePlayerData(serverPlayer.player);
			}
		} else {
			int size = LIST.size();
			for (int i = 0; i < size; i++)
				onPlayerEnable(LIST.get(i));
		}
	}

	public static void disableOnlinePlayer() {
		for (ServerPlayer serverPlayer : getOnlinePlayer())
			onPlayerDisable(serverPlayer);
	}

	private static ServerPlayer registerPlayer(Player player) {
		ServerPlayer serverPlayer = new ServerPlayer(player);
		LIST.add(serverPlayer);
		PLAYER_MAP.put(player, serverPlayer);
		return serverPlayer;
	}

	private static ServerPlayer unregisterPlayer(Player player) {
		ServerPlayer serverPlayer = PLAYER_MAP.remove(player);
		BUNGEE_MAP.remove(serverPlayer.bungeeID);
		LIST.remove(serverPlayer);
		return serverPlayer;
	}

	private static void initPlayer(ServerPlayer serverPlayer) {
		serverPlayer.initServerWorld();
		EquipmentListener.playerJoinCheck(serverPlayer);
		ChunkWatcher.join(serverPlayer);
		if (!SpigotConfig.HAS_BUNGEE)
			PlayerDataLoader.loadDataWithoutBungee(serverPlayer);
	}

	public static ServerPlayer onPlayerJoin(Player player) {
		applyResourcePack(player);
		ServerPlayer serverPlayer = registerPlayer(player);
		initPlayer(serverPlayer);
		PacketWatcher.inject(serverPlayer);
		return serverPlayer;
	}

	private static void onPlayerEnable(ServerPlayer serverPlayer) {
		initPlayer(serverPlayer);
		try {
			PacketWatcher.inject(serverPlayer);
		} catch (Exception e) {
			// swallow
		}
	}

	public static void onPlayerRecieveBungeeData(ServerPlayer serverPlayer) {
		BUNGEE_MAP.put(serverPlayer.bungeeID, serverPlayer);
		onPlayerRecieveData(serverPlayer);
	}

	public static void onPlayerRecieveData(ServerPlayer serverPlayer) {
		PlayerDataLoader.loadData(serverPlayer);
		UnlockableTypes.init(serverPlayer);
		serverPlayer.setEventHandler(serverPlayer.getRegion().getEventHandler());
		VisibilityManager.onPlayerJoin(serverPlayer);
		TPSOptimizer.onPlayerJoin(serverPlayer);
	}

	private static ServerPlayer closePlayer(ServerPlayer serverPlayer) {
		PlayerDataLoader.updateData(serverPlayer);
		PacketWatcher.eject(serverPlayer);
		VisibilityManager.onPlayerQuit(serverPlayer);
		serverPlayer.close();
		return serverPlayer;
	}

	public static ServerPlayer onPlayerQuit(Player player) {
		ServerPlayer serverPlayer = unregisterPlayer(player);
		ServerRequest.getHandler().leavePartyOnQuit(serverPlayer);
		serverPlayer.getServerWorld().unregisterPlayer(serverPlayer);
		serverPlayer.getRegion().unregisterPlayer(serverPlayer);
		serverPlayer.removeEventHandler(QuitReason.QUIT);
		closePlayer(serverPlayer);
		return serverPlayer;
	}

	public static ServerPlayer onPlayerDisable(ServerPlayer serverPlayer) {
		Player player = serverPlayer.player;
		serverPlayer.removeEventHandler(QuitReason.DISABLE);
		closePlayer(serverPlayer);
		ChunkWatcher.disable(serverPlayer);
		if (player.getOpenInventory().getTopInventory().getHolder() instanceof Menu)
			player.closeInventory();
		return serverPlayer;
	}

	public static void onPlayerDeath(ServerPlayer serverPlayer) {
		serverPlayer.resetCustomStats();
		EquipmentListener.playerDeathCheck(serverPlayer);
	}

	public static void onPlayerChangedWorld(Player player, World from) {
		ServerPlayer serverPlayer = getPlayer(player);
		if (serverPlayer != null) { // so changing world on player quit doesn't get called here!
			@SuppressWarnings("unused")
			ServerWorld prevWorld = serverPlayer.getServerWorld();
			ServerWorld joinedWorld = ServerWorld.getByWorld(player.getWorld());
			serverPlayer.setServerWorld(joinedWorld);
			IRegion region = joinedWorld.getRegion(serverPlayer.player.getLocation());
			serverPlayer.setRegion(region);
			serverPlayer.setEventHandler(region.getEventHandler(), QuitReason.CHANGED_WORLD);
			ChunkWatcher.changeWorld(player, joinedWorld);
			serverPlayer.getEffectData().clearWorldData();
		}
	}

	public static void checkRegionChanged(@Nonnull ServerPlayer serverPlayer, @Nonnull Location toLocation) {
		IRegion region = serverPlayer.getServerWorld().getRegion(serverPlayer.player.getLocation());
		if (region != serverPlayer.getRegion()) {
			serverPlayer.setRegion(region);
			EventHandler handler = region.getEventHandler();
			if (serverPlayer.getEventHandler() != handler)
				serverPlayer.setEventHandler(handler, QuitReason.CHANGED_REGION);
		}
	}

	public static void applyResourcePack(Player player) {
		player.setResourcePack(RESOURCEPACK_LINK);
	}

	public static void applyDefaultResourcePack(Player player) {
		player.setResourcePack(DEFAULT_RESOURCEPACK_LINK);
	}

	public static ServerPlayer getPlayer(Player player) {
		return PLAYER_MAP.get(player);
	}

	public static ServerPlayer getPlayer(int bungeeID) {
		return BUNGEE_MAP.get(bungeeID);
	}

	public static ServerPlayer getPlayer(UUID uuid) {
		return getPlayer(Bukkit.getPlayer(uuid));
	}

	public static ServerPlayer getPlayer(String name) {
		return getPlayer(Bukkit.getPlayer(name));
	}

	public static List<ServerPlayer> getOnlinePlayer() {
		return LIST_VIEW;
	}
}