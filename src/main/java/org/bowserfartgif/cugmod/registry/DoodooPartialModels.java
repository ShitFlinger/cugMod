package org.bowserfartgif.cugmod.registry;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.resources.ResourceLocation;
import org.bowserfartgif.cugmod.Cugmod;
import org.jetbrains.annotations.ApiStatus;

public class DoodooPartialModels {

    public static final PartialModel
            FLAPPYBIT = block("control/wing/flappybit"),
            THRUSTER_GLOW = block("thruster/thruster_glow"),
            DETONATOR_MAIN_RED = item("remote_detonator/main_red"),
            DETONATOR_MAIN_GREEN = item("remote_detonator/main_green"),
            DETONATOR_BUTTON_RED = item("remote_detonator/button_red"),
            DETONATOR_BUTTON_GREEN = item("remote_detonator/button_green")
    ;

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