package com.pm.aiost.player;

import static com.pm.aiost.misc.utils.ChatColor.YELLOW;

import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Deque;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.Int2ShortMap;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.Int2ShortOpenHashMap;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.IntArrayList;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.IntList;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.IntSet;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.objects.Object2IntMap;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;

import com.pm.aiost.collection.list.IdentityArrayList;
import com.pm.aiost.collection.list.UnorderedIdentityArrayList;
import com.pm.aiost.entity.AiostEntityTypes;
import com.pm.aiost.entity.ownable.OwnableEntity;
import com.pm.aiost.event.EquipmentListener;
import com.pm.aiost.event.effect.Effect;
import com.pm.aiost.event.effect.EffectHandler;
import com.pm.aiost.event.effect.collection.EffectData;
import com.pm.aiost.event.effect.effects.ChatHologramEffect;
import com.pm.aiost.event.eventHandler.EventHandler;
import com.pm.aiost.event.eventHandler.EventHandler.QuitReason;
import com.pm.aiost.event.eventHandler.handler.CancelEventHandler;
import com.pm.aiost.game.GamePlayer;
import com.pm.aiost.game.GameType;
import com.pm.aiost.item.spell.Spell;
import com.pm.aiost.misc.dataAccess.DataAccess;
import com.pm.aiost.misc.log.Logger;
import com.pm.aiost.misc.menu.Menu;
import com.pm.aiost.misc.menu.request.MenuRequest;
import com.pm.aiost.misc.packet.PacketFactory;
import com.pm.aiost.misc.packet.PacketSender;
import com.pm.aiost.misc.packet.disguise.Disguise;
import com.pm.aiost.misc.packet.entity.entities.ChatHologram;
import com.pm.aiost.misc.packet.entity.entities.EntityHologram;
import com.pm.aiost.misc.packet.object.objects.Hologram;
import com.pm.aiost.misc.particleEffect.particle.IParticle;
import com.pm.aiost.misc.rank.Rank;
import com.pm.aiost.misc.utils.nms.NMS;
import com.pm.aiost.player.handler.ItemBarHandler;
import com.pm.aiost.player.handler.VisibilityManager;
import com.pm.aiost.player.settings.PlayerSettings;
import com.pm.aiost.player.unlockable.UnlockableType;
import com.pm.aiost.player.unlockable.UnlockableTypes;
import com.pm.aiost.server.world.ServerWorld;
import com.pm.aiost.server.world.region.IRegion;

import net.md_5.bungee.api.chat.BaseComponent;
import net.minecraft.server.v1_15_R1.ChatMessageType;
import net.minecraft.server.v1_15_R1.EntityPlayer;
import net.minecraft.server.v1_15_R1.EntityTypes;

public class ServerPlayer implements AutoCloseable {

	public static final float BASE_MANA_REGEN = 0.1F;
	public static final int MAX_THIRST = 20;
	public static final int BASE_TEMPERATURE = 20;
	public static final int MAX_TEMPERATURE = 100;
	public static final int MIN_TEMPERATURE = -40;

	public final Player player;
	public final String name;
	private ServerWorld serverWorld;
	private IRegion region;
	private GamePlayer gamePlayer;
	private Party localParty;
	public boolean ignoreNextLeftClickInteract;
	public int lastInteractedTick;
	public @Nullable ItemStack lastRightClickedIS;
	public @Nullable EquipmentSlot lastRightClickedEquipmentSlot;
	public @Nullable BlockFace lastInteractedBlockface;
	public boolean onGround;
	int bungeeID;
	long databaseID;
	private Rank rank;
	private int level;
	private int experience;
	private int score;
	private int credits;
	private double mana;
	private double maxMana;
	private float manaRegeneration;
	private float thirst;
	private float temperature;
	private final IntSet permissions;
	private IntList newPermissions;
	private final Int2ShortMap settings;
	private IntList changedSettings;
	private EffectData effectData;
	private EventHandler eventHandler;
	private Map<Object, Menu> menus;
	private Object2ObjectLinkedOpenHashMap<Object, MenuRequest> storedMenuRequests;
	private Deque<MenuRequest> menuRequestQueue;
	private @Nullable ItemStack itemBarItem;
	private int itemBarSlot;
	private boolean itemBarSuffixAdded;
	private @Nullable Disguise disguise;
	private @Nullable Disguise defaultDisguise;
	private final List<ChatHologram> chatHolograms;
	private final Object2IntMap<Object> cooldowns;
	final List<IParticle> particles;
	private OwnableEntity petEntity;
	private PlayerDataCache dataCache;

