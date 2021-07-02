package io.github.solfeguido.factories

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import io.github.solfeguido.actors.*
import io.github.solfeguido.enums.IconName
import io.github.solfeguido.enums.SolfeGuidoPreferences
import io.github.solfeguido.settings.MeasureSettings
import io.github.solfeguido.settings.TimeSettings
import io.github.solfeguido.ui.*
import ktx.inject.Context
import ktx.scene2d.*
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

inline fun <S> KWidget<S>.slidingTable(
    align: Int,
    skin: Skin = Scene2DSkin.defaultSkin,
    init: SlidingTable.(S) -> Unit = {}
) = actor(SlidingTable(align, skin), init)

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
    init: (@Scene2dDsl STextButton).(S) -> Unit = {}
) =
    sTextButton(icon.value, style, skin, init)

inline fun <S> KWidget<S>.sTextButton(
    text: String,
    style: String = defaultStyle,
    skin: Skin = Scene2DSkin.defaultSkin,
    init: (@Scene2dDsl STextButton).(S) -> Unit = {}
) =
    actor(STextButton(text, style, skin), init)


inline fun <S> KWidget<S>.measure(
    settings: MeasureSettings,
    init: (@Scene2dDsl MeasureActor).(S) -> Unit = {}
) = actor(MeasureActor(settings), init)

inline fun <S> KWidget<S>.timer(
    context: Context,
    settings: TimeSettings,
    init: (@Scene2dDsl TimerActor).(S) -> Unit = {}
) = actor(TimerActor(context, settings), init)

inline fun <S> KWidget<S>.pianoAnswer(
    noteStyle: SolfeGuidoPreferences.NoteStyle,
    showNotes: Boolean = false,
    init: (@Scene2dDsl PianoAnswerActor).(S) -> Unit = {}
) = actor(PianoAnswerActor(noteStyle, showNotes), init)

inline fun <S> KWidget<S>.buttonAnswer(
    noteStyle: SolfeGuidoPreferences.NoteStyle,
    showAccidentals: Boolean,
    init: (@Scene2dDsl ButtonAnswerActor).(S) -> Unit = {}
) = actor(ButtonAnswerActor(noteStyle, showAccidentals), init)

inline fun <S> KWidget<S>.borderButton(
    text: String,
    style: String = "borderButtonStyle",
    skin: Skin = Scene2DSkin.defaultSkin,
    init: (@Scene2dDsl STextButton).(S) -> Unit = {}
) = actor(STextButton(text, style, skin), init)

inline fun <S> KWidget<S>.answerButton(
    text: String,
    init: (@Scene2dDsl AnswerButton).(S) -> Unit
) = actor(AnswerButton(text), init)

inline fun <S> KWidget<S>.borderContainer(
    init: BorderContainer<Actor>.(S) -> Unit = {}
): BorderContainer<Actor> {
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

inline fun <S> KWidget<S>.iconCheckBox(
    icon: IconName,
    init: IconCheckBox.(S) -> Unit = {}
): IconCheckBox {
    contract { callsInPlace(init, InvocationKind.EXACTLY_ONCE) }
    return actor(IconCheckBox(icon), init)
}

inline fun <S> KWidget<S>.score(
    initialValue: Int,
    init: ScoreActor.(S) -> Unit = {}
): ScoreActor {
    contract { callsInPlace(init, InvocationKind.EXACTLY_ONCE) }
    return actor(ScoreActor(initialValue), init)
}