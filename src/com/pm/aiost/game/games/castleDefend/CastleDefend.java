package com.pm.aiost.game.games.castleDefend;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.DARK_RED;
import static com.pm.aiost.misc.utils.ChatColor.GREEN;
import static org.bukkit.ChatColor.RED;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.Aiost;
import com.pm.aiost.collection.list.UnorderedIdentityArrayList;
import com.pm.aiost.entity.AiostEntityTypes;
import com.pm.aiost.entity.CustomInsentient;
import com.pm.aiost.entity.ai.pathfinderGoal.PathfinderGoalWalkToLocation;
import com.pm.aiost.entity.ai.pathfinderGoal.custom.CustomPathfinderGoalWalkToLocation;
import com.pm.aiost.entity.spawner.StageEntitySpawner;
import com.pm.aiost.entity.vanilla.EntityTrader;
import com.pm.aiost.event.eventHandler.EventHandler;
import com.pm.aiost.event.eventHandler.EventHandlerManager;
import com.pm.aiost.event.events.PacketThingAttackEvent;
import com.pm.aiost.game.Game;
import com.pm.aiost.game.GameKit;
import com.pm.aiost.game.GameTeam;
import com.pm.aiost.game.GameType;
import com.pm.aiost.game.GameTypes;
import com.pm.aiost.game.WinCondition;
import com.pm.aiost.item.Items;
import com.pm.aiost.misc.menu.Menu;
import com.pm.aiost.misc.menu.menus.ShopMenu;
import com.pm.aiost.misc.scoreboard.scoreboards.GameScoreboard;
import com.pm.aiost.player.ServerPlayer;

import net.minecraft.server.v1_15_R1.BlockPosition;
import net.minecraft.server.v1_15_R1.Entity;
import net.minecraft.server.v1_15_R1.EntityCreature;
import net.minecraft.server.v1_15_R1.EntityTypes;
import net.minecraft.server.v1_15_R1.EnumMobSpawn;

public class CastleDefend extends Game {

	protected static final int START_MOB_SIZE = 8;
	protected static final int ENTITY_TYPE_PROBABILITY = 4;
	protected static final int SPECIAL_WAVE_PROBABILITY = 10;
	protected static final int WAVE_PAUSE_TIME = 20;
	protected static final int SPAWNER_INTERVALL_TIME = 20;

	private int wave;
	private StageEntitySpawner spawner;
	private CastleDefenseMobEventHandler castleDefenseMobEventHandler;
	private Random random;
	private List<EntityTypes<?>> entityTypes;
	protected List<EntityTypes<?>> lockedTypes;
	private boolean isSpecialWave;
	private Location targetLocation;
	private int kills; // TODO should be in teams and saved to database!
	private Menu shopMenu;

	public CastleDefend() {
		random = new Random();
		spawner = new StageEntitySpawner(random);
		shopMenu = new ShopMenu(true);
	}

	@Override
	public GameType<? extends CastleDefend> getType() {
		return GameTypes.CASTLE_DEFEND;
	}

	@Override
	protected CastleDefendPlayer createGamePlayer() {
		return new CastleDefendPlayer(this);
	}

	@Override
	protected GameScoreboard createScoreboard() {
		return new CastleDefendScoreboard();
	}

	@Override
	public CastleDefendScoreboard getScoreboard() {
		return (CastleDefendScoreboard) super.getScoreboard();
	}

	@Override
	protected BossBar createBossbar() {
		return Bukkit.createBossBar(DARK_RED + BOLD + "Wave 1", BarColor.RED, BarStyle.SOLID);
	}

	@Override
	protected void createTeams(List<GameTeam> list) {
		list.add(createTeam("Defender", ChatColor.DARK_RED, Material.RED_BANNER));
	}

	@Override
	protected Menu createSettingMenu() {
		return new CastleDefendMenu(this);
	}

	@Override
	protected void init() {
		super.init();
		wave = 1;
		spawner.setIntervallTime(SPAWNER_INTERVALL_TIME);
		initEntityTypes();
		spawner.setEntityTypes(entityTypes);
		spawner.setLocations(getRegion().getMarkerLocations("Spawn"));
	}

