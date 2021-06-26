package io.github.solfeguido.core

import com.badlogic.gdx.Preferences
import com.badlogic.gdx.utils.Json
import io.github.solfeguido.config.Constants
import io.github.solfeguido.enums.ClefEnum
import ktx.collections.*
import ktx.json.addClassTag
import ktx.json.fromJson
import ktx.preferences.set

class LevelManager(private val preferences: Preferences) {

    data class ClefLevelScore(val level: Int = 0, val score: Int = 0)
    data class LevelRequirements(val minScore: Int, val lowerNote: Int, val higherNote: Int)
    data class LevelResult(val level: Int, val correctGuesses: Int, val wrongGuesses: Int)


    private lateinit var levelScores: GdxMap<ClefEnum, ClefLevelScore>
    private val levelRequirements: GdxMap<ClefEnum, GdxArray<LevelRequirements>> = gdxMapOf()

    fun registerLevlScore(clef: ClefEnum, level: Int, score: Int): Boolean {
        val obj = ClefLevelScore(level, score)
        val exist = levelScores.get(clef, obj)
        if (exist.score < score) {
            levelScores[clef] = obj
            save()
            return true
        }
        return false
    }

    private fun serializer() = Json().also { it.addClassTag<ClefLevelScore>("clefLevelScore") }

    fun save() {
        preferences[Constants.Preferences.LEVELS] = serializer().toJson(levelScores)
        preferences.flush()
    }

