package org.bowserfartgif.cugmod.content.harpoon;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.bowserfartgif.cugmod.Cugmod;
import org.jetbrains.annotations.NotNull;

public class HarpoonEntityRenderer extends ArrowRenderer<HarpoonEntity> {
    
    public static final ResourceLocation TEXTURE = Cugmod.id("textures/entity/harpoon");
    
    public HarpoonEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }
    
    @Override
    @NotNull
    public ResourceLocation getTextureLocation(@NotNull HarpoonEntity harpoon) {
        return Cugmod.id("textures/entity/harpoon.png");
    }
    
}