	ServerPlayer(Player player) {
		this.player = player;
		this.name = player.getName();
		gamePlayer = GamePlayer.NULL_GAME_PLAYER;
		manaRegeneration = BASE_MANA_REGEN;
		temperature = BASE_TEMPERATURE;
		permissions = new IntOpenHashSet();
		settings = new Int2ShortOpenHashMap();
		effectData = new EffectData();
		eventHandler = CancelEventHandler.INSTANCE;
		menus = new IdentityHashMap<Object, Menu>();
		menuRequestQueue = new ArrayDeque<MenuRequest>(10);
		chatHolograms = new IdentityArrayList<ChatHologram>();
		cooldowns = new Object2IntOpenHashMap<Object>();
		particles = new UnorderedIdentityArrayList<IParticle>();
		dataCache = new PlayerDataCache(this);
	}

	@Override
	public void close() {
		effectData = null;
		menus = null;
		gamePlayer = GamePlayer.NULL_GAME_PLAYER;
		if (!chatHolograms.isEmpty()) {
			for (EntityHologram chatHologram : chatHolograms)
				chatHologram.remove();
		}
		if (petEntity != null)
			despawnPet();
	}

	public void update() {
		EffectHandler.tickRunEffects(this);
		if (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE)
			regenMana();
	}

	public void resetStats() {
		resetMinecraftStats();
		resetCustomStats();
	}

	@SuppressWarnings("deprecation")
	public void resetMinecraftStats() {
		player.setHealth(player.getMaxHealth());
		player.setFoodLevel(20);
		player.setSaturation(20);
		player.setFireTicks(0);
		for (PotionEffect effect : player.getActivePotionEffects())
			player.removePotionEffect(effect.getType());
	}

	public void resetCustomStats() {
		mana = maxMana;
		thirst = MAX_THIRST;
		temperature = BASE_TEMPERATURE;
	}

	public void addItem(ItemStack... items) {
		for (ItemStack item : items)
			addItem(item);
	}

	public void addItem(ItemStack item) {
		PlayerInventory playerInv = player.getInventory();
		int amount = item.getAmount();
		for (ItemStack content : playerInv.getContents()) {
			if (content != null && content.isSimilar(item)) {
				int diffAmount = content.getMaxStackSize() - content.getAmount();
				if (diffAmount != 0) {
					if (amount > diffAmount) {
						content.setAmount(content.getAmount() + diffAmount);
						amount -= diffAmount;
					} else {
						content.setAmount(content.getAmount() + amount);
						return;
					}
				}
			}
		}

		int i = 0;
		for (ItemStack content : playerInv.getContents()) {
			if (content == null) {
				ItemStack is = item.clone();
				is.setAmount(amount);
				if (i == playerInv.getHeldItemSlot()) {
					if (!EquipmentListener.setHandItemCheck(this, EquipmentSlot.HAND, is))
						continue;
				}
				playerInv.setItem(i, is);
				return;
			}
			i++;
		}

		ItemStack is = item.clone();
		is.setAmount(amount);
		player.getWorld().dropItem(player.getLocation(), is);
	}

	public void setItem(EquipmentSlot slot, ItemStack item) {
		if (EquipmentListener.setItemCheck(this, slot, item))
			player.getInventory().setItem(slot, item);
	}

	public ItemStack getItem(EquipmentSlot slot) {
		return player.getInventory().getItem(slot);
	}

	public void setItem(int slot, ItemStack item) {
		if (EquipmentListener.setItemCheck(this, slot, item))
			player.getInventory().setItem(slot, item);
	}

	public ItemStack getItem(int slot) {
		return player.getInventory().getItem(slot);
	}

