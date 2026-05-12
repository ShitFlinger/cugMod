package org.bowserfartgif.cugmod.mixin.poultry;

import net.minecraft.world.level.Level;
import org.bowserfartgif.cugmod.content.poultry.PoultryManager;
import org.bowserfartgif.cugmod.ext.poultry.LevelPoultryExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Level.class)
public class PoultryLevelMixin implements LevelPoultryExtension {
    
    @Unique
    private final PoultryManager cugMod$poultryManager = new PoultryManager();
    
    @Override
    public PoultryManager cugMod$getPoultryManager() {
        return this.cugMod$poultryManager;
    }
}
