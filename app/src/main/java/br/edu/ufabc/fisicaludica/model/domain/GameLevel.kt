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
    var id: Long
        private set
      var backgroudUrl: String
        private set
    var title: String
        private set
    val widthMeters: Double
    val groundPosition: Double
    val elementsPosition : GameLevelElementsPosition
    val worldAtributtes: GameLevelAtributtes
    val correctAnswer:GameLevelAnswer

    val targetHeight = 3.0
    val projectileLauncherWidht = 4.0
    val projectileWidth = 1.2




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
