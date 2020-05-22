package io.github.seanboyy.enchantmentsreloaded.objects.blocks;

import io.github.seanboyy.enchantmentsreloaded.inventory.container.CursebreakerContainer;
import io.github.seanboyy.enchantmentsreloaded.objects.tileentity.CursebreakerTileEntity;
import io.github.seanboyy.enchantmentsreloaded.registers.TileEntities;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.INameable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CursebreakerBlock extends ContainerBlock {
    protected static final VoxelShape SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D);

    public CursebreakerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return TileEntities.CURSEBREAKER.get().create();
    }

    @Override
    public boolean isTransparent(BlockState state) {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new CursebreakerTileEntity();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (!worldIn.isRemote) {
            player.openContainer(state.getContainer(worldIn, pos));
        }
        return ActionResultType.SUCCESS;
    }

    @Nullable
    @Override
    public INamedContainerProvider getContainer(BlockState state, World worldIn, BlockPos pos) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if(tileEntity instanceof CursebreakerTileEntity) {
            ITextComponent iTextComponent = ((INameable)tileEntity).getDisplayName();
            return new SimpleNamedContainerProvider((id, playerInventoryIn, playerIn) -> new CursebreakerContainer(id, playerInventoryIn, (CursebreakerTileEntity) tileEntity), iTextComponent);
        } else {
            return null;
        }
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (stack.hasDisplayName()) {
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if (tileEntity instanceof CursebreakerTileEntity) {
                ((CursebreakerTileEntity)tileEntity).setCustomName(stack.getDisplayName());
            }
        }
    }

    @Override
    public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
        return false;
    }
}
