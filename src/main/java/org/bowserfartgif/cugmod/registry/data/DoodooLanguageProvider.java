package org.bowserfartgif.cugmod.registry.data;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.bowserfartgif.cugmod.Cugmod;

import java.util.Map;
import java.util.function.Supplier;

public class DoodooLanguageProvider extends LanguageProvider {
    
    private static final Map<Supplier<? extends Block>, String> BLOCK_LANGS = new Object2ObjectArrayMap<>();
    private static final Map<Supplier<? extends Item>, String> ITEM_LANGS = new Object2ObjectArrayMap<>();
    
    public DoodooLanguageProvider(PackOutput output, String locale) {
        super(output, Cugmod.MODID, locale);
    }
    
    @Override
    protected void addTranslations() {
        BLOCK_LANGS.forEach(this::addBlock);
        
        ITEM_LANGS.forEach(this::addItem);
        
        this.add("itemGroup.cugmod", "Junkyard");
        this.add("item.cugmod.wretched_disc.desc", "Bad Piggies theme - Ilmari Hakkola");
        this.add("lore.cugmod.thruster.line2", "§7It goes pretty fast I think, don't ask me how it works with only a nozzle, though");
    }
    
    public static void addBlockTranslation(Supplier<? extends Block> block, String name) {
        BLOCK_LANGS.put(block, name);
    }
    
    public static void addItemTranslation(Supplier<? extends Item> item, String name) {
        ITEM_LANGS.put(item, name);
    }
}
