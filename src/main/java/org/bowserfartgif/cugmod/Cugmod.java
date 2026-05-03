package org.bowserfartgif.cugmod;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.api.distmarker.Dist;
import org.bowserfartgif.cugmod.registry.DoodooParticleTypes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.bowserfartgif.cugmod.particle_bullshit.ThrusterParticle;
import org.bowserfartgif.cugmod.registry.*;
import org.bowserfartgif.cugmod.registry.data.DoodooBlockTagsProvider;
import org.bowserfartgif.cugmod.registry.data.DoodooItemTagsProvider;
import org.bowserfartgif.cugmod.registry.data.DoodooLanguageProvider;
import org.bowserfartgif.cugmod.registry.data.DoodooLootTableProvider;
import org.bowserfartgif.cugmod.registry.util.BlockBuilder;
import org.slf4j.Logger;
import org.bowserfartgif.cugmod.content.propulsion.ThrusterRenderer;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Mod(Cugmod.MODID)
public class Cugmod {
    public static final String MODID = "cugmod";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Cugmod(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        DoodooSounds.SOUND_EVENTS.register(modEventBus);
        DoodooBlocks.bootstrap();
        DoodooItems.bootstrap();
        DoodooCreativeModeTab.bootstrap();
        DoodooBlockEntities.bootstrap();
        DoodooParticleTypes.PARTICLE_TYPES.register(modEventBus);
        NeoForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {}

    private void addCreative(BuildCreativeModeTabContentsEvent event) {}

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("HELP IVE BEEN TRAPPED IN THE LOGS I CANT GET OUT PLEASE I NEED HELP PLEASE IM SO SCARED");
    }
}