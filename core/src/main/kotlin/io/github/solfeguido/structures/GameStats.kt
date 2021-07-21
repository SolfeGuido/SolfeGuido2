package io.github.solfeguido.structures

import kotlinx.serialization.Serializable

@Serializable
class GameStats(
    var correctGuesses: Int = 0,
    var wrongGuesses: Int = 0,
    // In seconds
    var timePlayed: Int = 0,
) {

    val totalGuesses
        get() = correctGuesses + wrongGuesses


    val score = (correctGuesses - wrongGuesses) * (timePlayed - wrongGuesses * 2)

    operator fun compareTo(other: GameStats) = this.score - other.score
}