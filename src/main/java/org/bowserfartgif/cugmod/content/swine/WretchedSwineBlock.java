package org.bowserfartgif.cugmod.content.swine;

import dev.ryanhcode.sable.api.SubLevelAssemblyHelper;
import dev.ryanhcode.sable.api.block.BlockWithSubLevelCollisionCallback;
import dev.ryanhcode.sable.api.physics.callback.BlockSubLevelCollisionCallback;
import dev.ryanhcode.sable.companion.math.BoundingBox3i;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.Tags;
import org.bowserfartgif.cugmod.registry.DoodooBlockEntities;
import org.bowserfartgif.cugmod.registry.DoodooBlocks;
import org.bowserfartgif.cugmod.registry.DoodooItems;
import org.bowserfartgif.cugmod.registry.DoodooSounds;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WretchedSwineBlock extends Block implements EntityBlock, BlockWithSubLevelCollisionCallback {

    public static final VoxelShape SHAPE = Block.box(1, 0, 1, 15, 14, 15);
    
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final EnumProperty<Mood> MOOD = EnumProperty.create("mood", Mood.class);
    public static final BooleanProperty HAT = BooleanProperty.create("hat");

    public WretchedSwineBlock(Properties props) {
        super(props);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH).setValue(MOOD, Mood.HAPPY).setValue(HAT, false)
        );
    }
    
    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
    
    //shift ameks place directionr eversed, i guess ill keep this
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context){
        Direction normal = context.getHorizontalDirection().getOpposite();
        if(context.getPlayer() != null && context.getPlayer().isShiftKeyDown()){
            normal = normal.getOpposite();
        }
        boolean hasHat = context.getLevel().random.nextFloat() <= 0.1f;
        if (context.getLevel().isClientSide()) {
            hasHat = false;
        }
        return this.defaultBlockState()
                .setValue(FACING, normal).setValue(HAT, hasHat);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (stack.is(Tags.Items.MUSIC_DISCS)) {
            if (!level.isClientSide) {
                stack.consume(1, player);
                player.addItem(DoodooItems.WRETCHED_DISC.get().getDefaultInstance());

            }
            level.playSound(player, pos, DoodooSounds.MONCH.get(), SoundSource.BLOCKS, 1, 1);

            return ItemInteractionResult.SUCCESS;
        }

        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if (oldState.getBlock() instanceof WretchedSwineBlock) {
            return;
        }
        if (level instanceof ServerLevel serverLevel) {
            BoundingBox3i bounds = BoundingBox3i.from(List.of(pos, pos.offset(1, 1, 1)));
            assert bounds != null;
            
            SubLevelAssemblyHelper.assembleBlocks(serverLevel, pos, List.of(pos), bounds);
            level.updateNeighborsAt(pos, state.getBlock());
        }
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING).add(MOOD).add(HAT);
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