package org.bowserfartgif.cugmod.content.poultry.explosive;

import foundry.veil.api.network.VeilPacketManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.bowserfartgif.cugmod.content.poultry.PoultryBlockEntity;

public class ExplosivePoultryBlockEntity extends PoultryBlockEntity {
    
    protected float fuze = -1.0f;
    protected float lastFuze = -1.0f;
    
    public ExplosivePoultryBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }
    
    public void setFuze(int i) {
        this.fuze = i;
        this.lastFuze = i;
        if (this.level != null && this.level.isClientSide()) {
            return;
        }
        VeilPacketManager.tracking(this).sendPacket(
            new FuzeSyncPacket(this.getBlockPos(), i)
        );
    }
}
