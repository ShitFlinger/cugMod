package org.bowserfartgif.cugmod.registry;

import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.model.BakedModelWrapper;

import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class DoodooClientExtensions {
    
    private static final Map<Supplier<? extends Item>, Supplier<IClientItemExtensions>> ITEM_EXTENSIONS = new Object2ObjectArrayMap<>();
    private static final Set<ResourceLocation> ITEMS_WITH_RENDERER = new ObjectArraySet<>();
    
    public static void register(RegisterClientExtensionsEvent event) {
        ITEM_EXTENSIONS.forEach(
                (item, extensions) -> event.registerItem(extensions.get(), item.get())
        );
    }
    
    public static void replaceWithCustomRenderers(Map<ModelResourceLocation, BakedModel> models) {
        models.replaceAll((modelLocation, model) -> withCustomRenderer(modelLocation.id(), model));
    }
    
    public static void addItemExtensions(Supplier<? extends Item> item, Supplier<IClientItemExtensions> extensions) {
        ITEM_EXTENSIONS.put(item, extensions);
    }
    
    public static void setWithCustomRenderer(ResourceLocation item) {
        ITEMS_WITH_RENDERER.add(item);
    }
    
    public static BakedModel withCustomRenderer(ResourceLocation item, BakedModel model) {
        if (ITEMS_WITH_RENDERER.contains(item)) {
            return new ModelWithCustomRenderer(model);
        }
        return model;
    }
    
    private static class ModelWithCustomRenderer extends BakedModelWrapper<BakedModel> {
        
        public ModelWithCustomRenderer(BakedModel originalModel) {
            super(originalModel);
        }
        
        @Override
        public boolean isCustomRenderer() {
            return true;
        }
        
        @Override
        public BakedModel applyTransform(
                ItemDisplayContext cameraTransformType,
                PoseStack poseStack,
                boolean applyLeftHandTransform
        ) {
            super.applyTransform(cameraTransformType, poseStack, applyLeftHandTransform);
            return this;
        }
    }
}
