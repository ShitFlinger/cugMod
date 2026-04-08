package org.bowserfartgif.doodoo_mod.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import org.bowserfartgif.doodoo_mod.Doodoo_mod;
import org.bowserfartgif.doodoo_mod.content.propulsion.ThrusterBlockEntity;

public class DoodooBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Doodoo_mod.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ThrusterBlockEntity>> THRUSTER =
            BLOCK_ENTITIES.register("thruster",
                    () -> BlockEntityType.Builder.of(
                            ThrusterBlockEntity::new,
                            DoodooBlocks.THRUSTER.get()
                    ).build(null)
            );
}