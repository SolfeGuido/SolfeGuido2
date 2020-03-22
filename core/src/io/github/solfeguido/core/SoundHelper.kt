package io.github.solfeguido.core

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.utils.ObjectSet
import io.github.solfeguido.Constants
import io.github.solfeguido.utils.NoteDataPool
import ktx.collections.toGdxSet
import ktx.inject.Context

class SoundHelper(private val context: Context) {

    val existingSounds : ObjectSet<MusicalNote>

    init {
        (11..30).map {  }
        existingSounds = listOf(
                "A" to (1..5),
                "C" to (2..6),
                "D#" to (2..6),
                "F#" to (2..6)
        ).map {
            pair -> pair.second.map { context.inject<NoteDataPool>().fromString("${pair.first}$it") }
        }.flatten()
         .toGdxSet()
    }

    private fun toAssetName(note: MusicalNote) = "${Constants.MUSICALNOTES_PATH}/$note.mp3"

    private fun noteExists(note: MusicalNote) = existingSounds.contains(note)

    fun playNote(note: MusicalNote, volume: Float = 1f) {
        val assetManager: AssetManager = context.inject()
        if(noteExists(note)) {
            assetManager.get<Sound>(toAssetName(note)).play(volume)
        } else {
            val pool : NoteDataPool = context.inject()
            var belowPitch = 1f
            val belowNote = pool.cloneNote(note)
            var abovePitch = 1f
            val aboveNote = pool.cloneNote(note)
            while(true) {
                belowNote.prevIndex()
                belowPitch += 1f/12f
                if(noteExists(belowNote)) {
                    assetManager.get<Sound>(toAssetName(belowNote)).play(volume, belowPitch, 0f)
                    break
                }
                aboveNote.nextIndex()
                abovePitch -= 1f/12f
                if(noteExists(aboveNote)) {
                    assetManager.get<Sound>(toAssetName(aboveNote)).play(volume, abovePitch, 0f)
                    break
                }
            }
            pool.free(belowNote)
            pool.free(aboveNote)
            // Find the closest note and play it
        }
    }
}