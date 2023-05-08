package br.edu.ufabc.fisicaludica.view.korge.game


import br.edu.ufabc.fisicaludica.model.domain.GameLevel
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

fun Container.gameWindow(gameWindowWidth:Double, gameWindowHeight: Double, gameMap: GameLevel,
                         projectileBitmap: Bitmap, launcherBitmap: Bitmap, backgroundBitmap: Bitmap, targetImageBitmap: Bitmap)
= GameWindow( gameWindowWidth, gameWindowHeight, gameMap, projectileBitmap, launcherBitmap, backgroundBitmap, targetImageBitmap)
    .addTo(this)


class GameWindow(private val gameWindowWidth:Double, private val gameWindowHeight: Double, val gameMap: GameLevel, projectileBitmap: Bitmap, launcherBitmap: Bitmap,
                 backgroundBitmap: Bitmap, targetImageBitmap: Bitmap ): Container() {

    private var background: BackgroundView
    private var projectile: Projectile
    private var projectileLauncher: ProjectileLauncherView
    private var target: TargetView
    private var gravity: Vec2
    private val worldScale: Double

    init {
        this.gravity = Vec2(gameMap.worldAtributtes.gravityX.toFloat(), gameMap.worldAtributtes.gravityY.toFloat())
        this.worldScale = gameWindowWidth / gameMap.widthMeters
        background = backgroundView(gameMap, backgroundBitmap, gameWindowWidth, gameWindowHeight)

        projectile = projectile(
            coordMetersToPixelX(gameMap.projectileWidth),
            coordMetersToPixelX(gameMap.projectileWidth),
            coordMetersToPixelX(gameMap.elementsPosition.posXLauncher),
            coordMetersToPixelY(gameMap.elementsPosition.posYLauncher) - coordMetersToPixelX(gameMap.projectileWidth),
            projectileBitmap)

        projectileLauncher = projectileLauncher(coordMetersToPixelX(gameMap.projectileLauncherWidht),
            coordMetersToPixelX(gameMap.projectileLauncherWidht) ,
            this.coordMetersToPixelX(gameMap.elementsPosition.posXLauncher - gameMap.projectileLauncherWidht/8),
            coordMetersToPixelY(gameMap.elementsPosition.posYLauncher) - coordMetersToPixelX(gameMap.projectileLauncherWidht),
            launcherBitmap)

        target = targetView(
            coordMetersToPixelX(gameMap.targetHeight),
            coordMetersToPixelX(gameMap.targetHeight/5.5),
            coordMetersToPixelX(gameMap.elementsPosition.posXTarget - gameMap.targetHeight/1.5),
            coordMetersToPixelY(gameMap.elementsPosition.posYTarget + gameMap.targetHeight/5.5 + gameMap.projectileWidth/3),
            gameMap.elementsPosition.targetRotation, targetImageBitmap)

        createWorldAndRegisterBodies()
    }

    private fun createWorldAndRegisterBodies() {
        worldView {
            nearestBox2dWorld.world.customScale = worldScale
            nearestBox2dWorld.world.gravity = gravity
            background.ground.registerBodyWithFixture(type= background.groundType, friction = 5)
            projectile.projetilObject.registerBodyWithFixture(type = projectile.type, friction = .02, density = 0.8)
            target.targetBody.registerBodyWithFixture(type = target.type, friction = 10)
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

}