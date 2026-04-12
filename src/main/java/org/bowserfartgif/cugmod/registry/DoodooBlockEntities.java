package org.bowserfartgif.cugmod.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import org.bowserfartgif.cugmod.Cugmod;
import org.bowserfartgif.cugmod.content.control.wing.ControlSurfaceBlockEntity;
import org.bowserfartgif.cugmod.content.control.wing.ControlSurfaceControllerCoreBlockEntity;
import org.bowserfartgif.cugmod.content.control.wing.WingBlockEntity;
import org.bowserfartgif.cugmod.content.propulsion.ThrusterBlockEntity;

public class DoodooBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Cugmod.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ThrusterBlockEntity>> THRUSTER =
            BLOCK_ENTITIES.register("thruster",
                    () -> BlockEntityType.Builder.of(
                            ThrusterBlockEntity::new,
                            DoodooBlocks.THRUSTER.get()
                    ).build(null)
            );

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<WingBlockEntity>> WING =
            BLOCK_ENTITIES.register("wing",
                    () -> BlockEntityType.Builder.of(
                            WingBlockEntity::new,
                            DoodooBlocks.WING.get()
                    ).build(null)
            );

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ControlSurfaceBlockEntity>> CONTROL_SURFACE =
            BLOCK_ENTITIES.register("control_surface",
                    () -> BlockEntityType.Builder.of(
                            ControlSurfaceBlockEntity::new,
                            DoodooBlocks.CONTROL_SURFACE.get()
                    ).build(null)
            );

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ControlSurfaceControllerCoreBlockEntity>> CONTROL_SURFACE_CONTROLLER_BLOCK =
            BLOCK_ENTITIES.register("control_surface_controller",
                    () -> BlockEntityType.Builder.of(
                            ControlSurfaceControllerCoreBlockEntity::new,
                            DoodooBlocks.CONTROL_SURFACE_CONTROLLER_BLOCK.get()
                    ).build(null)
            );
}