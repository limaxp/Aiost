package com.pm.aiost.event;

import java.util.Set;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockDispenseArmorEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkPopulateEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import com.pm.aiost.block.CustomBlock;
import com.pm.aiost.entity.spawner.AiostSpawnerCreature;
import com.pm.aiost.event.effect.EffectAction;
import com.pm.aiost.event.effect.EffectHandler;
import com.pm.aiost.event.effect.collection.EffectData;
import com.pm.aiost.event.eventHandler.EventHandlerManager;
import com.pm.aiost.event.events.PacketThingAttackEvent;
import com.pm.aiost.event.events.PacketThingInteractEvent;
import com.pm.aiost.event.events.PlayerEquipHandItemEvent;
import com.pm.aiost.event.events.PlayerEquipItemEvent;
import com.pm.aiost.event.events.PlayerJumpEvent;
import com.pm.aiost.item.Items;
import com.pm.aiost.misc.command.Commands;
import com.pm.aiost.misc.menu.InventoryEventHandler;
import com.pm.aiost.misc.menu.inventoryMenu.InventoryMenu;
import com.pm.aiost.misc.other.WordFilter;
import com.pm.aiost.misc.particleEffect.EntityParticleManager;
import com.pm.aiost.misc.particleEffect.particle.IParticle;
import com.pm.aiost.misc.utils.meta.MetaData;
import com.pm.aiost.misc.utils.nms.NMS;
import com.pm.aiost.player.PlayerManager;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.player.handler.ChatHologramHandler;
import com.pm.aiost.player.handler.DamageIndicator;
import com.pm.aiost.player.handler.QualityOfLiveHandler;
import com.pm.aiost.player.unlockable.UnlockableTypes;
import com.pm.aiost.server.world.ServerWorld;
import com.pm.aiost.server.world.WorldManager;
import com.pm.aiost.server.world.chunk.ChunkWatcher;
import com.pm.aiost.server.world.object.tileObject.tileObjects.ProximityFallingBlock.ConstantProximityFallingBlock;

