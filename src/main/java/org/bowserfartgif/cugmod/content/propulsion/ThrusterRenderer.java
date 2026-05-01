package org.bowserfartgif.cugmod.content.propulsion;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.phys.Vec3;
import org.bowserfartgif.cugmod.registry.DoodooPartialModels;
public class ThrusterRenderer implements BlockEntityRenderer<ThrusterBlockEntity> {

    public ThrusterRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(ThrusterBlockEntity be, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        BlockState state = be.getBlockState();
        if (!state.getValue(ThrusterBlock.POWERED)) return;

        Direction facing = state.getValue(ThrusterBlock.FACING);

        poseStack.pushPose();
        poseStack.translate(0.5, 0.5, 0.5); // pivot around block center

        // rotate based on facing
        switch (facing) {
            case NORTH -> {} // default orientation, no rotation needed
            case SOUTH -> poseStack.mulPose(Axis.YP.rotationDegrees(180));
            case EAST  -> poseStack.mulPose(Axis.YP.rotationDegrees(-90));
            case WEST  -> poseStack.mulPose(Axis.YP.rotationDegrees(90));
            case UP    -> poseStack.mulPose(Axis.XP.rotationDegrees(-90));
            case DOWN  -> poseStack.mulPose(Axis.XP.rotationDegrees(90));
        }

        poseStack.translate(-0.5, -0.5, -0.5); // translate back

        BakedModel glowModel = DoodooPartialModels.THRUSTER_GLOW.get();
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.translucent());

        Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(
                poseStack.last(),
                consumer,
                state,
                glowModel,
                1f, 1f, 1f,
                0xF000F0,
                OverlayTexture.NO_OVERLAY
        );

        poseStack.popPose();
    }
}