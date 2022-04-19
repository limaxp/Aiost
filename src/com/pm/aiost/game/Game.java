package com.pm.aiost.game;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.DARK_RED;
import static com.pm.aiost.misc.utils.ChatColor.YELLOW;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.IntArrayList;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;
import org.bukkit.scoreboard.Team.Option;
import org.bukkit.scoreboard.Team.OptionStatus;

import com.pm.aiost.Aiost;
import com.pm.aiost.collection.list.IdentityArrayList;
import com.pm.aiost.collection.list.UnorderedIdentityArrayList;
import com.pm.aiost.event.eventHandler.EventHandler;
import com.pm.aiost.event.eventHandler.handler.SpectatorEventHandler;
import com.pm.aiost.event.events.PacketThingAttackEvent;
import com.pm.aiost.game.data.IGameData;
import com.pm.aiost.misc.dataAccess.DataAccess;
import com.pm.aiost.misc.log.Logger;
import com.pm.aiost.misc.menu.Menu;
import com.pm.aiost.misc.menu.menus.DatabaseGameMenu.GameData;
import com.pm.aiost.misc.menu.menus.GameRateMenu;
import com.pm.aiost.misc.menu.menus.GameSettingMenu;
import com.pm.aiost.misc.menu.menus.request.TextMenu;
import com.pm.aiost.misc.menu.request.requests.CallbackMenuRequest;
import com.pm.aiost.misc.scoreboard.scoreboards.GameScoreboard;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.server.request.ServerRequest;
import com.pm.aiost.server.world.region.IRegion;

public abstract class Game implements AutoCloseable, IGameData, EventHandler {

	// TODO: reload doesn't work after game ending!

	private static final int TIME_TO_END = 10;

	public static enum GameState {
		PRE_GAME, IN_GAME, SUDDEN_DEATH, END;
	}

	private ServerPlayer host;
	private int id;
	private UUID uuid;
	private String name;
	private String authorName;
	private IRegion region;
	private int minPlayer;
	private int maxPlayer;
	private String password;
	private GameScoreboard scoreboard;
	private BossBar bossBar;
	private BukkitRunnable scheduler;
	private final List<ServerPlayer> player;
	private List<GamePlayer> playerData;
	private List<GameTeam> teams;
	private List<GameTeam> usedTeams;
	private List<GameTeam> unusedTeams;
	private Team spectatorTeam;
	private int time;
	private int endTime;
	private GameKit[] kits;
	private GameState gameState;
	private int startLives;
	private WinCondition winCondition;
	private EndAction endAction;
	private Menu settingMenu;

	public Game() {
		scoreboard = createScoreboard();
		bossBar = createBossbar();
		player = new UnorderedIdentityArrayList<ServerPlayer>();
		playerData = new UnorderedIdentityArrayList<GamePlayer>();
		teams = new IdentityArrayList<GameTeam>();
		usedTeams = new UnorderedIdentityArrayList<GameTeam>();
		unusedTeams = new UnorderedIdentityArrayList<GameTeam>();
		kits = createKits();
		GameManager.addGame(this);
		endTime = TIME_TO_END;
		winCondition = createWinCondition();
		spectatorTeam = createSpectatorTeam();
	}

	public final Game init(Game game) {
		return init(game.getHost(), game.getId(), game, game.getRegion(), game.getMinPlayer(), game.getMaxPlayer(),
				game.password);
	}

	public final Game init(ServerPlayer host, int id, IGameData data, IRegion region, int minPlayer, int maxPlayer,
			@Nullable String password) {
		gameState = GameState.PRE_GAME;
		this.host = host;
		this.id = id;
		this.uuid = data.getUniqueId();
		this.name = data.getName();
		this.authorName = data.getAuthorName();
		this.region = region;
		this.minPlayer = minPlayer;
		this.maxPlayer = maxPlayer;
		this.password = password;
		init();
		return this;
	}

	protected void init() {
		startLives = startLives();
		endAction = EndAction.DELETE;
		createTeams(teams);
		for (int i = 0; i < teams.size(); i++)
			teams.get(i).setId(i);
		for (int i = teams.size() - 1; i >= 0; i--)
			unusedTeams.add(teams.get(i));
	}

