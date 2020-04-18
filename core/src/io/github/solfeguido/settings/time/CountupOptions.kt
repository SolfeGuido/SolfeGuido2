package io.github.solfeguido.settings.time

import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonValue
import ktx.json.*

class CountupOptions : ITimeOptions {

    var limit = 30f
    var timeBonus = 1f
    var timePenalty = 0.5f

    override fun read(json: Json, jsonData: JsonValue) {
        limit = json.readValue(jsonData, "limit")
        timeBonus = json.readValue(jsonData, "timeBonus")
        timePenalty = json.readValue(jsonData, "timePenalty")
    }

    override fun write(json: Json) {
        json.writeValue("limit", limit)
        json.writeValue("timeBonus", timeBonus)
        json.writeValue("timePenalty", timePenalty)
    }
}