    package org.bowserfartgif.cugmod.registry;

    import net.minecraft.client.Minecraft;
    import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
    import net.minecraft.core.HolderLookup;
    import net.minecraft.data.DataGenerator;
    import net.minecraft.data.PackOutput;
    import net.minecraft.data.loot.LootTableProvider;
    import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
    import net.neoforged.api.distmarker.Dist;
    import net.neoforged.bus.api.SubscribeEvent;
    import net.neoforged.fml.common.EventBusSubscriber;
    import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
    import net.neoforged.neoforge.client.event.EntityRenderersEvent;
    import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
    import net.neoforged.neoforge.common.data.ExistingFileHelper;
    import net.neoforged.neoforge.data.event.GatherDataEvent;
    import org.bowserfartgif.cugmod.Cugmod;
    import org.bowserfartgif.cugmod.content.control.wing.ControlSurfaceBlockEntity;
    import org.bowserfartgif.cugmod.content.control.wing.ControlSurfaceRenderer;
    import org.bowserfartgif.cugmod.particle_bullshit.ThrusterParticle;
    import org.bowserfartgif.cugmod.registry.data.DoodooBlockTagsProvider;
    import org.bowserfartgif.cugmod.registry.data.DoodooItemTagsProvider;
    import org.bowserfartgif.cugmod.registry.data.DoodooLanguageProvider;
    import org.bowserfartgif.cugmod.registry.data.DoodooLootTableProvider;
    import org.bowserfartgif.cugmod.registry.util.BlockBuilder;
    
    import java.util.List;
    import java.util.Set;
    import java.util.concurrent.CompletableFuture;
    
    @EventBusSubscriber(modid = Cugmod.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public class DoodooClientEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            Cugmod.LOGGER.info("HEEELLLPPPPP HELP ME HEELLPPPP PLEASEEEE - I Am Kipti Discord");
            Cugmod.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
            DoodooPartialModels.bootstrap();
            BlockBuilder.registerRenderTypes();
            DoodooBlockEntityRenderers.register();
        }
        
        @SubscribeEvent
        public static void registerParticles(RegisterParticleProvidersEvent event) {
            event.registerSpriteSet(DoodooParticleTypes.THRUSTER_PARTICLE.get(), ThrusterParticle.Provider::new);
        }
        
        @SubscribeEvent
        public static void gatherData(GatherDataEvent event) {
            DataGenerator generator = event.getGenerator();
            PackOutput output = generator.getPackOutput();
            CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
            ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
            
            generator.addProvider(event.includeServer(),
                                  new LootTableProvider(output, Set.of(),
                                                        List.of(new LootTableProvider.SubProviderEntry(
                                                                DoodooLootTableProvider::new, LootContextParamSets.BLOCK)),
                                                        lookupProvider));
            
            generator.addProvider(event.includeClient(),
                                  new DoodooLanguageProvider(output, "en_us"));
            
            generator.addProvider(event.includeServer(),
                                  new DoodooBlockTagsProvider(output, lookupProvider, existingFileHelper));
            
            generator.addProvider(event.includeServer(),
                                  new DoodooItemTagsProvider(output, lookupProvider, existingFileHelper));
        }
    }