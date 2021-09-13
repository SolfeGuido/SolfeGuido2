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
    private val loadedParticles = hashMapOf<String, ParticleEffect>()

    // TODO : add an option to enable/ disable the particles
    private val isEnabled = context.inject<Preferences>().getBoolean("particles", true)

    private fun loadParticleEffect(path: String) = ParticleEffect().also {
        it.load(Gdx.files.internal(path), Gdx.files.internal(Constants.IMAGES_PATH))
    }

    private fun loadOrGetParticle(path : String) : ParticleEffect{
        if(!isEnabled) return emptyParticles
        if(loadedParticles.containsKey(path)) return loadedParticles[path]!!
        val nwLoad = loadParticleEffect(path)
        loadedParticles[path] = nwLoad
        return nwLoad
    }

    // TODO: find a cleaner way to handle this
    val sparkles get() = loadOrGetParticle(SPARKLE_PARTICLE)

    val absorb get() = loadOrGetParticle(ABSORB_PARTICLE)

    val explode get() = loadOrGetParticle(EXPLODE_PARTICLE)

    val implode get() = loadOrGetParticle(IMPLODE_PARTICLE)

    val noteBurst get() = loadOrGetParticle(NOTE_BURST_PARTICLE)
}