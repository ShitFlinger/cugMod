package org.bowserfartgif.cugmod.registry;

import foundry.veil.api.network.VeilPacketManager;
import org.bowserfartgif.cugmod.content.poultry.explosive.FuzeSyncPacket;

import static org.bowserfartgif.cugmod.Cugmod.MODID;

public class DoodooPackets {
    
    private static final VeilPacketManager PACKET_MANAGER = VeilPacketManager.create(MODID, "1");
    
    public static void bootstrap() {
        
        PACKET_MANAGER.registerClientbound(FuzeSyncPacket.TYPE, FuzeSyncPacket.CODEC, FuzeSyncPacket.HANDLER);
        
    }
    
}
