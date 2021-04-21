package io.github.solfeguido.settings.time

import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonValue
import ktx.json.*

class CountdownOptions : ITimeOptions {

    var duration = 30f
    var timeBonus = 0f
    var timePenalty = 0.5f

    override fun read(json: Json, jsonData: JsonValue) {
        duration = json.readValue(jsonData, "duration")
        timeBonus = json.readValue(jsonData, "timeBonus")
        timePenalty = json.readValue(jsonData, "timePenalty")
    }

    override fun write(json: Json) {
        json.writeValue("duration", duration)
        json.writeValue("timeBonus", timeBonus)
        json.writeValue("timePenalty", timePenalty)
    }
}