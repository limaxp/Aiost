package com.pm.aiost.misc.dataAccess;

public class DataAccess {

	private static SpigotDataAccess access;

	public static <T extends SpigotDataAccess> void init(T dataAccess) {
		if (DataAccess.access == null)
			DataAccess.access = dataAccess;
	}

	public static SpigotDataAccess getAccess() {
		return access;
	}
}
