package org.bowserfartgif.cugmod.registry;

import foundry.veil.platform.registry.RegistrationProvider;
import foundry.veil.platform.registry.RegistryObject;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.storage.loot.LootTable;
import org.bowserfartgif.cugmod.content.control.wing.CamberedWingBlock;
import org.bowserfartgif.cugmod.content.control.wing.ControlSurfaceBlock;
import org.bowserfartgif.cugmod.content.control.wing.WingBlock;
import org.bowserfartgif.cugmod.content.jesus.JesusBlock;
import org.bowserfartgif.cugmod.content.poultry.explosive.ExplosivePoultryBlock;
import org.bowserfartgif.cugmod.content.poultry.red.RedPoultryBlock;
import org.bowserfartgif.cugmod.content.poultry.splitting.SplittingPoultryBlock;
import org.bowserfartgif.cugmod.content.propulsion.ThrusterBlock;
import org.bowserfartgif.cugmod.content.swine.WretchedSwineBlock;
import org.bowserfartgif.cugmod.content.swine.WretchedSwineBlockItem;
import org.bowserfartgif.cugmod.registry.util.BlockBuilder;

import java.util.Set;
import java.util.function.Function;

import static org.bowserfartgif.cugmod.Cugmod.MODID;

public class DoodooBlocks {
    
    public static final RegistrationProvider<Block> BLOCKS = RegistrationProvider.get(Registries.BLOCK, MODID);
    public static final Registry<Block> REGISTRY = BLOCKS.asVanillaRegistry();
    
    public static void bootstrap() {
    }

    public static final RegistryObject<ThrusterBlock> THRUSTER = block("thruster", ThrusterBlock::new)
            .properties(p -> p
                    .noOcclusion()
                    .strength(0.2f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.COPPER))
            .lang("Thruster")
            .dropSelf()
            .tags(Set.of(BlockTags.NEEDS_IRON_TOOL, BlockTags.MINEABLE_WITH_PICKAXE))
            .simpleItem()
            .build();

    public static final RegistryObject<JesusBlock> JESUS = block("jesus", JesusBlock::new)
            .properties(p -> p
                    .noOcclusion()
                    .strength(0.2f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.NETHERITE_BLOCK))
            .lang("Jesus")
            .dropSelf()
            .simpleItem()
            .build();
    
    public static final RegistryObject<WingBlock> WING = block("wing", WingBlock::new)
            .properties(p -> p
                    .noOcclusion()
                    .strength(0.2f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.COPPER))
            .lang("Wing Panel")
            .dropSelf()
            .tags(Set.of(BlockTags.NEEDS_IRON_TOOL, BlockTags.MINEABLE_WITH_PICKAXE))
            .simpleItem()
            .build();
    
    public static final RegistryObject<CamberedWingBlock> CAMBERED_WING = block("cambered_wing", CamberedWingBlock::new)
            .properties(p -> p
                    .noOcclusion()
                    .strength(0.2f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.COPPER))
            .lang("Cambered Wing")
            .dropSelf()
            .tags(Set.of(BlockTags.NEEDS_IRON_TOOL, BlockTags.MINEABLE_WITH_PICKAXE))
            .simpleItem()
            .build();
    
    
    public static final RegistryObject<ControlSurfaceBlock> CONTROL_SURFACE = block("control_surface", ControlSurfaceBlock::new)
            .properties(p -> p
                    .noOcclusion()
                    .strength(0.2f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.COPPER))
            .lang("Control Surface")
            .dropSelf()
            .tags(Set.of(BlockTags.NEEDS_IRON_TOOL, BlockTags.MINEABLE_WITH_PICKAXE))
            .simpleItem()
            .build();
    
    public static final RegistryObject<WretchedSwineBlock> SWINE = block("wretched_swine", WretchedSwineBlock::new)
            .properties(p -> p
                                .noOcclusion()
                                .strength(1.0f, 30.0f)
                                .sound(DoodooSoundTypes.SWINE.get()))
            .renderType(() -> RenderType::cutout)
            .lootTable((block) ->
                               WretchedSwineBlock.addLoot(LootTable.lootTable(), block.get()))
            .item("hurt_swine", (block, properties) ->
                    new WretchedSwineBlockItem(block, properties, WretchedSwineBlock.Mood.HURT))
            .lang("Hurt Swine").endItem()
            .item("angry_swine", (block, properties) ->
                    new WretchedSwineBlockItem(block, properties, WretchedSwineBlock.Mood.ANGRY))
            .lang("Angry Swine").endItem()
            .item("burnt_swine", (block, properties) ->
                    new WretchedSwineBlockItem(block, properties, WretchedSwineBlock.Mood.BURNT))
            .lang("Burnt Swine").endItem()
            .item("wretched_swine", (block, properties) ->
                    new WretchedSwineBlockItem(block, properties, WretchedSwineBlock.Mood.HAPPY))
            .lang("Wretched Swine").endItem()
            .build();
    
    public static final RegistryObject<RedPoultryBlock> RED_POULTRY = block("red_poultry", RedPoultryBlock::new)
            .properties(p -> p.noOcclusion()
                                .strength(1.0f, 30.0f))
            .renderType(() -> RenderType::cutout)
            .dropSelf()
            .simpleItem()
            .lang("Furious Poultry")
            .build();

    public static final RegistryObject<ExplosivePoultryBlock> EXPLOSIVE_POULTRY = block("explosive_poultry", ExplosivePoultryBlock::new)
            .properties(p -> p.noOcclusion()
                    .strength(1.0f, 30.0f))
            .renderType(() -> RenderType::cutout)
            .dropSelf()
            .simpleItem()
            .lang("Explosive Poultry")
            .build();

    public static final RegistryObject<SplittingPoultryBlock> SPLITTING_POULTRY = block("splitting_poultry", SplittingPoultryBlock::new)
            .properties(p -> p.noOcclusion()
                    .strength(1.0f, 30.0f))
            .renderType(() -> RenderType::cutout)
            .dropSelf()
            .simpleItem()
            .lang("Splitting Poultry")
            .build();

    private static <B extends Block> BlockBuilder<B> block(String name, Function<BlockBehaviour.Properties, B> factory) {
        return new BlockBuilder<>(name, factory);
    }
}