public class AiostListener implements Listener {

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerJoin(PlayerJoinEvent event) {
		ServerPlayer serverPlayer = PlayerManager.onPlayerJoin(event.getPlayer());
		// TODO set join message with rank does not work with Bungeecord
//		event.setJoinMessage(serverPlayer.getRank().prefix(serverPlayer.name) + ChatColor.YELLOW + " joined the game");
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerQuit(PlayerQuitEvent event) {
		PlayerManager.onPlayerQuit(event.getPlayer());
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		if (player.isOnline()) {
			ServerPlayer serverPlayer = ServerPlayer.getByPlayer(player);
			serverPlayer.getEventHandler().onPlayerDeath(serverPlayer, event);
			EffectHandler.deathRunEffects(serverPlayer, event);
			PlayerManager.onPlayerDeath(serverPlayer);
//			Team team = player.getScoreboard().getEntryTeam(player.getName());
//			if (team != null)
//				event.setDeathMessage(team.getColor() + team.getSuffix() + " " + event.getDeathMessage());
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
		if (event.isCancelled())
			return;

		Set<Player> recipients = event.getRecipients();
		for (Player recipient : recipients)
			if (ServerPlayer.getByPlayer(recipient).hidesChat())
				recipients.remove(recipient);

		event.setMessage(WordFilter.process(event.getMessage()));

		ServerPlayer serverPlayer = ServerPlayer.getByPlayer(event.getPlayer());
		ChatHologramHandler.show(serverPlayer, event.getMessage(), recipients);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		if (event.isCancelled())
			return;

		event.setMessage('/' + Commands.doOveride(event.getMessage().substring(1)));
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onServerCommand(ServerCommandEvent event) {
		if (event.isCancelled())
			return;

		event.setCommand(Commands.doOveride(event.getCommand()));
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerMove(PlayerMoveEvent event) {
		if (event.isCancelled())
			return;

		ServerPlayer serverPlayer = ServerPlayer.getByPlayer(event.getPlayer());
		serverPlayer.getEventHandler().onPlayerMove(serverPlayer, event);
		if (event.isCancelled())
			return;

		EffectHandler.moveRunEffects(serverPlayer, event);
		if (event.isCancelled())
			return;

		Location fromLocation = event.getFrom();
		Location toLocation = event.getTo();
		if (fromLocation.getBlockX() != toLocation.getBlockX() || fromLocation.getBlockY() != toLocation.getBlockY()
				|| fromLocation.getBlockZ() != toLocation.getBlockZ()) { // Player change block position

			serverPlayer.getEventHandler().onPlayerChangeBlockPosition(serverPlayer, event);
			if (event.isCancelled())
				return;
			serverPlayer.getEffectData().onPlayerChangeBlockPosition(serverPlayer.getServerWorld(), toLocation);
			PlayerManager.checkRegionChanged(serverPlayer, toLocation);

			if (fromLocation.getChunk() != toLocation.getChunk()) { // Player change chunk
				serverPlayer.getEventHandler().onPlayerChangeChunk(serverPlayer, event);
				if (event.isCancelled())
					return;

				ChunkWatcher.move(serverPlayer, fromLocation, toLocation);
			}
		}
		jumpCheck(serverPlayer, event);
	}

	private static void jumpCheck(ServerPlayer serverPlayer, PlayerMoveEvent event) {
		Player player = serverPlayer.player;
		if (player.getVelocity().getY() > 0) {
			double jumpVelocity = (double) 0.42F;
			if (player.hasPotionEffect(PotionEffectType.JUMP))
				jumpVelocity += (double) ((float) (player.getPotionEffect(PotionEffectType.JUMP).getAmplifier() + 1)
						* 0.1F);

			if (player.getLocation().getBlock().getType() != Material.LADDER && serverPlayer.onGround) {
				if (!player.isOnGround() && Double.compare(player.getVelocity().getY(), jumpVelocity) == 0) {
					if (AiostEventFactory.callPlayerJumpEvent(serverPlayer).isCancelled()) {
						event.setCancelled(true);
						return;
					}
				}
			}
		}
		serverPlayer.onGround = player.isOnGround();
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerJump(PlayerJumpEvent event) {
		ServerPlayer serverPlayer = event.getServerPlayer();
		serverPlayer.getEventHandler().onPlayerJump(event);
		if (event.isCancelled())
			return;

		EffectHandler.jumpRunEffects(event);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		if (event.isCancelled())
			return;

		Location fromLocation = event.getFrom();
		Location toLocation = event.getTo();
		if (fromLocation.getWorld() == toLocation.getWorld()) {
			ServerPlayer serverPlayer = ServerPlayer.getByPlayer(event.getPlayer());
			ChunkWatcher.teleport(serverPlayer, fromLocation, toLocation);
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerInteract(PlayerInteractEvent event) {
		ServerPlayer serverPlayer = ServerPlayer.getByPlayer(event.getPlayer());
		serverPlayer.lastInteractedBlockface = event.getBlockFace();
		serverPlayer.getEventHandler().onPlayerInteract(serverPlayer, event);
		Action action = event.getAction();
		if (serverPlayer.ignoreNextLeftClickInteract) {
			if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
				serverPlayer.ignoreNextLeftClickInteract = false;
				return;
			}
		}

		int currentTick = NMS.getMinecraftServerTick();
		if (serverPlayer.lastInteractedTick != currentTick) {
			if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK)
				EffectHandler.interactRunEffects(serverPlayer, event, EffectAction.LEFT_CLICK);
			else if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)
				EffectHandler.interactRunEffects(serverPlayer, event, EffectAction.RIGHT_CLICK);

			serverPlayer.lastInteractedTick = currentTick;
		}

		if (event.getHand() == EquipmentSlot.HAND) {
			if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
				EffectHandler.interactMainHandRunEffects(serverPlayer, event, EffectAction.LEFT_CLICK_MAIN_HAND);
			} else if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
				EffectHandler.interactMainHandRunEffects(serverPlayer, event, EffectAction.RIGHT_CLICK_MAIN_HAND);
				onPlayerInteractRightClick(serverPlayer, event);
			}
		} else if (event.getHand() == EquipmentSlot.OFF_HAND) {
			if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
				EffectHandler.interactOffHandRunEffects(serverPlayer, event, EffectAction.LEFT_CLICK_OFF_HAND);
			} else if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
				EffectHandler.interactOffHandRunEffects(serverPlayer, event, EffectAction.RIGHT_CLICK_OFF_HAND);
				onPlayerInteractRightClick(serverPlayer, event);
			}
		}
	}

