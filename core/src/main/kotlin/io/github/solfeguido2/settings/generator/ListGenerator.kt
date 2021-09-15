package io.github.solfeguido2.settings.generator

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable()
@SerialName("miniGenerator")
class ListGenerator(val list: List<Int>) : IGeneratorOptions {

    @Transient
    var currentIndex = 0

    override fun next(): Int {
        if (list.isEmpty()) return 0
        val res = list[currentIndex]
        currentIndex = (currentIndex + 1) % list.size
        return res
    }

    override fun hasAccidentals(): Boolean {
        return false
    }


}