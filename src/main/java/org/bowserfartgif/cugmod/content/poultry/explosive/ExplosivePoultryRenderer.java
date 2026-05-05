package org.bowserfartgif.cugmod.content.poultry.explosive;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.Mth;

public class ExplosivePoultryRenderer implements BlockEntityRenderer<ExplosivePoultryBlockEntity> {
    
    public ExplosivePoultryRenderer(BlockEntityRendererProvider.Context context) {
    
    }
    
    @Override
    public void render(
            ExplosivePoultryBlockEntity be,
            float v,
            PoseStack poseStack,
            MultiBufferSource multiBufferSource,
            int i,
            int i1
    ) {
        VertexConsumer builder = multiBufferSource.getBuffer(RenderType.debugFilledBox());
        
        if (be.fuze > 0) {
            float fuze = Mth.lerp(v, be.lastFuze, be.fuze);
            float sin = 0.5f * (Mth.sin(fuze) + 1.0f);
            float scale = 0.015f * sin;
            LevelRenderer.addChainedFilledBoxVertices(
                    poseStack, builder,
                    0.0625f-scale, 0.0f-scale, 0.0625f-scale,
                    0.9375f+scale, 0.875f+scale, 0.9375f+scale,
                    1.0f, 0.3f, 0.1f, 0.25f * (sin + 0.5f)
            );
        }
        
    }
}
