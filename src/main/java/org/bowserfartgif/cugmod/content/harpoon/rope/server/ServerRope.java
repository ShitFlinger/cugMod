package org.bowserfartgif.cugmod.content.harpoon.rope.server;

import dev.ryanhcode.sable.api.physics.object.rope.RopeHandle;
import dev.ryanhcode.sable.api.physics.object.rope.RopePhysicsObject;
import dev.ryanhcode.sable.sublevel.SubLevel;
import org.bowserfartgif.cugmod.content.harpoon.RopeAttachmentEntity;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ServerRope extends RopePhysicsObject {
    
    private final ServerRopeAttachmentPoint startAttachmentPoint;
    private final ServerRopeAttachmentPoint endAttachmentPoint;
    protected ServerRopeManager.RopeHandle managerHandle;
    
    public ServerRope() {
        super(List.of(), 0.05d);
        this.startAttachmentPoint = new ServerRopeAttachmentPoint(RopeHandle.AttachmentPoint.START);
        this.startAttachmentPoint.parent = this;
        this.endAttachmentPoint = new ServerRopeAttachmentPoint(RopeHandle.AttachmentPoint.END);
        this.endAttachmentPoint.parent = this;
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
        }
    }
    
    public void physicsTick() {
        if (this.isActive() && this.handle != null) {
            this.managerHandle.updatePoints();
        }
    }
    
    public ServerRopeAttachmentPoint startAttachmentPoint() {
        return this.startAttachmentPoint;
    }
    
    public ServerRopeAttachmentPoint endAttachmentPoint() {
        return this.endAttachmentPoint;
    }
    
    public void onAttachToSublevel(SubLevel subLevel, ServerRopeAttachmentPoint serverRopeAttachmentPoint) {
    }
}
