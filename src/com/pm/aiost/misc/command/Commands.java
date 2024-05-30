package com.pm.aiost.misc.command;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.CommandExecutor;

import com.pm.aiost.Aiost;
import com.pm.aiost.misc.command.commands.DatabaseCommands;
import com.pm.aiost.misc.command.commands.ItemCommands;
import com.pm.aiost.misc.command.commands.MenuCommands;
import com.pm.aiost.misc.command.commands.PlayerCommands;
import com.pm.aiost.misc.command.commands.ServerCommands;
import com.pm.aiost.misc.command.commands.WorldCommands;
import com.pm.aiost.misc.command.commands.WorldCommands.SetTimeCommand;

public class Commands {

	private static final Map<String, String> OVERRIDEN_COMMANDS = new HashMap<String, String>();

	public static void init() {
		set("rl", ServerCommands::reload);
		override("reload", "rl");
		set("re", ServerCommands::restart);
		set("serverType", ServerCommands::setType);
		set("buildResourcePack", ServerCommands::buildResourcePack);
		set("resourcepack", ServerCommands::applyResourcePack);
		set("texturepack", ServerCommands::applyResourcePack);
		set("removeResourcepack", ServerCommands::removeResourcePack);
		set("removeTexturepack", ServerCommands::removeResourcePack);

		set("menu", MenuCommands::openMain);
		set("m", MenuCommands::openMain);
		set("itemMenu", MenuCommands::openItem);
		set("effectMenu", MenuCommands::openEffectItem);
		set("spawnMenu", MenuCommands::openSpawn);
		set("worldSettings", MenuCommands::openWorldSetting);
		set("worldEffects", MenuCommands::openWorldEffects);

		set("marker", ItemCommands::giveMarkerItem);
		set("buildstaff", ItemCommands::giveBuildstaff);
		set("worldEdit", ItemCommands::giveWorldEditItem);
		set("worldBrush", ItemCommands::giveWorldBrushItem);

		set("lobby", PlayerCommands::lobby);
		set("heal", PlayerCommands::heal);
		set("fly", PlayerCommands::fly);
		set("flySpeed", PlayerCommands::flySpeed);
		set("op", PlayerCommands::setOperator);
		set("operator", PlayerCommands::setOperator);
		set("deop", PlayerCommands::removeOperator);
		set("rank", PlayerCommands::setRank);
		set("addPermission", PlayerCommands::addPermission);
		set("setSetting", PlayerCommands::setSetting);
		set("friend", PlayerCommands::friendRequest);
		set("friendDecline", PlayerCommands::friendDecline);
		set("unfriend", PlayerCommands::unfriend);
		set("disguise", PlayerCommands::disguisePlayer);
		set("adminDisguise", PlayerCommands::disguiseAdmin);
		set("removeDisguise", PlayerCommands::disguiseRemove);
		set("showInvisibles", PlayerCommands::invisiblesShow);
		set("hideInvisibles", PlayerCommands::invisiblesHide);
		set("party", PlayerCommands::party);
		set("partyJoin", PlayerCommands::partyJoin);
		set("partyLeave", PlayerCommands::partyLeave);
		set("gamemode", PlayerCommands::changeGamemode);
		set("gm", PlayerCommands::changeGamemode);
		set("gmc", PlayerCommands::gamemodeCreative);
		set("gms", PlayerCommands::gamemodeSurvival);
		set("gma", PlayerCommands::gamemodeAdventure);
		set("credits", PlayerCommands::showCredit);
		set("credit", PlayerCommands::showCredit);
		set("c", PlayerCommands::showCredit);
		set("addCredits", PlayerCommands::addCredit);
		set("duel", PlayerCommands::duel);
		set("invite", PlayerCommands::invitePlayer);
		set("acceptInvite", PlayerCommands::acceptInvite);

		set("buildDatabase", DatabaseCommands::build);
		set("query", DatabaseCommands::query);
		set("updateQuery", DatabaseCommands::update);
		set("execQuery", DatabaseCommands::execute);
		set("callQuery", DatabaseCommands::call);
		set("batchQuery", DatabaseCommands::batch);

		set("createWorld", WorldCommands::createWorld);
		set("deleteWorld", WorldCommands::deleteWorld);
		set("addWorld", WorldCommands::addWorld);
		set("world", WorldCommands::changeWorld);
		set("tpWorld", WorldCommands::teleportToWorld);
		set("portal", WorldCommands::createPortal);
		set("deletePortal", WorldCommands::deletePortal);
		set("createRegion", WorldCommands::createRegion);
		set("deleteRegion", WorldCommands::deleteRegion);
		set("editRegion", WorldCommands::editRegion);
		set("setFurniture", WorldCommands::spawnFurniture);
		set("setHologram", WorldCommands::spawnHologram);
		set("setEntity", WorldCommands::spawnPacketEntity);
		set("setPlayer", WorldCommands::spawnPacketPlayer);
		set("day", new SetTimeCommand(1000));
		set("noon", new SetTimeCommand(6000));
		set("night", new SetTimeCommand(13000));
		set("midnight", new SetTimeCommand(18000));
	}

	public static void set(String command, CommandExecutor executor) {
		Aiost.getPlugin().getCommand(command).setExecutor(executor);
	}

	public static void override(String command, String override) {
		OVERRIDEN_COMMANDS.put(command, override);
	}

	public static String getOverride(String command) {
		return OVERRIDEN_COMMANDS.get(command);
	}

	public static String doOveride(String text) {
		int spacePos = text.indexOf(' ');
		if (spacePos != -1) {
			String override = getOverride(text.substring(0, spacePos));
			if (override != null)
				return override + ' ' + text.substring(spacePos + 1);
		} else {
			String override = getOverride(text);
			if (override != null)
				return override;
		}
		return text;
	}
}
