package org.bowserfartgif.cugmod.registry.util;

import foundry.veil.platform.registry.RegistrationProvider;
import foundry.veil.platform.registry.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.bowserfartgif.cugmod.registry.DoodooCreativeModeTab;
import org.bowserfartgif.cugmod.registry.data.DoodooItemTagsProvider;
import org.bowserfartgif.cugmod.registry.data.DoodooLanguageProvider;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.bowserfartgif.cugmod.Cugmod.MODID;

public class ItemBuilder<I extends Item> {
    
    public static final RegistrationProvider<Item> ITEMS = RegistrationProvider.get(Registries.ITEM, MODID);
    
    private final String name;
    private final Function<Item.Properties, I> factory;
    
    private Supplier<Item.Properties> itemProperties = Item.Properties::new;
    
    private Iterable<TagKey<Item>> tags = Set.of();
    
    private boolean addToCreativeTab = true;
    
    @Nullable
    private String lang = null;
    
    public ItemBuilder(String name, Function<Item.Properties, I> factory) {
        this.name = name;
        this.factory = factory;
    }
    
    public ItemBuilder<I> properties(Supplier<Item.Properties> properties) {
        this.itemProperties = properties;
        return this;
    }
    
    public ItemBuilder<I> lang(String lang) {
        this.lang = lang;
        return this;
    }
    
    public ItemBuilder<I> tags(Iterable<TagKey<Item>> tags) {
        this.tags = tags;
        return this;
    }
    
    public ItemBuilder<I> addToCreativeTab(boolean shouldAdd) {
        this.addToCreativeTab = shouldAdd;
        return this;
    }
    
    public RegistryObject<I> build() {
        RegistryObject<I> item = ITEMS.register(this.name, () -> this.factory.apply(this.itemProperties.get()));
        if (this.lang != null) {
            DoodooLanguageProvider.addItemTranslation(item, this.lang);
        }
        for (TagKey<Item> tag : this.tags) {
            DoodooItemTagsProvider.addItemTag(tag, item);
        }
        if (this.addToCreativeTab) {
            DoodooCreativeModeTab.addTabItem(item);
        }
        return item;
    }
}
