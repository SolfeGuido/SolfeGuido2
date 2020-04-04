package io.github.solfeguido.core

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.audio.Sound
import io.github.solfeguido.config.Constants
import io.github.solfeguido.factories.MidiNotePool
import ktx.collections.*
import ktx.inject.Context

typealias SoundMap = GdxMap<MidiNote, Pair<Int, Float>>

class SoundHelper(private val context: Context) {

    private val loadedSounds :GdxSet<MidiNote> = GdxSet()
    val existingSounds : SoundMap

    init {
        existingSounds=  (33..90 step 3).map {
                val note = MidiNotePool.fromIndex(it)
                loadedSounds.add(note)
                 note to (note.midiIndex to 1f)
        }.toGdxMap(0, defaultLoadFactor, {it.second}, {it.first})

    }

    private fun toAssetName(note: MidiNote) = toAssetName(note.midiIndex)
    private fun toAssetName(midiIndex: Int) = "${Constants.MUSICALNOTES_PATH}/$midiIndex.mp3"

    private fun isLoadedSound(note: MidiNote) = loadedSounds.contains(note)

    private fun noteExists(note: MidiNote) = existingSounds.containsKey(note)

    private fun ensureNoteExists(note: MidiNote) {
        if(noteExists(note)) return
        var belowPitch = 1f
        val belowNote = MidiNotePool.cloneNote(note)
        var abovePitch = 1f
        val aboveNote = MidiNotePool.cloneNote(note)
        while(true) {
            belowNote -= 1
            belowPitch += 1f/12f
            if(isLoadedSound(belowNote)) {
                existingSounds.put(MidiNotePool.cloneNote(note), (belowNote.midiIndex to belowPitch))
                break
            }
            aboveNote += 1
            abovePitch -= 0.5f/12f
            if(isLoadedSound(aboveNote)) {
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
        context.inject<AssetManager>().get<Sound>(toAssetName(assetData.first)).play(volume, assetData.second, 0f)
    }
}