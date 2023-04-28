package br.edu.ufabc.fisicaludica.view.korge

import br.edu.ufabc.fisicaludica.domain.GameGuess
import br.edu.ufabc.fisicaludica.domain.Map
import br.edu.ufabc.fisicaludica.view.korge.game.*
import com.soywiz.klock.TimeSpan
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.scene.delay
import com.soywiz.korge.view.*
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.async.launch
import com.soywiz.korio.file.std.resourcesVfs



class GameWorldScene(val gameMap : Map, private val gameGuess: GameGuess) : Scene() {


    override suspend fun SContainer.sceneInit() {
        val projectileBitmap = resourcesVfs["maca.png"].readBitmap()
        val launcherBitmap = resourcesVfs["canhao_80px.png"].readBitmap()
        val backgroundBitmap = resourcesVfs[gameMap.backgroud].readBitmap()

        val gameWindow = gameWindow(gameMap, projectileBitmap, launcherBitmap, backgroundBitmap, this.width, this.height)


        launch {
            delay(TimeSpan(2000.0))
            gameWindow.launchProjectile(gameGuess.initialVelocity.toFloat(), gameGuess.initialAngle.toFloat())
        }



    }




}