	@Override
	public void start() {
		super.start();

		targetLocation = getRegion().getSpawnLocation();
		int enemySize = getWaveMobAmount();
		CastleDefendScoreboard cdScoreboard = getScoreboard();
		cdScoreboard.setPlayerSize(getPlayer().size());
		cdScoreboard.setEnemySize(enemySize);
		castleDefenseMobEventHandler = new CastleDefenseMobEventHandler();
		spawner.setSpawnSize(enemySize);
		spawner.setSpawnCallback(this::spawnCallback);
		spawner.setTime(WAVE_PAUSE_TIME);
		EntityTrader trader = (EntityTrader) AiostEntityTypes.spawnEntity(AiostEntityTypes.TRADER, targetLocation);
		trader.setPersistent();
	}

	@Override
	protected void restart() {
		super.restart();

		targetLocation = getRegion().getSpawnLocation();
		int enemySize = spawner.getCurrentEntitySize();
		getBossBar().setProgress((double) enemySize / spawner.getSpawnSize());
		CastleDefendScoreboard cdScoreboard = getScoreboard();
		cdScoreboard.setPlayerSize(getPlayer().size());
		cdScoreboard.setEnemySize(enemySize);
		castleDefenseMobEventHandler = new CastleDefenseMobEventHandler();
		spawner.setSpawnCallback(this::spawnCallback);
		spawner.respawn();
	}

	@Override
	protected void tick() {
		getScoreboard().setTime(GREEN + timeToString(getTime()));
		spawner.tick();
	}

	@Override
	protected WinCondition createWinCondition() {
		return WinCondition.ALL_DEATH;
	}

	@Override
	protected void setPlayerList(ServerPlayer serverPlayer) {
		super.setPlayerList(serverPlayer);
		serverPlayer.player.setPlayerListName(ChatColor.DARK_RED + serverPlayer.player.getDisplayName() + ' '
				+ ChatColor.GOLD + ChatColor.BOLD + serverPlayer.getGameData().getLives());
	}

	@Override
	protected void removePlayerList(ServerPlayer serverPlayer) {
		serverPlayer.player.setPlayerListName(null);
	}

	@Override
	protected void defeat(ServerPlayer serverPlayer) {
		super.defeat(serverPlayer);
		getScoreboard().setPlayerSize(getPlayer().size());
	}

	public final void endWave() {
		wave++;
		addEntityType();
		progressWave();
	}

	public final void setWave(int wave) {
		this.wave = wave;
	}

	public final int getWave() {
		return wave;
	}

	protected void progressWave() {
		int enemySize = checkBossWave();
		if (enemySize == 0) {
			enemySize = getWaveMobAmount();
			if (rollSpecialWave())
				setRandomSpecialWave();
			else
				setNormalWave();
		}

		getBossBar().setProgress(1);
		CastleDefendScoreboard cdScoreboard = getScoreboard();
		cdScoreboard.setEnemySize(enemySize);
		cdScoreboard.setWave(wave);
		spawner.setSpawnSize(enemySize);
		spawner.setTime(WAVE_PAUSE_TIME);
	}

	@Override
	public void onPlayerDeath(ServerPlayer serverPlayer, PlayerDeathEvent event) {
		super.onPlayerDeath(serverPlayer, event);
		defeat(serverPlayer);
	}

	@Override
	public void onBlockBreak(ServerPlayer serverPlayer, BlockBreakEvent event) {
		event.setCancelled(true);
	}

	@Override
	public void onBlockPlace(ServerPlayer serverPlayer, BlockPlaceEvent event) {
		Material material = event.getBlock().getType();
		if (material != Material.TORCH && material != Material.TNT && material != Material.FIRE)
			event.setCancelled(true);
	}

	@Override
	public void onBlockDamage(ServerPlayer serverPlayer, BlockDamageEvent event) {
		event.setCancelled(true);
	}

	@Override
	public void onPacketThingAttack(PacketThingAttackEvent event) {
		event.setCancelled(true);
	}

