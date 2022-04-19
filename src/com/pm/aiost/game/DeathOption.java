package com.pm.aiost.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import com.pm.aiost.event.AiostEventFactory;
import com.pm.aiost.event.effect.effects.PotionEffect;
import com.pm.aiost.misc.packet.entity.PacketEntityTypes;
import com.pm.aiost.misc.packet.entity.entities.PacketPlayer;
import com.pm.aiost.player.ServerPlayer;

public interface DeathOption {

	public void onPlayerDamage(ServerPlayer serverPlayer, EntityDamageEvent event);

	public void onPlayerDeath(ServerPlayer serverPlayer, PlayerDeathEvent event);

	public static final List<ItemStack> EMPTY_DROPS = Collections.unmodifiableList(new ArrayList<ItemStack>());

	public static final DeathOption NONE = new DeathOption() {

		@Override
		public void onPlayerDamage(ServerPlayer serverPlayer, EntityDamageEvent event) {
			cancelPlayerDeath(serverPlayer.player, event);
		}

		@Override
		public void onPlayerDeath(ServerPlayer serverPlayer, PlayerDeathEvent event) {
		}
	};

	public static final DeathOption MENU = new DeathOption() {

		@Override
		public void onPlayerDamage(ServerPlayer serverPlayer, EntityDamageEvent event) {
		}

		@Override
		public void onPlayerDeath(ServerPlayer serverPlayer, PlayerDeathEvent event) {
		}
	};

	public static final DeathOption INSTANT = new DeathOption() {

		@Override
		public void onPlayerDamage(ServerPlayer serverPlayer, EntityDamageEvent event) {
			checkInstantPlayerDeath(serverPlayer.player, event);
		}

		@Override
		public void onPlayerDeath(ServerPlayer serverPlayer, PlayerDeathEvent event) {
			instantPlayerDeath(serverPlayer);
		}
	};

	public static final DeathOption SHOW_BODY_MENU = new DeathOption() {

		@Override
		public void onPlayerDamage(ServerPlayer serverPlayer, EntityDamageEvent event) {
		}

		@Override
		public void onPlayerDeath(ServerPlayer serverPlayer, PlayerDeathEvent event) {
			spawnBody(serverPlayer);
		}
	};

	public static final DeathOption SHOW_BODY_INSTANT = new DeathOption() {

		@Override
		public void onPlayerDamage(ServerPlayer serverPlayer, EntityDamageEvent event) {
			checkInstantPlayerDeath(serverPlayer.player, event);
		}

		@Override
		public void onPlayerDeath(ServerPlayer serverPlayer, PlayerDeathEvent event) {
			instantPlayerDeath(serverPlayer);
			spawnBody(serverPlayer);
		}
	};

	public static void cancelPlayerDeath(Player player, EntityDamageEvent event) {
		if (player.getHealth() - event.getFinalDamage() <= 0)
			event.setCancelled(true);
	}

	public static void checkInstantPlayerDeath(Player player, EntityDamageEvent event) {
		// TODO: Fix death messages
		if (player.getHealth() - event.getFinalDamage() <= 0) {
			AiostEventFactory.callPlayerDeathEvent(player, EMPTY_DROPS, 0, null);
			event.setCancelled(true);
		}
	}

	public static void instantPlayerDeath(ServerPlayer serverPlayer) {
		serverPlayer.resetStats();
		PotionEffect.potionEffect(serverPlayer.player, PotionEffectType.BLINDNESS, 40, 5);
		PotionEffect.potionEffect(serverPlayer.player, PotionEffectType.DAMAGE_RESISTANCE, 100, 10);
	}

	public static void spawnBody(ServerPlayer serverPlayer) {
		PacketPlayer packetPlayer = PacketEntityTypes.spawn(PacketEntityTypes.ENTITY_PLAYER,
				serverPlayer.player.getLocation());
		// TODO set DataWatcher flag for lying down!
	}
}
