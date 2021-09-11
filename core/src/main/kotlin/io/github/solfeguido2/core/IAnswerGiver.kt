package io.github.solfeguido2.core

import io.github.solfeguido2.enums.NoteOrderEnum

interface IAnswerGiver {

    fun highlightAnswer(note: NoteOrderEnum)

}