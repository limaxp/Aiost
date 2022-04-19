package com.pm.aiost.entity.npc.profile;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.HttpAuthenticationService;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService;
import com.mojang.authlib.yggdrasil.response.MinecraftProfilePropertiesResponse;
import com.mojang.util.UUIDTypeAdapter;
import com.pm.aiost.misc.log.Logger;

public class ProfileFetcher {

	private static final String AUTH_SERVER_URL = "https://sessionserver.mojang.com/session/minecraft/profile/";
	private static Method MAKE_REQUEST;

	private static final Object2ObjectLinkedOpenHashMap<UUID, GameProfile> CACHE = new Object2ObjectLinkedOpenHashMap<UUID, GameProfile>();
	private static final int CACHE_SIZE = 256;

	static {
		try {
			MAKE_REQUEST = YggdrasilAuthenticationService.class.getDeclaredMethod("makeRequest",
					new Class[] { URL.class, Object.class, Class.class });
			MAKE_REQUEST.setAccessible(true);
		} catch (Exception e) {
			Logger.err("ProfileFetcher: Error on makeRequest method reflection!", e);
		}
	}

	public static GameProfile fetch(String name, boolean requireSecure) {
		return fetch(name, UUIDFetcher.fetch(name), true);
	}

	public static GameProfile fetch(String name, UUID uuid, boolean requireSecure) {
		GameProfile profile;
		if ((profile = CACHE.get(uuid)) != null)
			return profile;
		return fetchFromMojang(name, uuid, requireSecure);
	}

	private static GameProfile fetchFromMojang(String name, UUID uuid, boolean requireSecure) {
		MinecraftSessionService sessionService = ((CraftServer) Bukkit.getServer()).getServer()
				.getMinecraftSessionService();
		if (!(sessionService instanceof YggdrasilMinecraftSessionService)) {
			GameProfile profile = new GameProfile(uuid, name);
			sessionService.fillProfileProperties(profile, requireSecure);
			cache(uuid, profile);
			return profile;
		}

		YggdrasilAuthenticationService auth = ((YggdrasilMinecraftSessionService) sessionService)
				.getAuthenticationService();
		URL url = HttpAuthenticationService.constantURL(AUTH_SERVER_URL + UUIDTypeAdapter.fromUUID(uuid));
		url = HttpAuthenticationService.concatenateURL(url, "unsigned=" + !requireSecure);

		try {
			MinecraftProfilePropertiesResponse response = (MinecraftProfilePropertiesResponse) MAKE_REQUEST.invoke(auth,
					url, null, MinecraftProfilePropertiesResponse.class);
			if (response == null)
				return new GameProfile(uuid, name);
			GameProfile profile = new GameProfile(response.getId(), response.getName());
			profile.getProperties().putAll(response.getProperties());
			cache(uuid, profile);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			Logger.err("ProfileFetcher: Error on fetching profile for id '" + uuid + "'", e);
		}
		return new GameProfile(uuid, name);
	}

	private static void cache(UUID uuid, GameProfile profile) {
		if (CACHE.size() >= CACHE_SIZE)
			CACHE.removeLast();
		CACHE.putAndMoveToFirst(uuid, profile);
	}
}
