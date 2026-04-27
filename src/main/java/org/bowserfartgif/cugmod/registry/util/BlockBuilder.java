package org.bowserfartgif.cugmod.registry.util;

import foundry.veil.platform.registry.RegistrationProvider;
import foundry.veil.platform.registry.RegistryObject;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectLists;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.storage.loot.LootTable;
import org.bowserfartgif.cugmod.registry.DoodooCreativeModeTab;
import org.bowserfartgif.cugmod.registry.data.DoodooBlockTagsProvider;
import org.bowserfartgif.cugmod.registry.data.DoodooItemTagsProvider;
import org.bowserfartgif.cugmod.registry.data.DoodooLanguageProvider;
import org.bowserfartgif.cugmod.registry.data.DoodooLootTableProvider;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.bowserfartgif.cugmod.Cugmod.MODID;

@ParametersAreNonnullByDefault
public class BlockBuilder<B extends Block> {
    
    public static final RegistrationProvider<Block> BLOCKS = RegistrationProvider.get(Registries.BLOCK, MODID);
    public static final RegistrationProvider<Item> ITEMS = RegistrationProvider.get(Registries.ITEM, MODID);
    
    private final String name;
    private final Function<BlockBehaviour.Properties, B> factory;
    
    private final List<ItemBuilder<?>> items = new ObjectArrayList<>();
    
    private Supplier<BlockBehaviour.Properties> blockProperties = BlockBehaviour.Properties::of;
    
    private Iterable<TagKey<Block>> tags = Set.of();
    
    @Nullable
    private Function<Supplier<B>, LootTable.Builder> lootTable = null;
    @Nullable
    private String lang = null;
    
    public BlockBuilder(String name, Function<BlockBehaviour.Properties, B> factory) {
        this.name = name;
        this.factory = factory;
    }
    
    public BlockBuilder<B> properties(Supplier<BlockBehaviour.Properties> properties) {
        this.blockProperties = properties;
        return this;
    }
    
    public BlockBuilder<B> dropSelf() {
        return this.lootTable(DoodooLootTableProvider::dropSelf);
    }
    
    public BlockBuilder<B> lootTable(Function<Supplier<B>, LootTable.Builder> lootTable) {
        this.lootTable = lootTable;
        return this;
    }
    
    public BlockBuilder<B> lootTable(Supplier<LootTable.Builder> lootTable) {
        return this.lootTable(block -> lootTable.get());
    }
    
    public BlockBuilder<B> simpleItem() {
        this.items.add(new ItemBuilder<>(BlockItem::new));
        return this;
    }
    
    public <I extends Item> ItemBuilder<I> item(String name, BiFunction<B, Item.Properties, I> factory) {
        ItemBuilder<I> item = new ItemBuilder<>(name, factory);
        this.items.add(item);
        return item;
    }
    
    public ItemBuilder<BlockItem> item(String name) {
        return this.item(name, BlockItem::new);
    }
    
    public BlockBuilder<B> lang(String lang) {
        this.lang = lang;
        return this;
    }
    
    public BlockBuilder<B> tags(Iterable<TagKey<Block>> tags) {
        this.tags = tags;
        return this;
    }
    
    public RegistryObject<B> build() {
        RegistryObject<B> block = BLOCKS.register(
                this.name,
                () -> this.factory.apply(this.blockProperties.get())
        );
        if (this.lootTable != null) {
            DoodooLootTableProvider.addLootTable(block.getId(), () -> this.lootTable.apply(block));
        }
        if (this.lang != null) {
            DoodooLanguageProvider.addBlockTranslation(block, this.lang);
        }
        for (TagKey<Block> tag : this.tags) {
            DoodooBlockTagsProvider.addBlockTag(tag, block);
        }
        this.items.forEach(item -> item.build(block));
        return block;
    }
    
    public class ItemBuilder<I extends Item> {
        
        private final String name;
        private final BiFunction<B, Item.Properties, I> factory;
        
        private Supplier<Item.Properties> itemProperties = Item.Properties::new;
        
        private Iterable<TagKey<Item>> tags = Set.of();
        
        private boolean addToCreativeTab = true;
        
        @Nullable
        private String lang = null;
        
        public ItemBuilder(String name, BiFunction<B, Item.Properties, I> factory) {
            this.name = name;
            this.factory = factory;
        }
        
        public ItemBuilder(BiFunction<B, Item.Properties, I> factory) {
            this(BlockBuilder.this.name, factory);
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
        
        public BlockBuilder<B> endItem() {
            return BlockBuilder.this;
        }
        
        private void build(Supplier<B> block) {
            RegistryObject<I> item = ITEMS.register(this.name, () -> this.factory.apply(block.get(), this.itemProperties.get()));
            if (BlockBuilder.this.items.size() > 1) {
                if (this.lang != null) {
                    DoodooLanguageProvider.addItemTranslation(item, this.lang);
                }
            }
            if (this.addToCreativeTab) {
                DoodooCreativeModeTab.addTabItem(item);
            }
            for (TagKey<Item> tag : this.tags) {
                DoodooItemTagsProvider.addItemTag(tag, item);
            }
            
        }
    }
    
}
