package com.pm.aiost.event.effect.effects;

import java.util.function.Consumer;
import java.util.function.Supplier;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.block.CustomBlock;
import com.pm.aiost.event.effect.Effect;
import com.pm.aiost.event.effect.EffectHelper;
import com.pm.aiost.event.effect.EffectType;
import com.pm.aiost.event.effect.EffectTypes;
import com.pm.aiost.misc.menu.menus.request.EffectActionMenu;
import com.pm.aiost.misc.menu.menus.request.EffectConditionMenu;
import com.pm.aiost.misc.menu.request.MenuRequest;
import com.pm.aiost.misc.menu.request.requests.MultiMenuRequest.SimpleMultiMenuRequest;
import com.pm.aiost.player.ServerPlayer;

public class PlaceCustomBlockEffect extends Effect {

	public PlaceCustomBlockEffect() {
	}

	public PlaceCustomBlockEffect(byte[] actions, byte condition) {
		super(actions, condition);
	}

	@Override
	public void onPlayerInteract(ServerPlayer serverPlayer, PlayerInteractEvent event) {
		if (event.getClickedBlock() == null)
			return;

		Block block = EffectHelper.blockFaceToLocation(event.getClickedBlock(), event.getBlockFace()).getBlock();
		ItemStack is = event.getPlayer().getInventory().getItem(event.getHand());
		if (placeCustomBlock(block, is))
			EffectHelper.decrementItemStack(serverPlayer, is, event.getHand(), event);
	}

	@Override
	public void onBlockPlace(ServerPlayer serverPlayer, BlockPlaceEvent event) {
		ItemStack is = event.getPlayer().getInventory().getItem(event.getHand());
		if (placeCustomBlock(event.getBlock(), is))
			EffectHelper.decrementItemStack(serverPlayer, is, EquipmentSlot.HAND, event);
	}

	public static boolean placeCustomBlock(Block block, ItemStack is) {
		if (block.getType() == Material.AIR) {
			CustomBlock.place(block, is);
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public MenuRequest getMenuRequest(ServerPlayer serverPlayer, Consumer<ServerPlayer> requestConsumer,
			Consumer<ServerPlayer> targetConsumer) {
		return new SimpleMultiMenuRequest(requestConsumer, targetConsumer,
				new Supplier[] { EffectActionMenu::new, EffectConditionMenu::getMenu },
				new Consumer[] { this::setActions, this::setCondition });
	}

	@Override
	public EffectType<? extends PlaceCustomBlockEffect> getType() {
		return EffectTypes.PLACE_CUSTOM_BLOCK;
	}
}
