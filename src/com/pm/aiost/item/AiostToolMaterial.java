package com.pm.aiost.item;

import java.lang.invoke.MethodHandle;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.pm.aiost.misc.log.Logger;
import com.pm.aiost.misc.utils.nms.NMS;
import com.pm.aiost.misc.utils.reflection.Reflection;

import net.minecraft.server.v1_15_R1.EnumToolMaterial;
import net.minecraft.server.v1_15_R1.IMaterial;
import net.minecraft.server.v1_15_R1.LazyInitVar;
import net.minecraft.server.v1_15_R1.RecipeItemStack;
import net.minecraft.server.v1_15_R1.ToolMaterial;

public class AiostToolMaterial implements ToolMaterial {

	private static final Class<?> ENUM_TOOL_MATERIAL_CLASS = NMS.getNMSClass("EnumToolMaterial");
	private static final MethodHandle ENUMTOOLMATERIAL_K_GET = Reflection.unreflectGetter(ENUM_TOOL_MATERIAL_CLASS,
			"k");
	private static final Map<String, AiostToolMaterial> NAME_MAP = new HashMap<String, AiostToolMaterial>();

	public static final AiostToolMaterial WOOD = a("wood", EnumToolMaterial.WOOD);

	public static final AiostToolMaterial STONE = a("stone", EnumToolMaterial.STONE);

	public static final AiostToolMaterial IRON = a("iron", EnumToolMaterial.IRON);

	public static final AiostToolMaterial DIAMOND = a("diamond", EnumToolMaterial.DIAMOND);

	public static final AiostToolMaterial GOLD = a("gold", EnumToolMaterial.GOLD);

	public static final AiostToolMaterial COPPER = a("copper", 1, 200, 5.0F, 1.5F, 8,
			() -> RecipeItemStack.a(new IMaterial[] {}));

	public static final AiostToolMaterial TIN = a("tin", 1, 200, 5.0F, 1.5F, 8,
			() -> RecipeItemStack.a(new IMaterial[] {}));

	public static final AiostToolMaterial BRONZE = a("bronze", 2, 250, 6.0F, 2.0F, 18,
			() -> RecipeItemStack.a(new IMaterial[] {}));

	public static final AiostToolMaterial EMERALD = a("emerald", 3, 1000, 7.0F, 2.5F, 10,
			() -> RecipeItemStack.a(new IMaterial[] {}));

	public static final AiostToolMaterial RUBY = a("ruby", 3, 1444, 7.0F, 2.5F, 14,
			() -> RecipeItemStack.a(new IMaterial[] {}));

	public static final AiostToolMaterial BLACK_DIAMOND = a("black_diamond", 4, 2372, 9.0F, 4.0F, 18,
			() -> RecipeItemStack.a(new IMaterial[] {}));

	public static final AiostToolMaterial OBSIDIAN_CRYSTAL = a("obsidian_crystal", 4, 3000, 9.0F, 4.0F, 12,
			() -> RecipeItemStack.a(new IMaterial[] {}));

	public static final AiostToolMaterial LAVA_CRYSTAL = a("lava_crystal", 4, 2700, 9.0F, 4.0F, 20,
			() -> RecipeItemStack.a(new IMaterial[] {}));

	public static final AiostToolMaterial QUARTZ_CRYSTAL = a("quartz_crystal", 5, 3500, 10.0F, 5.0F, 22,
			() -> RecipeItemStack.a(new IMaterial[] {}));

	public static AiostToolMaterial a(String name, EnumToolMaterial toolMaterial) {
		AiostToolMaterial aiostToolMaterial = new AiostToolMaterial(toolMaterial);
		register(name, aiostToolMaterial);
		return aiostToolMaterial;
	}

	public static AiostToolMaterial a(String name, int var2, int durability, float damage, float var5, int var6,
			Supplier<RecipeItemStack> var7) {
		AiostToolMaterial aiostToolMaterial = new AiostToolMaterial(var2, durability, damage, var5, var6, var7);
		register(name, aiostToolMaterial);
		return aiostToolMaterial;
	}

	public static void register(String name, AiostToolMaterial toolMaterial) {
		NAME_MAP.put(name.toLowerCase(), toolMaterial);
	}

	public static AiostToolMaterial get(String name) {
		return NAME_MAP.get(name);
	}

	public static AiostToolMaterial getOrDefault(String name, AiostToolMaterial defaultValue) {
		return NAME_MAP.getOrDefault(name, defaultValue);
	}

	public static AiostToolMaterial getIgnoreCase(String name) {
		return NAME_MAP.get(name.toLowerCase());
	}

	private final int f;
	private final int g;
	private final float h;
	private final float i;
	private final int j;
	private LazyInitVar<RecipeItemStack> k;

	public AiostToolMaterial(EnumToolMaterial toolMaterial) {
		this.f = toolMaterial.d();
		this.g = toolMaterial.a();
		this.h = toolMaterial.b();
		this.i = toolMaterial.c();
		this.j = toolMaterial.e();
		try {
			this.k = (LazyInitVar<RecipeItemStack>) ENUMTOOLMATERIAL_K_GET.invoke(toolMaterial);
		} catch (Throwable e) {
			Logger.err("AiostToolMaterial: Error could not reflect 'k' field", e);
			throw new RuntimeException();
		}
	}

	public AiostToolMaterial(int var2, int durability, float damage, float var5, int var6,
			Supplier<RecipeItemStack> var7) {
		this.f = var2;
		this.g = durability;
		this.h = damage;
		this.i = var5;
		this.j = var6;
		this.k = new LazyInitVar<RecipeItemStack>(var7);
	}

	@Override
	public int a() {
		return this.g;
	}

	@Override
	public float b() {
		return this.h;
	}

	@Override
	public float c() {
		return this.i;
	}

	@Override
	public int d() {
		return this.f;
	}

	@Override
	public int e() {
		return this.j;
	}

	@Override
	public RecipeItemStack f() {
		return (RecipeItemStack) this.k.a();
	}
}
