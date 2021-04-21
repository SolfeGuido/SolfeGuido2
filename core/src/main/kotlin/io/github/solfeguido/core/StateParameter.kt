package io.github.solfeguido.core

import java.lang.Error

class StateParameter(val data: Any = Object(), val clazz: Class<*>? = Object::class.java) {

    inline fun <reified Type: Any>getValue(): Type {
        if(data !is Type) throw Error("Expected type ${Type::class.java} is not the contained type $clazz")
        return data
    }

    companion object {

        private val EMPTY = StateParameter()

        inline fun <reified Type: Any>witType(value: Type) = StateParameter(value, Type::class.java)

        fun empty() = EMPTY

    }

}