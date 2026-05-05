package org.bowserfartgif.cugmod.particle_bullshit;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;

public class ThrusterParticle extends TextureSheetParticle {

    private final SpriteSet sprites;

    protected ThrusterParticle(ClientLevel level, double x, double y, double z,
                               double dx, double dy, double dz, SpriteSet sprites) {
        super(level, x, y, z, dx, dy, dz);
        this.sprites = sprites;
        this.lifetime = 10 + this.random.nextInt(15);
        this.gravity = 0f;
        this.xd = dx;
        this.yd = dy;
        this.zd = dz;
        this.rCol = 1.0f;
        this.gCol = 1f;
        this.bCol = 1f;
        this.alpha = 1.0f;
        this.scale(2f);
        this.setSpriteFromAge(sprites);
    }

    @Override
    public int getLightColor(float partialTick) {
        return 0xF000F0;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        super.tick();
        this.setSpriteFromAge(sprites);
        this.alpha = 1.0f - (float) this.age / this.lifetime;
        this.scale(0.95f);
    }

    public static class Provider implements ParticleProvider<ThrusterParticleOptions> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(ThrusterParticleOptions options, ClientLevel level,
                                       double x, double y, double z,
                                       double dx, double dy, double dz) {
            return new ThrusterParticle(level, x, y, z, dx, dy, dz, sprites);
        }
    }
}