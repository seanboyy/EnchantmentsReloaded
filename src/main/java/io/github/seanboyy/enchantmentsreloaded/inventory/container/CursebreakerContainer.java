package io.github.seanboyy.enchantmentsreloaded.inventory.container;

import io.github.seanboyy.enchantmentsreloaded.objects.tileentity.CursebreakerTileEntity;
import io.github.seanboyy.enchantmentsreloaded.registers.Blocks;
import io.github.seanboyy.enchantmentsreloaded.registers.Containers;
import io.github.seanboyy.enchantmentsreloaded.util.Config;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.Tags;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CursebreakerContainer extends Container {
    private final IInventory tableInventory = new Inventory(2) {
        public void markDirty() {
            super.markDirty();
            CursebreakerContainer.this.onCraftMatrixChanged(this);
        }
    };
    private final IWorldPosCallable worldPosCallable;

    public CursebreakerContainer(final int id, final PlayerInventory playerInventoryIn, @Nonnull final CursebreakerTileEntity cursebreakerTileEntity) {
        this(id, playerInventoryIn, IWorldPosCallable.of(cursebreakerTileEntity.getWorld(), cursebreakerTileEntity.getPos()));
    }

    public CursebreakerContainer(int id, PlayerInventory playerInventoryIn, IWorldPosCallable worldPosCallable) {
        super(Containers.CURSEBREAKER.get(), id);
        this.worldPosCallable = worldPosCallable;
        this.addSlot(new Slot(this.tableInventory, 0, 15, 47) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return true;
            }

            @Override
            public int getSlotStackLimit() {
                return 1;
            }
        });
        this.addSlot(new Slot(this.tableInventory, 1, 35, 47) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return Tags.Items.NETHER_STARS.func_230235_a_(stack.getItem());
            }
        });
        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventoryIn, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventoryIn, k, 8 + k * 18, 142));
        }
    }

    public CursebreakerContainer(int id, PlayerInventory playerInventory, PacketBuffer packetBuffer) {
        this(id, playerInventory, IWorldPosCallable.of(playerInventory.player.world, playerInventory.player.func_233580_cy_()));
    }

    public boolean removeCurses(PlayerEntity playerIn) {
        ItemStack cursedItem = this.tableInventory.getStackInSlot(0);
        int curseCount = 0;
        if(!cursedItem.isEmpty()) {
            if(!cursedItem.getEnchantmentTagList().isEmpty()) {
                Map<Enchantment, Integer> cursedItemEnchantments = EnchantmentHelper.getEnchantments(cursedItem);
                curseCount = (int) cursedItemEnchantments.keySet().stream().filter(Enchantment::isCurse).count();
            }
        }
        ItemStack fuel = this.tableInventory.getStackInSlot(1);
        if((fuel.isEmpty() || fuel.getCount() < 1) && !playerIn.abilities.isCreativeMode) {
            return false;
        } else if (cursedItem.isEmpty() || curseCount <= 0 || (cursedItem.getDamage() > cursedItem.getMaxDamage() * curseCount * Config.curse_removal_durability_factor) && !playerIn.abilities.isCreativeMode){
            return false;
        } else {
            int finalCurseCount = curseCount;
            this.worldPosCallable.consume((world, pos) -> {
                Map<Enchantment, Integer> changedEnchantments = EnchantmentHelper.getEnchantments(cursedItem);
                EnchantmentHelper.getEnchantments(cursedItem).forEach((enchantment, level) -> {
                    if(enchantment.isCurse()) {
                        changedEnchantments.remove(enchantment);
                    }
                });
                ItemStack decursedItem = new ItemStack(cursedItem.getItem(), cursedItem.getCount());
                EnchantmentHelper.setEnchantments(changedEnchantments, decursedItem);
                if(!playerIn.abilities.isCreativeMode) {
                    float newDamage = cursedItem.getMaxDamage() * (float)Config.curse_removal_durability_factor * finalCurseCount;
                    int damageChangeResult = Config.curse_removal_durability_rounding_style ? MathHelper.ceil(newDamage) : MathHelper.floor(newDamage);
                    decursedItem.setDamage(damageChangeResult);
                    fuel.shrink(1);
                    if(fuel.isEmpty()) {
                        this.tableInventory.setInventorySlotContents(1, ItemStack.EMPTY);
                    }
                }
                else {
                    decursedItem.setDamage(cursedItem.getDamage());
                }
                this.tableInventory.setInventorySlotContents(0, decursedItem);
                this.tableInventory.markDirty();
                this.onCraftMatrixChanged(this.tableInventory);
                world.playSound(null, pos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1F, world.rand.nextFloat() * 0.1F + 0.9F);
            });
            return true;
        }

    }

    @Override
    public void onContainerClosed(PlayerEntity playerIn) {
        super.onContainerClosed(playerIn);
        this.worldPosCallable.consume((world, pos) -> clearContainer(playerIn, playerIn.world, this.tableInventory));
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(this.worldPosCallable, playerIn, Blocks.CURSEBREAKER.get());
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if(slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if(index < 2) {
                if(!this.mergeItemStack(itemstack1, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (itemstack1.getItem() == Items.NETHER_STAR) {
                if(!this.mergeItemStack(itemstack1, 1, 2, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if(this.inventorySlots.get(0).getHasStack() || !this.inventorySlots.get(0).isItemValid(itemstack1)) {
                    return ItemStack.EMPTY;
                }

                if(itemstack1.hasTag()) {
                    this.inventorySlots.get(0).putStack(itemstack1.split(1));
                } else if (!itemstack1.isEmpty()) {
                    this.inventorySlots.get(0).putStack(new ItemStack(itemstack1.getItem()));
                    itemstack1.shrink(1);
                }
            }

            if(itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }
        return itemstack;
    }
}
