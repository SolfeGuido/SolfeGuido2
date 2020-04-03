package io.github.solfeguido.scenes

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.utils.Align
import io.github.solfeguido.Constants
import io.github.solfeguido.ui.UIScreen
import io.github.solfeguido.ui.iconButton
import io.github.solfeguido.enums.IconName
import io.github.solfeguido.ui.measure
import ktx.actors.onClick
import ktx.actors.plusAssign
import ktx.inject.Context
import ktx.log.info
import ktx.scene2d.label
import ktx.scene2d.table

class MenuScreen(context: Context) : UIScreen(context) {


    override fun show() {
        super.show()
        info { "Init stage" }

        stage += table {
            debug = true
            setFillParent(true)
            setPosition(0f, 0f)
            align(Align.center)
            slidingTable(Align.top) {
                debug = true

                    iconButton(IconName.Info) {
                        onClick {
                            //Slide to other state
                            context.inject<AssetManager>().get<Sound>(Constants.CLICK_SOUND).play()
                        }
                        pad(5f)
                    }.inCell.expandX().top().left()

                 label("SolfeGuido") {
                }

                iconButton(IconName.Cog) {
                    label.setAlignment(Align.topRight)
                    onClick {
                        setOrigin(Align.center)
                        this.addAction(Actions.rotateBy(360f, 0.3f))
                        info { "Show options" }
                    }
                    pad(5f)
                }.inCell.expandX().top().right()
                pad(10f)
                inCell.expandX().fillX()
            }
            row()
            val padding = 0f
            measure {
            }.inCell.grow().pad(padding, 0f, padding, 0f)
            row()
            slidingTable(Align.bottomLeft) {
                debug = true
                iconButton(IconName.Off){
                    label.setAlignment(Align.topRight)
                    onClick {
                        Gdx.app.exit()
                    }
                    pad(5f)
                }.inCell.expandX().bottom().left()
                pad(10f)
                inCell.expandX().fillX()
            }
        }
    }
}