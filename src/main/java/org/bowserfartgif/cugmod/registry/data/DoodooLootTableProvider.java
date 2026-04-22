package org.bowserfartgif.cugmod.registry.data;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.bowserfartgif.cugmod.Cugmod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class DoodooLootTableProvider implements LootTableSubProvider {
    
    private static final ConstantValue ONE = ConstantValue.exactly(1);
    
    private static final Map<ResourceLocation, Supplier<LootTable.Builder>> BLOCK_LOOT_TABLES = new Object2ObjectArrayMap<>();
    
    private BiConsumer<ResourceKey<LootTable>, LootTable.Builder> registrar;
    
    public DoodooLootTableProvider(HolderLookup.Provider registryLookup) {
    
    }
    
    @Override
    public void generate(@NotNull BiConsumer<ResourceKey<LootTable>, LootTable.Builder> registrar) {
        this.registrar = registrar;
        
        BLOCK_LOOT_TABLES.forEach(this::generateBlock);
    }
    
    private void generateBlock(ResourceLocation id, Supplier<LootTable.Builder> lootTable) {
        this.registrar.accept(resourceKey(id.withPrefix("blocks/")), lootTable.get());
    }
    
    private <T extends Block> void dropSelf(DeferredHolder<Block, T> block) {
        assert this.registrar != null;
        this.generateBlock(
                block.getId(),
                () -> DoodooLootTableProvider.dropSelf((Supplier<T>) block)
        );
    }
    
    public static <T extends Block> LootTable.Builder dropSelf(Supplier<T> block) {
        return LootTable.lootTable()
                .withPool(LootPool.lootPool()
                                  .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))
                                  .setRolls(ConstantValue.exactly(1.0F))
                                  .add(LootItem.lootTableItem(block.get())
                                  )
                );
    }
    
    private static ResourceKey<LootTable> resourceKey(ResourceLocation id) {
        return ResourceKey.create(Registries.LOOT_TABLE, id);
    }
    
    private static ResourceKey<LootTable> resourceKey(String path) {
        return resourceKey(Cugmod.id(path));
    }
    
    public static void addLootTable(ResourceLocation id, @Nullable Supplier<LootTable.Builder> lootTable) {
        if (lootTable == null) {
            return;
        }
        
        BLOCK_LOOT_TABLES.put(id, lootTable);
    }
}
