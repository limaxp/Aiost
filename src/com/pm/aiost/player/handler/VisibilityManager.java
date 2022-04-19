package com.pm.aiost.player.handler;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import com.pm.aiost.Aiost;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.player.settings.PlayerSettings;
import com.pm.aiost.server.ServerManager;

public class VisibilityManager {

	private static final List<Player> VISIBLE_PLAYER;
	private static final List<Player> INVISIBLE_PLAYER;
	private static final List<Player> SHOW_INVISIBLE_VISIBLE_PLAYER;
	private static final List<Player> SHOW_INVISIBLE_INVISIBLE_PLAYER;

	static {
		int maxPlayer = ServerManager.getServer().getType().maxPlayer;
		VISIBLE_PLAYER = new ArrayList<Player>(maxPlayer);
		INVISIBLE_PLAYER = new ArrayList<Player>(maxPlayer);
		SHOW_INVISIBLE_VISIBLE_PLAYER = new ArrayList<Player>(maxPlayer);
		SHOW_INVISIBLE_INVISIBLE_PLAYER = new ArrayList<Player>(maxPlayer);
	}

	public static void onPlayerJoin(ServerPlayer serverPlayer) {
		Player player = serverPlayer.player;
		short visibilitySetting = serverPlayer.getSetting(PlayerSettings.VISIBILITY);
		if (serverPlayer.getSetting(PlayerSettings.CAN_SEE_INVISIBLES) < 1) {
			hide(player, INVISIBLE_PLAYER);
			hideOthers(player, SHOW_INVISIBLE_INVISIBLE_PLAYER);
			if (visibilitySetting < 1) {
				hide(player, VISIBLE_PLAYER);
				hideOthers(player, SHOW_INVISIBLE_VISIBLE_PLAYER);
				INVISIBLE_PLAYER.add(player);
			} else
				VISIBLE_PLAYER.add(player);
		} else {
			hideSelf(player, INVISIBLE_PLAYER);
			if (visibilitySetting < 1) {
				hideSelf(player, VISIBLE_PLAYER);
				SHOW_INVISIBLE_INVISIBLE_PLAYER.add(player);
			} else
				SHOW_INVISIBLE_VISIBLE_PLAYER.add(player);
		}
	}

	public static void onPlayerQuit(ServerPlayer serverPlayer) {
		if (serverPlayer.getSetting(PlayerSettings.CAN_SEE_INVISIBLES) < 1) {
			if (serverPlayer.getSetting(PlayerSettings.VISIBILITY) < 1)
				INVISIBLE_PLAYER.remove(serverPlayer.player);
			else
				VISIBLE_PLAYER.remove(serverPlayer.player);
		}

		else {
			if (serverPlayer.getSetting(PlayerSettings.VISIBILITY) < 1)
				SHOW_INVISIBLE_INVISIBLE_PLAYER.remove(serverPlayer.player);
			else
				SHOW_INVISIBLE_VISIBLE_PLAYER.remove(serverPlayer.player);
		}
	}

	public static void setVisible(ServerPlayer serverPlayer) {
		if (serverPlayer.getSetting(PlayerSettings.VISIBILITY) < 1)
			return;

		Player player = serverPlayer.player;
		if (serverPlayer.getSetting(PlayerSettings.CAN_SEE_INVISIBLES) < 1) {
			INVISIBLE_PLAYER.remove(player);
			show(player, VISIBLE_PLAYER);
			showOthers(player, SHOW_INVISIBLE_VISIBLE_PLAYER);
			VISIBLE_PLAYER.add(player);
		}

		else {
			SHOW_INVISIBLE_INVISIBLE_PLAYER.remove(player);
			showSelf(player, VISIBLE_PLAYER);
			SHOW_INVISIBLE_VISIBLE_PLAYER.add(player);
		}
	}

