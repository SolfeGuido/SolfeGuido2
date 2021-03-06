package io.github.solfeguido.config

import io.github.solfeguido.enums.ClefEnum
import ktx.collections.gdxMapOf

class ClefConfig private constructor(val height: Float, val baseLine: Float) {

    companion object {
        val GClef = ClefConfig(7.4f, 1.8f)
        val FClef = ClefConfig(5.7f, 3f)
        val CClef3 = ClefConfig(4.5f, 3.5f)
        val CClef4 = ClefConfig(4.5f, 4.5f)

        val ClefEquivalent = gdxMapOf(
                ClefEnum.CClef4 to CClef4,
                ClefEnum.CClef3 to CClef3,
                ClefEnum.FClef to FClef,
                ClefEnum.GClef to GClef
        )

        val ClefMinNote = gdxMapOf(
                ClefEnum.GClef to 53,
                ClefEnum.FClef to 33,
                ClefEnum.CClef3 to 43,
                ClefEnum.CClef4 to 47
        )
    }


}