package io.github.solfeguido.utils

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.utils.Array
import io.github.solfeguido.midi.MidiFile
import ktx.collections.gdxArrayOf

class MidiLoader(fileHandleResolver: FileHandleResolver)  : AsynchronousAssetLoader<MidiFile, MidiLoader.MidiLoaderParameter>(fileHandleResolver){

    private var midiFile : MidiFile? = null

    class MidiLoaderParameter() : AssetLoaderParameters<MidiFile>()

    override fun loadSync(manager: AssetManager?, fileName: String?, file: FileHandle?, parameter: MidiLoaderParameter?): MidiFile {
        midiFile = MidiFile(file!!.file())
        return midiFile!!
    }

    override fun getDependencies(fileName: String?, file: FileHandle?, parameter: MidiLoaderParameter?): Array<AssetDescriptor<Any>> {
        return gdxArrayOf()
    }

    override fun loadAsync(manager: AssetManager?, fileName: String?, file: FileHandle?, parameter: MidiLoaderParameter?) {
        midiFile = MidiFile(file!!.file())
    }
}