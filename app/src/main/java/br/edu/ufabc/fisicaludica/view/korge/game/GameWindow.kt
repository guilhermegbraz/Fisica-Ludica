package br.edu.ufabc.fisicaludica.view.korge.game


import br.edu.ufabc.fisicaludica.domain.Map
import com.soywiz.korge.box2d.nearestBox2dWorld
import com.soywiz.korge.box2d.registerBodyWithFixture
import com.soywiz.korge.box2d.worldView
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


class GameWindow(val gameMap: Map, projectileBitmap: Bitmap, launcherBitmap: Bitmap,
                 backgroundBitmap: Bitmap, val gameWindowWidth:Double, val gameWindowHeight: Double): Container() {

     var background: BackgroundView
     var projectile: Projectile
     var projectileLauncher: ProjectileLauncherView
    lateinit var gravity: Vec2
    val worldScale: Double

    init {
        this.worldScale = gameWindowWidth / gameMap.widthMeters
        background = backgroundView(gameMap, backgroundBitmap, gameWindowWidth, gameWindowHeight)

        projectile = projectile(gameWindowWidth / 40, gameWindowWidth / 40,
            coordMetersToPixelX(gameMap.posXLauncher), coordMetersToPixelY(gameMap.posYLauncher) -
                    gameWindowWidth / 40 - 4, projectileBitmap)

        projectileLauncher = projectileLauncher(gameWindowWidth / 9, gameWindowWidth / 9,
            this.coordMetersToPixelX(gameMap.posXLauncher) - 10, coordMetersToPixelY(gameMap.posYLauncher) -
                    gameWindowWidth / 9 - 1, launcherBitmap)
        worldView {
            nearestBox2dWorld.world.customScale = worldScale
            background.ground.registerBodyWithFixture(type= background.groundType)
            projectile.projetilObject.registerBodyWithFixture(type = projectile.type, friction = 3, density = 1)

        }
    }

    fun launchProjectile(initialVelocity: Float, initialAngleDegres: Float) {
        val angle = Angle.Companion.fromDegrees(initialAngleDegres)
        val velocity = Vec2(cos(angle).toFloat().times(initialVelocity), - sin(angle).toFloat().times(initialVelocity))
        this.projectile.launch(velocity)
    }


    private fun coordMetersToPixelX(positionXMeters: Double): Double = this.worldScale * positionXMeters

    private fun coordMetersToPixelY(positionYMeters: Double): Double =
        this.gameWindowHeight * this.gameMap.groundPosition - positionYMeters * this.worldScale

    private fun velocityMetersToPixel(velocityInMeters: Double): Double = velocityInMeters * 4.9728
}