package org.bowserfartgif.cugmod;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.bowserfartgif.cugmod.registry.DoodooBlocks;
import org.bowserfartgif.cugmod.registry.DoodooBlockEntities;
import org.bowserfartgif.cugmod.registry.DoodooPartialModels;
import org.bowserfartgif.cugmod.registry.DoodooSounds;
import org.slf4j.Logger;
import net.minecraft.world.item.ItemStack;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Cugmod.MODID)
public class Cugmod {
    public static final String MODID = "cugmod";
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB =
            CREATIVE_MODE_TABS.register("cugmod", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.cugmod"))
                    .icon(() -> new ItemStack(DoodooBlocks.THRUSTER.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(DoodooBlocks.THRUSTER.get().asItem());
                        output.accept(DoodooBlocks.WING.get().asItem());
                        output.accept(DoodooBlocks.CAMBERED_WING.get().asItem());
                        output.accept(DoodooBlocks.CONTROL_SURFACE.get().asItem());
                    })
                    .build());

    public Cugmod(IEventBus modEventBus, ModContainer modContainer) {

        modEventBus.addListener(this::commonSetup);
        BLOCKS.register(modEventBus);
        DoodooSounds.SOUND_EVENTS.register(modEventBus);
        DoodooBlocks.registerBlockItems(ITEMS);
        DoodooBlocks.BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        NeoForge.EVENT_BUS.register(this);
        DoodooBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        modEventBus.addListener(this::addCreative);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELP IVE BEEN TRAPPED IN THE LOGS I CANT GET OUT PLEASE I NEED HELP PLEASE IM SO SCARED");
    }

    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            // Some client setup code
            LOGGER.info("HEEELLLPPPPP HELP ME HEELLPPPP PLEASEEEE - I Am Kipti Discord");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
            DoodooPartialModels.bootstrap();
            
            registerBlockRenderLayers();
        }
        
        private static void registerBlockRenderLayers() {
            ItemBlockRenderTypes.setRenderLayer(DoodooBlocks.SWINE.get(), RenderType.cutout());
        }
    }
}
