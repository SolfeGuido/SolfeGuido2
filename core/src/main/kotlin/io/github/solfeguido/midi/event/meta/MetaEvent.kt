//////////////////////////////////////////////////////////////////////////////
//	Copyright 2011 Alex Leffelman
// Translated and adapted to kotlin by AzariasB 2020
//
//	Licensed under the Apache License, Version 2.0 (the "License");
//	you may not use this file except in compliance with the License.
//	You may obtain a copy of the License at
//
//	http://www.apache.org/licenses/LICENSE-2.0
//
//	Unless required by applicable law or agreed to in writing, software
//	distributed under the License is distributed on an "AS IS" BASIS,
//	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//	See the License for the specific language governing permissions and
//	limitations under the License.
//////////////////////////////////////////////////////////////////////////////

package io.github.solfeguido.midi.event.meta

import io.github.solfeguido.midi.event.MidiEvent
import io.github.solfeguido.midi.util.VariableLengthInt
import java.io.InputStream
import java.io.OutputStream

abstract class MetaEvent protected constructor(tick: Long, delta: Long, type: Int, length: VariableLengthInt) : MidiEvent(tick, delta) {
    protected var mType: Int = type and 0xFF
    protected var mLength: VariableLengthInt = length

    override  fun writeToFile(out: OutputStream, writeType: Boolean) {
        writeToFile(out)
    }

    protected open fun writeToFile(out: OutputStream) {
        super.writeToFile(out, true)
        out.write(0xFF)
        out.write(mType)
    }

    class MetaEventData(input: InputStream) {
        val type: Int
        val length: VariableLengthInt
        val data: ByteArray

        init {
            type = input.read()
            length = VariableLengthInt(input)
            data = ByteArray(length.varValue)
            if (length.varValue > 0) {
                input.read(data)
            }
        }
    }

    companion object {
        fun parseMetaEvent(tick: Long, delta: Long, `in`: InputStream): MetaEvent {
            val eventData = MetaEventData(`in`)
            var isText = false
            when (eventData.type) {
                SEQUENCE_NUMBER, MIDI_CHANNEL_PREFIX, END_OF_TRACK, TEMPO, SMPTE_OFFSET, TIME_SIGNATURE, KEY_SIGNATURE -> {
                }
                TEXT_EVENT, COPYRIGHT_NOTICE, TRACK_NAME, INSTRUMENT_NAME, LYRICS, MARKER, CUE_POINT, SEQUENCER_SPECIFIC -> isText = true
                else -> isText = true
            }
            if (isText) {
                return EmptyEvent(tick, delta)
                /*
                Keep original code if needed, but really don't thing It's useful
                val text = String(eventData.data)
                return when (eventData.type) {
                    TEXT_EVENT -> Text(tick, delta, text)
                    COPYRIGHT_NOTICE -> CopyrightNotice(tick, delta, text)
                    TRACK_NAME -> TrackName(tick, delta, text)
                    INSTRUMENT_NAME -> InstrumentName(tick, delta, text)
                    LYRICS -> Lyrics(tick, delta, text)
                    MARKER -> Marker(tick, delta, text)
                    CUE_POINT -> CuePoint(tick, delta, text)
                    SEQUENCER_SPECIFIC -> SequencerSpecificEvent(tick, delta, eventData.data)
                    else -> GenericMetaEvent(tick, delta, eventData)
                }*/
            }
            when (eventData.type) {
                SEQUENCE_NUMBER -> return SequenceNumber.parseSequenceNumber(tick, delta, eventData)
                MIDI_CHANNEL_PREFIX -> return MidiChannelPrefix.parseMidiChannelPrefix(tick, delta, eventData)
                END_OF_TRACK -> return EndOfTrack(tick, delta)
                TEMPO -> return Tempo.parseTempo(tick, delta, eventData)
                SMPTE_OFFSET -> return SmpteOffset.parseSmpteOffset(tick, delta, eventData)
                TIME_SIGNATURE -> return TimeSignature.parseTimeSignature(tick, delta, eventData)
                KEY_SIGNATURE -> return KeySignature.parseKeySignature(tick, delta, eventData)
            }
            return EmptyEvent(tick, delta)
        }

        const val SEQUENCE_NUMBER = 0
        const val TEXT_EVENT = 1
        const val COPYRIGHT_NOTICE = 2
        const val TRACK_NAME = 3
        const val INSTRUMENT_NAME = 4
        const val LYRICS = 5
        const val MARKER = 6
        const val CUE_POINT = 7
        const val MIDI_CHANNEL_PREFIX = 0x20
        const val END_OF_TRACK = 0x2F
        const val TEMPO = 0x51
        const val SMPTE_OFFSET = 0x54
        const val TIME_SIGNATURE = 0x58
        const val KEY_SIGNATURE = 0x59
        const val SEQUENCER_SPECIFIC = 0x7F
        const val EMPTY = 0xFF
    }

}