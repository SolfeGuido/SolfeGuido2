package io.github.solfeguido2.core

import io.github.solfeguido2.actors.MeasureActor
import io.github.solfeguido2.events.AnswerGivenEvent
import io.github.solfeguido2.events.ResultEvent
import io.github.solfeguido2.factories.onResult
import io.github.solfeguido2.settings.GameSettings
import io.github.solfeguido2.settings.gamemode.LevelOptions
import io.github.solfeguido2.structures.GameStats
import ktx.collections.GdxArray
import ktx.inject.Context
import ktx.scene2d.KStack
import kotlin.math.max

class GameManager(private val context: Context, val settings: GameSettings, private val returnCallback: () -> Unit) {

    private val statsManager = context.inject<StatsManager>()
    private val levelManager = context.inject<LevelManager>()
    private val soundManager = context.inject<SoundManager>()
    private val preferencesManager = context.inject<PreferencesManager>()
    private val stats = GameStats()
    private val measures = GdxArray<MeasureActor>()
    private var currentMeasureIndex = -1
    var startTime = 0L
    var pauseTime = 0L
    var pauseStart = 0L

    val hasAccidentals
        get() = settings.options.hasAccidentals()

    private val currentMeasure
        get() = measures[currentMeasureIndex]

    val isPaused
        get() = pauseStart != 0L

    fun populateScene(parent: KStack, resultCallback: (ResultEvent) -> Unit) {
        val noteStyle = context.inject<PreferencesManager>().noteStyle
        measures.addAll(settings.options.generateMeasures(noteStyle))
        currentMeasureIndex = 0
        measures.forEach {
            parent.storeActor(it)
            it.onResult { result ->
                handleResult(result)
                resultCallback(result)
                true
            }
        }
        measures[currentMeasureIndex].highlightCurrentNote()
    }

    fun validateAnswer(answer: AnswerGivenEvent) {
        val measure = currentMeasure
        measure.lowlightCurrentNote()
        measure.checkNote(answer.note)
        currentMeasureIndex = (currentMeasureIndex + 1) % measures.size
        currentMeasure.highlightCurrentNote()
    }

    fun start() {
        startTime = System.currentTimeMillis()
    }

    fun pause() {
        pauseStart = System.currentTimeMillis()
        measures.forEach { it.pause() }
    }

    fun resume() {
        measures.forEach { it.resume() }
        pauseStart = 0
        pauseTime += (System.currentTimeMillis() - pauseStart)
    }

    private fun handleResult(result: ResultEvent) {
        statsManager.registerResult(result)
        soundManager.playNote(result.expected, preferencesManager.soundEnabled.volume)
        if (result.isCorrect) {
            stats.correctGuesses++
        } else {
            stats.wrongGuesses++
        }
    }

    fun end() {
        stats.timePlayed = max(((System.currentTimeMillis() - startTime - pauseTime) / 1000f).toInt(), 0)
        statsManager.saveGameScore(settings, stats)
        statsManager.save()

        measures.forEach { it.terminate() }

        val options = settings.options
        if (options is LevelOptions) {
            val unlockedLevel = levelManager.registerLevelScore(options.level, stats)
            levelManager.save()
        }
    }

    fun exit() {
        this.returnCallback.invoke()
    }

}