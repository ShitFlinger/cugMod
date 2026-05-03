package org.bowserfartgif.cugmod.content.swine;

import dev.ryanhcode.sable.Sable;
import dev.ryanhcode.sable.api.sublevel.SubLevelContainer;
import dev.ryanhcode.sable.sublevel.SubLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.LavaFluid;
import org.bowserfartgif.cugmod.registry.DoodooBlockEntities;

public class WretchedSwineBlockEntity extends BlockEntity {
    
    private int collisionCooldown = 0;
    private int ticksSinceFire = 0;
    private int fireTicks = 0;
    private boolean fireTickAdded = false;
    
    public WretchedSwineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }
    
    public void tick() {
        
        BlockState blockState = this.getBlockState();
        BlockPos blockPos = this.getBlockPos();
        Level level = this.getLevel();
        if (level != null) {
            
            SubLevel subLevel = Sable.HELPER.getContaining(level, blockPos);
            
            Sable.HELPER.runIncludingSubLevels(level, blockPos.getCenter(), false, subLevel, (subLevel0, blockPos0) -> {
                BlockState blockState0 = level.getBlockState(blockPos0);
                Block block = blockState0.getBlock();
                if (block instanceof BaseFireBlock || blockState0.getFluidState().getType() instanceof LavaFluid) {
                    this.addFireTicks(3);
                    this.tryBurn();
                }
                return null;
            });
        }
        
        if (this.collisionCooldown > 0) {
            this.collisionCooldown--;
        }
        if (this.fireTickAdded) {
            this.fireTickAdded = false;
        }
        if (this.ticksSinceFire > 0) {
            this.ticksSinceFire--;
        }
        if (this.fireTicks > 0 && this.ticksSinceFire == 0) {
            this.fireTicks = Math.max(this.fireTicks - 5, 0);
        }
    }
    
    public void setCollisionCooldown(int collisionCooldown) {
        this.collisionCooldown = collisionCooldown;
    }
    
    public boolean shouldApplyCollision() {
        return this.collisionCooldown == 0;
    }
    
    public void addFireTicks(int ticks) {
        if (!this.fireTickAdded) {
            this.fireTicks += ticks;
            this.ticksSinceFire = 20;
            this.fireTickAdded = true;
        }
    }
    
    public void tryBurn() {
        Level level = this.getLevel();
        if (level == null) {
            return;
        }
        BlockState blockState = this.getBlockState();
        BlockPos blockPos = this.getBlockPos();
        if (this.fireTicks >= 300) {
            if (blockState.getValue(WretchedSwineBlock.MOOD) != WretchedSwineBlock.Mood.BURNT) {
                BlockState newState = blockState.setValue(WretchedSwineBlock.MOOD, WretchedSwineBlock.Mood.BURNT);
                level.setBlockAndUpdate(blockPos, newState);
                this.fireTicks = 0;
            }
        }
        if (this.fireTicks >= 2000) {
            if (blockState.getValue(WretchedSwineBlock.MOOD) == WretchedSwineBlock.Mood.BURNT) {
                level.destroyBlock(blockPos, false);
            }
        }
    }
}
