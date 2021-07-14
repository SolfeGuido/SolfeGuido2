package io.github.solfeguido.core.progression

import io.github.solfeguido.enums.ClefEnum
import kotlinx.serialization.Serializable

@Serializable
class Level(val clef: ClefEnum, val stage: Int, val requirements: LevelRequirements) {

}