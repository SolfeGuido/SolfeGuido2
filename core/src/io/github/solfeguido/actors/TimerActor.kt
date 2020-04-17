package io.github.solfeguido.actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.ParticleEffect
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup
import com.badlogic.gdx.utils.Pools
import io.github.solfeguido.config.Constants
import io.github.solfeguido.enums.TimeModeEnum
import io.github.solfeguido.factories.colorDrawable
import io.github.solfeguido.factories.gCol
import io.github.solfeguido.settings.TimeSettings
import io.github.solfeguido.settings.time.CountdownOptions
import io.github.solfeguido.settings.time.CountupOptions
import io.github.solfeguido.settings.time.InfiniteTimeOptions

class TimerActor(private val settings: TimeSettings) : WidgetGroup() {

    private val frontColor = gCol("correct")
    private val timeLine = colorDrawable(1, 1, frontColor)
    private val max : Float
    private var current: Float
    private var timeBonus: Float
    private var timePenalty : Float
    private val direction: Int
    private var started = false

    private val particles = ParticleEffect().also {
        it.load(Gdx.files.internal(Constants.SPARKLE_PARTICLE), Gdx.files.internal(Constants.IMAGES_PATH))
        it.emitters.first().tint.colors = floatArrayOf(frontColor.r, frontColor.g, frontColor.b, frontColor.a)
        it.emitters.first().setPosition(0f, 0f)
    }


    private val progress
        get() = width * (current / max)

    init {
        when(settings.type){
            TimeModeEnum.Countdown -> {
                val options = settings.options as CountdownOptions
                max = options.duration
                current = options.duration
                timePenalty = options.timePenalty
                timeBonus = options.timeBonus
                direction = -1
            }
            TimeModeEnum.Countup -> {
                val options = settings.options as CountupOptions
                max = options.limit
                timeBonus = options.timeBonus
                timePenalty = options.timePenalty
                current = 0f
                direction = 1
            }
            TimeModeEnum.Infinite -> {
                max = 1f
                current = 0f
                direction = 0
                timeBonus = 0f
                timePenalty= 0f
            }
        }
    }

    fun correct(){
        current -= timeBonus * direction
    }

    fun wrong(){
        current += timePenalty * direction
    }

    override fun act(delta: Float) {
        if(!started) {
            started = true
            particles.start()
        }

        super.act(delta)
        current += delta * direction
        particles.update(delta)
    }

    override fun getPrefHeight(): Float = 4f
    override fun getPrefWidth(): Float = Gdx.graphics.width.toFloat()


    override fun draw(batch: Batch, parentAlpha: Float) {
        particles.emitters.first().setPosition(progress, y)
        timeLine.draw(batch, x, y, progress, height)
        particles.draw(batch)

    }

}