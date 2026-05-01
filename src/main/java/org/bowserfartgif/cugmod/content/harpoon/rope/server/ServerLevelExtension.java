package org.bowserfartgif.cugmod.content.harpoon.rope.server;

import dev.ryanhcode.sable.api.sublevel.ServerSubLevelContainer;

public interface ServerLevelExtension {
    
    ServerRopeManager cugMod$getRopeManager();
    
    void cugMod$init(ServerSubLevelContainer container);
    
}
