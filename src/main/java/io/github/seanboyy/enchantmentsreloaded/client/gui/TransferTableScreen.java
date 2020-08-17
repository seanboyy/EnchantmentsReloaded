package io.github.seanboyy.enchantmentsreloaded.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.seanboyy.enchantmentsreloaded.EnchantmentsReloaded;
import io.github.seanboyy.enchantmentsreloaded.inventory.container.TransferTableContainer;
import io.github.seanboyy.enchantmentsreloaded.network.Network;
import io.github.seanboyy.enchantmentsreloaded.network.packet.PacketEnchantmentTransfer;
import io.github.seanboyy.enchantmentsreloaded.network.packet.PacketEnchantmentTransferRequest;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TransferTableScreen extends ContainerScreen<TransferTableContainer> {
    private static final ResourceLocation TRANSFER_TABLE_GUI_TEXTURE = new ResourceLocation(EnchantmentsReloaded.MOD_ID, "textures/gui/container/transfer_table.png");
    private static final int BUTTON_BEGIN_X = 44;
    private static final int BUTTON_BEGIN_Y = 52;
    private static final int BUTTON_WIDTH = 88;
    private static final int BUTTON_HEIGHT = 16;

    public TransferTableScreen(TransferTableContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.font.drawString(this.title.getFormattedText(), 12.0F, 5.0F, 4210752);
        this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8.0F, (float)(this.ySize - 96 + 2), 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        RenderHelper.setupGuiFlatDiffuseLighting();
        RenderSystem.color4f(1F, 1F, 1F, 1F);
        this.minecraft.getTextureManager().bindTexture(TRANSFER_TABLE_GUI_TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.blit(i, j, 0, 0, this.xSize, this.ySize);
        int j1 = i + BUTTON_BEGIN_X;
        int k1 = j + BUTTON_BEGIN_Y;
        this.setBlitOffset(0);
        int k2 = mouseX - j1;
        int l2 = mouseY - k1;
        if(k2 >= 0 && l2 >= 0 && k2 < BUTTON_WIDTH && l2 < BUTTON_HEIGHT) {
            this.blit(j1, k1, 0, 198, BUTTON_WIDTH, BUTTON_HEIGHT);
        } else {
            this.blit(j1, k1, 0, 166, BUTTON_WIDTH, BUTTON_HEIGHT);
        }
        this.minecraft.fontRenderer.drawString("Transfer", j1 + 18, k1 + 4, 0xF5F5F5);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int p_mouseClicked_5_) {
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        double d0 = mouseX - (i + BUTTON_BEGIN_X);
        double d1 = mouseY - (j + BUTTON_BEGIN_Y);
        if(d0 >= 0 && d1 >= 0 && d0 < BUTTON_WIDTH && d1 < BUTTON_HEIGHT && this.container.transferEnchantments(this.minecraft.player)) {
            Network.sendPacketToServer(new PacketEnchantmentTransferRequest());
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, p_mouseClicked_5_);
    }

    @Override
    public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
        this.renderBackground();
        super.render(p_render_1_, p_render_2_, p_render_3_);
        this.renderHoveredToolTip(p_render_1_, p_render_2_);
    }
}
