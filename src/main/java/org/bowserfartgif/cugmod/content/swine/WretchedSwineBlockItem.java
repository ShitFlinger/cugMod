package org.bowserfartgif.cugmod.content.swine;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.ryanhcode.sable.api.block.propeller.BlockEntityPropeller;
import dev.ryanhcode.sable.api.block.propeller.BlockEntitySubLevelPropellerActor;
import dev.ryanhcode.sable.api.command.SableCommandHelper;
import dev.ryanhcode.sable.api.physics.PhysicsPipeline;
import dev.ryanhcode.sable.api.physics.PhysicsPipelineBody;
import dev.ryanhcode.sable.api.physics.force.ForceGroups;
import dev.ryanhcode.sable.api.physics.force.QueuedForceGroup;
import dev.ryanhcode.sable.api.physics.handle.RigidBodyHandle;
import dev.ryanhcode.sable.api.sublevel.ServerSubLevelContainer;
import dev.ryanhcode.sable.api.sublevel.SubLevelContainer;
import dev.ryanhcode.sable.companion.SableCompanion;
import dev.ryanhcode.sable.companion.impl.SableCompanionUtil;
import dev.ryanhcode.sable.companion.math.JOMLConversion;
import dev.ryanhcode.sable.companion.math.Pose3d;
import dev.ryanhcode.sable.sublevel.ServerSubLevel;
import dev.ryanhcode.sable.sublevel.SubLevel;
import dev.ryanhcode.sable.sublevel.plot.LevelPlot;
import dev.ryanhcode.sable.sublevel.system.SubLevelPhysicsSystem;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;
import org.bowserfartgif.cugmod.registry.DoodooBlocks;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

import static dev.ryanhcode.sable.api.block.propeller.BlockEntitySubLevelPropellerActor.THRUST_POSITION;
import static dev.ryanhcode.sable.api.block.propeller.BlockEntitySubLevelPropellerActor.THRUST_VECTOR;

public class WretchedSwineBlockItem extends BlockItem {
    
    private final WretchedSwineBlock.Mood mood;
    
    public WretchedSwineBlockItem(Properties properties, WretchedSwineBlock.Mood mood) {
        super(DoodooBlocks.SWINE.get(), properties);
        this.mood = mood;
    }
    
    @Nullable
    @Override
    protected BlockState getPlacementState(BlockPlaceContext context) {
        BlockState state = super.getPlacementState(context);
        if (state == null) {
            return null;
        }
        return state.setValue(WretchedSwineBlock.MOOD, this.mood);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof Player player) {
            final SubLevelContainer plotContainer = SubLevelContainer.getContainer(level);

            final Vec3 playerPos = player.position();

            final Pose3d pose = new Pose3d();
            pose.position().set(playerPos.x, playerPos.y, playerPos.z);

            final SubLevel subLevel = plotContainer.allocateNewSubLevel(pose);
            final LevelPlot plot = subLevel.getPlot();

            final ChunkPos center = plot.getCenterChunk();
            plot.newEmptyChunk(center);

            plot.getEmbeddedLevelAccessor().setBlock(BlockPos.ZERO, DoodooBlocks.SWINE.get().defaultBlockState(), 3);
            subLevel.updateLastPose();
        }
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    public void applyForces(final ServerSubLevel subLevel, final Vec3 thrustDirection, final BlockPos blockPos, final double timeStep) {
        final Vec3 thrust = thrustDirection.scale(1 * timeStep);

        THRUST_POSITION.set(JOMLConversion.atCenterOf(blockPos));
        THRUST_VECTOR.set(thrust.x, thrust.y, thrust.z);

        final QueuedForceGroup forceGroup = subLevel.getOrCreateQueuedForceGroup(ForceGroups.PROPULSION.get());
        forceGroup.applyAndRecordPointForce(new Vector3d(THRUST_POSITION), new Vector3d(THRUST_VECTOR));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        if (!level.isClientSide) {

            ServerSubLevelContainer plotContainer = (ServerSubLevelContainer) SubLevelContainer.getContainer(level);

            Vec3 playerPos = player.getEyePosition();

            Pose3d pose = new Pose3d();
            pose.position().set(playerPos.x, playerPos.y, playerPos.z);

            SubLevel subLevel = plotContainer.allocateNewSubLevel(pose);
            subLevel.setName("Jerry");

            LevelPlot plot = subLevel.getPlot();

            ChunkPos center = plot.getCenterChunk();
            plot.newEmptyChunk(center);

            plot.getEmbeddedLevelAccessor().setBlock(
                    BlockPos.ZERO,
                    DoodooBlocks.SWINE.get().defaultBlockState(),
                    3
            );

            subLevel.updateLastPose();

            // fucking kill me. guy I have no idea how to apply physics.
            // I looked at SablePhysicsCommands.executeLinearImpulseCommand, I looked at Thrusters in both cugmod and cassini and they both do weird stuff I can't use.
            // KILL MEEEEE
            SubLevelPhysicsSystem system = SubLevelPhysicsSystem.get(level);
            system.getPhysicsHandle((ServerSubLevel) subLevel)
                    .applyLinearImpulse(
                            JOMLConversion.toJOML(new Vec3(20, 1, 0))
                    );
        }

        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide);
    }
}
