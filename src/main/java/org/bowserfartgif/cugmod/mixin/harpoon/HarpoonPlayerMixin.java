package org.bowserfartgif.cugmod.mixin.harpoon;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.bowserfartgif.cugmod.content.harpoon.HarpoonEntity;
import org.bowserfartgif.cugmod.content.harpoon.HarpoonOwner;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;
import org.joml.Vector3dc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Player.class)
public abstract class HarpoonPlayerMixin extends LivingEntity implements HarpoonOwner {
    
    @Unique
    private static final Vector3dc cugMod$PENAR = new Vector3d(0.0d, 1.25d, 0.0d);
    
    protected HarpoonPlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }
    
    @Unique
    @Nullable
    private HarpoonEntity cugMod$harpoon;
    
    @Override
    public void cugMod$setHarpoon(HarpoonEntity harpoon) {
        this.cugMod$harpoon = harpoon;
    }
    
    @Override
    @Nullable
    public HarpoonEntity cugMod$getHarpoon() {
        return this.cugMod$harpoon;
    }
    
    @Override
    public Vector3dc cugMod$getAttachmentPoint() {
        return cugMod$PENAR;
    }
    
    @Override
    public void cugMod$onTouchHarpoon(HarpoonEntity harpoon) {
        this.take(harpoon, 1);
    }
    
}
