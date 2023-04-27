package br.edu.ufabc.fisicaludica.view.korge
import android.util.Log
import com.soywiz.korge.resources.resourceBitmap
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korio.resources.ResourcesContainer


class CustomScene() : Scene() {

    val ResourcesContainer.korge_png by resourceBitmap("urbancity.png")
    override suspend fun SContainer.sceneInit() {
        Log.d(" tamanho sceneContainer", "o tamanho do container eh ${this.width}, ${this.height}")
        val fundo = roundRect(this.width, this.height, 0.1, 0.1, Colors.GREEN).position(0, 0)
        val frente = roundRect(100.0, 100.0, 0.1, 0.1, Colors.PURPLE).position(this.width - 102, this.height - 102)


        /*
        val imagem = image(korge_png)
        Log.d("imagem", "o tamanho da imagem eh (${imagem.scaledHeight} , ${imagem.frameHeight})")
        imagem.scale(widthParam.toDouble()/1000.0, heightParam.toDouble() / 1000.0)
        Log.d("imagem", "o tamanho da imagem eh (${imagem.scaledHeight} , ${imagem.frameHeight})")
        fundo.addChild(imagem)
         */


    }


}