	@Override
	public void close() {
		GameManager.removeGame(this);
		stopScheduler();
		if (gameState == GameState.END)
			sendToDatabase();
		// playerData and team lists do leak memory on reload!
		playerData = null;
		teams = null;
		usedTeams = null;
		unusedTeams = null;
		endAction.accept(this);
	}

	private final void sendToDatabase() {
		try {
			DataAccess.getAccess().gameFinished(this);
		} catch (SQLException e) {
			Logger.err("Game: Error! Could not send data for game '" + uuid + "'", e);
		}
	}

	public abstract GameType<?> getType();

	protected abstract GameScoreboard createScoreboard();

	protected abstract BossBar createBossbar();

	protected abstract void createTeams(List<GameTeam> list);

	public final GameTeam createTeam(String name, ChatColor color, Material material) {
		return createTeam().init(name, color, material);
	}

	public final GameTeam createTeam(String name, ChatColor color, ItemStack is) {
		return createTeam().init(name, color, is);
	}

	protected GameTeam createTeam() {
		return new GameTeam(this);
	}

	public void onTeamCreation(GameTeam team) {
	}

	private final GameTeam registerTeam(GameTeam team) {
		unusedTeams.remove(team);
		usedTeams.add(team);
		team.register();
		return team;
	}

	private final GameTeam unregisterTeam(GameTeam team) {
		usedTeams.remove(team);
		unusedTeams.add(team);
		team.unregister();
		return team;
	}

	public final List<GameTeam> getTeams() {
		return teams;
	}

	public final List<GameTeam> getUsedTeams() {
		return usedTeams;
	}

	public final List<GameTeam> getUnusedTeams() {
		return unusedTeams;
	}

	public final GameTeam getTeam(int index) {
		return teams.get(index);
	}

	private final void addPlayerToTeam(GamePlayer gamePlayer) {
		GameTeam smallestTeam;
		if (usedTeams.size() < teams.size())
			smallestTeam = registerTeam(unusedTeams.get(unusedTeams.size() - 1));
		else
			smallestTeam = getSmallestTeam();
		gamePlayer.setTeam(smallestTeam);
	}

	private final void removePlayerFromTeam(GamePlayer gamePlayer) {
		GameTeam gameTeam = gamePlayer.getTeam();
		gamePlayer.setTeam(null);
		if (gameTeam.getTeam().getSize() < 1)
			unregisterTeam(gameTeam);
	}

	public final void changeTeam(ServerPlayer serverPlayer, int id) {
		changeTeam(serverPlayer, teams.get(id));
	}

	public final void changeTeam(ServerPlayer serverPlayer, GameTeam team) {
		GamePlayer gamePlayer = serverPlayer.getGameData();
		GameTeam prevTeam = gamePlayer.getTeam();
		if (prevTeam == team) {
			serverPlayer.sendActionBar(YELLOW + team.getName() + " team was already selected");
			return;
		}

		if (team.isAlive()) {
			if (team.getTeam().getSize() >= prevTeam.getTeam().getSize()) {
				serverPlayer.sendActionBar(YELLOW + team.getName() + " team to big to join");
				return;
			}
		} else
			registerTeam(team);

		removePlayerFromTeam(gamePlayer);
		gamePlayer.setTeam(team);
		serverPlayer.sendActionBar(team.getColor() + BOLD + team.getName() + " team selected");
	}

	private final GameTeam getSmallestTeam() { // can be replaced by ordered team list
		int smallestSize = Integer.MAX_VALUE;
		GameTeam smallestTeam = null;
		for (GameTeam team : usedTeams) {
			int size = team.getTeam().getSize();
			if (size < smallestSize) {
				smallestSize = size;
				smallestTeam = team;
			}
		}
		return smallestTeam;
	}

	protected Team createSpectatorTeam() {
		Team team = scoreboard.addTeam("Spectator");
		team.setPrefix(ChatColor.DARK_GRAY + "[Spectator] ");
		team.setOption(Option.COLLISION_RULE, OptionStatus.NEVER);
		return team;
	}

	public final Team getSpectatorTeam() {
		return spectatorTeam;
	}

	private final void setSpectator(ServerPlayer serverPlayer) {
		spectatorTeam.addEntry(serverPlayer.name);
		serverPlayer.setEventHandler(SpectatorEventHandler.INSTANCE);
	}

