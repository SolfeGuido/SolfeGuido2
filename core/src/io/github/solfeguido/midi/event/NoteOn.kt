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

package io.github.solfeguido.midi.event


class NoteOn : ChannelEvent {
    constructor(tick: Long, channel: Int, note: Int, velocity: Int) : super(tick, NOTE_ON, channel, note, velocity) {}
    constructor(tick: Long, delta: Long, channel: Int, note: Int, velocity: Int) : super(tick, delta, NOTE_ON, channel, note, velocity) {}

    var noteValue: Int
        get() = value1
        set(p) {
            value1 = p
        }

    var velocity: Int
        get() = value2
        set(v) {
            value2 = v
        }

}