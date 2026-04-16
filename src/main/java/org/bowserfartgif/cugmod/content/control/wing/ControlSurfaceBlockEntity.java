package org.bowserfartgif.cugmod.content.control.wing;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.bowserfartgif.cugmod.registry.DoodooBlockEntities;

import java.util.HashSet;
import java.util.Set;

public class ControlSurfaceBlockEntity extends BlockEntity {
    public float receivedAngle = 0f;

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

    public float getEffectiveAngle() {
        float own = getControlSurfaceAngle();
        return own != 0 ? own : receivedAngle;
    }

    public void startPropagation() {
        Set<BlockPos> visited = new HashSet<>();
        visited.add(getBlockPos().immutable());
        propagateAngle(getControlSurfaceAngle(), 16, visited);
    }

    void propagateAngle(float angle, int depth, Set<BlockPos> visited) {
        if (level == null || depth <= 0) return;

        for (Direction direction : Direction.values()) {
            BlockPos neighborPos = getBlockPos().relative(direction).immutable();
            if (visited.contains(neighborPos)) continue;
            BlockEntity neighborBe = level.getBlockEntity(neighborPos);

            if (neighborBe instanceof ControlSurfaceBlockEntity neighbor) {
                if (neighbor.getControlSurfaceAngle() == 0) {
                    visited.add(neighborPos);
                    neighbor.receivedAngle = angle;
                    neighbor.setChanged();
                    level.sendBlockUpdated(neighborPos, neighbor.getBlockState(), neighbor.getBlockState(), 3);
                    neighbor.propagateAngle(angle, depth - 1, visited);
                }
            }
        }
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider registries) {
        receivedAngle = tag.getFloat("receivedAngle");
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putFloat("receivedAngle", receivedAngle);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        receivedAngle = tag.getFloat("receivedAngle");
    }
}