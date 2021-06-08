package io.github.solfeguido.factories

import com.badlogic.gdx.utils.IntIntMap
import ktx.collections.*

fun gdxIntArrayOf(vararg element: Int) = GdxIntArray().also {
    it.addAll(*element)
}

fun gdxFloatArrayOf(vararg elements: Float) = GdxFloatArray().also {
    it.addAll(*elements)
}

fun gdxIntArrayOf(range: IntRange) = GdxIntArray().also {
    it.addAll(*range.toList().toIntArray())
}

fun gdxIntIntMapOf(vararg keysToValues: Pair<Int, Int>,
                   initialCapacity: Int = defaultMapSize,
                   loadFactor: Float = defaultLoadFactor): IntIntMap {
    val map = IntIntMap(initialCapacity, loadFactor)
    keysToValues.forEach { map[it.first] = it.second }
    return map
}