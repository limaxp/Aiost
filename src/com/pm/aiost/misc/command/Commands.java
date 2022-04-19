package com.pm.aiost.misc.command;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.plugin.java.JavaPlugin;

import com.pm.aiost.Aiost;
import com.pm.aiost.misc.command.commands.DatabaseCommands.BatchQueryCommand;
import com.pm.aiost.misc.command.commands.DatabaseCommands.BuildDatabaseCommand;
import com.pm.aiost.misc.command.commands.DatabaseCommands.CallQueryCommand;
import com.pm.aiost.misc.command.commands.DatabaseCommands.ExecuteQueryCommand;
import com.pm.aiost.misc.command.commands.DatabaseCommands.QueryCommand;
import com.pm.aiost.misc.command.commands.DatabaseCommands.UpdateQueryCommand;
import com.pm.aiost.misc.command.commands.ItemCommands.GiveBuildStaffCommand;
import com.pm.aiost.misc.command.commands.ItemCommands.GiveMarkerItemCommand;
import com.pm.aiost.misc.command.commands.ItemCommands.GiveWorldBrushItemCommand;
import com.pm.aiost.misc.command.commands.ItemCommands.GiveWorldEditItemCommand;
import com.pm.aiost.misc.command.commands.MenuCommands.OpenEffectItemMenuCommand;
import com.pm.aiost.misc.command.commands.MenuCommands.OpenItemMenuCommand;
import com.pm.aiost.misc.command.commands.MenuCommands.OpenMainMenuCommand;
import com.pm.aiost.misc.command.commands.MenuCommands.OpenSpawnMenuCommand;
import com.pm.aiost.misc.command.commands.MenuCommands.OpenWorldEffectsMenuCommand;
import com.pm.aiost.misc.command.commands.MenuCommands.OpenWorldSettingMenuCommand;
import com.pm.aiost.misc.command.commands.PlayerCommands.AcceptInviteCommand;
import com.pm.aiost.misc.command.commands.PlayerCommands.AddCreditCommand;
import com.pm.aiost.misc.command.commands.PlayerCommands.AddPermissionCommand;
import com.pm.aiost.misc.command.commands.PlayerCommands.ChangeGamemodeCommand;
import com.pm.aiost.misc.command.commands.PlayerCommands.DisguiseAdminCommand;
import com.pm.aiost.misc.command.commands.PlayerCommands.DisguisePlayerCommand;
import com.pm.aiost.misc.command.commands.PlayerCommands.DisguiseRemoveCommand;
import com.pm.aiost.misc.command.commands.PlayerCommands.DuelCommand;
import com.pm.aiost.misc.command.commands.PlayerCommands.FlyCommand;
import com.pm.aiost.misc.command.commands.PlayerCommands.FlySpeedCommand;
import com.pm.aiost.misc.command.commands.PlayerCommands.FriendDeclineCommand;
import com.pm.aiost.misc.command.commands.PlayerCommands.FriendRequestCommand;
import com.pm.aiost.misc.command.commands.PlayerCommands.GamemodeAdventureCommand;
import com.pm.aiost.misc.command.commands.PlayerCommands.GamemodeCreativeCommand;
import com.pm.aiost.misc.command.commands.PlayerCommands.GamemodeSurvivalCommand;
import com.pm.aiost.misc.command.commands.PlayerCommands.HealCommand;
import com.pm.aiost.misc.command.commands.PlayerCommands.InvisiblesHideCommand;
import com.pm.aiost.misc.command.commands.PlayerCommands.InvisiblesShowCommand;
import com.pm.aiost.misc.command.commands.PlayerCommands.InvitePlayerCommand;
import com.pm.aiost.misc.command.commands.PlayerCommands.LobbyCommand;
import com.pm.aiost.misc.command.commands.PlayerCommands.PartyCommand;
import com.pm.aiost.misc.command.commands.PlayerCommands.PartyJoinCommand;
import com.pm.aiost.misc.command.commands.PlayerCommands.PartyLeaveCommand;
import com.pm.aiost.misc.command.commands.PlayerCommands.RemoveOperatorCommand;
import com.pm.aiost.misc.command.commands.PlayerCommands.SetOperatorCommand;
import com.pm.aiost.misc.command.commands.PlayerCommands.SetRankCommand;
import com.pm.aiost.misc.command.commands.PlayerCommands.SetSettingCommand;
import com.pm.aiost.misc.command.commands.PlayerCommands.ShowCreditCommand;
import com.pm.aiost.misc.command.commands.PlayerCommands.UnfriendCommand;
import com.pm.aiost.misc.command.commands.ResourcePackCommands.ApplyResourcePackCommand;
import com.pm.aiost.misc.command.commands.ResourcePackCommands.BuildResourcePackCommand;
import com.pm.aiost.misc.command.commands.ResourcePackCommands.RemoveResourcePackCommand;
import com.pm.aiost.misc.command.commands.ServerCommands.ReloadCommand;
import com.pm.aiost.misc.command.commands.ServerCommands.RestartCommand;
import com.pm.aiost.misc.command.commands.ServerCommands.SetServerTypeCommand;
import com.pm.aiost.misc.command.commands.SpawnCommands.SetFurnitureCommand;
import com.pm.aiost.misc.command.commands.SpawnCommands.SetHologramCommand;
import com.pm.aiost.misc.command.commands.SpawnCommands.SpawnPacketEntityCommand;
import com.pm.aiost.misc.command.commands.SpawnCommands.SpawnPacketPlayerCommand;
import com.pm.aiost.misc.command.commands.WorldCommands.AddWorldCommand;
import com.pm.aiost.misc.command.commands.WorldCommands.ChangeWorldCommand;
import com.pm.aiost.misc.command.commands.WorldCommands.CreatePortalCommand;
import com.pm.aiost.misc.command.commands.WorldCommands.CreateRegionCommand;
import com.pm.aiost.misc.command.commands.WorldCommands.CreateWorldCommand;
import com.pm.aiost.misc.command.commands.WorldCommands.DeletePortalCommand;
import com.pm.aiost.misc.command.commands.WorldCommands.DeleteRegionCommand;
import com.pm.aiost.misc.command.commands.WorldCommands.DeleteWorldCommand;
import com.pm.aiost.misc.command.commands.WorldCommands.EditRegionCommand;
import com.pm.aiost.misc.command.commands.WorldCommands.TimeDayCommand;
import com.pm.aiost.misc.command.commands.WorldCommands.TimeMidnightCommand;
import com.pm.aiost.misc.command.commands.WorldCommands.TimeNightCommand;
import com.pm.aiost.misc.command.commands.WorldCommands.TimeNoonCommand;
import com.pm.aiost.misc.command.commands.WorldCommands.WorldTeleportCommand;

