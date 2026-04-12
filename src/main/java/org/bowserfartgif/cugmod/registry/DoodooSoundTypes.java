package org.bowserfartgif.cugmod.registry;

import net.minecraft.world.level.block.SoundType;

public class DoodooSoundTypes {

    public static final SoundType SWINE = new SoundType(
            1.0F,
            1.0F,
            DoodooSounds.SWINE_BREAK.get(),
            DoodooSounds.SWINE_STEP.get(),
            DoodooSounds.SWINE_PLACE.get(),
            DoodooSounds.SWINE_HIT.get(),
            DoodooSounds.SWINE_FALL.get()
    );
}