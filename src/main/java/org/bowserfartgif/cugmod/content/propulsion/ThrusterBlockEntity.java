package org.bowserfartgif.cugmod.content.propulsion;

import dev.ryanhcode.sable.api.block.BlockEntitySubLevelActor;
import dev.ryanhcode.sable.api.block.BlockSubLevelAssemblyListener;
import dev.ryanhcode.sable.api.block.propeller.BlockEntityPropeller;
import dev.ryanhcode.sable.api.block.propeller.BlockEntitySubLevelPropellerActor;
import dev.ryanhcode.sable.api.physics.force.ForceGroups;
import dev.ryanhcode.sable.api.physics.force.QueuedForceGroup;
import dev.ryanhcode.sable.api.physics.handle.RigidBodyHandle;
import dev.ryanhcode.sable.companion.math.JOMLConversion;
import dev.ryanhcode.sable.sublevel.ServerSubLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.bowserfartgif.cugmod.Config;
import org.bowserfartgif.cugmod.registry.DoodooBlockEntities;
import org.joml.Vector3d;

public class ThrusterBlockEntity extends BlockEntity
        implements BlockEntitySubLevelPropellerActor, BlockSubLevelAssemblyListener, BlockEntityPropeller {

    Direction facing = getBlockState().getValue(ThrusterBlock.FACING);

    private double thrust = 50;

    private double airflow = 200;

    @Override
    public BlockEntityPropeller getPropeller() {
        return this;
    }

    public ThrusterBlockEntity(BlockPos pos, BlockState state) {
        super(DoodooBlockEntities.THRUSTER.get(), pos, state);
    }

    @Override
    public Direction getBlockDirection() {
        return facing.getOpposite();
    }

    @Override
    public double getAirflow() {
        return airflow * Config.airflowMultiplier;
    }

    @Override
    public double getThrust() {
        return thrust * Config.thrustMultiplier;
    }

    @Override
    public boolean isActive() {
       return getJetPower() > 0.01f;
    }



    double getJetPower()
    {
        int redstoneLevel = 0;
        if (level != null) {
            redstoneLevel = level.getBestNeighborSignal(getBlockPos());
        }
        return redstoneLevel * thrust;
    }

    @Override
    public void afterMove(ServerLevel oldLevel, ServerLevel newLevel, BlockState state,
                          BlockPos oldPos, BlockPos newPos) {
    }
}