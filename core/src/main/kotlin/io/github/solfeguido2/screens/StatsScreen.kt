package io.github.solfeguido2.screens

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.utils.Align
import io.github.solfeguido2.core.StateMachine
import io.github.solfeguido2.core.StateParameter
import io.github.solfeguido2.core.StatsManager
import io.github.solfeguido2.enums.IconName
import io.github.solfeguido2.enums.Nls
import io.github.solfeguido2.factories.borderButton
import io.github.solfeguido2.factories.iconButton
import ktx.actors.onClick
import ktx.inject.Context
import ktx.scene2d.label
import ktx.scene2d.scene2d
import ktx.scene2d.table
import java.time.Duration

class StatsScreen(context: Context) : UIScreen(context) {

    private data class GlobalStats(
        val gamesPlayed: Int,
        val timePlayed: Long,
        val correctGuesses: Long,
        val wrongGuesses: Long,
        val averageReactionTime: Float
    )


    private fun formatSeconds(seconds: Long): String {
        val dur = Duration.ofSeconds(seconds)
        var res = ""
        val secs = dur.toSecondsPart()
        if (secs > 0) {
            res = "${secs}s"
        }
        val minutes = dur.toMinutesPart()
        if (minutes > 0) {
            res = "${minutes}mn " + res
        }
        val hours = dur.toHoursPart()
        if (hours > 0) {
            res = "${hours}h " + res
        }
        val days = dur.toDaysPart()
        if (days > 0) {
            res = "${days}j " + res
        }
        if(res.isBlank()) return "0s"

        return res
    }

    private fun generateGlobalStats(): GlobalStats {
        val allStats = context.inject<StatsManager>().allSaves()
        var time = 0L
        var correctGuesses = 0L
        var wrongGuesses = 0L
        for (stat in allStats) {
            val res = stat.result;
            time += res.timePlayed
            correctGuesses += res.correctGuesses
            wrongGuesses += res.wrongGuesses
        }
        return GlobalStats(
            allStats.size,
            time,
            correctGuesses,
            wrongGuesses,
            if (correctGuesses > 0 || wrongGuesses > 0) {
                time.toFloat() /  (correctGuesses + wrongGuesses)
            } else {
                0F
            }
        )
    }

    override fun setup(settings: StateParameter): Actor {

        val globalStats = generateGlobalStats()

        return scene2d.table {
            setFillParent(true)
            setPosition(0f, 0f)
            align(Align.center)

            slidingTable(Align.top) {

                iconButton(IconName.Home) {
                    onClick {
                        context.inject<StateMachine>().switch<MenuScreen>()
                    }
                    pad(5f)
                    it.top().left()
                }

                label(Nls.Statistics()) {
                    it.expandX()
                }
                pad(10f)
                it.fillX().expandX()
            }

            row()

            table {
                val customSpace = "  "
                label("Games played$customSpace")
                label(globalStats.gamesPlayed.toString())

                row()

                label("Time played$customSpace")
                label(formatSeconds(globalStats.timePlayed))

                row()

                label("Correct guesses$customSpace")
                label(globalStats.correctGuesses.toString())

                row()

                label("Wrong guesses$customSpace")
                label(globalStats.wrongGuesses.toString())

                row()


                label("Average reaction time$customSpace")
                label(String.format("%.2fs", globalStats.averageReactionTime))

                row()

                borderButton(Nls.Menu()) {
                    this.pad(10f)
                    it.colspan(2)
                    it.pad(10f)

                    onClick {
                        back()
                    }
                }

                it.grow()
            }

        }
    }


    override fun back(): Boolean {
        context.inject<StateMachine>().switch<MenuScreen>()
        return true
    }
}