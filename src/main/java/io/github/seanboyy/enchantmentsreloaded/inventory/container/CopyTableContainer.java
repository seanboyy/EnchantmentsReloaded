package io.github.seanboyy.enchantmentsreloaded.inventory.container;

import io.github.seanboyy.enchantmentsreloaded.objects.tileentity.CopyTableTileEntity;
import io.github.seanboyy.enchantmentsreloaded.registers.Blocks;
import io.github.seanboyy.enchantmentsreloaded.registers.Containers;
import mcp.MethodsReturnNonnullByDefault;
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
import net.minecraftforge.common.Tags;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CopyTableContainer extends Container {
    private final IInventory tableInventory = new Inventory(3) {
        public void markDirty() {
            super.markDirty();
            CopyTableContainer.this.onCraftMatrixChanged(this);
        }
    };

    private final IWorldPosCallable worldPosCallable;

    public CopyTableContainer(final int id, final PlayerInventory playerInventoryIn, @Nonnull final CopyTableTileEntity copyTableTileEntity) {
        this(id, playerInventoryIn, IWorldPosCallable.of(copyTableTileEntity.getWorld(), copyTableTileEntity.getPos()));
    }

    public CopyTableContainer(int id, PlayerInventory playerInventoryIn, IWorldPosCallable worldPosCallable) {
        super(Containers.COPY_TABLE.get(), id);
        this.worldPosCallable = worldPosCallable;
        this.addSlot(new Slot(this.tableInventory, 0, 45, 20) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return stack.getTag().contains("Enchantments");
            }

            @Override
            public int getSlotStackLimit() {
                return 1;
            }
        });
        this.addSlot(new Slot(this.tableInventory, 1, 73, 20) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return Tags.Items.INGOTS_NETHERITE.contains(stack.getItem());
            }
        });
        this.addSlot(new Slot(this.tableInventory, 2, 117, 20) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return false;
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

    public CopyTableContainer(int id, PlayerInventory playerInventory, PacketBuffer packetBuffer) {
        this(id, playerInventory, IWorldPosCallable.of(playerInventory.player.world, playerInventory.player.getPosition()));
    }

    public boolean copyItem(PlayerEntity playerIn, ItemStack copyableItem) {
        ItemStack source = this.tableInventory.getStackInSlot(0);
        int sourceEnchantmentCount = 0;
        if(!source.isEmpty() && !source.getEnchantmentTagList().isEmpty()) {
            sourceEnchantmentCount = EnchantmentHelper.getEnchantments(source).keySet().size();
        }
        ItemStack dest = this.tableInventory.getStackInSlot(2);
        ItemStack fuel = this.tableInventory.getStackInSlot(1);
        if((fuel.isEmpty() || fuel.getCount() < 3) && !playerIn.abilities.isCreativeMode) return false;
        else if (source.isEmpty() || sourceEnchantmentCount <= 0) return false;
        else if (!dest.isEmpty()) return false;
        else {
            this.worldPosCallable.consume((world, pos) -> {
                if(!playerIn.abilities.isCreativeMode) {
                    fuel.shrink(3);
                    if(fuel.isEmpty()) this.tableInventory.setInventorySlotContents(1, ItemStack.EMPTY);
                }
                this.tableInventory.setInventorySlotContents(2, source.copy());
                this.tableInventory.markDirty();
                this.onCraftMatrixChanged(this.tableInventory);
                world.playSound(null, pos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1F, world.rand.nextFloat() * 0.1F + 0.9F);
            });
        }
        return true;
    }

    @Override
    public void onContainerClosed(PlayerEntity playerIn) {
        super.onContainerClosed(playerIn);
        this.worldPosCallable.consume((world, pos) -> clearContainer(playerIn, playerIn.world, this.tableInventory));
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(this.worldPosCallable, playerIn, Blocks.COPY_TABLE.get());
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if(slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if(index < 3) {
                if(!this.mergeItemStack(itemstack1, 3, inventorySlots.size(), true)) return ItemStack.EMPTY;
            } else if(itemstack1.getItem() == Items.NETHERITE_INGOT) {
                if(!this.mergeItemStack(itemstack1, 1, 2, true)) return ItemStack.EMPTY;
            } else {
                if(!this.inventorySlots.get(0).getHasStack() && this.inventorySlots.get(0).isItemValid(itemstack1)) {
                    if(itemstack1.hasTag()) {
                        this.inventorySlots.get(0).putStack(itemstack1.split(1));
                    } else if(!itemstack1.isEmpty()) {
                        this.inventorySlots.get(0).putStack(new ItemStack(itemstack1.getItem()));
                    }
                } else if (!this.inventorySlots.get(2).getHasStack() && this.inventorySlots.get(2).isItemValid(itemstack1)) {
                    if(itemstack1.hasTag()) {
                        this.inventorySlots.get(2).putStack(itemstack1.split(1));
                    } else if(!itemstack1.isEmpty()) {
                        this.inventorySlots.get(2).putStack(new ItemStack(itemstack.getItem()));
                        itemstack1.shrink(1);
                    }
                } else return ItemStack.EMPTY;
            }
            if(itemstack1.isEmpty()) slot.putStack(ItemStack.EMPTY);
            else slot.onSlotChanged();
            if(itemstack1.getCount() == itemstack.getCount()) return ItemStack.EMPTY;
            slot.onTake(playerIn, itemstack1);
        }
        return super.transferStackInSlot(playerIn, index);
    }

    public ItemStack getCopyableItem() {
        return this.tableInventory.getStackInSlot(0).isEmpty() ? ItemStack.EMPTY : this.tableInventory.getStackInSlot(0);
    }
}
