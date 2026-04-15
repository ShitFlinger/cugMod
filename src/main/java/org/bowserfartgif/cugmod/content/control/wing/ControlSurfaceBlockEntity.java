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
        int redstoneLevelAbove = level.getBestNeighborSignal(getBlockPos().above());
        int redstoneLevelBelow = level.getBestNeighborSignal(getBlockPos().below()) * -1;



        float mult = -1f;
        return mult * (redstoneLevelAbove + redstoneLevelBelow);
    }

    public float angle;
    public float prevAngle;

    public void tick(){
        prevAngle = angle;
        angle = -getControlSurfaceAngle() * 1.5f;
    }

}