public class Commands {

	private static final Map<String, String> OVERRIDEN_COMMANDS = new HashMap<String, String>();

	public static void init() {
		linkCommands();
		overrideCommands();
	}

	private static void linkCommands() {
		JavaPlugin plugin = Aiost.getPlugin();
		plugin.getCommand("rl").setExecutor(new ReloadCommand());
		plugin.getCommand("re").setExecutor(new RestartCommand());
		plugin.getCommand("serverType").setExecutor(new SetServerTypeCommand());

		OpenMainMenuCommand openMainMenuCommand = new OpenMainMenuCommand();
		plugin.getCommand("menu").setExecutor(openMainMenuCommand);
		plugin.getCommand("m").setExecutor(openMainMenuCommand);
		plugin.getCommand("itemMenu").setExecutor(new OpenItemMenuCommand());
		plugin.getCommand("effectMenu").setExecutor(new OpenEffectItemMenuCommand());
		plugin.getCommand("spawnMenu").setExecutor(new OpenSpawnMenuCommand());
		plugin.getCommand("worldSettings").setExecutor(new OpenWorldSettingMenuCommand());
		plugin.getCommand("worldEffects").setExecutor(new OpenWorldEffectsMenuCommand());

		plugin.getCommand("marker").setExecutor(new GiveMarkerItemCommand());
		plugin.getCommand("buildstaff").setExecutor(new GiveBuildStaffCommand());
		plugin.getCommand("worldEdit").setExecutor(new GiveWorldEditItemCommand());
		plugin.getCommand("worldBrush").setExecutor(new GiveWorldBrushItemCommand());

		plugin.getCommand("lobby").setExecutor(new LobbyCommand());
		plugin.getCommand("heal").setExecutor(new HealCommand());
		plugin.getCommand("fly").setExecutor(new FlyCommand());
		plugin.getCommand("flySpeed").setExecutor(new FlySpeedCommand());
		SetOperatorCommand setOperatorCommand = new SetOperatorCommand();
		plugin.getCommand("op").setExecutor(setOperatorCommand);
		plugin.getCommand("operator").setExecutor(setOperatorCommand);
		plugin.getCommand("rank").setExecutor(new SetRankCommand());
		plugin.getCommand("deop").setExecutor(new RemoveOperatorCommand());
		plugin.getCommand("addPermission").setExecutor(new AddPermissionCommand());
		plugin.getCommand("setSetting").setExecutor(new SetSettingCommand());
		plugin.getCommand("friend").setExecutor(new FriendRequestCommand());
		plugin.getCommand("friendDecline").setExecutor(new FriendDeclineCommand());
		plugin.getCommand("unfriend").setExecutor(new UnfriendCommand());
		plugin.getCommand("disguise").setExecutor(new DisguisePlayerCommand());
		plugin.getCommand("adminDisguise").setExecutor(new DisguiseAdminCommand());
		plugin.getCommand("removeDisguise").setExecutor(new DisguiseRemoveCommand());
		plugin.getCommand("showInvisibles").setExecutor(new InvisiblesShowCommand());
		plugin.getCommand("hideInvisibles").setExecutor(new InvisiblesHideCommand());
		plugin.getCommand("party").setExecutor(new PartyCommand());
		plugin.getCommand("partyJoin").setExecutor(new PartyJoinCommand());
		plugin.getCommand("partyLeave").setExecutor(new PartyLeaveCommand());
		ChangeGamemodeCommand changeGamemode = new ChangeGamemodeCommand();
		plugin.getCommand("gamemode").setExecutor(changeGamemode);
		plugin.getCommand("gm").setExecutor(changeGamemode);
		plugin.getCommand("gmc").setExecutor(new GamemodeCreativeCommand());
		plugin.getCommand("gms").setExecutor(new GamemodeSurvivalCommand());
		plugin.getCommand("gma").setExecutor(new GamemodeAdventureCommand());
		ShowCreditCommand showCreditCommand = new ShowCreditCommand();
		plugin.getCommand("credits").setExecutor(showCreditCommand);
		plugin.getCommand("credit").setExecutor(showCreditCommand);
		plugin.getCommand("c").setExecutor(showCreditCommand);
		plugin.getCommand("addCredits").setExecutor(new AddCreditCommand());
		plugin.getCommand("duel").setExecutor(new DuelCommand());
		plugin.getCommand("invite").setExecutor(new InvitePlayerCommand());
		plugin.getCommand("acceptInvite").setExecutor(new AcceptInviteCommand());

		plugin.getCommand("buildResourcePack").setExecutor(new BuildResourcePackCommand());
		ApplyResourcePackCommand applyResourcePackCommand = new ApplyResourcePackCommand();
		plugin.getCommand("resourcepack").setExecutor(applyResourcePackCommand);
		plugin.getCommand("texturepack").setExecutor(applyResourcePackCommand);
		RemoveResourcePackCommand removeResourcePackCommand = new RemoveResourcePackCommand();
		plugin.getCommand("removeResourcepack").setExecutor(removeResourcePackCommand);
		plugin.getCommand("removeTexturepack").setExecutor(removeResourcePackCommand);

		plugin.getCommand("buildDatabase").setExecutor(new BuildDatabaseCommand());
		plugin.getCommand("query").setExecutor(new QueryCommand());
		plugin.getCommand("updateQuery").setExecutor(new UpdateQueryCommand());
		plugin.getCommand("execQuery").setExecutor(new ExecuteQueryCommand());
		plugin.getCommand("callQuery").setExecutor(new CallQueryCommand());
		plugin.getCommand("batchQuery").setExecutor(new BatchQueryCommand());

		plugin.getCommand("createWorld").setExecutor(new CreateWorldCommand());
		plugin.getCommand("deleteWorld").setExecutor(new DeleteWorldCommand());
		plugin.getCommand("addWorld").setExecutor(new AddWorldCommand());
		plugin.getCommand("world").setExecutor(new ChangeWorldCommand());
		plugin.getCommand("tpWorld").setExecutor(new WorldTeleportCommand());
		plugin.getCommand("portal").setExecutor(new CreatePortalCommand());
		plugin.getCommand("deletePortal").setExecutor(new DeletePortalCommand());
		plugin.getCommand("createRegion").setExecutor(new CreateRegionCommand());
		plugin.getCommand("deleteRegion").setExecutor(new DeleteRegionCommand());
		plugin.getCommand("editRegion").setExecutor(new EditRegionCommand());
		plugin.getCommand("day").setExecutor(new TimeDayCommand());
		plugin.getCommand("noon").setExecutor(new TimeNoonCommand());
		plugin.getCommand("night").setExecutor(new TimeNightCommand());
		plugin.getCommand("midnight").setExecutor(new TimeMidnightCommand());

		plugin.getCommand("setFurniture").setExecutor(new SetFurnitureCommand());
		plugin.getCommand("setHologram").setExecutor(new SetHologramCommand());
		plugin.getCommand("setEntity").setExecutor(new SpawnPacketEntityCommand());
		plugin.getCommand("setPlayer").setExecutor(new SpawnPacketPlayerCommand());
	}

	private static void overrideCommands() {
		override("reload", "rl");
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
