package io.github.seanboyy.enchantmentsreloaded.registers;

import io.github.seanboyy.enchantmentsreloaded.EnchantmentsReloaded;
import io.github.seanboyy.enchantmentsreloaded.inventory.container.*;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Containers {
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, EnchantmentsReloaded.MOD_ID);

    public static final RegistryObject<ContainerType<CursebreakerContainer>> CURSEBREAKER = CONTAINERS.register("cursebreaker", () -> IForgeContainerType.create(CursebreakerContainer::new));
    public static final RegistryObject<ContainerType<EnchantmentCraftingTableContainer>> ENCHANTMENT_CRAFTING_TABLE = CONTAINERS.register("enchantment_crafting_table", () -> IForgeContainerType.create(EnchantmentCraftingTableContainer::new));
    public static final RegistryObject<ContainerType<TransferTableContainer>> TRANSFER_TABLE = CONTAINERS.register("transfer_table", () -> IForgeContainerType.create(TransferTableContainer::new));
    public static final RegistryObject<ContainerType<CopyTableContainer>> COPY_TABLE = CONTAINERS.register("copy_table", () -> IForgeContainerType.create(CopyTableContainer::new));
}
