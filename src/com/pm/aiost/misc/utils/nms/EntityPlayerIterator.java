package com.pm.aiost.misc.utils.nms;

import java.util.Iterator;

import org.bukkit.entity.Player;

public class EntityPlayerIterator implements Iterator<Player>, Iterable<Player> {

	private final Iterator<net.minecraft.world.entity.player.Player> iterator;

	public EntityPlayerIterator(Iterable<net.minecraft.world.entity.player.Player> iterable) {
		this.iterator = iterable.iterator();
	}

	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}

	@Override
	public Player next() {
		return (Player) iterator.next().getBukkitEntity();
	}

	@Override
	public Iterator<Player> iterator() {
		return this;
	}
}
