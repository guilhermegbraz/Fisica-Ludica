package br.edu.ufabc.fisicaludica.view.korge


import br.edu.ufabc.fisicaludica.model.domain.GameGuess
import br.edu.ufabc.fisicaludica.model.domain.GameLevel
import com.soywiz.korge.scene.Module
import com.soywiz.korge.scene.Scene
import com.soywiz.korinject.AsyncInjector
import com.soywiz.korma.geom.SizeInt
import kotlin.reflect.KClass

@Suppress("unused")
class CustomModule (
    private val width: Int = DEFAULT_WIDTH,
    private val height: Int = DEFAULT_HEIGHT,
    private val gameLevel: GameLevel,
    private val gameGuess: GameGuess,
    val callback: () -> Unit,
    )
    : Module() {

    companion object {
        const val DEFAULT_WIDTH = 640
        const val DEFAULT_HEIGHT = 480
    }

    override val size: SizeInt
        get() = SizeInt.invoke(width, height)

    override val windowSize: SizeInt
        get() = SizeInt.invoke(width, height)

    override val mainScene: KClass<out Scene> = GameWorldScene::class

    override suspend fun AsyncInjector.configure() {
        mapPrototype { GameWorldScene(gameLevel, gameGuess) }
    }
}