package com.pm.aiost.game.games.castleDefend;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.pm.aiost.game.Game;
import com.pm.aiost.game.GamePlayer;
import com.pm.aiost.misc.utils.ChatColor;

public class CastleDefendPlayer extends GamePlayer {

	public int kills;

	public CastleDefendPlayer(Game game) {
		super(game);
	}

	@Override
	public int getCreditsEarned() {
		return getPoints() / 1000;
	}

	public void load(ConfigurationSection section) {
		super.load(section);
		kills = section.getInt("kills");
	}

	public void save(ConfigurationSection section) {
		super.save(section);
		section.set("kills", kills);
	}

	@Override
	public void databaseSave(StringBuilder builder) {
		super.databaseSave(builder);
		builder.append(',');
		builder.append(kills);
	}

	@Override
	public int setLives(int lives) {
		super.setLives(lives);
		Player player = getServerPlayer().player;
		player.setPlayerListName(
				ChatColor.DARK_RED + player.getDisplayName() + ' ' + ChatColor.GOLD + ChatColor.BOLD + lives);
		return lives;
	}
}
