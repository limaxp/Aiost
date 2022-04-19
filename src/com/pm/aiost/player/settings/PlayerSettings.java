package com.pm.aiost.player.settings;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.objects.Object2IntMap;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

import com.pm.aiost.entity.npc.profile.Profiles;
import com.pm.aiost.misc.command.commands.PlayerCommands.DisguisePlayerCommand;
import com.pm.aiost.misc.command.commands.PlayerCommands.DisguiseRemoveCommand;
import com.pm.aiost.misc.packet.disguise.disguises.DisguisePlayer;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.player.handler.VisibilityManager;

public class PlayerSettings {

	private static final int START_SIZE = 10;
	private static final Object2IntMap<String> NAME_MAP = new Object2IntOpenHashMap<String>(START_SIZE);
	private static short[] defaultValues = new short[START_SIZE];
	private static SettingInfo[] infos = new SettingInfo[START_SIZE];
	private static SettingChangedCallback[] callbacks = new SettingChangedCallback[START_SIZE];
	private static int runningId;

	public static final int TRAIL = a(new SettingInfo("trail"), 0);

	public static final int PARTICLE_EFFECT = a(new SettingInfo("particleEffect"), 0);

	public static final int PROJECTILE_PARTICLE = a(new SettingInfo("projectileParticle"), 0);

	public static final int MORPH = a(new SettingInfo("morph"), 0);

	public static final int PET = a(new SettingInfo("pet"), 0);

	public static final int HAT = a(new SettingInfo("hat"), 0);

	public static final int HIDE_CHAT = a(new SettingInfo("hide_chat", "Hide chat", Arrays.asList("Hides chat")), 0);

	public static final int VISIBILITY = a(
			new SettingInfo("Visibility",
					Arrays.asList("Toggles player visibility", "This will completely hide you and other players")),
			1, (serverPlayer, value) -> {
				if (value < 1)
					VisibilityManager.setInvisible(serverPlayer);
				else
					VisibilityManager.setVisible(serverPlayer);
			});

	public static final int CAN_SEE_INVISIBLES = a(new SettingInfo("can_see_invisibles", "Can see invisibles",
			Arrays.asList("Allows player to see hidden players")), 0, (serverPlayer, value) -> {
				if (value < 1)
					VisibilityManager.hideInvisibles(serverPlayer);
				else
					VisibilityManager.showInvisibles(serverPlayer);
			});

	public static final int DISGUISED = a(
			new SettingInfo("Disguised", Arrays.asList("Disguises you as some other player")), 0,
			(serverPlayer, value) -> {
				if (value < 1)
					DisguiseRemoveCommand.removeDisguise(serverPlayer);
				else
					DisguisePlayerCommand.setDisguise(serverPlayer, new DisguisePlayer(Profiles.getRandom()));
			});

	public static int a(SettingInfo info, int defaultValue) {
		return a(info, defaultValue, SettingChangedCallback.NULL);
	}

	public static int a(SettingInfo info, int defaultValue, SettingChangedCallback callback) {
		int id = runningId;
		register(info, (short) defaultValue, callback);
		return id;
	}

	public static void register(SettingInfo info, short defaultValue) {
		register(info, defaultValue, SettingChangedCallback.NULL);
	}

	public static void register(SettingInfo info, short defaultValue, SettingChangedCallback callback) {
		ensureCapacity(runningId);
		int id = runningId++;
		NAME_MAP.put(info.name, id);
		defaultValues[id] = defaultValue;
		infos[id] = info;
		callbacks[id] = callback;
	}

	public static void register(ConfigurationSection section) {
		Collection<String> settingNames = section.getKeys(false);
		ensureCapacity(runningId + settingNames.size());

		int i = runningId;
		for (String settingName : settingNames) {
			Object obj = section.get(settingName);
			if (obj instanceof ConfigurationSection) {
				ConfigurationSection settingSection = (ConfigurationSection) obj;
				defaultValues[i] = (byte) settingSection.getInt("value");
				infos[i] = new SettingInfo(settingName, settingSection.getString("name"),
						settingSection.getStringList("lore"));
			} else if (obj instanceof Integer) {
				defaultValues[i] = ((Integer) obj).byteValue();
				infos[i] = new SettingInfo(settingName);
			}
			NAME_MAP.put(infos[i].name, i);
			callbacks[i] = SettingChangedCallback.NULL;
			i++;
		}
		runningId = i;
	}

	public static int get(String name) {
		return NAME_MAP.getInt(name);
	}

	public static int getIgnoreCase(String name) {
		return NAME_MAP.getInt(name.toLowerCase());
	}

	public static short getDefaultValue(int id) {
		return defaultValues[id];
	}

	public static SettingInfo getInfo(int id) {
		return infos[id];
	}

	public static String getName(int id) {
		return infos[id].name;
	}

	public static String getDisplayName(int id) {
		return infos[id].displayName;
	}

	public static List<String> getDescription(int id) {
		return infos[id].description;
	}

	public static void setCallback(int id, SettingChangedCallback callback) {
		callbacks[id] = callback;
	}

	public static SettingChangedCallback getCallback(int id) {
		return callbacks[id];
	}

	public static int size() {
		return runningId;
	}

	protected static void ensureCapacity(int minCapacity) {
		int length = defaultValues.length;
		if (minCapacity > length)
			resize(Math.max(length * 2, minCapacity));
	}

	protected static void resize(int size) {
		short[] newDefaultValues = new short[size];
		System.arraycopy(defaultValues, 0, newDefaultValues, 0, runningId);
		defaultValues = newDefaultValues;

		SettingInfo[] newInfos = new SettingInfo[size];
		System.arraycopy(infos, 0, newInfos, 0, runningId);
		infos = newInfos;

		SettingChangedCallback[] newCallbacks = new SettingChangedCallback[size];
		System.arraycopy(callbacks, 0, newCallbacks, 0, runningId);
		callbacks = newCallbacks;
	}

	public static class SettingInfo {

		public final String name;
		public final String displayName;
		public final List<String> description;

		private SettingInfo(String name) {
			this(name, name, null);
		}

		private SettingInfo(String name, String displayName) {
			this(name, displayName, null);
		}

		private SettingInfo(String name, List<String> description) {
			this(name, name, description);
		}

		private SettingInfo(String name, String displayName, List<String> description) {
			this.name = name.toLowerCase();
			this.displayName = displayName;
			this.description = description;
		}
	}

	@FunctionalInterface
	public static interface SettingChangedCallback {

		public static final SettingChangedCallback NULL = (serverPlayer, value) -> {
		};

		public void accept(ServerPlayer serverPlayer, short value);
	}
}
