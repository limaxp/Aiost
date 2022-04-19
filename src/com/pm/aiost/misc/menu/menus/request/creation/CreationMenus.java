package com.pm.aiost.misc.menu.menus.request.creation;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.event.effect.Effect;
import com.pm.aiost.event.effect.EffectType;
import com.pm.aiost.misc.menu.Menu;
import com.pm.aiost.misc.menu.inventoryMenu.InventoryMenu;
import com.pm.aiost.misc.menu.menus.request.enumeration.EnumerationMenus;
import com.pm.aiost.misc.other.Banner;
import com.pm.aiost.misc.particleEffect.particle.IParticle;
import com.pm.aiost.misc.particleEffect.particle.ParticleType;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.server.world.object.tileObject.TileObject;
import com.pm.aiost.server.world.object.tileObject.TileObjectType;

public class CreationMenus {

	private static final ItemStack TILE_OBJECT_ITEM = Banner.create(Material.BLACK_BANNER,
			GRAY + BOLD + "Create block effect", Arrays.asList(GRAY + "Click to create a new block effect"),
			Banner.plusPattern(DyeColor.BLACK, DyeColor.WHITE));

	private static final ItemStack PARTICLE_EFFECT_ITEM = Banner.create(Material.BLACK_BANNER,
			GRAY + BOLD + "Create particle", Arrays.asList(GRAY + "Click to create a new particle"),
			Banner.plusPattern(DyeColor.BLACK, DyeColor.WHITE));

	private static final ItemStack EFFECT_ITEM = Banner.create(Material.BLACK_BANNER, GRAY + BOLD + "Create effect",
			Arrays.asList(GRAY + "Click to create a new effect"), Banner.plusPattern(DyeColor.BLACK, DyeColor.WHITE));

	public static Menu getTileObjectMenu(ServerPlayer serverPlayer) {
		return serverPlayer.getOrCreateMenu(TILE_OBJECT_ITEM, CreationMenus::createTileObjectMenu);
	}

	private static InventoryMenu createTileObjectMenu() {
		return new CreationMenu<TileObject>(BOLD + "Create block effect", TILE_OBJECT_ITEM,
				EnumerationMenus.TILE_OBJECT_TYPE_MENU) {

			@Override
			protected TileObject createValue(ServerPlayer serverPlayer, Object obj, int slot) {
				TileObject tileObject = ((TileObjectType<?>) obj).constructor.get(null);
				tileObject.setDefault();
				serverPlayer.doMenuRequest(tileObject.getClass(),
						tileObject.getMenuRequest(serverPlayer, (serverPlayer1) -> {
							setItem(tileObject, slot);
							open(serverPlayer1);
						}));
				return tileObject;
			}

			@Override
			protected ItemStack createItem(TileObject value) {
				List<String> lore = new ArrayList<String>();
				lore.add(GRAY + "Left click to choose block effect");
				lore.add(GRAY + "Right click to change block effect");
				lore.add(null);
				value.createDescription(lore);
				return MetaHelper.setMeta(new ItemStack(Material.RED_BANNER),
						GRAY + BOLD + value.getTileObjectType().displayName, lore);
			}
		};
	}

	public static Menu getParticleEffectMenu(ServerPlayer serverPlayer) {
		return serverPlayer.getOrCreateMenu(PARTICLE_EFFECT_ITEM, CreationMenus::createParticleEffectMenu);
	}

	private static InventoryMenu createParticleEffectMenu() {
		return new CreationMenu<IParticle>(BOLD + "Create particle effect", PARTICLE_EFFECT_ITEM,
				EnumerationMenus.PARTICLE_TYPE_MENU) {

			@Override
			protected IParticle createValue(ServerPlayer serverPlayer, Object obj, int slot) {
				IParticle particle = ((ParticleType<?>) obj).create();
				particle.setDefault();
				serverPlayer.doMenuRequest(particle.getClass(), particle.getMenuRequest((serverPlayer1) -> {
					setItem(particle, slot);
					open(serverPlayer1);
				}));
				return particle.init();
			}

			@Override
			protected ItemStack createItem(IParticle value) {
				List<String> lore = new ArrayList<String>();
				lore.add(GRAY + "Left click to choose particle");
				lore.add(GRAY + "Right click to change particle");
				lore.add(null);
				value.createDescription(lore);
				return MetaHelper.setMeta(new ItemStack(Material.RED_BANNER), GRAY + BOLD + value.getType().displayName,
						lore);
			}
		};
	}

	public static Menu getEffectMenu(ServerPlayer serverPlayer) {
		return serverPlayer.getOrCreateMenu(EFFECT_ITEM, CreationMenus::createEffectMenu);
	}

	private static InventoryMenu createEffectMenu() {
		return new CreationMenu<Effect>(BOLD + "Create effect", EFFECT_ITEM, EnumerationMenus.EFFECT_TYPE_MENU) {

			@Override
			protected Effect createValue(ServerPlayer serverPlayer, Object obj, int slot) {
				Effect effect = ((EffectType<?>) obj).create();
				effect.setDefault();
				serverPlayer.doMenuRequest(effect.getClass(), effect.getMenuRequest(serverPlayer, (serverPlayer1) -> {
					setItem(effect, slot);
					open(serverPlayer1);
				}));
				return effect;
			}

			@Override
			protected ItemStack createItem(Effect value) {
				List<String> lore = new ArrayList<String>();
				lore.add(GRAY + "Left click to choose effect");
				lore.add(GRAY + "Right click to change effect");
				lore.add(null);
				value.createDescription(lore);
				return MetaHelper.setMeta(new ItemStack(Material.RED_BANNER), GRAY + BOLD + value.getType().displayName,
						lore);
			}
		};
	}
}