	private final void removeSpectator(ServerPlayer serverPlayer) {
		spectatorTeam.removeEntry(serverPlayer.name);
		serverPlayer.removeEventHandler(SpectatorEventHandler.INSTANCE, QuitReason.CHANGE_HANDLER);
	}

	protected abstract void tick();

	protected abstract void loadData(ConfigurationSection section);

	protected abstract void saveData(ConfigurationSection section);

	protected abstract WinCondition createWinCondition();

	public final WinCondition getWinCondition() {
		return winCondition;
	}

	public DeathOption getDeathOption() {
		return DeathOption.INSTANT;
	}

	public void setEndAction(EndAction endAction) {
		this.endAction = endAction;
	}

	public void start() {
		gameState = GameState.IN_GAME;
		scoreboard.init(this);
		scoreboard.create(this);
		scheduler = startScheduler();
	}

	protected void restart() {
		scoreboard.create(this);
		scheduler = startScheduler();
	}

	private final BukkitRunnable startScheduler() {
		BukkitRunnable scheduler = new BukkitRunnable() {
			@Override
			public void run() {
				if (gameState == GameState.END) {
					if (endTime == 0)
						close();
					endTime--;
				} else {
					tick();
					time++;
				}
			}
		};
		scheduler.runTaskTimer(Aiost.getPlugin(), 0, 20);
		return scheduler;
	}

	private final void stopScheduler() {
		if (scheduler != null && !scheduler.isCancelled())
			scheduler.cancel();
	}

	protected GamePlayer createGamePlayer() {
		return new GamePlayer(this);
	}

	private final GamePlayer registerPlayer(ServerPlayer serverPlayer) {
		GamePlayer gamePlayer = createGamePlayer();
		playerData.add(gamePlayer);
		linkPlayer(serverPlayer, gamePlayer);
		return gamePlayer;
	}

	private final GamePlayer registerPlayer() {
		GamePlayer gamePlayer = createGamePlayer();
		playerData.add(gamePlayer);
		return gamePlayer;
	}

	private final void linkPlayer(ServerPlayer serverPlayer, GamePlayer gamePlayer) {
		gamePlayer.setServerPlayer(serverPlayer);
		serverPlayer.setGameData(gamePlayer);
	}

	private final GamePlayer unregisterPlayer(ServerPlayer serverPlayer) {
		GamePlayer gamePlayer = serverPlayer.getGameData();
		gamePlayer.unequipKit();
		serverPlayer.setGameData(GamePlayer.NULL_GAME_PLAYER);
		player.remove(serverPlayer);
		if (isHost(serverPlayer))
			initNewHost();
		return gamePlayer;
	}

	protected final GamePlayer addPlayer(ServerPlayer serverPlayer) {
		GamePlayer gamePlayer = registerPlayer(serverPlayer);
		gamePlayer.equipDefaultKit();
		player.add(serverPlayer);
		addPlayerToTeam(gamePlayer);
		return gamePlayer;
	}

	protected final void removePlayer(ServerPlayer serverPlayer) {
		GamePlayer gamePlayer = serverPlayer.getGameData();
		unregisterPlayer(serverPlayer);
		playerData.remove(gamePlayer);
		removePlayerFromTeam(gamePlayer);
	}

	protected void defeat(ServerPlayer serverPlayer) {
		if (serverPlayer.getGameData().removeLive() < 1) {
			GameRateMenu.getMenu().open(serverPlayer);
			quit(serverPlayer);
			setSpectator(serverPlayer);
		}
	}

	private final void quit(ServerPlayer serverPlayer) {
		GamePlayer gamePlayer = unregisterPlayer(serverPlayer);
		GameTeam gameTeam = gamePlayer.getTeam();
		Team team = gameTeam.getTeam();
		team.removeEntry(serverPlayer.name);
		if (team.getPlayers().size() == 0) {
			gameTeam.unregister();
			if (getWinCondition().onTeamDefeat(this, gameTeam))
				end();
		}
		if (getWinCondition().onPlayerDefeat(this, serverPlayer, gameTeam))
			end();
	}

	protected void defeat(GameTeam team) {
		for (OfflinePlayer offlinePlayer : team.getTeam().getPlayers()) {
			ServerPlayer serverPlayer = ServerPlayer.getByPlayer(offlinePlayer.getPlayer());
			unregisterPlayer(serverPlayer);
			setSpectator(serverPlayer);
		}
		team.unregister();
		if (getWinCondition().onTeamDefeat(this, team))
			end();
	}