	public void onPlayerInteractRightClick(ServerPlayer serverPlayer, PlayerInteractEvent event) {
		ItemStack itemStack = event.getItem();
		if (itemStack != null && itemStack.getType() == Material.FISHING_ROD)
			serverPlayer.ignoreNextLeftClickInteract = true;
		serverPlayer.lastRightClickedIS = itemStack;
		serverPlayer.lastRightClickedEquipmentSlot = event.getHand();
		EquipmentListener.playerInteractCheck(serverPlayer, event);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		if (event.isCancelled())
			return;

		ServerPlayer serverPlayer = ServerPlayer.getByPlayer(event.getPlayer());
		serverPlayer.getEventHandler().onPlayerInteractEntity(serverPlayer, event);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
		if (event.isCancelled())
			return;

		ServerPlayer serverPlayer = ServerPlayer.getByPlayer(event.getPlayer());
		serverPlayer.getEventHandler().onPlayerInteractAtEntity(serverPlayer, event);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerEquipItem(PlayerEquipItemEvent event) {
		if (event.isCancelled())
			return;

		event.getServerPlayer().getEventHandler().onPlayerEquipItem(event);
		if (event.isCancelled())
			return;

		EffectHandler.itemUnequipRunEffects(event);
		if (event.isCancelled())
			return;

		EffectData effectData = event.getServerPlayer().getEffectData();
		effectData.removeEffect(event);
		if (event.getItemStack() == null)
			return;

		EffectHandler.itemEquipRunEffects(event);
		if (!event.isCancelled())
			effectData.setEffect(event);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerEquipHandItem(PlayerEquipHandItemEvent event) {
		if (event.isCancelled())
			return;

		event.getServerPlayer().getEventHandler().onPlayerEquipItem(event);
		if (event.isCancelled())
			return;

		EffectHandler.itemUnequipRunEffects(event);
		if (event.isCancelled())
			return;

		EffectData effectData = event.getServerPlayer().getEffectData();
		effectData.removeHandEffect(event);
		if (event.getItemStack() == null)
			return;

		EffectHandler.itemEquipRunEffects(event);
		if (!event.isCancelled())
			effectData.setHandEffect(event);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onEntityPickupItem(EntityPickupItemEvent event) {
		if (event.isCancelled())
			return;

		if (event.getEntityType() == EntityType.PLAYER)
			onPlayerPickupItem(event);
		else
			EventHandlerManager.get(event.getEntity()).onEntityPickupItem(event);
	}

	public void onPlayerPickupItem(EntityPickupItemEvent event) {
		if (event.isCancelled())
			return;

		ServerPlayer serverPlayer = ServerPlayer.getByPlayer((Player) event.getEntity());
		serverPlayer.getEventHandler().onPlayerPickupItem(serverPlayer, event);
		if (event.isCancelled())
			return;

		EffectHandler.itemPickupRunEffects(serverPlayer, event);
		if (!event.isCancelled())
			EquipmentListener.itemPickupCheck(serverPlayer, event);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		if (event.isCancelled())
			return;

		ServerPlayer serverPlayer = ServerPlayer.getByPlayer(event.getPlayer());
		serverPlayer.getEventHandler().onPlayerDropItem(serverPlayer, event);
		if (event.isCancelled())
			return;

		EffectHandler.itemDropRunEffects(serverPlayer, event);
		if (!event.isCancelled())
			EquipmentListener.itemDropCheck(serverPlayer, event);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerItemHeld(PlayerItemHeldEvent event) {
		if (event.isCancelled())
			return;

		ServerPlayer serverPlayer = ServerPlayer.getByPlayer(event.getPlayer());
		serverPlayer.getEventHandler().onPlayerItemHeld(serverPlayer, event);
		if (event.isCancelled())
			return;

		EffectHandler.itemHeldRunEffects(serverPlayer, event);
		if (event.isCancelled())
			return;

		EquipmentListener.playerItemHeldCheck(serverPlayer, event);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerSwapItems(PlayerSwapHandItemsEvent event) {
		if (event.isCancelled())
			return;

		ServerPlayer serverPlayer = ServerPlayer.getByPlayer(event.getPlayer());
		serverPlayer.getEventHandler().onPlayerSwapItems(serverPlayer, event);
		if (event.isCancelled())
			return;

		EffectHandler.itemSwapRunEffects(serverPlayer, event);
		if (event.isCancelled())
			return;
		EquipmentListener.swapItemsCheck(serverPlayer, event);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
		if (event.isCancelled())
			return;

		ServerPlayer serverPlayer = ServerPlayer.getByPlayer(event.getPlayer());
		serverPlayer.getEventHandler().onPlayerItemConsume(serverPlayer, event);
		if (event.isCancelled())
			return;

		EffectHandler.consumeItemRunEffects(serverPlayer, event);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerItemBreak(PlayerItemBreakEvent event) {
		ServerPlayer serverPlayer = ServerPlayer.getByPlayer(event.getPlayer());
		serverPlayer.getEventHandler().onPlayerItemBreak(serverPlayer, event);
		EquipmentListener.itemBreakCheck(serverPlayer, event);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onBlockDispenseArmor(BlockDispenseArmorEvent event) {
		if (event.isCancelled())
			return;

		if (event.getTargetEntity() instanceof Player)
			EquipmentListener.blockDispenseArmorCheck(ServerPlayer.getByPlayer((Player) event.getTargetEntity()),
					event);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onItemMerge(ItemMergeEvent event) {
		if (event.isCancelled())
			return;

		EffectHandler.itemMergeRunEffects(event);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerFish(PlayerFishEvent event) {
		if (event.isCancelled())
			return;

		ServerPlayer serverPlayer = ServerPlayer.getByPlayer(event.getPlayer());
		serverPlayer.getEventHandler().onPlayerFish(serverPlayer, event);
		if (event.isCancelled())
			return;

		EffectHandler.fishingRunEffects(serverPlayer, event);
		if (event.isCancelled())
			return;

		if (event.getState() == State.FISHING)
			EffectHandler.fishingRodLaunchRunEffects(serverPlayer, event);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onInventoryOpen(InventoryOpenEvent event) {
		if (event.isCancelled())
			return;

		Inventory inventory = event.getInventory();
		if (inventory.getHolder() instanceof InventoryEventHandler)
			((InventoryEventHandler) inventory.getHolder()).onInventoryOpen(event);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onInventoryClose(InventoryCloseEvent event) {
		Inventory inventory = event.getInventory();
		if (inventory.getHolder() instanceof InventoryEventHandler)
			((InventoryEventHandler) inventory.getHolder()).onInventoryClose(event);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onInventoryClick(InventoryClickEvent event) {
		if (event.isCancelled())
			return;

		if (event.getWhoClicked() instanceof Player) {
			ServerPlayer serverPlayer = ServerPlayer.getByPlayer((Player) event.getWhoClicked());
			serverPlayer.getEventHandler().onInventoryClick(serverPlayer, event);
			if (event.isCancelled())
				return;

			Inventory inventory = event.getInventory();
			if (inventory.getHolder() instanceof InventoryEventHandler) {
				if (InventoryMenu.isSlotInCustomInventory(inventory, event.getRawSlot()))
					((InventoryEventHandler) inventory.getHolder()).onInventoryClick(serverPlayer, event);
			}
			if (event.isCancelled())
				return;

			if (event.getCurrentItem() != null) {
				EffectHandler.inventoryClickRunEffects(serverPlayer, event);
				if (event.isCancelled())
					return;
			}

			InventoryAction action = event.getAction();
			if (action == InventoryAction.SWAP_WITH_CURSOR || action == InventoryAction.PLACE_ALL
					|| action == InventoryAction.PLACE_ONE || action == InventoryAction.PLACE_SOME) {
				EffectHandler.inventoryPlaceRunEffects(serverPlayer, event);
				if (event.isCancelled())
					return;
			}

			EquipmentListener.inventoryClickCheck(serverPlayer, event);
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onInventoryDrag(InventoryDragEvent event) {
		if (event.isCancelled())
			return;

		if (event.getWhoClicked() instanceof Player) {
			ServerPlayer serverPlayer = ServerPlayer.getByPlayer((Player) event.getWhoClicked());
			serverPlayer.getEventHandler().onInventoryDrag(serverPlayer, event);
			if (event.isCancelled())
				return;

			Inventory inventory = event.getInventory();
			if (inventory.getHolder() instanceof InventoryMenu) {
				if (InventoryMenu.isSlotInCustomInventory(inventory, event.getRawSlots())) {
					event.setCancelled(true);
					return;
				}
			}

			EffectHandler.inventoryDragRunEffects(serverPlayer, event);
			if (!event.isCancelled())
				EquipmentListener.inventoryDragCheck(serverPlayer, event);

			if (!event.isCancelled())
				Items.onInventoryDrag(event);
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onInventoryCreative(InventoryCreativeEvent event) {
		if (event.isCancelled())
			return;

		if (event.getWhoClicked() instanceof Player) {
			ServerPlayer serverPlayer = ServerPlayer.getByPlayer((Player) event.getWhoClicked());
			serverPlayer.getEventHandler().onInventoryCreative(serverPlayer, event);
			if (event.isCancelled())
				return;

			if (event.getCurrentItem() != null && event.getCurrentItem().getAmount() != 0)
				EffectHandler.inventoryCreativeClickRunEffects(serverPlayer, event);

			if (event.getCursor() != null && event.getCursor().getAmount() != 0)
				EffectHandler.inventoryCreativePlaceRunEffects(serverPlayer, event);

			if (event.isCancelled())
				return;

			serverPlayer.getServerWorld().onInventoryCreative(serverPlayer, event);
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onBlockPlace(BlockPlaceEvent event) {
		if (event.isCancelled())
			return;

		ServerPlayer serverPlayer = ServerPlayer.getByPlayer(event.getPlayer());
		serverPlayer.getEventHandler().onBlockPlace(serverPlayer, event);
		if (event.isCancelled())
			return;

		EffectHandler.blockPlaceRunEffects(serverPlayer, event);
		if (event.isCancelled())
			return;

		QualityOfLiveHandler.tryfillUpEmptyHeldItem(event);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onBlockBreak(BlockBreakEvent event) {
		if (event.isCancelled())
			return;

		ServerPlayer serverPlayer = ServerPlayer.getByPlayer(event.getPlayer());
		serverPlayer.getEventHandler().onBlockBreak(serverPlayer, event);
		if (event.isCancelled())
			return;

		EffectHandler.blockBreakRunEffects(serverPlayer, event);
		if (event.isCancelled())
			return;

		serverPlayer.getServerWorld().removeBlock(event.getBlock());
		if (event.isCancelled())
			return;

		CustomBlock.blockBreak(event);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onBlockDamage(BlockDamageEvent event) {
		if (event.isCancelled())
			return;

		ServerPlayer serverPlayer = ServerPlayer.getByPlayer(event.getPlayer());
		serverPlayer.getEventHandler().onBlockDamage(serverPlayer, event);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onEntityChangeBlockEvent(EntityChangeBlockEvent event) {
		Entity entity = event.getEntity();
		if (entity instanceof LivingEntity)
			EventHandlerManager.get(entity).onEntityChangeBlock(event);
		else if (entity instanceof FallingBlock) {
			Object obj = MetaData.get(entity, "ConstantProximityFallingBlock");
			if (obj != null) {
				((ConstantProximityFallingBlock) obj).hitFallingBlock((FallingBlock) entity);
				event.setCancelled(true);
				return;
			}
		}

		if (event.getTo() == Material.AIR)
			ServerWorld.getByWorld(entity.getWorld()).removeBlock(event.getBlock());
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onBlockPistonExtendEvent(BlockPistonExtendEvent event) {
		ServerWorld serverWorld = ServerWorld.getByWorld(event.getBlock().getWorld());
		serverWorld.onPistonExpand(event);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntitySpawn(EntitySpawnEvent event) {
		if (event.isCancelled())
			return;

		com.pm.aiost.event.eventHandler.EventHandler eventHandler = EventHandlerManager.get(event.getLocation());
		eventHandler.onEntitySpawn(event);
		if (event.isCancelled())
			return;

		if (event.getEntity() instanceof LivingEntity)
			EventHandlerManager.setEntityHandler((LivingEntity) event.getEntity(), eventHandler);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		if (event.isCancelled())
			return;

		AiostSpawnerCreature.onCreatureSpawn(event);
		if (event.isCancelled())
			return;

		EventHandlerManager.get(event.getEntity()).onCreatureSpawn(event);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onEntityDeath(EntityDeathEvent event) {
		LivingEntity entity = event.getEntity();
		Player killer = entity.getKiller();
		if (killer != null && killer.isOnline()) {
			ServerPlayer serverPlayer = ServerPlayer.getByPlayer(killer);
			serverPlayer.getEventHandler().onEntityDeathByPlayer(serverPlayer, event);
		}
		if (entity instanceof Player && ((Player) entity).isOnline())
			ServerPlayer.getByPlayer((Player) entity).getEventHandler().onEntityDeath(event);
		else
			EventHandlerManager.removeEntityHandler(entity).onEntityDeath(event);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onEntityExplode(EntityExplodeEvent event) {
		if (event.getEntity() instanceof LivingEntity)
			EventHandlerManager.removeEntityHandler((LivingEntity) event.getEntity()).onEntityExplode(event);
		else
			EventHandlerManager.get(event.getLocation()).onEntityExplode(event);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onProjectileLaunch(ProjectileLaunchEvent event) {
		if (event.isCancelled())
			return;

		Projectile projectile = event.getEntity();
		if (projectile.getShooter() instanceof LivingEntity) {
			LivingEntity shooter = (LivingEntity) projectile.getShooter();

			if (shooter.getType() == EntityType.PLAYER) {
				ServerPlayer serverPlayer = ServerPlayer.getByPlayer((Player) shooter);
				serverPlayer.getEventHandler().onProjectileLaunch(event);
				serverPlayer.getEventHandler().onPlayerProjectileLaunch(serverPlayer, event);
				if (!event.isCancelled()) {
					EffectHandler.projectileLaunchRunEffects(serverPlayer, event);
					int projectileParticle = UnlockableTypes.PROJECTILE_PARTICLE.get(serverPlayer);
					if (projectileParticle > 0)
						EntityParticleManager.registerEntity(projectile,
								(IParticle) UnlockableTypes.PROJECTILE_PARTICLE.getObject(projectileParticle));
				}
			}

			else {
				EventHandlerManager.get((LivingEntity) shooter).onProjectileLaunch(event);
			}
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onEntityShootBow(EntityShootBowEvent event) {
		if (event.isCancelled())
			return;

		LivingEntity shooter = event.getEntity();
		if (shooter.getType() == EntityType.PLAYER) {
			ServerPlayer serverPlayer = ServerPlayer.getByPlayer((Player) shooter);
			serverPlayer.getEventHandler().onEntityShootBow(event);
			if (!event.isCancelled())
				EffectHandler.shootBowRunEffects(serverPlayer, event);
		}

		else {
			EventHandlerManager.get(shooter).onEntityShootBow(event);
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onProjectileHit(ProjectileHitEvent event) {
		Entity hitEntity = event.getHitEntity();
		if (hitEntity != null) {
			if (hitEntity.getType() == EntityType.PLAYER)
				ServerPlayer.getByPlayer((Player) hitEntity).getEventHandler().onProjectileHit(event);
			else
				EventHandlerManager.get(hitEntity).onProjectileHit(event);
		} else
			EventHandlerManager.get(event.getHitBlock().getLocation()).onProjectileHit(event);

		if (event.getEntity().getShooter() instanceof Player) {
			ServerPlayer serverPlayer = ServerPlayer.getByPlayer((Player) event.getEntity().getShooter());
			EffectHandler.projectileHitRunEffects(serverPlayer, event);
			EntityParticleManager.unregisterEntity(event.getEntity());
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onEntityTarget(EntityTargetEvent event) {
		Entity target = event.getTarget();
		if (target instanceof Player) {
			ServerPlayer serverPlayer = ServerPlayer.getByPlayer((Player) target);
			serverPlayer.getEventHandler().onEntityTargetPlayer(serverPlayer, event);
			if (!event.isCancelled())
				serverPlayer.getEventHandler().onEntityTarget(event);
			return;
		}
		EventHandlerManager.getOrEmpty(event.getEntity()).onEntityTarget(event);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onEntityDamage(EntityDamageEvent event) {
		if (event.isCancelled())
			return;

		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if (player.isOnline())
				onPlayerDamage(player, event);
		} else
			EventHandlerManager.get(event.getEntity()).onEntityDamage(event);
	}

	public void onPlayerDamage(Player player, EntityDamageEvent event) {
		if (event.isCancelled())
			return;

		ServerPlayer serverPlayer = ServerPlayer.getByPlayer(player);
		serverPlayer.getEventHandler().onPlayerDamage(serverPlayer, event);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (event.isCancelled())
			return;

		if (event.getEntity() instanceof Player) {
			Player damagedPlayer = (Player) event.getEntity();
			if (damagedPlayer.isOnline()) {
				ServerPlayer damagedServerPlayer = ServerPlayer.getByPlayer(damagedPlayer);
				damagedServerPlayer.getEventHandler().onPlayerDamageByEntity(damagedServerPlayer, event);
			}
		} else
			EventHandlerManager.get(event.getEntity()).onEntityDamageByEntity(event);

		if (event.getDamager() instanceof Player) {
			Player damagerPlayer = (Player) event.getDamager();
			if (damagerPlayer.isOnline()) {
				ServerPlayer damagerServerPlayer = ServerPlayer.getByPlayer(damagerPlayer);
				damagerServerPlayer.getEventHandler().onEntityDamageByPlayer(damagerServerPlayer, event);

				if (!event.isCancelled())
					DamageIndicator.show(damagerServerPlayer, event.getEntity(), event.getFinalDamage(),
							damagerPlayer.getFallDistance() > 0 && !damagerPlayer.isOnGround());
			}
		} else if (event.getDamager() instanceof Projectile) {
			Projectile damagerProjectile = (Projectile) event.getDamager();
			if (damagerProjectile.getShooter() instanceof Player) {
				Player shooterPlayer = (Player) damagerProjectile.getShooter();
				if (shooterPlayer.isOnline()) {
					ServerPlayer shooterServerPlayer = ServerPlayer.getByPlayer(shooterPlayer);
					shooterServerPlayer.getEventHandler().onEntityDamageByPlayer(shooterServerPlayer, event);

					if (!event.isCancelled())
						DamageIndicator.show(shooterServerPlayer, event.getEntity(), event.getFinalDamage(),
								damagerProjectile instanceof Arrow && ((Arrow) damagerProjectile).isCritical());
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onWorldInit(WorldInitEvent event) {
		// This removes lag on world loading!
		event.getWorld().setKeepSpawnInMemory(false);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onWorldLoad(WorldLoadEvent event) {

	}

	@EventHandler(priority = EventPriority.LOW)
	public void onWorldUnload(WorldUnloadEvent event) {
		if (event.isCancelled())
			return;

		WorldManager.unregisterWorld(event.getWorld());
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onWorldSave(WorldSaveEvent event) {
		ServerWorld.getByWorld(event.getWorld()).save();
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
		PlayerManager.onPlayerChangedWorld(event.getPlayer(), event.getFrom());
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onChunkLoad(ChunkLoadEvent event) {
		Chunk chunk = event.getChunk();
		WorldManager.registerIfAbsent(event.getWorld()).loadChunk(chunk);
		EventHandlerManager.registerEntities(chunk);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onChunkPopulate(ChunkPopulateEvent event) {

	}

	@EventHandler(priority = EventPriority.LOW)
	public void onChunkUnload(ChunkUnloadEvent event) {
		Chunk chunk = event.getChunk();
		ServerWorld.getByWorld(event.getWorld()).unloadChunk(chunk, event.isSaveChunk());
		EventHandlerManager.unregisterEntities(chunk);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPacketThingAttack(PacketThingAttackEvent event) {
		if (event.isCancelled())
			return;

		event.getServerPlayer().getEventHandler().onPacketThingAttack(event);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPacketThingInteract(PacketThingInteractEvent event) {
		if (event.isCancelled())
			return;

		event.getServerPlayer().getEventHandler().onPacketThingInteract(event);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onCraftItem(CraftItemEvent event) {
		if (event.isCancelled())
			return;
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onFurnaceSmelt(FurnaceSmeltEvent event) {
		if (event.isCancelled())
			return;

		Items.onFurnaceSmelt(event);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerToggleSneak(PlayerInteractEvent event) {
		Player player = event.getPlayer();
//		ServerPlayer serverPlayer = ServerPlayer.getByPlayer(player);
//		EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();

//		NpcBase test = new NpcBase(entityPlayer.server, entityPlayer.getWorldServer(), "Tiimmyy1209", "Ben");
//		Npc test = new Npc(entityPlayer.server, entityPlayer.getWorldServer(), "Tiimmyy1209", "Ben");
//		NpcEnemy test = new NpcEnemy(entityPlayer.server, entityPlayer.getWorldServer(), "Tiimmyy1209", "Ben");

//		GameProfile profile = ProfileCache.get("Tiimmyy1209", "Ben");
//		System.out.println(profile1);
//		System.out.println(ProfileBuilder.getSkinProperty(profile1).getSignature() + " "
//				+ ProfileBuilder.getSkinProperty(profile1).getValue());

//		NpcEnemy test = new NpcEnemy(entityPlayer.world, Profiles.ORC);
//		AiostEntityTypes.spawnEntity(test, player.getLocation());

//		Snail snail = new Snail(entityPlayer.getWorld(), player.getLocation().getX(), player.getLocation().getY(),
//				player.getLocation().getZ());
//		entityPlayer.getWorld().addEntity(snail);
	}
}
