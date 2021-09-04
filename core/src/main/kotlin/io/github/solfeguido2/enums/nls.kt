package io.github.solfeguido2.enums
        
import ktx.i18n.BundleLine
import com.badlogic.gdx.utils.I18NBundle
/** Generated from assets/i18n/nls.properties file. */
enum class Nls(private val key: String) : BundleLine {
    Play("play"),
    Classic("classic"),
    Levels("levels"),
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
    AboutContributors("about.contributors"),
;
    override fun toString() = key

    override val bundle: I18NBundle
        get() = i18nBundle

    companion object {
        lateinit var i18nBundle: I18NBundle
    }
}
