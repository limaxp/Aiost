package com.pm.aiost.misc.utils.nms;

import java.util.Iterator;

import org.bukkit.entity.Player;

import net.minecraft.server.v1_15_R1.EntityPlayer;

public class EntityPlayerIterator implements Iterator<Player>, Iterable<Player> {

	private final Iterator<EntityPlayer> iterator;

	public EntityPlayerIterator(Iterable<EntityPlayer> iterable) {
		this.iterator = iterable.iterator();
	}

	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}

	@Override
	public Player next() {
		return iterator.next().getBukkitEntity();
	}

	@Override
	public Iterator<Player> iterator() {
		return this;
	}
}
