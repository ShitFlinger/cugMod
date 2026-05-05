package org.bowserfartgif.cugmod.content.poultry.explosive;

import dev.ryanhcode.sable.api.physics.callback.BlockSubLevelCollisionCallback;
import dev.ryanhcode.sable.sublevel.system.SubLevelPhysicsSystem;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3d;

public class ExplosivePoultryCallback implements BlockSubLevelCollisionCallback {
    public static final ExplosivePoultryCallback INSTANCE = new ExplosivePoultryCallback();
    
    public double getTriggerVelocity() {
        return 12.0;
    }
    
    @Override
    public BlockSubLevelCollisionCallback.CollisionResult sable$onCollision(final BlockPos pos, final Vector3d hitPos, final double impactVelocity) {
        final SubLevelPhysicsSystem system = SubLevelPhysicsSystem.getCurrentlySteppingSystem();
        final ServerLevel level = system.getLevel();
        
        final BlockState state = level.getBlockState(pos);
        
        if (!(state.getBlock() instanceof ExplosivePoultryBlock)) {
            return BlockSubLevelCollisionCallback.CollisionResult.NONE;
        }
        
        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof ExplosivePoultryBlockEntity blockEntity)) {
            return BlockSubLevelCollisionCallback.CollisionResult.NONE;
        }
        
        
        final double triggerVelocity = this.getTriggerVelocity();
        
        if (impactVelocity * impactVelocity < triggerVelocity * triggerVelocity) {
            return BlockSubLevelCollisionCallback.CollisionResult.NONE;
        }
        
        if (blockEntity.fuze < 0) {
            blockEntity.setFuze(20);
        }
        
        return BlockSubLevelCollisionCallback.CollisionResult.NONE;
    }
}
