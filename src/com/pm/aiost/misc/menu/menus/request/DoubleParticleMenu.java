package com.pm.aiost.misc.menu.menus.request;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;
import static com.pm.aiost.misc.utils.ChatColor.RED;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.misc.menu.inventoryMenu.InventoryMenu;
import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.SingleInventoryMenu;
import com.pm.aiost.misc.menu.menus.request.creation.CreationMenus;
import com.pm.aiost.misc.menu.menus.request.enumeration.EnumerationMenus;
import com.pm.aiost.misc.menu.request.requests.SingleMenuRequest;
import com.pm.aiost.misc.particleEffect.particle.IParticle;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.player.ServerPlayer;

public class DoubleParticleMenu extends SingleInventoryMenu {

	private static final ItemStack NO_PARTICLE_1_ITEM = MetaHelper.setMeta(Material.BARRIER,
			RED + BOLD + "Particle 1 cannot be empty!", Arrays.asList(GRAY + "You have set particle 1"));

	private static final ItemStack NO_PARTICLE_2_ITEM = MetaHelper.setMeta(Material.BARRIER,
			RED + BOLD + "Particle 2 cannot be empty!", Arrays.asList(GRAY + "You have set particle 2"));

	protected IParticle[] particles;

	public DoubleParticleMenu() {
		super(BOLD + "Create double particle", 3, true);
		particles = new IParticle[2];
		set(MultiParticleMenu.CHOOSE_PARTICLE_ITEM_1.clone(), MultiParticleMenu.CHOOSE_PARTICLE_ITEM_2.clone(), null,
				null, null, MultiParticleMenu.RESET_ITEM, MultiParticleMenu.ACCEPT_ITEM);
		setBackLink(ServerPlayer::openMenuRequestPrevMenu);
	}

	@Override
	protected void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);

		ItemStack is = event.getCurrentItem();
		if (is != null) {
			switch (is.getType()) {

			case BLACK_BANNER:
				particleItemClick(serverPlayer, event);
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
	}

	private void particleItemClick(ServerPlayer serverPlayer, InventoryClickEvent event) {
		int index = InventoryMenu.convertSlotToIndex(event.getSlot());
		ClickType click = event.getClick();
		if (click == ClickType.LEFT || click == ClickType.SHIFT_LEFT)
			serverPlayer.doMenuRequest(new SingleMenuRequest(EnumerationMenus.PARTICLE_EFFECT_MENU) {

				@Override
				public void onResult(ServerPlayer serverPlayer, Object obj) {
					setParticle(index, (IParticle) obj);
				}

				@Override
				public void openRequest(ServerPlayer serverPlayer) {
					DoubleParticleMenu.this.open(serverPlayer);
				}
			});
		else if (click == ClickType.RIGHT || click == ClickType.SHIFT_RIGHT)
			serverPlayer.doMenuRequest(new SingleMenuRequest(CreationMenus.getParticleEffectMenu(serverPlayer)) {

				@Override
				public void onResult(ServerPlayer serverPlayer, Object obj) {
					setParticle(index, (IParticle) obj);
				}

				@Override
				public void openRequest(ServerPlayer serverPlayer) {
					DoubleParticleMenu.this.open(serverPlayer);
				}
			});
	}

	private void setParticle(int index, IParticle particle) {
		particles[index] = particle;
		setItem(index, particle);
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
		particles[0] = null;
		setItem(0, GRAY + BOLD + "Particle 1");
		particles[1] = null;
		setItem(1, GRAY + BOLD + "Particle 2");
	}

	private void accept(ServerPlayer serverPlayer, InventoryClickEvent event) {
		if (particles[0] == null)
			displayInSlot(event, NO_PARTICLE_1_ITEM, MultiParticleMenu.ACCEPT_ITEM, 30);
		else if (particles[1] == null)
			displayInSlot(event, NO_PARTICLE_2_ITEM, MultiParticleMenu.ACCEPT_ITEM, 30);
		else
			serverPlayer.setMenuRequestResult(particles);
	}
}