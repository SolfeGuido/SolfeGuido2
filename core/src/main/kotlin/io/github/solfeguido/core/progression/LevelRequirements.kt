package io.github.solfeguido.core.progression

data class LevelRequirements(
    val minScore: Int,
    val lowerNote: Int,
    val higherNote: Int,
    val hasAccidentals: Boolean = false
)