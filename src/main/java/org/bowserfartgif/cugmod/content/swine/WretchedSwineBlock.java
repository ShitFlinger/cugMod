package org.bowserfartgif.cugmod.content.swine;

import dev.ryanhcode.sable.api.block.BlockWithSubLevelCollisionCallback;
import dev.ryanhcode.sable.api.physics.callback.BlockSubLevelCollisionCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.bowserfartgif.cugmod.registry.DoodooBlockEntities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WretchedSwineBlock extends Block implements EntityBlock, BlockWithSubLevelCollisionCallback {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final EnumProperty<Mood> MOOD = EnumProperty.create("mood", Mood.class);

    public WretchedSwineBlock(Properties props) {
        super(props);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH).setValue(MOOD, Mood.HAPPY)
        );
    }


    //shift ameks place directionr eversed, i guess ill keep this
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context){
        Direction normal = context.getHorizontalDirection().getOpposite();
        if(context.getPlayer() != null && context.getPlayer().isShiftKeyDown()){
            normal = normal.getOpposite();
        }
        return this.defaultBlockState()
                .setValue(FACING, normal);
    }


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING).add(MOOD);
    }
    
    @Override
    public BlockSubLevelCollisionCallback sable$getCallback() {
        return WretchedSwineCallback.INSTANCE;
    }
    
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return DoodooBlockEntities.SWINE.get().create(blockPos, blockState);
    }
    
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            Level level,
            BlockState state,
            BlockEntityType<T> blockEntityType
    ) {
        if (level.isClientSide()) {
            return null;
        }
        return (level0, blockPos, blockState, blockEntity) -> {
            if (blockEntity instanceof WretchedSwineBlockEntity be) {
                be.tick();
            }
        };
    }
    
    public enum Mood implements StringRepresentable {
        HAPPY,
        HURT,
        ANGRY,
        BURNT;
        
        @Override
        @NotNull
        public String getSerializedName() {
            return this.name().toLowerCase();
        }
    }
    
}