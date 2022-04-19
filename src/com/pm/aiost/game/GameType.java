package com.pm.aiost.game;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import org.bukkit.inventory.ItemStack;

import com.pm.aiost.event.eventHandler.EventHandler;
import com.pm.aiost.server.world.ServerWorld;

public class GameType<T extends Game> implements Supplier<EventHandler> {

	private static int runningIndex = 0;

	public final String name;
	private int id;
	public final int index;
	private final String[] lore;
	public final byte minPlayer;
	public final byte maxPlayer;
	public final ItemStack item;
	public final Supplier<T> constructor;
	public final Function<ServerWorld, String> requireFunction;

	public GameType(String name, String[] lore, byte minPlayer, byte maxPlayer, ItemStack is, Supplier<T> constructor,
			Function<ServerWorld, String> requireFunction) {
		this.name = name;
		this.index = runningIndex++;
		this.lore = lore;
		this.minPlayer = minPlayer;
		this.maxPlayer = maxPlayer;
		this.item = is;
		this.constructor = constructor;
		this.requireFunction = requireFunction;
	}

	public T get() {
		return constructor.get();
	}

	public String checkRequired(ServerWorld serverWorld) {
		return requireFunction.apply(serverWorld);
	}

	void setId(int id) {
		if (this.id == 0)
			this.id = id;
	}

	public int getId() {
		return id;
	}

	public List<String> getLore() {
		return Arrays.asList(lore);
	}
}