package org.bowserfartgif.cugmod.registry.data;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimaps;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.bowserfartgif.cugmod.Cugmod;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class DoodooItemTagsProvider extends IntrinsicHolderTagsProvider<Item> {
    
    private static final ListMultimap<TagKey<Item>, Supplier<? extends Item>> ITEM_TAGS = Multimaps.newListMultimap(new Object2ObjectArrayMap<>(), ObjectArrayList::new);
    
    public DoodooItemTagsProvider(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> lookupProvider,
            @Nullable ExistingFileHelper existingFileHelper
    ) {
        super(output, Registries.ITEM, lookupProvider, (item) -> BuiltInRegistries.ITEM.getResourceKey(item).orElseThrow(), Cugmod.MODID, existingFileHelper);
    }
    
    @Override
    protected void addTags(HolderLookup.Provider provider) {
        for (TagKey<Item> tag : ITEM_TAGS.keySet()) {
            IntrinsicTagAppender<Item> tagAppender = this.tag(tag);
            for (Supplier<? extends Item> block : ITEM_TAGS.get(tag)) {
                tagAppender.add(block.get());
            }
        }
    }
    
    public static void addItemTag(TagKey<Item> tag, Supplier<? extends Item> item) {
        ITEM_TAGS.put(tag, item);
    }
}
