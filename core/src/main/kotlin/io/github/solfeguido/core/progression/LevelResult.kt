package io.github.solfeguido.core.progression

import kotlinx.serialization.Serializable

@Serializable
data class LevelResult(val correctGuesses: Int, val wrongGuesses: Int)
