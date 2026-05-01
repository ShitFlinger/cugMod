package org.bowserfartgif.cugmod.mixin.harpoon;

import dev.ryanhcode.sable.api.sublevel.ServerSubLevelContainer;
import net.minecraft.server.level.ServerLevel;
import org.bowserfartgif.cugmod.content.harpoon.rope.server.ServerLevelExtension;
import org.bowserfartgif.cugmod.content.harpoon.rope.server.ServerRopeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(ServerLevel.class)
public class HarpoonServerLevelMixin implements ServerLevelExtension {
    
    @Unique
    private ServerRopeManager cugMod$ropeManager;
    
    @Override
    public ServerRopeManager cugMod$getRopeManager() {
        return this.cugMod$ropeManager;
    }
    
    @Inject(method = "tick", at = @At("HEAD"))
    private void cugMod$tick(BooleanSupplier hasTimeLeft, CallbackInfo ci) {
        this.cugMod$ropeManager.tick();
    }
    
    public void cugMod$init(ServerSubLevelContainer container) {
        this.cugMod$ropeManager = new ServerRopeManager((ServerLevel) (Object) this); // WHY THE FUCK IS CONSTRUCTOR INIT BROKEN???
        this.cugMod$ropeManager.init(container);
    }
    
}
