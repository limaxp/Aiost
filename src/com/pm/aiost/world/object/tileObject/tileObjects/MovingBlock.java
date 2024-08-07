package com.pm.aiost.world.object.tileObject.tileObjects;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.DARK_GRAY;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.bukkit.ChatColor;
import org.bukkit.Location;

import com.pm.aiost.misc.menu.menus.request.LocationsMenu;
import com.pm.aiost.misc.menu.menus.request.NumberMenu;
import com.pm.aiost.misc.menu.request.MenuRequest;
import com.pm.aiost.misc.menu.request.requests.MultiMenuRequest.SimpleMultiMenuRequest;
import com.pm.aiost.misc.nms.NBT.NBTType;
import com.pm.aiost.misc.utils.LocationHelper;
import com.pm.aiost.misc.utils.Tickable;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.world.ServerWorld;
import com.pm.aiost.world.object.tileObject.TileObject;
import com.pm.aiost.world.object.tileObject.TileObjectType;
import com.pm.aiost.world.object.tileObject.TileObjectTypes;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

public class MovingBlock extends TileObject implements Tickable {

	protected List<Location> locations;
	protected int delay;
	protected int currentLoc;
	protected int counter;

	public MovingBlock(ServerWorld world) {
		super(world);
	}

	@Override
	public void tick() {
		if (counter++ >= delay) {
			counter = 0;
			if (currentLoc < locations.size())
				move(locations.get(currentLoc++));
			else {
				move(locations.get(0));
				currentLoc = 1;
			}
		}
	}

	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		locations = Arrays.asList(LocationHelper.load(nbt.getList("locations", NBTType.COMPOUND)));
		delay = nbt.getInt("delay");
	}

	@Override
	public CompoundTag save(CompoundTag nbt) {
		super.save(nbt);
		nbt.put("locations", LocationHelper.save(locations, new ListTag()));
		nbt.putInt("delay", delay);
		return nbt;
	}

	public void addLocation(Location location) {
		locations.add(location);
	}

	public void removeLocation(Location location) {
		locations.remove(location);
	}

	public void removeLocation(int index) {
		locations.remove(index);
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public int getDelay() {
		return delay;
	}

	@Override
	public TileObjectType<?> getTileObjectType() {
		return TileObjectTypes.MOVING_BLOCK;
	}

	@SuppressWarnings("unchecked")
	@Override
	public MenuRequest getMenuRequest(ServerPlayer serverPlayer, Consumer<ServerPlayer> requestConsumer,
			Consumer<ServerPlayer> targetConsumer) {
		return new SimpleMultiMenuRequest(requestConsumer, targetConsumer,
				new Supplier[] { () -> serverPlayer.getOrCreateMenu(LocationsMenu.class, LocationsMenu::new),
						() -> new NumberMenu(BOLD + "Choose delay value") },
				new Consumer[] { (locations) -> this.locations = (List<Location>) locations,
						(delay) -> this.delay = ((Double) delay).intValue() });
	}

	public void createDescription(List<String> list) {
		for (int i = 0; i < locations.size(); i++) {
			Location loc = locations.get(i);
			list.add(ChatColor.GRAY + "Location " + (i + 1) + ":");
			LocationHelper.createDescription(list, loc);
			list.add(null);
		}
		list.add(GRAY + "delay: " + DARK_GRAY + delay);
	}

	@Override
	public void setDefault() {
		locations = new ArrayList<Location>();
		delay = 0;
	}

	@Override
	public MovingBlock clone() {
		MovingBlock mb = (MovingBlock) super.clone();
		mb.locations = new ArrayList<Location>(locations);
		mb.delay = delay;
		return mb;
	}
}
