package io.github.solfeguido.factories

import com.badlogic.gdx.utils.IntIntMap
import ktx.collections.*


fun gdxIntIntMapOf(
    vararg keysToValues: Pair<Int, Int>,
    initialCapacity: Int = defaultMapSize,
    loadFactor: Float = defaultLoadFactor
): IntIntMap {
    val map = IntIntMap(initialCapacity, loadFactor)
    keysToValues.forEach { map[it.first] = it.second }
    return map
}