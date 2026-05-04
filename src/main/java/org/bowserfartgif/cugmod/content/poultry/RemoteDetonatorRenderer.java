package org.bowserfartgif.cugmod.content.poultry;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.bowserfartgif.cugmod.registry.DoodooPartialModels;
import org.bowserfartgif.cugmod.registry.util.CustomItemRenderer;

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
        Item item = itemStack.getItem();
        float partialTick = minecraft.getTimer().getGameTimeDeltaPartialTick(false);
        float cooldownPercent = 1.0f - minecraft.player.getCooldowns().getCooldownPercent(item, partialTick);
        
        final float begin = 0.1f;
        if (cooldownPercent < begin) {
            cooldownPercent = 1.0f - cooldownPercent/begin;
        } else {
            cooldownPercent = (cooldownPercent-begin)/(1.0f-begin);
        }
        
        boolean fabulous = minecraft.levelRenderer.getTranslucentTarget() != null;
        renderModelAndFoil(itemStack, model, poseStack, bufferSource, packedLight, packedOverlay, fabulous);
        
        BakedModel buttonModel = DoodooPartialModels.DETONATOR_BUTTON.get();
        poseStack.pushPose();
        poseStack.translate(0.0f, 0.0f, Math.max((1.0f-cooldownPercent)/16.0f, -0.25f));
        renderModelAndFoil(itemStack, buttonModel, poseStack, bufferSource, packedLight, packedOverlay, fabulous);
        poseStack.popPose();
    }
    
    public static RemoteDetonatorRenderer get() {
        return INSTANCE;
    }
}
