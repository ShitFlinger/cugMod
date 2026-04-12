package org.bowserfartgif.cugmod.content.control.wing;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.bowserfartgif.cugmod.registry.DoodooBlockEntities;

public class ControlSurfaceBlockEntity extends BlockEntity {

    public ControlSurfaceBlockEntity(BlockPos pos, BlockState blockState) {
        super(DoodooBlockEntities.CONTROL_SURFACE.get(), pos, blockState);
    }

    public float getControlSurfaceAngle() {
        if (level == null) return 0f;
        int redstoneLevel = level.getBestNeighborSignal(getBlockPos());
        float mult = -1f;
        return redstoneLevel * mult;
    }
}
