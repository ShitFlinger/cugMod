package org.bowserfartgif.cugmod.registry;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.resources.ResourceLocation;
import org.bowserfartgif.cugmod.Cugmod;
import org.jetbrains.annotations.ApiStatus;

public class DoodooPartialModels {

    public static final PartialModel
            FLAPPYBIT = block("control/wing/flappybit");
    public static final PartialModel
            THRUSTER_GLOW = block("thruster/thruster_glow");

    private DoodooPartialModels() {
    }

    private static PartialModel block(String path) {
        return PartialModel.of(ResourceLocation.fromNamespaceAndPath(Cugmod.MODID, "block/" + path));
    }

    private static PartialModel item(String path) {
        return PartialModel.of(ResourceLocation.fromNamespaceAndPath(Cugmod.MODID, "item/" + path));
    }

    @ApiStatus.Internal
    public static void bootstrap() {
    }
}