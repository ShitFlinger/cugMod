package org.bowserfartgif.cugmod.content.poultry.explosive;

import foundry.veil.api.network.VeilPacketManager;
import foundry.veil.api.network.handler.ClientPacketContext;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.bowserfartgif.cugmod.Cugmod;

public record FuzeSyncPacket(BlockPos blockPos, int fuze) implements CustomPacketPayload {
    
    public static final Type<FuzeSyncPacket> TYPE = new Type<>(Cugmod.id("poultry/fuze_sync"));
    public static final StreamCodec<FriendlyByteBuf, FuzeSyncPacket> CODEC = StreamCodec.ofMember(
            (packet, buf) -> {
                buf.writeBlockPos(packet.blockPos);
                buf.writeInt(packet.fuze);
            },
            buf -> new FuzeSyncPacket(buf.readBlockPos(), buf.readInt())
    );
    public static final VeilPacketManager.PacketHandler<ClientPacketContext, FuzeSyncPacket> HANDLER = (packet, ctx) -> {
        Level level = ctx.level();
        if (level == null) {
            return;
        }
        
        BlockEntity blockEntity = level.getBlockEntity(packet.blockPos);
        if (blockEntity instanceof ExplosivePoultryBlockEntity be) {
            be.setFuze(packet.fuze);
        }
    };
    
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
