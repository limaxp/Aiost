package com.pm.aiost.misc.menu.menus.request;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.GOLD;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;
import static com.pm.aiost.misc.utils.ChatColor.RED;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.misc.menu.inventoryMenu.InventoryMenu;
import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.SingleInventoryMenu;
import com.pm.aiost.misc.menu.menus.request.creation.CreationMenus;
import com.pm.aiost.misc.menu.menus.request.enumeration.EnumerationMenus;
import com.pm.aiost.misc.menu.request.requests.SingleMenuRequest;
import com.pm.aiost.misc.other.Banner;
import com.pm.aiost.misc.particleEffect.particle.IParticle;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.player.ServerPlayer;

public class MultiParticleMenu extends SingleInventoryMenu {

	static final List<String> CLICK_LORE = Arrays.asList(GRAY + "Left click to choose an existing particle",
			GRAY + "Right click to create a new particle");

	static final ItemStack CHOOSE_PARTICLE_ITEM_1 = Banner.create(Material.BLACK_BANNER, GRAY + BOLD + "Particle 1",
			CLICK_LORE, Banner.onePattern(DyeColor.BLACK, DyeColor.WHITE));

	static final ItemStack CHOOSE_PARTICLE_ITEM_2 = Banner.create(Material.BLACK_BANNER, GRAY + BOLD + "Particle 2",
			CLICK_LORE, Banner.twoPattern(DyeColor.BLACK, DyeColor.WHITE));

	static final ItemStack CHOOSE_PARTICLE_ITEM_3 = Banner.create(Material.BLACK_BANNER, GRAY + BOLD + "Particle 3",
			CLICK_LORE, Banner.threePattern(DyeColor.BLACK, DyeColor.WHITE));

	static final ItemStack CHOOSE_PARTICLE_ITEM_4 = Banner.create(Material.BLACK_BANNER, GRAY + BOLD + "Particle 4",
			CLICK_LORE, Banner.fourPattern(DyeColor.BLACK, DyeColor.WHITE));

	static final ItemStack CHOOSE_PARTICLE_ITEM_5 = Banner.create(Material.BLACK_BANNER, GRAY + BOLD + "Particle 5",
			CLICK_LORE, Banner.fivePattern(DyeColor.BLACK, DyeColor.WHITE));

	static final ItemStack RESET_ITEM = MetaHelper.setMeta(Material.LAVA_BUCKET, RED + BOLD + "Reset settings",
			Arrays.asList(GRAY + "Click to reset all settings"));

	static final ItemStack ACCEPT_ITEM = MetaHelper.setMeta(Material.NETHER_STAR, GOLD + BOLD + "Accept settings",
			Arrays.asList(GRAY + "Click to accept your settings"));

	static final ItemStack NO_PARTICLE_ITEM = MetaHelper.setMeta(Material.BARRIER,
			RED + BOLD + "Particles cannot be empty!", Arrays.asList(GRAY + "You have set at least one particle"));

	protected List<IParticle> particles;

	public MultiParticleMenu() {
		super(BOLD + "Create multi particle", 3, true);
		particles = new ArrayList<IParticle>();
		set(CHOOSE_PARTICLE_ITEM_1.clone(), CHOOSE_PARTICLE_ITEM_2.clone(), CHOOSE_PARTICLE_ITEM_3.clone(),
				CHOOSE_PARTICLE_ITEM_4.clone(), CHOOSE_PARTICLE_ITEM_5.clone(), RESET_ITEM, ACCEPT_ITEM);
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
		particleItemClick_(serverPlayer, event, index > particles.size() ? particles.size() : index);
	}

	private void particleItemClick_(ServerPlayer serverPlayer, InventoryClickEvent event, int index) {
		ClickType click = event.getClick();
		if (click == ClickType.LEFT || click == ClickType.SHIFT_LEFT)
			serverPlayer.doMenuRequest(new SingleMenuRequest(EnumerationMenus.PARTICLE_EFFECT_MENU) {

				@Override
				public void onResult(ServerPlayer serverPlayer, Object obj) {
					setParticle(index, (IParticle) obj);
				}

				@Override
				public void openRequest(ServerPlayer serverPlayer) {
					MultiParticleMenu.this.open(serverPlayer);
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
					MultiParticleMenu.this.open(serverPlayer);
				}
			});
	}

	private void setParticle(int index, IParticle particle) {
		if (index < particles.size())
			particles.set(index, particle);
		else
			particles.add(particle);
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
		int size = particles.size();
		for (int i = 0; i < size; i++)
			setItem(i, GRAY + BOLD + "Particle " + (i + 1));
		particles.clear();
	}

	private void accept(ServerPlayer serverPlayer, InventoryClickEvent event) {
		if (particles.size() > 0)
			serverPlayer.setMenuRequestResult(particles);
		else
			displayInSlot(event, NO_PARTICLE_ITEM, ACCEPT_ITEM, 30);
	}
}