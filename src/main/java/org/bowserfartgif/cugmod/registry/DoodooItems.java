package org.bowserfartgif.cugmod.registry;

import foundry.veil.platform.registry.RegistrationProvider;
import foundry.veil.platform.registry.RegistryObject;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import org.bowserfartgif.cugmod.content.harpoon.HarpoonGunItem;
import org.bowserfartgif.cugmod.registry.util.ItemBuilder;

import java.util.function.Function;

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
    
    public static final RegistryObject<HarpoonGunItem> HARPOON_GUN = item("harpoon_gun", HarpoonGunItem::new)
            .properties(() -> new Item.Properties().rarity(Rarity.RARE).stacksTo(1))
            .lang("Harpoon Gun")
            .build();
    
    private static <I extends Item> ItemBuilder<I> item(String name, Function<Item.Properties, I> factory) {
        return new ItemBuilder<>(name, factory);
    }
    
}
