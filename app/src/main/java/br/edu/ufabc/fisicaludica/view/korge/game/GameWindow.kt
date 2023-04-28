package br.edu.ufabc.fisicaludica.view.korge.game


import br.edu.ufabc.fisicaludica.domain.Map
import com.soywiz.korge.box2d.registerBodyWithFixture
import com.soywiz.korge.box2d.worldView
import com.soywiz.korge.input.onClick
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.addTo
import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korma.geom.Angle
import com.soywiz.korma.geom.cos
import com.soywiz.korma.geom.sin
import org.jbox2d.common.Vec2

fun Container.gameWindow(gameMap: Map, projectileBitmap: Bitmap, launcherBitmap: Bitmap,
                         backgroundBitmap: Bitmap,  gameWindowWidth:Double,  gameWindowHeight: Double)
= GameWindow(gameMap, projectileBitmap, launcherBitmap, backgroundBitmap, gameWindowWidth, gameWindowHeight)
    .addTo(this)


class GameWindow(gameMap: Map, projectileBitmap: Bitmap, launcherBitmap: Bitmap,
                 backgroundBitmap: Bitmap, val gameWindowWidth:Double, val gameWindowHeight: Double): Container() {

     var background: BackgroundView
     var projectile: Projectile
     var projectileLauncher: ProjectileLauncherView
    lateinit var gravity: Vec2
    val worldScale: Double = 9.8

    init {
            background = backgroundView(gameMap, backgroundBitmap, gameWindowWidth, gameWindowHeight)

            projectileLauncher = projectileLauncher(gameWindowWidth / 9, gameWindowWidth / 9,
            100, gameMap.groundPosition * gameWindowHeight - gameWindowWidth / 9 + 4, launcherBitmap)

            projectile = projectile(gameWindowWidth / 40, gameWindowWidth / 40,
                gameWindowWidth / 2.0, gameWindowHeight / 2.0, projectileBitmap)

            background.ground.registerBodyWithFixture(type= background.groundType)
            projectile.projetilObject.registerBodyWithFixture(type = projectile.type)

        projectile.projetilObject.onClick {
            projectile.launch(Vec2(5.0F, 100.0F))
        }
    }

    fun launchProjectile(initialVelocity: Float, initialAngleDegres: Float) {
        val angle = Angle.Companion.fromDegrees(initialAngleDegres)
        val velocity = Vec2(cos(angle).toFloat().times(initialVelocity), - sin(angle).toFloat().times(initialVelocity))
        this.projectile.launch(velocity)
    }
}