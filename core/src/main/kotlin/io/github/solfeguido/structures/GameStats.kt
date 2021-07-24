package io.github.solfeguido.structures

import kotlinx.serialization.Serializable

@Serializable
data class GameStats(
    var correctGuesses: Int = 0,
    var wrongGuesses: Int = 0,
    // In seconds
    var timePlayed: Int = 0,
) : Comparable<GameStats> {

    val totalGuesses
        get() = correctGuesses + wrongGuesses


    val score
        get() = (correctGuesses - wrongGuesses) * (timePlayed - wrongGuesses * 2)

    override operator fun compareTo(other: GameStats) = this.score - other.score
}