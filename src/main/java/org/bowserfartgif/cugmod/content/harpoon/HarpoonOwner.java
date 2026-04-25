package org.bowserfartgif.cugmod.content.harpoon;

public interface HarpoonOwner extends RopeAttachmentEntity {
    
    void cugMod$setHarpoon(HarpoonEntity harpoon);
    
    HarpoonEntity cugMod$getHarpoon();
    
    void cugMod$onTouchHarpoon(HarpoonEntity harpoon);
}
