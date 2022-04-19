package com.pm.aiost.server.world.object.tileObject.tileObjects;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.DARK_GRAY;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;

import com.pm.aiost.misc.menu.menus.request.NumberMenu;
import com.pm.aiost.misc.menu.request.MenuRequest;
import com.pm.aiost.misc.menu.request.requests.MultiMenuRequest.SimpleMultiMenuRequest;
import com.pm.aiost.misc.utils.meta.MetaData;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.server.world.ServerWorld;
import com.pm.aiost.server.world.object.TickingObject;
import com.pm.aiost.server.world.object.tileObject.TileObject;
import com.pm.aiost.server.world.object.tileObject.TileObjectType;
import com.pm.aiost.server.world.object.tileObject.TileObjectTypes;

public class ProximityFallingBlock extends TileObject implements TickingObject {

	public static final int MAX_RANGE = 16;

	protected double damage; // TODO: make damage work!
	protected double range;

	public ProximityFallingBlock(ServerWorld world) {
		super(world);
	}

	@Override
	public void tick() {
		Location loc = new Location(world.world, x + 0.5D, y, z + 0.5D);
		for (@SuppressWarnings("unused")
		Entity entity : world.world.getNearbyEntities(loc, range, range, range, (entity) -> entity instanceof Player)) {
			dropFallingBlock(loc);
			break;
		}
	}

	protected void dropFallingBlock(Location loc) {
		Block block = getBlock();
		world.world.spawnFallingBlock(loc, block.getBlockData());
		block.setType(Material.AIR);
		world.removeTileObject(this.x, this.y, this.z);
	}

	public void setDamage(double damage) {
		this.damage = damage;
	}

	public double getDamage() {
		return damage;
	}

	public void setRange(double range) {
		this.range = range < MAX_RANGE ? range : MAX_RANGE;
	}

	public double getRange() {
		return range;
	}

	@Override
	public TileObjectType<?> getTileObjectType() {
		return TileObjectTypes.PROXIMITY_FALLING_BLOCK;
	}

	@SuppressWarnings("unchecked")
	@Override
	public MenuRequest getMenuRequest(ServerPlayer serverPlayer, Consumer<ServerPlayer> requestConsumer,
			Consumer<ServerPlayer> targetConsumer) {
		return new SimpleMultiMenuRequest(requestConsumer, targetConsumer,
				new Supplier[] { () -> new NumberMenu(BOLD + "Choose damage value"),
						() -> new NumberMenu(BOLD + "Choose range value") },
				new Consumer[] { (damage) -> this.damage = (Double) damage, (range) -> setRange((Double) range) });
	}

	public void createDescription(List<String> list) {
		list.add(ChatColor.GRAY + "damage: " + DARK_GRAY + damage);
		list.add(ChatColor.GRAY + "range: " + DARK_GRAY + range);
	}

	@Override
	public void setDefault() {
		damage = 0;
		range = 8;
	}

	@Override
	public ProximityFallingBlock clone() {
		ProximityFallingBlock pfb = (ProximityFallingBlock) super.clone();
		pfb.damage = damage;
		pfb.range = range;
		return pfb;
	}

	public static class ConstantProximityFallingBlock extends ProximityFallingBlock {

		protected boolean isFalling;

		public ConstantProximityFallingBlock(ServerWorld world) {
			super(world);
		}

		@Override
		public void tick() {
			if (!isFalling)
				super.tick();
		}

		@Override
		protected void dropFallingBlock(Location loc) {
			isFalling = true;
			Block block = getBlock();
			FallingBlock fallingBlock = world.world.spawnFallingBlock(loc, block.getBlockData());
			block.setType(Material.AIR);
			fallingBlock.setDropItem(false);
			MetaData.set(fallingBlock, "ConstantProximityFallingBlock", this);
		}

		public void hitFallingBlock(FallingBlock fallingBlock) {
			isFalling = false;
			getBlock().setBlockData(fallingBlock.getBlockData(), true);
			fallingBlock.getWorld().spawnParticle(Particle.BLOCK_CRACK, fallingBlock.getLocation(), 10, 0.0, 0.0, 0.0,
					0.0, fallingBlock.getBlockData(), false);
		}

		@Override
		public TileObjectType<?> getTileObjectType() {
			return TileObjectTypes.CONSTANT_PROXIMITY_FALLING_BLOCK;
		}
	}
}
