package io.github.seanboyy.enchantmentsreloaded.objects.items;

import io.github.seanboyy.enchantmentsreloaded.util.EnchantmentModifierType;
import io.github.seanboyy.enchantmentsreloaded.util.helpers.KeyboardHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class EnchantmentModifierItem extends Item {
    public final EnchantmentModifierType modifierType;
    public EnchantmentModifierItem(Properties properties, EnchantmentModifierType modifierType) {
        super(properties);
        this.modifierType = modifierType;
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return true;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        String informationMessage = "";
        switch(modifierType) {
            case ADD:
                informationMessage = "Use this item in the Enchantment Crafting Table to add a random enchantment that could enchant the supplied enchanted item";
                break;
            case SUBTRACT:
                informationMessage = "Use this item in the Enchantment Crafting Table to remove a random enchantment from the supplied enchanted item, except for curses";
                break;
            case LEVEL:
                informationMessage = "Use this item in the Enchantment Crafting Table to level up a random levelable enchantment on the supplied enchanted item";
                break;
            case RANDOMIZE:
                informationMessage = "Use this item in the Enchantment Crafting Table to completely change the enchantments on supplied item to a random number of enchantments with random levels";
        }
        if(KeyboardHelper.isHoldingShift()){
            tooltip.add(new StringTextComponent(informationMessage));
        } else{
            tooltip.add(new StringTextComponent("Hold \u00A7eSHIFT\u00A7f for more information"));
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
}
