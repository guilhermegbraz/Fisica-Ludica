package br.edu.ufabc.fisicaludica.view.korge.game

import br.edu.ufabc.fisicaludica.domain.Map
import com.soywiz.korge.view.*
import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korim.color.Colors
import org.jbox2d.dynamics.BodyType

fun Container.backgroundView(gameMap: Map, backgroundImageBitmap: Bitmap, width: Double, height: Double) = BackgroundView(gameMap, backgroundImageBitmap, width, height).addTo(this)
class BackgroundView(gameMap: Map, backgroundImageBitmap: Bitmap, width: Double, height: Double): Container() {
    val backgroundImage: Image
    val ground: SolidRect
    val groundType = BodyType.STATIC

    init {
        ground = solidRect(width, 2.0, Colors.TRANSPARENT_WHITE).
        position(0.0, gameMap.groundPosition * height)

        backgroundImage = image(backgroundImageBitmap)
        backgroundImage.scale(width / backgroundImage.width, height / backgroundImage.height)
    }
}