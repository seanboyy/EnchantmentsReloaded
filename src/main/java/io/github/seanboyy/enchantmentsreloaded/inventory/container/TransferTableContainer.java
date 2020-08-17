package io.github.seanboyy.enchantmentsreloaded.inventory.container;

import io.github.seanboyy.enchantmentsreloaded.objects.blocks.TransferTableBlock;
import io.github.seanboyy.enchantmentsreloaded.registers.Blocks;
import io.github.seanboyy.enchantmentsreloaded.registers.Containers;
import io.github.seanboyy.enchantmentsreloaded.util.Config;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
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
import net.minecraftforge.common.util.Constants;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.LinkedHashMap;
import java.util.Map;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TransferTableContainer extends Container {
    private final IInventory tableInventory = new Inventory(3) {
        @Override
        public void markDirty() {
            super.markDirty();
            TransferTableContainer.this.onCraftMatrixChanged(this);
        }
    };
    private final IWorldPosCallable worldPosCallable;

    public TransferTableContainer(final int id, final PlayerInventory playerInventoryIn, final PacketBuffer packetBuffer) {
        this(id, playerInventoryIn, IWorldPosCallable.of(playerInventoryIn.player.world, playerInventoryIn.player.getPosition()));
    }

    public TransferTableContainer(final int id, final PlayerInventory playerInventoryIn, IWorldPosCallable worldPosCallable) {
        super(Containers.TRANSFER_TABLE.get(), id);
        this.worldPosCallable = worldPosCallable;
        this.addSlot(new Slot(this.tableInventory, 0, 44, 17) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return !stack.getEnchantmentTagList().isEmpty();
            }

            @Override
            public int getSlotStackLimit() {
                return 1;
            }
        });
        this.addSlot(new Slot(this.tableInventory, 1, 116, 17) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return stack.getEnchantmentTagList().isEmpty();
            }

            @Override
            public int getSlotStackLimit() {
                return 1;
            }
        });
        this.addSlot(new Slot(this.tableInventory, 2, 80, 29) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return stack.getItem() == Items.END_ROD;
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

    public boolean transferEnchantments(PlayerEntity playerIn) {
        ItemStack source = this.tableInventory.getStackInSlot(0);
        int sourceEnchantmentCount = 0;
        if(!source.isEmpty() && !source.getEnchantmentTagList().isEmpty()) {
            sourceEnchantmentCount = EnchantmentHelper.getEnchantments(source).keySet().size();
        }
        ItemStack dest = this.tableInventory.getStackInSlot(1);
        int destEnchantmentCount = 0;
        if(!source.isEmpty() && !source.getEnchantmentTagList().isEmpty()) {
            destEnchantmentCount = EnchantmentHelper.getEnchantments(dest).keySet().size();
        }
        ItemStack fuel = this.tableInventory.getStackInSlot(2);
        if((fuel.isEmpty() || fuel.getCount() < 1) && !playerIn.abilities.isCreativeMode) return false;
        else if (source.isEmpty() || sourceEnchantmentCount <= 0) return false;
        else if (dest.isEmpty() || destEnchantmentCount > 0) return false;
        else {
            Map<Enchantment, Integer> enchantmentsOnSource = EnchantmentHelper.getEnchantments(source);
            this.worldPosCallable.consume((world, pos) -> {
                Map<Enchantment, Integer> enchantmentsToPutOnDest = new LinkedHashMap<>();
                if(dest.getItem() != Items.BOOK) {
                    enchantmentsOnSource.forEach((enchantment, level) -> {
                        if (enchantment.canApply(dest)) enchantmentsToPutOnDest.put(enchantment, level);
                    });
                }
                else {
                    enchantmentsOnSource.forEach(enchantmentsToPutOnDest::put);
                }
                if(!playerIn.abilities.isCreativeMode) {
                    fuel.shrink(1);
                    if(fuel.isEmpty()) this.tableInventory.setInventorySlotContents(2, ItemStack.EMPTY);
                    Block block = world.getBlockState(pos).getBlock();
                    if(block instanceof TransferTableBlock) {
                        ((TransferTableBlock)block).breakChance += Config.enchantment_transfer_use_break_chance;
                        if(playerIn.getRNG().nextFloat() < ((TransferTableBlock)block).breakChance) {
                            BlockState damageState = TransferTableBlock.damage(world.getBlockState(pos));
                            if(damageState == null) {
                                world.removeBlock(pos, false);
                                world.playEvent(Constants.WorldEvents.ANVIL_DESTROYED_SOUND, pos, 0);
                            } else {
                                world.setBlockState(pos, damageState, 2);
                            }
                        }
                    }
                }
                this.tableInventory.setInventorySlotContents(0, ItemStack.EMPTY);
                if(dest.getItem() != Items.BOOK) {
                    EnchantmentHelper.setEnchantments(enchantmentsToPutOnDest, dest);
                    this.tableInventory.setInventorySlotContents(1, dest);
                }
                else {
                    ItemStack newDest = new ItemStack(Items.ENCHANTED_BOOK);
                    EnchantmentHelper.setEnchantments(enchantmentsToPutOnDest, newDest);
                    this.tableInventory.setInventorySlotContents(1, newDest);
                }
                this.tableInventory.markDirty();
                this.onCraftMatrixChanged(this.tableInventory);
                world.playSound(null, pos, SoundEvents.BLOCK_ANVIL_USE, SoundCategory.BLOCKS, 1F, world.rand.nextFloat() * 0.1F + 0.9F);
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
        return isWithinUsableDistance(this.worldPosCallable, playerIn, Blocks.TRANSFER_TABLE.get()) ||
                isWithinUsableDistance(this.worldPosCallable, playerIn, Blocks.TRANSFER_TABLE_CHIPPED.get()) ||
                isWithinUsableDistance(this.worldPosCallable, playerIn, Blocks.TRANSFER_TABLE_DAMAGED.get());
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
            } else if (itemstack1.getItem() == Items.END_ROD) {
                if(!this.mergeItemStack(itemstack1, 2, 3, true)) return ItemStack.EMPTY;
            } else {
                if(!this.inventorySlots.get(0).getHasStack() && this.inventorySlots.get(0).isItemValid(itemstack1)) {
                    if(itemstack1.hasTag()) {
                        this.inventorySlots.get(0).putStack(itemstack1.split(1));
                    } else if(!itemstack1.isEmpty()) {
                        this.inventorySlots.get(0).putStack(new ItemStack(itemstack1.getItem()));
                        itemstack1.shrink(1);
                    }
                } else if (!this.inventorySlots.get(1).getHasStack() && this.inventorySlots.get(1).isItemValid(itemstack1)) {
                    if(itemstack1.hasTag()) {
                        this.inventorySlots.get(1).putStack(itemstack1.split(1));
                    } else if(!itemstack1.isEmpty()) {
                        this.inventorySlots.get(1).putStack(new ItemStack(itemstack.getItem()));
                        itemstack1.shrink(1);
                    }
                } else return ItemStack.EMPTY;
            }
            if(itemstack1.isEmpty()) slot.putStack(ItemStack.EMPTY);
            else slot.onSlotChanged();
            if(itemstack1.getCount() == itemstack.getCount()) return ItemStack.EMPTY;
            slot.onTake(playerIn, itemstack1);
        }
        return itemstack;
    }
}
