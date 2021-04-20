package io.github.solfeguido.settings

import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonValue
import io.github.solfeguido.enums.TimeModeEnum
import io.github.solfeguido.settings.time.CountdownOptions
import io.github.solfeguido.settings.time.CountupOptions
import io.github.solfeguido.settings.time.ITimeOptions
import io.github.solfeguido.settings.time.InfiniteTimeOptions
import ktx.json.*

class TimeSettings(type: TimeModeEnum = TimeModeEnum.Countdown, options: ITimeOptions = CountdownOptions()) : Json.Serializable {

    var type = TimeModeEnum.Countdown
    var options: ITimeOptions = CountdownOptions()


    init {
        this.type = type
        this.options = options
    }

    override fun read(json: Json, jsonData: JsonValue) {
        type = json.readValue(jsonData, "type")
        options = when (type) {
            TimeModeEnum.Countdown -> json.readValue<CountupOptions>(jsonData, "options")
            TimeModeEnum.Countup -> json.readValue<CountupOptions>(jsonData, "options")
            TimeModeEnum.Infinite -> json.readValue<InfiniteTimeOptions>(jsonData, "options")
        }
    }

    override fun write(json: Json) {
        json.writeValue("type", type)
        json.writeValue("options", options)
    }

}