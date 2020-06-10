package io.github.seanboyy.enchantmentsreloaded;

import io.github.seanboyy.enchantmentsreloaded.client.renderer.tileentity.CursebreakerTileEntityRenderer;
import io.github.seanboyy.enchantmentsreloaded.client.renderer.tileentity.EnchantmentCraftingTableTileEntityRenderer;
import io.github.seanboyy.enchantmentsreloaded.network.NetworkHandler;
import io.github.seanboyy.enchantmentsreloaded.registers.Blocks;
import io.github.seanboyy.enchantmentsreloaded.registers.Containers;
import io.github.seanboyy.enchantmentsreloaded.registers.Items;
import io.github.seanboyy.enchantmentsreloaded.registers.TileEntities;
import io.github.seanboyy.enchantmentsreloaded.util.Config;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.TableLootEntry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("enchantmentsreloaded")
@Mod.EventBusSubscriber(modid = EnchantmentsReloaded.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EnchantmentsReloaded {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "enchantmentsreloaded";

    public EnchantmentsReloaded() {
        LOGGER.info("Starting up Enchantments Reloaded");
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_SPEC);
        LOGGER.info("\tConfig loaded");
        NetworkHandler networkHandler = new NetworkHandler();
        commonStart(modEventBus, networkHandler);
        clientStart(modEventBus, networkHandler);
        LOGGER.info("\tPacket handlers registered");
        Items.ITEMS.register(modEventBus);
        LOGGER.info("\tItems registered");
        Blocks.BLOCKS.register(modEventBus);
        LOGGER.info("\tBlocks registered");
        TileEntities.TILE_ENTITIES.register(modEventBus);
        LOGGER.info("\tTile entities registered");
        Containers.CONTAINERS.register(modEventBus);
        LOGGER.info("\tContainers registered");
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public static void onRegisterItems(final RegistryEvent.Register<Item> event){
        final IForgeRegistry<Item> registry = event.getRegistry();
        Blocks.BLOCKS.getEntries().stream().map(RegistryObject::get).forEach(block -> {
            final Item.Properties properties = new Item.Properties().group(ItemGroup.DECORATIONS);
            final BlockItem blockItem = new BlockItem(block, properties);
            blockItem.setRegistryName(block.getRegistryName());
            registry.register(blockItem);
        });
        LOGGER.info("\tBlockItems registered");
    }

    @SubscribeEvent
    public static void onRegisterTileEntities(final RegistryEvent.Register<TileEntityType<?>> event) {
        ClientRegistry.bindTileEntityRenderer(TileEntities.CURSEBREAKER.get(), CursebreakerTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(TileEntities.ENCHANTMENT_CRAFTING_TABLE.get(), EnchantmentCraftingTableTileEntityRenderer::new);
    }

    @SubscribeEvent
    public static void onModConfig(final ModConfig.ModConfigEvent configEvent) {
        if(configEvent.getConfig().getSpec() == Config.CLIENT_SPEC) Config.bakeConfig();
    }

    private static void commonStart(IEventBus modEventBus, NetworkHandler networkHandler) {
        modEventBus.addListener(EventPriority.NORMAL, false, FMLCommonSetupEvent.class, event -> networkHandler.createServerPacketHandler());
    }

    private static void clientStart(IEventBus modEventBus, NetworkHandler networkHandler) {
        modEventBus.addListener(EventPriority.NORMAL, false, FMLClientSetupEvent.class, event -> networkHandler.createClientPacketHandler());
    }

    public static final class Tags{
        public static final Tag<Item> ENCHANTMENT_MODIFIERS = new ItemTags.Wrapper(new ResourceLocation(EnchantmentsReloaded.MOD_ID, "enchantment_modifiers"));
    }
}
