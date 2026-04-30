package org.bowserfartgif.cugmod.content.harpoon;

import org.joml.Vector3dc;

public interface RopeAttachmentEntity {
    
    // in object local space
    Vector3dc cugMod$getAttachmentPoint();
    
    boolean cugMod$shouldUpdateAttachment();
    
}
