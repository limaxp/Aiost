package com.pm.aiost.game;

import java.util.function.Function;
import java.util.function.Supplier;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.game.games.castleDefend.CastleDefend;
import com.pm.aiost.game.games.soccer.Soccer;
import com.pm.aiost.game.games.spleef.Spleef;
import com.pm.aiost.game.games.tntThrow.TntThrow;
import com.pm.aiost.misc.registry.AiostRegistry;
import com.pm.aiost.server.world.ServerWorld;
import com.pm.aiost.server.world.marker.Marker;

public class GameTypes {

	public static final GameType<Spleef> SPLEEF = a("Spleef",
			new String[] { "Hit blocks with snowballs and other", "gadgets to destroy them",
					"Make other players fall to their death", "Be the last man standing to win",
					"Break blocks to refill your ammo" },
			2, 16, new ItemStack(Material.SNOWBALL), Spleef::new,
			(serverWorld) -> hasMarker(serverWorld, Marker.SPAWN, 16));

	public static final GameType<CastleDefend> CASTLE_DEFEND = a("Castle Defend",
			new String[] { "Defend an position against waves of enemies", "Game ends if all players die",
					"or the position gets destroyed" },
			1, 16, new ItemStack(Material.SKELETON_SKULL), CastleDefend::new,
			(serverWorld) -> hasMinMarker(serverWorld, Marker.SPAWN, 1));

	public static final GameType<TntThrow> TNT_THROW = a("Tnt Thrower",
			new String[] { "Hit the enemy team with tnt based weapons", "or make them fall into fluids or void" }, 2,
			32, new ItemStack(Material.TNT), TntThrow::new, (serverWorld) -> hasMarker(serverWorld, Marker.SPAWN, 2));

	public static final GameType<Soccer> SOCCER = a("Soccer", new String[] { "A simple game of soccer" }, 2, 12,
			new ItemStack(Material.CLAY_BALL), Soccer::new, (serverWorld) -> null);

	public static <T extends Game> GameType<T> a(String name, String[] lore, int minPlayer, int maxPlayer, ItemStack is,
			Supplier<T> constructor, Function<ServerWorld, String> requireFunction) {
		GameType<T> type = new GameType<T>(name, lore, (byte) minPlayer, (byte) maxPlayer, is, constructor,
				requireFunction);
		type.setId(AiostRegistry.GAMES.register(name, type));
		AiostRegistry.EVENT_HANDLER.register(name, type);
		return type;
	}

	public static String hasMarker(ServerWorld serverWorld, String name) {
		return serverWorld.hasMarker(name) ? null : "No '" + name + "' marker found!";
	}

	public static String hasMarker(ServerWorld serverWorld, String name, int amount) {
		return serverWorld.hasMarker(name, amount) ? null
				: "World must have exactly " + amount + " '" + name + "' marker!";
	}

	public static String hasMinMarker(ServerWorld serverWorld, String name, int amount) {
		return serverWorld.hasMinMarker(name, amount) ? null
				: "World must have at least " + amount + " '" + name + "' marker!";
	}

	public static GameType<?> get(int id) {
		return AiostRegistry.GAMES.get(id);
	}
}