	public static void setInvisible(ServerPlayer serverPlayer) {
		if (serverPlayer.getSetting(PlayerSettings.VISIBILITY) > 0)
			return;

		Player player = serverPlayer.player;
		if (serverPlayer.getSetting(PlayerSettings.CAN_SEE_INVISIBLES) < 1) {
			VISIBLE_PLAYER.remove(player);
			hide(player, VISIBLE_PLAYER);
			hideOthers(player, SHOW_INVISIBLE_INVISIBLE_PLAYER);
			INVISIBLE_PLAYER.add(player);
		}

		else {
			SHOW_INVISIBLE_VISIBLE_PLAYER.remove(player);
			hideSelf(player, VISIBLE_PLAYER);
			SHOW_INVISIBLE_INVISIBLE_PLAYER.add(player);
		}
	}

	public static void showInvisibles(ServerPlayer serverPlayer) {
		if (serverPlayer.getSetting(PlayerSettings.CAN_SEE_INVISIBLES) < 1)
			return;

		Player player = serverPlayer.player;
		if (serverPlayer.getSetting(PlayerSettings.VISIBILITY) < 1) {
			INVISIBLE_PLAYER.remove(player);
			showOthers(player, INVISIBLE_PLAYER);
			showOthers(player, VISIBLE_PLAYER);
			showOthers(player, SHOW_INVISIBLE_INVISIBLE_PLAYER);
			showOthers(player, SHOW_INVISIBLE_VISIBLE_PLAYER);
			SHOW_INVISIBLE_INVISIBLE_PLAYER.add(player);
		}

		else {
			VISIBLE_PLAYER.remove(player);
			showOthers(player, INVISIBLE_PLAYER);
			showOthers(player, SHOW_INVISIBLE_INVISIBLE_PLAYER);
			SHOW_INVISIBLE_VISIBLE_PLAYER.add(player);
		}
	}

	public static void hideInvisibles(ServerPlayer serverPlayer) {
		if (serverPlayer.getSetting(PlayerSettings.CAN_SEE_INVISIBLES) > 0)
			return;

		Player player = serverPlayer.player;
		if (serverPlayer.getSetting(PlayerSettings.VISIBILITY) < 1) {
			SHOW_INVISIBLE_INVISIBLE_PLAYER.remove(player);
			hideOthers(player, INVISIBLE_PLAYER);
			hideOthers(player, VISIBLE_PLAYER);
			hideOthers(player, SHOW_INVISIBLE_INVISIBLE_PLAYER);
			hideOthers(player, SHOW_INVISIBLE_VISIBLE_PLAYER);
			INVISIBLE_PLAYER.add(player);
		}

		else {
			SHOW_INVISIBLE_VISIBLE_PLAYER.remove(player);
			hideOthers(player, INVISIBLE_PLAYER);
			hideOthers(player, SHOW_INVISIBLE_INVISIBLE_PLAYER);
			VISIBLE_PLAYER.add(player);
		}
	}

	private static void show(Player player, List<Player> playerList) {
		for (Player listPlayer : playerList) {
			player.showPlayer(Aiost.getPlugin(), listPlayer);
			listPlayer.showPlayer(Aiost.getPlugin(), player);
		}
	}

	private static void showSelf(Player player, List<Player> playerList) {
		for (Player listPlayer : playerList)
			listPlayer.showPlayer(Aiost.getPlugin(), player);
	}

	private static void showOthers(Player player, List<Player> playerList) {
		for (Player listPlayer : playerList)
			player.showPlayer(Aiost.getPlugin(), listPlayer);
	}

	private static void hide(Player player, List<Player> playerList) {
		for (Player listPlayer : playerList) {
			player.hidePlayer(Aiost.getPlugin(), listPlayer);
			listPlayer.hidePlayer(Aiost.getPlugin(), player);
		}
	}

	private static void hideSelf(Player player, List<Player> playerList) {
		for (Player listPlayer : playerList)
			listPlayer.hidePlayer(Aiost.getPlugin(), player);
	}

	private static void hideOthers(Player player, List<Player> playerList) {
		for (Player listPlayer : playerList)
			player.hidePlayer(Aiost.getPlugin(), listPlayer);
	}
}
