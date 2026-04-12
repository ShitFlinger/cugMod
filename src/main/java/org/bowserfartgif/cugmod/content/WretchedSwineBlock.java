package org.bowserfartgif.cugmod.content;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.Nullable;

public class WretchedSwineBlock extends Block {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public WretchedSwineBlock(Properties props) {
        super(props);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH));
    }


    //shift ameks place directionr eversed, i guess ill keep this
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context){
        Direction normal = context.getNearestLookingDirection();
        if(context.getPlayer() != null && context.getPlayer().isShiftKeyDown()){
            normal = normal.getOpposite();
        }
        return this.defaultBlockState()
                .setValue(FACING, normal);
    }


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

}