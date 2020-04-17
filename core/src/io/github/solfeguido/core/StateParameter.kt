package io.github.solfeguido.core

import java.lang.Error

class StateParameter(val data: Any? = null, val clazz: Class<*>? = null) {

    val hasValue: Boolean = data != null

    inline fun <reified Type: Any>getValue(): Type {
        if(!hasValue) throw Error("No parameter")
        if(data !is Type) throw Error("Expected type ${Type::class.java} is not the contained type $clazz")
        return data
    }

    companion object {

        private val EMPTY = StateParameter()

        inline fun <reified Type: Any>witType(value: Type) = StateParameter(value, Type::class.java)

        fun empty() = EMPTY

    }

}