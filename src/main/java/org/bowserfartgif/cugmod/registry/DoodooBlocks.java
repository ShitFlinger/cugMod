package org.bowserfartgif.cugmod.registry;

import foundry.veil.platform.registry.RegistrationProvider;
import foundry.veil.platform.registry.RegistryObject;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.bowserfartgif.cugmod.content.control.joints.HingeBlock;
import org.bowserfartgif.cugmod.content.control.wing.CamberedWingBlock;
import org.bowserfartgif.cugmod.content.control.wing.ControlSurfaceBlock;
import org.bowserfartgif.cugmod.content.control.wing.WingBlock;
import org.bowserfartgif.cugmod.content.propulsion.ThrusterBlock;
import org.bowserfartgif.cugmod.content.swine.WretchedSwineBlock;
import org.bowserfartgif.cugmod.content.swine.WretchedSwineBlockItem;
import org.bowserfartgif.cugmod.registry.util.BlockBuilder;

import java.util.function.Function;

import static org.bowserfartgif.cugmod.Cugmod.MODID;

public class DoodooBlocks {
    
    public static final RegistrationProvider<Item> ITEMS = RegistrationProvider.get(Registries.ITEM, MODID);
    
    public static void bootstrap() {
    }
    
    public static final RegistryObject<ThrusterBlock> THRUSTER = block("thruster", ThrusterBlock::new)
            .properties(() -> BlockBehaviour.Properties.of()
                    .noOcclusion()
                    .strength(0.2f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.COPPER))
            .lang("Thruster")
            .dropSelf()
            .simpleItem()
            .build();
    
    public static final RegistryObject<WingBlock> WING = block("wing", WingBlock::new)
            .properties(() -> BlockBehaviour.Properties.of()
                    .noOcclusion()
                    .strength(0.2f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.COPPER))
            .lang("Wing Panel")
            .dropSelf()
            .simpleItem()
            .build();
    
    public static final RegistryObject<CamberedWingBlock> CAMBERED_WING = block("cambered_wing", CamberedWingBlock::new)
            .properties(() -> BlockBehaviour.Properties.of()
                    .noOcclusion()
                    .strength(0.2f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.COPPER))
            .lang("Cambered Wing")
            .dropSelf()
            .simpleItem()
            .build();
    
    
    public static final RegistryObject<ControlSurfaceBlock> CONTROL_SURFACE = block("control_surface", ControlSurfaceBlock::new)
            .properties(() -> BlockBehaviour.Properties.of()
                    .noOcclusion()
                    .strength(0.2f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.COPPER))
            .lang("Control Surface")
            .dropSelf()
            .simpleItem()
            .build();
    
    public static final RegistryObject<WretchedSwineBlock> SWINE = block("wretched_swine", WretchedSwineBlock::new)
            .properties(() -> BlockBehaviour.Properties.of()
                                .noOcclusion()
                                .strength(0.2f)
                                .requiresCorrectToolForDrops()
                                .sound(DoodooSoundTypes.SWINE))
            .lang("Wretched Swine")
            .dropSelf()
            .simpleItem()
            .build();
    
    public static final RegistryObject<HingeBlock> HINGE = block("hinge", HingeBlock::new)
            .properties(() -> BlockBehaviour.Properties.of()
                                .noOcclusion()
                                .strength(0.2f)
                                .requiresCorrectToolForDrops()
                                .sound(SoundType.COPPER))
            .lang("Hinge")
            .dropSelf()
            .simpleItem()
            .build();
    
    public static void registerBlockItems() {
        registerSpecialBlockItems(ITEMS);
    }
    
    private static void registerSpecialBlockItems(RegistrationProvider<Item> registrar) {
        registerSwineBlockItems(registrar);
        
    }
    
    private static void registerSwineBlockItems(RegistrationProvider<Item> registrar) {
        registrar.register(
                "wretched_swine",
                () -> new WretchedSwineBlockItem(new Item.Properties(), WretchedSwineBlock.Mood.HAPPY)
        );
        registrar.register(
                "burnt_swine",
                () -> new WretchedSwineBlockItem(new Item.Properties(), WretchedSwineBlock.Mood.BURNT)
        );
        registrar.register(
                "angry_swine",
                () -> new WretchedSwineBlockItem(new Item.Properties(), WretchedSwineBlock.Mood.ANGRY)
        );
    }
    
    private static <B extends Block> BlockBuilder<B> block(String name, Function<BlockBehaviour.Properties, B> factory) {
        return new BlockBuilder<>(name, factory);
    }
}