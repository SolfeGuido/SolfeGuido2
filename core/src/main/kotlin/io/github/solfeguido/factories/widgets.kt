package io.github.solfeguido.factories

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.IntIntMap
import io.github.solfeguido.actors.ButtonAnswerActor
import io.github.solfeguido.actors.MeasureActor
import io.github.solfeguido.actors.PianoAnswerActor
import io.github.solfeguido.actors.TimerActor
import io.github.solfeguido.enums.ClefEnum
import io.github.solfeguido.enums.IconName
import io.github.solfeguido.enums.KeySignatureEnum
import io.github.solfeguido.settings.TimeSettings
import io.github.solfeguido.ui.*
import ktx.collections.defaultLoadFactor
import ktx.collections.defaultMapSize
import ktx.collections.set
import ktx.inject.Context
import ktx.scene2d.*
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

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
       sTextButton(icon.value, style, skin, init)

inline fun <S> KWidget<S>.sTextButton(
        text: String,
        style: String = defaultStyle,
        skin: Skin = Scene2DSkin.defaultSkin,
        init: (@Scene2dDsl STextButton).(S) -> Unit = {}) =
        actor(STextButton(text, style, skin), init)


inline fun <S> KWidget<S>.measure(
        clef: ClefEnum = ClefEnum.GClef,
        keySignature: KeySignatureEnum = KeySignatureEnum.CMajor,
        init: (@Scene2dDsl MeasureActor).(S) -> Unit = {}
) = actor(MeasureActor(clef, keySignature), init)

inline  fun <S> KWidget<S>.timer(
        context: Context,
        settings: TimeSettings,
        init: (@Scene2dDsl TimerActor).(S) -> Unit = {}
) = actor(TimerActor(context, settings), init)

inline fun <S> KWidget<S>.pianoAnswer(
        init: (@Scene2dDsl PianoAnswerActor).(S) -> Unit = {}
) = actor(PianoAnswerActor(), init)

inline fun <S> KWidget<S>.buttonAnswer(
        init: (@Scene2dDsl ButtonAnswerActor).(S) -> Unit = {}
) = actor(ButtonAnswerActor(), init)

inline fun <S> KWidget<S>.borderButton(
        text: String,
        style: String = "borderButtonStyle",
        skin: Skin = Scene2DSkin.defaultSkin, 
        init: (@Scene2dDsl STextButton).(S) -> Unit = {}
) = actor(STextButton(text, style, skin), init)

inline fun <S> KWidget<S>.answerButton (
        text: String,
        style: String = "answerButtonStyle",
        skin: Skin = Scene2DSkin.defaultSkin,
        init: (@Scene2dDsl AnswerButton).(S) -> Unit
) = actor(AnswerButton(text), init)

inline fun <S> KWidget<S>.borderContainer(
        init: BorderContainer<Actor>.(S) -> Unit = {}) : BorderContainer<Actor> {
    contract { callsInPlace(init, InvocationKind.EXACTLY_ONCE) }
    return actor(BorderContainer(), init)
}


@Scene2dDsl
inline fun RootWidget.zoomDialog(
        style: String = defaultStyle,
        skin: Skin = Scene2DSkin.defaultSkin,
        init: ZoomDialog.() -> Unit = {}
): ZoomDialog {
    contract { callsInPlace(init, InvocationKind.EXACTLY_ONCE) }
    return storeActor(ZoomDialog(style, skin)).apply(init)
}