package org.bowserfartgif.cugmod.registry;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.SoundType;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.bowserfartgif.cugmod.Cugmod;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.bowserfartgif.cugmod.content.swine.WretchedSwineBlock;
import org.bowserfartgif.cugmod.content.control.joints.HingeBlock;
import org.bowserfartgif.cugmod.content.control.wing.CamberedWingBlock;
import org.bowserfartgif.cugmod.content.control.wing.ControlSurfaceBlock;
import org.bowserfartgif.cugmod.content.control.wing.WingBlock;
import org.bowserfartgif.cugmod.content.propulsion.ThrusterBlock;

public class DoodooBlocks {


 private static final String MODID = Cugmod.MODID;

 public static final DeferredRegister.Blocks BLOCKS =
         DeferredRegister.createBlocks(MODID);

 public static final DeferredHolder<Block, ThrusterBlock> THRUSTER =
         BLOCKS.register("thruster",
                 () -> new ThrusterBlock(BlockBehaviour.Properties.of()
                         .noOcclusion()
                         .strength(0.2f)
                         .requiresCorrectToolForDrops()
                         .sound(SoundType.COPPER)
                 )
         );

    public static final DeferredHolder<Block, WingBlock> WING =
            BLOCKS.register("wing",
                    () -> new WingBlock(BlockBehaviour.Properties.of()
                            .noOcclusion()
                            .strength(0.2f)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.COPPER)
                    )
            );

    public static final DeferredHolder<Block, CamberedWingBlock> CAMBERED_WING =
            BLOCKS.register("cambered_wing",
                    () -> new CamberedWingBlock(BlockBehaviour.Properties.of()
                            .noOcclusion()
                            .strength(0.2f)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.COPPER)
                    )
            );



    public static final DeferredHolder<Block, ControlSurfaceBlock> CONTROL_SURFACE =
            BLOCKS.register("control_surface",
                    () -> new ControlSurfaceBlock(BlockBehaviour.Properties.of()
                            .noOcclusion()
                            .strength(0.2f)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.COPPER)
                    )
            );

    public static final DeferredHolder<Block, WretchedSwineBlock> SWINE =
            BLOCKS.register("wretched_swine",
                    () -> new WretchedSwineBlock(BlockBehaviour.Properties.of()
                            .noOcclusion()
                            .strength(0.2f)
                            .requiresCorrectToolForDrops()
                            .sound(DoodooSoundTypes.SWINE)
                    )
            );


    public static final DeferredHolder<Block, HingeBlock> HINGE =
            BLOCKS.register("hinge",
                    () -> new HingeBlock(BlockBehaviour.Properties.of()
                            .noOcclusion()
                            .strength(0.2f)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.COPPER)
                    )
            );



 public static void registerBlockItems(DeferredRegister.Items itemRegistry){
     BLOCKS.getEntries().forEach(block -> {
     itemRegistry.register(block.getId().getPath(),
             () -> new BlockItem(block.get(), new Item.Properties()));
     });
 }
}