package br.edu.ufabc.fisicaludica.domain.dataproviders

import br.edu.ufabc.fisicaludica.domain.Map

/**
 * Parser to map.
 */
class MapDtoToMap {

    fun execute(mapDto: MapDto): Map {
        return Map(mapDto.id, mapDto.background, mapDto.title)
    }

}