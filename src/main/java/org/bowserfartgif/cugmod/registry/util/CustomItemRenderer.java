package org.bowserfartgif.cugmod.registry.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public abstract class CustomItemRenderer extends BlockEntityWithoutLevelRenderer {
    
    private static final Direction[] directions = Direction.values();
    
    public CustomItemRenderer() {
        super(null, null);
    }
    
    @Override
    public void renderByItem(
            ItemStack itemStack,
            ItemDisplayContext displayContext,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight,
            int packedOverlay
    ) {
        
        BakedModel model = Minecraft.getInstance()
                .getItemRenderer()
                .getModel(itemStack, null, null, 42);
        
        this.render(itemStack, model, displayContext, poseStack, bufferSource, packedLight, packedOverlay);
    }
    
    protected abstract void render(
            ItemStack itemStack,
            BakedModel model,
            ItemDisplayContext displayContext,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight,
            int packedOverlay
    );
    
    protected static void renderModelAndFoil(
            ItemStack itemStack,
            BakedModel model,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight,
            int packedOverlay,
            boolean fabulous
    ) {
        if (itemStack.hasFoil()) {
            renderModelFoil(itemStack, model, poseStack, bufferSource, packedLight, packedOverlay, fabulous);
        } else {
            renderModel(itemStack, model, poseStack, bufferSource, packedLight, packedOverlay, fabulous);
        }
        
    }
    
    
    protected static void renderModel(
            ItemStack itemStack,
            BakedModel model,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight,
            int packedOverlay,
            boolean fabulous
    ) {
        RandomSource random = RandomSource.create(42L);
        List<BakedModel> renderPasses = model.getRenderPasses(itemStack, fabulous);
        List<RenderType> renderTypes = model.getRenderTypes(itemStack, fabulous);
        
        PoseStack.Pose pose = poseStack.last();
        
        for (int i = 0; i < renderPasses.size(); i++) {
            BakedModel pass = renderPasses.get(i);
            RenderType renderType = renderTypes.get(i);
            
            random.setSeed(42L);
            renderPass(pose, pass, bufferSource, renderType, random, packedLight, packedOverlay);
        }
    }
    
    protected static void renderModelFoil(
            ItemStack itemStack,
            BakedModel model,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight,
            int packedOverlay,
            boolean fabulous
    ) {
        RandomSource random = RandomSource.create(42L);
        List<BakedModel> renderPasses = model.getRenderPasses(itemStack, fabulous);
        List<RenderType> renderTypes = model.getRenderTypes(itemStack, fabulous);
        
        PoseStack.Pose pose = poseStack.last();
        
        for (int i = 0; i < renderPasses.size(); i++) {
            BakedModel pass = renderPasses.get(i);
            RenderType renderType = renderTypes.get(i);
            
            random.setSeed(42L);
            renderPassFoil(pose, pass, bufferSource, renderType, random, packedLight, packedOverlay);
        }
    }
    
    protected static void renderPass(
            PoseStack.Pose pose,
            BakedModel pass,
            MultiBufferSource bufferSource,
            RenderType renderType,
            RandomSource random,
            int packedLight,
            int packedOverlay
    ) {
        VertexConsumer builder = bufferSource.getBuffer(renderType);
        
        for (Direction side : directions) {
            renderQuads(pose, pass, renderType, side, random, builder, packedLight, packedOverlay);
        }
        
        renderQuads(pose, pass, renderType, null, random, builder, packedLight, packedOverlay);
    }
    
    protected static void renderPassFoil(
            PoseStack.Pose pose,
            BakedModel pass,
            MultiBufferSource bufferSource,
            RenderType renderType,
            RandomSource random,
            int packedLight,
            int packedOverlay
    ) {
        VertexConsumer builder = ItemRenderer.getFoilBuffer(bufferSource, renderType, true, true);
        
        for (Direction side : directions) {
            renderQuads(pose, pass, renderType, side, random, builder, packedLight, packedOverlay);
        }
        
        renderQuads(pose, pass, renderType, null, random, builder, packedLight, packedOverlay);
    }
    
    protected static void renderQuads(
            PoseStack.Pose pose,
            BakedModel pass,
            RenderType renderType,
            Direction side,
            RandomSource random,
            VertexConsumer builder,
            int packedLight,
            int packedOverlay
    ) {
        List<BakedQuad> quads = pass.getQuads(null, side, random, null, renderType);
        for (int j = 0; j < quads.size(); j++) {
            BakedQuad quad = quads.get(j);
            builder.putBulkData(pose, quad, 1.0f, 1.0f, 1.0f, 1.0f, packedLight, packedOverlay);
        }
    }
}
