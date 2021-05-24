package io.github.solfeguido.settings

import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonValue
import ktx.json.*

class TimeSettings(
    var start : Float = 0.0f,
    var max: Float = 60.0f,
    var timeBonus: Float = 0f,
    var timePenalty: Float = 1f,
    var multiplicator: Float = 1f,
    var showParticles: Boolean = true
) : Json.Serializable {

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
            start = 120f,
            max = 120f,
            timeBonus = 0f,
            timePenalty = 2f,
            multiplicator = -1f,
            showParticles = true
        )

    }

    override fun read(json: Json, jsonData: JsonValue) {
        start = json.readValue(jsonData, "start")
        max = json.readValue(jsonData, "max")
        timeBonus = json.readValue(jsonData, "timeBonus")
        timePenalty = json.readValue(jsonData, "timePenalty")
        multiplicator = json.readValue(jsonData, "multiplicator")
        showParticles = json.readValue(jsonData, "showParticles")
    }

    override fun write(json: Json) {
        json.writeValue("start", start)
        json.writeValue("max", max)
        json.writeValue("timeBonus", timeBonus)
        json.writeValue("timePenalty", timePenalty)
        json.writeValue("multiplicator", multiplicator)
        json.writeValue("showParticles", showParticles)
    }

}