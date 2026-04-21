package org.bowserfartgif.cugmod.content.control.wing;

import dev.ryanhcode.sable.api.block.BlockSubLevelLiftProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CamberedWingBlock extends Block implements BlockSubLevelLiftProvider {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context){
        Direction normal = context.getNearestLookingDirection();
        if(context.getPlayer() != null && context.getPlayer().isShiftKeyDown()){
            normal = normal.getOpposite();
        }
        return this.defaultBlockState()
                .setValue(FACING, normal);
    }

    private static final VoxelShape SHAPEY = Block.box(0.0, 5, 0.0, 16, 11, 16);
    private static final VoxelShape SHAPEX = Block.box(5.0, 0, 0.0, 11, 16, 16);
    private static final VoxelShape SHAPEZ = Block.box(0.0, 0, 5, 16, 16, 11);

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        switch (state.getValue(FACING)) {
            case EAST -> {return SHAPEX;}
            case WEST -> {return SHAPEX;}
            case NORTH -> {return SHAPEZ;}
            case SOUTH -> {return SHAPEZ;}
            case DOWN -> {return SHAPEY;}
            case UP -> {return SHAPEY;}
            case null -> {return SHAPEY;}
        }
    }

    public CamberedWingBlock(Properties properties){
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
        );
    }

    @Override
    public float sable$getLiftScalar() {
        return 0.6f;
    }

    @Override
    public float sable$getParallelDragScalar() {
        return 1.75f;
    }

    @Override
    public @NotNull Direction sable$getNormal(final BlockState blockState) {
        return blockState.getValue(FACING).getOpposite();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

}
