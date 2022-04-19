package com.pm.aiost.entity.npc.profile;

import java.lang.reflect.Type;
import java.util.Map.Entry;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.util.UUIDTypeAdapter;

public class ProfileBuilder {

	private static final Gson GSON = new GsonBuilder().disableHtmlEscaping()
			.registerTypeAdapter(UUID.class, new UUIDTypeAdapter())
			.registerTypeAdapter(GameProfile.class, new GameProfileSerializer())
			.registerTypeAdapter(PropertyMap.class, new PropertyMap.Serializer()).create();

	public static GameProfile fromJson(String json) {
		return GSON.fromJson(json, GameProfile.class);
	}

	public static String toJson(GameProfile profile) {
		return GSON.toJson(profile, GameProfile.class);
	}

	public static GameProfile create(String name) {
		return new GameProfile(UUID.randomUUID(), name);
	}

	public static GameProfile create(String name, UUID uuid) {
		return new GameProfile(uuid, name);
	}

	public static GameProfile create(String name, String skinSignature, String skin) {
		return create(name, UUID.randomUUID(), skinSignature, skin);
	}

	public static GameProfile create(String name, UUID uuid, String skinSignature, String skin) {
		GameProfile profile = new GameProfile(uuid, name);
		profile.getProperties().put("textures", new Property("textures", skin, skinSignature));
		return profile;
	}

	public static GameProfile create(String name, String skinSignature, String skin, String capeSignature,
			String cape) {
		return create(name, UUID.randomUUID(), skinSignature, skin, capeSignature, cape);
	}

	public static GameProfile create(String name, UUID uuid, String skinSignature, String skin, String capeSignature,
			String cape) {
		GameProfile profile = new GameProfile(uuid, name);
		profile.getProperties().put("textures", new Property("textures", skin, skinSignature));
		profile.getProperties().put("textures", new Property("textures", cape, capeSignature));
		return profile;
	}

	public static GameProfile create(String name, GameProfile profile) {
		GameProfile result = new GameProfile(profile.getId(), name);
		PropertyMap resultPropertyMap = result.getProperties();
		for (Entry<String, Property> entry : profile.getProperties().entries())
			resultPropertyMap.put(entry.getKey(), entry.getValue());
		return result;
	}

	private static class GameProfileSerializer implements JsonSerializer<GameProfile>, JsonDeserializer<GameProfile> {

		public GameProfile deserialize(JsonElement json, Type type, JsonDeserializationContext context)
				throws JsonParseException {
			JsonObject object = (JsonObject) json;
			UUID id = object.has("id") ? (UUID) context.deserialize(object.get("id"), UUID.class) : null;
			String name = object.has("name") ? object.getAsJsonPrimitive("name").getAsString() : null;
			GameProfile profile = new GameProfile(id, name);
			if (object.has("properties")) {
				for (Entry<String, Property> prop : ((PropertyMap) context.deserialize(object.get("properties"),
						PropertyMap.class)).entries()) {
					profile.getProperties().put(prop.getKey(), prop.getValue());
				}
			}
			return profile;
		}

		public JsonElement serialize(GameProfile profile, Type type, JsonSerializationContext context) {
			JsonObject result = new JsonObject();
			if (profile.getId() != null)
				result.add("id", context.serialize(profile.getId()));
			if (profile.getName() != null)
				result.addProperty("name", profile.getName());
			if (!profile.getProperties().isEmpty())
				result.add("properties", context.serialize(profile.getProperties()));
			return result;
		}
	}
}
