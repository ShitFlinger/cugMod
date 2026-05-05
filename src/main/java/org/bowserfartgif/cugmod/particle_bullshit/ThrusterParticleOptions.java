package org.bowserfartgif.cugmod.particle_bullshit;

import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.codec.StreamCodec;
import org.bowserfartgif.cugmod.registry.DoodooParticleTypes;

public class ThrusterParticleOptions implements ParticleOptions {
    public static final MapCodec<ThrusterParticleOptions> CODEC = MapCodec.unit(new ThrusterParticleOptions());
    public static final StreamCodec<ByteBuf, ThrusterParticleOptions> STREAM_CODEC = StreamCodec.unit(new ThrusterParticleOptions());

    @Override
    public ParticleType<ThrusterParticleOptions> getType() {
        return DoodooParticleTypes.THRUSTER_PARTICLE.get();
    }
}