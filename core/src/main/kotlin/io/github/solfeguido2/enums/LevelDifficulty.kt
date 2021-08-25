package io.github.solfeguido2.enums

enum class LevelDifficulty(val minimumScore: Int, val hasAccidentals: Boolean) {
    BEGINNER(20, false),
    EASY(30, false),
    NORMAL(40, true),
    HARD(50, true)

}