package com.pm.aiost.item.spell.spells;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.pm.aiost.Aiost;
import com.pm.aiost.item.spell.Spell;
import com.pm.aiost.misc.packet.disguise.Disguise;
import com.pm.aiost.player.ServerPlayer;

public class DisguiseSpell extends Spell {

	private final Disguise disguise;
	private final int duration;

	public DisguiseSpell(String name, int cooldown, double cost, Disguise disguise, int duration) {
		this(name, name, cooldown, cost, disguise, duration);
	}

	public DisguiseSpell(String name, String displayName, int cooldown, double cost, Disguise disguise, int duration) {
		super(name, displayName, cooldown, cost);
		this.disguise = disguise;
		this.duration = duration;
	}

	@Override
	public boolean cast(ServerPlayer serverPlayer, int i) {
		serverPlayer.setDisguise(disguise);
		Player player = serverPlayer.player;
		doAnimation(player.getWorld(), player.getLocation());

		Bukkit.getScheduler().runTaskLater(Aiost.getPlugin(), () -> {
			if (serverPlayer.getDisguise() == disguise) {
				serverPlayer.removeDisguise();
				doAnimation(player.getWorld(), player.getLocation());
			}
		}, duration);
		return true;
	}

	@Override
	public void cast(LivingEntity entity, int i) {
		throw new UnsupportedOperationException();
	}

	public Disguise getDisguise() {
		return disguise;
	}

	public int getDuration() {
		return duration;
	}

	protected void doAnimation(World world, Location loc) {
		world.playSound(loc, Sound.ENTITY_ENDERMAN_TELEPORT, SoundCategory.NEUTRAL, 0.5F, 0.4F / (0.5F * 0.4F + 0.8F));
		world.spawnParticle(Particle.PORTAL, loc, 20, 0.5F, 0.5F, 0.5F, 0.1F, null, false);
	}
}
