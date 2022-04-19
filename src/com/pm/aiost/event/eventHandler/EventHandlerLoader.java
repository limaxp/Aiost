package com.pm.aiost.event.eventHandler;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.pm.aiost.misc.log.Logger;

public class EventHandlerLoader {

	private final File file;
	private FileConfiguration config;
	private boolean forceSave;
	private boolean hasChanged;
	private boolean hasDefaultEventHandler;

	public EventHandlerLoader(File file) {
		this.file = file;
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				Logger.err("EventHandlerLoader: Error on event handler file creation!", e);
			}
		}
		config = YamlConfiguration.loadConfiguration(file);
	}

	public EventHandler load() {
		hasDefaultEventHandler = false;
		if (config.contains("forceSave"))
			forceSave = config.getBoolean("forceSave");

		if (config.contains("WorldEventHandler"))
			return load(config.getConfigurationSection("WorldEventHandler"));
		hasDefaultEventHandler = true;
		return EventHandlerManager.getDefault();
	}

	public void save(EventHandler eventHandler) {
		if (hasDefaultEventHandler)
			return;
		if (hasChanged) {
			saveEventHandler(eventHandler);
			hasChanged = false;
		} else if (eventHandler.hasChanged())
			saveEventHandler(eventHandler);
	}

	private void saveEventHandler(EventHandler eventHandler) {
		config.set("WorldEventHandler", null);
		save(config.createSection("WorldEventHandler"), eventHandler);
		saveFile();
	}

	private void saveFile() {
		try {
			config.save(file);
		} catch (IOException e) {
			Logger.err("EventHandlerLoader: Error on saving event handler config file!", e);
		}
	}

	public void setForceSave(boolean forceSave) {
		config.set("forceSave", forceSave);
		this.forceSave = forceSave;
	}

	public boolean doesForceSave() {
		return forceSave;
	}

	public void setHasChanged() {
		this.hasChanged = true;
	}

	public void setDefaultEventHandler(boolean isDefault) {
		this.hasDefaultEventHandler = isDefault;
	}

	public boolean hasDefaultEventHandler() {
		return hasDefaultEventHandler;
	}

	public static EventHandler load(ConfigurationSection section) {
		EventHandler handler = EventHandler.create(section.getString("eventHandler"));
		handler.load(section);
		return handler;
	}

	public static void save(ConfigurationSection section, EventHandler eventHandler) {
		section.set("eventHandler", eventHandler.getEventHandlerName());
		eventHandler.save(section);
	}
}
