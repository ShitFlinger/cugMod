package org.bowserfartgif.cugmod.content.harpoon.rope.server;

import dev.ryanhcode.sable.api.physics.object.rope.RopeHandle;
import dev.ryanhcode.sable.api.physics.object.rope.RopePhysicsObject;
import org.bowserfartgif.cugmod.content.harpoon.RopeAttachmentEntity;

import java.util.List;

public class ServerRope extends RopePhysicsObject {
    
    private final ServerRopeAttachmentPoint startAttachmentPoint;
    private final ServerRopeAttachmentPoint endAttachmentPoint;
    
    public ServerRope() {
        super(List.of(), -0.5d);
        this.startAttachmentPoint = new ServerRopeAttachmentPoint(RopeHandle.AttachmentPoint.START);
        this.endAttachmentPoint = new ServerRopeAttachmentPoint(RopeHandle.AttachmentPoint.END);
    }
    
    public ServerRope(RopeAttachmentEntity startAttachment, RopeAttachmentEntity endAttachment) {
        this();
        this.startAttachmentPoint.setAttachmentEntity(startAttachment);
        this.endAttachmentPoint.setAttachmentEntity(endAttachment);
    }
    
    public void tick() {
        if (this.isActive() && this.handle != null) {
            this.startAttachmentPoint.tick(this.handle);
            this.endAttachmentPoint.tick(this.handle);
            this.updatePose();
        }
        
    }
    
    public ServerRopeAttachmentPoint startAttachmentPoint() {
        return this.startAttachmentPoint;
    }
    
    public ServerRopeAttachmentPoint endAttachmentPoint() {
        return this.endAttachmentPoint;
    }
}
