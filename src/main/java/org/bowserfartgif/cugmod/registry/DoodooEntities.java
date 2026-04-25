package org.bowserfartgif.cugmod.registry;

import foundry.veil.platform.registry.RegistrationProvider;
import foundry.veil.platform.registry.RegistryObject;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import org.bowserfartgif.cugmod.Cugmod;
import org.bowserfartgif.cugmod.content.harpoon.HarpoonEntity;

public class DoodooEntities {
    
    public static final RegistrationProvider<EntityType<?>> ENTITIES = RegistrationProvider.get(Registries.ENTITY_TYPE, Cugmod.MODID);
    public static final Registry<EntityType<?>> REGISTRY = ENTITIES.asVanillaRegistry();
    
    public static void bootstrap() {
    }
    
    public static final RegistryObject<EntityType<HarpoonEntity>> HARPOON = register(
            "harpoon",
            EntityType.Builder.<HarpoonEntity>of(HarpoonEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).eyeHeight(0.13F).clientTrackingRange(4).updateInterval(20)
    );
    
    public static <T extends Entity> RegistryObject<EntityType<T>> register(String name, EntityType.Builder<T> builder) {
        return ENTITIES.register(
                name,
                () -> builder.build(name)
        );
    }
}
