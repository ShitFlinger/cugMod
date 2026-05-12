package org.bowserfartgif.cugmod.mixin.poultry;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import org.bowserfartgif.cugmod.ext.poultry.LevelPoultryExtension;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelChunk.class)
public class PoultryLevelChunkMixin{
    
    @Shadow
    @Final
    Level level;
    
    @Inject(method = "removeBlockEntity", at = @At("TAIL"))
    private void cugMod$removeBlockEntity(BlockPos pos, CallbackInfo ci) {
        ((LevelPoultryExtension) this.level).cugMod$getPoultryManager().birds.remove(pos);
    }
    
}
