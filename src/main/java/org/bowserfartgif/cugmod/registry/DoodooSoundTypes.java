package org.bowserfartgif.cugmod.registry;

import com.google.common.base.Suppliers;
import net.minecraft.world.level.block.SoundType;

import java.util.function.Supplier;

public class DoodooSoundTypes {

    public static final Supplier<SoundType> SWINE = Suppliers.memoize(() -> new SoundType(
            1.0F,
            1.0F,
            DoodooSounds.SWINE_BREAK.get(),
            DoodooSounds.SWINE_STEP.get(),
            DoodooSounds.SWINE_PLACE.get(),
            DoodooSounds.SWINE_HIT.get(),
            DoodooSounds.SWINE_FALL.get()
    ));
}