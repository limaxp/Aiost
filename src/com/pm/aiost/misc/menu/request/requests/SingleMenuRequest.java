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
		this(menu, EMPTY_CONSUMER, EMPTY_CONSUMER, isSaved);
	}

	public SingleMenuRequest(Menu menu, boolean isSaved) {
		this(menu, EMPTY_CONSUMER, EMPTY_CONSUMER, isSaved);
	}

	public SingleMenuRequest(Supplier<Menu> menu, Consumer<ServerPlayer> consumer, boolean isSaved) {
		this(menu, consumer, consumer, isSaved);
	}

	public SingleMenuRequest(Menu menu, Consumer<ServerPlayer> consumer, boolean isSaved) {
		this(menu, consumer, consumer, isSaved);
	}

	public SingleMenuRequest(Supplier<Menu> menu, Consumer<ServerPlayer> requestConsumer,
			Consumer<ServerPlayer> targetConsumer, boolean isSaved) {
		super(requestConsumer, targetConsumer, isSaved);
		this.menuSupplier = menu;
	}

	public SingleMenuRequest(Menu menu, Consumer<ServerPlayer> requestConsumer, Consumer<ServerPlayer> targetConsumer,
			boolean isSaved) {
		super(requestConsumer, targetConsumer, isSaved);
		this.menu = menu;
	}

	@Override
	public void setResult(ServerPlayer serverPlayer, Object obj) {
		finish(serverPlayer);
		onResult(serverPlayer, obj);
	}

	protected abstract void onResult(ServerPlayer serverPlayer, Object obj);

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