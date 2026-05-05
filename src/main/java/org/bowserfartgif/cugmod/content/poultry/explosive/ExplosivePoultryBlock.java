package org.bowserfartgif.cugmod.content.poultry.explosive;

import dev.ryanhcode.sable.Sable;
import dev.ryanhcode.sable.api.block.BlockWithSubLevelCollisionCallback;
import dev.ryanhcode.sable.api.physics.callback.BlockSubLevelCollisionCallback;
import dev.ryanhcode.sable.companion.math.JOMLConversion;
import dev.ryanhcode.sable.sublevel.SubLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.bowserfartgif.cugmod.content.poultry.PoultryBlock;
import org.bowserfartgif.cugmod.registry.DoodooBlockEntities;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

public class ExplosivePoultryBlock extends PoultryBlock implements BlockWithSubLevelCollisionCallback {
    
    public ExplosivePoultryBlock(Properties properties) {
        super(properties);
    }
    
    @Override
    public void doSomething(Level level, BlockPos block, Entity entity, Vector3d blockPos, SubLevel subLevel) {
        if (level.getBlockEntity(block) instanceof ExplosivePoultryBlockEntity poultryBlockEntity) {
            poultryBlockEntity.setFuze(5);
        }
    }
    
    public void explode(Level level, BlockPos block, Vector3d blockPos) {
        if (level.isClientSide()) {
            return;
        }
        level.removeBlock(block, false);
        level.explode(null, blockPos.x, blockPos.y, blockPos.z, 5.0f, Level.ExplosionInteraction.MOB);
    }
    
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            Level level,
            BlockState state,
            BlockEntityType<T> blockEntityType
    ) {
        return (level1, blockPos, blockState, t) -> {
            if (t instanceof ExplosivePoultryBlockEntity poultryBlockEntity) {
                if (poultryBlockEntity.fuze > 0) {
                    poultryBlockEntity.fuze--;
                    poultryBlockEntity.lastFuze = poultryBlockEntity.fuze;
                } else if (poultryBlockEntity.fuze == 0) {
                    SubLevel subLevel = Sable.HELPER.getContaining(level1, blockPos);
                    Vector3d block = JOMLConversion.atCenterOf(blockPos);
                    if (subLevel != null) {
                        subLevel.logicalPose().transformPosition(block);
                    }
                    this.explode(level1, blockPos, block);
                }
            }
        };
    }
    
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return DoodooBlockEntities.EXPLOSIVE_POULTRY.get().create(blockPos, blockState);
    }
    
    @Override
    public BlockSubLevelCollisionCallback sable$getCallback() {
        return ExplosivePoultryCallback.INSTANCE;
    }
}
