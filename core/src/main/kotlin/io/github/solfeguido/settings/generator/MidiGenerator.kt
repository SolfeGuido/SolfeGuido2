package io.github.solfeguido.settings.generator

import kotlin.random.Random

class MidiGenerator(var filename: String = "") : IGeneratorOptions {

    override fun next(): Int {
        //TODO
        return Random.nextInt()
    }

    override fun hasAccidentals() = true

}