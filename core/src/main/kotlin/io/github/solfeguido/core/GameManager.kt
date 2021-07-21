package io.github.solfeguido.core

import io.github.solfeguido.actors.MeasureActor
import io.github.solfeguido.events.AnswerGivenEvent
import io.github.solfeguido.events.ResultEvent
import io.github.solfeguido.factories.onResult
import io.github.solfeguido.settings.GameSettings
import io.github.solfeguido.settings.gamemode.LevelOptions
import io.github.solfeguido.structures.GameStats
import ktx.collections.GdxArray
import ktx.inject.Context
import ktx.scene2d.KStack
import kotlin.math.max

class GameManager(private val context: Context, val settings: GameSettings, private val returnCallback: () -> Unit) {

    private val statsManager = context.inject<StatsManager>()
    private val stats = GameStats()
    private val measures = GdxArray<MeasureActor>()
    private var currentMeasure = -1
    var startTime = 0L
    var pauseTime = 0L
    var pauseStart = 0L

    val hasAccidentals
        get() = settings.options.hasAccidentals()

    fun populateScene(parent: KStack, resultCallback: (ResultEvent) -> Unit) {
        val noteStyle = context.inject<PreferencesManager>().noteStyle
        measures.addAll(settings.options.generateMeasures(noteStyle))
        currentMeasure = 0
        measures.forEach {
            parent.storeActor(it)
            it.onResult { result ->
                handleResult(result)
                resultCallback(result)
                true
            }
        }
    }

    fun validateAnswer(answer: AnswerGivenEvent) {
        measures[currentMeasure].checkNote(answer.note)
        currentMeasure = (currentMeasure + 1) % measures.size
    }

    fun start() {
        startTime = System.currentTimeMillis()
    }

    fun pause() {
        pauseStart = System.currentTimeMillis()
    }

    fun resume() {
        pauseStart = 0
        pauseTime += (System.currentTimeMillis() - pauseStart)
    }

    fun handleResult(result: ResultEvent) {
        statsManager.registerResult(result)
        if (result.isCorrect) {
            stats.correctGuesses++
        } else {
            stats.wrongGuesses++
        }
    }

    fun end() {
        stats.timePlayed = max(((System.currentTimeMillis() - startTime - pauseTime) / 1000f).toInt(), 0)

        val options = settings.options
        if (options is LevelOptions) {
            context.inject<LevelManager>().registerLevelScore(options.level, stats)
        }
        statsManager.save()
    }

    fun exit() {
        this.returnCallback.invoke()
    }

}