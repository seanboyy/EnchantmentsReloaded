package io.github.seanboyy.enchantmentsreloaded.registers;

import io.github.seanboyy.enchantmentsreloaded.EnchantmentsReloaded;
import io.github.seanboyy.enchantmentsreloaded.objects.tileentity.*;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TileEntities {
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, EnchantmentsReloaded.MOD_ID);

    public static final RegistryObject<TileEntityType<CursebreakerTileEntity>> CURSEBREAKER = TILE_ENTITIES.register("cursebreaker", () -> TileEntityType.Builder.create(CursebreakerTileEntity::new, Blocks.CURSEBREAKER.get()).build(null));
    public static final RegistryObject<TileEntityType<EnchantmentCraftingTableTileEntity>> ENCHANTMENT_CRAFTING_TABLE = TILE_ENTITIES.register("enchantment_crafting_table", () -> TileEntityType.Builder.create(EnchantmentCraftingTableTileEntity::new, Blocks.ENCHANTMENT_CRAFTING_TABLE.get()).build(null));
}
