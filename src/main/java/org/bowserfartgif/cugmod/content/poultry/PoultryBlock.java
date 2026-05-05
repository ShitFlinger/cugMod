package org.bowserfartgif.cugmod.content.poultry;

import dev.ryanhcode.sable.Sable;
import dev.ryanhcode.sable.api.SubLevelAssemblyHelper;
import dev.ryanhcode.sable.companion.math.BoundingBox3i;
import dev.ryanhcode.sable.companion.math.BoundingBox3ic;
import dev.ryanhcode.sable.sublevel.ServerSubLevel;
import dev.ryanhcode.sable.sublevel.SubLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.bowserfartgif.cugmod.content.swine.WretchedSwineBlock;
import org.bowserfartgif.cugmod.registry.DoodooBlockEntities;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class PoultryBlock extends Block implements EntityBlock, Equipable {
    
    public static final VoxelShape SHAPE = Block.box(1, 0, 1, 15, 14, 15);
    
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    
    public PoultryBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(
                this.stateDefinition.any().setValue(FACING, Direction.NORTH)
        );
    }
    
    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
    
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context){
        Direction normal = context.getHorizontalDirection().getOpposite();
        return this.defaultBlockState().setValue(FACING, normal);
    }
    
    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if (oldState.getBlock() instanceof WretchedSwineBlock) {
            return;
        }
        if (level instanceof ServerLevel serverLevel) {
            SubLevel subLevel = Sable.HELPER.getContaining(serverLevel, pos);
            if (subLevel != null && isSingleBlock(subLevel)) {
                return;
            }
            
            BoundingBox3i bounds = BoundingBox3i.from(List.of(pos, pos.offset(1, 1, 1)));
            assert bounds != null;
            
            ServerSubLevel serverSubLevel = SubLevelAssemblyHelper.assembleBlocks(
                    serverLevel,
                    pos,
                    List.of(pos),
                    bounds
            );
            level.updateNeighborsAt(pos, oldState.getBlock());
        }
    }
    
    @Override
    public EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.HEAD;
    }
    
    private static boolean isSingleBlock(SubLevel subLevel) {
        BoundingBox3ic bounds = subLevel.getPlot().getBoundingBox();
        return bounds != null && bounds.minX() == bounds.maxX() && bounds.minY() == bounds.maxY() && bounds.minZ() == bounds.maxZ();
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }
    
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return DoodooBlockEntities.POULTRY.get().create(blockPos, blockState);
    }
    
    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof PoultryBlockEntity poultry) {
            poultry.onRemove(level);
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }
}
