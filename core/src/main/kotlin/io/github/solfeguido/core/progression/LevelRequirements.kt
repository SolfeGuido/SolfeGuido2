package io.github.solfeguido.core.progression

import kotlinx.serialization.Serializable

@Serializable
data class LevelRequirements(
    val minScore: Int,
    val lowerNote: Int,
    val higherNote: Int,
    val hasAccidentals: Boolean = false
)