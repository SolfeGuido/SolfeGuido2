package io.github.solfeguido.core

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.utils.ObjectMap
import com.badlogic.gdx.utils.Timer
import io.github.solfeguido.Constants
import io.github.solfeguido.utils.NoteDataPool
import ktx.collections.gdxMapOf
import ktx.inject.Context


data class JingleNote(val after: Float = 0f,
                      val note: String = "",
                      val volume: Float = 1f
){

}

data class Jingle(
        val notes: List<JingleNote> = emptyList()
) {
}

class Jingles(
        val context: Context,
        private val jingles: ObjectMap<String, Jingle> = gdxMapOf<String, Jingle>()
) {

    val allJingles: Array<FileHandle> by lazy {
        Gdx.files.internal(Constants.JINGLES_PATH).list()
    }

    fun playJingle(name: String): Boolean {
        if(!jingles.containsKey(name)) return false
        val jingle = jingles[name]
        jingle.notes.forEach{
            Timer.schedule(object: Timer.Task() {
                override fun run() {
                    val pool: NoteDataPool = context.inject()
                    pool.withString(it.note){
                        note -> context.inject<SoundHelper>().playNote(note, it.volume)
                    }
                }
            }, it.after)
        }
        return true
    }

    fun registerJingles(assetManager: AssetManager) {
        allJingles.forEach {
            jingles.put(it.nameWithoutExtension(), assetManager.get(it.path()))
        }
    }

}