	public void setItemInMainHand(ItemStack item) {
		if (EquipmentListener.setHandItemCheck(this, EquipmentSlot.HAND, item))
			player.getInventory().setItemInMainHand(item);
	}

	public ItemStack getItemInMainHand() {
		return player.getInventory().getItemInMainHand();
	}

	public void setItemInOffHand(ItemStack item) {
		if (EquipmentListener.setHandItemCheck(this, EquipmentSlot.OFF_HAND, item))
			player.getInventory().setItemInOffHand(item);
	}

	public ItemStack getItemInOffHand() {
		return player.getInventory().getItemInOffHand();
	}

	public void setHelmet(ItemStack item) {
		if (EquipmentListener.setArmorItemCheck(this, EquipmentSlot.HEAD, item))
			player.getInventory().setHelmet(item);
	}

	public ItemStack getHelmet() {
		return player.getInventory().getHelmet();
	}

	public void setChestplate(ItemStack item) {
		if (EquipmentListener.setArmorItemCheck(this, EquipmentSlot.CHEST, item))
			player.getInventory().setChestplate(item);
	}

	public ItemStack getChestplate() {
		return player.getInventory().getChestplate();
	}

	public void setLeggings(ItemStack item) {
		if (EquipmentListener.setArmorItemCheck(this, EquipmentSlot.LEGS, item))
			player.getInventory().setLeggings(item);
	}

	public ItemStack getLeggings() {
		return player.getInventory().getLeggings();
	}

	public void setBoots(ItemStack item) {
		if (EquipmentListener.setArmorItemCheck(this, EquipmentSlot.FEET, item))
			player.getInventory().setBoots(item);
	}

	public ItemStack getBoots() {
		return player.getInventory().getBoots();
	}

	public void sendMessage(String msg) {
		player.sendMessage(msg);
	}

	public void sendMessage(String... msg) {
		player.sendMessage(msg);
	}

	public void sendMessage(BaseComponent component) {
		player.spigot().sendMessage(component);
	}

	public void sendMessage(BaseComponent... components) {
		player.spigot().sendMessage(components);
	}

	public void sendMessage(net.md_5.bungee.api.ChatMessageType type, BaseComponent component) {
		player.spigot().sendMessage(type, component);
	}

	public void sendMessage(net.md_5.bungee.api.ChatMessageType type, BaseComponent... components) {
		player.spigot().sendMessage(type, components);
	}

	public void sendTitle(String title) {
		player.sendTitle(title, null, 10, 70, 20);
	}

	public void sendTitle(String title, int fadeIn, int stay, int fadeOut) {
		player.sendTitle(title, null, fadeIn, stay, fadeOut);
	}

	public void sendTitle(String title, String subtitle) {
		player.sendTitle(title, subtitle, 10, 70, 20);
	}

