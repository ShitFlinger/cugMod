    package org.bowserfartgif.cugmod.registry;

    import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
    import net.neoforged.api.distmarker.Dist;
    import net.neoforged.bus.api.SubscribeEvent;
    import net.neoforged.fml.common.EventBusSubscriber;
    import net.neoforged.neoforge.client.event.EntityRenderersEvent;
    import org.bowserfartgif.cugmod.Cugmod;
    import org.bowserfartgif.cugmod.content.control.wing.ControlSurfaceBlockEntity;
    import org.bowserfartgif.cugmod.content.control.wing.ControlSurfaceRenderer;

    @EventBusSubscriber(modid = Cugmod.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public class DoodooClientEvents {

        @SubscribeEvent
        public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(DoodooBlockEntities.CONTROL_SURFACE.get(), ControlSurfaceRenderer::new);
        }
    }