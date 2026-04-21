package org.bowserfartgif.cugmod.content.propulsion;

import dev.ryanhcode.sable.api.block.BlockSubLevelAssemblyListener;
import dev.ryanhcode.sable.api.block.propeller.BlockEntityPropeller;
import dev.ryanhcode.sable.api.block.propeller.BlockEntitySubLevelPropellerActor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.bowserfartgif.cugmod.Config;
import org.bowserfartgif.cugmod.registry.DoodooBlockEntities;

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

    private double getPowerModifier() {
        if (level == null) return 0.0;
        int redstoneLevel = level.getBestNeighborSignal(getBlockPos());
        return redstoneLevel / 15.0;
    }

    @Override
    public double getAirflow() {
        return airflow * getPowerModifier() * Config.airflowMultiplier;
    }

    @Override
    public double getThrust() {
        return thrust * getPowerModifier() * Config.thrustMultiplier;
    }

    @Override
    public boolean isActive() {
        return getPowerModifier() > 0.001f;
    }

    public Direction getFacing() {
        return facing;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ThrusterBlockEntity be) {
        Direction dir = state.getValue(ThrusterBlock.FACING).getOpposite();
        float particleSpeed = 1f;
        if (level.isClientSide && state.getValue(ThrusterBlock.POWERED)) {
            level.addParticle(
                    ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, // particle type
                    pos.getX() + 0.5 + dir.getStepX(),
                    pos.getY() + 0.5 + dir.getStepY(),
                    pos.getZ() + 0.5 + dir.getStepZ(),
                    dir.getStepX() * particleSpeed, dir.getStepY() * particleSpeed, dir.getStepZ() * particleSpeed// motion
            );
        }
    }

    @Override
    public void afterMove(ServerLevel oldLevel, ServerLevel newLevel, BlockState state,
                          BlockPos oldPos, BlockPos newPos) {
    }
}