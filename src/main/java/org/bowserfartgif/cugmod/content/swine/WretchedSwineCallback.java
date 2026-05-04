package org.bowserfartgif.cugmod.content.swine;

import dev.ryanhcode.sable.Sable;
import dev.ryanhcode.sable.api.physics.callback.BlockSubLevelCollisionCallback;
import dev.ryanhcode.sable.api.sublevel.ServerSubLevelContainer;
import dev.ryanhcode.sable.api.sublevel.SubLevelContainer;
import dev.ryanhcode.sable.sublevel.SubLevel;
import dev.ryanhcode.sable.sublevel.system.SubLevelPhysicsSystem;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import org.bowserfartgif.cugmod.registry.DoodooSoundTypes;
import org.joml.Vector3d;

public class WretchedSwineCallback implements BlockSubLevelCollisionCallback {
    
    public static final WretchedSwineCallback INSTANCE = new WretchedSwineCallback();
    
    public double getTriggerVelocity() {
        return 12.0;
    }
    
    @Override
    public BlockSubLevelCollisionCallback.CollisionResult sable$onCollision(final BlockPos pos, final Vector3d hitPos, final double impactVelocity) {
        final SubLevelPhysicsSystem system = SubLevelPhysicsSystem.getCurrentlySteppingSystem();
        final ServerLevel level = system.getLevel();
        
        final BlockState state = level.getBlockState(pos);
        
        if (!(state.getBlock() instanceof WretchedSwineBlock)) {
            return CollisionResult.NONE;
        }
        
        if (!(level.getBlockEntity(pos) instanceof WretchedSwineBlockEntity blockEntity)) {
            return CollisionResult.NONE;
        }
        
        
        final double triggerVelocity = this.getTriggerVelocity();
        
        if (impactVelocity * impactVelocity < triggerVelocity * triggerVelocity) {
            return CollisionResult.NONE;
        }
        
        ServerSubLevelContainer container = SubLevelContainer.getContainer(level);
        assert container != null;
        
        SubLevel subLevel = Sable.HELPER.getContaining(level, pos);
        
        
        if (this.shouldTriggerFor(state)) {
            return this.hurt(level, pos, state, hitPos, blockEntity);
        }
        
        return CollisionResult.NONE;
    }
    
    public boolean shouldTriggerFor(final BlockState state) {
        return true;
    }
    
    public CollisionResult hurt(ServerLevel level, BlockPos pos, BlockState state, Vector3d hitPos, WretchedSwineBlockEntity blockEntity) {
        
        if (!blockEntity.shouldApplyCollision()) {
            return CollisionResult.NONE;
        }
        blockEntity.setCollisionCooldown(20);
        
        WretchedSwineBlock.Mood mood = state.getValue(WretchedSwineBlock.MOOD);
        BlockState newState;
        
        float volume = 0.4f;
        float pitch = level.random.nextFloat();
        SoundType soundType = DoodooSoundTypes.SWINE.get();
        SoundEvent sound = soundType.getPlaceSound();
        
        switch (mood) {
            case HAPPY -> {
                newState = state.setValue(WretchedSwineBlock.MOOD, WretchedSwineBlock.Mood.HURT);
                level.setBlockAndUpdate(pos, newState);
                
                volume *= 3.0f;
                pitch *= 0.4f;
                pitch += 1.6f;
            }
            case HURT -> {
                level.destroyBlock(pos, false);
                volume *= 0.4f;
                pitch *= 0.15f;
            }
        }
        
        level.playSound(null, hitPos.x, hitPos.y, hitPos.z, sound, SoundSource.BLOCKS, volume, pitch);
        
        return CollisionResult.NONE;
    }
}
