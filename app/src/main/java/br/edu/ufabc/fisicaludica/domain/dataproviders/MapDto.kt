package br.edu.ufabc.fisicaludica.domain.dataproviders

/**
 * Dto class for parsing the game map.
 */
data class MapDto(val id: Long,
                  val background: String,
                  val title: String,
                  val widthInMeters: Double,
                  val groundPosition: Double,
                  val posXLauncher: Double,
                  val posYLauncher: Double,
                  val posXTarget: Double,
                  val posYTarget: Double,
                  val initialVelocity: Double,
                  val isVelocityVariable: Boolean,
                  val initialAngleDegrees: Double,
                  val isAngleVariable: Boolean)