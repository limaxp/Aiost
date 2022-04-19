package com.pm.aiost.item.custom;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.pm.aiost.misc.utils.nms.NMS;

import net.minecraft.server.v1_15_R1.AttributeModifier;
import net.minecraft.server.v1_15_R1.BlockPosition;
import net.minecraft.server.v1_15_R1.EntityHuman;
import net.minecraft.server.v1_15_R1.EntityLiving;
import net.minecraft.server.v1_15_R1.EnumAnimation;
import net.minecraft.server.v1_15_R1.EnumHand;
import net.minecraft.server.v1_15_R1.EnumInteractionResult;
import net.minecraft.server.v1_15_R1.EnumItemSlot;
import net.minecraft.server.v1_15_R1.IBlockData;
import net.minecraft.server.v1_15_R1.Item;
import net.minecraft.server.v1_15_R1.ItemActionContext;

public class AiostItem extends Item {

	public AiostItem(Info info) {
		super(info);
	}

	@Override
	public boolean a(IBlockData blockData, net.minecraft.server.v1_15_R1.World world, BlockPosition pos,
			EntityHuman entity) {
		return onLeftClickBlock(NMS.getBukkit(blockData),
				new Location(NMS.getBukkit(world), pos.getX(), pos.getY(), pos.getZ()), NMS.getBukkit(entity));
	}

	protected boolean onLeftClickBlock(BlockData blockData, Location loc, HumanEntity entity) {
		return true;
	}

	@Override
	public boolean a(net.minecraft.server.v1_15_R1.ItemStack is, EntityLiving entity1, EntityLiving entity2) {
		return onLeftClickEntity(NMS.getBukkit(is), NMS.getBukkit(entity1), NMS.getBukkit(entity2));
	}

	protected boolean onLeftClickEntity(ItemStack is, LivingEntity entity1, LivingEntity entity2) {
		return false;
	}

	@Override
	public net.minecraft.server.v1_15_R1.InteractionResultWrapper<net.minecraft.server.v1_15_R1.ItemStack> a(
			net.minecraft.server.v1_15_R1.World world, EntityHuman human, EnumHand hand) {
		net.minecraft.server.v1_15_R1.ItemStack is = human.b(hand);
		return onRightClick(NMS.getBukkit(world), NMS.getBukkit(human), Hand.get(hand), NMS.getBukkit(is)).get(is);
	}

	protected InteractionResultWrapper onRightClick(World world, HumanEntity human, Hand hand, ItemStack is) {
		if (isFood()) {
			EntityHuman human1 = NMS.getNMS(human);
			if (human1.p(getFoodInfo().d())) {
				holdRightClick(human1, hand.hand);
				return InteractionResultWrapper.CONSUME;
			}
			return InteractionResultWrapper.FAIL;
		}
		return InteractionResultWrapper.PASS;
	}

	protected static void holdRightClick(HumanEntity entity, Hand hand) {
		holdRightClick(NMS.getNMS(entity), hand.hand);
	}

	protected static void holdRightClick(EntityHuman entity, EnumHand hand) {
		entity.c(hand);
	}

	@Override
	public void a(net.minecraft.server.v1_15_R1.ItemStack is, net.minecraft.server.v1_15_R1.World world,
			EntityLiving entity, int i) {
		onRightClickRelease(NMS.getBukkit(is), NMS.getBukkit(world), NMS.getBukkit(entity), i);
	}

	/**
	 * You need to call holdRightClick() in onRightClick() for this to get called!
	 */
	protected void onRightClickRelease(ItemStack is, World world, LivingEntity entity, int i) {
	}

	@Override
	public boolean a(net.minecraft.server.v1_15_R1.ItemStack item, EntityHuman human, EntityLiving entity,
			EnumHand hand) {
		return onRightClickEntity(NMS.getBukkit(item), NMS.getBukkit(human), NMS.getBukkit(entity), Hand.get(hand));
	}

	protected boolean onRightClickEntity(ItemStack item, HumanEntity human, LivingEntity entity, Hand hand) {
		return false;
	}

	@Override
	public EnumInteractionResult a(ItemActionContext actionContext) {
		return onRightClickBlock(actionContext);
	}

