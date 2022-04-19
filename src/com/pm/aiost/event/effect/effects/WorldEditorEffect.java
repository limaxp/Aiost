package com.pm.aiost.event.effect.effects;

import org.bukkit.block.Block;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.pm.aiost.event.effect.Effect;
import com.pm.aiost.event.effect.EffectAction;
import com.pm.aiost.event.effect.EffectCondition;
import com.pm.aiost.event.effect.EffectType;
import com.pm.aiost.event.effect.blueprints.SingletonEffect;
import com.pm.aiost.misc.menu.menus.WorldEditorMenu;
import com.pm.aiost.player.ServerPlayer;

public class WorldEditorEffect extends SingletonEffect {

	private static final byte[] ACTIONS = { EffectAction.CLICK };

	private Block block1;
	private Block block2;

	public WorldEditorEffect() {
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
			openWorldEditorMenu(serverPlayer, block1, block2);
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

	public void openWorldEditorMenu(ServerPlayer serverPlayer, Block block1, Block block2) {
		WorldEditorMenu menu = (WorldEditorMenu) serverPlayer.getOrCreateMenu(this, WorldEditorMenu::new);
		menu.setBlock1(block1);
		menu.setBlock2(block2);
		menu.open(serverPlayer);
	}

	@Override
	public EffectType<? extends Effect> getType() {
		return null;
	}

	public void setBlock1(Block block1) {
		this.block1 = block1;
	}

	public Block getBlock1() {
		return block1;
	}

	public void setBlock2(Block block2) {
		this.block2 = block2;
	}

	public Block getBlock2() {
		return block2;
	}
}
