package com.pm.aiost.event.effect.effects;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.pm.aiost.event.effect.Effect;
import com.pm.aiost.event.effect.EffectAction;
import com.pm.aiost.event.effect.EffectCondition;
import com.pm.aiost.event.effect.EffectHelper;
import com.pm.aiost.event.effect.EffectType;
import com.pm.aiost.event.effect.blueprints.SingletonEffect;
import com.pm.aiost.misc.menu.menus.request.MarkerMenu;
import com.pm.aiost.misc.menu.request.requests.SingleMenuRequest;
import com.pm.aiost.misc.packet.object.PacketObjectTypes;
import com.pm.aiost.misc.packet.object.objects.Marker;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.server.world.ServerWorld;

public class MarkerPlacerEffect extends SingletonEffect {

	private static final byte[] ACTIONS = { EffectAction.CLICK };

	private String name;

	public MarkerPlacerEffect() {
		super(ACTIONS, EffectCondition.NONE);
		name = com.pm.aiost.server.world.marker.Marker.SPAWN;
	}

	@Override
	public void onPlayerInteract(ServerPlayer serverPlayer, PlayerInteractEvent event) {
		if (serverPlayer.player.isSneaking()) {
			openMarkerMenu(serverPlayer);
			event.setCancelled(true);
			return;
		}
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			createMarker(name, EffectHelper.blockFaceToLocation(event.getClickedBlock(), event.getBlockFace()));
			event.setCancelled(true);
		}
	}

	public void openMarkerMenu(ServerPlayer serverPlayer) {
		serverPlayer.doMenuRequest(this, new SingleMenuRequest(MarkerMenu.MENU) {

			@Override
			protected void openRequest(ServerPlayer serverPlayer) {
				serverPlayer.closeInventory();
			}

			@Override
			protected void onResult(ServerPlayer serverPlayer, Object obj) {
				name = (String) obj;
			}
		});
	}

	public static void createMarker(String name, Block block) {
		if (block.getType() == Material.AIR) {
			Location loc = block.getLocation();
			ServerWorld serverWorld = ServerWorld.getByWorld(loc.getWorld());
			if (!serverWorld.hasPacketObject(loc)) {
				int index = serverWorld.addMarker(name, loc);
				PacketObjectTypes.spawn(new Marker(serverWorld, name, index), loc);
			}
		}
	}

	public static void createMarker(String name, Location loc) {
		if (loc.getBlock().getType() == Material.AIR) {
			ServerWorld serverWorld = ServerWorld.getByWorld(loc.getWorld());
			if (!serverWorld.hasPacketObject(loc)) {
				int index = serverWorld.addMarker(name, loc);
				PacketObjectTypes.spawn(new Marker(serverWorld, name, index), loc);
			}
		}
	}

	@Override
	public EffectType<? extends Effect> getType() {
		return null;
	}
}
