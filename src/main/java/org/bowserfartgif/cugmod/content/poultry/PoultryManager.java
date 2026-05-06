package org.bowserfartgif.cugmod.content.poultry;

import dev.ryanhcode.sable.Sable;
import dev.ryanhcode.sable.companion.math.JOMLConversion;
import dev.ryanhcode.sable.companion.math.Pose3dc;
import dev.ryanhcode.sable.sublevel.ClientSubLevel;
import dev.ryanhcode.sable.sublevel.SubLevel;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSets;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.joml.Vector3d;

import java.util.Map;
import java.util.Set;

public class PoultryManager {
    
    public static final Set<BlockPos> birds = ObjectSets.synchronize(new ObjectOpenHashSet<>());
    
    public static final double BIAS = 0.5f;
    
    public static Map<BlockPos, SubLevel> getBirdsNear(Level level, Vector3d entityPos, Vector3d viewVector, double bias, float partialTick) {
        Map<BlockPos, SubLevel> birdsNear = new Object2ObjectArrayMap<>();
        Vector3d blockPos = new Vector3d();
        for (BlockPos bird : birds) {
            JOMLConversion.atCenterOf(bird, blockPos);
            SubLevel subLevel = Sable.HELPER.getContaining(level, bird);
            if (subLevel != null) {
                Pose3dc pose;
                if (subLevel instanceof ClientSubLevel clientSubLevel) {
                    pose = clientSubLevel.renderPose(partialTick);
                } else {
                    pose = subLevel.logicalPose();
                }
                pose.transformPosition(blockPos);
            }
            
            blockPos.sub(entityPos);
            double distanceToEntity = blockPos.length() - 0.75d;
            blockPos.normalize();
            double distance = blockPos.distance(viewVector);
            distance *= Math.sqrt(distanceToEntity);
            if (distance <= bias) {
                birdsNear.put(bird, subLevel);
            }
        }
        return birdsNear;
    }
    
    public static boolean hasBirdsNear(Level level, Vector3d entityPos, Vector3d viewVector, double bias, float partialTick) {
        Vector3d blockPos = new Vector3d();
        for (BlockPos bird : birds) {
            JOMLConversion.atCenterOf(bird, blockPos);
            SubLevel subLevel = Sable.HELPER.getContaining(level, bird);
            if (subLevel != null) {
                Pose3dc pose;
                if (subLevel instanceof ClientSubLevel clientSubLevel) {
                    pose = clientSubLevel.renderPose(partialTick);
                } else {
                    pose = subLevel.logicalPose();
                }
                pose.transformPosition(blockPos);
            }
            
            blockPos.sub(entityPos);
            double distanceToEntity = blockPos.length() - 0.75d;
            blockPos.normalize();
            double distance = blockPos.distance(viewVector);
            distance *= Math.sqrt(distanceToEntity);
            if (distance <= bias) {
                return true;
            }
        }
        return false;
    }
    
}
