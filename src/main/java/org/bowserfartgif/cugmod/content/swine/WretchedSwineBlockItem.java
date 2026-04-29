package org.bowserfartgif.cugmod.content.swine;

import dev.ryanhcode.sable.api.physics.force.ForceGroups;
import dev.ryanhcode.sable.api.physics.force.QueuedForceGroup;
import dev.ryanhcode.sable.api.physics.handle.RigidBodyHandle;
import dev.ryanhcode.sable.api.sublevel.ServerSubLevelContainer;
import dev.ryanhcode.sable.api.sublevel.SubLevelContainer;
import dev.ryanhcode.sable.companion.math.JOMLConversion;
import dev.ryanhcode.sable.companion.math.Pose3d;
import dev.ryanhcode.sable.platform.SableEventPlatform;
import dev.ryanhcode.sable.sublevel.ServerSubLevel;
import dev.ryanhcode.sable.sublevel.SubLevel;
import dev.ryanhcode.sable.sublevel.plot.LevelPlot;
import dev.ryanhcode.sable.sublevel.system.SubLevelPhysicsSystem;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.bowserfartgif.cugmod.registry.DoodooBlocks;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

import static dev.ryanhcode.sable.api.block.propeller.BlockEntitySubLevelPropellerActor.THRUST_POSITION;
import static dev.ryanhcode.sable.api.block.propeller.BlockEntitySubLevelPropellerActor.THRUST_VECTOR;

public class WretchedSwineBlockItem extends ItemNameBlockItem {
    
    private static final double ROOT_2 = Math.sqrt(2.0d);
    
    private final WretchedSwineBlock.Mood mood;
    
    public WretchedSwineBlockItem(Block block, Properties properties, WretchedSwineBlock.Mood mood) {
        super(block, properties);
        this.mood = mood;
    }
    
    @Nullable
    @Override
    protected BlockState getPlacementState(BlockPlaceContext context) {
        BlockState state = super.getPlacementState(context);
        if (state == null) {
            return null;
        }
        return this.getPlacementState(context.getLevel(), state);
    }
    
    BlockState getPlacementState(Level level, BlockState state) {
        boolean hasHat = level.random.nextFloat() <= 0.1f;
        if (level.isClientSide()) {
            hasHat = false;
        }
        return state.setValue(WretchedSwineBlock.MOOD, this.mood).setValue(WretchedSwineBlock.HAT, hasHat);
    }
    
    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if (entity instanceof Player player && !level.isClientSide() ) {
            ServerSubLevelContainer container = SubLevelContainer.getContainer((ServerLevel) level);
            
            assert container != null;
            
            Vec3 playerPos = player.getEyePosition();
            Vector3d viewVector = JOMLConversion.toJOML(player.getViewVector(1.0f));
            Vector3d oldViewVector = JOMLConversion.toJOML(player.getViewVector(0.0f));
            
            int timeCharged = this.getUseDuration(stack, entity) - timeLeft;
            Vector3d viewDiff = viewVector.sub(oldViewVector, new Vector3d());
            double power = BowItem.getPowerForTime(timeCharged) * Math.min(1.25d * viewDiff.length(), 1.0d);
            
            if (power < 0.2d) {
                return;
            }
            stack.consume(1, entity);
            
            Vector3d spawnLocation = viewVector.mul(ROOT_2, new Vector3d());
            
            Pose3d pose = new Pose3d();
            pose.position().set(playerPos.x + spawnLocation.x, playerPos.y + spawnLocation.y, playerPos.z + spawnLocation.z);
            
            //rotate to match the player's view vector. why does it look so shit? idk.
            //i wrote this for cassini's space-swimming and it just kinda works ig
            pose.orientation().rotationY((180.0f - player.getYRot()) * Mth.DEG_TO_RAD).rotateX(-player.getXRot() * Mth.DEG_TO_RAD);
            
            ServerSubLevel subLevel = (ServerSubLevel) container.allocateNewSubLevel(pose);
            LevelPlot plot = subLevel.getPlot();
            ChunkPos center = plot.getCenterChunk();
            plot.newEmptyChunk(center);
            
            plot.getEmbeddedLevelAccessor().setBlock(BlockPos.ZERO, this.getPlacementState(level, DoodooBlocks.SWINE.get().defaultBlockState()), 3);
            subLevel.updateLastPose();
            
            SubLevelPhysicsSystem physicsSystem = container.physicsSystem();
            RigidBodyHandle handle = physicsSystem.getPhysicsHandle(subLevel);
            
            power *=  12.5d;
            
            Vector3d angularVelocity = new Vector3d(0.0d, 1.0d, 0.0d);
            viewDiff.cross(viewDiff.cross(angularVelocity, angularVelocity), angularVelocity);
            handle.addLinearAndAngularVelocity(viewVector.add(viewDiff).mul(power), angularVelocity.mul(-0.25d * power));
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

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(itemstack);
    }
}
