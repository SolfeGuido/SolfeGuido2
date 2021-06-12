package io.github.solfeguido.core

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.utils.ObjectMap
import io.github.solfeguido.config.Constants
import io.github.solfeguido.factories.schedule
import io.github.solfeguido.midi.MidiFile
import io.github.solfeguido.midi.event.NoteOn
import ktx.assets.async.AssetStorage
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
            schedule(wait / 1000f){
                context.inject<SoundHelper>().playNote(it)
            }
            wait += 500
        }
    }

    fun playJingle(name: String): Boolean {
        if(!jingles.containsKey(name)) {
            ktx.log.error { "Jingle $name not found" }
            return false
        }
        val jingle = jingles[name]
        if(jingle.trackCount == 0) return false
        jingle.tracks.forEach {
            it.events.filterIsInstance<NoteOn>()
                    .filter { note -> note.velocity > 0 }
                    .forEach {note ->
                        schedule(note.tick / 1000f) {
                            context.inject<SoundHelper>().playNote(note.noteValue)
                        }
                    }
        }
        return true
    }

    fun registerJingles(assetManager: AssetStorage) {
        allJingles.filter { assetManager.contains<MidiFile>(it.path())}
                .forEach {
                    jingles.put(it.nameWithoutExtension(), assetManager[it.path()])
                }
    }

}