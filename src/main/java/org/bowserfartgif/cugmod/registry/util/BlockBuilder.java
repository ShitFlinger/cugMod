package org.bowserfartgif.cugmod.registry.util;

import foundry.veil.platform.registry.RegistrationProvider;
import foundry.veil.platform.registry.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.storage.loot.LootTable;
import org.bowserfartgif.cugmod.registry.data.DoodooLanguageProvider;
import org.bowserfartgif.cugmod.registry.data.DoodooLootTableProvider;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.bowserfartgif.cugmod.Cugmod.MODID;

public class BlockBuilder<B extends Block> {
    
    public static final RegistrationProvider<Block> BLOCKS = RegistrationProvider.get(Registries.BLOCK, MODID);
    public static final RegistrationProvider<Item> ITEMS = RegistrationProvider.get(Registries.ITEM, MODID);
    
    private final String name;
    private final Function<BlockBehaviour.Properties, B> factory;
    
    private Supplier<BlockBehaviour.Properties> blockProperties = BlockBehaviour.Properties::of;
    
    @Nullable
    private Function<Supplier<B>, LootTable.Builder> lootTable = null;
    @Nullable
    private ItemBuilder<?> itemBuilder = null;
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
        this.itemBuilder = new ItemBuilder<>(BlockItem::new);
        return this;
    }
    
    public BlockBuilder<B> lang(String lang) {
        this.lang = lang;
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
            DoodooLanguageProvider.BLOCK_LANGS.put(block, this.lang);
        }
        if (this.itemBuilder != null) {
            this.itemBuilder.build(block);
        }
        return block;
    }
    
    class ItemBuilder<I extends Item> {
        
        private final BiFunction<B, Item.Properties, I> factory;
        
        private Supplier<Item.Properties> itemProperties = Item.Properties::new;
        
        public ItemBuilder(BiFunction<B, Item.Properties, I> factory) {
            this.factory = factory;
        }
        
        public ItemBuilder<I> properties(Supplier<Item.Properties> properties) {
            this.itemProperties = properties;
            return this;
        }
        
        public BlockBuilder<B> endItem() {
            return BlockBuilder.this;
        }
        
        private void build(Supplier<B> block) {
            RegistryObject<I> item = ITEMS.register(BlockBuilder.this.name, () -> this.factory.apply(block.get(), this.itemProperties.get()));
        }
    }
    
}
