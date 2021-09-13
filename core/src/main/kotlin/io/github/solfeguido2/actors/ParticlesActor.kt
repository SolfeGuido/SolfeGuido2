package io.github.solfeguido2.actors

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.ParticleEffect
import com.badlogic.gdx.scenes.scene2d.ui.Widget

class ParticlesActor(private val particles: ParticleEffect) : Widget() {

    override fun draw(batch: Batch?, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        particles.draw(batch)
    }

    override fun act(delta: Float) {
        particles.update(delta)
    }

    override fun setPosition(x: Float, y: Float) {
        super.setPosition(x, y)
        particles.emitters.forEach { it.setPosition(x, y) }
    }

    fun start() {
        particles.start()
    }
}