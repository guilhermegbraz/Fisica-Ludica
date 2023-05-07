package br.edu.ufabc.fisicaludica.model.dataproviders

/**
 * Dto class for parsing the game map.
 */
data class GameLevelDTO(
    val id: Long,
    val background: String,
    val title: String,
    val widthInMeters: Double,
    val groundPosition: Double,
    val launcherPositionX: Double,
    val launcherPositionY: Double,
    val targetPositionX: Double,
    val targetPositionY: Double,
    val initialVelocity: Double,
    val isVelocityVariable: Boolean,
    val initialAngleDegrees: Double,
    val isAngleVariable: Boolean,
    val targetRotation: Int,
    val gravityX: Double,
    val gravityY: Double) {

    fun toFirestore(): GameLevelFirestoreRepository.GameLevelFirestore {
        return GameLevelFirestoreRepository.GameLevelFirestore(id, background, gravityX, gravityY, groundPosition
        , initialAngleDegrees, initialVelocity, isAngleVariable, isVelocityVariable, launcherPositionX,
            launcherPositionY, targetPositionX, targetPositionY, targetRotation, title, widthInMeters
        )
    }
}