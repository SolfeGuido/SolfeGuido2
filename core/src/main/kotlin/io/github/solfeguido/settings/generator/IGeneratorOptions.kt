package io.github.solfeguido.settings.generator


interface IGeneratorOptions {

    fun next(): Int

    fun hasAccidentals(): Boolean
}