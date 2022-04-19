package com.pm.aiost.item.custom;

import net.minecraft.server.v1_15_R1.ItemStack;

@FunctionalInterface
public interface InteractionResultWrapper {

	public static final InteractionResultWrapper CONSUME = net.minecraft.server.v1_15_R1.InteractionResultWrapper::consume;

	public static final InteractionResultWrapper PASS = net.minecraft.server.v1_15_R1.InteractionResultWrapper::pass;

	public static final InteractionResultWrapper FAIL = net.minecraft.server.v1_15_R1.InteractionResultWrapper::fail;

	public static final InteractionResultWrapper SUCCESS = net.minecraft.server.v1_15_R1.InteractionResultWrapper::success;

	public net.minecraft.server.v1_15_R1.InteractionResultWrapper<ItemStack> get(ItemStack itemstack);

}
