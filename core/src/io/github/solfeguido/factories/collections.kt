package io.github.solfeguido.factories

import ktx.collections.GdxIntArray
import ktx.collections.toGdxArray

fun gdxIntArrayOf(vararg element: Int) = GdxIntArray().also {
    it.addAll(*element)
}

fun gdxIntArrayOf(range: IntRange) = GdxIntArray().also {
    it.addAll(*range.toList().toIntArray())
}