	public void end() {
		gameState = GameState.END;
		broadcastTitle(getWinCondition().getBroadcastMessage(this));
	}

	@Override
	public void onPlayerJoin(ServerPlayer serverPlayer) {
		Player player = serverPlayer.player;
		scoreboard.addPlayer(player);
		bossBar.addPlayer(player);
		setPlayerList(serverPlayer);

		GamePlayer gamePlayer = serverPlayer.getGameData();
		if (gamePlayer.game != this || gamePlayer.isDeath())
			setSpectator(serverPlayer);
	}

	@Override
	public void onPlayerQuit(ServerPlayer serverPlayer, QuitReason reason) {
		bossBar.removePlayer(serverPlayer.player);
		removePlayerList(serverPlayer);
		if (reason == QuitReason.DISABLE)
			serverPlayer.getGameData().deapplyKit();
		else {
			GamePlayer gamePlayer = serverPlayer.getGameData();
			if (gamePlayer.game == this && gamePlayer.isAlive())
				quit(serverPlayer);
			else
				removeSpectator(serverPlayer);
			scoreboard.removePlayer(serverPlayer);
		}
	}

	@Override
	public void onPlayerDamage(ServerPlayer serverPlayer, EntityDamageEvent event) {
		getDeathOption().onPlayerDamage(serverPlayer, event);
	}

	@Override
	public void onPlayerDeath(ServerPlayer serverPlayer, PlayerDeathEvent event) {
		getDeathOption().onPlayerDeath(serverPlayer, event);
	}

	@Override
	public void onPacketThingAttack(PacketThingAttackEvent event) {
		event.getPacketThing().defaultPlayerAttack(event);
	}

	@Override
	public final void load(ConfigurationSection section) {
		loadBase(section);
		restart();
	}

	final void loadBase(ConfigurationSection section) {
		loadBaseData(section);
		loadTeams(section.getConfigurationSection("teams"));
		loadActiveTeams(section);
		loadPlayers(section.getConfigurationSection("player"));
		loadHost(section);
		loadData(section);
	}

	@Override
	public final void save(ConfigurationSection section) {
		if (gameState == GameState.END)
			return;
		saveBaseData(section);
		saveTeams(section.createSection("teams"));
		saveActiveTeams(section);
		savePlayers(section.createSection("player"));
		saveHost(section);
		saveData(section);
	}

	private final void loadBaseData(ConfigurationSection section) {
		gameState = GameState.values()[section.getInt("gameState")];
		id = section.getInt("id");
		uuid = UUID.fromString(section.getString("uuid"));
		name = section.getString("name");
		authorName = section.getString("authorName");
		region = IRegion.load(section);
		minPlayer = section.getInt("minPlayer");
		maxPlayer = section.getInt("maxPlayer");
		password = section.getString("password");
		time = section.getInt("time");
		scoreboard.load(section.getConfigurationSection("scoreboard"));
		startLives = section.getInt("startLives");
		winCondition.load(section.getConfigurationSection("winCondition"));
		endAction = EndAction.get(section.getInt("endAction"));
	}

	private final void saveBaseData(ConfigurationSection section) {
		section.set("gameState", gameState.ordinal());
		section.set("id", id);
		section.set("uuid", uuid.toString());
		section.set("name", name);
		section.set("authorName", authorName);
		IRegion.save(section, region);
		section.set("minPlayer", minPlayer);
		section.set("maxPlayer", maxPlayer);
		section.set("password", password);
		section.set("time", time);
		scoreboard.save(section.createSection("scoreboard"));
		section.set("startLives", startLives);
		winCondition.save(section.createSection("winCondition"));
		section.set("endAction", endAction.id);
	}

	private final void loadTeams(ConfigurationSection section) {
		for (String teamName : section.getKeys(false)) {
			GameTeam team = createTeam();
			team.load(section.getConfigurationSection(teamName));
			teams.add(team);
		}
	}

	private final void saveTeams(ConfigurationSection section) {
		for (GameTeam team : teams)
			team.save(section.createSection(team.getName()));
	}

	private final void loadActiveTeams(ConfigurationSection section) {
		for (int usedTeam : section.getIntegerList("usedTeams"))
			usedTeams.add(teams.get(usedTeam));

		for (int unusedTeam : section.getIntegerList("unusedTeams"))
			unusedTeams.add(teams.get(unusedTeam));
	}

