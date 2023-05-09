package br.edu.ufabc.fisicaludica.model.domain

class GameLevelAtributtes(
    var initialVelocity: Double,
    val isVelocityVariable: Boolean,
    var initialAngleDegrees: Double,
    val isAngleVariable: Boolean,
    val gravityX: Double,
    val gravityY: Double,
)