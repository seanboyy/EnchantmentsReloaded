package io.github.seanboyy.enchantmentsreloaded.objects.tileentity;

import io.github.seanboyy.enchantmentsreloaded.EnchantmentsReloaded;
import io.github.seanboyy.enchantmentsreloaded.registers.TileEntities;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.INameable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@MethodsReturnNonnullByDefault
public class CursebreakerTileEntity extends TileEntity implements INameable, ITickableTileEntity {
    public int tickAccumulator;
    public float f, g, h, i, j, k, l, m, n;
    private static final Random random = new Random();
    private ITextComponent customName;

    public CursebreakerTileEntity() {
        super(TileEntities.CURSEBREAKER.get());
    }

    @Override
    public void tick() {
        k = j;
        m = l;
        PlayerEntity playerEntity = this.world.getClosestPlayer((float)this.pos.getX() + 0.5F, (float)this.pos.getY() + 0.5F, (float)this.pos.getZ() + 0.5F, 3D, false);
        if(playerEntity != null) {
            double d0 = playerEntity.getPosX() - (this.pos.getX() + 0.5);
            double d1 = playerEntity.getPosZ() - (this.pos.getZ() + 0.5);
            n = (float)MathHelper.atan2(d1, d0);
            j += 0.1F;
            if(j < 0.5F || random.nextInt(40) == 0) {
                float f1 = h;
                do {
                    h += (float) (random.nextInt(4) - random.nextInt(4));
                } while (f1 == h);
            }
        }
        else {
            n += 0.02F;
            j -= 0.1F;
        }

        while(l >= Math.PI) l -= (Math.PI * 2F);
        while(l < -Math.PI) l += (Math.PI * 2F);

        while(n >= Math.PI) n -= (Math.PI * 2F);
        while(n < -Math.PI) n += (Math.PI * 2F);

        float f2 = n - l;
        while(f2 >= Math.PI) f2 -= (Math.PI * 2F);
        while(f2 < - Math.PI) f2 += (Math.PI * 2F);
        l += f2 * 0.4F;
        j = MathHelper.clamp(j, 0F, 1F);
        ++tickAccumulator;
        g = f;
        float f_ = (h - f) * 0.4F;
        f_ = MathHelper.clamp(f_, -0.2F, 0.2F);
        i += (f_ - i) * 0.9F;
        f += i;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        if(nbt.contains("CustomName", Constants.NBT.TAG_STRING)){
            this.customName = ITextComponent.Serializer.func_240643_a_(nbt.getString("CustomName"));
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        if(this.hasCustomName()) {
            compound.putString("CustomName", ITextComponent.Serializer.toJson(this.customName));
        }
        return compound;
    }

    @Override
    public ITextComponent getName() {
        return this.customName != null ? this.customName : new TranslationTextComponent(EnchantmentsReloaded.MOD_ID + ".container.cursebreaker");
    }

    public void setCustomName(@Nullable ITextComponent name) {
        this.customName = name;
    }

    public ITextComponent getCustomName() {
        return this.customName;
    }
}
