package com.pm.aiost.misc.menu.menus.request;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.SingleInventoryMenu;
import com.pm.aiost.misc.menu.menus.request.creation.CreationMenus;
import com.pm.aiost.misc.menu.menus.request.enumeration.EnumerationMenus;
import com.pm.aiost.misc.menu.request.requests.SingleMenuRequest;
import com.pm.aiost.misc.particle.IParticle;
import com.pm.aiost.misc.particle.particles.AnimationParticle;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.player.ServerPlayer;

public class AnimationParticleMenu extends SingleInventoryMenu {

	private static final ItemStack CHOOSE_PARTICLE = MetaHelper.setMeta(Material.FIREWORK_STAR,
			GRAY + BOLD + "Change particle", MultiParticleMenu.CLICK_LORE);

	private static final ItemStack CHOOSE_ANIMATION = MetaHelper.setMeta(Material.REPEATER,
			GRAY + BOLD + "Change animation", Arrays.asList(GRAY + "Click to change animation"));

	protected AnimationParticle particle;

	public AnimationParticleMenu(AnimationParticle particle) {
		super(BOLD + "Create animation particle", 3, true);
		this.particle = particle;
		set(CHOOSE_PARTICLE.clone(), CHOOSE_ANIMATION.clone(), null, null, null, MultiParticleMenu.RESET_ITEM,
				MultiParticleMenu.ACCEPT_ITEM);
		setBackLink(ServerPlayer::openMenuRequestPrev);
		setClickCallback(this::menuClick);
	}

	private void menuClick(ServerPlayer serverPlayer, InventoryClickEvent event) {
		switch (event.getCurrentItem().getType()) {

		case FIREWORK_STAR:
			particleItemClick(serverPlayer, event);
			break;

		case REPEATER:
			animationItemClick(serverPlayer);
			break;

		case LAVA_BUCKET:
			reset();
			break;

		case NETHER_STAR:
			accept(serverPlayer, event);
			break;

		default:
			break;
		}
	}

	private void particleItemClick(ServerPlayer serverPlayer, InventoryClickEvent event) {
		ClickType click = event.getClick();
		if (click == ClickType.LEFT || click == ClickType.SHIFT_LEFT)
			serverPlayer.menuRequest(CHOOSE_PARTICLE, () -> new SingleMenuRequest(EnumerationMenus.PARTICLE_EFFECT_MENU,
					AnimationParticleMenu.this::open, false) {

				@Override
				public void onResult(ServerPlayer serverPlayer, Object obj) {
					setParticle((IParticle) obj);
				}
			});
		else if (click == ClickType.RIGHT || click == ClickType.SHIFT_RIGHT)
			serverPlayer.menuRequest(particle,
					() -> new SingleMenuRequest(CreationMenus.getParticleEffectMenu(serverPlayer),
							AnimationParticleMenu.this::open, false) {

						@Override
						public void onResult(ServerPlayer serverPlayer, Object obj) {
							setParticle((IParticle) obj);
						}
					});
	}

	private void animationItemClick(ServerPlayer serverPlayer) {
		serverPlayer.menuRequest(CHOOSE_ANIMATION, () -> particle.getAnimationMenuRequest(this));
	}

	private void setParticle(IParticle particle) {
		this.particle.setParticle(particle);
		setItem(0, particle);
	}

	private void setItem(int index, IParticle particle) {
		List<String> lore = new ArrayList<String>();
		lore.addAll(MultiParticleMenu.CLICK_LORE);
		lore.add(null);
		particle.createDescription(lore);
		MetaHelper.setMeta(getInventory().getItem(FIRST_BORDERED_SLOT + index),
				GRAY + BOLD + particle.getType().displayName, lore);
	}

	private void setItem(int index, String name) {
		MetaHelper.setMeta(getInventory().getItem(FIRST_BORDERED_SLOT + index), GRAY + BOLD + name,
				MultiParticleMenu.CLICK_LORE);
	}

	private void reset() {
		this.particle.setParticle(null);
		setItem(0, GRAY + BOLD + "Change particle");
	}

	private void accept(ServerPlayer serverPlayer, InventoryClickEvent event) {
		if (particle.getParticle() == null)
			displayInSlot(event, MultiParticleMenu.NO_PARTICLE_ITEM, MultiParticleMenu.ACCEPT_ITEM, 30);
		else
			serverPlayer.setMenuRequestResult(particle);
	}
}