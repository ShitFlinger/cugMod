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
        return -(redstoneLevelAbove + redstoneLevelBelow);
    }

    public float getEffectiveAngle() {
        float own = getControlSurfaceAngle();
        return own != 0f ? own : receivedAngle;
    }

    /**
     * Called when any neighbor changes. Finds the root of the linkage network
     * (any block with a direct redstone signal), then does a single flood-fill
     * to clear and re-propagate in one pass.
     */
    public void onNeighborChanged() {
        if (level == null || level.isClientSide) return;

        // Find the network root — a connected surface with direct redstone signal
        Set<BlockPos> network = new HashSet<>();
        float sourceAngle = findNetworkAngle(getBlockPos(), network);

        // Single pass: set every surface in the network to the found angle
        for (BlockPos pos : network) {
            if (level.getBlockEntity(pos) instanceof ControlSurfaceBlockEntity be) {
                // Root blocks keep their own redstone angle; passive blocks get propagated angle
                if (be.getControlSurfaceAngle() == 0f) {
                    be.receivedAngle = sourceAngle;
                    be.setChanged();
                    level.sendBlockUpdated(pos, be.getBlockState(), be.getBlockState(), 3);
                }
            }
        }
    }

    /**
     * Flood-fills the connected network of ControlSurfaceBlockEntities,
     * returning the first direct redstone angle found (or 0 if none).
     */
    private float findNetworkAngle(BlockPos start, Set<BlockPos> visited) {
        float found = 0f;
        // Use an iterative flood fill to avoid stack overflow on large linkages
        Set<BlockPos> frontier = new HashSet<>();
        frontier.add(start);

        while (!frontier.isEmpty()) {
            Set<BlockPos> next = new HashSet<>();
            for (BlockPos pos : frontier) {
                if (!visited.add(pos)) continue;
                if (!(level.getBlockEntity(pos) instanceof ControlSurfaceBlockEntity be)) continue;

                float own = be.getControlSurfaceAngle();
                if (own != 0f) found = own;

                for (Direction dir : Direction.values()) {
                    BlockPos neighbor = pos.relative(dir).immutable();
                    if (!visited.contains(neighbor) &&
                            level.getBlockEntity(neighbor) instanceof ControlSurfaceBlockEntity) {
                        next.add(neighbor);
                    }
                }
            }
            frontier = next;
        }
        return found;
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