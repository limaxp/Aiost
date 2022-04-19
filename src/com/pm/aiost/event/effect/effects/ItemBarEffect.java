package com.pm.aiost.event.effect.effects;

import org.bukkit.GameMode;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import com.pm.aiost.event.effect.EffectAction;
import com.pm.aiost.event.effect.EffectCondition;
import com.pm.aiost.event.effect.EffectType;
import com.pm.aiost.event.effect.blueprints.SingletonEffect;
import com.pm.aiost.player.ServerPlayer;

public class ItemBarEffect extends SingletonEffect {

	private static final byte[] ACTIONS = new byte[] { EffectAction.DEATH, EffectAction.ITEM_HELD,
			EffectAction.ITEM_SWAP, EffectAction.ITEM_DROP, EffectAction.INVENTORY_CLICK };
	public static final ItemBarEffect INSTANCE = new ItemBarEffect();

	protected ItemBarEffect() {
		super(ACTIONS, EffectCondition.UNIQUE);
	}

	@Override
	public void onPlayerDeath(ServerPlayer serverPlayer, PlayerDeathEvent event) {
		serverPlayer.clearItemBar();
	}

	@Override
	public void onPlayerItemHeld(ServerPlayer serverPlayer, PlayerItemHeldEvent event) {
		serverPlayer.clearItemBar();
	}

	@Override
	public void onPlayerSwapItems(ServerPlayer serverPlayer, PlayerSwapHandItemsEvent event) {
		serverPlayer.clearItemBar();
	}

	@Override
	public void onPlayerDropItem(ServerPlayer serverPlayer, PlayerDropItemEvent event) {
		serverPlayer.clearItemBarSilent();
	}

	@Override
	public void onInventoryClick(ServerPlayer serverPlayer, InventoryClickEvent event) {
		if (serverPlayer.player.getGameMode() == GameMode.CREATIVE)
			serverPlayer.clearItemBarCreative(event);
		else
			serverPlayer.clearItemBar();
	}

	@Override
	public EffectType<? extends SingletonEffect> getType() {
		return null;
	}
}
