package org.bowserfartgif.cugmod.registry.util;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Supplier;

public interface ItemExtensions {
    
    static Supplier<IClientItemExtensions> simpleRenderer(Supplier<BlockEntityWithoutLevelRenderer> customRenderer) {
        return () -> new SimpleRenderer(customRenderer);
    }
    
    class SimpleRenderer implements IClientItemExtensions {
        
        private final Supplier<BlockEntityWithoutLevelRenderer> customRenderer;
        
        public SimpleRenderer(Supplier<BlockEntityWithoutLevelRenderer> customRenderer) {
            this.customRenderer = customRenderer;
        }
        
        @Override
        public BlockEntityWithoutLevelRenderer getCustomRenderer() {
            return this.customRenderer.get();
        }
    }
    
}
