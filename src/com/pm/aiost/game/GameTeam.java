package com.pm.aiost.game;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Team;
import org.bukkit.scoreboard.Team.Option;
import org.bukkit.scoreboard.Team.OptionStatus;

import com.pm.aiost.misc.other.TeamData;

public class GameTeam extends TeamData {

	public final Game game;
	private int id;
	private Team team;
	private int points;

	public GameTeam(Game game) {
		this.game = game;
	}

	public GameTeam init(String name, ChatColor color, Material material) {
		return init(name, color, new ItemStack(material));
	}

	public GameTeam init(String name, ChatColor color, ItemStack item) {
		this.name = name;
		this.color = color;
		this.item = item;
		return this;
	}

	void register() {
		team = super.register(game.getScoreboard().scoreboard);
		game.onTeamCreation(this);
	}

	void unregister() {
		team.unregister();
		team = null;
	}

	void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public Team getTeam() {
		return team;
	}

	public boolean isAlive() {
		return team != null;
	}

	public int addPoints(int points) {
		this.points += points;
		if (game.getWinCondition().onTeamGetPoints(game, this, this.points))
			game.end();
		return this.points;
	}

	public int removePoints(int points) {
		return this.points -= points;
	}

	public int getPoints() {
		return points;
	}

	@Override
	public void load(ConfigurationSection section) {
		super.load(section);
		id = section.getInt("id");
		points = section.getInt("points");
		if (section.contains("bukkit")) {
			register();
			loadBukkit(section.getConfigurationSection("bukkit"));
		}
	}

	@Override
	public void save(ConfigurationSection section) {
		super.save(section);
		section.set("id", id);
		section.set("points", points);
		if (isAlive())
			saveBukkit(section.createSection("bukkit"));
	}

	private void loadBukkit(ConfigurationSection section) {
		team.setPrefix(section.getString("prefix"));
		team.setColor(ChatColor.getByChar(section.getString("color")));
		OptionStatus[] optionStatus = OptionStatus.values();
		team.setOption(Option.COLLISION_RULE, optionStatus[section.getInt("collision_rule")]);
		team.setOption(Option.DEATH_MESSAGE_VISIBILITY, optionStatus[section.getInt("death_message_visibility")]);
		team.setOption(Option.NAME_TAG_VISIBILITY, optionStatus[section.getInt("name_tag_visibility")]);

		List<String> entries = section.getStringList("entries");
		int size = entries.size();
		for (int i = 0; i < size; i++)
			team.addEntry(entries.get(i));
	}

	private void saveBukkit(ConfigurationSection section) {
		section.set("prefix", team.getPrefix());
		section.set("color", team.getColor().getChar());
		section.set("collision_rule", team.getOption(Option.COLLISION_RULE).ordinal());
		section.set("death_message_visibility", team.getOption(Option.DEATH_MESSAGE_VISIBILITY).ordinal());
		section.set("name_tag_visibility", team.getOption(Option.NAME_TAG_VISIBILITY).ordinal());

		List<String> entryList = new ArrayList<String>();
		for (String entry : team.getEntries())
			entryList.add(entry);
		section.set("entries", entryList);
	}

	public void databaseSave(StringBuilder builder) {
		builder.append(name);
		builder.append(',');
		builder.append(points);
	}

	public static GameTeam blue(Game game) {
		return game.createTeam("Blue", ChatColor.DARK_BLUE, Material.BLUE_BANNER);
	}

	public static GameTeam red(Game game) {
		return game.createTeam("Red", ChatColor.DARK_RED, Material.RED_BANNER);
	}

	public static GameTeam green(Game game) {
		return game.createTeam("Green", ChatColor.DARK_GREEN, Material.GREEN_BANNER);
	}

	public static GameTeam yellow(Game game) {
		return game.createTeam("Yellow", ChatColor.YELLOW, Material.YELLOW_BANNER);
	}

	public static GameTeam magenta(Game game) {
		return game.createTeam("Magenta", ChatColor.LIGHT_PURPLE, Material.MAGENTA_BANNER);
	}

	public static GameTeam cyan(Game game) {
		return game.createTeam("Cyan", ChatColor.AQUA, Material.CYAN_BANNER);
	}

	public static GameTeam orange(Game game) {
		return game.createTeam("Orange", ChatColor.GOLD, Material.ORANGE_BANNER);
	}

	public static GameTeam silver(Game game) {
		return game.createTeam("Silver", ChatColor.GRAY, Material.LIGHT_GRAY_BANNER);
	}

	public static GameTeam light_blue(Game game) {
		return game.createTeam("Light Blue", ChatColor.BLUE, Material.LIGHT_BLUE_BANNER);
	}

	public static GameTeam pink(Game game) {
		return game.createTeam("Pink", ChatColor.RED, Material.PINK_BANNER);
	}

	public static GameTeam lime(Game game) {
		return game.createTeam("Lime", ChatColor.GREEN, Material.LIME_BANNER);
	}

	public static GameTeam purple(Game game) {
		return game.createTeam("Purple", ChatColor.DARK_PURPLE, Material.PURPLE_BANNER);
	}

	public static GameTeam dark_cyan(Game game) {
		return game.createTeam("Dark Cyan", ChatColor.DARK_AQUA, Material.CYAN_BANNER);
	}

	public static GameTeam gray(Game game) {
		return game.createTeam("Gray", ChatColor.DARK_GRAY, Material.GRAY_BANNER);
	}

	public static GameTeam black(Game game) {
		return game.createTeam("Black", ChatColor.BLACK, Material.BLACK_BANNER);
	}

	public static GameTeam white(Game game) {
		return game.createTeam("White", ChatColor.WHITE, Material.WHITE_BANNER);
	}

	public static void twoTeams(Game game, List<GameTeam> list) {
		list.add(blue(game));
		list.add(red(game));
	}

	public static void fourTeams(Game game, List<GameTeam> list) {
		twoTeams(game, list);
		list.add(green(game));
		list.add(yellow(game));
	}

	public static void eightTeams(Game game, List<GameTeam> list) {
		fourTeams(game, list);
		list.add(magenta(game));
		list.add(cyan(game));
		list.add(orange(game));
		list.add(silver(game));
	}

	public static void allTeams(Game game, List<GameTeam> list) {
		eightTeams(game, list);
		list.add(light_blue(game));
		list.add(pink(game));
		list.add(lime(game));
		list.add(purple(game));
		list.add(dark_cyan(game));
		list.add(gray(game));
		list.add(black(game));
		list.add(white(game));
	}
}