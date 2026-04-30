package org.bowserfartgif.cugmod.content.harpoon.rope.server;

import dev.ryanhcode.sable.Sable;
import dev.ryanhcode.sable.api.physics.object.rope.RopeHandle;
import dev.ryanhcode.sable.companion.math.JOMLConversion;
import dev.ryanhcode.sable.mixinterface.entity.entities_stick_sublevels.EntityStickExtension;
import dev.ryanhcode.sable.sublevel.ServerSubLevel;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.bowserfartgif.cugmod.content.harpoon.RopeAttachmentEntity;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;
import org.joml.Vector3dc;

public class ServerRopeAttachmentPoint {
    
    private final RopeHandle.AttachmentPoint attachmentPoint;
    private final Vector3d position;
    
    private ServerLevel serverLevel;
    private RopeAttachmentEntity attachmentEntity;
    protected ServerRope parent;
    
    public ServerRopeAttachmentPoint(RopeHandle.AttachmentPoint attachmentPoint) {
        this.attachmentPoint = attachmentPoint;
        this.position = new Vector3d();
    }
    
    public void setAttachmentEntity(@NotNull RopeAttachmentEntity attachmentEntity) {
        Entity entity = (Entity) attachmentEntity;
        Level level = entity.level();
        
        assert level instanceof ServerLevel;
        
        this.serverLevel = ((ServerLevel) entity.level());
        this.attachmentEntity = attachmentEntity;
        
        this.updatePosition();
    }
    
    public void tick(RopeHandle handle) {
        if (this.attachmentEntity == null) {
            return;
        }
        
        if (this.attachmentEntity.cugMod$shouldUpdateAttachment()) {
            this.updatePosition();
        }
        
        EntityStickExtension stickyEntity = (EntityStickExtension) this.attachmentEntity;
        Vec3 plotPos = stickyEntity.sable$getPlotPosition();
        ServerSubLevel subLevel = null;
        if (plotPos != null) {
            subLevel = (ServerSubLevel) Sable.HELPER.getContaining(this.serverLevel, plotPos);
        } else if (Sable.HELPER.isInPlotGrid(this.serverLevel, this.position)) {
            subLevel = (ServerSubLevel) Sable.HELPER.getContaining(this.serverLevel, this.position);
        }
        handle.setAttachment(this.attachmentPoint, this.position, subLevel);
    }
    
    private void updatePosition() {
        EntityStickExtension stickyEntity = (EntityStickExtension) this.attachmentEntity;
        Vec3 pos = stickyEntity.sable$getPlotPosition();
        if (pos == null) {
            pos = ((Entity) this.attachmentEntity).position();
        } else {
            this.parent.onAttachToSublevel(Sable.HELPER.getContaining(this.serverLevel, pos), this);
        }
        JOMLConversion.toJOML(pos, this.position).add(this.attachmentEntity.cugMod$getAttachmentPoint());
    }
    
    public RopeHandle.AttachmentPoint getAttachmentPoint() {
        return this.attachmentPoint;
    }
    
    public Vector3dc getPosition() {
        return this.position;
    }
    
    public RopeAttachmentEntity getAttachmentEntity() {
        return this.attachmentEntity;
    }
}
