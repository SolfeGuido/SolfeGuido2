package io.github.solfeguido.core

import com.badlogic.gdx.audio.Sound
import io.github.solfeguido.structures.Constants
import io.github.solfeguido.factories.MidiNotePool
import io.github.solfeguido.structures.MidiNote
import ktx.assets.async.AssetStorage
import ktx.collections.*

typealias SoundMap = GdxMap<MidiNote, Pair<Int, Float>>

class SoundManager(private val assetStorage: AssetStorage) {

    companion object {
        private val SOUND_INDEXES = listOf(0, 2, 3, 5, 7, 8, 10)

        fun toAssetName(midiIndex: Int) = "${Constants.MUSICAL_NOTES_PATH}/$midiIndex.ogg"
    }

    private val loadedSounds: GdxSet<MidiNote> = GdxSet()
    val existingSounds: SoundMap = (0..5).map { mult ->
        SOUND_INDEXES.map { baseIndex -> 33 + 12 * mult + baseIndex }
                    .filter { it in 33..89 }
                    .map {
            val note = MidiNotePool.fromIndex(it)
            loadedSounds.add(note)
            note to (note.midiIndex to 1f)
        }
    }.flatten().toGdxMap(0, defaultLoadFactor, { it.second }, { it.first })


    private fun isLoadedSound(note: MidiNote) = loadedSounds.contains(note)

    private fun noteExists(note: MidiNote) = existingSounds.containsKey(note)

    private fun ensureNoteExists(note: MidiNote) {
        if (noteExists(note)) return
        var belowPitch = 1f
        val belowNote = MidiNotePool.cloneNote(note)
        var abovePitch = 1f
        val aboveNote = MidiNotePool.cloneNote(note)
        while (true) {
            belowNote -= 1
            belowPitch += 1f / 12f
            if (isLoadedSound(belowNote)) {
                existingSounds.put(MidiNotePool.cloneNote(note), (belowNote.midiIndex to belowPitch))
                break
            }
            aboveNote += 1
            abovePitch -= 0.5f / 12f
            if (isLoadedSound(aboveNote)) {
                existingSounds.put(MidiNotePool.cloneNote(note), (aboveNote.midiIndex to abovePitch))
                break
            }
        }
        MidiNotePool.free(aboveNote)
        MidiNotePool.free(belowNote)
    }

    fun playNote(midiIndex: Int, volume: Float = 1f) {
        MidiNotePool.withIndex(midiIndex) { playNote(it, volume) }
    }

    fun playNote(note: MidiNote, volume: Float = 1f) {
        ensureNoteExists(note)
        val assetData = existingSounds[note]!!
        assetStorage.get<Sound>(toAssetName(assetData.first)).play(volume, assetData.second, 0f)
    }
}