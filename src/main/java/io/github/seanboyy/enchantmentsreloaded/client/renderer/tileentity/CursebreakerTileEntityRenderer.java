package io.github.seanboyy.enchantmentsreloaded.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import io.github.seanboyy.enchantmentsreloaded.objects.tileentity.CursebreakerTileEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.client.renderer.entity.model.BookModel;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CursebreakerTileEntityRenderer extends TileEntityRenderer<CursebreakerTileEntity> {
    public static final RenderMaterial TEXTURE_BOOK = new RenderMaterial(AtlasTexture.LOCATION_BLOCKS_TEXTURE, new ResourceLocation("entity/enchanting_table_book"));
    private final BookModel modelBook = new BookModel();

    public CursebreakerTileEntityRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(CursebreakerTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        matrixStackIn.push();
        matrixStackIn.translate(0.5, 0.75, 0.5);
        float f = (float)tileEntityIn.tickAccumulator + partialTicks;
        matrixStackIn.translate(0D, 0.1F + MathHelper.sin(f * 0.1F) * 0.01F, 0D);
        float f1 = tileEntityIn.l - tileEntityIn.m;
        while(f1 >= Math.PI) f1 -= (Math.PI * 2F);
        while(f1 < -Math.PI) f1 += (Math.PI * 2F);
        float f2 = tileEntityIn.m + f1 * partialTicks;
        matrixStackIn.rotate(Vector3f.YP.rotation(-f2));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(80.0F));
        float f3 = MathHelper.lerp(partialTicks, tileEntityIn.g, tileEntityIn.f);
        float f4 = MathHelper.frac(f3 + 0.25F) * 1.6F - 0.3F;
        float f5 = MathHelper.frac(f3 + 0.75F) * 1.6F - 0.3F;
        float f6 = MathHelper.lerp(partialTicks, tileEntityIn.k, tileEntityIn.j);
        this.modelBook.setBookState(f, MathHelper.clamp(f4, 0F, 1F), MathHelper.clamp(f5, 0F, 1F), f6);
        IVertexBuilder vertexBuilder = TEXTURE_BOOK.getBuffer(bufferIn, RenderType::getEntitySolid);
        this.modelBook.renderAll(matrixStackIn, vertexBuilder, combinedLightIn, combinedOverlayIn, 1F, 1F, 1F, 1F);
        matrixStackIn.pop();
    }
}
