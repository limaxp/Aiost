package com.pm.aiost.event.effect.effects;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.Vector;

import com.pm.aiost.event.effect.Effect;
import com.pm.aiost.event.effect.EffectType;
import com.pm.aiost.event.effect.EffectTypes;
import com.pm.aiost.event.effect.blueprints.SimpleBlockEffect;
import com.pm.aiost.misc.menu.menus.request.EffectActionMenu;
import com.pm.aiost.misc.menu.menus.request.EffectConditionMenu;
import com.pm.aiost.misc.menu.request.MenuRequest;
import com.pm.aiost.misc.menu.request.requests.MultiMenuRequest.SimpleMultiMenuRequest;
import com.pm.aiost.misc.utils.nbt.custom.INBTTagCompound;
import com.pm.aiost.player.ServerPlayer;

public class ShootBlockUpEffect extends SimpleBlockEffect {

	private double velocity;

	public ShootBlockUpEffect() {
	}

	public ShootBlockUpEffect(byte[] actions, byte condition, double velocity) {
		super(actions, condition);
		this.velocity = velocity;
	}

	@Override
	public void runEffect(Block block) {
		shootBlockUp(block.getRelative(BlockFace.DOWN), velocity);
	}

	@Override
	public void onProjectileHit(ProjectileHitEvent event) {
		Block block = event.getHitBlock() == null
				? event.getHitEntity().getLocation().getBlock().getRelative(BlockFace.DOWN)
				: event.getHitBlock();
		shootBlockUp(block, velocity);
	}

	public static void shootBlockUp(Block block, double velocity) {
		if (block.getType() != Material.AIR) {
			Location loc = block.getLocation().clone().add(0.5, 1, 0.5);
			block.getWorld().getNearbyEntities(loc, 1.0, 1.0, 1.0, (e) -> {
				e.setVelocity(new Vector(0, velocity, 0));
				return false;
			});

			FallingBlock fallingBlock = block.getWorld().spawnFallingBlock(loc.add(0, -1, 0), block.getBlockData());
			fallingBlock.setVelocity(new Vector(0, velocity * 0.25, 0));
			block.setType(Material.AIR);
		}
	}

	@Override
	public boolean equals(Effect effect) {
		if (!super.equals(effect))
			return false;
		if (((ShootBlockUpEffect) effect).velocity != velocity)
			return false;
		return true;
	}

	@Override
	public void load(ConfigurationSection effectSection) {
		super.load(effectSection);
		velocity = effectSection.getDouble("mana");
	}

	@Override
	public void load(Effect effect) {
		super.load(effect);
		ShootBlockUpEffect shootBlockUp = (ShootBlockUpEffect) effect;
		if (shootBlockUp.velocity != 0)
			velocity = shootBlockUp.velocity;
	}

	@Override
	public void load(INBTTagCompound nbt) {
		super.load(nbt);
		velocity = nbt.getDouble("velocity");
	}

	@Override
	public INBTTagCompound save(INBTTagCompound nbt) {
		super.save(nbt);
		nbt.setDouble("velocity", velocity);
		return nbt;
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
	public EffectType<? extends ShootBlockUpEffect> getType() {
		return EffectTypes.SHOOT_BLOCK_UP;
	}

	@Override
	public void setDefault() {
		super.setDefault();
		velocity = 1;
	}

	@Override
	public void createDescription(List<String> list) {
		super.createDescription(list);
		list.add(ChatColor.GRAY + "Velocity: " + ChatColor.DARK_GRAY + velocity);
	}

	public void setVelocity(double velocity) {
		this.velocity = velocity;
	}

	public double getVelocity() {
		return velocity;
	}
}