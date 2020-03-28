package io.github.solfeguido.ui

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.IntIntMap
import io.github.solfeguido.utils.IconName
import ktx.actors.onChange
import ktx.actors.onClick
import ktx.collections.defaultLoadFactor
import ktx.collections.defaultMapSize
import ktx.collections.set
import ktx.log.info
import ktx.scene2d.*

inline fun <S> KWidget<S>.slidingTable(
        align: Int,
        skin: Skin = Scene2DSkin.defaultSkin,
        init: SlidingTable.(S) -> Unit = {}) = actor(SlidingTable(align, skin), init)

inline fun <S> KWidget<S>.icon(
        icon: IconName,
        style: String = "iconStyle",
        skin: Skin = Scene2DSkin.defaultSkin,
        init: (@Scene2dDsl Label).(S) -> Unit = {}
) = label(icon.value, style, skin, init)

inline fun <S> KWidget<S>.iconButton(
        icon: IconName,
        style: String = "iconButtonStyle",
        skin: Skin = Scene2DSkin.defaultSkin,
        init: (@Scene2dDsl STextButton).(S) -> Unit = {}) =
        actor(STextButton(icon.value, style, skin), init)


fun gdxIntIntMapOf(vararg keysToValues: Pair<Int, Int>,
                                 initialCapacity: Int = defaultMapSize,
                                 loadFactor: Float = defaultLoadFactor): IntIntMap {
    val map = IntIntMap(initialCapacity, loadFactor)
    keysToValues.forEach { map[it.first] = it.second }
    return map
}