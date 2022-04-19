package com.pm.aiost.event.effect.effects;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.pm.aiost.event.effect.Effect;
import com.pm.aiost.event.effect.EffectAction;
import com.pm.aiost.event.effect.EffectCondition;
import com.pm.aiost.event.effect.EffectType;
import com.pm.aiost.event.effect.EffectTypes;
import com.pm.aiost.misc.menu.menus.request.NumberMenu;
import com.pm.aiost.misc.menu.request.MenuRequest;
import com.pm.aiost.misc.menu.request.requests.MultiMenuRequest.SimpleMultiMenuRequest;
import com.pm.aiost.misc.utils.nbt.custom.INBTTagCompound;
import com.pm.aiost.misc.utils.worldEdit.WorldEdit;
import com.pm.aiost.player.ServerPlayer;

public class BuildStaffEffect extends Effect {

	private static final byte[] ACTIONS = { EffectAction.CLICK };
	public static final int MAX_RADIUS = 16;

	private int radius;

	public BuildStaffEffect() {
		super(ACTIONS, EffectCondition.UNIQUE);
	}

	public BuildStaffEffect(int radius) {
		this();
		setRadius(radius);
	}

	@Override
	public void onPlayerInteract(ServerPlayer serverPlayer, PlayerInteractEvent event) {
		if (event.getClickedBlock() != null)
			buildStaffEffect(event.getAction(), event.getClickedBlock(), event.getBlockFace(), radius);
	}

	public static void buildStaffEffect(Action action, Block block, BlockFace blockFace, int radius) {
		if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK)
			WorldEdit.buildStaffBreakEffect(block, radius, radius, radius, blockFace);
		else if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)
			WorldEdit.buildStaffPlaceEffect(block, radius, radius, radius, blockFace);
	}

	@Override
	public boolean equals(Effect effect) {
		if (!super.equals(effect))
			return false;
		if (((BuildStaffEffect) effect).radius != radius)
			return false;
		return true;
	}

	@Override
	public void load(ConfigurationSection effectSection) {
		radius = effectSection.getInt("radius");
	}

	@Override
	public void load(Effect effect) {
		BuildStaffEffect buildStaff = (BuildStaffEffect) effect;
		if (buildStaff.radius != 0)
			radius = buildStaff.radius;
	}

	@Override
	public void load(INBTTagCompound nbt) {
		radius = nbt.getInt("radius");
	}

	@Override
	public INBTTagCompound save(INBTTagCompound nbt) {
		nbt.setInt("radius", radius);
		return nbt;
	}

	@SuppressWarnings("unchecked")
	@Override
	public MenuRequest getMenuRequest(ServerPlayer serverPlayer, Consumer<ServerPlayer> requestConsumer,
			Consumer<ServerPlayer> targetConsumer) {
		return new SimpleMultiMenuRequest(requestConsumer, targetConsumer,
				new Supplier[] { () -> new NumberMenu(BOLD + "Choose radius") },
				new Consumer[] { (radius) -> setRadius(((Double) radius).intValue()) });
	}

	@Override
	public EffectType<? extends BuildStaffEffect> getType() {
		return EffectTypes.BUILD_STAFF_EFFECT;
	}

	@Override
	public void setDefault() {
		super.setDefault();
		radius = MAX_RADIUS;
	}

	@Override
	public void createDescription(List<String> list) {
		super.createDescription(list);
		list.add(ChatColor.GRAY + "radius: " + ChatColor.DARK_GRAY + radius);
	}

	public void setRadius(int radius) {
		this.radius = radius < MAX_RADIUS ? radius : MAX_RADIUS;
	}

	public int getRadius() {
		return radius;
	}
}
