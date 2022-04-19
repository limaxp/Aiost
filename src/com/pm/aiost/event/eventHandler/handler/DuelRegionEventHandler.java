package com.pm.aiost.event.eventHandler.handler;

import com.pm.aiost.event.eventHandler.EventHandler;
import com.pm.aiost.player.ServerPlayer;

public class DuelRegionEventHandler extends DuelEventHandler {

	public DuelRegionEventHandler() {
	}

	public DuelRegionEventHandler(int bet) {
		this.bet = bet;
	}

	@Override
	public void onPlayerJoin(ServerPlayer serverPlayer) {
		if (serverPlayer1 == null) {
			if (serverPlayer.hasCredits(bet))
				serverPlayer1 = serverPlayer;
		} else if (serverPlayer2 == null) {
			if (serverPlayer.hasCredits(bet)) {
				serverPlayer2 = serverPlayer;
				startScheduler();
			}
		}
	}

	@Override
	public void onPlayerQuit(ServerPlayer serverPlayer, QuitReason reason) {
		// TODO: implement this!
	}

	@Override
	public boolean allowsChange(EventHandler eventHandler) {
		return true;
	}

	@Override
	public String getEventHandlerName() {
		return "DuelRegion";
	}
}
