package io.github.solfeguido.core

import com.badlogic.gdx.Screen
import com.badlogic.gdx.utils.ObjectMap
import io.github.solfeguido.screens.UIScreen

typealias ScreenProvider = () -> UIScreen

class StateMachine(private val constructors : ObjectMap<Class<out UIScreen>, ScreenProvider>, current: Class<out UIScreen>) : Screen {
    private val stack = mutableListOf<UIScreen>()

    private val changes = mutableListOf<() -> Unit>()


    private fun performChanges() {
        changes.map { it.invoke() }
        changes.clear()
    }

    init {
        push(current)
    }

    private fun <Type : UIScreen>createScreen(type: Class<Type>, param: StateParameter): UIScreen = constructors[type]().also { it.create(param) }

    fun peek() = stack.last()

    fun first() = stack.first()

    fun <Type: UIScreen> push(type: Class<Type>, param: StateParameter = StateParameter.empty()): StateMachine {
        changes.add {
            stack.add(createScreen(type, param))
        }
        return this
    }

    internal inline fun <reified  Type : UIScreen> push(param:  StateParameter) = push(Type::class.java, param)

    public fun pop() : StateMachine{
        changes.add {
            stack.last().hide()
            stack.last().dispose()
            stack.removeAt(stack.size - 1)
            stack.last().show()
        }
        return this
    }

    internal inline fun <reified  Type: UIScreen>switch(param: StateParameter = StateParameter.empty()): StateMachine {
        changes.add {
            hide()
            dispose()
            stack.clear()
            stack.add( createScreen(Type::class.java, param) )
        }
        return this
    }

    override fun hide() {
        stack.reversed().map { it.hide() }
    }

    override fun show() {
        stack.reversed().map { it.show() }
    }

    override fun render(delta: Float) {
        stack.map { it.render(delta) }
        performChanges()
    }

    override fun pause() {
        stack.map { it.pause() }
    }

    override fun resume() {
        stack.map { it.resume() }
    }

    override fun resize(width: Int, height: Int) {
        stack.map { it.resize(width, height) }
    }

    override fun dispose() {
        stack.map { it.dispose() }
    }

}