	private final void saveActiveTeams(ConfigurationSection section) {
		List<Integer> usedTeamsList = new IntArrayList();
		for (GameTeam team : usedTeams)
			usedTeamsList.add(team.getId());
		section.set("usedTeams", usedTeamsList);

		List<Integer> unusedTeamsList = new IntArrayList();
		for (GameTeam team : unusedTeams)
			unusedTeamsList.add(team.getId());
		section.set("unusedTeams", unusedTeamsList);
	}

	private final void loadPlayers(ConfigurationSection section) {
		for (String uuid : section.getKeys(false))
			loadPlayer(section, uuid);
	}

	private final void loadPlayer(ConfigurationSection section, String uuidString) {
		GamePlayer gamePlayer = registerPlayer();
		gamePlayer.load(section.getConfigurationSection(uuidString));
		UUID uuid = UUID.fromString(uuidString);
		ServerPlayer serverPlayer = ServerPlayer.getByUUID(uuid);
		if (serverPlayer != null && gamePlayer.isAlive()) {
			linkPlayer(serverPlayer, gamePlayer);
			this.player.add(serverPlayer);
			gamePlayer.applyKit();
		} else
			gamePlayer.setUniqueId(uuid);
	}

	private final void savePlayers(ConfigurationSection section) {
		for (GamePlayer gamePlayer : playerData)
			gamePlayer.save(section.createSection(gamePlayer.getUniqueId().toString()));
	}

	private final void loadHost(ConfigurationSection section) {
		String hostUUID = section.getString("host");
		if (hostUUID == null || hostUUID.isEmpty())
			return;
		host = ServerPlayer.getByUUID(UUID.fromString(hostUUID));
		if (host == null)
			initNewHost();
	}

	private final void saveHost(ConfigurationSection section) {
		section.set("host", host != null ? host.getUniqueId().toString() : "");
	}

	protected void setPlayerList(ServerPlayer serverPlayer) {
		serverPlayer.player.setPlayerListHeaderFooter(getPlayerListHeader(), getPlayerListFooter());
	}

	protected void removePlayerList(ServerPlayer serverPlayer) {
	}

	protected String getPlayerListHeader() {
		return DARK_RED + BOLD + getType().name;
	}

	protected String getPlayerListFooter() {
		return YELLOW + "www.aiost.com";
	}

	public final void broadcastMessage(String msg) {
		List<ServerPlayer> playerList = region.getServerPlayer();
		int length = playerList.size();
		for (int i = 0; i < length; i++)
			playerList.get(i).player.sendMessage(msg);
	}

	public final void broadcastMessage(String[] msg) {
		List<ServerPlayer> playerList = region.getServerPlayer();
		int length = playerList.size();
		for (int i = 0; i < length; i++)
			playerList.get(i).player.sendMessage(msg);
	}

	public final void broadcastTitle(String title) {
		List<ServerPlayer> playerList = region.getServerPlayer();
		int length = playerList.size();
		for (int i = 0; i < length; i++)
			playerList.get(i).sendTitle(title);
	}

	public final void broadcastTitle(String title, int fadeIn, int stay, int fadeOut) {
		List<ServerPlayer> playerList = region.getServerPlayer();
		int length = playerList.size();
		for (int i = 0; i < length; i++)
			playerList.get(i).sendTitle(title, fadeIn, stay, fadeOut);
	}

	public final void broadcastTitle(String title, String subtitle) {
		List<ServerPlayer> playerList = region.getServerPlayer();
		int length = playerList.size();
		for (int i = 0; i < length; i++)
			playerList.get(i).sendTitle(title, subtitle);
	}

