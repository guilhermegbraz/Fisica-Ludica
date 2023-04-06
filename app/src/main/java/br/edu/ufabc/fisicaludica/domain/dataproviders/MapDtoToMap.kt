package br.edu.ufabc.fisicaludica.domain.dataproviders

import br.edu.ufabc.fisicaludica.domain.Map

class MapDtoToMap {

    fun execute(mapDto: MapDto): br.edu.ufabc.fisicaludica.domain.Map {
        return Map(mapDto.id, mapDto.background)
    }

    fun execute(mapDtos: List<MapDto>): List<Map> {
        val maps = mutableListOf<Map>()
        mapDtos.stream().map { maps.add(execute(it)) }
        return maps
    }
}