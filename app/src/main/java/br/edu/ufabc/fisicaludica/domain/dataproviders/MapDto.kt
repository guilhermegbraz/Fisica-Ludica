package br.edu.ufabc.fisicaludica.domain.dataproviders

/**
 * Dto class for parsing the game map.
 */
data class MapDto(
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
    val gravityY: Double)