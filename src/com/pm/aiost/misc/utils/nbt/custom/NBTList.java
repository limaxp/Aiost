package com.pm.aiost.misc.utils.nbt.custom;

import net.minecraft.server.v1_15_R1.NBTTagList;

public class NBTList extends NBTTagList implements INBTTagList {

	public NBTList() {
	}

	@Override
	public NBTTagList getNMS() {
		return this;
	}
}