	@Override
	public void onEntityDeath(EntityDeathEvent event) {
		if (!(event.getEntity() instanceof Player))
			event.getDrops().clear();
	}

	@Override
	public void onPlayerInteractAtEntity(ServerPlayer serverPlayer, PlayerInteractAtEntityEvent event) {
		if (event.getRightClicked().getType() == EntityType.VILLAGER)
			Bukkit.getScheduler().runTaskLater(Aiost.getPlugin(), () -> shopMenu.open(event.getPlayer()), 5);
	}

	@Override
	public void onEntityDeathByPlayer(ServerPlayer serverPlayer, EntityDeathEvent event) {
		int reward = getReward(event.getEntity());
		giveReward(serverPlayer.player, event.getEntity(), reward);
		getScoreboard().setKilled(++kills);
		CastleDefendPlayer gamePlayer = ((CastleDefendPlayer) serverPlayer.getGameData());
		gamePlayer.kills++;
		gamePlayer.addPoints(reward * 100);
	}

	protected final void giveReward(Player player, LivingEntity entity, int reward) {
		ItemStack is = new ItemStack(Material.GOLD_NUGGET, reward);
		HashMap<Integer, ItemStack> couldntStore = player.getInventory().addItem(is);
		if (couldntStore.size() > 0)
			player.getWorld().dropItem(entity.getLocation(), is);
	}

	protected int getReward(LivingEntity entity) {
		return (int) (entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() / 5);
	}

	protected void spawnCallback(Entity entity) {
		if (entity instanceof EntityCreature) {
			EntityCreature insentient = (EntityCreature) entity;
			EventHandlerManager.setEntityHandler(insentient.getBukkitEntity(), castleDefenseMobEventHandler);
			insentient.setPersistent();
			insentient.goalSelector.a(2, new PathfinderGoalWalkToLocation(insentient, targetLocation, 1.0F));
			insentient.prepare(entity.world, entity.world.getDamageScaler(new BlockPosition(entity)),
					EnumMobSpawn.SPAWNER, null, null);
		} else if (entity instanceof CustomInsentient) {
			CustomInsentient insentient = (CustomInsentient) entity;
			EventHandlerManager.setEntityHandler(insentient.getBukkitEntity(), castleDefenseMobEventHandler);
			insentient.setPersistent();
			insentient.getGoalSelector().a(2, new CustomPathfinderGoalWalkToLocation(insentient, targetLocation, 1.0F));
			insentient.prepare(entity.world, entity.world.getDamageScaler(new BlockPosition(entity)),
					EnumMobSpawn.SPAWNER, null, null);
		}
	}

	protected void initEntityTypes() {
		addStartTypes(entityTypes = new UnorderedIdentityArrayList<EntityTypes<?>>());
		addLockedTypes(lockedTypes = new UnorderedIdentityArrayList<EntityTypes<?>>());
		addEntityType();
	}

	protected void addStartTypes(List<EntityTypes<?>> list) {
//		list.add(AiostEntityTypes.TEST_ZOMBIE);
//		list.add(AiostEntityTypes.NO_COMBUST_ZOMBIE);
//		list.add(AiostEntityTypes.NO_COMBUST_MELEE_SKELETON);
		list.add(AiostEntityTypes.ENEMY_NPC);
	}

	protected void addLockedTypes(List<EntityTypes<?>> list) {
		list.add(AiostEntityTypes.NO_COMBUST_WITHER_SKELETON);
		list.add(AiostEntityTypes.NO_COMBUST_MULTISHOT_SKELETON);
		list.add(AiostEntityTypes.ALWAYS_ATTACK_SPIDER);
		list.add(AiostEntityTypes.NO_COMBUST_SKELETON);
		list.add(AiostEntityTypes.ALWAYS_ATTACK_CAVE_SPIDER);
//		list.add(EntityTypes.SLIME);
//		list.add(EntityTypes.MAGMA_CUBE);
		list.add(AiostEntityTypes.CREEPER);
		list.add(AiostEntityTypes.MELEE_CREEPER);
		list.add(AiostEntityTypes.FAKE_CREEPER);
//		list.add(EntityTypes.MELEE_WITCH);
		list.add(AiostEntityTypes.WITCH);
		list.add(AiostEntityTypes.MELEE_BLAZE);
		list.add(AiostEntityTypes.BLAZE);
		list.add(AiostEntityTypes.MULTISHOT_BLAZE);
		list.add(AiostEntityTypes.GHAST);
		list.add(AiostEntityTypes.ALWAYS_ATTACK_ENDERMAN);
		list.add(AiostEntityTypes.HOSTILE_IRON_GOLEM);
	}

