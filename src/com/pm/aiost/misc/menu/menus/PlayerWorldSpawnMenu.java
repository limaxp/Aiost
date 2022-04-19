package com.pm.aiost.misc.menu.menus;

import static com.pm.aiost.misc.utils.ChatColor.BLUE;
import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;
import static com.pm.aiost.misc.utils.ChatColor.GREEN;
import static com.pm.aiost.misc.utils.ChatColor.PURPLE;
import static com.pm.aiost.misc.utils.ChatColor.RED;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.entity.AiostEntityTypes;
import com.pm.aiost.misc.menu.inventoryMenu.InventoryMenu;
import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.SingleInventoryMenu;
import com.pm.aiost.misc.menu.menus.request.CreateTextMenu;
import com.pm.aiost.misc.menu.menus.request.creation.CreationMenus;
import com.pm.aiost.misc.menu.menus.request.enumeration.EnumerationMenus;
import com.pm.aiost.misc.menu.request.requests.CallbackMenuRequest;
import com.pm.aiost.misc.packet.entity.PacketEntity;
import com.pm.aiost.misc.packet.entity.PacketEntityTypes;
import com.pm.aiost.misc.packet.entity.entities.EntityHologram;
import com.pm.aiost.misc.packet.entity.entities.ParticleSpawner;
import com.pm.aiost.misc.particleEffect.particle.IParticle;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.player.ServerPlayer;

import net.minecraft.server.v1_15_R1.EntityTypes;

public class PlayerWorldSpawnMenu {

	private static final Object CREATE_TEXT_MENU_IDENTIFIER = new Object();
	private static final Object ENTITY_TYPE_MENU_IDENTIFIER = new Object();
	private static final Object PARTICLE_EFFECT_MENU_IDENTIFIER = new Object();
	private static final Object CREATE_PARTICLE_EFFECT_MENU_IDENTIFIER = new Object();

	private static final InventoryMenu MENU = createMenu();

	private static InventoryMenu createMenu() {
		InventoryMenu menu = new SingleInventoryMenu(BOLD + "Spawn Menu", 3, true);
		menu.set(
				MetaHelper.setMeta(Material.ARMOR_STAND, PURPLE + BOLD + "Text",
						Arrays.asList(GRAY + "Click to spawn a text")),

				MetaHelper.setMeta(Material.ZOMBIE_HEAD, BLUE + BOLD + "Entities",
						Arrays.asList(GRAY + "Click to spawn entities")),

				MetaHelper.setMeta(Material.PLAYER_HEAD, GREEN + BOLD + "Deco",
						Arrays.asList(GRAY + "Click to spawn deco entities")),

				MetaHelper.setMeta(Material.LAVA_BUCKET, RED + BOLD + "Particle effect",
						Arrays.asList(GRAY + "Left click to spawn an existing particle",
								GRAY + "Right click to create and spawn a new particle",
								GRAY + "Shift click to delete near particles")));
		menu.setInventoryClickCallback(PlayerWorldSpawnMenu::menuClick);
		menu.setBackLink(ServerPlayer::openEventHandlerMenu);
		return menu;
	}

	private static void menuClick(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack is = event.getCurrentItem();
		if (is != null) {
			switch (is.getType()) {

			case ARMOR_STAND:
				serverPlayer.doMenuRequest(CREATE_TEXT_MENU_IDENTIFIER, () -> new CallbackMenuRequest(
						serverPlayer.getOrCreateMenu(CreateTextMenu.class, CreateTextMenu::new)) {

					@SuppressWarnings("unchecked")
					@Override
					public void onResult(ServerPlayer serverPlayer, Object obj) {
						spawnHologram(serverPlayer, (List<String>) obj);
					}

					@Override
					public void openRequest(ServerPlayer serverPlayer) {
						MENU.open(serverPlayer);
					}
				});
				break;

			case ZOMBIE_HEAD:
				serverPlayer.doMenuRequest(ENTITY_TYPE_MENU_IDENTIFIER,
						() -> new CallbackMenuRequest(EnumerationMenus.ENTITY_TYPE_MENU) {

							@Override
							public void onResult(ServerPlayer serverPlayer, Object obj) {
								spawnEntity(serverPlayer, (EntityTypes<?>) obj);
							}

							@Override
							public void openRequest(ServerPlayer serverPlayer) {
								MENU.open(serverPlayer);
							}
						});
				break;

			case PLAYER_HEAD:
				PlayerWorldSpawnDecoMenu.getMenu().open(serverPlayer);
				break;

			case LAVA_BUCKET:
				ClickType click = event.getClick();
				if (click == ClickType.LEFT)
					serverPlayer.doMenuRequest(PARTICLE_EFFECT_MENU_IDENTIFIER,
							() -> new CallbackMenuRequest(EnumerationMenus.PARTICLE_EFFECT_MENU) {

								@Override
								public void onResult(ServerPlayer serverPlayer, Object obj) {
									spawnParticleEffect(serverPlayer, (IParticle) obj);
								}

								@Override
								public void openRequest(ServerPlayer serverPlayer) {
									MENU.open(serverPlayer);
								}
							});
				else if (click == ClickType.RIGHT)
					serverPlayer.doMenuRequest(CREATE_PARTICLE_EFFECT_MENU_IDENTIFIER,
							() -> new CallbackMenuRequest(CreationMenus.getParticleEffectMenu(serverPlayer)) {

								@Override
								public void onResult(ServerPlayer serverPlayer, Object obj) {
									spawnParticleEffect(serverPlayer, (IParticle) obj);
								}

								@Override
								public void openRequest(ServerPlayer serverPlayer) {
									MENU.open(serverPlayer);
								}
							});
				else if (click == ClickType.SHIFT_LEFT || click == ClickType.SHIFT_RIGHT)
					for (PacketEntity packetEntity : serverPlayer.getServerWorld().getPacketEntities(
							serverPlayer.player.getLocation(), 2, PacketEntityTypes.PARTICLE_SPAWNER))
						packetEntity.remove();
				break;

			default:
				break;
			}
		}
	}

	public static void spawnEntity(ServerPlayer serverPlayer, EntityTypes<?> type) {
		AiostEntityTypes.spawnEntity(type, serverPlayer.player.getLocation());
	}

	public static void spawnParticleEffect(ServerPlayer serverPlayer, IParticle particle) {
		PacketEntityTypes.spawn(new ParticleSpawner(serverPlayer.getServerWorld(), particle),
				serverPlayer.player.getLocation());
	}

	private static void spawnHologram(ServerPlayer serverPlayer, List<String> text) {
		PacketEntityTypes.spawn(new EntityHologram(serverPlayer.getServerWorld(), text),
				serverPlayer.player.getLocation());
		serverPlayer.player.closeInventory();
	}

	public static InventoryMenu getMenu() {
		return MENU;
	}
}