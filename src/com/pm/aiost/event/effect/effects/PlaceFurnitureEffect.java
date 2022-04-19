package com.pm.aiost.event.effect.effects;

import java.util.function.Consumer;
import java.util.function.Supplier;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.event.effect.Effect;
import com.pm.aiost.event.effect.EffectHelper;
import com.pm.aiost.event.effect.EffectType;
import com.pm.aiost.event.effect.EffectTypes;
import com.pm.aiost.misc.menu.menus.request.EffectActionMenu;
import com.pm.aiost.misc.menu.menus.request.EffectConditionMenu;
import com.pm.aiost.misc.menu.request.MenuRequest;
import com.pm.aiost.misc.menu.request.requests.MultiMenuRequest.SimpleMultiMenuRequest;
import com.pm.aiost.misc.packet.object.PacketObjectTypes;
import com.pm.aiost.misc.packet.object.objects.Furniture;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.server.world.ServerWorld;

public class PlaceFurnitureEffect extends Effect {

	public PlaceFurnitureEffect() {
	}

	public PlaceFurnitureEffect(byte[] actions, byte condition) {
		super(actions, condition);
	}

	@Override
	public void onPlayerInteract(ServerPlayer serverPlayer, PlayerInteractEvent event) {
		if (event.getClickedBlock() == null)
			return;

		Block block = EffectHelper.blockFaceToLocation(event.getClickedBlock(), event.getBlockFace()).getBlock();
		ItemStack is = event.getPlayer().getInventory().getItem(event.getHand());
		if (placeFurniture(serverPlayer.player, block, is))
			EffectHelper.decrementItemStack(serverPlayer, is, event.getHand(), event);
	}

	public static boolean placeFurniture(Player player, Block block, ItemStack is) {
		if (player.isSneaking())
			return placeFurniture(block, player.getLocation().getYaw() - 180, is);
		else
			return placeFurniture(block, is);
	}

	public static boolean placeFurniture(Block block, float yaw, ItemStack is) {
		if (block.getType() == Material.AIR) {
			PacketObjectTypes.spawn(new Furniture(ServerWorld.getByWorld(block.getWorld()), is), block.getX(),
					block.getY(), block.getZ(), yaw);
			return true;
		}
		return false;
	}

	public static boolean placeFurniture(Block block, ItemStack is) {
		if (block.getType() == Material.AIR) {
			PacketObjectTypes.spawn(new Furniture(ServerWorld.getByWorld(block.getWorld()), is), block.getX(),
					block.getY(), block.getZ());
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
	public EffectType<? extends PlaceFurnitureEffect> getType() {
		return EffectTypes.PLACE_FURNITURE;
	}
}