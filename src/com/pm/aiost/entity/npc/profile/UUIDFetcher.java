package com.pm.aiost.entity.npc.profile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.util.UUIDTypeAdapter;
import com.pm.aiost.misc.log.Logger;

public class UUIDFetcher {

	private static final String UUID_URL = "https://api.mojang.com/users/profiles/minecraft/%s?at=%d";

	private static final Gson GSON = new GsonBuilder().registerTypeAdapter(UUID.class, new UUIDTypeAdapter()).create();

	private static final Object2ObjectLinkedOpenHashMap<String, UUID> CACHE = new Object2ObjectLinkedOpenHashMap<String, UUID>();
	private static final int CACHE_SIZE = 256;

	private UUID id;

	public static UUID fetch(String name) {
		return fetch(name, System.currentTimeMillis());
	}

	public static UUID fetch(String name, long timestamp) {
		UUID uuid;
		if ((uuid = CACHE.get(name)) != null)
			return uuid;
		return fetchFromMojang(name, timestamp);
	}

	private static UUID fetchFromMojang(String name, long timestamp) {
		UUID uuid;
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(String.format(UUID_URL, name, timestamp / 1000))
					.openConnection();
			connection.setReadTimeout(5000);
			UUIDFetcher data = GSON.fromJson(new BufferedReader(new InputStreamReader(connection.getInputStream())),
					UUIDFetcher.class);
			uuid = data.id;
		} catch (Exception e) {
			Logger.err("UUIDFetcher: Error on fetching uuid for name '" + name + "'", e);
			return null;
		}
		cache(name, uuid);
		return uuid;
	}

	private static void cache(String name, UUID uuid) {
		if (CACHE.size() >= CACHE_SIZE)
			CACHE.removeLast();
		CACHE.putAndMoveToFirst(name, uuid);
	}
}
