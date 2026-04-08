package org.bowserfartgif.doodoo_mod.content.propulsion;

import dev.ryanhcode.sable.api.block.BlockEntitySubLevelActor;
import dev.ryanhcode.sable.api.block.BlockSubLevelAssemblyListener;
import dev.ryanhcode.sable.api.physics.force.ForceGroups;
import dev.ryanhcode.sable.api.physics.force.QueuedForceGroup;
import dev.ryanhcode.sable.api.physics.handle.RigidBodyHandle;
import dev.ryanhcode.sable.companion.math.JOMLConversion;
import dev.ryanhcode.sable.sublevel.ServerSubLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.bowserfartgif.doodoo_mod.registry.DoodooBlockEntities;
import org.joml.Vector3d;

public class ThrusterBlockEntity extends BlockEntity
        implements BlockEntitySubLevelActor, BlockSubLevelAssemblyListener {

    public static final double DEFAULT_THRUST = 150.0;

    private double thrust = DEFAULT_THRUST;

    public ThrusterBlockEntity(BlockPos pos, BlockState state) {
        super(DoodooBlockEntities.THRUSTER.get(), pos, state);
    }



    @Override
    public void sable$physicsTick(ServerSubLevel subLevel, RigidBodyHandle handle, double timeStep) {
        int redstoneLevel = 0;
        if (level != null) {
            redstoneLevel = level.getBestNeighborSignal(getBlockPos());
        }

        double finalThrust = thrust * (redstoneLevel / 15.0);

        Direction facing = getBlockState().getValue(ThrusterBlock.FACING);

        Vector3d normal = JOMLConversion.atLowerCornerOf(facing.getNormal());

        Vector3d force = new Vector3d(normal)
                .mul(finalThrust * timeStep);

        QueuedForceGroup forceGroup = subLevel.getOrCreateQueuedForceGroup(ForceGroups.PROPULSION.get());

        forceGroup.applyAndRecordPointForce(
                JOMLConversion.atCenterOf(getBlockPos()),
                force
        );
    }

    @Override
    public void afterMove(ServerLevel oldLevel, ServerLevel newLevel, BlockState state,
                          BlockPos oldPos, BlockPos newPos) {
    }
}