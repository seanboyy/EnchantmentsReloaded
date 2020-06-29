package io.github.seanboyy.enchantmentsreloaded.registers;

import io.github.seanboyy.enchantmentsreloaded.EnchantmentsReloaded;
import io.github.seanboyy.enchantmentsreloaded.objects.blocks.*;
import net.minecraft.block.Block;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Blocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, EnchantmentsReloaded.MOD_ID);

    public static final RegistryObject<Block> CURSEBREAKER = BLOCKS.register("cursebreaker", () -> new CursebreakerBlock(Block.Properties.from(net.minecraft.block.Blocks.ENCHANTING_TABLE)));
    public static final RegistryObject<Block> ENCHANTMENT_CRAFTING_TABLE = BLOCKS.register("enchantment_crafting_table", () -> new EnchantmentCraftingTableBlock(Block.Properties.from(net.minecraft.block.Blocks.ENCHANTING_TABLE)));
    public static final RegistryObject<Block> TRANSFER_TABLE = BLOCKS.register("transfer_table", () -> new TransferTableBlock(Block.Properties.from(net.minecraft.block.Blocks.ANVIL)));
    public static final RegistryObject<Block> TRANSFER_TABLE_CHIPPED = BLOCKS.register("chipped_transfer_table", () -> new TransferTableBlock(Block.Properties.from(net.minecraft.block.Blocks.CHIPPED_ANVIL)));
    public static final RegistryObject<Block> TRANSFER_TABLE_DAMAGED = BLOCKS.register("damaged_transfer_table", () -> new TransferTableBlock(Block.Properties.from(net.minecraft.block.Blocks.DAMAGED_ANVIL)));
}
