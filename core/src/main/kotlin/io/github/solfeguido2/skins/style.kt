package io.github.solfeguido2.skins

import com.badlogic.gdx.scenes.scene2d.ui.Skin
import io.github.solfeguido2.ui.STextButton
import ktx.style.SkinDsl
import ktx.style.addStyle
import ktx.style.defaultStyle
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@SkinDsl
inline fun Skin.borderButton(
        name: String = defaultStyle,
        init: (@SkinDsl STextButton.STextButtonStyle).() -> Unit = {}
): STextButton.STextButtonStyle {
    contract { callsInPlace(init, InvocationKind.EXACTLY_ONCE) }
    return addStyle(name, STextButton.STextButtonStyle(), init)
}