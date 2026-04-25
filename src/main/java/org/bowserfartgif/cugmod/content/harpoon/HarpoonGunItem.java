package org.bowserfartgif.cugmod.content.harpoon;

import dev.ryanhcode.sable.Sable;
import dev.ryanhcode.sable.companion.math.JOMLConversion;
import dev.ryanhcode.sable.companion.math.Pose3dc;
import dev.ryanhcode.sable.mixinterface.clip_overwrite.LevelPoseProviderExtension;
import dev.ryanhcode.sable.sublevel.SubLevel;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class HarpoonGunItem extends Item {
    public HarpoonGunItem(Properties properties) {
        super(properties);
    }
    
    @Override
    @NotNull
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack handItem = player.getItemInHand(usedHand);
        
        HarpoonEntity harpoon = ((HarpoonOwner) player).cugMod$getHarpoon();
        if (harpoon != null) {
            if (level instanceof ServerLevel serverLevel) {
                if (Sable.HELPER.isInPlotGrid(harpoon)) {
                    harpoon.discard();
                }
                harpoon.retract();
            }
        } else {
            if (level instanceof ServerLevel serverLevel) {
                harpoon = new HarpoonEntity(serverLevel, player, 40.0d);
                harpoon.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f, 3.0f, 0.0f);
                serverLevel.addFreshEntity(harpoon);
            }
        }

        return InteractionResultHolder.fail(handItem);
    }
}
