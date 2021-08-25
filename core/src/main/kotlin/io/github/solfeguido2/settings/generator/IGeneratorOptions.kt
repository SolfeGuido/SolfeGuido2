package io.github.solfeguido2.settings.generator


sealed interface IGeneratorOptions {

    fun next(): Int

    fun hasAccidentals(): Boolean
}