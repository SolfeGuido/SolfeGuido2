package io.github.solfeguido.enums

enum class GeneratorTypeEnum {
    Constant,// Always the same note (for testing)
    Random,// All the notes from min to max
    RangedRandom,// Notes between a min and a max
    CustomMidi,// From a midi imported by the user
    MidiFile// data from a midi file (shipped with the app)
}