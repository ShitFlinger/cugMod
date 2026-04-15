package org.bowserfartgif.cugmod.content.control.wing;

import com.sun.jna.platform.win32.WinBase;
import dev.ryanhcode.sable.api.block.BlockSubLevelLiftProvider;
import dev.ryanhcode.sable.companion.math.Pose3d;
import dev.ryanhcode.sable.physics.config.dimension_physics.DimensionPhysicsData;
import dev.ryanhcode.sable.sublevel.ServerSubLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import org.bowserfartgif.cugmod.registry.DoodooBlockEntities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;
import org.joml.Vector3dc;

import java.util.Optional;

public class ControlSurfaceBlock extends Block implements EntityBlock, BlockSubLevelLiftProvider {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    private static final VoxelShape SHAPE_Z = Block.box(0, 5, 0, 16, 11, 16);
    public ControlSurfaceBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
        );
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE_Z;
    }

    //propogate da signal bro ..
    @Override
    protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        if(level instanceof Level l)
        {
            int best = 0;
            for (Direction dir : Direction.values()) {
                if (dir == direction) continue;
                BlockPos neighborPos = pos.relative(dir);
                // skip if neighbor is also this block to avoid loop
                //^ hey so claude how the Freak is this possible??
                //i mean it works
                if (l.getBlockState(neighborPos).getBlock() == this) continue;
                best = Math.max(best, l.getSignal(neighborPos, dir));
            }
            return best;
        }
        return 0;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public float sable$getLiftScalar() {
        return 0.0f;
    }

    @Override
    public float sable$getParallelDragScalar() {
        return 1.75f;
    }

    @Override
    public @NotNull Direction sable$getNormal(final BlockState blockState) {
        return blockState.getValue(FACING);
    }

    //the override of death and despair
    @Override
    public void sable$contributeLiftAndDrag(LiftProviderContext ctx, ServerSubLevel subLevel, @NotNull Pose3d localPose, double timeStep, Vector3dc linearVelocity, Vector3dc angularVelocity, Vector3d linearImpulse, Vector3d angularImpulse, @Nullable LiftProviderGroup group) {
        BlockSubLevelLiftProvider.resetVectors();
        Direction facing = subLevel.getLevel().getBlockState(ctx.pos()).getValue(FACING);
        LIFT_NORMAL.set(ctx.dir().x(), ctx.dir().y(), ctx.dir().z());
        LIFT_POS.set(ctx.pos().getX() + 0.5, ctx.pos().getY() + 0.5, ctx.pos().getZ() + 0.5);

        Optional<ControlSurfaceBlockEntity> blockEntity = subLevel.getLevel().getBlockEntity(ctx.pos(), DoodooBlockEntities.CONTROL_SURFACE.get());
        blockEntity.ifPresent(be -> {
            float angle = be.getControlSurfaceAngle();
            switch (facing) {
                case WEST  -> { LIFT_NORMAL.rotateZ(Math.PI / 2); LIFT_NORMAL.rotateZ(-angle); }
                case EAST  -> { LIFT_NORMAL.rotateZ(Math.PI / 2); LIFT_NORMAL.rotateZ(angle);  }
                case NORTH -> { LIFT_NORMAL.rotateX(Math.PI / 2); LIFT_NORMAL.rotateX(angle);  }
                case SOUTH -> { LIFT_NORMAL.rotateX(Math.PI / 2); LIFT_NORMAL.rotateX(-angle); }
            }
        });

        if (localPose != null) {
            localPose.transformNormal(LIFT_NORMAL);
            localPose.transformPosition(LIFT_POS);
        }

        final Pose3d pose = subLevel.logicalPose();
        final double pressure = DimensionPhysicsData.getAirPressure(subLevel.getLevel(), pose.transformPosition(LIFT_POS, TEMP));

        pose.transformPosition(LIFT_POS, TEMP).sub(pose.position());
        LIFT_VELO.set(linearVelocity).add(angularVelocity.cross(TEMP, TEMP));
        pose.transformNormalInverse(LIFT_VELO);

        LIFT_FORCE.zero();

        if (this.sable$getParallelDragScalar() > 0) {
            final double dragStrength = LIFT_NORMAL.dot(LIFT_VELO) * this.sable$getParallelDragScalar() * pressure * timeStep;
            final Vector3d parallelDrag = LIFT_NORMAL.mul(dragStrength, DRAG);
            LIFT_FORCE.add(parallelDrag);

            if (group != null) {
                group.totalDrag().sub(parallelDrag);
                group.dragCenter().fma(Math.abs(dragStrength), LIFT_POS);
                group.totalDragStrength += Math.abs(dragStrength);
            }
        }

        if (this.sable$getDirectionlessDragScalar() > 0) {
            final double dragStrength = this.sable$getDirectionlessDragScalar() * pressure * timeStep;
            final Vector3d directionlessDrag = LIFT_VELO.mul(dragStrength, TEMP);
            LIFT_FORCE.add(directionlessDrag);

            if (group != null) {
                group.totalDrag().sub(directionlessDrag);
                group.dragCenter().fma(directionlessDrag.length(), LIFT_POS);
                group.totalDragStrength += directionlessDrag.length();
            }
        }

        if (this.sable$getLiftScalar() > 0) {
            final double liftStrength = LIFT_VELO.sub(DRAG, TEMP).length() * this.sable$getLiftScalar() * pressure * timeStep;
            final Vector3d lift = LIFT_NORMAL.mul(liftStrength, TEMP);
            LIFT_FORCE.add(lift);

            if (group != null) {
                group.totalLift().sub(lift);
                group.liftCenter().fma(Math.abs(liftStrength), LIFT_POS);
                group.totalLiftStrength += liftStrength;
            }
        }

        linearImpulse.sub(LIFT_FORCE);
        LIFT_POS.sub(subLevel.getMassTracker().getCenterOfMass(), TEMP);
        angularImpulse.sub(TEMP.cross(LIFT_FORCE));
        BlockSubLevelLiftProvider.resetVectors();
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ControlSurfaceBlockEntity(blockPos, blockState);
    }
}