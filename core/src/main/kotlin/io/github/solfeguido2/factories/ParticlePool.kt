package io.github.solfeguido2.factories

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.graphics.g2d.ParticleEffect
import io.github.solfeguido2.structures.Constants
import ktx.inject.Context

class ParticlePool(context: Context) {

    companion object {
        // Particles config files
        const val SPARKLE_PARTICLE = "${Constants.PARTICLES_PATH}/sparkles.p"
        const val ABSORB_PARTICLE = "${Constants.PARTICLES_PATH}/absorb.p"
        const val EXPLODE_PARTICLE = "${Constants.PARTICLES_PATH}/explode.p"
        const val IMPLODE_PARTICLE = "${Constants.PARTICLES_PATH}/implode.p"
        const val NOTE_BURST_PARTICLE = "${Constants.PARTICLES_PATH}/note_burst.p"
    }

    val emptyParticles = ParticleEffect()

    // TODO : add an option to enable/ disable the particles
    private val isEnabled = true// context.inject<Preferences>().getBoolean("particles", true)

    private fun loadParticleEffect(path: String) = ParticleEffect().also {
        it.load(Gdx.files.internal(path), Gdx.files.internal(Constants.IMAGES_PATH))
    }

    // TODO: find a cleaner way to handle this
    private val _sparkles = loadParticleEffect(SPARKLE_PARTICLE)
    val sparkles: ParticleEffect
        get() = if (isEnabled) _sparkles else emptyParticles

    private val _absorb = loadParticleEffect(ABSORB_PARTICLE)
    val absorb: ParticleEffect
        get() = if (isEnabled) _absorb else emptyParticles

    private val _explode = loadParticleEffect(EXPLODE_PARTICLE)
    val explode: ParticleEffect
        get() = if (isEnabled) _explode else emptyParticles

    private val _implode = loadParticleEffect(IMPLODE_PARTICLE)
    val implode: ParticleEffect
        get() = if (isEnabled) _implode else emptyParticles

    private val _noteBurst = loadParticleEffect(NOTE_BURST_PARTICLE)
    val noteBurst: ParticleEffect
        get() = if (isEnabled) _noteBurst else emptyParticles
}