package io.github.solfeguido.structures

class GameStats(
    var correctNotes: Int = 0,
    var wrongNotes: Int = 0,
    // In seconds
    var timePlayed: Int = 0,
    var timeLost: Int = 0,
    var timeWon: Int = 0
) {

    val totalNotes
        get() = correctNotes + wrongNotes
}