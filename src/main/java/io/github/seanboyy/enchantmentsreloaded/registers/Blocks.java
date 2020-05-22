package io.github.seanboyy.enchantmentsreloaded.registers;

import io.github.seanboyy.enchantmentsreloaded.EnchantmentsReloaded;
import io.github.seanboyy.enchantmentsreloaded.objects.blocks.CursebreakerBlock;
import io.github.seanboyy.enchantmentsreloaded.objects.blocks.EnchantmentCraftingTableBlock;
import net.minecraft.block.Block;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Blocks {
    public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, EnchantmentsReloaded.MOD_ID);

    public static final RegistryObject<Block> CURSEBREAKER = BLOCKS.register("cursebreaker", () -> new CursebreakerBlock(Block.Properties.from(net.minecraft.block.Blocks.ENCHANTING_TABLE)));
    public static final RegistryObject<Block> ENCHANTMENT_CRAFTING_TABLE = BLOCKS.register("enchantment_crafting_table", () -> new EnchantmentCraftingTableBlock(Block.Properties.from(net.minecraft.block.Blocks.ENCHANTING_TABLE)));
}