	public void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
		player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
	}

	public void sendSubTitle(String subtitle) {
		player.sendTitle("", subtitle, 10, 70, 20);
	}

	public void sendSubTitle(String subtitle, int fadeIn, int stay, int fadeOut) {
		player.sendTitle("", subtitle, fadeIn, stay, fadeOut);
	}

	public void sendActionBar(String msg) {
		sendActionBar(player, msg);
	}

	public void sendItemBar(String msg) {
		ItemBarHandler.send(this, msg);
	}

	public void clearItemBar() {
		ItemBarHandler.clear(this);
	}

	public void clearItemBarSilent() {
		ItemBarHandler.clearSilent(this);
	}

	public void clearItemBarCreative(InventoryClickEvent event) {
		ItemBarHandler.clearCreative(this, event);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setDisguise(Disguise disguise) {
		List packets = new ArrayList();
		packets.add(PacketFactory.packetEntityDestroy(player.getEntityId()));
		if (this.disguise != null)
			this.disguise.removePackets(player, packets);
		disguise.addPackets(player, packets);
		PacketSender.sendNMS_(NMS.getTrackedPlayers(player), packets);
		setSelfDisguise();
		this.disguise = disguise;
	}

	public void setDefaultDisguise(Disguise disguise) {
		if (this.disguise == null || usesDefaultDisguise())
			setDisguise(disguise);
		defaultDisguise = disguise;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void removeDisguise() {
		if (this.disguise == null)
			return;

		List packets = new ArrayList();
		EntityPlayer entityPlayer = NMS.getNMS(player);
		packets.add(PacketFactory.packetEntityDestroy(entityPlayer.getId()));
		disguise.removePackets(player, packets);
		if (defaultDisguise != null)
			defaultDisguise.addPackets(player, packets);
		else {
			packets.add(PacketFactory.packetNamedEntitySpawn(entityPlayer));
			Disguise.addPlayerStatePackets(entityPlayer, packets);
		}
		PacketSender.sendNMS_(NMS.getTrackedPlayers(player), packets);
		removeSelfDisguise();
		this.disguise = null;
	}

	public void removeDefaultDisguise() {
		if (defaultDisguise == null)
			return;

		if (usesDefaultDisguise()) {
			defaultDisguise = null;
			removeDisguise();
		} else
			defaultDisguise = null;
	}

	private void setSelfDisguise() {
//		EntityTrackerHelper.getTrackedPlayers(player).add(NMS.getNMS(player));
	}

	private void removeSelfDisguise() {
//		EntityTrackerHelper.getTrackedPlayers(player).remove(NMS.getNMS(player));
	}

	public @Nullable Disguise getDisguise() {
		return disguise;
	}

	public boolean hasDisguise() {
		return disguise != null;
	}

	public boolean usesDefaultDisguise() {
		return disguise == defaultDisguise;
	}

	public void addChatHologram(ChatHologram hologram) {
		if (chatHolograms.isEmpty())
			addEffect(ChatHologramEffect.INSTANCE);
		else {
			double yAdd = hologram.lineSize() * Hologram.ABS;
			for (ChatHologram chatHolo : chatHolograms)
				chatHolo.teleport(chatHolo.x, chatHolo.y + yAdd, chatHolo.z, chatHolo.yaw, chatHolo.pitch, false);
		}
		chatHolograms.add(0, hologram);
	}

	public void removeChatHologram() {
		int newSize = chatHolograms.size() - 1;
		chatHolograms.remove(newSize).remove();
		if (newSize < 1)
			removeEffect(ChatHologramEffect.INSTANCE);
	}

	public List<ChatHologram> getChatHolograms() {
		return chatHolograms;
	}

	public void addParticle(IParticle particleEffect) {
		particles.add(particleEffect);
	}

	public void removeParticle(IParticle particleEffect) {
		particles.remove(particleEffect);
	}

	public void clearParticles() {
		particles.clear();
	}

	public boolean hasParticles() {
		return particles.size() > 0;
	}

	public void spawnParticles() {
		Location loc = player.getLocation();
		for (int i = 0; i < particles.size(); i++)
			particles.get(i).spawn(loc);
	}

	public void addPermission(int id) {
		if (!permissions.contains(id)) {
			permissions.add(id);
			if (newPermissions == null)
				newPermissions = new IntArrayList();
			newPermissions.add(id);
		}
	}

	public boolean hasPermission(int id) {
		return permissions.contains(id);
	}

	public IntSet getPermissions() {
		return permissions;
	}

	public boolean hasNewPermissions() {
		return newPermissions != null;
	}

	public IntList getNewPermissions() {
		return newPermissions;
	}

	public void setSetting(int id, short value) {
		if (settings.containsKey(id))
			settings.replace(id, value);
		else
			settings.put(id, value);

		if (changedSettings == null)
			changedSettings = new IntArrayList();
		if (!changedSettings.contains(id))
			changedSettings.add(id);
		PlayerSettings.getCallback(id).accept(this, value);
	}

	public short getSetting(int id) {
		return (byte) settings.getOrDefault(id, PlayerSettings.getDefaultValue(id));
	}

	public Int2ShortMap getSettings() {
		return settings;
	}

	public boolean hasChangedSettings() {
		return changedSettings != null;
	}

	public IntList getChangedSettings() {
		return changedSettings;
	}

	public boolean setEventHandler(EventHandler eventHandler) {
		return setEventHandler(eventHandler, QuitReason.CHANGE_HANDLER);
	}

	public boolean setEventHandler(EventHandler eventHandler, QuitReason reason) {
		if (reason == QuitReason.CHANGE_HANDLER && !this.eventHandler.allowsChange(eventHandler))
			return false;
		this.eventHandler.onPlayerQuit(this, reason);
		this.eventHandler = eventHandler;
		eventHandler.onPlayerJoin(this);
		return true;
	}

	public boolean setEventHandlerSilent(EventHandler eventHandler) {
		if (this.eventHandler.allowsChange(eventHandler)) {
			this.eventHandler = eventHandler;
			return true;
		}
		return false;
	}

	public void removeEventHandler() {
		removeEventHandler(QuitReason.CHANGE_HANDLER);
	}

	public void removeEventHandler(QuitReason reason) {
		eventHandler.onPlayerQuit(this, reason);
		this.eventHandler = null;
	}

	public void removeEventHandler(EventHandler eventHandler, QuitReason reason) {
		if (this.eventHandler == eventHandler)
			removeEventHandler(reason);
	}

	public void resetEventHandler() {
		eventHandler = region.getEventHandler();
		eventHandler.onPlayerJoin(this);
	}

	public void resetEventHandler(QuitReason reason) {
		eventHandler.onPlayerQuit(this, reason);
		eventHandler = region.getEventHandler();
		eventHandler.onPlayerJoin(this);
	}

	public EventHandler getEventHandler() {
		return eventHandler;
	}

	public void openEventHandlerMenu() {
		eventHandler.getMenu().open(player);
	}

	public void addEffect(Effect... effects) {
		effectData.add(effects);
	}

	public void addEffect(Effect effect) {
		effectData.add(effect);
	}

	public boolean addEffect(int time, Effect... effects) {
		return effectData.add(time, effects);
	}

	public boolean addEffect(int time, Effect effect) {
		return effectData.add(time, effect);
	}

	public void removeEffect(Effect... effects) {
		effectData.remove(effects);
	}

	public void removeEffect(Effect effect) {
		effectData.remove(effect);
	}

	public EffectData getEffectData() {
		return effectData;
	}

	public void setVisible() {
		VisibilityManager.setVisible(this);
	}

	public void setInvisible() {
		VisibilityManager.setInvisible(this);
	}

	public void showInvisibles() {
		VisibilityManager.showInvisibles(this);
	}

	public void hideInvisibles() {
		VisibilityManager.hideInvisibles(this);
	}

	public void addMenu(Object identifier, Menu menu) {
		menus.put(identifier, menu);
	}

	public Menu removeMenu(Object identifier) {
		return menus.remove(identifier);
	}

	public Menu replaceMenu(Object identifier, Menu menu) {
		return menus.replace(identifier, menu);
	}

	public Menu getMenu(Object identifier) {
		return menus.get(identifier);
	}

	public Collection<Menu> getMenus() {
		return menus.values();
	}

	public Menu getOrCreateMenu(Object identifier, Supplier<Menu> supplier) {
		Menu menu = menus.get(identifier);
		if (menu == null)
			menus.put(identifier, menu = supplier.get());
		return menu;
	}

	public Menu getOrCreateMenu(Object identifier, Function<ServerPlayer, Menu> supplier) {
		Menu menu = menus.get(identifier);
		if (menu == null)
			menus.put(identifier, menu = supplier.apply(this));
		return menu;
	}

	public <T> Menu getOrCreateMenu(Object identifier, Function<T, Menu> supplier, T t) {
		Menu menu = menus.get(identifier);
		if (menu == null)
			menus.put(identifier, menu = supplier.apply(t));
		return menu;
	}

	public void doMenuRequest(@Nonnull Object identifier, @Nonnull MenuRequest menuRequest) {
		doMenuRequest(identifier, () -> menuRequest);
	}

	public void doMenuRequest(@Nonnull Object identifier, @Nonnull Supplier<MenuRequest> supplier) {
		MenuRequest request = getStoredMenuRequest(identifier);
		if (request == null) {
			request = supplier.get();
			storeMenuRequest(identifier, request);
		}
		doMenuRequest(request);
	}

	public void doMenuRequest(@Nonnull Supplier<MenuRequest> supplier) {
		doMenuRequest(supplier.get());
	}

	public void doMenuRequest(@Nonnull MenuRequest menuRequest) {
		if (menuRequestQueue.size() >= 10)
			menuRequestQueue.pollLast();
		menuRequestQueue.offerFirst(menuRequest);
		menuRequest.open(this);
	}

	private MenuRequest getStoredMenuRequest(@Nonnull Object identifier) {
		if (storedMenuRequests == null)
			storedMenuRequests = new Object2ObjectLinkedOpenHashMap<Object, MenuRequest>(10);
		return storedMenuRequests.get(identifier);
	}

	private void storeMenuRequest(@Nonnull Object identifier, @Nonnull MenuRequest menuRequest) {
		if (storedMenuRequests.size() >= 10)
			storedMenuRequests.removeLast();
		storedMenuRequests.putAndMoveToFirst(identifier, menuRequest);
	}

	public MenuRequest popMenuRequest() {
		return menuRequestQueue.pollFirst();
	}

	public void openMenuRequest() {
		menuRequestQueue.peekFirst().open(this);
	}

	public void openMenuRequestPrevMenu() {
		menuRequestQueue.peekFirst().openPrev(this);
	}

	public void openMenuRequestRequestMenu() {
		menuRequestQueue.peekFirst().doOpenRequest(this);
	}

	public void openMenuRequestTargetMenu() {
		menuRequestQueue.peekFirst().doOpenTarget(this);
	}

	public void setMenuRequestResult(Object obj) {
		menuRequestQueue.peekFirst().setResult(this, obj);
	}

	public boolean hasMenuRequest() {
		return menuRequestQueue.peekFirst() != null;
	}

	public void setCooldown(Object obj, int timeInTicks) {
		int answer = cooldowns.replace(obj, NMS.getMinecraftServerTick() + timeInTicks);
		if (answer == 0)
			cooldowns.put(obj, NMS.getMinecraftServerTick() + timeInTicks);
	}

	public int removeCooldown(Object obj) {
		return cooldowns.removeInt(obj);
	}

	public boolean hasCooldown(Object obj) {
		return cooldowns.getOrDefault(obj, NMS.getMinecraftServerTick()) > NMS.getMinecraftServerTick();
	}

	public boolean checkCooldown(Object obj) {
		int currentCooldown = cooldowns.getOrDefault(obj, NMS.getMinecraftServerTick()) - NMS.getMinecraftServerTick();
		if (currentCooldown > 0) {
			sendActionBar(YELLOW + "Cooldown: " + ((float) currentCooldown / 20) + " sec");
			return true;
		}
		return false;
	}

	public UUID getUniqueId() {
		return player.getUniqueId();
	}

	public int getBungeeID() {
		return bungeeID;
	}

	public long getDatabaseID() {
		return databaseID;
	}

	public void setRank(Rank rank) {
		this.rank = rank;
		try {
			DataAccess.getAccess().setRank(databaseID, rank);
		} catch (SQLException e) {
			Logger.err("ServerPlayer: Error! Could not update rank for player '" + name + "'", e);
		}
	}

	public Rank getRank() {
		return rank;
	}

	public boolean isAdmin() {
		return rank.isAdmin();
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getLevel() {
		return level;
	}

	public void setExperience(int experience) {
		this.experience = experience;
	}

	public int getExperience() {
		return experience;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getScore() {
		return score;
	}

	public int addCredits(int credits) {
		return this.credits += credits;
	}

	public int removeCredits(int credits) {
		return this.credits -= credits;
	}

	public boolean hasCredits(int credits) {
		return this.credits >= credits;
	}

	public void setCredits(int credits) {
		this.credits = credits;
	}

	public int getCredits() {
		return credits;
	}

	public boolean addUnlockable(UnlockableType<?> type, short id) {
		return addUnlockable(type.id, id);
	}

	public boolean addUnlockable(GameType<?> type, short id) {
		return addUnlockable(type.getId(), id);
	}

	public boolean addUnlockable(int type, short id) {
		return dataCache.addUnlockable(type, id);
	}

	public boolean buyUnlockable(UnlockableType<?> type, short id, int price) {
		return buyUnlockable(type.id, id, price);
	}

	public boolean buyUnlockable(GameType<?> type, short id, int price) {
		return buyUnlockable(type.getId(), id, price);
	}

	public boolean buyUnlockable(int type, short id, int price) {
		return dataCache.buyUnlockable(type, id, price);
	}

	public boolean hasUnlockable(UnlockableType<?> type, short id) {
		return hasUnlockable(type.id, id);
	}

	public boolean hasUnlockable(GameType<?> type, short id) {
		return hasUnlockable(type.getId(), id);
	}

	public boolean hasUnlockable(int type, short id) {
		return dataCache.hasUnlockable(type, id);
	}

	public BitSet getUnlockables(UnlockableType<?> type) {
		return getUnlockables(type.id);
	}

	public BitSet getUnlockables(GameType<?> type) {
		return getUnlockables(type.getId());
	}

	public BitSet getUnlockables(int type) {
		return dataCache.getUnlockables(type);
	}

	public void joinLocalParty(Party party) {
		leaveLocalParty();
		party.addMember(this);
		this.localParty = party;
	}

	public void leaveLocalParty() {
		if (this.localParty != null) {
			localParty.removeMember(this);
			localParty = null;
		}
	}

	public Party getLocalParty() {
		if (localParty == null)
			localParty = new Party(this);
		return localParty;
	}

	public boolean hasLocalParty() {
		return localParty != null;
	}

	public void setMana(double mana) {
		if (mana < 0)
			this.mana = 0;
		else if (mana > maxMana)
			this.mana = maxMana;
		else
			this.mana = mana;
	}

	public void addMana(double mana) {
		double calculatedMana = this.mana + mana;
		this.mana = calculatedMana > maxMana ? maxMana : calculatedMana;
	}

	public void regenMana() {
		if (this.mana < maxMana) {
			double manaPerSymbol = maxMana / 10;
			int prevAmount = (int) Math.round(this.mana / manaPerSymbol);
			addMana(manaRegeneration);
			int newAmount = (int) Math.round(this.mana / manaPerSymbol);
			if (prevAmount != newAmount)
				Spell.showMana(this, newAmount);
		}
	}

	public void removeMana(double mana) {
		double calculatedMana = this.mana - mana;
		this.mana = calculatedMana < 0 ? 0 : calculatedMana;
	}

	public boolean hasMana(double mana) {
		return this.mana >= mana;
	}

	public double getMana() {
		return mana;
	}

	@SuppressWarnings("deprecation")
	public void addHealth(double health) {
		double calculatedHealth = player.getHealth() + health;
		player.setHealth(calculatedHealth > player.getMaxHealth() ? player.getMaxHealth() : calculatedHealth);
	}

	public void setMaxMana(double maxMana) {
		this.maxMana = maxMana;
	}

	public void addMaxMana(double maxMana) {
		this.maxMana += maxMana;
	}

	public void removeMaxMana(double maxMana) {
		this.maxMana -= maxMana;
	}

	public double getMaxMana() {
		return maxMana;
	}

	public void setManaRegeneration(float manaRegeneration) {
		this.manaRegeneration = manaRegeneration;
	}

	public void addManaRegeneration(float manaRegeneration) {
		this.manaRegeneration += manaRegeneration;
	}

	public void removeManaRegeneration(float manaRegeneration) {
		this.manaRegeneration -= manaRegeneration;
	}

	public float getManaRegeneration() {
		return manaRegeneration;
	}

	public void setThirst(float thirst) {
		if (thirst < 0)
			this.thirst = 0;
		else if (thirst > MAX_THIRST)
			this.thirst = MAX_THIRST;
		else
			this.thirst = thirst;
	}

	public void addThirst(float thirst) {
		float calculatedThirst = this.thirst + thirst;
		this.thirst = calculatedThirst > MAX_THIRST ? MAX_THIRST : calculatedThirst;
	}

	public void removeThirst(float thirst) {
		float calculatedThirst = this.thirst - thirst;
		this.thirst = calculatedThirst < 0 ? 0 : calculatedThirst;
	}

	public float getThirst() {
		return thirst;
	}

	public void setTemperature(float temperature) {
		if (temperature < 0)
			this.temperature = 0;
		else if (temperature > MAX_TEMPERATURE)
			this.temperature = MAX_TEMPERATURE;
		else
			this.temperature = temperature;
	}

	public float changeTemperature(float target) {
		if (temperature > target)
			return this.temperature -= (temperature - target) * 0.01;
		else if (temperature < target)
			return this.temperature += (target - temperature) * 0.01;
		return target;
	}

	public float getTemperature() {
		return temperature;
	}

	public void spawnPet(int id) {
		petEntity = (OwnableEntity) AiostEntityTypes.spawnEntity((EntityTypes<?>) UnlockableTypes.PETS.getObject(id),
				player.getLocation());
		if (petEntity != null)
			petEntity.setOwner(player);
	}

	public void despawnPet() {
		petEntity.die();
		petEntity = null;
	}

	public boolean hasPet() {
		return petEntity != null;
	}

	public boolean hidesChat() {
		return getSetting(PlayerSettings.HIDE_CHAT) == 1;
	}

	public boolean showsChat() {
		return getSetting(PlayerSettings.HIDE_CHAT) == 0;
	}

	void initServerWorld() {
		serverWorld = ServerWorld.getByWorld(player.getWorld());
		this.region = serverWorld.getRegion(player.getLocation());
		region.registerPlayer(this);
	}

	void setServerWorld(ServerWorld serverWorld) {
		this.serverWorld = serverWorld;
	}

	public final ServerWorld getServerWorld() {
		return serverWorld;
	}

	void setRegion(IRegion region) {
		this.region.unregisterPlayer(this);
		this.region = region;
		region.registerPlayer(this);
	}

	public IRegion getRegion() {
		return region;
	}

	public final void setGameData(GamePlayer gamePlayer) {
		this.gamePlayer = gamePlayer;
	}

	public final GamePlayer getGameData() {
		return gamePlayer;
	}

	public final boolean playsGame() {
		return gamePlayer != GamePlayer.NULL_GAME_PLAYER;
	}

	public void setItemBarItem(ItemStack item, int slot) {
		this.itemBarItem = item;
		this.itemBarSlot = slot;
	}

	public ItemStack getItemBarItem() {
		return itemBarItem;
	}

	public int getItemBarSlot() {
		return itemBarSlot;
	}

	public boolean isItemBarSuffixAdded() {
		return itemBarSuffixAdded;
	}

	public void setItemBarSuffixAdded(boolean itemBarSuffixAdded) {
		this.itemBarSuffixAdded = itemBarSuffixAdded;
	}

	public void openInventory(Inventory inventory) {
		player.openInventory(inventory);
	}

	public void openInventory(InventoryView inventoryView) {
		player.openInventory(inventoryView);
	}

	public void openInventory(Menu menu) {
		menu.open(this);
	}

	public void openMenu(Menu menu) {
		menu.open(this);
	}

	public void closeInventory() {
		player.closeInventory();
	}

	@SuppressWarnings("deprecation")
	public boolean collidesWithEntities() {
		return player.spigot().getCollidesWithEntities();
	}

	@SuppressWarnings("deprecation")
	public void setCollidesWithEntities(boolean collides) {
		player.spigot().setCollidesWithEntities(collides);
	}

	public Set<Player> getHiddenPlayers() {
		return player.spigot().getHiddenPlayers();
	}

	public static ServerPlayer getByPlayer(Player player) {
		return PlayerManager.getPlayer(player);
	}

	public static ServerPlayer getByBungeeID(int bungeeID) {
		return PlayerManager.getPlayer(bungeeID);
	}

	public static ServerPlayer getByUUID(UUID uuid) {
		return PlayerManager.getPlayer(uuid);
	}

	public static ServerPlayer getByName(String name) {
		return PlayerManager.getPlayer(name);
	}

	public static List<ServerPlayer> getOnlinePlayer() {
		return PlayerManager.getOnlinePlayer();
	}

	public static void sendActionBar(Player player, String msg) {
		PacketSender.send(player, PacketFactory.packetChat(msg, ChatMessageType.GAME_INFO));
	}
}
