package com.pm.aiost.misc.menu.request.requests;

import java.util.function.Supplier;

import com.pm.aiost.misc.menu.Menu;
import com.pm.aiost.misc.menu.request.MenuRequest;
import com.pm.aiost.player.ServerPlayer;

public abstract class CallbackMenuRequest extends MenuRequest {

	protected Supplier<Menu> menuSupplier;
	protected Menu menu;
	protected boolean staysOpen;

	public CallbackMenuRequest(Supplier<Menu> menuSupplier) {
		this(menuSupplier, false);
	}

	public CallbackMenuRequest(Menu menu) {
		this(menu, false);
	}

	public CallbackMenuRequest(Supplier<Menu> menuSupplier, boolean staysOpen) {
		this.menuSupplier = menuSupplier;
		this.staysOpen = staysOpen;
	}

	public CallbackMenuRequest(Menu menu, boolean staysOpen) {
		this.menu = menu;
		this.staysOpen = staysOpen;
	}

	protected CallbackMenuRequest() {
	}

	@Override
	public void setResult(ServerPlayer serverPlayer, Object obj) {
		if (!staysOpen)
			serverPlayer.popMenuRequest();
		onResult(serverPlayer, obj);
	}

	protected abstract void onResult(ServerPlayer serverPlayer, Object obj);

	@Override
	public void openPrev(ServerPlayer serverPlayer) {
		doOpenRequest(serverPlayer);
	}

	@Override
	public void open(ServerPlayer serverPlayer) {
		getMenu().open(serverPlayer);
	}

	@Override
	public boolean hasPrevMenu() {
		return false;
	}

	@Override
	public boolean hasNextMenu() {
		return false;
	}

	@Override
	public Menu getMenu() {
		if (menu == null)
			menu = menuSupplier.get();
		return menu;
	}

	@Override
	public Menu getMenu(int index) {
		return getMenu();
	}
}