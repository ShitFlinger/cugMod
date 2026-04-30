package org.bowserfartgif.cugmod.content.harpoon.rope.server;

import dev.ryanhcode.sable.Sable;
import dev.ryanhcode.sable.api.sublevel.ServerSubLevelContainer;
import dev.ryanhcode.sable.api.sublevel.SubLevelContainer;
import dev.ryanhcode.sable.api.sublevel.SubLevelTrackingPlugin;
import dev.ryanhcode.sable.companion.math.JOMLConversion;
import dev.ryanhcode.sable.sublevel.SubLevel;
import dev.ryanhcode.sable.sublevel.system.SubLevelPhysicsSystem;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectLists;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.bowserfartgif.cugmod.content.harpoon.RopeAttachmentEntity;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;
import org.joml.Vector3dc;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ServerRopeManager implements SubLevelTrackingPlugin {
    
    private final ServerLevel serverLevel;
    private final ObjectList<ServerRope> ropes = new ObjectArrayList<>();
    private final List<ServerRope> ropesView = ObjectLists.unmodifiable(this.ropes);
    
    public ServerRopeManager(ServerLevel serverLevel) {
        this.serverLevel = serverLevel;
    }
    
    @ApiStatus.Internal
    public void init(@NotNull ServerSubLevelContainer container) {
        container.trackingSystem().addTrackingPlugin(this);
    }
    
    @Override
    public Iterable<UUID> neededPlayers() {
        Set<UUID> players = new ObjectOpenHashSet<>();
        
        Set<Entity> attachments = new ObjectOpenHashSet<>();
        for (ServerRope rope : this.ropes) {
            attachments.add((Entity) rope.startAttachmentPoint().getAttachmentEntity());
            attachments.add((Entity) rope.endAttachmentPoint().getAttachmentEntity());
        }
        
        ChunkMap chunkMap = this.serverLevel.getChunkSource().chunkMap;
        for (Entity attachment : attachments) {
            for (ServerPlayer player : chunkMap.getPlayers(attachment.chunkPosition(), false)) {
                players.add(player.getUUID());
            }
        }
        
        return players;
    }
    
    public List<ServerRope> getRopes() {
        return this.ropesView;
    }
    
    @Override
    public void sendTrackingData(int interpolationTick) {
    
    }
    
    public static ServerRopeManager getManager(ServerLevel serverLevel) {
        return ((ServerLevelExtension) serverLevel).cugMod$getRopeManager();
    }
    
    public void tick() {
        for (int i = 0; i < this.ropes.size(); i++) {
            ServerRope rope = this.ropes.get(i);
            rope.tick();
        }
    }
    
    public void physicsTick() {
        for (int i = 0; i < this.ropes.size(); i++) {
            ServerRope rope = this.ropes.get(i);
            rope.physicsTick();
        }
    }
    
    public RopeHandle createRope(RopeAttachmentEntity startAttachment, RopeAttachmentEntity endAttachment) {
        ServerRope rope = new ServerRope(startAttachment, endAttachment);
        return this.addRope(rope);
    }
    
    public RopeHandle addRope(ServerRope rope) {
        RopeHandle managerHandle = new RopeHandle(rope);
        rope.managerHandle = managerHandle;
        managerHandle.addPoint(rope.endAttachmentPoint().getPosition());
        managerHandle.addPoint(rope.startAttachmentPoint().getPosition());
        SubLevelPhysicsSystem.require(this.serverLevel).addObject(rope);
        this.ropes.add(rope);
        return managerHandle;
    }
    
    public class RopeHandle {
        
        private final ServerRope rope;
        private final Vector3d lastPoint;
        
        public RopeHandle(ServerRope rope) {
            this.rope = rope;
            this.lastPoint = new Vector3d();
        }
        
        public void remove() {
            ServerRopeManager.this.ropes.remove(this.rope);
            SubLevelPhysicsSystem.require(ServerRopeManager.this.serverLevel).removeObject(this.rope);
        }
        
        public void addPoint(Vector3dc point) {
            this.lastPoint.set(point);
            this.rope.addPoint(point);
        }
        
        public Vector3dc lastPoint() {
            return this.lastPoint;
        }
        
        public void updatePoints() {
            this.rope.updatePose();
            this.lastPoint.set(this.rope.getPoints().getLast());
        }
        
        public List<Vector3d> getPoints() {
            return this.rope.getPoints();
        }
        
        public Vector3dc startAttachment() {
            return this.rope.startAttachmentPoint().getPosition();
        }
        
        public Vector3d startAttachmentGlobal() {
            Vector3d attachment = new Vector3d(this.startAttachment());
            
            SubLevel subLevel = Sable.HELPER.getContaining(this.getLevel(), attachment);
            if (subLevel != null) {
                subLevel.logicalPose().transformPosition(attachment);
            }
            
            return attachment;
        }
        
        public Vector3dc endAttachment() {
            return this.rope.endAttachmentPoint().getPosition();
        }
        
        public Vector3d endAttachmentGlobal() {
            Vector3d attachment = new Vector3d(this.endAttachment());
            
            SubLevel subLevel = Sable.HELPER.getContaining(this.getLevel(), attachment);
            if (subLevel != null) {
                subLevel.logicalPose().transformPosition(attachment);
            }
            
            return attachment;
        }
        
        public void removeFirstPoint() {
            this.rope.removeFirstPoint();
        }
        
        public Level getLevel() {
            return ServerRopeManager.this.serverLevel;
        }
    }
}
