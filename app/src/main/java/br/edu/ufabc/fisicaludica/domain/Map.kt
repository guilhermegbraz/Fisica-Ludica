package br.edu.ufabc.fisicaludica.domain


/**
 * domain class for game map.
 */
class Map (id:Long, backgroud: String, title: String, groundPosition: Double, laucherPositionX:Double,
           laucherPositionY: Double, targetPositionX: Double, targetPositionY: Double, linearVelocity: Double,
           isVelocityVariable: Boolean, rotationAngle: Double, isAngleVariable: Boolean,
           widthMeters: Double,
           isTargetHorizontal: Boolean,
           gravityX: Double,
           gravityY: Double) {
     var id: Long
        private set
      var backgroud: String
        private set
    var title: String
        private set
    val widthMeters: Double

    val groundPosition: Double
    val posXLauncher: Double
    val posYLauncher: Double

    val posXTarget: Double
    val posYTarget: Double

    val initialVelocity: Double
    val isVelocityVariable: Boolean

    val initialAngleDegrees: Double
    val isAngleVariable: Boolean

    val targetHeight = 4.5
    val projectileLauncherWidht = 4.5
    val projectileWidth = 1.2

    val isTargetHorizontal: Boolean
    val gravityX: Double
    val gravityY: Double

    init {
        this.id = id
        this.backgroud = backgroud
        this.title = title
        this.groundPosition = groundPosition
        posXLauncher = laucherPositionX
        posYLauncher = laucherPositionY
        posXTarget = targetPositionX
        posYTarget = targetPositionY
        initialAngleDegrees = rotationAngle;
        this.isAngleVariable = isAngleVariable
        initialVelocity = linearVelocity
        this.isVelocityVariable = isVelocityVariable
        this.widthMeters = widthMeters
        this.isTargetHorizontal = true
        this.gravityX = gravityX
        this.gravityY = gravityY
    }
}
