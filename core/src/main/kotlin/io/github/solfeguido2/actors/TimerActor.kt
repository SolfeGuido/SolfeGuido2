package io.github.solfeguido2.actors

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.ParticleEffect
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup
import io.github.solfeguido2.events.GiveAnswerEvent
import io.github.solfeguido2.structures.Constants
import io.github.solfeguido2.factories.*
import io.github.solfeguido2.settings.TimeSettings
import io.github.solfeguido2.events.TimerFinishedEvent
import ktx.inject.Context

class TimerActor(context: Context, settings: TimeSettings) : WidgetGroup() {

    private val frontColor = gCol("correct")
    private val errorColor = gCol("error")
    private val timeLine = colorDrawable(frontColor)
    private val max: Float
    private val giveAnswerFrequency: Float
    private var nextGiveAnswer: Float
    private var current: Float
    private var timeBonus: Float
    private var timePenalty: Float
    private val direction: Float
    private var isRunning = false
    private var isPaused = false

    private val defaultParticles: ParticleEffect
    private val wrongParticles: ParticleEffect


    private val progress
        get() = width * (current / max)

    init {
        val pool = context.inject<ParticlePool>()
        max = settings.max
        giveAnswerFrequency = settings.giveAnswerFrequency
        nextGiveAnswer = if (giveAnswerFrequency > 0) {
            0f
        } else {
            -1f
        }
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

    fun pause() {
        isPaused = true
    }

    fun resume() {
        isPaused = false
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
        if (!isRunning) {
            // Timer already finished
            if (current !in 0f..max) return
            isRunning = true
            defaultParticles.start()
        }
        if (isPaused) return

        defaultParticles.update(delta)
        wrongParticles.update(delta)

        current += delta * direction
        if (current !in 0f..max) {
            firePooled<TimerFinishedEvent>()
            isRunning = false
        }

        if (nextGiveAnswer >= 0f) {
            nextGiveAnswer += delta
            if (nextGiveAnswer >= giveAnswerFrequency) {
                nextGiveAnswer = 0f
                firePooled<GiveAnswerEvent>()
            }
        }
    }

    override fun getPrefHeight(): Float = 4f
    override fun getPrefWidth(): Float = Constants.WIDTH.toFloat()


    override fun draw(batch: Batch, parentAlpha: Float) {
        val c = color
        batch.setColor(c.r, c.g, c.b, c.a * parentAlpha)
        defaultParticles.emitters.firstOrNull()?.setPosition(progress, y)
        timeLine.draw(batch, x, y, progress, height)
        defaultParticles.draw(batch)
        wrongParticles.draw(batch)

    }

}