package io.github.solfeguido.actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.ParticleEffect
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup
import com.badlogic.gdx.utils.Pools
import io.github.solfeguido.factories.ParticlePool
import io.github.solfeguido.factories.colorDrawable
import io.github.solfeguido.factories.gCol
import io.github.solfeguido.settings.TimeSettings
import io.github.solfeguido.ui.events.TimerEvent
import ktx.inject.Context

class TimerActor(context: Context, settings: TimeSettings) : WidgetGroup() {

    private val frontColor = gCol("correct")
    private val errorColor = gCol("error")
    private val timeLine = colorDrawable(frontColor)
    private val max: Float
    private var current: Float
    private var timeBonus: Float
    private var timePenalty: Float
    private val direction: Float
    private var running = false

    private val defaultParticles: ParticleEffect
    private val wrongParticles: ParticleEffect


    private val progress
        get() = width * (current / max)

    init {
        val pool = context.inject<ParticlePool>()
        max = settings.max
        current = settings.start
        timePenalty = settings.timePenalty
        timeBonus = settings.timeBonus
        direction = settings.multiplicator
        if (settings.showParticles) {
            defaultParticles = pool.sparkles.also {
                it.emitters.first().tint.colors =
                    floatArrayOf(frontColor.r, frontColor.g, frontColor.b, frontColor.a)
                it.emitters.first().setPosition(0f, 0f)
            }
            wrongParticles = pool.explode.also {
                it.emitters.first().tint.colors =
                    floatArrayOf(errorColor.r, errorColor.g, errorColor.b, errorColor.a)
                it.emitters.first().setPosition(0f, 0f)
            }
        } else {
            defaultParticles = pool.emptyParticles
            wrongParticles = pool.emptyParticles
        }

    }

    fun correct() {
        current -= timeBonus * direction
    }

    fun wrong() {
        current += timePenalty * direction
        wrongParticles.start()
        wrongParticles.emitters.firstOrNull()?.setPosition(progress, y)
    }

    override fun act(delta: Float) {
        super.act(delta)
        defaultParticles.update(delta)
        wrongParticles.update(delta)

        if (!running) {
            // Timer already finished
            if (current !in 0f..max) return
            running = true
            defaultParticles.start()
        }

        current += delta * direction
        if (current !in 0f..max) {
            val event = Pools.obtain(TimerEvent::class.java)
            fire(event)
            Pools.free(event)
            running = false
        }
    }

    override fun getPrefHeight(): Float = 4f
    override fun getPrefWidth(): Float = Gdx.graphics.width.toFloat()


    override fun draw(batch: Batch, parentAlpha: Float) {
        val c = color
        batch.setColor(c.r, c.g, c.b, c.a * parentAlpha)
        defaultParticles.emitters.firstOrNull()?.setPosition(progress, y)
        timeLine.draw(batch, x, y, progress, height)
        defaultParticles.draw(batch)
        wrongParticles.draw(batch)

    }

}