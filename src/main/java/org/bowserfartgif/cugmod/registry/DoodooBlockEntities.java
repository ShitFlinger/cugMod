package org.bowserfartgif.cugmod.registry;

import foundry.veil.platform.registry.RegistrationProvider;
import foundry.veil.platform.registry.RegistryObject;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import org.bowserfartgif.cugmod.Cugmod;
import org.bowserfartgif.cugmod.content.control.wing.ControlSurfaceBlockEntity;
import org.bowserfartgif.cugmod.content.control.wing.ControlSurfaceRenderer;
import org.bowserfartgif.cugmod.content.poultry.PoultryBlockEntity;
import org.bowserfartgif.cugmod.content.poultry.explosive.ExplosivePoultryBlockEntity;
import org.bowserfartgif.cugmod.content.poultry.explosive.ExplosivePoultryRenderer;
import org.bowserfartgif.cugmod.content.propulsion.ThrusterBlockEntity;
import org.bowserfartgif.cugmod.content.propulsion.ThrusterRenderer;
import org.bowserfartgif.cugmod.content.swine.WretchedSwineBlockEntity;
import org.bowserfartgif.cugmod.registry.util.BlockBuilder;
import org.bowserfartgif.cugmod.registry.util.BlockEntityBuilder;
import org.bowserfartgif.cugmod.registry.util.TriFunction;

import java.util.function.Function;

import static org.bowserfartgif.cugmod.Cugmod.MODID;

public class DoodooBlockEntities {
    
    public static final RegistrationProvider<BlockEntityType<?>> BLOCKS_ENTITY_TYPES = RegistrationProvider.get(Registries.BLOCK_ENTITY_TYPE, MODID);
    public static final Registry<BlockEntityType<?>> REGISTRY = BLOCKS_ENTITY_TYPES.asVanillaRegistry();
    
    public static void bootstrap() {
    }
    
    public static final RegistryObject<BlockEntityType<ThrusterBlockEntity>> THRUSTER = blockEntity("thruster", ThrusterBlockEntity::new)
            .validBlocks(DoodooBlocks.THRUSTER)
            .renderer(() -> ThrusterRenderer::new)
            .build();
    
    public static final RegistryObject<BlockEntityType<ControlSurfaceBlockEntity>> CONTROL_SURFACE = blockEntity("control_surface", ControlSurfaceBlockEntity::new)
            .validBlocks(DoodooBlocks.CONTROL_SURFACE)
            .renderer(() -> ControlSurfaceRenderer::new)
            .build();
    
    public static final RegistryObject<BlockEntityType<WretchedSwineBlockEntity>> SWINE = blockEntity("wretched_swine", WretchedSwineBlockEntity::new)
            .validBlocks(DoodooBlocks.SWINE)
            .build();
    
    public static final RegistryObject<BlockEntityType<PoultryBlockEntity>> POULTRY = blockEntity("furious_poultry", PoultryBlockEntity::new)
            .validBlocks(DoodooBlocks.RED_POULTRY, DoodooBlocks.EXPLOSIVE_POULTRY)
            .build();
    
    public static final RegistryObject<BlockEntityType<ExplosivePoultryBlockEntity>> EXPLOSIVE_POULTRY = blockEntity("explosive_poultry", ExplosivePoultryBlockEntity::new)
            .validBlocks(DoodooBlocks.EXPLOSIVE_POULTRY)
            .renderer(() -> ExplosivePoultryRenderer::new)
            .build();
    
    
    private static <BE extends BlockEntity> BlockEntityBuilder<BE> blockEntity(String name, TriFunction<BlockEntityType<BE>, BlockPos, BlockState, BE> factory) {
        return new BlockEntityBuilder<>(name, factory);
    }
    
}