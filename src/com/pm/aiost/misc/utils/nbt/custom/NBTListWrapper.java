package com.pm.aiost.misc.utils.nbt.custom;

import java.io.DataOutput;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import net.minecraft.server.v1_15_R1.IChatBaseComponent;
import net.minecraft.server.v1_15_R1.NBTBase;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import net.minecraft.server.v1_15_R1.NBTTagList;
import net.minecraft.server.v1_15_R1.NBTTagType;

public class NBTListWrapper implements INBTTagList {

	public final NBTTagList nbtList;

	public NBTListWrapper(NBTTagList nbtList) {
		this.nbtList = nbtList;
	}

	@Override
	public NBTTagList getNMS() {
		return nbtList;
	}

	@Override
	public IChatBaseComponent a(String var0, int var1) {
		return nbtList.a(var0, var1);
	}

	@Override
	public NBTTagType<?> b() {
		return nbtList.b();
	}

	@Override
	public NBTBase clone() {
		return nbtList.clone();
	}

	@Override
	public void write(DataOutput var0) throws IOException {
		nbtList.write(var0);
	}

	@Override
	public byte getTypeId() {
		return nbtList.getTypeId();
	}

	@Override
	public NBTBase set(int paramInt, NBTBase paramT) {
		return nbtList.set(paramInt, paramT);
	}

	@Override
	public void add(int paramInt, NBTBase paramT) {
		nbtList.add(paramInt, paramT);
	}

	@Override
	public NBTBase remove(int paramInt) {
		return nbtList.remove(paramInt);
	}

	@Override
	public NBTBase get(int var0) {
		return nbtList.get(var0);
	}

	@Override
	public boolean a(int paramInt, NBTBase paramNBTBase) {
		return nbtList.a(paramInt, paramNBTBase);
	}

	@Override
	public boolean b(int paramInt, NBTBase paramNBTBase) {
		return nbtList.b(paramInt, paramNBTBase);
	}

	@Override
	public NBTTagCompound getCompound(int var0) {
		return nbtList.getCompound(var0);
	}

	@Override
	public String getString(int var0) {
		return nbtList.getString(var0);
	}

	@Override
	public int size() {
		return nbtList.size();
	}

	@Override
	public boolean isEmpty() {
		return nbtList.isEmpty();
	}

	@Override
	public boolean equals(Object obj) {
		return nbtList.equals(obj);
	}

	@Override
	public void clear() {
		nbtList.clear();
	}

	@Override
	public boolean add(NBTBase e) {
		return nbtList.add(e);
	}

	@Override
	public boolean addAll(Collection<? extends NBTBase> c) {
		return nbtList.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends NBTBase> c) {
		return nbtList.addAll(index, c);
	}

	@Override
	public boolean contains(Object o) {
		return nbtList.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return nbtList.containsAll(c);
	}

	@Override
	public int indexOf(Object o) {
		return nbtList.indexOf(o);
	}

	@Override
	public Iterator<NBTBase> iterator() {
		return nbtList.iterator();
	}

	@Override
	public int lastIndexOf(Object o) {
		return nbtList.lastIndexOf(o);
	}

	@Override
	public ListIterator<NBTBase> listIterator() {
		return nbtList.listIterator();
	}

	@Override
	public ListIterator<NBTBase> listIterator(int index) {
		return nbtList.listIterator(index);
	}

	@Override
	public boolean remove(Object o) {
		return nbtList.remove(o);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return nbtList.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return nbtList.retainAll(c);
	}

	@Override
	public List<NBTBase> subList(int fromIndex, int toIndex) {
		return nbtList.subList(fromIndex, toIndex);
	}

	@Override
	public Object[] toArray() {
		return nbtList.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return nbtList.toArray(a);
	}
}
