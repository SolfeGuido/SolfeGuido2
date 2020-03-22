package io.github.solfeguido.utils

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Json
import io.github.solfeguido.core.Jingle
import ktx.json.fromJson

class JingleLoader(fileHandleResolver: FileHandleResolver) : AsynchronousAssetLoader<Jingle, JingleLoader.JingleParameter>(fileHandleResolver) {

    private var jingle: Jingle? = null

    class JingleParameter(public val json: Json) : AssetLoaderParameters<Jingle>() {}

    override fun loadSync(manager: AssetManager?, fileName: String?, file: FileHandle?, parameter: JingleParameter?): Jingle {
        val jsn = parameter?.json ?: Json()
        jingle = jsn.fromJson<Jingle>(file!!)
        return jingle!!
    }

    override fun getDependencies(fileName: String?, file: FileHandle?, parameter: JingleParameter?): Array<AssetDescriptor<Any>>? {
        return null
    }

    override fun loadAsync(manager: AssetManager?, fileName: String?, file: FileHandle?, parameter: JingleParameter?) {
        val jsn = parameter?.json ?: Json()
        jingle = jsn.fromJson<Jingle>(file!!)
    }
}