package org.bowserfartgif.cugmod.content.control.joints;

import dev.ryanhcode.sable.api.block.BlockEntitySubLevelActor;
import dev.ryanhcode.sable.api.block.propeller.BlockEntitySubLevelPropellerActor;
import dev.ryanhcode.sable.api.physics.constraint.ConstraintJointAxis;
import dev.ryanhcode.sable.api.physics.constraint.PhysicsConstraintConfiguration;
import dev.ryanhcode.sable.api.physics.constraint.PhysicsConstraintHandle;
import dev.ryanhcode.sable.api.physics.handle.RigidBodyHandle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.bowserfartgif.cugmod.registry.DoodooBlockEntities;
import org.joml.Vector3d;

public class HingeBlockEntity extends BlockEntity implements BlockEntitySubLevelActor, PhysicsConstraintHandle, PhysicsConstraintConfiguration {
    public HingeBlockEntity(BlockPos pos, BlockState blockState){
        super(DoodooBlockEntities.HINGE.get(), pos, blockState);
    }

    @Override
    public void getJointImpulses(Vector3d linearImpulseDest, Vector3d angularImpulseDest) {

    }

    @Override
    public void setContactsEnabled(boolean enabled) {

    }

    @Override
    public void setMotor(ConstraintJointAxis axis, double target, double stiffness, double damping, boolean hasMaxForce, double maxForce) {

    }

    @Override
    public void remove() {

    }

    @Override
    public boolean isValid() {
        return false;
    }
}
