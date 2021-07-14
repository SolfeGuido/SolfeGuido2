package io.github.solfeguido.settings

import io.github.solfeguido.enums.ClefEnum
import io.github.solfeguido.enums.KeySignatureEnum
import io.github.solfeguido.settings.generator.IGeneratorOptions
import io.github.solfeguido.settings.generator.RandomGenerator
import kotlinx.serialization.Serializable

@Serializable
class MeasureSettings(
    var clef: ClefEnum = ClefEnum.GClef,
    var signature: KeySignatureEnum = KeySignatureEnum.CMajor,
    var generator: IGeneratorOptions = RandomGenerator(50, 70)
)
