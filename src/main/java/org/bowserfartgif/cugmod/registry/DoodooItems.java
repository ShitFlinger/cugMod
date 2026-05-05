package org.bowserfartgif.cugmod.registry;

import foundry.veil.platform.registry.RegistrationProvider;
import foundry.veil.platform.registry.RegistryObject;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import org.bowserfartgif.cugmod.content.poultry.detonator.RemoteDetonatorItem;
import org.bowserfartgif.cugmod.content.poultry.detonator.RemoteDetonatorRenderer;
import org.bowserfartgif.cugmod.registry.util.ItemBuilder;

import java.util.function.Function;

import static org.bowserfartgif.cugmod.Cugmod.MODID;

public class DoodooItems {
    public static final RegistrationProvider<Item> ITEMS = RegistrationProvider.get(Registries.ITEM, MODID);
    public static final Registry<Item> REGISTRY = ITEMS.asVanillaRegistry();
    
    public static void bootstrap() {
    }
    
    // Cugmod(TM) does NOT tolerate making entire classes for 1 Field. - AstralWuzHere
    // Yes it does. - GuyApooye
    // Okay it's 2 fields now. - AstralWuzHere
    public static final RegistryObject<Item> WRETCHED_DISC = item("wretched_disc", Item::new)
            .properties(p -> p.jukeboxPlayable(DoodooSounds.WRETCHED_DISC_KEY).rarity(
                    Rarity.EPIC).stacksTo(1))
            .lang("Wretched Music Disc")
            .build();

    public static final RegistryObject<RemoteDetonatorItem> REMOTE_DETONATOR = item("remote_detonator", RemoteDetonatorItem::new)
            .properties(p -> p.stacksTo(1))
            .renderer(() -> RemoteDetonatorRenderer::get)
            .lang("Remote Detonator")
            .build();
    
    private static <I extends Item> ItemBuilder<I> item(String name, Function<Item.Properties, I> factory) {
        return new ItemBuilder<>(name, factory);
    }
    
}
