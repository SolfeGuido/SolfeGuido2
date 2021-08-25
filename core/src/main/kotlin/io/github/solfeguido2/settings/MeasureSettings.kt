package io.github.solfeguido2.settings

import io.github.solfeguido2.enums.ClefEnum
import io.github.solfeguido2.enums.KeySignatureEnum
import io.github.solfeguido2.settings.generator.IGeneratorOptions
import io.github.solfeguido2.settings.generator.RandomGenerator
import kotlinx.serialization.Serializable

@Serializable
class MeasureSettings(
    var clef: ClefEnum = ClefEnum.GClef,
    var signature: KeySignatureEnum = KeySignatureEnum.CMajor,
    var generator: IGeneratorOptions = RandomGenerator(50, 70)
)
