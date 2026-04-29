package org.bowserfartgif.cugmod.content.harpoon.rope.client;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.ryanhcode.sable.companion.math.JOMLConversion;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.bowserfartgif.cugmod.content.harpoon.rope.server.ServerRope;
import org.bowserfartgif.cugmod.content.harpoon.rope.server.ServerRopeManager;
import org.joml.Vector3d;
import org.joml.Vector3dc;

public class RopeRenderer {
    
    
    public static void renderRopes(MultiBufferSource bufferSource, Level level) {
        ServerLevel serverLevel = ServerLifecycleHooks.getCurrentServer().getLevel(level.dimension());
        ServerRopeManager manager = ServerRopeManager.getManager(serverLevel);
        
        VertexConsumer builder = bufferSource.getBuffer(RenderType.lines());
        
        Vec3 position = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        
        Vector3d camPos = JOMLConversion.toJOML(position);
        Vector3d vector3d = new Vector3d();
        for (ServerRope rope : manager.getRopes()) {
            ObjectList<Vector3d> points = rope.getPoints();
            float size = points.size();
            for (int i = 1; i < size; i++) {
                Vector3d firstPoint = points.get(i-1);
                Vector3d secondPoint = points.get(i);
                
                Vector3d normal = secondPoint.sub(firstPoint, new Vector3d());
                
                firstPoint.sub(camPos, vector3d);
                builder.addVertex((float) vector3d.x, (float) vector3d.y, (float) vector3d.z)
                        .setColor(i/size, (size-i)/size, 0.0f, 1.0f)
                        .setNormal((float) normal.x, (float) normal.y, (float) normal.z);
                
                secondPoint.sub(camPos, vector3d);
                builder.addVertex((float) vector3d.x, (float) vector3d.y, (float) vector3d.z)
                        .setColor(i/size, (size-i)/size, 0.0f, 1.0f)
                        .setNormal((float) -normal.x, (float) -normal.y, (float) -normal.z);
            }
        }
        
    }
    
}
