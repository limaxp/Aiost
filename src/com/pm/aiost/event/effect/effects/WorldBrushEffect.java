package com.pm.aiost.event.effect.effects;

import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.pm.aiost.event.effect.Effect;
import com.pm.aiost.event.effect.EffectAction;
import com.pm.aiost.event.effect.EffectCondition;
import com.pm.aiost.event.effect.EffectType;
import com.pm.aiost.event.effect.blueprints.SingletonEffect;
import com.pm.aiost.misc.menu.menus.WorldBrushMenu;
import com.pm.aiost.misc.utils.worldEdit.Brush;
import com.pm.aiost.misc.utils.worldEdit.BrushData;
import com.pm.aiost.player.ServerPlayer;

public class WorldBrushEffect extends SingletonEffect {

	private static final int RANGE = 120;
	private static final byte[] ACTIONS = { EffectAction.CLICK };

	private Brush brush = Brush.SPHERE;
	private BrushData data;

	public WorldBrushEffect() {
		super(ACTIONS, EffectCondition.NONE);
		data = new BrushData();
	}

	@Override
	public void onPlayerInteract(ServerPlayer serverPlayer, PlayerInteractEvent event) {
		if (serverPlayer.player.isSneaking()) {
			openWorldBrushMenu(serverPlayer);
			event.setCancelled(true);
			return;
		}
		Action action = event.getAction();
		if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
			updateData(serverPlayer.player);
			brush.run(data, false);
			event.setCancelled(true);
		} else if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
			updateData(serverPlayer.player);
			brush.run(data, true);
			event.setCancelled(true);
		}
	}

	private void updateData(LivingEntity entity) {
		List<Block> lastTwoTargetBlocks = entity.getLastTwoTargetBlocks(null, RANGE);
		if (lastTwoTargetBlocks.size() != 2 || !lastTwoTargetBlocks.get(1).getType().isOccluding())
			return;
		Block adjacentBlock = lastTwoTargetBlocks.get(0);
		data.block = lastTwoTargetBlocks.get(1);
		data.face = data.block.getFace(adjacentBlock);
	}

	public void openWorldBrushMenu(ServerPlayer serverPlayer) {
		WorldBrushMenu menu = (WorldBrushMenu) serverPlayer.getOrCreateMenu(this, WorldBrushMenu::new);
		menu.setEffect(this);
		menu.open(serverPlayer);
	}

	@Override
	public EffectType<? extends Effect> getType() {
		return null;
	}

	public void setBrush(Brush brush) {
		this.brush = brush;
	}

	public Brush getBrush() {
		return brush;
	}

	public void setData(BrushData data) {
		this.data = data;
	}

	public BrushData getData() {
		return data;
	}
}
