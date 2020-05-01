package io.github.solfeguido.factories

import com.badlogic.gdx.scenes.scene2d.Actor


fun Actor.inside(x: Float, y : Float) = x in 0f..width && y in 0f..height