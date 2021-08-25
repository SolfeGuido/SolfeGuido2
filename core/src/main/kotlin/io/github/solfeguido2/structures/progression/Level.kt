package io.github.solfeguido2.structures.progression

import io.github.solfeguido2.enums.ClefEnum
import kotlinx.serialization.Serializable

@Serializable
class Level(val clef: ClefEnum, val stage: Int, val requirements: LevelRequirements) {

}