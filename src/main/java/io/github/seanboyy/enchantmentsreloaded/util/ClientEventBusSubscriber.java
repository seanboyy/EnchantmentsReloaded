package io.github.seanboyy.enchantmentsreloaded.util;

import io.github.seanboyy.enchantmentsreloaded.EnchantmentsReloaded;
import io.github.seanboyy.enchantmentsreloaded.client.gui.CursebreakerScreen;
import io.github.seanboyy.enchantmentsreloaded.client.gui.EnchantmentCraftingTableScreen;
import io.github.seanboyy.enchantmentsreloaded.registers.Containers;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = EnchantmentsReloaded.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventBusSubscriber {
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        ScreenManager.registerFactory(Containers.CURSEBREAKER.get(), CursebreakerScreen::new);
        ScreenManager.registerFactory(Containers.ENCHANTMENT_CRAFTING_TABLE.get(), EnchantmentCraftingTableScreen::new);
    }
}
