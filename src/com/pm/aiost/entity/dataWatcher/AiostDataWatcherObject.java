package com.pm.aiost.entity.dataWatcher;

import net.minecraft.server.v1_15_R1.DataWatcherObject;
import net.minecraft.server.v1_15_R1.DataWatcherSerializer;

public class AiostDataWatcherObject<T> extends DataWatcherObject<T> {

	public AiostDataWatcherObject(int var0, DataWatcherSerializer<T> var1) {
		super(var0, var1);
	}
}
