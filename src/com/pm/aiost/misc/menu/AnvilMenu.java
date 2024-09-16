package com.pm.aiost.misc.menu;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;
import static com.pm.aiost.misc.utils.ChatColor.RED;

import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R4.inventory.CraftInventoryAnvil;
import org.bukkit.craftbukkit.v1_20_R4.inventory.CraftInventoryView;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.item.Items;
import com.pm.aiost.misc.menu.inventoryMenu.InventoryMenu;
import com.pm.aiost.misc.nms.NMS;
import com.pm.aiost.misc.packet.PacketFactory;
import com.pm.aiost.misc.packet.PacketSender;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.player.ServerPlayer;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.ContainerLevelAccess;

public class AnvilMenu implements Menu, InventoryEventHandler, InventoryHolder {

	private static final ItemStack BACK_ITEM = MetaHelper.setMeta(new ItemStack(Material.BARRIER),
			RED + BOLD + "Cancel", Arrays.asList(GRAY + "Click to go back to previous menu"));

	private final String name;
	private ItemStack first;
	private ItemStack second;
	private Consumer<ServerPlayer> backLink;
	private BiConsumer<ServerPlayer, InventoryClickEvent> clickCallback;

	public AnvilMenu(String name) {
		this(name, new ItemStack(Material.PAPER), Items.AIR);
	}

	public AnvilMenu(String name, Material first) {
		this(name, new ItemStack(first), Items.AIR);
	}

	public AnvilMenu(String name, Material first, Material second) {
		this(name, new ItemStack(first), new ItemStack(second));
	}

	public AnvilMenu(String name, ItemStack first) {
		this(name, first, Items.AIR);
	}

	public AnvilMenu(String name, ItemStack first, ItemStack second) {
		this.name = name;
		this.first = first;
		this.second = second;
		backLink = DEFAULT_BACK_LINK;
	}

	@Override
	public void open(ServerPlayer serverPlayer) {
		open(serverPlayer.player);
	}

	@Override
	public void open(HumanEntity player) {
		open((Player) player);
	}

	@Override
	public void open(Player player) {
		net.minecraft.server.level.ServerPlayer p = ((CraftPlayer) player).getHandle();
		AnvilContainer container = new AnvilContainer(p.nextContainerCounter(), p.getInventory(),
				ContainerLevelAccess.create(p.level(), new BlockPos(0, 0, 0)));
		container.checkReachable = false;
		container.setTitle(Component.literal(name));

		Inventory inventory = container.getBukkitView().getTopInventory();
		inventory.setItem(0, first);
		inventory.setItem(1, second);

		PacketSender.send(player, PacketFactory.packetOpenWindow(container, container.getTitle()));
		p.containerMenu = container;
		p.initMenu(container);
	}

	@Override
	public void onInventoryOpen(InventoryOpenEvent event) {
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent event) {
		event.getInventory().clear();
	}

	@Override
	public final void onInventoryClick(ServerPlayer serverPlayer, InventoryClickEvent event) {
		playClickSound((Player) event.getWhoClicked());
		int slot = event.getSlot();
		if (slot == -999) {
			if (event.getCursor().getAmount() == 0) {
				openBackLink(serverPlayer);
				event.setCancelled(true);
			}
			return;
		}
		inventoryClickCallback(serverPlayer, event);
	}

	protected void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
		if (event.getSlot() == 2)
			clickCallback.accept(serverPlayer, event);
	}

	public void setClickCallback(BiConsumer<ServerPlayer, InventoryClickEvent> clickCallback) {
		this.clickCallback = clickCallback;
	}

	public BiConsumer<ServerPlayer, InventoryClickEvent> getClickCallback() {
		return clickCallback;
	}

	public void openBackLink(ServerPlayer serverPlayer) {
		backLink.accept(serverPlayer);
	}

	public void setBackLink(Consumer<ServerPlayer> backLink) {
		this.backLink = backLink;
	}

	public void setBackLink(InventoryMenu backLink) {
		this.backLink = backLink::open;
	}

	public void setBackLink(Inventory backLink) {
		this.backLink = (serverPlayer) -> serverPlayer.player.openInventory(backLink);
	}

	public Consumer<ServerPlayer> getBackLink() {
		return backLink;
	}

	@Override
	public Inventory getInventory() {
		return null;
	}

	public String getName() {
		return name;
	}

	public void set(ItemStack... items) {
		this.first = items[0];
		this.second = items[1];
	}

	public void set(Material... materials) {
		this.first = new ItemStack(materials[0]);
		this.second = new ItemStack(materials[1]);
	}

	public void setFirst(ItemStack first) {
		this.first = first;
	}

	public void setFirst(Material mat) {
		this.first = new ItemStack(mat);
	}

	public ItemStack getFirst() {
		return first;
	}

	public void setSecond(ItemStack second) {
		this.second = second;
	}

	public void setSecond(Material mat) {
		this.second = new ItemStack(mat);
	}

	public ItemStack getSecond() {
		return second;
	}

	public void setBackButton(ItemStack is) {
		second = is;
	}

	public void setBackButton() {
		second = BACK_ITEM;
	}

	private class AnvilContainer extends net.minecraft.world.inventory.AnvilMenu {

		public AnvilContainer(int i, net.minecraft.world.entity.player.Inventory playerinventory,
				ContainerLevelAccess containeraccess) {
			super(i, playerinventory, containeraccess);
		}

		public AnvilContainer(int i, net.minecraft.world.entity.player.Inventory playerinventory) {
			super(i, playerinventory);
		}

		@Override
		public CraftInventoryView getBukkitView() {
			return new AnvilInventoryView(super.getBukkitView());
		}
	}

	private class AnvilInventoryView extends CraftInventoryView {

		public AnvilInventoryView(CraftInventoryView view) {
			super(view.getPlayer(), view.getTopInventory(), view.getHandle());
		}

		@Override
		public Inventory getTopInventory() {
			try {
				return new AnvilInventory((CraftInventoryAnvil) super.getTopInventory());
			} catch (Throwable e) {
				return null;
			}
		}
	}

	private class AnvilInventory extends CraftInventoryAnvil {

		public AnvilInventory(CraftInventoryAnvil inventory) throws Throwable {
			super(inventory.getLocation(), inventory.getInventory(), inventory.getResultInventory(),
					(net.minecraft.world.inventory.AnvilMenu) NMS.CRAFTINVENTORYANVIL_GET_CONTAINER.invoke(inventory));
		}

		@Override
		public InventoryHolder getHolder() {
			return AnvilMenu.this;
		}
	}
}
