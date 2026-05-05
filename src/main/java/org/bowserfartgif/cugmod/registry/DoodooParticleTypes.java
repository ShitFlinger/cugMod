package org.bowserfartgif.cugmod.registry;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.bowserfartgif.cugmod.Cugmod;
import org.bowserfartgif.cugmod.particle_bullshit.ThrusterParticleOptions;

public class DoodooParticleTypes {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(Registries.PARTICLE_TYPE, Cugmod.MODID);

    public static final DeferredHolder<ParticleType<?>, ParticleType<ThrusterParticleOptions>> THRUSTER_PARTICLE =
            PARTICLE_TYPES.register("thruster_particle", () ->
                    new ParticleType<ThrusterParticleOptions>(false) {
                        @Override
                        public MapCodec<ThrusterParticleOptions> codec() {
                            return ThrusterParticleOptions.CODEC;
                        }

                        @Override
                        public StreamCodec<? super RegistryFriendlyByteBuf, ThrusterParticleOptions> streamCodec() {
                            return ThrusterParticleOptions.STREAM_CODEC.cast();
                        }
                    }
            );
}