package io.github.solfeguido.settings.generator


sealed interface IGeneratorOptions {

    fun next(): Int

    fun hasAccidentals(): Boolean
}