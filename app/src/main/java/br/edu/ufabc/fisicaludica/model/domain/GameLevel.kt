package br.edu.ufabc.fisicaludica.model.domain


/**
 * domain class for game map.
 */
class GameLevel (
    id:Long,
    backgroudUrl: String,
    title: String,
    groundPosition: Double,
    widthMeters: Double,
    elementsPosition: GameLevelElementsPosition,
    worldAtributtes: GameLevelAtributtes,
    correctAnswer: GameLevelAnswer
    ) {

    companion object {
        const val targetH = 3.0
        const val projectileLauncherW = 4.0
        const val projectileW = 1.2
    }
    val id: Long
    val backgroudUrl: String
    val title: String
    val widthMeters: Double
    val groundPosition: Double

    val elementsPosition : GameLevelElementsPosition
    val worldAtributtes: GameLevelAtributtes
    val correctAnswer:GameLevelAnswer

    var isEnable: Boolean = false
    val targetHeight = targetH
    val projectileLauncherWidht = projectileLauncherW
    val projectileWidth = projectileW

    init {
        this.id = id
        this.backgroudUrl = backgroudUrl
        this.title = title
        this.groundPosition = groundPosition
        this.elementsPosition = elementsPosition
        this.widthMeters = widthMeters
        this.worldAtributtes = worldAtributtes
        this.correctAnswer = correctAnswer
    }
}
