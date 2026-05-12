package org.bowserfartgif.cugmod.content.poultry.detonator;

import dev.ryanhcode.sable.companion.math.JOMLConversion;
import dev.ryanhcode.sable.sublevel.SubLevel;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.bowserfartgif.cugmod.content.poultry.PoultryBlock;
import org.bowserfartgif.cugmod.content.poultry.PoultryManager;
import org.bowserfartgif.cugmod.registry.DoodooDataComponents;
import org.joml.Vector3d;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class RemoteDetonatorItem extends Item {
    
    public RemoteDetonatorItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        
        ItemCooldowns cooldowns = player.getCooldowns();
        if (cooldowns.isOnCooldown(this)) {
            return InteractionResultHolder.fail(stack);
        } else if (!level.isClientSide()) {
            cooldowns.addCooldown(this, 10);
            this.tryDetonate(level, player);
        }
        return InteractionResultHolder.pass(stack);
    }
    
    private void tryDetonate(Level level, Entity entity) {
        Map<BlockPos, SubLevel> birds = PoultryManager.getBirdsNear(
                level,
                JOMLConversion.toJOML(entity.getEyePosition(0.5f)),
                JOMLConversion.toJOML(entity.getViewVector(0.5f)),
                PoultryManager.BIAS,
                1.0f
        );
        if (birds.isEmpty()) {
            return;
        }
        Vector3d blockPos = new Vector3d();
        birds.forEach((block, subLevel) -> {
            JOMLConversion.atCenterOf(block, blockPos);
            if (subLevel != null) {
                subLevel.logicalPose().transformPosition(blockPos);
            }
            
            if (level.getBlockState(block).getBlock() instanceof PoultryBlock poultryBlock) {
                poultryBlock.doSomething(level, block, entity, blockPos, subLevel);
            }
        });
    }
    
    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        stack.set(DoodooDataComponents.ENTITY_HOLDING, entity.getId());
    }
    
    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        stack.set(DoodooDataComponents.ENTITY_HOLDING, entity.getId());
        return super.onEntityItemUpdate(stack, entity);
    }
}
