package org.bowserfartgif.cugmod.content.swine;

import dev.ryanhcode.sable.Sable;
import dev.ryanhcode.sable.api.SubLevelAssemblyHelper;
import dev.ryanhcode.sable.api.block.BlockWithSubLevelCollisionCallback;
import dev.ryanhcode.sable.api.physics.callback.BlockSubLevelCollisionCallback;
import dev.ryanhcode.sable.api.physics.handle.RigidBodyHandle;
import dev.ryanhcode.sable.companion.math.BoundingBox3i;
import dev.ryanhcode.sable.companion.math.BoundingBox3ic;
import dev.ryanhcode.sable.sublevel.ServerSubLevel;
import dev.ryanhcode.sable.sublevel.SubLevel;
import dev.ryanhcode.sable.sublevel.system.SubLevelPhysicsSystem;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
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
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.DynamicLoot;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.Tags;
import org.bowserfartgif.cugmod.Cugmod;
import org.bowserfartgif.cugmod.registry.DoodooBlockEntities;
import org.bowserfartgif.cugmod.registry.DoodooItems;
import org.bowserfartgif.cugmod.registry.DoodooSounds;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

import java.util.List;
import java.util.Objects;

public class WretchedSwineBlock extends Block implements EntityBlock, BlockWithSubLevelCollisionCallback, Equipable {

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
    
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context){
        Direction normal = context.getHorizontalDirection().getOpposite();
        return this.defaultBlockState().setValue(FACING, normal);
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
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
    
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
    
    @SuppressWarnings("deprecation")
    @Override
    @NotNull
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return Objects.requireNonNull(DoodooItems.REGISTRY.get(state.getValue(MOOD).getItemId())).getDefaultInstance();
    }
    
    private static boolean isSingleBlock(SubLevel subLevel) {
        BoundingBox3ic bounds = subLevel.getPlot().getBoundingBox();
        return bounds != null && bounds.minX() == bounds.maxX() && bounds.minY() == bounds.maxY() && bounds.minZ() == bounds.maxZ();
    }

    @Override
    public EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.HEAD;
    }

    public enum Mood implements StringRepresentable {
        HAPPY("wretched"),
        HURT("hurt"),
        ANGRY("angry"),
        BURNT("burnt");
        
        private final ResourceLocation itemId;
        
        Mood(String itemName) {
            this.itemId = Cugmod.id(itemName + "_swine");
        }
        
        @Override
        @NotNull
        public String getSerializedName() {
            return this.name().toLowerCase();
        }
        
        public ResourceLocation getItemId() {
            return this.itemId;
        }
    }
    
    public static LootTable.Builder addLoot(LootTable.Builder builder, WretchedSwineBlock block) {
        addLoot(builder, block, Mood.HAPPY);
        addLoot(builder, block, Mood.HURT);
        addLoot(builder, block, Mood.ANGRY);
        addLoot(builder, block, Mood.BURNT);
        return builder;
    }
    
    public static void addLoot(LootTable.Builder builder, WretchedSwineBlock block, Mood mood) {
        builder.withPool(LootPool.lootPool()
                          .setRolls(ConstantValue.exactly(1.0F))
                          .add(swineLoot(mood))
                          .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                          .setProperties(StatePropertiesPredicate.Builder.properties()
                          .hasProperty(WretchedSwineBlock.MOOD, mood)))
        );
    }
    
    private static LootPoolEntryContainer.Builder<?> swineLoot(Mood mood) {
        return LootItem.lootTableItem(Objects.requireNonNull(DoodooItems.REGISTRY.get(mood.getItemId())));
    }
    
}