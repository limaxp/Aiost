package com.pm.aiost.event.effect.effects;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

import com.pm.aiost.event.effect.Effect;
import com.pm.aiost.event.effect.EffectType;
import com.pm.aiost.event.effect.EffectTypes;
import com.pm.aiost.event.effect.blueprints.SimplePlayerBlockEffect;
import com.pm.aiost.misc.menu.menus.request.EffectActionMenu;
import com.pm.aiost.misc.menu.menus.request.EffectConditionMenu;
import com.pm.aiost.misc.menu.menus.request.NumberMenu;
import com.pm.aiost.misc.menu.request.MenuRequest;
import com.pm.aiost.misc.menu.request.requests.MultiMenuRequest.SimpleMultiMenuRequest;
import com.pm.aiost.misc.utils.nbt.custom.INBTTagCompound;
import com.pm.aiost.misc.utils.worldEdit.WorldEdit;
import com.pm.aiost.player.ServerPlayer;

public class BreakPlaneEffect extends SimplePlayerBlockEffect {

	private int radius;

	public BreakPlaneEffect() {
	}

	public BreakPlaneEffect(byte[] actions, byte condition, int radius) {
		super(actions, condition);
		this.radius = radius;
	}

	@Override
	public void runEffect(ServerPlayer serverPlayer, Block block) {
		WorldEdit.breakPlane(serverPlayer.player, block, radius, radius, radius);
	}

	@Override
	public void onProjectileHit(ProjectileHitEvent event) {
		if (event.getHitBlock() != null)
			WorldEdit.breakPlane(event.getHitBlock(), radius, radius, radius, event.getHitBlockFace());
	}

	@Override
	public void onBlockBreak(ServerPlayer serverPlayer, BlockBreakEvent event) {
		WorldEdit.breakPlane(event.getBlock(), radius, radius, radius, serverPlayer.lastInteractedBlockface);
	}

	@Override
	public boolean equals(Effect effect) {
		if (!super.equals(effect))
			return false;
		if (((BreakPlaneEffect) effect).radius != radius)
			return false;
		return true;
	}

	@Override
	public void load(ConfigurationSection effectSection) {
		super.load(effectSection);
		radius = effectSection.getInt("radius");
	}

	@Override
	public void load(Effect effect) {
		super.load(effect);
		BreakPlaneEffect breakBlocks = (BreakPlaneEffect) effect;
		if (breakBlocks.radius != 0)
			radius = breakBlocks.radius;
	}

	@Override
	public void load(INBTTagCompound nbt) {
		super.load(nbt);
		radius = nbt.getInt("radius");
	}

	@Override
	public INBTTagCompound save(INBTTagCompound nbt) {
		super.save(nbt);
		nbt.setInt("radius", radius);
		return nbt;
	}

	@SuppressWarnings("unchecked")
	@Override
	public MenuRequest getMenuRequest(ServerPlayer serverPlayer, Consumer<ServerPlayer> requestConsumer,
			Consumer<ServerPlayer> targetConsumer) {
		return new SimpleMultiMenuRequest(requestConsumer, targetConsumer,
				new Supplier[] { EffectActionMenu::new, EffectConditionMenu::getMenu,
						() -> new NumberMenu(BOLD + "Choose radius") },
				new Consumer[] { this::setActions, this::setCondition,
						(radius) -> this.radius = ((Double) radius).intValue() });
	}

	@Override
	public EffectType<? extends BreakPlaneEffect> getType() {
		return EffectTypes.BREAK_PLANE;
	}

	@Override
	public void setDefault() {
		super.setDefault();
		radius = 1;
	}

	@Override
	public void createDescription(List<String> list) {
		super.createDescription(list);
		list.add(ChatColor.GRAY + "Radius: " + ChatColor.DARK_GRAY + radius);
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public int getRadius() {
		return radius;
	}
}