	protected void addEntityType() {
		if (rollEntityType())
			entityTypes.add(lockedTypes.remove(random.nextInt(lockedTypes.size())));
	}

	public void addEntityType(EntityTypes<?> type) {
		if (lockedTypes.remove(type))
			entityTypes.add(type);
	}

	public void removeEntityType(EntityTypes<?> type) {
		if (entityTypes.remove(type))
			lockedTypes.add(type);
	}

	public List<EntityTypes<?>> getEntityTypes() {
		return entityTypes;
	}

	public List<EntityTypes<?>> getLockedTypes() {
		return lockedTypes;
	}

	protected boolean rollEntityType() {
		return random.nextInt(ENTITY_TYPE_PROBABILITY) == 0;
	}

	protected int getWaveMobAmount() {
		return (int) (START_MOB_SIZE + wave * 2) * getPlayer().size();
	}

	protected boolean rollSpecialWave() {
		return random.nextInt(SPECIAL_WAVE_PROBABILITY) == 0;
	}

	protected void setRandomSpecialWave() {
		switch (random.nextInt(8)) {
		case 7:
			setSpecialWave("Chicken Wave!", AiostEntityTypes.HOSTILE_CHICKEN);
			break;
		case 6:
			setSpecialWave("Skeleton Wave!", AiostEntityTypes.NO_COMBUST_SKELETON,
					AiostEntityTypes.NO_COMBUST_MELEE_SKELETON);
			break;
		case 5:
			setSpecialWave("Spider Wave!", AiostEntityTypes.SPIDER, AiostEntityTypes.CAVE_SPIDER);
			break;
		case 4:
			setSpecialWave("Creeper Wave!", AiostEntityTypes.CREEPER, AiostEntityTypes.MELEE_CREEPER,
					AiostEntityTypes.FAKE_CREEPER);
			break;
		case 3:
			setSpecialWave("Ghast Wave!", AiostEntityTypes.GHAST);
			break;
		case 2:
			setSpecialWave("Blaze Wave!", AiostEntityTypes.BLAZE, AiostEntityTypes.MELEE_BLAZE);
			break;
		case 1:
			setSpecialWave("Witch Wave!", AiostEntityTypes.WITCH, AiostEntityTypes.MELEE_WITCH);
			break;

		case 0:
		default:
			setSpecialWave("Zombie Wave!", AiostEntityTypes.NO_COMBUST_ZOMBIE);
			break;
		}
	}

	protected int checkBossWave() {
		switch (wave) {
		case 30:
			setSpecialWave(RED + "Wither Wave!", AiostEntityTypes.WITHER);
			return 1;
		case 50:
			setSpecialWave(RED + "Enderdragon Wave!", AiostEntityTypes.ENDER_DRAGON);
			return 1;
		case 80:
			setSpecialWave(RED + "Wither Wave!", AiostEntityTypes.WITHER);
			return 3;
		case 100:
			setSpecialWave(RED + "Enderdragon Wave!", AiostEntityTypes.ENDER_DRAGON);
			return 3;

		default:
			return 0;
		}
	}

	protected final void setSpecialWave(String msg, EntityTypes<?>... entityTypes) {
		isSpecialWave = true;
		spawner.setEntityTypes(Arrays.asList(entityTypes));
		getBossBar().setTitle(msg);
	}

