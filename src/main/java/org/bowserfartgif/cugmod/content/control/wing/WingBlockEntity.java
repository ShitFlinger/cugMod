package org.bowserfartgif.cugmod.content.control.wing;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.bowserfartgif.cugmod.registry.DoodooBlockEntities;
import org.jetbrains.annotations.Nullable;

public class WingBlockEntity extends BlockEntity  {

    public WingBlockEntity(BlockPos pos, BlockState blockState) {
        super(DoodooBlockEntities.WING.get(), pos, blockState);
    }

}
