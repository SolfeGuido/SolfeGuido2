package io.github.solfeguido.core

import com.badlogic.gdx.Preferences
import io.github.solfeguido.structures.progression.Level
import io.github.solfeguido.structures.progression.LevelRequirements
import io.github.solfeguido.structures.Constants
import io.github.solfeguido.enums.ClefEnum
import io.github.solfeguido.enums.LevelDifficulty
import io.github.solfeguido.structures.GameStats
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ktx.preferences.get
import ktx.preferences.set

class LevelManager(private val preferences: Preferences) {

    companion object {
        private val EMPTY_RESULT = GameStats()
    }

    private lateinit var levelScores: HashMap<ClefEnum, HashMap<Int, GameStats>>
    lateinit var levelRequirements: HashMap<ClefEnum, List<LevelRequirements>>


    fun registerLevelScore(level: Level, gameStats: GameStats): Boolean {
        val clef = level.clef
        val difficulty = level.stage
        if (!levelScores.containsKey(clef)) {
            levelScores[clef] = hashMapOf()
        }

        val exist = levelScores[clef]!!.getOrDefault(difficulty, EMPTY_RESULT)
        println("Scored ${gameStats.score}, vs : ${exist.score}")
        if (exist < gameStats) {
            levelScores[clef]!![difficulty] = gameStats
            save()
            return true
        }
        return false
    }


    private fun generateLevel(vararg levels: Pair<Int, Int>) = LevelDifficulty.values().flatMap { ld ->
        levels.map { (from, to) -> LevelRequirements(ld.minimumScore, from, to, ld.hasAccidentals) }
    }

    fun generateLevel(clef: ClefEnum, level: Int) = Level(clef, level, levelRequirements[clef]!![level])

    fun levelResult(clef: ClefEnum, level: Int): GameStats = levelScores[clef]?.get(level) ?: EMPTY_RESULT

    fun save() {
        preferences[Constants.Preferences.LEVELS] = Json.encodeToString(levelScores)
        preferences.flush()
    }

    fun load() {
        val pref: String? = preferences[Constants.Preferences.LEVELS]

        levelScores = if (pref.isNullOrBlank()) {
            hashMapOf()
        } else {
            Json.decodeFromString(pref)
        }

        levelRequirements = hashMapOf(
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