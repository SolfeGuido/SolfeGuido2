package io.github.solfeguido2.settings

import io.github.solfeguido2.structures.Constants
import kotlinx.serialization.Serializable

@Serializable
class TimeSettings(
    var start: Float = 0.0f,
    var max: Float = Constants.CLASSIC_TIME,
    var timeBonus: Float = 0f,
    var timePenalty: Float = 1f,
    var multiplicator: Float = 1f,
    var showParticles: Boolean = true,
    val giveAnswerFrequency: Float = -1f
) {

    companion object {

        val InfiniteMode = TimeSettings(
            start = 0f,
            max = 1f,
            timeBonus = 0f,
            timePenalty = 0f,
            multiplicator = 0f,
            showParticles = false
        )

        val ClassicCountdownMode = TimeSettings(
            start = 5f,//Constants.CLASSIC_TIME,
            max = 5f,//Constants.CLASSIC_TIME,
            timeBonus = 0f,
            timePenalty = 2f,
            multiplicator = -1f,
            showParticles = true
        )

        val InfiniteAutoMode = TimeSettings(
            start = 0f,
            max = 1f,
            timeBonus = 0f,
            timePenalty = 0f,
            multiplicator = 0f,
            showParticles = false,
            giveAnswerFrequency = 1f
        )
    }


}