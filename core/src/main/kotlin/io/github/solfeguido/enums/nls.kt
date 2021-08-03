package io.github.solfeguido.enums
import com.badlogic.gdx.utils.I18NBundle
import ktx.i18n.BundleLine
/** Generated from assets/i18n/nls.properties file. */
enum class Nls(private val key: String) : BundleLine {
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
    LongestStreak("longest_streak"),
    Statistics("statistics"),
    BestScore("best_score"),
    Score("score"),
    Scoreboard("scoreboard"),
    Loading("loading"),
    NewVersion("new_version"),
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

