package io.github.solfeguido.settings.gamemode

import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonValue
import io.github.solfeguido.enums.KeySignatureEnum
import io.github.solfeguido.enums.NoteOrderEnum
import io.github.solfeguido.settings.MeasureSettings
import io.github.solfeguido.settings.generator.IGeneratorOptions
import io.github.solfeguido.settings.generator.RandomGenerator
import io.github.solfeguido.ui.events.ResultEvent
import ktx.inject.Context
import ktx.json.*
import ktx.scene2d.KStack

class EarTrainingOptions : IGameModeOptions {

    var keySignature = KeySignatureEnum.CMajor
    var measure: MeasureSettings = MeasureSettings()
    var generator: IGeneratorOptions = RandomGenerator()

    override fun read(json: Json, jsonData: JsonValue) {
        keySignature = json.readValue(jsonData, "keySignature")
        measure = json.readValue(jsonData, "measure")
        generator = IGeneratorOptions.toInstance(json, jsonData)
    }

    override fun write(json: Json) {
        super.write(json)
        json.writeValue("generator", generator)
        json.writeValue("measure", measure)
        json.writeValue("keySignature", keySignature)
    }

    override fun populateScene(parent: KStack, resultCallback: (ResultEvent) -> Unit) {
    // TODO
    }

    override fun endGame(context: Context, score: Int) {
        // TODO
    }

    override fun hasAccidentals(): Boolean {
        TODO("Not yet implemented")
    }

    override fun validateNote(note: NoteOrderEnum) = false
}