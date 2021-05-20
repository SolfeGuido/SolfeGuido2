package io.github.solfeguido.actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.ParticleEffect
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup
import io.github.solfeguido.enums.TimeModeEnum
import io.github.solfeguido.factories.ParticlePool
import io.github.solfeguido.factories.colorDrawable
import io.github.solfeguido.factories.gCol
import io.github.solfeguido.settings.TimeSettings
import io.github.solfeguido.settings.time.CountdownOptions
import io.github.solfeguido.settings.time.CountupOptions
import io.github.solfeguido.ui.events.TimerEvent
import ktx.inject.Context

class TimerActor(context: Context, private val settings: TimeSettings) : WidgetGroup() {

    private val frontColor = gCol("correct")
    private val errorColor = gCol("error")
    private val timeLine = colorDrawable(frontColor)
    private val max: Float
    private var current: Float
    private var timeBonus: Float
    private var timePenalty: Float
    private val direction: Int
    private var running = false

    private val defaultParticles: ParticleEffect
    private val wrongParticles: ParticleEffect


    private val progress
        get() = width * (current / max)

    init {
        val pool = context.inject<ParticlePool>()
        when (settings.type) {
            TimeModeEnum.Countdown -> {
                val options = settings.options as CountdownOptions
                max = options.duration
                current = options.duration
                timePenalty = options.timePenalty
                timeBonus = options.timeBonus
                direction = -1
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
            }
            TimeModeEnum.Countup -> {
                val options = settings.options as CountupOptions
                max = options.limit
                timeBonus = options.timeBonus
                timePenalty = options.timePenalty
                current = 0f
                direction = 1
                defaultParticles = pool.absorb.also {
                    it.emitters.first().tint.colors =
                        floatArrayOf(frontColor.r, frontColor.g, frontColor.b, frontColor.a)
                    it.emitters.first().setPosition(0f, 0f)
                }
                wrongParticles = pool.implode.also {
                    it.emitters.first().tint.colors =
                        floatArrayOf(errorColor.r, errorColor.g, errorColor.b, errorColor.a)
                    it.emitters.first().setPosition(0f, 0f)
                }
            }
            TimeModeEnum.Infinite -> {
                max = 1f
                current = 0f
                direction = 0
                timeBonus = 0f
                timePenalty = 0f
                defaultParticles = pool.emptyParticles
                wrongParticles = pool.emptyParticles
            }
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
            fire(TimerEvent())
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