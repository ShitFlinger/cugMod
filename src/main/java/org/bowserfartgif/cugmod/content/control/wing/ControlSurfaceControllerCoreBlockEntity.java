package org.bowserfartgif.cugmod.content.control.wing;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.bowserfartgif.cugmod.registry.DoodooBlockEntities;

public class ControlSurfaceControllerCoreBlockEntity extends BlockEntity {

    public ControlSurfaceControllerCoreBlockEntity(BlockPos pos, BlockState blockState) {
        super(DoodooBlockEntities.CONTROL_SURFACE_CONTROLLER_BLOCK.get(), pos, blockState);
    }

    public void updateControlSurfaces() {
        if (level == null) return;

        BlockState thisState = level.getBlockState(getBlockPos());
        Direction facing = thisState.getValue(ControlSurfaceControllerCoreBlock.FACING);
        boolean isElevator = thisState.getValue(ControlSurfaceControllerCoreBlock.ISELEVATOR);

        Direction left = facing.getCounterClockWise();
        Direction right = facing.getClockWise();

        Direction up = facing.UP;
        Direction down = facing.DOWN;

        int leftSignal = level.getSignal(getBlockPos().relative(left), left);
        int rightSignal = level.getSignal(getBlockPos().relative(right), right);

        float angle;
        if (leftSignal > 0 && rightSignal == 0) {
            angle = 1f;
        } else if (rightSignal > 0 && leftSignal == 0) {
            angle = -1f;
        } else {
            angle = 0f;
        }

        scanAndUpdate(left, facing, isElevator, angle);
        scanAndUpdate(right, facing, isElevator, angle);
    }

    private void scanAndUpdate(Direction scanDirection, Direction facing, boolean isElevator, float angle) {

        }
    }
