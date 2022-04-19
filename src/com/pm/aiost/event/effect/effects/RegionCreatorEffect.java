package com.pm.aiost.event.effect.effects;

import org.bukkit.block.Block;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.pm.aiost.event.effect.Effect;
import com.pm.aiost.event.effect.EffectAction;
import com.pm.aiost.event.effect.EffectCondition;
import com.pm.aiost.event.effect.EffectType;
import com.pm.aiost.event.effect.blueprints.SingletonEffect;
import com.pm.aiost.misc.menu.menus.CreateRegionMenu;
import com.pm.aiost.player.ServerPlayer;

public class RegionCreatorEffect extends SingletonEffect {

	private static final byte[] ACTIONS = { EffectAction.CLICK };

	private Block block1;
	private Block block2;

	public RegionCreatorEffect() {
		super(ACTIONS, EffectCondition.NONE);
	}

	@Override
	public void onPlayerInteract(ServerPlayer serverPlayer, PlayerInteractEvent event) {
		if (serverPlayer.player.isSneaking()) {
			if (block1 == null) {
				serverPlayer.sendMessage("position 1 is missing!");
				return;
			}
			if (block2 == null) {
				serverPlayer.sendMessage("position 2 is missing!");
				return;
			}
			openCreateRegionMenu(serverPlayer, block1, block2);
			event.setCancelled(true);
			return;
		}
		Action action = event.getAction();
		if (action == Action.LEFT_CLICK_BLOCK) {
			block1 = event.getClickedBlock();
			serverPlayer
					.sendMessage("position 1 set to " + block1.getX() + " " + +block1.getY() + " " + +block1.getZ());
			event.setCancelled(true);
		} else if (action == Action.RIGHT_CLICK_BLOCK) {
			block2 = event.getClickedBlock();
			serverPlayer
					.sendMessage("position 2 set to " + block2.getX() + " " + +block2.getY() + " " + +block2.getZ());
			event.setCancelled(true);
		}
	}

	public void openCreateRegionMenu(ServerPlayer serverPlayer, Block block1, Block block2) {
		CreateRegionMenu menu = (CreateRegionMenu) serverPlayer.getOrCreateMenu(this, CreateRegionMenu::new);
		menu.setLocation1(block1.getLocation());
		menu.setLocation2(block2.getLocation());
		menu.open(serverPlayer);
	}

	@Override
	public EffectType<? extends Effect> getType() {
		return null;
	}
}
