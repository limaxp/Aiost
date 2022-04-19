package com.pm.aiost.misc.menu.menus.request;

import java.util.List;

import com.pm.aiost.misc.menu.menus.YesNoMenu;
import com.pm.aiost.player.ServerPlayer;

public class BooleanMenu {

	public static YesNoMenu create(String name) {
		return create(name, null, null);
	}

	public static YesNoMenu create(String name, List<String> description) {
		return create(name, description, YesNoMenu.CANCEL_LORE);
	}

	public static YesNoMenu create(String name, List<String> yesDescription, List<String> noDescription) {
		YesNoMenu menu = new YesNoMenu(name, yesDescription, noDescription);
		menu.setBackLink(ServerPlayer::openMenuRequestPrevMenu);
		menu.setYesCallback((serverPlayer, event) -> serverPlayer.setMenuRequestResult(true));
		menu.setNoCallback((serverPlayer, event) -> serverPlayer.setMenuRequestResult(false));
		return menu;
	}
}
