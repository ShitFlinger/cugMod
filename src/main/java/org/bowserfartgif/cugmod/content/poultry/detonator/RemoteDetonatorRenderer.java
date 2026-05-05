package org.bowserfartgif.cugmod.content.poultry.detonator;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.ryanhcode.sable.companion.math.JOMLConversion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.bowserfartgif.cugmod.content.poultry.PoultryManager;
import org.bowserfartgif.cugmod.registry.DoodooDataComponents;
import org.bowserfartgif.cugmod.registry.DoodooPartialModels;
import org.bowserfartgif.cugmod.registry.util.CustomItemRenderer;

import java.util.Arrays;

public class RemoteDetonatorRenderer extends CustomItemRenderer {
    
    private static final RemoteDetonatorRenderer INSTANCE = new RemoteDetonatorRenderer();
    
    @Override
    protected void render(
            ItemStack itemStack,
            BakedModel model,
            ItemDisplayContext displayContext,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight,
            int packedOverlay
    ) {
        Minecraft minecraft = Minecraft.getInstance();
        Integer entityId = itemStack.get(DoodooDataComponents.ENTITY_HOLDING);
        
        float cooldownPercent = 1.0f;
        boolean hasBirdsNearby = false;
        if (entityId != null) {
            Entity entity = minecraft.level.getEntity(entityId);
            float partialTick = minecraft.getTimer().getGameTimeDeltaPartialTick(false);
            if (entity instanceof Player player) {
                Item item = itemStack.getItem();
                cooldownPercent = 1.0f - player.getCooldowns().getCooldownPercent(item, partialTick);
                
                final float begin = 0.1f;
                if (cooldownPercent < begin) {
                    cooldownPercent = 1.0f - cooldownPercent / begin;
                } else {
                    cooldownPercent = (cooldownPercent - begin) / (1.0f - begin);
                }
            }
            if (entity != null) {
                if (entity instanceof LivingEntity livingEntity) {
                    InteractionHand hand = livingEntity.getUsedItemHand();
                    hasBirdsNearby = livingEntity.getItemInHand(hand).equals(itemStack);
                }
                if (hasBirdsNearby) {
                    hasBirdsNearby = PoultryManager.hasBirdsNear(
                            minecraft.level,
                            JOMLConversion.toJOML(entity.getEyePosition(partialTick)),
                            JOMLConversion.toJOML(entity.getViewVector(partialTick)),
                            PoultryManager.BIAS,
                            partialTick
                    );
                }
            }
            
        }
        
        boolean fabulous = minecraft.levelRenderer.getTranslucentTarget() != null;
        BakedModel mainModel;
        BakedModel buttonModel;
        poseStack.pushPose();
        if (hasBirdsNearby) {
            mainModel = DoodooPartialModels.DETONATOR_MAIN_GREEN.get();
            buttonModel = DoodooPartialModels.DETONATOR_BUTTON_GREEN.get();
        } else {
            mainModel = DoodooPartialModels.DETONATOR_MAIN_RED.get();
            buttonModel = DoodooPartialModels.DETONATOR_BUTTON_RED.get();
        }
        renderModelAndFoil(itemStack, mainModel, poseStack, bufferSource, packedLight, packedOverlay, fabulous);
        poseStack.translate(0.0f, 0.0f, Math.max((1.0f-cooldownPercent)/16.0f, -0.25f));
        renderModelAndFoil(itemStack, buttonModel, poseStack, bufferSource, packedLight, packedOverlay, fabulous);
        poseStack.popPose();
    }
    
    public static RemoteDetonatorRenderer get() {
        return INSTANCE;
    }
}