	protected final void setNormalWave() {
		if (isSpecialWave) {
			isSpecialWave = false;
			spawner.setEntityTypes(entityTypes);
		}
		getBossBar().setTitle(DARK_RED + BOLD + "Wave " + wave);
	}

	@Override
	protected void loadData(ConfigurationSection section) {
		wave = section.getInt("wave");
		spawner.load(section.getConfigurationSection("spawner"));
		isSpecialWave = section.getBoolean("isSpecialWave");
		if (isSpecialWave) {
			getBossBar().setTitle(section.getString("specialWaveText"));
			entityTypes = AiostEntityTypes.load(section, "entityTypes");
		} else {
			getBossBar().setTitle(DARK_RED + BOLD + "Wave " + wave);
			entityTypes = spawner.getEntityTypes();
		}
		lockedTypes = AiostEntityTypes.load(section, "lockedTypes");
		kills = section.getInt("kills");
	}

	@Override
	protected void saveData(ConfigurationSection section) {
		section.set("wave", wave);
		spawner.save(section.createSection("spawner"));
		section.set("isSpecialWave", isSpecialWave);
		if (isSpecialWave) {
			section.set("specialWaveText", getBossBar().getTitle());
			AiostEntityTypes.save(section, "entityTypes", entityTypes);
		}
		AiostEntityTypes.save(section, "lockedTypes", lockedTypes);
		section.set("kills", kills);
	}

	@Override
	protected GameKit[] createKits() {
		return new GameKit[] {
				new GameKit("Peasant", Material.STONE_SWORD, 0, new String[] { "A simple peasant" },
						new ItemStack[] { new ItemStack(Material.STONE_SWORD) }),

				new GameKit("Fighter", Material.IRON_SWORD, 1000, new String[] { "Still had your old weapons" },
						new ItemStack[] { new ItemStack(Material.IRON_SWORD), new ItemStack(Material.SHIELD) }),

				new GameKit("Spellcaster", Items.get("flame"), 1000, new String[] { "A basic spellcaster" },
						new ItemStack[] { new ItemStack(Material.WOODEN_SWORD), Items.get("flame"),
								new ItemStack(Material.GOLD_NUGGET, 10) }) {
					@Override
					public void apply(ServerPlayer serverPlayer) {
						serverPlayer.addManaRegeneration(0.4F);
						serverPlayer.addMaxMana(20F);
					};

					public void deapply(ServerPlayer serverPlayer) {
						serverPlayer.removeManaRegeneration(0.4F);
						serverPlayer.removeMaxMana(20F);
					};
				},

				new GameKit("Knight", Material.IRON_SWORD, 3000, new String[] { "A strong knight" },
						new ItemStack[] { new ItemStack(Material.IRON_SWORD), new ItemStack(Material.SHIELD) }),

				new GameKit("Mage", Items.get("flame"), 3000, new String[] { "A powerful mage" },
						new ItemStack[] { new ItemStack(Material.WOODEN_SWORD), Items.get("flame"),
								new ItemStack(Material.GOLD_NUGGET, 10) }) {
					@Override
					public void apply(ServerPlayer serverPlayer) {
						serverPlayer.addManaRegeneration(0.8F);
						serverPlayer.addMaxMana(40F);
					};

					public void deapply(ServerPlayer serverPlayer) {
						serverPlayer.removeManaRegeneration(0.8F);
						serverPlayer.removeMaxMana(40F);
					};
				} };
	}

	protected class CastleDefenseMobEventHandler implements EventHandler {

		@Override
		public void onEntityDeath(EntityDeathEvent event) {
			CastleDefend.this.onEntityDeath(event);
			progressEntityDeath();
		}

		@Override
		public void onEntityExplode(EntityExplodeEvent event) {
			progressEntityDeath();
		}

		private void progressEntityDeath() {
			spawner.onEntityDeath();
			if (spawner.allSpawnedAndDeath())
				endWave();
			else {
				int currentEntitySize = spawner.getCurrentEntitySize();
				getScoreboard().setEnemySize(currentEntitySize);
				getBossBar().setProgress((double) currentEntitySize / spawner.getSpawnSize());
			}
		}
	}
}