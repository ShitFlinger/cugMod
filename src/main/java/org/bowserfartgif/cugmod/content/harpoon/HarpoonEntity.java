package org.bowserfartgif.cugmod.content.harpoon;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.bowserfartgif.cugmod.content.harpoon.rope.server.ServerRopeManager;
import org.bowserfartgif.cugmod.registry.DoodooEntities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaterniond;
import org.joml.Quaterniondc;
import org.joml.Vector3d;
import org.joml.Vector3dc;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class HarpoonEntity extends AbstractArrow implements RopeAttachmentEntity {
    
    private static final Quaterniondc IDENTITY = new Quaterniond();
    private static final Vector3dc SLIGHTLY_DOWN = new Vector3d(0.0d, -0.125d, 0.0d);
    
    private double maxLength;
    private boolean retracting = false;
    private ServerRopeManager.RopeHandle ropeHandle;
    
    public HarpoonEntity(EntityType<HarpoonEntity> entityType, Level level) {
        super(entityType, level);
    }
    
    public HarpoonEntity(ServerLevel level, Entity shooter, double maxLength) {
        super(DoodooEntities.HARPOON.get(), level);
        if (!(shooter instanceof HarpoonOwner)) {
            throw new IllegalArgumentException("Shooter isnt an HarpoonOwner!");
        }
        
        this.maxLength = maxLength;
        double x = shooter.getX();
        double y = shooter.getEyeY() - 0.1d;
        double z = shooter.getZ();
        this.setPos(x, y, z);
        this.setOwner(shooter);
        if (this.level() instanceof ServerLevel serverLevel) {
            this.ropeHandle = ServerRopeManager.getManager(serverLevel).createRope(this, this.getHarpoonOwner());
        }
        
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
        
        if (this.ropeHandle != null && !(this.inGround || this.retracting)) {
            Vector3dc lastPoint = this.ropeHandle.lastPoint();
            Vector3dc attachmentPos = this.ropeHandle.endAttachment();
            double distance = attachmentPos.distance(lastPoint);
            float newPoints = (int) (distance/1.5f);
            if (newPoints > 0) {
                Vector3d direction = attachmentPos.sub(lastPoint, new Vector3d()).normalize(1.5f);
                Vector3d newPoint = new Vector3d(lastPoint);
                for (int i = 0; i < newPoints; i++) {
                    this.ropeHandle.addPoint(newPoint.add(direction));
                }
            }
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
        
        Vec3 pos = this.position();
        Vector3d dirToPlayer = new Vector3d(owner.getX(), owner.getEyeY() - 0.1d, owner.getZ()).sub(pos.x, pos.y, pos.z).normalize();
        this.shoot(dirToPlayer.x, dirToPlayer.y, dirToPlayer.z, 5.0f, 0.0f);
    }
    
    @Override
    public Vector3dc cugMod$getAttachmentPoint() {
        return SLIGHTLY_DOWN;
    }
}
