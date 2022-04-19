package com.pm.aiost.misc.other;

import java.util.function.Consumer;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.pm.aiost.misc.utils.nbt.NBTHelper;
import com.pm.aiost.misc.utils.nbt.custom.NBTCompound;

public class TeamData {

	private static final Consumer<Team> NULL_CALLBACK = new Consumer<Team>() {
		@Override
		public void accept(Team t) {
		}
	};

	protected String name;
	protected ChatColor color;
	protected ItemStack item;
	private Consumer<Team> callback;

	public TeamData() {
		this.callback = NULL_CALLBACK;
	}

	public TeamData(String name, ChatColor color, Material material) {
		this(name, color, new ItemStack(material));
	}

	public TeamData(String name, ChatColor color, ItemStack item) {
		this.name = name;
		this.color = color;
		this.item = item;
		this.callback = NULL_CALLBACK;
	}

	public Team register(Scoreboard scoreboard) {
		Team team = scoreboard.registerNewTeam(name);
		team.setColor(color);
		callback.accept(team);
		return team;
	}

	public Team unregister(Scoreboard scoreboard) {
		Team team = scoreboard.getTeam(name);
		team.unregister();
		return team;
	}

	public String getName() {
		return name;
	}

	public ChatColor getColor() {
		return color;
	}

	public ItemStack getItem() {
		return item;
	}

	public void setCallback(Consumer<Team> callback) {
		this.callback = callback;
	}

	public void load(ConfigurationSection section) {
		name = section.getString("name");
		color = ChatColor.values()[section.getInt("color")];
		item = NBTHelper.loadItem(NBTHelper.fromString(section.getString("item")));
	}

	public void save(ConfigurationSection section) {
		section.set("name", name);
		section.set("color", color.ordinal());
		section.set("item", NBTHelper.saveItem(new NBTCompound(), item).toString());
	}
}
