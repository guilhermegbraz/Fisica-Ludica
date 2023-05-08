package br.edu.ufabc.fisicaludica.model.dataproviders.inMemory

import br.edu.ufabc.fisicaludica.model.domain.GameLevel
import br.edu.ufabc.fisicaludica.model.domain.GameLevelAtributtes
import br.edu.ufabc.fisicaludica.model.domain.GameLevelElementsPosition

/**
 * Parser to map.
 */
class GameLevelDtoToGameLevel {

    fun execute(gameLevelDTO: GameLevelDTO): GameLevel {
        return GameLevel(gameLevelDTO.id, gameLevelDTO.background, gameLevelDTO.title, gameLevelDTO.groundPosition,
            gameLevelDTO.widthInMeters, createGameLevelElementsPosition(gameLevelDTO),
            createGameLevelAtributes(gameLevelDTO)
           )
    }
    fun createGameLevelAtributes(gameLevelDTO: GameLevelDTO): GameLevelAtributtes {
        return GameLevelAtributtes(gameLevelDTO.initialVelocity, gameLevelDTO.isVelocityVariable,
            gameLevelDTO.initialAngleDegrees, gameLevelDTO.isAngleVariable,
            gameLevelDTO.gravityX, gameLevelDTO.gravityY)
    }

    fun createGameLevelElementsPosition(gameLevelDTO: GameLevelDTO): GameLevelElementsPosition {
        return GameLevelElementsPosition(gameLevelDTO.launcherPositionX, gameLevelDTO.launcherPositionY,
            gameLevelDTO.targetPositionX, gameLevelDTO.targetPositionY, gameLevelDTO.targetRotation)
    }

}