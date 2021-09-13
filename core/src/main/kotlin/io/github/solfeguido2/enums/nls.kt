package io.github.solfeguido2.enums
        
import ktx.i18n.BundleLine
import com.badlogic.gdx.utils.I18NBundle
/** Generated from assets/i18n/nls.properties file. */
enum class Nls(private val key: String) : BundleLine {
    Play("play"),
    Classic("classic"),
    Levels("levels"),
    LevelUnlocked("level_unlocked"),
    ModeClassic("mode.classic"),
    ModeLevel("mode.level"),
    Settings("settings"),
    MadeWith("made_with"),
    By("by"),
    Icons("icons"),
    Sounds("sounds"),
    Credits("credits"),
    Menu("menu"),
    Paused("paused"),
    Resume("resume"),
    Finished("finished"),
    Level("level"),
    TotalGames("total_games"),
    TotalTime("total_time"),
    CorrectGuesses("correct_guesses"),
    WrongGuesses("wrong_guesses"),
    AvgReactionTime("avg_reaction_time"),
    Statistics("statistics"),
    Score("score"),
    Scoreboard("scoreboard"),
    Loading("loading"),
    Nice("nice"),
    DialogPauseText("dialog.pause_text"),
    SettingsVibrations("settings.vibrations"),
    SettingsSound("settings.sound"),
    SettingsLanguage("settings.language"),
    SettingsNoteStyle("settings.note_style"),
    SettingsAnswer("settings.answer"),
    SettingsTheme("settings.theme"),
    KeySignatureCMajor("key_signature.c_major"),
    KeySignatureGMajor("key_signature.g_major"),
    KeySignatureDMajor("key_signature.d_major"),
    KeySignatureAMajor("key_signature.a_major"),
    KeySignatureEMajor("key_signature.e_major"),
    KeySignatureBMajor("key_signature.b_major"),
    KeySignatureFSharpMajor("key_signature.f_sharp_major"),
    KeySignatureCSharpMajor("key_signature.c_sharp_major"),
    KeySignatureCFlatMajor("key_signature.c_flat_major"),
    KeySignatureGFlatMajor("key_signature.g_flat_major"),
    KeySignatureDFlatMajor("key_signature.d_flat_major"),
    KeySignatureAFlatMajor("key_signature.a_flat_major"),
    KeySignatureEFlatMajor("key_signature.e_flat_major"),
    KeySignatureBFlatMajor("key_signature.b_flat_major"),
    KeySignatureFMajor("key_signature.f_major"),
    KeySignatureAMinor("key_signature.a_minor"),
    KeySignatureEMinor("key_signature.e_minor"),
    KeySignatureBMinor("key_signature.b_minor"),
    KeySignatureFSharpMinor("key_signature.f_sharp_minor"),
    KeySignatureCSharpMinor("key_signature.c_sharp_minor"),
    KeySignatureGSharpMinor("key_signature.g_sharp_minor"),
    KeySignatureDSharpMinor("key_signature.d_sharp_minor"),
    KeySignatureASharpMinor("key_signature.a_sharp_minor"),
    KeySignatureAFlatMinor("key_signature.a_flat_minor"),
    KeySignatureEFlatMinor("key_signature.e_flat_minor"),
    KeySignatureBFlatMinor("key_signature.b_flat_minor"),
    KeySignatureFMinor("key_signature.f_minor"),
    KeySignatureCMinor("key_signature.c_minor"),
    KeySignatureGMinor("key_signature.g_minor"),
    KeySignatureDMinor("key_signature.d_minor"),
;
    override fun toString() = key

    override val bundle: I18NBundle
        get() = i18nBundle

    companion object {
        lateinit var i18nBundle: I18NBundle
    }
}
