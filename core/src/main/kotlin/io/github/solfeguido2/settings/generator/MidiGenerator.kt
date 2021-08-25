package io.github.solfeguido2.settings.generator

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.random.Random

@Serializable()
@SerialName("miniGenerator")
class MidiGenerator(var filename: String = "") : IGeneratorOptions {

    override fun next(): Int {
        //TODO
        return Random.nextInt()
    }

    override fun hasAccidentals() = true

}