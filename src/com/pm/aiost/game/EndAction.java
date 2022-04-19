package com.pm.aiost.game;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.bukkit.World;

import com.pm.aiost.misc.menu.menus.DatabaseGameMenu.GameData;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.server.request.ServerRequest;
import com.pm.aiost.server.world.creation.WorldList;
import com.pm.aiost.server.world.creation.WorldLoader;

public abstract class EndAction implements Consumer<Game> {

	private static final List<EndAction> LIST = new ArrayList<EndAction>();

	public final int id;

	public EndAction() {
		id = LIST.size();
		LIST.add(this);
	}

	public static EndAction get(int id) {
		return LIST.get(id);
	}

	public static final EndAction DELETE = new EndAction() {

		@Override
		public void accept(Game game) {
			game.getRegion().delete(true); // false doesn't work!
		}
	};

	public static final EndAction RERUN = new EndAction() {

		@Override
		public void accept(Game game) {
			Game newGame = game.getType().get().init(game);
			newGame.setEndAction(EndAction.RERUN);
			newGame.getRegion().setEventHandler(new GameLobby(newGame), false);
		}
	};

	public static final EndAction RELOAD = new EndAction() {
		@Override
		public void accept(Game game) {
			ServerPlayer host = game.getHost();
			GameData gameData = new GameData();
			gameData.uuid = game.getUniqueId();
			gameData.name = game.getType().name;
			gameData.authorName = game.getAuthorName();
			gameData.gameType = game.getType();
			World world = host.player.getWorld();
			gameData.environment = world.getEnvironment();
			gameData.worldType = WorldList.getType(world);
			gameData.generateStructures = world.canGenerateStructures();

			GameLobby gameLobby = WorldLoader.loadGame(host, game.getId(), gameData, game.getMinPlayer(),
					game.getMaxPlayer(), null); // TODO password
			if (gameLobby != null) {
				gameLobby.getGame().setEndAction(EndAction.RELOAD);
				for (ServerPlayer serverPlayer : game.getRegion().getServerPlayer())
					ServerRequest.getHandler().joinGame(serverPlayer, gameLobby.getGame());
			}
		}
	};

	public static abstract class NextGameAction extends EndAction {

		private GameData gameData;

		public NextGameAction(GameData gameData) {
			this.gameData = gameData;
		}

		@Override
		public void accept(Game game) {
			GameLobby gameLobby = WorldLoader.loadGame(game.getHost(), game.getId(), gameData,
					gameData.gameType.minPlayer, gameData.gameType.maxPlayer, null); // TODO password
			if (gameLobby != null) {
				for (ServerPlayer serverPlayer : game.getRegion().getServerPlayer())
					ServerRequest.getHandler().joinGame(serverPlayer, gameLobby.getGame());
			}
		}

		public void setGameData(GameData gameData) {
			this.gameData = gameData;
		}

		public GameData getGameData() {
			return gameData;
		}
	};
}
