package org.bowserfartgif.cugmod.registry;

import com.ibm.icu.text.RelativeDateTimeFormatter;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.bowserfartgif.cugmod.Cugmod;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.bowserfartgif.cugmod.content.WretchedSwineBlock;
import org.bowserfartgif.cugmod.content.control.wing.ControlSurfaceBlock;
import org.bowserfartgif.cugmod.content.control.wing.ControlSurfaceControllerCoreBlock;
import org.bowserfartgif.cugmod.content.control.wing.WingBlock;
import org.bowserfartgif.cugmod.content.control.wing.WingBlockEntity;
import org.bowserfartgif.cugmod.content.propulsion.ThrusterBlock;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
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

    public static final DeferredHolder<Block, ControlSurfaceBlock> CONTROL_SURFACE =
            BLOCKS.register("control_surface",
                    () -> new ControlSurfaceBlock(BlockBehaviour.Properties.of()
                            .noOcclusion()
                            .strength(0.2f)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.COPPER)
                    )
            );

    public static final DeferredHolder<Block, Block> SWINE =
            BLOCKS.register("wretched_swine",
                    () -> new WretchedSwineBlock(BlockBehaviour.Properties.of()
                            .noOcclusion()
                            .strength(0.2f)
                            .requiresCorrectToolForDrops()
                            .sound(DoodooSoundTypes.SWINE)
                    )
            );

    public static final DeferredHolder<Block, ControlSurfaceControllerCoreBlock> CONTROL_SURFACE_CONTROLLER_BLOCK =
            BLOCKS.register("control_surface_controller",
                    () -> new ControlSurfaceControllerCoreBlock(BlockBehaviour.Properties.of()
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