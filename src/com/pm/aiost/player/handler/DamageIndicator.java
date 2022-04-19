package com.pm.aiost.player.handler;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.pm.aiost.Aiost;
import com.pm.aiost.misc.packet.PacketFactory;
import com.pm.aiost.misc.packet.PacketSender;
import com.pm.aiost.misc.packet.entity.entities.EntitySimpleText;
import com.pm.aiost.misc.utils.ChatColor;
import com.pm.aiost.player.ServerPlayer;

public class DamageIndicator {

	private static final int SHOW_TIME = 10;
	private static final Random RANDOM = new Random();

	public static void show(ServerPlayer serverPlayer, Entity damaged, double damage, boolean isCrit) {
		String color;
		if (isCrit)
			color = ChatColor.RED;
		else
			color = ChatColor.YELLOW;
		EntitySimpleText entity = new EntitySimpleText(serverPlayer.getServerWorld(),
				color + ChatColor.BOLD + Math.round(damage * 10.0) / 10.0);
		Location loc = damaged.getLocation();
		double x = loc.getX() + RANDOM.nextDouble() * 1.5 - 0.75;
		double y = loc.getY() + damaged.getHeight() - 2.5;
		double z = loc.getZ() + RANDOM.nextDouble() * 1.5 - 0.75;
		entity.setPositionRotation(x, y, z, 0.0F, 0.0F);
		Player player = serverPlayer.player;
		entity.spawn(player);
		PacketSender.send(player, PacketFactory.packetEntityTeleport(entity.getId(), x, y + 0.5, z, 0.0F, 0.0F, false));
		Bukkit.getScheduler().runTaskLater(Aiost.getPlugin(), () -> entity.hide(player), SHOW_TIME);
	}
}
