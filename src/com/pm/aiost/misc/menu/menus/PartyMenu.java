package com.pm.aiost.misc.menu.menus;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;
import static com.pm.aiost.misc.utils.ChatColor.GREEN;
import static com.pm.aiost.misc.utils.ChatColor.RED;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.google.common.io.ByteArrayDataInput;
import com.pm.aiost.item.Items;
import com.pm.aiost.misc.menu.anvilMenu.AnvilMenu;
import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.SingleInventoryMenu;
import com.pm.aiost.misc.other.PlayerHead;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.server.request.ServerRequest;

public class PartyMenu extends SingleInventoryMenu {

	private static final ItemStack INVITE_ITEM = MetaHelper.setMeta(Material.WRITABLE_BOOK,
			GREEN + "" + BOLD + "Invite to party", Arrays.asList(GRAY + "Click to invite someone to your party", "",
					GRAY + "Or use " + BOLD + "/party [player name] " + GRAY + "instead"));

	private static final ItemStack LEAVE_ITEM = MetaHelper.setMeta(Material.LAVA_BUCKET,
			RED + "" + BOLD + "Leave party", Arrays.asList(GRAY + "Click to leave current party", "",
					GRAY + "Or use " + BOLD + "/partyLeave " + GRAY + "instead"));

	private ServerPlayer serverPlayer;
	private List<UUID> uuids;

	public PartyMenu(ServerPlayer serverPlayer) {
		super(BOLD + "Party", 3, true);
		this.serverPlayer = serverPlayer;
		uuids = new ArrayList<UUID>();
		addBorderItems(new int[] { 9, 17 }, LEAVE_ITEM, INVITE_ITEM);
		setBackLink(SocialMenu.getMenu());
	}

	@Override
	public void onInventoryOpen(InventoryOpenEvent event) {
		super.onInventoryOpen(event);
		ServerRequest.getHandler().requestPartyData(serverPlayer);
	}

	@Override
	protected void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack is = event.getCurrentItem();
		if (is == null)
			return;

		switch (is.getType()) {
		case WRITABLE_BOOK:
			createPartyInviteMenu().open(serverPlayer);
			break;

		case LAVA_BUCKET:
			ServerRequest.getHandler().leaveParty(serverPlayer);
			clear();
			break;

		case PLAYER_HEAD:
			ServerRequest.getHandler().removeFromParty(serverPlayer, uuids.get(convertSlotToIndex(event.getSlot())));
			break;

		default:
			break;
		}
	}

	private AnvilMenu createPartyInviteMenu() {
		AnvilMenu menu = new AnvilMenu(BOLD + "Invite player", MetaHelper.setMeta(Material.PAPER, "name")) {
			@Override
			public void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
				event.setCancelled(true);
				if (event.getSlot() == 2) {
					ServerRequest.getHandler().inviteParty(serverPlayer,
							event.getCurrentItem().getItemMeta().getDisplayName());
					PartyMenu.this.open(serverPlayer);
				}
			}
		};
		menu.setBackLink(this);
		return menu;
	}

	public void clear() {
		Inventory inv = getInventory();
		setMember(inv, serverPlayer.player.getUniqueId(), 0);
		for (int i = 0; i < uuids.size(); i++)
			inv.setItem(11 + i, Items.AIR);
		uuids.clear();
	}

	public void setMembers(ServerPlayer owner, List<ServerPlayer> member) {
		Inventory inv = getInventory();
		setMember(inv, owner.player.getUniqueId(), 0);
		for (int i = 0; i < member.size(); i++)
			setMember(inv, member.get(i).player.getUniqueId(), i + 1);
		if (member.size() < uuids.size() - 1) {
			for (int i = uuids.size() - 1; i > member.size(); i--) {
				inv.setItem(10 + i, Items.AIR);
				uuids.remove(i);
			}
		}
	}

	public void setMembers(ByteArrayDataInput in) {
		Inventory inv = getInventory();
		setMember(inv, UUID.fromString(in.readUTF()), 0);
		int size = in.readInt();
		for (int i = 0; i < size; i++)
			setMember(inv, UUID.fromString(in.readUTF()), i + 1);
		if (size < uuids.size() - 1) {
			for (int i = uuids.size() - 1; i > size; i--) {
				inv.setItem(10 + i, Items.AIR);
				uuids.remove(i);
			}
		}
	}

	private void setMember(Inventory inv, UUID uuid, int i) {
		if (i < uuids.size()) {
			if (uuids.get(i).equals(uuid))
				return;
			else
				uuids.set(i, uuid);
		} else
			uuids.add(uuid);
		inv.setItem(10 + i, PlayerHead.create(uuid));
	}
}
