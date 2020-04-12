package io.github.solfeguido.settings

import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonValue
import io.github.solfeguido.enums.TimeModeEnum
import io.github.solfeguido.settings.time.CountupOptions
import io.github.solfeguido.settings.time.EmptyTimeOptions
import io.github.solfeguido.settings.time.ITimeOptions
import ktx.json.*

class TimeSettings : Json.Serializable {

    var type = TimeModeEnum.Countdown
    var options: ITimeOptions = EmptyTimeOptions()

    override fun read(json: Json, jsonData: JsonValue) {
        type = json.readValue(jsonData, "type")
        options = when(type) {
            TimeModeEnum.Countdown -> json.readValue<CountupOptions>(jsonData, "options")
            TimeModeEnum.Countup -> json.readValue<CountupOptions>(jsonData, "options")
            else -> EmptyTimeOptions()
        }
    }

    override fun write(json: Json) {
        json.writeValue("type", type)
        json.writeValue("options", options)
    }

}