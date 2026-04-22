package org.bowserfartgif.cugmod.registry.data;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimaps;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.bowserfartgif.cugmod.Cugmod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class DoodooBlockTagsProvider extends BlockTagsProvider {
    
    private static final ListMultimap<TagKey<Block>, Supplier<? extends Block>> BLOCK_TAGS = Multimaps.newListMultimap(new Object2ObjectArrayMap<>(), ObjectArrayList::new);
    
    public DoodooBlockTagsProvider(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> lookupProvider,
            @Nullable ExistingFileHelper existingFileHelper
    ) {
        super(output, lookupProvider, Cugmod.MODID, existingFileHelper);
    }
    
    @Override
    protected void addTags(@NotNull HolderLookup.Provider provider) {
        for (TagKey<Block> tag : BLOCK_TAGS.keySet()) {
            IntrinsicTagAppender<Block> tagAppender = this.tag(tag);
            for (Supplier<? extends Block> block : BLOCK_TAGS.get(tag)) {
                tagAppender.add(block.get());
            }
        }
    }
    
    public static void addBlockTag(TagKey<Block> tag, Supplier<? extends Block> block) {
        BLOCK_TAGS.put(tag, block);
    }
}
