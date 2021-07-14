package io.github.solfeguido.settings

import io.github.solfeguido.config.Constants
import kotlinx.serialization.Serializable

@Serializable
class TimeSettings(
    var start : Float = 0.0f,
    var max: Float = Constants.CLASSIC_TIME,
    var timeBonus: Float = 0f,
    var timePenalty: Float = 1f,
    var multiplicator: Float = 1f,
    var showParticles: Boolean = true
)  {

    companion object {

        val InfiniteMode = TimeSettings(
            start = 0f,
            max = 1f,
            timeBonus =  0f,
            timePenalty = 0f,
            multiplicator = 0f,
            showParticles = false
        )

        val ClassicCountdownMode = TimeSettings(
            start = Constants.CLASSIC_TIME,
            max = Constants.CLASSIC_TIME,
            timeBonus = 0f,
            timePenalty = 2f,
            multiplicator = -1f,
            showParticles = true
        )

    }


}