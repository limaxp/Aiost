package com.pm.aiost.misc.command.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.pm.aiost.misc.command.CommandUtil;
import com.pm.aiost.misc.database.Database;
import com.pm.aiost.misc.database.DatabaseBuilder;
import com.pm.aiost.misc.database.DatabaseManager;
import com.pm.aiost.misc.database.ScriptReader;

public class DatabaseCommands {

	public static class BuildDatabaseCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (!CommandUtil.isAdminOrConsole(sender))
				return false;
			DatabaseBuilder.buildDatabase();
			return true;
		}
	}

	public static class QueryCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (!CommandUtil.isAdminOrConsole(sender))
				return false;
			if (args.length > 0) {
				ArrayList<Object[]> result = DatabaseManager.getDatabase().query(String.join(" ", args));
				CommandUtil.sendMsg(sender,
						"Database query complete with result:\n" + Database.resultListToString(result));
				return true;
			} else {
				CommandUtil.sendError(sender, "Command must have at least 1 argument!");
				return false;
			}
		}
	}

	public static class UpdateQueryCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (!CommandUtil.isAdminOrConsole(sender))
				return false;
			if (args.length > 0) {
				int result = DatabaseManager.getDatabase().update(String.join(" ", args));
				CommandUtil.sendMsg(sender, "Database query complete with result: " + result);
				return true;
			} else {
				CommandUtil.sendError(sender, "Command must have at least 1 argument!");
				return false;
			}
		}
	}

	public static class ExecuteQueryCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (!CommandUtil.isAdminOrConsole(sender))
				return false;
			if (args.length > 0) {
				boolean result = DatabaseManager.getDatabase().exec(String.join(" ", args));
				CommandUtil.sendMsg(sender, "Database query complete with result: " + result);
				return true;
			} else {
				CommandUtil.sendError(sender, "Command must have at least 1 argument!");
				return false;
			}
		}
	}

	public static class CallQueryCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (!CommandUtil.isAdminOrConsole(sender))
				return false;
			if (args.length > 0) {
				ArrayList<Object[]> result;
				if (args.length > 1) {
					List<Object> argObjects = new ArrayList<Object>();
					String arg;
					for (int i = 1; i < args.length; i++) { // TODO: make real arg parsing here and in other
															// command!
						arg = args[i];
						if (arg.matches(".*\\d+.*"))
							argObjects.add(Integer.parseInt(arg));
						else
							argObjects.add(arg);
					}
					result = DatabaseManager.getDatabase().call("CALL " + String.join(" ", args), argObjects);
				} else
					result = DatabaseManager.getDatabase().call("CALL " + String.join(" ", args));

				CommandUtil.sendMsg(sender,
						"Database call complete with result:\n" + Database.resultListToString(result));
				return true;

			} else {
				CommandUtil.sendError(sender, "Command must have at least 1 argument!");
				return false;
			}

		}
	}

	public static class BatchQueryCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (!CommandUtil.isAdminOrConsole(sender))
				return false;
			if (args.length > 0) {
				int[] result = DatabaseManager.getDatabase()
						.batch(ScriptReader.scriptToBatchList(String.join(" ", args)));
				CommandUtil.sendMsg(sender, "Database query complete with result:\n" + Arrays.toString(result));
				return true;
			} else {
				CommandUtil.sendError(sender, "Command must have at least 1 argument!");
				return false;
			}
		}
	}
}
