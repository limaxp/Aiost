package com.pm.aiost.event.effect.effects;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;

import com.pm.aiost.event.effect.Effect;
import com.pm.aiost.event.effect.EffectType;
import com.pm.aiost.event.effect.EffectTypes;
import com.pm.aiost.event.effect.blueprints.SimpleBlockEffect;
import com.pm.aiost.misc.menu.menus.request.EffectActionMenu;
import com.pm.aiost.misc.menu.menus.request.EffectConditionMenu;
import com.pm.aiost.misc.menu.menus.request.NumberMenu;
import com.pm.aiost.misc.menu.request.MenuRequest;
import com.pm.aiost.misc.menu.request.requests.MultiMenuRequest.SimpleMultiMenuRequest;
import com.pm.aiost.misc.utils.nbt.custom.INBTTagCompound;
import com.pm.aiost.misc.utils.worldEdit.WorldEdit;
import com.pm.aiost.player.ServerPlayer;

public class BreakBallEffect extends SimpleBlockEffect {

	private int radius;

	public BreakBallEffect() {
	}

	public BreakBallEffect(byte[] actions, byte condition, int radius) {
		super(actions, condition);
		this.radius = radius;
	}

	@Override
	public void runEffect(Block block) {
		WorldEdit.breakBall(block, radius);
	}

	@Override
	public boolean equals(Effect effect) {
		if (!super.equals(effect))
			return false;
		if (((BreakBallEffect) effect).radius != radius)
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
		BreakBallEffect breakBall = (BreakBallEffect) effect;
		if (breakBall.radius != 0)
			radius = breakBall.radius;
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
	public EffectType<? extends BreakBallEffect> getType() {
		return EffectTypes.BREAK_BALL;
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
