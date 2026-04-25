package org.bowserfartgif.cugmod.mixin.harpoon;

import dev.ryanhcode.sable.platform.SableEventPlatform;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.world.RandomSequences;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.ServerLevelData;
import org.bowserfartgif.cugmod.content.harpoon.rope.server.ServerLevelExtension;
import org.bowserfartgif.cugmod.content.harpoon.rope.server.ServerRopeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.BooleanSupplier;

@Mixin(ServerLevel.class)
public class HarpoonServerLevelMixin implements ServerLevelExtension {
    
    @Unique
    private final ServerRopeManager cugMod$ropeManager = new ServerRopeManager((ServerLevel) (Object) this);
    
    @Override
    public ServerRopeManager cugMod$getRopeManager() {
        return this.cugMod$ropeManager;
    }
    
    @Inject(method = "<init>", at = @At("TAIL"))
    private void cugMod$init(
            MinecraftServer server,
            Executor dispatcher,
            LevelStorageSource.LevelStorageAccess levelStorageAccess,
            ServerLevelData serverLevelData,
            ResourceKey dimension,
            LevelStem levelStem,
            ChunkProgressListener progressListener,
            boolean isDebug,
            long biomeZoomSeed,
            List customSpawners,
            boolean tickTime,
            RandomSequences randomSequences,
            CallbackInfo ci
    ) {
        SableEventPlatform.INSTANCE.onSubLevelContainerReady((level, container) -> {
            if (this.equals(level)) {
                this.cugMod$ropeManager.init();
            }
        });
    }
    
    @Inject(method = "tick", at = @At("HEAD"))
    private void cugMod$tick(BooleanSupplier hasTimeLeft, CallbackInfo ci) {
        this.cugMod$ropeManager.tick();
    }
    
}