	protected EnumInteractionResult onRightClickBlock(ItemActionContext actionContext) {
		return EnumInteractionResult.PASS;
	}

	@Override
	public void a(net.minecraft.server.v1_15_R1.ItemStack is, net.minecraft.server.v1_15_R1.World world,
			net.minecraft.server.v1_15_R1.Entity entity, int i, boolean bool) {
		onInventoryTick(NMS.getBukkit(is), NMS.getBukkit(world), NMS.getBukkit(entity), i, bool);
	}

	protected void onInventoryTick(ItemStack is, World world, Entity entity, int i, boolean bool) {
	}

	@Override
	public Multimap<String, AttributeModifier> a(EnumItemSlot slot) {
		return onEquip(slot);
	}

	protected Multimap<String, AttributeModifier> onEquip(EnumItemSlot var0) {
		return HashMultimap.create();
	}

	@Override
	public EnumAnimation e_(net.minecraft.server.v1_15_R1.ItemStack is) {
		return getAnimation(NMS.getBukkit(is)).nms;
	}

	protected ItemAnimation getAnimation(ItemStack is) {
		return is.getType().isEdible() ? ItemAnimation.EAT : ItemAnimation.NONE;
	}

	@Override
	public float getDestroySpeed(net.minecraft.server.v1_15_R1.ItemStack is, IBlockData block) {
		return getDestroySpeed(NMS.getBukkit(is), NMS.getBukkit(block));
	}

	public float getDestroySpeed(ItemStack is, BlockData block) {
		return 1.0F;
	}

	@Override
	public boolean canDestroySpecialBlock(IBlockData block) {
		return canDestroySpecialBlock(NMS.getBukkit(block));
	}

	protected boolean canDestroySpecialBlock(BlockData block) {
		return false;
	}

	@Override
	public boolean a(net.minecraft.server.v1_15_R1.ItemStack item, net.minecraft.server.v1_15_R1.World world,
			IBlockData block, BlockPosition pos, EntityLiving entity) {
		return onBlockBreak(NMS.getBukkit(item), new Location(NMS.getBukkit(world), pos.getX(), pos.getY(), pos.getZ()),
				NMS.getBukkit(block), NMS.getBukkit(entity));
	}

	/**
	 * Does only get called if not in creative!
	 */
	protected boolean onBlockBreak(ItemStack item, Location loc, BlockData block, LivingEntity entity) {
		return false;
	}

	@Override
	public void a(net.minecraft.server.v1_15_R1.World world, EntityLiving entity,
			net.minecraft.server.v1_15_R1.ItemStack item, int i) {
		onHoldRightClick(NMS.getBukkit(world), NMS.getBukkit(entity), NMS.getBukkit(item), i);
	}

	/**
	 * You need to call holdRightClick() in onRightClick() for this to get called!
	 */
	protected void onHoldRightClick(World world, LivingEntity entity, ItemStack item, int i) {
	}

	@Override
	public net.minecraft.server.v1_15_R1.ItemStack a(net.minecraft.server.v1_15_R1.ItemStack item,
			net.minecraft.server.v1_15_R1.World world, EntityLiving entity) {
		if (isFood()) // TODO this might make problems!
			item = entity.a(world, item);
		return NMS.getNMS(onConsumeItem(NMS.getBukkit(item), NMS.getBukkit(world), NMS.getBukkit(entity)));
	}

	// TODO: probably also destruction and eating! Potion drinking is safe!
	public ItemStack onConsumeItem(ItemStack item, World world, LivingEntity entity) {
		System.out.println("AIOSTITEM: SENNA");
		return item;
	}

	@Override
	public void b(net.minecraft.server.v1_15_R1.ItemStack var0, net.minecraft.server.v1_15_R1.World var1,
			EntityHuman var2) {
		// TODO find out what this does!
		System.out.println("AIOSTITEM: SERVUS");
		super.b(var0, var1, var2);
	}

	@Override
	public boolean a(net.minecraft.server.v1_15_R1.ItemStack var0, net.minecraft.server.v1_15_R1.ItemStack var1) {
		// TODO find out what this does!
		System.out.println("AIOSTITEM: WALUM");
		return super.a(var0, var1);
	}
}
