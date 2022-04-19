package com.pm.aiost.server.world.object.tileObject;

import java.util.List;
import java.util.function.Consumer;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import com.pm.aiost.misc.log.Logger;
import com.pm.aiost.misc.menu.Menu;
import com.pm.aiost.misc.menu.request.MenuRequest;
import com.pm.aiost.misc.menu.request.requests.NoMenuRequest.SimpleNoMenuRequest;
import com.pm.aiost.misc.utils.nbt.custom.INBTTagCompound;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.server.world.ServerWorld;
import com.pm.aiost.server.world.chunk.ServerChunk;

public abstract class TileObject implements Cloneable {

	protected ServerWorld world;
	public int x;
	public int y;
	public int z;

	public TileObject(ServerWorld world) {
		this.world = world;
	}

	public abstract TileObjectType<?> getTileObjectType();

	public void setPositionRotation(Location loc) {
		setPositionRotation(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
	}

	public void setPositionRotation(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void load(INBTTagCompound nbt) {
		x = nbt.getInt("x");
		y = nbt.getInt("y");
		z = nbt.getInt("z");
	}

	public INBTTagCompound save(INBTTagCompound nbt) {
		nbt.setInt("id", getTileObjectType().id);
		nbt.setInt("x", x);
		nbt.setInt("y", y);
		nbt.setInt("z", z);
		return nbt;
	}

	public void move(Location loc) {
		move(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
	}

	public void move(int x, int y, int z) {
		Block targetBlock = world.world.getBlockAt(x, y, z);
		if (targetBlock.getType() == Material.AIR) {
			Block block = getBlock();
			targetBlock.setBlockData(block.getBlockData());
			block.setType(Material.AIR);
			ServerChunk chunk1 = world.getChunk(this.x >> 4, this.z >> 4);
			ServerChunk chunk2 = world.getChunk(x >> 4, z >> 4);
			int effectId = chunk1.removeEffect(this.x, this.y, this.z);
			if (effectId != 0)
				chunk2.setEffect(x, y, z, effectId);
			chunk1.removeTileObject(this.x, this.y, this.z);
			this.x = x;
			this.y = y;
			this.z = z;
			chunk2.addTileObject(this);
		}
	}

	public String getName() {
		return getTileObjectType().name;
	}

	public Block getBlock() {
		return world.world.getBlockAt(x, y, z);
	}

	public void setWorld(ServerWorld world) {
		if (this.world == null)
			this.world = world;
	}

	public ServerWorld getWorld() {
		return world;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public MenuRequest getMenuRequest(ServerPlayer serverPlayer) {
		return getMenuRequest(serverPlayer, MenuRequest.EMPTY_CONSUMER);
	}

	public MenuRequest getMenuRequest(ServerPlayer serverPlayer, Menu requestMenu) {
		return getMenuRequest(serverPlayer, requestMenu::open);
	}

	public MenuRequest getMenuRequest(ServerPlayer serverPlayer, Consumer<ServerPlayer> requestConsumer) {
		return getMenuRequest(serverPlayer, requestConsumer, requestConsumer);
	}

	public MenuRequest getMenuRequest(ServerPlayer serverPlayer, Menu requestMenu, Menu resultMenu) {
		return getMenuRequest(serverPlayer, requestMenu::open, resultMenu::open);
	}

	public MenuRequest getMenuRequest(ServerPlayer serverPlayer, Consumer<ServerPlayer> requestConsumer,
			Consumer<ServerPlayer> targetConsumer) {
		return new SimpleNoMenuRequest(requestConsumer, targetConsumer);
	}

	public void createDescription(List<String> list) {
	}

	public void setDefault() {
	}

	@Override
	public TileObject clone() {
		try {
			return (TileObject) super.clone();
		} catch (CloneNotSupportedException e) {
			Logger.err("TileObject: Error! Clone failed for class + '" + getClass().getSimpleName() + "'", e);
			return null;
		}
	}
}
