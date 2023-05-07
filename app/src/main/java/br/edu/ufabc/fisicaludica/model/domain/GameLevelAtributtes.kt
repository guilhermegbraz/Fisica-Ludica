package br.edu.ufabc.fisicaludica.model.domain

class GameLevelAtributtes(
    val initialVelocity: Double,
    val isVelocityVariable: Boolean,

    val initialAngleDegrees: Double,
    val isAngleVariable: Boolean,
    val gravityX: Double,
    val gravityY: Double,
)