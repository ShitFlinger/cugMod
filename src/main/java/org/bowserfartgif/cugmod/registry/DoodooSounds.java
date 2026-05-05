package org.bowserfartgif.cugmod.registry;

import foundry.veil.platform.registry.RegistrationProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.JukeboxSong;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.bowserfartgif.cugmod.Cugmod;

import java.util.function.Supplier;

import static org.bowserfartgif.cugmod.Cugmod.MODID;

public class DoodooSounds {

    public static final RegistrationProvider<SoundEvent> SOUND_EVENTS = RegistrationProvider.get(BuiltInRegistries.SOUND_EVENT, MODID);

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
        return ResourceKey.create(Registries.JUKEBOX_SONG, Cugmod.id("wretched_disc"));
    }

    private static Supplier<SoundEvent> register(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(Cugmod.id(name)));
    }
    
    public static void bootstrap() {
    }
}