package com.pm.aiost.player;

import static com.pm.aiost.misc.utils.ChatColor.RED;

import java.util.UUID;

import com.google.common.io.ByteArrayDataInput;
import com.pm.aiost.misc.menu.Menu;
import com.pm.aiost.misc.menu.menus.PartyMenu;

public class PartyManager {

	public static void inviteParty(ServerPlayer serverPlayer, String playerName) {
		ServerPlayer targetServerPlayer = ServerPlayer.getByName(playerName);
		if (targetServerPlayer == null) {
			serverPlayer.player.sendMessage(RED + "No player found for name '" + playerName + "'");
			return;
		}
		Party party = serverPlayer.getLocalParty();
		if (!party.isOwner(serverPlayer)) {
			serverPlayer.player.sendMessage(RED + "Only party owner can invite other player!");
			return;
		}
		targetServerPlayer.setCooldown("partyInvite_" + serverPlayer.player.getUniqueId(), 1200);
		Party.sendRequest(targetServerPlayer, serverPlayer.name);
		serverPlayer.player.sendMessage("Party invitation was sent to player '" + playerName + "'");
	}

	public static void joinParty(ServerPlayer serverPlayer, String requestPlayerName) {
		ServerPlayer requestServerPlayer = ServerPlayer.getByName(requestPlayerName);
		if (requestServerPlayer == null) {
			serverPlayer.player.sendMessage(RED + "No player found for name '" + requestPlayerName + "'");
			return;
		}
		if (!serverPlayer.hasCooldown("partyInvite_" + requestServerPlayer.player.getUniqueId())) {
			serverPlayer.player.sendMessage(RED + "You don't have an invitation or it has run out!");
			return;
		}
		serverPlayer.joinLocalParty(requestServerPlayer.getLocalParty());
		serverPlayer.player.sendMessage("Accepted party invitation from '" + requestPlayerName + "'");
	}

	public static void leaveParty(ServerPlayer serverPlayer) {
		if (!serverPlayer.hasLocalParty()) {
			serverPlayer.player.sendMessage(RED + "Not currently in a party!");
			return;
		}
		serverPlayer.leaveLocalParty();
		serverPlayer.player.sendMessage("Party left!");
	}

	public static void requestPartyData(ServerPlayer serverPlayer) {
		if (!serverPlayer.hasLocalParty())
			return;
		Menu menu = serverPlayer.getMenu(PartyMenu.class);
		if (menu == null)
			return;
		Party party = serverPlayer.getLocalParty();
		((PartyMenu) menu).setMembers(party.getOwner(), party.getMember());
	}

	public static void removeFromParty(ServerPlayer serverPlayer, UUID uuid) {
		ServerPlayer targetServerPlayer = ServerPlayer.getByUUID(uuid);
		if (targetServerPlayer == null) {
			serverPlayer.player.sendMessage(RED + "No player found for uuid '" + uuid.toString() + "'");
			return;
		}
		Party party = serverPlayer.getLocalParty();
		if (!party.isOwner(serverPlayer)) {
			serverPlayer.player.sendMessage(RED + "Only party owner can remove player!");
			return;
		}
		if (!party.contains(targetServerPlayer)) {
			serverPlayer.player.sendMessage(RED + "party does not contain this player!");
			return;
		}
		targetServerPlayer.leaveLocalParty();
		requestPartyData(serverPlayer);
	}

	public static void recievePartyRequest(ServerPlayer serverPlayer, ByteArrayDataInput in) {
		Menu menu = serverPlayer.getMenu(PartyMenu.class);
		if (menu != null)
			((PartyMenu) menu).setMembers(in);
	}
}
