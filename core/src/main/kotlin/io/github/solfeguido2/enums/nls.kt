package io.github.solfeguido2.enums
        
import ktx.i18n.BundleLine
import com.badlogic.gdx.utils.I18NBundle
/** Generated from assets/i18n/nls.properties file. */
enum class Nls(private val key: String) : BundleLine {
    Play("play"),
    Classic("classic"),
    Levels("levels"),
    Gclef("gclef"),
    Fclef("fclef"),
    Cclef3("cclef3"),
    Cclef4("cclef4"),
    Both("both"),
    MadeWith("made_with"),
    By("by"),
    Icons("icons"),
    Sounds("sounds"),
    Credits("credits"),
    All("all"),
    Menu("menu"),
    Back("back"),
    Paused("paused"),
    Resume("resume"),
    Finished("finished"),
    Restart("restart"),
    Days("days"),
    Level("level"),
    TotalGames("total_games"),
    TotalPoints("total_points"),
    AvgReactionTime("avg_reaction_time"),
    Statistics("statistics"),
    BestScore("best_score"),
    Score("score"),
    Scoreboard("scoreboard"),
    Loading("loading"),
    Nice("nice"),
    SettingsLanguage("settings.language"),
    SettingsNoteStyle("settings.note_style"),
    SettingsNoteStyleSubtitle("settings.note_style_subtitle"),
    SettingsEnglishNote("settings.english_note"),
    SettingsRomanNotes("settings.roman_notes"),
    SettingsLatinNotes("settings.latin_notes"),
    SettingsAnswer("settings.answer"),
    SettingsAnswerButton("settings.answer.button"),
    SettingsAnswerPiano("settings.answer.piano"),
    SettingsAnswerPianowithnotes("settings.answer.pianowithnotes"),
    SettingsTheme("settings.theme"),
    SettingsThemeLight("settings.theme.light"),
    SettingsThemeDark("settings.theme.dark"),
    AboutContributors("about.contributors"),
;
    override fun toString() = key

    override val bundle: I18NBundle
        get() = i18nBundle

    companion object {
        lateinit var i18nBundle: I18NBundle
    }
}
