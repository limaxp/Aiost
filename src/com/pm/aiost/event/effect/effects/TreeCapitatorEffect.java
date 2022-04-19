package com.pm.aiost.event.effect.effects;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.BlockBreakEvent;

import com.pm.aiost.block.AiostMaterial;
import com.pm.aiost.event.effect.EffectAction;
import com.pm.aiost.event.effect.EffectCondition;
import com.pm.aiost.event.effect.EffectType;
import com.pm.aiost.event.effect.EffectTypes;
import com.pm.aiost.event.effect.blueprints.SingletonEffect;
import com.pm.aiost.misc.utils.nms.NMS;
import com.pm.aiost.player.ServerPlayer;

public class TreeCapitatorEffect extends SingletonEffect {

	private static final byte[] ACTIONS = { EffectAction.BLOCK_BREAK };

	public static final TreeCapitatorEffect INSTANCE = new TreeCapitatorEffect();

	protected TreeCapitatorEffect() {
		super(ACTIONS, EffectCondition.MAIN_HAND);
	}

	@Override
	public void onBlockBreak(ServerPlayer serverPlayer, BlockBreakEvent event) {
		treeCapacitor(event.getBlock().getRelative(BlockFace.UP));
	}

	public void treeCapacitor(Block block) {
		if (block != null && NMS.getNMS(block).getMaterial() == AiostMaterial.WOOD) {
			block.breakNaturally();
			breakLeaves(block);
			treeCapacitor(block.getRelative(BlockFace.UP));
		}
	}

	public void breakLeaves(Block block) {
		breakLeave(block.getRelative(BlockFace.UP));
		breakLeave(block.getRelative(BlockFace.DOWN));
		breakLeave(block.getRelative(BlockFace.NORTH));
		breakLeave(block.getRelative(BlockFace.SOUTH));
		breakLeave(block.getRelative(BlockFace.EAST));
		breakLeave(block.getRelative(BlockFace.WEST));
	}

	public void breakLeave(Block block) {
		if (block != null && NMS.getNMS(block).getMaterial() == AiostMaterial.LEAVES) {
			block.breakNaturally();
			breakLeaves(block);
		}
	}

	@Override
	public EffectType<? extends TreeCapitatorEffect> getType() {
		return EffectTypes.TREE_CAPITATOR;
	}

	public static TreeCapitatorEffect getInstance() {
		return INSTANCE;
	}
}
