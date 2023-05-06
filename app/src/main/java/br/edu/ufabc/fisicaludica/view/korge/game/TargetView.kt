package br.edu.ufabc.fisicaludica.view.korge.game

import android.graphics.Matrix
import com.soywiz.korge.view.*
import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korim.color.Colors
import com.soywiz.korma.geom.Angle
import org.jbox2d.dynamics.BodyType

fun Container.targetView(width: Double, height: Double, posX: Number, posY: Number,
                         rotation: Int,targetImageBitmap: Bitmap)
                = TargetView(width, height, posX, posY, rotation, targetImageBitmap).addTo(this)
class TargetView(width: Double, height: Double, posX: Number, posY: Number,
                 rotation: Int, targetImageBitmap: Bitmap): Container() {

    val targetBody = roundRect(width, height, width / 50, width / 50, Colors.TRANSPARENT_WHITE)
    val type = BodyType.STATIC

    init {
        targetBody.position(posX.toDouble(), posY.toDouble())
        val targetImage = image(targetImageBitmap)
        targetBody.rotation =(Angle.Companion.fromDegrees(-rotation))
        targetImage.scale(
            targetBody.width / targetImage.width,
            targetBody.height / targetImage.height
        )

        targetBody.addChild(targetImage)
    }
}