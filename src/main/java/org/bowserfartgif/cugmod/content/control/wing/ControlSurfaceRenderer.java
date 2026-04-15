package org.bowserfartgif.cugmod.content.control.wing;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import foundry.veil.api.client.render.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.bowserfartgif.cugmod.Cugmod;
import org.bowserfartgif.cugmod.content.control.wing.ControlSurfaceBlock;
import org.bowserfartgif.cugmod.registry.DoodooPartialModels;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Random;

public class ControlSurfaceRenderer implements BlockEntityRenderer<ControlSurfaceBlockEntity> {

    private static final Quaternionf DUMMY = new Quaternionf();

    private static final RandomSource RANDOM = RandomSource.create();

    private static final ModelResourceLocation FLAP = new ModelResourceLocation(
            ResourceLocation.fromNamespaceAndPath(Cugmod.MODID, "control/wing/flappybit"),
            "standalone"
    );


    public ControlSurfaceRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(ControlSurfaceBlockEntity be, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {


        ModelManager modelManager = Minecraft.getInstance()
                .getBlockRenderer()
                .getBlockModelShaper().getModelManager();

        VertexConsumer builder = bufferSource.getBuffer(RenderType.cutout());

        poseStack.pushPose();

        ((MatrixStack) poseStack).rotateAround(
                quaternionForFacing(be.getBlockState().getValue(ControlSurfaceBlock.FACING)),
                0.5f, 0.5f, 0.5f
        );

        poseStack.pushPose();


        float targetAngle = -be.getControlSurfaceAngle() * 1.5f;


        //lets just duct tape over that lerp for now
        float angle = Mth.lerp(partialTick, targetAngle, targetAngle);



        float translatex = 0.5f;
        float translatey = 0.5f;
        float translatez = 0.5f;

        poseStack.translate(translatex,translatey,translatez);

        poseStack.mulPose(new Quaternionf().rotateX((float) Math.toRadians(angle)));

        poseStack.translate(-translatex,-translatey,-translatez);

        BakedModel model = DoodooPartialModels.FLAPPYBIT.get();
        for(var quad : model.getQuads(be.getBlockState(), null, RANDOM)){
            builder.putBulkData(poseStack.last(), quad, 1, 1, 1, 1, packedLight, packedOverlay);
        }

        poseStack.popPose();

        poseStack.popPose();
    }


    //thanks cassini
    private static Quaternionfc quaternionForFacing(Direction facing) {
        return switch (facing) {
            case UP -> DUMMY.identity().rotateX(-Mth.HALF_PI);
            case DOWN -> DUMMY.identity().rotateX(Mth.HALF_PI);
            case NORTH -> DUMMY.identity();
            case SOUTH -> DUMMY.identity().rotateY(Mth.PI);
            case EAST -> DUMMY.identity().rotateY(-Mth.HALF_PI);
            case WEST -> DUMMY.identity().rotateY(Mth.HALF_PI);
            case null -> DUMMY.identity();
        };
    }

}