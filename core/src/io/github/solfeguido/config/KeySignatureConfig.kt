package io.github.solfeguido.config

import io.github.solfeguido.core.music.NoteAccidentalEnum
import io.github.solfeguido.enums.ClefEnum
import ktx.collections.gdxArrayOf
import ktx.collections.gdxMapOf

object KeySignatureConfig {

    val sharpOrder = gdxArrayOf(8, 5, 9, 6, 3, 7, 4)
    val flatOrder = gdxArrayOf(4, 7, 3, 6, 2, 5, 1)

    val cClef4Order = gdxArrayOf(2, 6, 3, 7, 4, 8, 5)

    val ClefTranslate = gdxMapOf(
            ClefEnum.GClef to 0,
            ClefEnum.FClef to -2,
            ClefEnum.CClef3 to -1,
            ClefEnum.CClef4 to 1
    )
}