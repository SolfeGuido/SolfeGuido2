package io.github.solfeguido.core

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.utils.ObjectMap
import com.badlogic.gdx.utils.Timer
import io.github.solfeguido.config.Constants
import io.github.solfeguido.midi.MidiFile
import io.github.solfeguido.midi.event.NoteOn
import ktx.collections.gdxMapOf
import ktx.inject.Context

class Jingles(
        val context: Context,
        private val jingles: ObjectMap<String, MidiFile> = gdxMapOf()
) {

    val allJingles: Array<FileHandle> by lazy {
        Gdx.files.internal(Constants.JINGLES_PATH).list().filter { it.extension().endsWith("mid") }.toTypedArray()
    }

    fun playAllNotes() {
        var wait = 3000
        (33..90).forEach {
            Gdx.app.postRunnable {

            }
            Timer.schedule(object: Timer.Task() {
                override fun run() {
                    println("Play $it")
                    context.inject<SoundHelper>().playNote(it)
                }
            }, wait / 1000f)
            wait += 500
        }
    }

    fun playJingle(name: String): Boolean {
        if(!jingles.containsKey(name)) return false
        val jingle = jingles[name]
        if(jingle.trackCount == 0) return false
        jingle.tracks.forEach {
            it.events.filterIsInstance<NoteOn>()
                    .filter { it.velocity > 0 }
                    .forEach {
                        Timer.schedule(object: Timer.Task() {
                            override fun run() {
                                context.inject<SoundHelper>().playNote(it.noteValue)
                            }
                        }, it.tick / 1000f)
                    }
        }
        return true
    }

    fun registerJingles(assetManager: AssetManager) {
        allJingles.filter { assetManager.contains(it.path()) }.forEach {
            jingles.put(it.nameWithoutExtension(), assetManager.get(it.path()))
        }
    }

}