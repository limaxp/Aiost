package com.pm.aiost.misc.menu.request.requests;

import java.util.function.Consumer;
import java.util.function.Supplier;

import com.pm.aiost.misc.menu.Menu;
import com.pm.aiost.misc.menu.request.MenuRequest;
import com.pm.aiost.player.ServerPlayer;

public abstract class SingleMenuRequest extends MenuRequest {

	protected Supplier<Menu> menuSupplier;
	protected Menu menu;

	public SingleMenuRequest(Supplier<Menu> menu, boolean isSaved) {
		super(EMPTY_CONSUMER, EMPTY_CONSUMER, isSaved);
		this.menuSupplier = menu;
	}

	public SingleMenuRequest(Menu menu, boolean isSaved) {
		super(EMPTY_CONSUMER, EMPTY_CONSUMER, isSaved);
		this.menu = menu;
	}

	public SingleMenuRequest(Supplier<Menu> menu, Consumer<ServerPlayer> requestConsumer,
			Consumer<ServerPlayer> targetConsumer) {
		super(requestConsumer, targetConsumer, false);
		this.menuSupplier = menu;
	}

	public SingleMenuRequest(Menu menu, Consumer<ServerPlayer> requestConsumer, Consumer<ServerPlayer> targetConsumer) {
		super(requestConsumer, targetConsumer, false);
		this.menu = menu;
	}

	@Override
	public void setResult(ServerPlayer serverPlayer, Object obj) {
		if (!isSaved)
			serverPlayer.popMenuRequest();
		onResult(serverPlayer, obj);
		openTarget(serverPlayer);
	}

	protected abstract void onResult(ServerPlayer serverPlayer, Object obj);

//	@Override
//	public void setResult(ServerPlayer serverPlayer, Object obj) {
//		finish(serverPlayer);
//	}

	@Override
	public void openPrev(ServerPlayer serverPlayer) {
		cancel(serverPlayer);
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