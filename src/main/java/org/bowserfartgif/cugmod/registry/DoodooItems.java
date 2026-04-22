package org.bowserfartgif.cugmod.registry;

import foundry.veil.platform.registry.RegistrationProvider;
import foundry.veil.platform.registry.RegistryObject;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.bowserfartgif.cugmod.registry.util.BlockBuilder;
import org.bowserfartgif.cugmod.registry.util.ItemBuilder;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.bowserfartgif.cugmod.Cugmod.MODID;

public class DoodooItems {
    public static final RegistrationProvider<Item> ITEMS = RegistrationProvider.get(Registries.ITEM, MODID);
    public static final Registry<Item> REGISTRY = ITEMS.asVanillaRegistry();
    
    public static void bootstrap() {
    }
    
    // Cugmod(TM) does NOT tolerate making entire classes for 1 Field.
    // Yes it does. - GuyApooye
    public static final RegistryObject<Item> WRETCHED_DISC = item("wretched_disc", Item::new)
            .properties(() -> new Item.Properties().jukeboxPlayable(DoodooSounds.WRETCHED_DISC_KEY).rarity(
                    Rarity.EPIC).stacksTo(1))
            .lang("Wretched Music Disc")
            .build();
    
    private static <I extends Item> ItemBuilder<I> item(String name, Function<Item.Properties, I> factory) {
        return new ItemBuilder<>(name, factory);
    }
    
}
