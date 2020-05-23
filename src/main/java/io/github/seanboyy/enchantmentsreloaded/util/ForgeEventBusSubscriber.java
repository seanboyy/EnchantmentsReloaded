package io.github.seanboyy.enchantmentsreloaded.util;

import io.github.seanboyy.enchantmentsreloaded.EnchantmentsReloaded;
import io.github.seanboyy.enchantmentsreloaded.registers.Items;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collection;

@Mod.EventBusSubscriber(modid = EnchantmentsReloaded.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEventBusSubscriber {
    private static final float UNCOMMON_DROP_CHANCE = 0.125F;
    private static final float RARE_DROP_CHANCE = UNCOMMON_DROP_CHANCE;
    private static final float EPIC_DROP_CHANCE = RARE_DROP_CHANCE / 4;

    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        LivingEntity entity = event.getEntityLiving();
        World world = entity.getEntityWorld();
        BlockPos pos = entity.getPosition();
        Collection<ItemEntity> drops = event.getDrops();
        if(entity instanceof MonsterEntity) {
            if(world.rand.nextFloat() < EPIC_DROP_CHANCE) drops.add(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.LEVELER.get())));
            if(world.rand.nextFloat() < RARE_DROP_CHANCE) {
                if(world.rand.nextBoolean()) drops.add(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.ENCHANTER.get())));
                else drops.add(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.DISENCHANTER.get())));
            }
            if(world.rand.nextFloat() < UNCOMMON_DROP_CHANCE) drops.add(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.RANDOMIZER.get())));
        }
    }
}
