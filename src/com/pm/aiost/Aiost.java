package com.pm.aiost;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.pm.aiost.effect.EffectType;
import com.pm.aiost.effect.EffectTypes;
import com.pm.aiost.effect.group.EffectGroupBuilder;
import com.pm.aiost.entity.AiostEntityTypes;
import com.pm.aiost.entity.EntityConfig;
import com.pm.aiost.entity.entities.projectile.EntityProjectile;
import com.pm.aiost.game.GameType;
import com.pm.aiost.game.GameTypes;
import com.pm.aiost.item.Items;
import com.pm.aiost.item.spell.Spell;
import com.pm.aiost.item.spell.Spells;
import com.pm.aiost.misc.SpigotConfig;
import com.pm.aiost.misc.SpigotConfigManager;
import com.pm.aiost.misc.SpigotLogger;
import com.pm.aiost.misc.command.Commands;
import com.pm.aiost.misc.dataAccess.DataAccess;
import com.pm.aiost.misc.dataAccess.SpigotDatabaseAccess;
import com.pm.aiost.misc.dataAccess.SpigotFileAccess;
import com.pm.aiost.misc.database.DatabaseManager;
import com.pm.aiost.misc.event.AiostListener;
import com.pm.aiost.misc.event.eventHandler.EventHandlerManager;
import com.pm.aiost.misc.log.Logger;
import com.pm.aiost.misc.menu.inventoryMenu.InventoryMenuCustomAnimationHandler;
import com.pm.aiost.misc.menu.inventoryMenu.InventoryMenuHandler;
import com.pm.aiost.misc.menu.menus.GameJoinMenu;
import com.pm.aiost.misc.packet.entity.PacketEntityType;
import com.pm.aiost.misc.packet.entity.PacketEntityTypes;
import com.pm.aiost.misc.packet.object.PacketObjectType;
import com.pm.aiost.misc.packet.object.PacketObjectTypes;
import com.pm.aiost.misc.particle.EntityParticleManager;
import com.pm.aiost.misc.particle.ParticleBuilder;
import com.pm.aiost.misc.particle.ParticleType;
import com.pm.aiost.misc.particle.ParticleTypes;
import com.pm.aiost.misc.scoreboard.scoreboards.LobbyScoreboard;
import com.pm.aiost.misc.server.ServerManager;
import com.pm.aiost.misc.server.messaging.AiostPluginMessageListener;
import com.pm.aiost.misc.server.messaging.ServerDataRequester;
import com.pm.aiost.misc.utils.scheduler.AiostScheduler;
import com.pm.aiost.player.PlayerManager;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.player.unlockable.UnlockableManager;
import com.pm.aiost.player.unlockable.UnlockableType;
import com.pm.aiost.player.unlockable.UnlockableTypes;
import com.pm.aiost.server.http.HttpServer;
import com.pm.aiost.world.WorldManager;
import com.pm.aiost.world.tileObject.TileObjectType;
import com.pm.aiost.world.tileObject.TileObjectTypes;
import com.pm.aiost.world.type.AiostWorldType;
import com.pm.aiost.world.type.AiostWorldTypes;

import net.minecraft.world.entity.EntityType;

// All in one server technology
public class Aiost extends JavaPlugin {

	private static Aiost plugin;

	@Override
	public void onLoad() {
		plugin = this;
		Logger.setLogger(new SpigotLogger());

		Logger.log("Start loading aisot...");

		EventHandlerManager.init();
		EntityType<EntityProjectile> entityType = AiostEntityTypes.PROJECTILE;
		GameType<?> gameType = GameTypes.SPLEEF;
		UnlockableType<?> unlockableType = UnlockableTypes.HATS;
		Spell spell = Spells.FLAME;
		ParticleType<?> particleType = ParticleTypes.BEAM;
		EffectType<?> effectType = EffectTypes.POTION_EFFECT;
		PacketObjectType<?> packetObjectType = PacketObjectTypes.FURNITURE;
		PacketEntityType<?> packetEntityType = PacketEntityTypes.ENTITY_FURNITURE;
		TileObjectType<?> tileObjectType = TileObjectTypes.BLINKING_BLOCK;
		AiostWorldType<?> worldType = AiostWorldTypes.DEFAULT;
		EntityConfig entityConfig = EntityConfig.CHICKEN_HOSTILE;

		Logger.log("Loading aisot complete!");
	}

	@Override
	public void onEnable() {
		Logger.log("Initialize Aiost...");

		getServer().getPluginManager().registerEvents(new AiostListener(), this);
		Commands.init();
		SpigotConfigManager.init();
		intDatabase();
		if (!SpigotConfig.HAS_BUNGEE)
			HttpServer.start();
		ServerManager.init();
		AiostPluginMessageListener.init(this);
		new EffectGroupBuilder().loadEffectGroups(SpigotConfigManager.getEffectConfig());
		Items.init(); // TODO test if this in onLoad makes custom Items work better!
		EffectTypes.init();
		ParticleBuilder.registerConfig();
		UnlockableManager.init();
		PlayerManager.registerOnlinePlayer();
		WorldManager.init();
		PlayerManager.enableOnlinePlayer();
		startScheduler();

		Logger.log("Aiost initialized!");
	}

	@Override
	public void onDisable() {
		Logger.log("Disable Aiost...");

		if (!SpigotConfig.HAS_BUNGEE)
			HttpServer.stop();
		PlayerManager.disableOnlinePlayer();
		WorldManager.terminate();
		SpigotConfigManager.terminate();
		AiostEntityTypes.terminate();

		Logger.log("Aiost disabled!");
	}

	private static void intDatabase() {
		Logger.log("Initialize Database...");

		if (DatabaseManager.initConnection(SpigotConfigManager.getAiostConfig()))
			DataAccess.init(new SpigotDatabaseAccess());
		else
			DataAccess.init(new SpigotFileAccess());

		Logger.log("Database initialized!");
	}

	private static void startScheduler() {
		// 0.2 sec
		new BukkitRunnable() {
			@Override
			public void run() {
				for (ServerPlayer serverPlayer : ServerPlayer.getOnlinePlayer()) {
					serverPlayer.spawnParticles(); // TODO: Check visibility and render only to self!
					serverPlayer.update();
				}
				EntityParticleManager.render();
				EventHandlerManager.update();
				WorldManager.updateWorlds();
				AiostScheduler.update();
			}
		}.runTaskTimer(Aiost.getPlugin(), 0, 4);

		// 0.5 sec
		new BukkitRunnable() {
			@Override
			public void run() {
				InventoryMenuHandler.animateMenusSchedulerTick();
				InventoryMenuCustomAnimationHandler.animateMenusSchedulerTick();
			}
		}.runTaskTimer(Aiost.getPlugin(), 0, 10);

		// 5 sec
		new BukkitRunnable() {
			@Override
			public void run() {
				if (SpigotConfig.HAS_BUNGEE)
					ServerDataRequester.requestData();
				else
					GameJoinMenu.updateMenus();
				LobbyScoreboard.updatePlayerSize();
			}
		}.runTaskTimer(Aiost.getPlugin(), 0, 100);
	}

	public static Aiost getPlugin() {
		return plugin;
	}

	public static String getUrl() {
		return SpigotConfigManager.getWebUrl();
	}
}
