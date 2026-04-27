package org.bowserfartgif.cugmod.registry;

import foundry.veil.platform.registry.RegistrationProvider;
import foundry.veil.platform.registry.RegistryObject;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.List;
import java.util.function.Supplier;

import static org.bowserfartgif.cugmod.Cugmod.MODID;

public class DoodooCreativeModeTab {
    
    public static final RegistrationProvider<CreativeModeTab> CREATIVE_MODE_TABS = RegistrationProvider.get(Registries.CREATIVE_MODE_TAB, MODID);
    
    private static final List<Supplier<? extends ItemLike>> TAB_ITEMS = new ObjectArrayList<>();
    
    public static final RegistryObject<CreativeModeTab> CUGMOD_TAB =
            CREATIVE_MODE_TABS.register("cugmod", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.cugmod"))
                    .icon(() -> new ItemStack(DoodooBlocks.THRUSTER.get()))
                    .displayItems((parameters, output) -> {
                        for (Supplier<? extends ItemLike> tabItem : TAB_ITEMS) {
                            output.accept(tabItem.get());
                        }
                    })
                    .build());
    
    public static void addTabItem(Supplier<? extends ItemLike> itemLike) {
        TAB_ITEMS.add(itemLike);
    }
    
    public static void bootstrap() {
    }
}
