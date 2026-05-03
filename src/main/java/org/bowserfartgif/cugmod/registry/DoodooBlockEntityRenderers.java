package org.bowserfartgif.cugmod.registry;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class DoodooBlockEntityRenderers {
    
    private static final List<TypeAndRendererPair<?>> BLOCK_ENTITY_RENDERERS = new ObjectArrayList<>();
    
    public static void register() {
        for (TypeAndRendererPair<?> blockEntityRenderer : BLOCK_ENTITY_RENDERERS) {
            register(blockEntityRenderer);
        }
    }
    
    private static <T extends BlockEntity> void register(TypeAndRendererPair<T> blockEntityRenderer) {
        BlockEntityRenderers.register(blockEntityRenderer.type.get(), context -> blockEntityRenderer.rendererProvider.get().apply(context));
    }
    
    public static <T extends BlockEntity> void addRenderer(
            Supplier<? extends BlockEntityType<? extends T>> type,
            Supplier<Function<BlockEntityRendererProvider.Context, BlockEntityRenderer<T>>> renderer
    ) {
        BLOCK_ENTITY_RENDERERS.add(new TypeAndRendererPair<>(type, renderer));
    }
    
    private record TypeAndRendererPair<T extends BlockEntity>(
            Supplier<? extends BlockEntityType<? extends T>> type,
            Supplier<Function<BlockEntityRendererProvider.Context, BlockEntityRenderer<T>>> rendererProvider
    ) {
    }
    
}
