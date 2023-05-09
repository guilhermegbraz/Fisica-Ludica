package br.edu.ufabc.fisicaludica.model.dataproviders.firestore

import android.util.Log
import br.edu.ufabc.fisicaludica.model.domain.GameLevel
import br.edu.ufabc.fisicaludica.model.domain.GameLevelAnswer
import br.edu.ufabc.fisicaludica.model.domain.GameLevelAtributtes
import br.edu.ufabc.fisicaludica.model.domain.GameLevelElementsPosition

data class GameLevelFirestore(
    val gameLevelId: Long? = null,
    val backgroundUrl: String? = null,
    val correctAngle: Double? = null,
    val correctVelocity: Double? = null,
    val gravityX: Double? = null,
    val gravityY: Double? = null,
    val groundPosition: Double? = null,
    val initialAngleDegrees: Double? = null,
    val initialVelocity: Double? = null,
    val angleVariable: Boolean? = null,
    val velocityVariable: Boolean? = null,
    val launcherPositionX : Double? = null,
    val launcherPositionY : Double? = null,
    val targetPositionX : Double? = null,
    val targetPositionY : Double? = null,
    val targetRotation : Int? = null,
    val title: String? = null,
    val widthInMeters: Double? = null,
) {
    fun toGameLevel(): GameLevel  {
        return GameLevel(
            id= gameLevelId?:0,
            title= this.title?:"",
            groundPosition = this.groundPosition?: 0.0,
            widthMeters = this.widthInMeters?:0.0,
            elementsPosition = createGameLevelElementsPosition(),
            worldAtributtes = createGameLevelAtributes(),
            backgroudUrl = this.backgroundUrl?: "",
            correctAnswer = createGameLevelAnswer()
        )


    }

    fun createGameLevelAtributes(): GameLevelAtributtes {
        val isAngleVariable = this.angleVariable == true
        val isVelocityVariable = this.velocityVariable == true
        return GameLevelAtributtes(initialVelocity= initialVelocity?: 0.0,
            isVelocityVariable= isVelocityVariable,
            initialAngleDegrees= this.initialAngleDegrees?: 0.0,
            isAngleVariable= isAngleVariable,
            gravityX= this.gravityX?: 0.0,
            gravityY= this.gravityY?: 9.8)
    }

    fun createGameLevelElementsPosition(): GameLevelElementsPosition =
        GameLevelElementsPosition(
            this.launcherPositionX?: 5.0, this.launcherPositionY?: 0.0,
            this.targetPositionX?: 20.0,
            this.targetPositionY?: 0.0,
            this.targetRotation?: 0)
    fun createGameLevelAnswer(): GameLevelAnswer =
        GameLevelAnswer(correctAngle?: 0.0, correctVelocity?: 0.0)

}
