package io.github.seanboyy.enchantmentsreloaded.registers;

import io.github.seanboyy.enchantmentsreloaded.EnchantmentsReloaded;
import io.github.seanboyy.enchantmentsreloaded.objects.items.EnchantmentModifierItem;
import io.github.seanboyy.enchantmentsreloaded.util.EnchantmentModifierType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Rarity;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Items {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, EnchantmentsReloaded.MOD_ID);

    public static final RegistryObject<Item> RANDOMIZER = ITEMS.register("enchantment_randomizer", () -> new EnchantmentModifierItem(new Item.Properties().group(ItemGroup.MISC).maxStackSize(16).rarity(Rarity.UNCOMMON), EnchantmentModifierType.RANDOMIZE));
    public static final RegistryObject<Item> ENCHANTER = ITEMS.register("enchantment_adder", () -> new EnchantmentModifierItem(new Item.Properties().group(ItemGroup.MISC).maxStackSize(16).rarity(Rarity.RARE), EnchantmentModifierType.ADD));
    public static final RegistryObject<Item> DISENCHANTER = ITEMS.register("enchantment_subtractor", () -> new EnchantmentModifierItem(new Item.Properties().group(ItemGroup.MISC).maxStackSize(16).rarity(Rarity.RARE), EnchantmentModifierType.SUBTRACT));
    public static final RegistryObject<Item> LEVELER = ITEMS.register("enchantment_leveler", () -> new EnchantmentModifierItem(new Item.Properties().group(ItemGroup.MISC).maxStackSize(16).rarity(Rarity.EPIC), EnchantmentModifierType.LEVEL));
}