    fun load() {
        levelScores =
            preferences.getString(Constants.Preferences.LEVELS)?.let { serializer().fromJson(it) } ?: gdxMapOf()

        levelRequirements[ClefEnum.GClef] = gdxArrayOf(
            // Difficulty 1
            LevelRequirements(Constants.Difficulty.BEGINNER, 60, 66),
            LevelRequirements(Constants.Difficulty.BEGINNER, 67, 75),
            LevelRequirements(Constants.Difficulty.BEGINNER, 76, 82),
            LevelRequirements(Constants.Difficulty.BEGINNER, 53, 59),
            LevelRequirements(Constants.Difficulty.BEGINNER, 53, 82),//All notes

            // Difficulty 2
            LevelRequirements(Constants.Difficulty.EASY, 60, 66),
            LevelRequirements(Constants.Difficulty.EASY, 67, 75),
            LevelRequirements(Constants.Difficulty.EASY, 76, 82),
            LevelRequirements(Constants.Difficulty.EASY, 53, 59),
            LevelRequirements(Constants.Difficulty.EASY, 53, 82),//All notes

            // Difficulty 3
            LevelRequirements(Constants.Difficulty.NORMAL, 60, 66),
            LevelRequirements(Constants.Difficulty.NORMAL, 67, 75),
            LevelRequirements(Constants.Difficulty.NORMAL, 76, 82),
            LevelRequirements(Constants.Difficulty.NORMAL, 53, 59),
            LevelRequirements(Constants.Difficulty.NORMAL, 53, 82),//All notes

            // Difficulty 4
            LevelRequirements(Constants.Difficulty.HARD, 60, 66),
            LevelRequirements(Constants.Difficulty.HARD, 67, 75),
            LevelRequirements(Constants.Difficulty.HARD, 76, 82),
            LevelRequirements(Constants.Difficulty.HARD, 53, 59),
            LevelRequirements(Constants.Difficulty.HARD, 53, 82),//All notes
        )
        levelRequirements[ClefEnum.FClef] = gdxArrayOf(
            // Difficulty 1
            LevelRequirements(Constants.Difficulty.BEGINNER, 54, 60),
            LevelRequirements(Constants.Difficulty.BEGINNER, 47, 53),
            LevelRequirements(Constants.Difficulty.BEGINNER, 43, 49),
            LevelRequirements(Constants.Difficulty.BEGINNER, 58, 64),
            LevelRequirements(Constants.Difficulty.BEGINNER, 43, 64),//All notes

            // Difficulty 2
            LevelRequirements(Constants.Difficulty.EASY, 47, 53),
            LevelRequirements(Constants.Difficulty.EASY, 43, 49),
            LevelRequirements(Constants.Difficulty.EASY, 58, 64),
            LevelRequirements(Constants.Difficulty.EASY, 54, 60),
            LevelRequirements(Constants.Difficulty.EASY, 43, 64),//All notes

            // Difficulty 3
            LevelRequirements(Constants.Difficulty.NORMAL, 54, 60),
            LevelRequirements(Constants.Difficulty.NORMAL, 47, 53),
            LevelRequirements(Constants.Difficulty.NORMAL, 43, 49),
            LevelRequirements(Constants.Difficulty.NORMAL, 58, 64),
            LevelRequirements(Constants.Difficulty.NORMAL, 43, 64),//All notes

            // Difficulty 4
            LevelRequirements(Constants.Difficulty.HARD, 54, 60),
            LevelRequirements(Constants.Difficulty.HARD, 47, 53),
            LevelRequirements(Constants.Difficulty.HARD, 43, 49),
            LevelRequirements(Constants.Difficulty.HARD, 58, 64),
            LevelRequirements(Constants.Difficulty.HARD, 43, 64),//All notes
        )

        levelRequirements[ClefEnum.CClef3] = gdxArrayOf(
            // Difficulty 1
            LevelRequirements(Constants.Difficulty.BEGINNER, 53, 59),
            LevelRequirements(Constants.Difficulty.BEGINNER, 43, 49),
            LevelRequirements(Constants.Difficulty.BEGINNER, 47, 52),
            LevelRequirements(Constants.Difficulty.BEGINNER, 60, 66),
            LevelRequirements(Constants.Difficulty.BEGINNER, 43, 66),//All notes

            // Difficulty 2
            LevelRequirements(Constants.Difficulty.EASY, 53, 59),
            LevelRequirements(Constants.Difficulty.EASY, 43, 49),
            LevelRequirements(Constants.Difficulty.EASY, 47, 52),
            LevelRequirements(Constants.Difficulty.EASY, 60, 66),
            LevelRequirements(Constants.Difficulty.EASY, 43, 66),//All notes

            // Difficulty 3
            LevelRequirements(Constants.Difficulty.NORMAL, 53, 59),
            LevelRequirements(Constants.Difficulty.NORMAL, 43, 49),
            LevelRequirements(Constants.Difficulty.NORMAL, 47, 52),
            LevelRequirements(Constants.Difficulty.NORMAL, 60, 66),
            LevelRequirements(Constants.Difficulty.NORMAL, 43, 66),//All notes

            // Difficulty 4
            LevelRequirements(Constants.Difficulty.HARD, 53, 59),
            LevelRequirements(Constants.Difficulty.HARD, 43, 49),
            LevelRequirements(Constants.Difficulty.HARD, 47, 52),
            LevelRequirements(Constants.Difficulty.HARD, 60, 66),
            LevelRequirements(Constants.Difficulty.HARD, 43, 66),//All notes
        )
        levelRequirements[ClefEnum.CClef4] = gdxArrayOf(
            // Difficulty 1
            LevelRequirements(Constants.Difficulty.BEGINNER, 53, 59),
            LevelRequirements(Constants.Difficulty.BEGINNER, 43, 49),
            LevelRequirements(Constants.Difficulty.BEGINNER, 47, 52),
            LevelRequirements(Constants.Difficulty.BEGINNER, 60, 66),
            LevelRequirements(Constants.Difficulty.BEGINNER, 43, 66),//All notes

            // Difficulty 2
            LevelRequirements(Constants.Difficulty.BEGINNER, 53, 59),
            LevelRequirements(Constants.Difficulty.BEGINNER, 43, 49),
            LevelRequirements(Constants.Difficulty.BEGINNER, 47, 52),
            LevelRequirements(Constants.Difficulty.BEGINNER, 60, 66),
            LevelRequirements(Constants.Difficulty.BEGINNER, 43, 66),//All notes

            // Difficulty 3
            LevelRequirements(Constants.Difficulty.BEGINNER, 53, 59),
            LevelRequirements(Constants.Difficulty.BEGINNER, 43, 49),
            LevelRequirements(Constants.Difficulty.BEGINNER, 47, 52),
            LevelRequirements(Constants.Difficulty.BEGINNER, 60, 66),
            LevelRequirements(Constants.Difficulty.BEGINNER, 43, 66),//All notes

            // Difficulty 4
            LevelRequirements(Constants.Difficulty.BEGINNER, 53, 59),
            LevelRequirements(Constants.Difficulty.BEGINNER, 43, 49),
            LevelRequirements(Constants.Difficulty.BEGINNER, 47, 52),
            LevelRequirements(Constants.Difficulty.BEGINNER, 60, 66),
            LevelRequirements(Constants.Difficulty.BEGINNER, 43, 66),//All notes
        )
    }
}