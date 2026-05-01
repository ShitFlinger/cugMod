package org.bowserfartgif.cugmod.content.propulsion;

import dev.ryanhcode.sable.api.block.BlockSubLevelAssemblyListener;
import dev.ryanhcode.sable.api.block.propeller.BlockEntityPropeller;
import dev.ryanhcode.sable.api.block.propeller.BlockEntitySubLevelPropellerActor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.bowserfartgif.cugmod.Config;
import org.bowserfartgif.cugmod.particle_bullshit.ThrusterParticleOptions;
import org.bowserfartgif.cugmod.registry.DoodooBlockEntities;
import org.bowserfartgif.cugmod.registry.DoodooParticleTypes;

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
        double particleSpeed = 1 * be.getPowerModifier();
        if (level.isClientSide && state.getValue(ThrusterBlock.POWERED)) {
            RandomSource random = level.getRandom();
            for (int i = 0; i < 15; i++) {
                double spread = 0.4;
                level.addParticle(
                        new ThrusterParticleOptions(),
                        pos.getX() + 0.5 + dir.getStepX() + (random.nextDouble() - 0.5) * spread,
                        pos.getY() + 0.5 + dir.getStepY() + (random.nextDouble() - 0.5) * spread,
                        pos.getZ() + 0.5 + dir.getStepZ() + (random.nextDouble() - 0.5) * spread,
                        dir.getStepX() * particleSpeed,
                        dir.getStepY() * particleSpeed,
                        dir.getStepZ() * particleSpeed
                );
            }
        }
    }

    @Override
    public void afterMove(ServerLevel oldLevel, ServerLevel newLevel, BlockState state,
                          BlockPos oldPos, BlockPos newPos) {
    }
}