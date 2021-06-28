package io.github.solfeguido.enums

enum class LevelDifficulty(val minimumScore: Int, val hasAccidentals: Boolean) {
    BEGINNER(20, false),
    EASY(30, false),
    NORMAL(40, true),
    HARD(50, true)

}