	public final void broadcastTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
		List<ServerPlayer> playerList = region.getServerPlayer();
		int length = playerList.size();
		for (int i = 0; i < length; i++)
			playerList.get(i).sendTitle(title, subtitle, fadeIn, stay, fadeOut);
	}

	public final void broadcastSubtitle(String subtitle) {
		List<ServerPlayer> playerList = region.getServerPlayer();
		int length = playerList.size();
		for (int i = 0; i < length; i++)
			playerList.get(i).sendSubTitle(subtitle);
	}

	public final void broadcastSubtitle(String subtitle, int fadeIn, int stay, int fadeOut) {
		List<ServerPlayer> playerList = region.getServerPlayer();
		int length = playerList.size();
		for (int i = 0; i < length; i++)
			playerList.get(i).sendSubTitle(subtitle, fadeIn, stay, fadeOut);
	}

	public final void broadcastActionBar(String msg) {
		List<ServerPlayer> playerList = region.getServerPlayer();
		int length = playerList.size();
		for (int i = 0; i < length; i++)
			playerList.get(i).sendActionBar(msg);
	}

	public boolean isHost(ServerPlayer serverPlayer) {
		return serverPlayer == host;
	}

	public boolean isHost(Player player) {
		return player == host.player;
	}

	public ServerPlayer getHost() {
		return host;
	}

	private final void initNewHost() {
		host = player.get(0);
	}

	void setId(int id) {
		this.id = id;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public UUID getUniqueId() {
		return uuid;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getAuthorName() {
		return authorName;
	}

	@Override
	public IRegion getRegion() {
		return region;
	}

	@Override
	public int getMinPlayer() {
		return minPlayer;
	}

	@Override
	public int getMaxPlayer() {
		return maxPlayer;
	}

	@Override
	public boolean hasPassword() {
		return password != null;
	}

	@Override
	public boolean checkPassword(String password) {
		if (password == null)
			return true;
		return this.password.equals(password);
	}

	public GameScoreboard getScoreboard() {
		return scoreboard;
	}

	public BossBar getBossBar() {
		return bossBar;
	}

	public List<ServerPlayer> getPlayer() {
		return player;
	}

	@Override
	public int getPlayerSize() {
		return player.size();
	}

	public List<GamePlayer> getPlayerData() {
		return playerData;
	}

	public final int getTime() {
		return time;
	}

	protected GameKit[] createKits() {
		return GameKit.EMPTY_GROUP;
	}

	public final GameKit[] getKits() {
		return kits;
	}

	public final GameKit getDefaultKit() {
		return kits[0];
	}

	protected int startLives() {
		return 1;
	}

	public final void setStartLives(int lives) {
		this.startLives = lives;
	}

	public final int getStartLives() {
		return startLives;
	}

	public final GameState getGameState() {
		return gameState;
	}

	protected Menu createSettingMenu() {
		return new GameSettingMenu<Game>(this);
	}

	public final Menu getSettingMenu() {
		if (settingMenu == null)
			return settingMenu = createSettingMenu();
		return settingMenu;
	}

	@Override
	public final String getEventHandlerName() {
		return getType().name;
	}

	@Override
	public final boolean hasChanged() {
		return true;
	}

	protected final static String timeToString(int time) {
		int minutes = time / 60;
		if (minutes > 0) {
			if (minutes > 60) {
				int hours = minutes / 60;
				return hours + ":" + minutes + ":" + parseSeconds(time);
			}
			return minutes + ":" + parseSeconds(time);
		}
		return "" + time;
	}

	private final static String parseSeconds(int time) {
		int seconds = time % 60;
		return seconds < 10 ? "0" + seconds : Integer.toString(seconds);
	}

	public static void host(ServerPlayer serverPlayer, GameData game, int minPlayer, int maxPlayer,
			@Nullable String password) {
		ServerRequest.getHandler().hostGame(serverPlayer, game, minPlayer, maxPlayer, password);
	}

	public static void join(ServerPlayer serverPlayer, IGameData game) {
		join(serverPlayer, game, null);
	}

	public static void join(ServerPlayer serverPlayer, IGameData game, @Nullable Inventory backLink) {
		if (!game.hasPassword()) {
			ServerRequest.getHandler().joinGame(serverPlayer, game);
			return;
		}
		serverPlayer.doMenuRequest(new CallbackMenuRequest(TextMenu.create(BOLD + "Password", "password"), true) {
			@Override
			protected void openRequest(ServerPlayer serverPlayer) {
				if (backLink == null)
					serverPlayer.closeInventory();
				else
					serverPlayer.openInventory(backLink);
			}

			@Override
			protected void onResult(ServerPlayer serverPlayer, Object obj) {
				if (game.checkPassword((String) obj))
					ServerRequest.getHandler().joinGame(serverPlayer, game);
				else
					serverPlayer.sendMessage(ChatColor.RED + "Wrong password!");
			}
		});
	}
}