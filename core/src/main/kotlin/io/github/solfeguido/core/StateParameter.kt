package io.github.solfeguido.core

import java.lang.Error

class StateParameter(val data: Any = Object(), val clazz: Class<*>? = Object::class.java) {

    inline fun <reified Type: Any>get(): Type {
        if(data !is Type) throw Error("Expected type ${Type::class.java} is not the contained type $clazz")
        return data
    }

    inline fun <reified Type: Any>getOrDefault(default: Type): Type = if(data is Type) data else default

    override fun toString(): String {
        return "$data ($clazz)"
    }

    companion object {

        private val EMPTY = StateParameter()

        inline fun <reified Type: Any>witType(value: Type) = StateParameter(value, Type::class.java)

        fun empty() = EMPTY

    }

}