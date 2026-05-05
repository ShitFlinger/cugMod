package org.bowserfartgif.cugmod.registry;

import com.mojang.serialization.Codec;
import foundry.veil.platform.registry.RegistrationProvider;
import foundry.veil.platform.registry.RegistryObject;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;

import static org.bowserfartgif.cugmod.Cugmod.MODID;

public class DoodooDataComponents {
    
    public static final RegistrationProvider<DataComponentType<?>> DATA_COMPONENT_TYPES = RegistrationProvider.get(
            Registries.DATA_COMPONENT_TYPE,
            MODID
    );
    
//    public static final RegistryObject<DataComponentType<UUID>> ENTITY_HOLDING = DATA_COMPONENT_TYPES.register(
//            "entity_holding",
//            () -> DataComponentType.<UUID>builder()
//                    .networkSynchronized(
//                            StreamCodec.ofMember(
//                                    (component, buf) -> buf.writeUUID(component),
//                                    buf -> buf.readUUID()
//                            )
//                    )
//                    .persistent(Codec.STRING.xmap(UUID::fromString, UUID::toString))
//                    .build()
//    );
    
    public static final RegistryObject<DataComponentType<Integer>> ENTITY_HOLDING = DATA_COMPONENT_TYPES.register(
            "entity_holding",
            () -> DataComponentType.<Integer>builder()
                    .networkSynchronized(ByteBufCodecs.INT)
                    .persistent(Codec.INT)
                    .build()
    );
    
    public static void bootstrap() {
    
    }
}
