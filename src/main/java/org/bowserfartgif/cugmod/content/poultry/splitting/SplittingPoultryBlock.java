package org.bowserfartgif.cugmod.content.poultry.splitting;

import dev.ryanhcode.sable.api.block.BlockWithSubLevelCollisionCallback;
import dev.ryanhcode.sable.api.physics.callback.BlockSubLevelCollisionCallback;
import dev.ryanhcode.sable.api.physics.handle.RigidBodyHandle;
import dev.ryanhcode.sable.api.sublevel.ServerSubLevelContainer;
import dev.ryanhcode.sable.api.sublevel.SubLevelContainer;
import dev.ryanhcode.sable.companion.math.JOMLConversion;
import dev.ryanhcode.sable.companion.math.Pose3d;
import dev.ryanhcode.sable.sublevel.ServerSubLevel;
import dev.ryanhcode.sable.sublevel.SubLevel;
import dev.ryanhcode.sable.sublevel.plot.LevelPlot;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.bowserfartgif.cugmod.content.poultry.PoultryBlock;
import org.bowserfartgif.cugmod.content.poultry.explosive.ExplosivePoultryCallback;
import org.bowserfartgif.cugmod.registry.DoodooBlockEntities;
import org.bowserfartgif.cugmod.registry.DoodooBlocks;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

public class SplittingPoultryBlock extends PoultryBlock  {
    // lazy shit
    public static final BooleanProperty CLONE = BooleanProperty.create("clone");
    public static final BooleanProperty HAS_CLONED = BooleanProperty.create("has_cloned");

    public SplittingPoultryBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(CLONE, false)
                .setValue(HAS_CLONED, false));
    }

    public static final VoxelShape SHAPE = Block.box(4, 0, 4, 12, 8, 12);

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, CLONE, HAS_CLONED);
    }

    @Override
    public void doSomething(Level level, BlockPos block, Entity entity, Vector3d blockPos, SubLevel subLevel) {
        if (level.getBlockEntity(block) instanceof SplittingPoultryBlockEntity poultryBlockEntity) {
            if (level.getBlockState(block).getValue(HAS_CLONED)) {
                return;
            }

            BlockState withCloned = level.getBlockState(block).setValue(HAS_CLONED, true);
            level.setBlockAndUpdate(block, withCloned);

            ServerSubLevelContainer container = SubLevelContainer.getContainer((ServerLevel) level);
            assert container != null;

            Pose3d pose = new Pose3d();
            
            Vector3d right = JOMLConversion.atLowerCornerOf(withCloned.getValue(FACING).getClockWise().getNormal());
            right.mul(0.5d);
            pose.set(subLevel.logicalPose());
            pose.transformNormal(right);
            
            RigidBodyHandle subLevelHandle = RigidBodyHandle.of((ServerSubLevel) subLevel);
            Vector3d linearVelocity = subLevelHandle.getLinearVelocity(new Vector3d());
            Vector3d angularVelocity = subLevelHandle.getAngularVelocity(new Vector3d());
            Vector3d vector3d = new Vector3d();
            
            for (int i = -1; i <= 1; i += 2) {
                pose.set(subLevel.logicalPose());
                vector3d.zero();
                right.mul(i, vector3d);
                pose.position().add(i*right.x, i*right.y, i*right.z);
                ServerSubLevel cloneSubLevel = (ServerSubLevel) container.allocateNewSubLevel(pose);
                LevelPlot plot = cloneSubLevel.getPlot();
                ChunkPos center = plot.getCenterChunk();
                plot.newEmptyChunk(center);
                plot.getEmbeddedLevelAccessor().setBlock(BlockPos.ZERO, withCloned, 3);
                
                RigidBodyHandle cloneHandle = RigidBodyHandle.of(cloneSubLevel);
                vector3d.mul(10.0d);
                cloneHandle.addLinearAndAngularVelocity(vector3d.add(linearVelocity), angularVelocity);
                
            }
        }
    }
    
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return DoodooBlockEntities.SPLITTING_POULTRY.get().create(blockPos, blockState);
    }
}
