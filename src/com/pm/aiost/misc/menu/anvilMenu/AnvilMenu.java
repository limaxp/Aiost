package com.pm.aiost.misc.menu.anvilMenu;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;
import static com.pm.aiost.misc.utils.ChatColor.RED;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.function.Consumer;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.item.Items;
import com.pm.aiost.misc.log.Logger;
import com.pm.aiost.misc.menu.InventoryEventHandler;
import com.pm.aiost.misc.menu.Menu;
import com.pm.aiost.misc.menu.inventoryMenu.InventoryMenu;
import com.pm.aiost.misc.packet.PacketFactory;
import com.pm.aiost.misc.packet.PacketSender;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.player.ServerPlayer;

import net.minecraft.server.v1_15_R1.BlockPosition;
import net.minecraft.server.v1_15_R1.ContainerAccess;
import net.minecraft.server.v1_15_R1.ContainerAnvil;
import net.minecraft.server.v1_15_R1.EntityPlayer;
import net.minecraft.server.v1_15_R1.InventorySubcontainer;

public abstract class AnvilMenu implements Menu, InventoryEventHandler, InventoryHolder {

	private static final ItemStack BACK_ITEM = MetaHelper.setMeta(new ItemStack(Material.BARRIER),
			RED + BOLD + "Cancel", Arrays.asList(GRAY + "Click to go back to previous menu"));

	private final String name;
	private ItemStack first;
	private ItemStack second;
	private Consumer<ServerPlayer> backLink;

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

	protected abstract void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event);

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
		EntityPlayer p = ((CraftPlayer) player).getHandle();
		AnvilContainer container = new AnvilContainer(p, this);
		Inventory inventory = container.getBukkitView().getTopInventory();
		inventory.setItem(0, first);
		inventory.setItem(1, second);

		PacketSender.send(player, PacketFactory.packetOpenWindow(container, name));
		p.activeContainer = container;
		p.activeContainer.addSlotListener(p);
	}

	@Override
	public void onInventoryOpen(InventoryOpenEvent event) {
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent event) {
		event.getInventory().clear();
	}

	@Override
	public void onInventoryClick(ServerPlayer serverPlayer, InventoryClickEvent event) {
		playClickSound((Player) event.getWhoClicked());
		int slot = event.getSlot();
		if (slot == -999) {
			if (event.getCursor().getAmount() == 0) {
				openBackLink(serverPlayer);
				event.setCancelled(true);
			}
			return;
		}
		ItemStack is = event.getCurrentItem();
		if (is.getAmount() == 0 || (slot == 1 && is.getType() == Material.BARRIER)) {
			openBackLink(serverPlayer);
			event.setCancelled(true);
			return;
		}

		inventoryClickCallback(serverPlayer, event);
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

	public static class AnvilContainer extends ContainerAnvil {

		private static Field repairInventoryField;
		private static Field bukkitOwnerField;

		static {
			try {
				repairInventoryField = ContainerAnvil.class.getDeclaredField("repairInventory");
				repairInventoryField.setAccessible(true);
				bukkitOwnerField = InventorySubcontainer.class.getDeclaredField("bukkitOwner");
				bukkitOwnerField.setAccessible(true);
			} catch (NoSuchFieldException | SecurityException e) {
				Logger.err("AnvilContainer: Error on reflection of packet fields!", e);
			}
		}

		public AnvilContainer(Player player) {
			this(((CraftPlayer) player).getHandle());
		}

		public AnvilContainer(Player player, InventoryHolder owner) {
			this(((CraftPlayer) player).getHandle());
			setInventoryOwner(owner);
		}

		public AnvilContainer(EntityPlayer player, InventoryHolder owner) {
			this(player);
			setInventoryOwner(owner);
		}

		public AnvilContainer(EntityPlayer player) {
			super(player.nextContainerCounter(), player.inventory,
					ContainerAccess.at(player.getWorld(), new BlockPosition(0, 0, 0)));
			this.checkReachable = false;
		}

		public void setInventoryOwner(InventoryHolder owner) {
			try {
				bukkitOwnerField.set(repairInventoryField.get(this), owner);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				Logger.err("AnvilContainer: Error on setting bukkit owner field!", e);
			}
		}

		@Override
		public void e() {
			super.e();
			this.levelCost.set(0);
		}
	}
}
