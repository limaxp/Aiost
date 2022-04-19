package com.pm.aiost.event.effect;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.pm.aiost.event.AiostEventFactory;
import com.pm.aiost.event.events.PlayerEquipItemEvent.EquipmentAction;
import com.pm.aiost.player.ServerPlayer;

public class EffectHelper {

	public static void decrementItemStack(ServerPlayer serverPlayer, ItemStack is, EquipmentSlot slot,
			Cancellable event) {
		if (serverPlayer.player.getGameMode() != GameMode.CREATIVE) {
			int amount = is.getAmount() - 1;
			if (amount > 0)
				is.setAmount(amount);
			else {
				PlayerInventory playerInv = serverPlayer.player.getInventory();
				if (slot == EquipmentSlot.HAND) {
					AiostEventFactory.callPlayerEquipHandItemEvent(serverPlayer, is, slot, EquipmentAction.CUSTOM);
					playerInv.setItemInMainHand(null);
				} else if (slot == EquipmentSlot.OFF_HAND) {
					AiostEventFactory.callPlayerEquipHandItemEvent(serverPlayer, is, slot, EquipmentAction.CUSTOM);
					playerInv.setItemInOffHand(null);
				}
			}
		}
	}

	public static Location blockFaceToLocation(Block block, BlockFace face) {
		Location loc = block.getLocation();
		switch (face) {
		case DOWN:
			loc.setY(loc.getY() - 1);
			break;
		case EAST:
			loc.setX(loc.getX() + 1);
			break;
		case NORTH:
			loc.setZ(loc.getZ() - 1);
			break;
		case SOUTH:
			loc.setZ(loc.getZ() + 1);
			break;
		case UP:
			loc.setY(loc.getY() + 1);
			break;
		case WEST:
			loc.setX(loc.getX() - 1);
			break;
		default:
			break;
		}
		return loc;
	}

	public static String getCardinalDirection(Location location) {
		double rotation = (location.getYaw() - 90) % 360;
		if (rotation < 0) {
			rotation += 360.0;
		}
		if (0 <= rotation && rotation < 22.5) {
			return "N";
		} else if (22.5 <= rotation && rotation < 67.5) {
			return "NE";
		} else if (67.5 <= rotation && rotation < 112.5) {
			return "E";
		} else if (112.5 <= rotation && rotation < 157.5) {
			return "SE";
		} else if (157.5 <= rotation && rotation < 202.5) {
			return "S";
		} else if (202.5 <= rotation && rotation < 247.5) {
			return "SW";
		} else if (247.5 <= rotation && rotation < 292.5) {
			return "W";
		} else if (292.5 <= rotation && rotation < 337.5) {
			return "NW";
		} else if (337.5 <= rotation && rotation < 360.0) {
			return "N";
		} else {
			return null;
		}
	}
}
