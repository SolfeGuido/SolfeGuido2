package io.github.solfeguido.core

import com.badlogic.gdx.Preferences
import com.badlogic.gdx.utils.Json
import io.github.solfeguido.config.Constants
import io.github.solfeguido.enums.ClefEnum
import ktx.collections.GdxMap
import ktx.collections.gdxMapOf
import ktx.collections.set
import ktx.json.addClassTag
import ktx.json.fromJson
import ktx.preferences.set

class LevelManager(private val preferences: Preferences) {

    data class ClefLevelScore(val level: Int = 0, val score: Int = 0)

    private lateinit var levelScores: GdxMap<ClefEnum, ClefLevelScore>

    fun passedLevel(clef: ClefEnum, level: Int, score: Int): Boolean {
        val obj = ClefLevelScore(level, score)
        val exist = levelScores.get(clef, obj)
        if (exist.score < score) {
            levelScores[clef] = obj
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
    }
}