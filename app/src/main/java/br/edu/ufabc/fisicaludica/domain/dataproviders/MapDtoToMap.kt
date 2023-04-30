package br.edu.ufabc.fisicaludica.domain.dataproviders

import br.edu.ufabc.fisicaludica.domain.Map

/**
 * Parser to map.
 */
class MapDtoToMap {

    fun execute(mapDto: MapDto): Map {
        return Map(mapDto.id, mapDto.background, mapDto.title, mapDto.groundPosition, mapDto.posXLauncher,
            mapDto.posYLauncher, mapDto.posXTarget, mapDto.posYTarget, mapDto.initialVelocity,
            mapDto.isVelocityVariable, mapDto.initialAngleDegrees, mapDto.isAngleVariable, mapDto.widthInMeters,
            mapDto.isTargetHorizontal, mapDto.gravityX, mapDto.gravityY)
    }

}