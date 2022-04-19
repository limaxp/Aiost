package com.pm.aiost.misc.utils.nms;

import com.pm.aiost.Aiost;

public class NMSUtils {

	public static final String VERSION;
	public static final String NET_MINECRAFT_SERVER_PACKAGE;
	public static final String NET_MINECRAFT_SERVER_PACKAGE_DOT;

	public static final String CRAFTBUKKIT_SERVER_PACKAGE;
	public static final String CRAFTBUKKIT_SERVER_PACKAGE_DOT;

	static {
		String serverPackageName = Aiost.getPlugin().getServer().getClass().getPackage().getName();
		VERSION = serverPackageName.substring(serverPackageName.lastIndexOf('.') + 1);
		NET_MINECRAFT_SERVER_PACKAGE = "net.minecraft.server." + VERSION;
		NET_MINECRAFT_SERVER_PACKAGE_DOT = NET_MINECRAFT_SERVER_PACKAGE + '.';

		CRAFTBUKKIT_SERVER_PACKAGE = "org.bukkit.craftbukkit." + VERSION;
		CRAFTBUKKIT_SERVER_PACKAGE_DOT = CRAFTBUKKIT_SERVER_PACKAGE + '.';
	}

	public static String getNMSClassPath(String className) {
		return NET_MINECRAFT_SERVER_PACKAGE_DOT + className;
	}

	public static String getCraftBukkitClassPath(String className) {
		return CRAFTBUKKIT_SERVER_PACKAGE_DOT + className;
	}

	public static Class<?> getNMSClass(String name) throws ClassNotFoundException {
		return Class.forName(NET_MINECRAFT_SERVER_PACKAGE_DOT + name);
	}

	public static Class<?> getCraftBukkitClass(String name) throws ClassNotFoundException {
		return Class.forName(CRAFTBUKKIT_SERVER_PACKAGE_DOT + name);
	}
}
