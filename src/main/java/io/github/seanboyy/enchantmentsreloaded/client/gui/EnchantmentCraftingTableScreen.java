package io.github.seanboyy.enchantmentsreloaded.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import io.github.seanboyy.enchantmentsreloaded.EnchantmentsReloaded;
import io.github.seanboyy.enchantmentsreloaded.inventory.container.EnchantmentCraftingTableContainer;
import io.github.seanboyy.enchantmentsreloaded.network.Network;
import io.github.seanboyy.enchantmentsreloaded.network.packet.PacketEnchantmentModified;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.ParametersAreNonnullByDefault;

@OnlyIn(Dist.CLIENT)
public class EnchantmentCraftingTableScreen extends ContainerScreen<EnchantmentCraftingTableContainer> {
    private static final ResourceLocation ENCHANTMENT_CRAFTING_TABLE_GUI_TEXTURE = new ResourceLocation(EnchantmentsReloaded.MOD_ID, "textures/gui/container/enchantment_crafting_table.png");
    private static final int BUTTON_BEGIN_X = 45;
    private static final int BUTTON_BEGIN_Y = 47;
    private static final int BUTTON_WIDTH = 88;
    private static final int BUTTON_HEIGHT = 16;

    public EnchantmentCraftingTableScreen(EnchantmentCraftingTableContainer screenContainer, PlayerInventory inv, ITextComponent titleIn){
        super(screenContainer, inv, titleIn);
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void func_230450_a_(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderHelper.setupGuiFlatDiffuseLighting();
        RenderSystem.color4f(1F, 1F, 1F, 1F);
        this.field_230706_i_.getTextureManager().bindTexture(ENCHANTMENT_CRAFTING_TABLE_GUI_TEXTURE);
        int i = (this.field_230708_k_ - this.xSize) / 2;
        int j = (this.field_230709_l_ - this.ySize) / 2;
        this.func_238474_b_(matrixStack, i, j, 0, 0, this.xSize, this.ySize);
        RenderHelper.setupGui3DDiffuseLighting();
        RenderSystem.color4f(1F, 1F, 1F, 1F);
        int j1 = i + BUTTON_BEGIN_X;
        int k1 = j + BUTTON_BEGIN_Y;
        int k2 = mouseX - j1;
        int l2 = mouseY - k1;
        if(k2 >= 0 && l2 >= 0 && k2 < BUTTON_WIDTH && l2 < BUTTON_HEIGHT) {
            this.func_238474_b_(matrixStack, j1, k1, 0, 198, BUTTON_WIDTH, BUTTON_HEIGHT);
        } else {
            this.func_238474_b_(matrixStack, j1, k1, 0, 166, BUTTON_WIDTH, BUTTON_HEIGHT);
        }
        this.field_230706_i_.fontRenderer.func_238405_a_(matrixStack, "Modify Item", j1 + 17, k1 + 4, 0xF5F5F5);
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void func_230451_b_(MatrixStack matrixStack, int mouseX, int mouseY) {
        this.field_230712_o_.func_238422_b_(matrixStack, this.field_230704_d_, 12.0F, 5.0F, 4210752);
        this.field_230712_o_.func_238422_b_(matrixStack, this.playerInventory.getDisplayName(), 8.0F, (float)(this.ySize - 96 + 2), 4210752);
    }

    @Override
    public boolean func_231044_a_(double mouseX, double mouseY, int p_mouseClicked_5_) {
        int i = (this.field_230708_k_ - this.xSize) / 2;
        int j = (this.field_230709_l_ - this.ySize) / 2;
        double d0 = mouseX - (i + BUTTON_BEGIN_X);
        double d1 = mouseY - (j + BUTTON_BEGIN_Y);
        if(d0 >= 0 && d1 >= 0 && d0 < BUTTON_WIDTH && d1 < BUTTON_HEIGHT) {
            if(this.container.performModification(this.field_230706_i_.player, this.container.getModifierType())) {
                Network.sendPacketToServer(new PacketEnchantmentModified(this.container.getModifierType()));
                return true;
            }
        }
        return super.func_231044_a_(mouseX, mouseY, p_mouseClicked_5_);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void func_230430_a_(MatrixStack matrixStack, int p_render_1_, int p_render_2_, float p_render_3_) {
        this.func_230446_a_(matrixStack);
        super.func_230430_a_(matrixStack, p_render_1_, p_render_2_, p_render_3_);
        this.func_230459_a_(matrixStack, p_render_1_, p_render_2_);
    }
}
