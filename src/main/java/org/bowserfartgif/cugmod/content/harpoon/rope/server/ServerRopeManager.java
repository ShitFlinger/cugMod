package org.bowserfartgif.cugmod.content.harpoon.rope.server;

import dev.ryanhcode.sable.api.sublevel.ServerSubLevelContainer;
import dev.ryanhcode.sable.api.sublevel.SubLevelContainer;
import dev.ryanhcode.sable.api.sublevel.SubLevelTrackingPlugin;
import dev.ryanhcode.sable.companion.math.JOMLConversion;
import dev.ryanhcode.sable.sublevel.system.SubLevelPhysicsSystem;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectLists;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.bowserfartgif.cugmod.content.harpoon.RopeAttachmentEntity;
import org.jetbrains.annotations.ApiStatus;
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
    public void init() {
        ServerSubLevelContainer container = SubLevelContainer.getContainer(this.serverLevel);
        assert container != null;
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
        for (ServerRope rope : this.ropes) {
            rope.tick();
        }
    }
    
    public RopeHandle createRope(RopeAttachmentEntity startAttachment, RopeAttachmentEntity endAttachment) {
        ServerRope rope = new ServerRope(startAttachment, endAttachment);
        rope.addPoint(JOMLConversion.toJOML(((Entity) startAttachment).position()));
        rope.addPoint(JOMLConversion.toJOML(((Entity) endAttachment).position()));
        this.ropes.add(rope);
        SubLevelPhysicsSystem.require(this.serverLevel).addObject(rope);
        return new RopeHandle(rope);
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
        
        public Vector3dc startAttachment() {
            return this.rope.startAttachmentPoint().getPosition();
        }
        
        public Vector3dc endAttachment() {
            return this.rope.endAttachmentPoint().getPosition();
        }
    }
}
