package com.pm.aiost.event.effect.effects;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.block.BlockPlaceEvent;

import com.pm.aiost.event.effect.Effect;
import com.pm.aiost.event.effect.EffectAction;
import com.pm.aiost.event.effect.EffectCondition;
import com.pm.aiost.event.effect.EffectType;
import com.pm.aiost.misc.utils.nbt.custom.INBTTagCompound;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.server.world.object.tileObject.TileObject;

public class PlaceTileObjectEffect extends Effect {

	private static final byte[] ACTIONS = new byte[] { EffectAction.BLOCK_PLACE };

	private TileObject tileObject;

	public PlaceTileObjectEffect(TileObject tileObject) {
		super(ACTIONS, EffectCondition.NONE);
		this.tileObject = tileObject;
	}

	@Override
	public void onBlockPlace(ServerPlayer serverPlayer, BlockPlaceEvent event) {
		TileObject tileObject = this.tileObject.clone();
		tileObject.setWorld(serverPlayer.getServerWorld());
		tileObject.setPositionRotation(event.getBlock().getLocation());
		serverPlayer.getServerWorld().addTileObject(tileObject);
	}

	@Override
	public boolean equals(Effect effect) {
		if (!super.equals(effect))
			return false;
		PlaceTileObjectEffect placeEffectBlock = (PlaceTileObjectEffect) effect;
		if (placeEffectBlock.tileObject.equals(tileObject))
			return false;
		return true;
	}

	@Override
	public void load(ConfigurationSection effectSection) {
		super.load(effectSection);
	}

	@Override
	public void load(Effect effect) {
		super.load(effect);
		PlaceTileObjectEffect placeEffectBlock = (PlaceTileObjectEffect) effect;
		if (placeEffectBlock.tileObject != null)
			tileObject = placeEffectBlock.tileObject;
	}

	@Override
	public void load(INBTTagCompound nbt) {
	}

	@Override
	public INBTTagCompound save(INBTTagCompound nbt) {
		return nbt;
	}

	@Override
	public EffectType<? extends PlaceTileObjectEffect> getType() {
		return null;
	}

	@Override
	public void setDefault() {
		super.setDefault();
		tileObject = null;
	}

	@Override
	public void createDescription(List<String> list) {
		super.createDescription(list);
		list.add(ChatColor.GRAY + "Block effect: " + ChatColor.DARK_GRAY + tileObject);
	}
}
