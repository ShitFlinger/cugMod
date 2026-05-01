package org.bowserfartgif.cugmod.content.harpoon;

import dev.ryanhcode.sable.companion.math.JOMLConversion;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.bowserfartgif.cugmod.content.harpoon.rope.server.HarpoonRope;
import org.bowserfartgif.cugmod.content.harpoon.rope.server.ServerRopeManager;
import org.bowserfartgif.cugmod.registry.DoodooEntities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaterniond;
import org.joml.Quaterniondc;
import org.joml.Vector3d;
import org.joml.Vector3dc;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class HarpoonEntity extends AbstractArrow implements RopeAttachmentEntity {
    
    private static final Quaterniondc IDENTITY = new Quaterniond();
    private static final Vector3dc SLIGHTLY_DOWN = new Vector3d(0.0d, -0.125d, 0.0d);
    
    private double maxLength;
    protected boolean retracting = false;
    private ServerRopeManager.RopeHandle ropeHandle;
    
    public HarpoonEntity(EntityType<HarpoonEntity> entityType, Level level) {
        super(entityType, level);
    }
    
    public HarpoonEntity(ServerLevel level, Entity shooter, double maxLength) {
        super(DoodooEntities.HARPOON.get(), level);
        if (!(shooter instanceof HarpoonOwner)) {
            throw new IllegalArgumentException("Shooter isnt a HarpoonOwner!");
        }
        
        this.maxLength = maxLength;
        double x = shooter.getX();
        double y = shooter.getEyeY() - 0.1d;
        double z = shooter.getZ();
        this.setPos(x, y, z);
        this.setOwner(shooter);
        if (this.level() instanceof ServerLevel serverLevel) {
            HarpoonRope harpoonRope = new HarpoonRope(this.getHarpoonOwner(), this);
            this.ropeHandle = ServerRopeManager.getManager(serverLevel).addRope(harpoonRope);
        }
        
    }
    
    public boolean isRetracting() {
        return this.retracting;
    }
    
    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putDouble("RopeMaxLength", this.maxLength);
        compound.putBoolean("IsRetracting", this.retracting);
    }
    
    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.maxLength = compound.getDouble("RopeMaxLength");
        this.retracting = compound.getBoolean("IsRetracting");
    }
    
    @Override
    @NotNull
    protected ItemStack getDefaultPickupItem() {
        return ItemStack.EMPTY;
    }
    
    @Override
    protected boolean tryPickup(Player player) {
        return this.ownedBy(player);
    }
    
    @Override
    public void tick() {
        Level level = this.level();
        Entity owner = this.getOwner();
        if (owner != null && !level.isClientSide()) {
            double distSquared = this.distanceToSqr(owner);
            double dist = Math.sqrt(distSquared);
            if (this.maxLength < dist) {
                this.retract();
            }
        }
        if (this.retracting) {
            this.inGround = false;
            this.applyRetraction();
        }
        
        super.tick();
    }
    
    @Override
    public void remove(RemovalReason reason) {
        super.remove(reason);
        this.updateOwnerInformation(null);
        if (this.ropeHandle != null) {
            this.ropeHandle.remove();
        }
    }
    
    @Override
    public void onClientRemoval() {
        super.onClientRemoval();
        this.updateOwnerInformation(null);
    }
    
    @Nullable
    public HarpoonOwner getHarpoonOwner() {
        return (HarpoonOwner) this.getOwner();
    }
    
    @Override
    public void setOwner(@Nullable Entity entity) {
        if (entity instanceof HarpoonOwner) {
            super.setOwner(entity);
            this.updateOwnerInformation(this);
        }
    }
    
    private void updateOwnerInformation(@Nullable HarpoonEntity harpoon) {
        HarpoonOwner harpoonOwner = this.getHarpoonOwner();
        if (harpoonOwner != null) {
            harpoonOwner.cugMod$setHarpoon(harpoon);
        }
    }
    
    @Override
    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        super.shoot(x, y, z, velocity, inaccuracy);
    }
    
    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity owner = this.getOwner();
        if (owner != null && owner == result.getEntity() && !this.level().isClientSide()) {
            HarpoonOwner harpoonOwner = this.getHarpoonOwner();
            assert harpoonOwner != null;
            harpoonOwner.cugMod$onTouchHarpoon(this);
            this.discard();
        }
        
    }
    
    public void retract() {
        Entity owner = this.getOwner();
        if (owner == null) {
            return;
        }
        
        this.retracting = true;
        this.inGround = false;
    }
    
    public void applyRetraction() {
        List<Vector3d> points = this.ropeHandle.getPoints();
        int size = points.size();
        
        
        Vector3d attachmentPoint = this.ropeHandle.endAttachmentGlobal();
        Vector3d secondToLastPoint = points.get(size-2);
        
        Vector3d moveDir = secondToLastPoint.sub(attachmentPoint, new Vector3d()).mul(2.0d);
        
        this.setDeltaMovement(JOMLConversion.toMojang(moveDir));
    }
    
    @Override
    public Vector3dc cugMod$getAttachmentPoint() {
        return SLIGHTLY_DOWN;
    }
    
    @Override
    public boolean cugMod$shouldUpdateAttachment() {
        return !this.onGround();
    }
    
    @Override
    public boolean cugMod$shouldAttachToSublevel() {
        return false;
    }
}
