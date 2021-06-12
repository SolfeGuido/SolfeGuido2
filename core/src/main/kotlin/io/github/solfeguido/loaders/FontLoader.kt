package io.github.solfeguido.loaders

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.utils.Array
import ktx.collections.gdxArrayOf


class FontLoader(resolver: FileHandleResolver): AsynchronousAssetLoader<BitmapFont, FontLoader.FontLoaderParameter>(resolver) {

    private var font: BitmapFont? = null

    class FontLoaderParameter() : AssetLoaderParameters<BitmapFont>() {
        var fontFileName = ""

        var fontParameters: FreeTypeFontGenerator.FreeTypeFontParameter = FreeTypeFontGenerator.FreeTypeFontParameter()
    }

    override fun loadSync(manager: AssetManager?, fileName: String?, file: FileHandle?, parameter: FontLoaderParameter?): BitmapFont {
        if(parameter == null) throw RuntimeException("FreetypeFontParameter must be set in AssetManager#load to point at a TTF file!")
        val generator = FreeTypeFontGenerator(resolve(parameter.fontFileName))
        font = generator.generateFont(parameter.fontParameters) // font size 12 pixels
        generator.dispose() // don't forget to dispose to avoid memory leaks!
        return font!!
    }

    override fun getDependencies(fileName: String?, file: FileHandle?, parameter: FontLoaderParameter?): Array<AssetDescriptor<Any>> {
        return gdxArrayOf()
    }

    override fun loadAsync(manager: AssetManager?, fileName: String?, file: FileHandle?, parameter: FontLoaderParameter?) {
        if(parameter == null) throw RuntimeException("FreetypeFontParameter must be set in AssetManager#load to point at a TTF file!")
    }


}