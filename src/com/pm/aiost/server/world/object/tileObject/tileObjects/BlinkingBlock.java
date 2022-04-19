package com.pm.aiost.server.world.object.tileObject.tileObjects;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;

import com.pm.aiost.misc.menu.menus.request.NumberMenu;
import com.pm.aiost.misc.menu.menus.request.enumeration.EnumerationMenus;
import com.pm.aiost.misc.menu.request.MenuRequest;
import com.pm.aiost.misc.menu.request.requests.MultiMenuRequest.SimpleMultiMenuRequest;
import com.pm.aiost.misc.utils.nbt.custom.INBTTagCompound;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.server.world.ServerWorld;
import com.pm.aiost.server.world.object.TickingObject;
import com.pm.aiost.server.world.object.tileObject.TileObject;
import com.pm.aiost.server.world.object.tileObject.TileObjectType;
import com.pm.aiost.server.world.object.tileObject.TileObjectTypes;

public class BlinkingBlock extends TileObject implements TickingObject {

	protected Material material;
	protected int delay;
	protected int counter;

	public BlinkingBlock(ServerWorld world) {
		super(world);
	}

	@Override
	public void tick() {
		if (counter++ >= delay) {
			counter = 0;
			Block block = getBlock();
			if (block.getType() == Material.AIR)
				block.setType(material);
			else
				block.setType(Material.AIR);
		}
	}

	@Override
	public void load(INBTTagCompound nbt) {
		super.load(nbt);
		material = Material.valueOf(nbt.getString("material"));
		delay = nbt.getInt("delay");
	}

	@Override
	public INBTTagCompound save(INBTTagCompound nbt) {
		super.save(nbt);
		nbt.setString("material", material.name());
		nbt.setInt("delay", delay);
		return nbt;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public Material getMaterial() {
		return material;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public int getDelay() {
		return delay;
	}

	@Override
	public TileObjectType<?> getTileObjectType() {
		return TileObjectTypes.BLINKING_BLOCK;
	}

	@SuppressWarnings("unchecked")
	@Override
	public MenuRequest getMenuRequest(ServerPlayer serverPlayer, Consumer<ServerPlayer> requestConsumer,
			Consumer<ServerPlayer> targetConsumer) {
		return new SimpleMultiMenuRequest(requestConsumer, targetConsumer,
				new Supplier[] { () -> EnumerationMenus.BLOCK_MENU, () -> new NumberMenu(BOLD + "Choose delay value") },
				new Consumer[] { (material) -> this.material = (Material) material,
						(delay) -> this.delay = ((Double) delay).intValue() });
	}

	@Override
	public void createDescription(List<String> list) {
		list.add(ChatColor.GRAY + "material: " + ChatColor.DARK_GRAY + material.name());
		list.add(ChatColor.GRAY + "delay: " + ChatColor.DARK_GRAY + (delay * 0.25D));
	}

	@Override
	public void setDefault() {
		material = Material.STONE;
		delay = 0;
	}

	@Override
	public TileObject clone() {
		BlinkingBlock bb = (BlinkingBlock) super.clone();
		bb.material = material;
		bb.delay = delay;
		return bb;
	}
}
