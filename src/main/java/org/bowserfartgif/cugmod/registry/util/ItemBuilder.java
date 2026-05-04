package org.bowserfartgif.cugmod.registry.util;

import foundry.veil.platform.registry.RegistrationProvider;
import foundry.veil.platform.registry.RegistryObject;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.bowserfartgif.cugmod.registry.DoodooClientExtensions;
import org.bowserfartgif.cugmod.registry.DoodooCreativeModeTab;
import org.bowserfartgif.cugmod.registry.data.DoodooItemTagsProvider;
import org.bowserfartgif.cugmod.registry.data.DoodooLanguageProvider;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.bowserfartgif.cugmod.Cugmod.MODID;

public class ItemBuilder<I extends Item> {
    
    public static final RegistrationProvider<Item> ITEMS = RegistrationProvider.get(Registries.ITEM, MODID);
    
    private final String name;
    private final Function<Item.Properties, I> factory;
    
    private final Item.Properties itemProperties = new Item.Properties();
    
    private Function<Item.Properties, Item.Properties> properties = Function.identity();
    private Iterable<TagKey<Item>> tags = Set.of();
    private boolean addToCreativeTab = true;
    
    @Nullable
    private String lang = null;
    @Nullable
    private Supplier<Supplier<BlockEntityWithoutLevelRenderer>> customRenderer = null;
    
    public ItemBuilder(String name, Function<Item.Properties, I> factory) {
        this.name = name;
        this.factory = factory;
    }
    
    public ItemBuilder<I> properties(Function<Item.Properties, Item.Properties> properties) {
        this.properties = this.properties.andThen(properties);
        return this;
    }
    
    public ItemBuilder<I> lang(String lang) {
        assert this.lang == null : "Attempted to set lang twice!";
        this.lang = lang;
        return this;
    }
    
    public ItemBuilder<I> tags(Iterable<TagKey<Item>> tags) {
        assert this.tags == null : "Attempted to set tags twice!";
        this.tags = tags;
        return this;
    }
    
    public ItemBuilder<I> addToCreativeTab(boolean shouldAdd) {
        this.addToCreativeTab = shouldAdd;
        return this;
    }
    
    public ItemBuilder<I> renderer(Supplier<Supplier<BlockEntityWithoutLevelRenderer>> renderer) {
        assert this.customRenderer == null : "Attempted to add custom renderer twice!";
        this.customRenderer = renderer;
        return this;
    }
    
    public RegistryObject<I> build() {
        RegistryObject<I> item = ITEMS.register(this.name, () -> this.factory.apply(this.properties.apply(this.itemProperties)));
        if (this.lang != null) {
            DoodooLanguageProvider.addItemTranslation(item, this.lang);
        }
        for (TagKey<Item> tag : this.tags) {
            DoodooItemTagsProvider.addItemTag(tag, item);
        }
        if (this.addToCreativeTab) {
            DoodooCreativeModeTab.addTabItem(item);
        }
        if (this.customRenderer != null) {
            DoodooClientExtensions.addItemExtensions(
                    item, ItemExtensions.simpleRenderer(this.customRenderer.get())
            );
            DoodooClientExtensions.setWithCustomRenderer(item.getId());
        }
        return item;
    }
}
