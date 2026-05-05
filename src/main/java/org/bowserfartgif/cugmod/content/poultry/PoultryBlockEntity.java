package org.bowserfartgif.cugmod.content.poultry;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class PoultryBlockEntity extends BlockEntity {
    
    public PoultryBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }
    
    public void onCreate(Level level) {
        if (level != null) {
            PoultryManager.birds.add(this.getBlockPos());
        }
    }
    
    @Override
    public void onLoad() {
        super.onLoad();
        this.onCreate(this.level);
    }
    
    public void onRemove(Level level) {
        if (level != null) {
            PoultryManager.birds.remove(this.getBlockPos());
        }
    }
    
    @Override
    public void onChunkUnloaded() {
        super.onChunkUnloaded();
        this.onRemove(this.level);
    }
}
