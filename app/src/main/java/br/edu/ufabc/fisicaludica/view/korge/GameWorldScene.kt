package br.edu.ufabc.fisicaludica.view.korge

import br.edu.ufabc.fisicaludica.model.domain.GameGuess
import br.edu.ufabc.fisicaludica.model.domain.GameLevel
import br.edu.ufabc.fisicaludica.model.domain.GameLevelAnswer
import br.edu.ufabc.fisicaludica.view.korge.game.*
import com.soywiz.klock.TimeSpan
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.scene.delay
import com.soywiz.korge.view.*
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.async.launch
import com.soywiz.korio.file.std.resourcesVfs



class GameWorldScene(val gameMap : GameLevel, private val answer: GameLevelAnswer) : Scene() {


    override suspend fun SContainer.sceneInit() {
        val projectileBitmap = resourcesVfs["maca.png"].readBitmap()
        val launcherBitmap = resourcesVfs["canhao_80px.png"].readBitmap()
        val backgroundBitmap = resourcesVfs[gameMap.backgroudUrl].readBitmap()
        val targetBitmap = resourcesVfs["alvo_circular_deitado.png"].readBitmap()


        val gameWindow = gameWindow( this.width, this.height, gameMap, projectileBitmap,
            launcherBitmap, backgroundBitmap, targetBitmap)


        launch {
            delay(TimeSpan(2000.0))
            gameWindow.launchProjectile(answer.velocity.toFloat(), answer.angle.toFloat())
        }



    }




}
