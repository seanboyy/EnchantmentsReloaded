package io.github.seanboyy.enchantmentsreloaded.inventory.container;

import io.github.seanboyy.enchantmentsreloaded.EnchantmentsReloaded;
import io.github.seanboyy.enchantmentsreloaded.objects.tileentity.EnchantmentCraftingTableTileEntity;
import io.github.seanboyy.enchantmentsreloaded.registers.Blocks;
import io.github.seanboyy.enchantmentsreloaded.registers.Containers;
import io.github.seanboyy.enchantmentsreloaded.registers.Items;
import io.github.seanboyy.enchantmentsreloaded.util.EnchantmentModifierType;
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
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class EnchantmentCraftingTableContainer extends Container {
    private final Random randomizer = new Random();
    private final IInventory tableInventory = new Inventory(2) {
        @Override
        public void markDirty() {
            super.markDirty();
            EnchantmentCraftingTableContainer.this.onCraftMatrixChanged(this);
        }
    };
    private final IWorldPosCallable worldPosCallable;

    public EnchantmentCraftingTableContainer(final int id, final PlayerInventory playerInventoryIn, @Nonnull final EnchantmentCraftingTableTileEntity enchantmentCraftingTableTileEntity) {
        this(id, playerInventoryIn, IWorldPosCallable.of(enchantmentCraftingTableTileEntity.getWorld(), enchantmentCraftingTableTileEntity.getPos()));
    }

    public EnchantmentCraftingTableContainer(int id, PlayerInventory playerInventoryIn, IWorldPosCallable worldPosCallable) {
        super(Containers.ENCHANTMENT_CRAFTING_TABLE.get(), id);
        this.worldPosCallable = worldPosCallable;
        this.addSlot(new Slot(this.tableInventory, 0, 70, 20) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return true;
            }

            @Override
            public int getSlotStackLimit() {
                return 1;
            }
        });
        this.addSlot(new Slot(this.tableInventory, 1, 90, 20) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return EnchantmentsReloaded.Tags.ENCHANTMENT_MODIFIERS.func_230235_a_(stack.getItem());
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

    public EnchantmentCraftingTableContainer(int id, PlayerInventory playerInventoryIn, PacketBuffer data) {
        this(id, playerInventoryIn, IWorldPosCallable.of(playerInventoryIn.player.world, playerInventoryIn.player.func_233580_cy_()));
    }

    public EnchantmentModifierType getModifierType() {
        ItemStack modifierItem = tableInventory.getStackInSlot(1);
        if (!modifierItem.isEmpty()) {
            if(modifierItem.getItem() == Items.RANDOMIZER.get()) return EnchantmentModifierType.RANDOMIZE;
            if(modifierItem.getItem() == Items.DISENCHANTER.get()) return EnchantmentModifierType.SUBTRACT;
            if(modifierItem.getItem() == Items.ENCHANTER.get()) return EnchantmentModifierType.ADD;
            if(modifierItem.getItem() == Items.LEVELER.get()) return EnchantmentModifierType.LEVEL;
        }
        return EnchantmentModifierType.NONE;
    }

    public static boolean enchantmentCanApplyAgainstCollection(Collection<Enchantment> enchantments, Enchantment enchantment) {
        for(Enchantment ench : enchantments) {
            if(!enchantment.canApplyTogether(ench)) return false;
        }
        return true;
    }

    public boolean performModification(PlayerEntity playerIn, EnchantmentModifierType modifierTypeIn) {
        ItemStack enchantedItem = this.tableInventory.getStackInSlot(0);
        ItemStack fuel = this.tableInventory.getStackInSlot(1);
        Map<Enchantment, Integer> enchantmentsOnItem = EnchantmentHelper.getEnchantments(enchantedItem);
        if(enchantmentsOnItem.isEmpty() || ((fuel.isEmpty() || fuel.getCount() < 1) && !playerIn.abilities.isCreativeMode)) return false;
        this.worldPosCallable.consume((world, pos) -> {
            List<Enchantment> enchantments = new ArrayList<>();
            switch(modifierTypeIn) {
                case ADD:
                    ForgeRegistries.ENCHANTMENTS.getValues().stream().filter(enchantment -> enchantment.canApply(enchantedItem)).filter(enchantment -> enchantmentCanApplyAgainstCollection(enchantmentsOnItem.keySet(), enchantment)).forEach(enchantments::add);
                    if (enchantments.size() > 0) {
                        int randomEnchantment = randomizer.nextInt(enchantments.size());
                        int randomLevel = MathHelper.clamp(randomizer.nextInt(enchantments.get(randomEnchantment).getMaxLevel() + 1), enchantments.get(randomEnchantment).getMinLevel(), enchantments.get(randomEnchantment).getMaxLevel());
                        enchantmentsOnItem.put(enchantments.get(randomEnchantment), randomLevel);
                    }
                    break;
                case SUBTRACT:
                    int rand = randomizer.nextInt(enchantmentsOnItem.size());
                    List<Enchantment> orderedListEnchantmentsOnItem = new ArrayList<>(enchantmentsOnItem.keySet());
                    if(orderedListEnchantmentsOnItem.size() > 1) {
                        while (orderedListEnchantmentsOnItem.get(rand).isCurse())
                            rand = randomizer.nextInt(enchantmentsOnItem.size());
                        enchantmentsOnItem.remove(orderedListEnchantmentsOnItem.get(rand));
                    } else if (!orderedListEnchantmentsOnItem.get(0).isCurse()) {
                        enchantmentsOnItem.clear();
                    }
                    break;
                case RANDOMIZE:
                    ForgeRegistries.ENCHANTMENTS.getValues().stream().filter(enchantment -> enchantment.canApply(enchantedItem)).forEach(enchantments::add);
                    enchantmentsOnItem.clear();
                    if(enchantments.size() > 0) {
                        int randomEnchantmentCount = MathHelper.clamp(randomizer.nextInt(enchantments.size()), 1, randomizer.nextFloat() < 0.2F ? enchantments.size() : 3);
                        List<Integer> numbers = new ArrayList<>();
                        for (int i = 0; i < enchantments.size(); ++i) {
                            numbers.add(i);
                        }
                        Collections.shuffle(numbers);
                        List<Enchantment> proposedEnchantments = new ArrayList<>();
                        List<Integer> proposedLevels = new ArrayList<>();
                        for (int i = 0; i < randomEnchantmentCount; ++i) {
                            Enchantment proposedEnchantment = enchantments.get(numbers.get(i));
                            if (enchantmentCanApplyAgainstCollection(proposedEnchantments, proposedEnchantment)) {
                                proposedEnchantments.add(proposedEnchantment);
                                proposedLevels.add(MathHelper.clamp(randomizer.nextInt(proposedEnchantment.getMaxLevel() + 1), 1, proposedEnchantment.getMaxLevel()));
                            }
                        }
                        for (int i = 0; i < proposedEnchantments.size(); ++i) {
                            enchantmentsOnItem.put(proposedEnchantments.get(i), proposedLevels.get(i));
                        }
                    }
                    break;
                case LEVEL:
                    List<Pair<Enchantment, Integer>> levelableEnchantmentsOnItem = new ArrayList<>();
                    enchantmentsOnItem.forEach((enchantment, level) -> {
                        if(level < enchantment.getMaxLevel()) levelableEnchantmentsOnItem.add(Pair.of(enchantment, level));
                    });
                    if(!levelableEnchantmentsOnItem.isEmpty()) {
                        int randomEnchantmentToLevelUp = randomizer.nextInt(levelableEnchantmentsOnItem.size());
                        int randomAmountToLevelEnchantmentUpBy = randomizer.nextFloat() > 0.75F ? 2 : 1;
                        int newEnchantmentLevel = MathHelper.clamp(
                                levelableEnchantmentsOnItem.get(randomEnchantmentToLevelUp).getRight() + randomAmountToLevelEnchantmentUpBy,
                                levelableEnchantmentsOnItem.get(randomEnchantmentToLevelUp).getLeft().getMinLevel(),
                                levelableEnchantmentsOnItem.get(randomEnchantmentToLevelUp).getLeft().getMaxLevel());
                        Pair<Enchantment, Integer> newEnchantmentWithNewLevel = Pair.of(levelableEnchantmentsOnItem.get(randomEnchantmentToLevelUp).getLeft(), newEnchantmentLevel);
                        enchantmentsOnItem.remove(newEnchantmentWithNewLevel.getLeft());
                        enchantmentsOnItem.put(newEnchantmentWithNewLevel.getLeft(), newEnchantmentWithNewLevel.getRight());
                    }
                    break;
            }
            EnchantmentHelper.setEnchantments(enchantmentsOnItem, enchantedItem);
            if(!playerIn.abilities.isCreativeMode) {
                fuel.shrink(1);
                if(fuel.isEmpty()) this.tableInventory.setInventorySlotContents(1, ItemStack.EMPTY);
            }
            this.tableInventory.setInventorySlotContents(0, enchantedItem);
            this.tableInventory.markDirty();
            this.onCraftMatrixChanged(this.tableInventory);
            world.playSound(null, pos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1F, world.rand.nextFloat() * 0.1F + 0.9F);
        });
        return true;
    }

    @Override
    public void onContainerClosed(PlayerEntity playerIn) {
        super.onContainerClosed(playerIn);
        this.worldPosCallable.consume(((world, pos) -> clearContainer(playerIn, playerIn.world, this.tableInventory)));
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(this.worldPosCallable, playerIn, Blocks.ENCHANTMENT_CRAFTING_TABLE.get());
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if(slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if(index < 2) {
                if(!this.mergeItemStack(itemstack1, 2, 38, true)) return ItemStack.EMPTY;
            } else if (itemstack1.getItem() == Items.RANDOMIZER.get() || itemstack1.getItem() == Items.ENCHANTER.get() || itemstack1.getItem() == Items.DISENCHANTER.get() || itemstack1.getItem() == Items.LEVELER.get()) {
                if(!this.mergeItemStack(itemstack1, 1, 2, true)) return ItemStack.EMPTY;
            } else {
                if(this.inventorySlots.get(0).getHasStack() || !this.inventorySlots.get(0).isItemValid(itemstack1)) return ItemStack.EMPTY;
                if(itemstack1.hasTag()) this.inventorySlots.get(0).putStack(itemstack1.split(1));
                else if (!itemstack1.isEmpty()) {
                    this.inventorySlots.get(0).putStack(new ItemStack(itemstack1.getItem()));
                    itemstack1.shrink(1);
                }
            }

            if(itemstack1.isEmpty()) slot.putStack(ItemStack.EMPTY);
            else slot.onSlotChanged();

            if (itemstack1.getCount() == itemstack.getCount()) return ItemStack.EMPTY;

            slot.onTake(playerIn, itemstack1);
        }
        return itemstack;
    }
}
