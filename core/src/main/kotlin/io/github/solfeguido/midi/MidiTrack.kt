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

package io.github.solfeguido.midi

import io.github.solfeguido.midi.event.MidiEvent
import io.github.solfeguido.midi.event.NoteOff
import io.github.solfeguido.midi.event.NoteOn
import io.github.solfeguido.midi.event.meta.EndOfTrack
import io.github.solfeguido.midi.event.meta.Tempo
import io.github.solfeguido.midi.event.meta.TimeSignature
import io.github.solfeguido.midi.util.VariableLengthInt
import io.github.solfeguido.midi.util.bytesEqual
import io.github.solfeguido.midi.util.bytesToInt
import io.github.solfeguido.midi.util.intToBytes
import ktx.log.info
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.io.OutputStream
import java.lang.IllegalArgumentException
import java.util.*

class MidiTrack() {

    companion object {
        const val VERBOSE = false
        val IDENTIFIER = byteArrayOf(
            'M'.code.toByte(),
            'T'.code.toByte(),
            'r'.code.toByte(),
            'k'.code.toByte()
        )

        fun createTempTrack() = MidiTrack().also {
            it.insertEvent(TimeSignature())
            it.insertEvent(Tempo())
        }
    }

    val eventCount
        get() = events.size


    val lengthInTicks: Long
        get() {
            if(events.isEmpty()) return 0L
            return events.last().tick
        }

    private var _size = 0
    var size: Int
        private set(p) {
            _size = p
        }
        get() {
            if(sizeNeedsRecalculating) {
                recalculateSize()
            }
            return _size
        }


    var sizeNeedsRecalculating = false
        private set
    var closed = false
        private set
    var endOfTrackDelta = 0L

    var events: TreeSet<MidiEvent> = TreeSet()
        private set


    constructor(input: InputStream): this() {
        var buffer = ByteArray(4)
        input.read(buffer)

        if(!bytesEqual(IDENTIFIER, buffer, 0, 4)) {
            info { "${buffer.map { it.toInt().toChar() }}" }
            ktx.log.error { "Track identifier did not match MTrk" }
            return
        }

        input.read(buffer)
        size = bytesToInt(buffer, 0, 4)
        buffer = ByteArray(size)
        input.read(buffer)
        this.readTrackData(buffer)
    }

    private fun readTrackData(data: ByteArray) {
        val input = ByteArrayInputStream(data)
        var totalTicks = 0L

        while(input.available() > 0) {
            val delta = VariableLengthInt(input)
            totalTicks += delta.varValue

            val event = MidiEvent.parseEvent(totalTicks, delta.varValue.toLong(), input)
            if(event == null){
                info { "Event skipped" }
                continue
            }

            if(VERBOSE) {
                info { "$event" }
            }

            if(event is EndOfTrack) {
                endOfTrackDelta = event.delta
                break
            }
            events.add(event)
        }
    }

    private fun recalculateSize(){
        size = 0
        var last: MidiEvent? = null
        events.forEach {
            size += it.size

            if(last != null && it.requiresStatusByte(last)){
                size--
            }
            last = it
        }
        sizeNeedsRecalculating = false
    }

    fun rinsertNote(channel: Int, pitch: Int, velocity: Int, tick: Long, duration: Long) {
        insertEvent(NoteOn(tick, channel, pitch, velocity))
        insertEvent(NoteOff(tick+ duration, channel, pitch, 0))
    }

    fun insertEvent(nwEvent: MidiEvent?) {
        if(nwEvent !is MidiEvent) return
        if(closed) {
            ktx.log.error { "Cannot add event to closed track" }
            return
        }

        val prev = events.floor(nwEvent)
        val next = events.ceiling(nwEvent)

        events.add(nwEvent)
        sizeNeedsRecalculating = true
        nwEvent.delta = nwEvent.tick - (prev?.tick ?: 0L)

        next?.delta = (next?.tick ?: 0L) - nwEvent.tick
        size  += nwEvent.size

        if(nwEvent is EndOfTrack) {
            if(next != null) {
                throw IllegalArgumentException("Attempting to insert EndOfTrack before an existing event. Use closeTrack() when finished with MidiTrack.")
            }
            closed = true
        }
    }

    fun removeEvent(E : MidiEvent): Boolean {

        var prev: MidiEvent? = null
        var cur: MidiEvent? = null
        var next: MidiEvent? = null

        for (ev in events) {
            next = ev
            if(E == cur) break

            prev = cur
            cur = next
            next =  null
        }

        if(next == null) {
            return events.remove(cur)
        }

        if(!events.remove(cur)) {
            return false
        }

        next.delta = next.tick - (prev?.tick ?: 0L)
        return true
    }

    fun closeTrack(){
        var lastTick = 0L
        if(!events.isEmpty()) {
            val last = events.last()
            lastTick = last.tick
        }
        val eot = EndOfTrack(lastTick + endOfTrackDelta, 0)
        insertEvent(eot)
    }

    fun dumpEvents(){
        events.forEach {
            info { "$it" }
        }
    }

    fun writeTofile(out : OutputStream){
        if(!closed) closeTrack()

        if(sizeNeedsRecalculating) recalculateSize()

        out.write(IDENTIFIER)
        out.write(intToBytes(size, 4))
        var last: MidiEvent? = null
        events.forEach {
            if(VERBOSE) {
                info { "Writing $it" }
            }
            it.writeToFile(out, it.requiresStatusByte(last))
            last = it
        }
    }
}