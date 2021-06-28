package io.github.solfeguido.core

import com.badlogic.gdx.Preferences
import com.badlogic.gdx.utils.Json
import io.github.solfeguido.config.Constants
import io.github.solfeguido.enums.ClefEnum
import io.github.solfeguido.enums.LevelDifficulty
import ktx.collections.*
import ktx.json.addClassTag
import ktx.json.fromJson
import ktx.preferences.set

class LevelManager(private val preferences: Preferences) {

    data class LevelRequirements(
        val minScore: Int,
        val lowerNote: Int,
        val higherNote: Int,
        val hasAccidentals: Boolean = false
    )

    data class LevelResult(val correctGuesses: Int, val wrongGuesses: Int)

    private lateinit var levelScores: GdxMap<ClefEnum, GdxMap<Int, LevelResult>>
    lateinit var levelRequirements: Map<ClefEnum, List<LevelRequirements>>

    private val EMPTY_RESULT = LevelResult(-1, 0)

    fun registerLevelScore(clef: ClefEnum, level: Int, score: Int): Boolean {
        val obj = LevelResult(score, 0)//TODO wrong guesses
        if(!levelScores.containsKey(clef)) {
            levelScores[clef] = gdxMapOf()
        }

        val exist = levelScores[clef].get(level, obj)
        if (exist.correctGuesses < score) {
            levelScores[clef][level] = obj
            save()
            return true
        }
        return false
    }

    private fun serializer() = Json().also { it.addClassTag<LevelResult>("levelResult") }

    private inline fun generateLevel(vararg levels: Pair<Int, Int>) = LevelDifficulty.values().flatMap { ld ->
        levels.map { (from, to) -> LevelRequirements(ld.minimumScore, from, to, ld.hasAccidentals) }
    }

    fun levelResult(clef: ClefEnum, level: Int) = levelScores.get(clef, gdxMapOf()).get(level, EMPTY_RESULT)

    fun save() {
        preferences[Constants.Preferences.LEVELS] = serializer().toJson(levelScores)
        preferences.flush()
    }

    fun load() {
        levelScores =
            preferences.getString(Constants.Preferences.LEVELS)?.let { serializer().fromJson(it) } ?: gdxMapOf()

        levelRequirements = mapOf(
            ClefEnum.GClef to generateLevel(
                60 to 66,
                67 to 75,
                76 to 82,
                53 to 59,
                53 to 82
            ),
            ClefEnum.FClef to generateLevel(
                54 to 60,
                47 to 53,
                43 to 49,
                58 to 64,
                43 to 64
            ),
            ClefEnum.CClef3 to generateLevel(
                53 to 59,
                43 to 49,
                47 to 53,
                60 to 66,
                43 to 66
            ),
            ClefEnum.CClef4 to generateLevel(
                53 to 59,
                43 to 49,
                47 to 52,
                60 to 66,
                43 to 66
            )
        )
    }
}