package br.edu.ufabc.fisicaludica.view.korge.game

import com.soywiz.klock.TimeSpan
import com.soywiz.korge.box2d.body
import com.soywiz.korge.view.*
import com.soywiz.korge.view.tween.rotateTo
import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korim.color.Colors
import com.soywiz.korma.geom.Angle
import org.jbox2d.common.Vec2
import org.jbox2d.dynamics.BodyType

fun Container.projectileLauncher(width: Double, height: Double, posX: Number, posY: Number, launcherBitmap: Bitmap)
        = ProjectileLauncherView(width, height, posX, posY, launcherBitmap).addTo(this)
class ProjectileLauncherView(width: Double, height: Double, posX: Number, posY: Number, launcherBitmap: Bitmap): Container() {

    val launcherObject: RoundRect
    val type = BodyType.STATIC

    init {
        launcherObject = roundRect(width, height,1.0, 1.0, Colors.TRANSPARENT_WHITE)
            .position(posX.toDouble(), posY.toDouble())
        val launcherImage = image(launcherBitmap)
        launcherImage.scale(launcherObject.width / launcherImage.width,
            launcherObject.height / launcherImage.height)
        launcherObject.addChild(launcherImage)
    }

    suspend fun rotate(angleDegress: Number) {
        this.launcherObject.rotateTo(
            Angle.Companion.fromDegrees(angleDegress.toFloat()),
            TimeSpan(500.0)
        )
    }

}