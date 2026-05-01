package org.bowserfartgif.cugmod.content.harpoon.rope.server;

import dev.ryanhcode.sable.sublevel.SubLevel;
import net.minecraft.util.Mth;
import org.bowserfartgif.cugmod.content.harpoon.HarpoonEntity;
import org.bowserfartgif.cugmod.content.harpoon.RopeAttachmentEntity;
import org.joml.Vector3d;
import org.joml.Vector3dc;

import java.util.List;

public class HarpoonRope extends ServerRope {
    
    public HarpoonRope(RopeAttachmentEntity startAttachment, HarpoonEntity endAttachment) {
        super(startAttachment, endAttachment);
    }
    
    @Override
    public void physicsTick() {
        super.physicsTick();
        if (this.isActive() && this.handle != null) {
            List<Vector3d> points = this.getPoints();
            
            final double tension = 2.0d;
            
            int currentSize = points.size();
            Vector3dc startAttachment = this.managerHandle.startAttachmentGlobal();
            Vector3dc endAttachment = this.managerHandle.endAttachmentGlobal();
            double distance = startAttachment.distance(endAttachment);
            int expectedSize = Mth.ceil(1.0d/tension * distance);
            Vector3dc firstPoint = points.getFirst();
            
            int sizeDiff;
            if (currentSize < expectedSize) {
                sizeDiff = expectedSize - currentSize;
                Vector3d direction = startAttachment.sub(firstPoint, new Vector3d()).normalize(tension);
                Vector3d newPoint = new Vector3d(firstPoint);
                for (int i = 0; i < sizeDiff; i++) {
                    this.addPoint(newPoint.add(direction));
                }
            } else if (expectedSize > 2 && currentSize > expectedSize) {
                sizeDiff = currentSize - expectedSize;
                for (int i = 0; i < sizeDiff; i++) {
                    this.removeFirstPoint();
                }
            }
        }
    }
    
    @Override
    public void onAttachToSublevel(SubLevel subLevel, ServerRopeAttachmentPoint serverRopeAttachmentPoint) {
        super.onAttachToSublevel(subLevel, serverRopeAttachmentPoint);
        this.physicsTick();
    }
}
