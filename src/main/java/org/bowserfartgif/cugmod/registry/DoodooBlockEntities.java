package org.bowserfartgif.cugmod.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import org.bowserfartgif.cugmod.Cugmod;
import org.bowserfartgif.cugmod.content.control.joints.HingeBlock;
import org.bowserfartgif.cugmod.content.control.joints.HingeBlockEntity;
import org.bowserfartgif.cugmod.content.control.wing.ControlSurfaceBlockEntity;
import org.bowserfartgif.cugmod.content.control.wing.WingBlockEntity;
import org.bowserfartgif.cugmod.content.propulsion.ThrusterBlockEntity;
import org.bowserfartgif.cugmod.content.swine.WretchedSwineBlockEntity;

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

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<HingeBlockEntity>> HINGE =
            BLOCK_ENTITIES.register("hinge",
                    () -> BlockEntityType.Builder.of(
                            HingeBlockEntity::new,
                            DoodooBlocks.HINGE.get()
                    ).build(null)
            );
    
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<WretchedSwineBlockEntity>> SWINE =
            BLOCK_ENTITIES.register("wretched_swine",
                    () -> BlockEntityType.Builder.of(
                            WretchedSwineBlockEntity::new,
                            DoodooBlocks.SWINE.get()
                    ).build(null)
            );


}