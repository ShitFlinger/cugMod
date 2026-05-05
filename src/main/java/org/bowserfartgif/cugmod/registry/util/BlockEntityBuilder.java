package org.bowserfartgif.cugmod.registry.util;

import com.mojang.datafixers.types.Type;
import foundry.veil.platform.registry.RegistrationProvider;
import foundry.veil.platform.registry.RegistryObject;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.fml.loading.FMLEnvironment;
import org.bowserfartgif.cugmod.Cugmod;
import org.bowserfartgif.cugmod.registry.DoodooBlockEntityRenderers;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class BlockEntityBuilder<BE extends BlockEntity> {
    
    public static final RegistrationProvider<BlockEntityType<?>> BLOCK_ENTITIES = RegistrationProvider.get(Registries.BLOCK_ENTITY_TYPE, Cugmod.MODID);
    
    private final String name;
    private final TriFunction<BlockEntityType<BE>, BlockPos, BlockState, BE> factory;
    
    private Set<Supplier<? extends Block>> validBlocks = Set.of();
    
    @Nullable
    private Type<?> dataType = null;
    @Nullable
    private Supplier<Function<BlockEntityRendererProvider.Context, BlockEntityRenderer<BE>>> renderer = null;
    
    private RegistryObject<BlockEntityType<BE>> registeredObject;
    
    public BlockEntityBuilder(String name, TriFunction<BlockEntityType<BE>, BlockPos, BlockState, BE> factory) {
        this.name = name;
        this.factory = factory;
    }
    
    @SafeVarargs // makes the method sus and final bruh. but needed to not have 1000 warnings.
    public final BlockEntityBuilder<BE> validBlocks(Supplier<? extends Block>... blocks) {
        assert this.validBlocks.isEmpty() : "Attempted to set valid blocks twice!";
        this.validBlocks = Set.of(blocks);
        return this;
    }
    
    public BlockEntityBuilder<BE> dataType(Type<?> dataType) {
        assert this.validBlocks == null : "Attempted to set data type twice!";
        this.dataType = dataType;
        return this;
    }
    
    public BlockEntityBuilder<BE> renderer(
            Supplier<Function<BlockEntityRendererProvider.Context, BlockEntityRenderer<BE>>> renderer
    ) {
        this.renderer = renderer;
        return this;
    }
    
    private Set<Block> unwrapBlocks() {
        return this.validBlocks.stream().map(Supplier::get).collect(Collectors.toSet());
    }
    
    public RegistryObject<BlockEntityType<BE>> build() {
        this.registeredObject = BLOCK_ENTITIES.register(
                this.name,
                () -> new BlockEntityType<>(
                        (blockPos, blockState) -> this.factory.apply(this.registeredObject.get(), blockPos, blockState),
                        this.unwrapBlocks(),
                        this.dataType
                )
        );
        if (this.renderer != null && FMLEnvironment.dist.isClient()) {
            DoodooBlockEntityRenderers.addRenderer(this.registeredObject, this.renderer);
        }
        return this.registeredObject;
    }
    
}
