package io.github.solfeguido.structures

import com.badlogic.gdx.Gdx

object Constants {

    val WIDTH
        get() = Gdx.graphics.width
    val HEIGHT
        get() = Gdx.graphics.height

    const val TITLE_SIZE = 50

    const val JINGLES_PATH = "midi/jingles"
    const val SOUNDS_PATH = "sounds"
    const val FONTS_PATH = "fonts"
    const val PARTICLES_PATH = "particles"
    const val SHADERS_PATH = "shaders"
    const val IMAGES_PATH = "images"

    // Actual values
    const val MUSICALNOTES_PATH = "$SOUNDS_PATH/notes"
    const val CLICK_SOUND = "$SOUNDS_PATH/click.mp3"

    //Fonts
    const val ICONS_PATH = "$FONTS_PATH/Icons.woff"
    const val TITLE_FONT = "$FONTS_PATH/MarckScript.woff"
    const val PRIMARY_FONT = "$FONTS_PATH/Oswald.woff"
    const val LINE_THICKNESS = 2f

    // Animations
    const val PRESSED_SCALING = 0.8f
    const val PIANO_PRESSED_SCALING = 0.9f
    const val FADEOUT_DURATION = 3f

    // Others
    const val CLASSIC_TIME = 60f
    const val MAX_NOTE_SPAN = 40
    const val ASYNC_THREADS = 2

    // Preferences
    const val PREFERENCES_NAME = "solfeguido2"
    object Preferences {
        const val LEVELS = "level"
        const val BEST_SCORES = "stats"
        const val WRONGE_NOTES = "errors"
        const val SETTINGS = "settings"
    }
}