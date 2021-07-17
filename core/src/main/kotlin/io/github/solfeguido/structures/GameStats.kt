package io.github.solfeguido.structures

class GameStats(
    val correctNotes: Int,
    val wrongNotes: Int,
    // In seconds
    val timePlayed: Int,
    val timeLost: Int,
    val timeWon: Int
) {

    val totalNotes
            get() = correctNotes + wrongNotes
}