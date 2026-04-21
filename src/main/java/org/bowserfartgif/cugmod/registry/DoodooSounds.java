package org.bowserfartgif.cugmod.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.JukeboxSong;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class DoodooSounds {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(Registries.SOUND_EVENT, "cugmod");

    public static final Supplier<SoundEvent> THRUSTER =
            register("thruster");

    public static final Supplier<SoundEvent> SWINE_BREAK =
            register("swine_break");

    public static final Supplier<SoundEvent> SWINE_STEP =
            register("swine_step");

    public static final Supplier<SoundEvent> SWINE_PLACE =
            register("swine_place");

    public static final Supplier<SoundEvent> SWINE_HIT =
            register("swine_hit");

    public static final Supplier<SoundEvent> SWINE_FALL =
            register("swine_fall");

    public static final Supplier<SoundEvent> MONCH =
            register("monch");

    public static final Supplier<SoundEvent> WRETCHED_DISC = register("wretched_disc");
    public static final ResourceKey<JukeboxSong> WRETCHED_DISC_KEY = createSong();

    private static ResourceKey<JukeboxSong> createSong() {
        return ResourceKey.create(Registries.JUKEBOX_SONG, ResourceLocation.fromNamespaceAndPath("cugmod", "wretched_disc"));
    }

    private static Supplier<SoundEvent> register(String name) {
        return SOUND_EVENTS.register(name,
                () -> SoundEvent.createVariableRangeEvent(
                        ResourceLocation.fromNamespaceAndPath("cugmod", name)
                )
        );
    }
}