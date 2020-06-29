package io.github.seanboyy.enchantmentsreloaded.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import io.github.seanboyy.enchantmentsreloaded.EnchantmentsReloaded;
import io.github.seanboyy.enchantmentsreloaded.inventory.container.CursebreakerContainer;
import io.github.seanboyy.enchantmentsreloaded.network.Network;
import io.github.seanboyy.enchantmentsreloaded.network.packet.PacketItemDecursed;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.model.BookModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class CursebreakerScreen extends ContainerScreen<CursebreakerContainer> {
    private static final ResourceLocation CURSEBREAKER_TABLE_GUI_TEXTURE = new ResourceLocation(EnchantmentsReloaded.MOD_ID, "textures/gui/container/cursebreaker.png");
    private static final ResourceLocation CURSEBREAKER_TABLE_BOOK_TEXTURE = new ResourceLocation("textures/entity/enchanting_table_book.png");
    private static final BookModel MODEL_BOOK = new BookModel();
    private final Random random = new Random();
    public int ticks;
    public float flip, oFlip, flipT, flipA, open, oOpen;
    private ItemStack last = ItemStack.EMPTY;
    private static final int BUTTON_BEGIN_X = 62;
    private static final int BUTTON_BEGIN_Y = 47;
    private static final int BUTTON_WIDTH = 88;
    private static final int BUTTON_HEIGHT = 16;

    public CursebreakerScreen(CursebreakerContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    public void func_231023_e_() {
        super.func_231023_e_();
        this.tickBook();
    }

    @Override
    protected void func_230450_a_(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderHelper.setupGuiFlatDiffuseLighting();
        RenderSystem.color4f(1F, 1F, 1F, 1F);
        this.field_230706_i_.getTextureManager().bindTexture(CURSEBREAKER_TABLE_GUI_TEXTURE);
        int i = (this.field_230708_k_ - this.xSize) / 2;
        int j = (this.field_230709_l_ - this.ySize) / 2;
        this.func_238474_b_(matrixStack, i, j, 0, 0, this.xSize, this.ySize);
        RenderSystem.matrixMode(GL11.GL_PROJECTION);
        RenderSystem.pushMatrix();
        RenderSystem.loadIdentity();
        int k = (int)this.field_230706_i_.getMainWindow().getGuiScaleFactor();
        RenderSystem.viewport((this.field_230708_k_ - 320)/ 2 * k, (this.field_230709_l_ - 240) / 2 * k, 320 * k, 240 * k);
        RenderSystem.translatef(-0.34F, 0.23F, 0F);
        RenderSystem.multMatrix(Matrix4f.perspective(90D, 1.3333334F, 9F, 80F));
        RenderSystem.matrixMode(GL11.GL_MODELVIEW);
        matrixStack.push();
        MatrixStack.Entry matrixStack$entry = matrixStack.getLast();
        matrixStack$entry.getMatrix().setIdentity();
        matrixStack$entry.getNormal().setIdentity();
        matrixStack.translate(0D, 3.3, 1984D);
        matrixStack.scale(5F, 5F, 5F);
        matrixStack.rotate(Vector3f.ZP.rotationDegrees(180F));
        matrixStack.rotate(Vector3f.XP.rotationDegrees(20F));
        float f1 = MathHelper.lerp(partialTicks, this.oOpen, this.open);
        matrixStack.translate((1F - f1) * 0.2F, (1F - f1) * 0.1F, (1F - f1) * 0.25F);
        float f2 = -(1F - f1) * 90F - 90F;
        matrixStack.rotate(Vector3f.YP.rotationDegrees(f2));
        matrixStack.rotate(Vector3f.XP.rotationDegrees(180F));
        float f3 = MathHelper.clamp(MathHelper.lerp(partialTicks, this.oFlip, this.flip) + 0.25F, 0F, 1F);
        float f4 = MathHelper.clamp(MathHelper.lerp(partialTicks, this.oFlip, this.flip) + 0.75F, 0F, 1F);
        RenderSystem.enableRescaleNormal();
        MODEL_BOOK.func_228247_a_(0F, f3, f4, f1);
        IRenderTypeBuffer.Impl iRenderTypeBuffer$impl = IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());
        IVertexBuilder iVertexBuilder = iRenderTypeBuffer$impl.getBuffer(MODEL_BOOK.getRenderType(CURSEBREAKER_TABLE_BOOK_TEXTURE));
        MODEL_BOOK.render(matrixStack, iVertexBuilder, 15728880, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
        iRenderTypeBuffer$impl.finish();
        matrixStack.pop();
        RenderSystem.matrixMode(GL11.GL_PROJECTION);
        RenderSystem.viewport(0, 0, this.field_230706_i_.getMainWindow().getFramebufferWidth(), this.field_230706_i_.getMainWindow().getFramebufferHeight());
        RenderSystem.popMatrix();
        RenderSystem.matrixMode(GL11.GL_MODELVIEW);
        RenderHelper.setupGui3DDiffuseLighting();
        RenderSystem.color4f(1F, 1F, 1F, 1F);
        int j1 = i + BUTTON_BEGIN_X;
        int k1 = j + BUTTON_BEGIN_Y;
        this.field_230706_i_.getTextureManager().bindTexture(CURSEBREAKER_TABLE_GUI_TEXTURE);
        RenderSystem.color4f(1F, 1F, 1F, 1F);
        int k2 = mouseX - j1;
        int l2 = mouseY - k1;
        if(k2 >= 0 && l2 >= 0 && k2 < BUTTON_WIDTH && l2 < BUTTON_HEIGHT) {
            this.func_238474_b_(matrixStack, j1, k1, 0, 198, BUTTON_WIDTH, BUTTON_HEIGHT);
        } else {
            this.func_238474_b_(matrixStack, j1, k1, 0, 166, BUTTON_WIDTH, BUTTON_HEIGHT);
        }
        this.field_230706_i_.fontRenderer.func_238405_a_(matrixStack,"Remove Curses", j1 + 6, k1 + 4, 0xF5F5F5);
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void func_230451_b_(MatrixStack p_230451_1_, int p_230451_2_, int p_230451_3_) {
        this.field_230712_o_.func_238422_b_(p_230451_1_, this.field_230704_d_, 12F, 5F, 4210752);
        this.field_230712_o_.func_238422_b_(p_230451_1_, this.playerInventory.getDisplayName(), 8F, (float)(this.ySize - 96 + 2), 4210752);
    }

    @Override
    public boolean func_231044_a_(double mouseX, double mouseY, int p_mouseClicked_5_) {
        int i = (this.field_230708_k_ - this.xSize) / 2;
        int j = (this.field_230709_l_ - this.ySize) / 2;
        double d0 = mouseX - (i + BUTTON_BEGIN_X);
        double d1 = mouseY - (j + BUTTON_BEGIN_Y);
        if(d0 >= 0 && d1 >= 0 && d0 < BUTTON_WIDTH && d1 < BUTTON_HEIGHT && this.container.removeCurses(this.field_230706_i_.player)) {
            Network.sendPacketToServer(new PacketItemDecursed());
            return true;
        }
        return super.func_231044_a_(mouseX, mouseY, p_mouseClicked_5_);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void func_230430_a_(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        this.func_230446_a_(p_230430_1_);
        super.func_230430_a_(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
        this.func_230459_a_(p_230430_1_, p_230430_2_, p_230430_3_);
    }

    private void tickBook() {
        ItemStack itemStack = this.container.getSlot(0).getStack();
        if(!ItemStack.areItemStacksEqual(itemStack, this.last)) {
            this.last = itemStack;
            do {
                this.flipT += this.random.nextInt(4) - this.random.nextInt(4);
            } while (this.flip < this.flipT + 1F && this.flip > this.flipT - 1F);
        }
        ++this.ticks;
        this.oFlip = this.flip;
        this.oOpen = this.open;
        boolean flag = false;
        if(!this.container.getSlot(0).getStack().isEmpty()) {
            flag = true;
        }
        if(flag) this.open += 0.2F;
        else this.open -= 0.2F;

        this.open = MathHelper.clamp(this.open, 0.0F, 1.0F);
        float f1 = (this.flipT - this.flip) * 0.4F;
        f1 = MathHelper.clamp(f1, -0.2F, 0.2F);
        this.flipA += (f1 - this.flipA) * 0.9F;
        this.flip += this.flipA;
    }
}
