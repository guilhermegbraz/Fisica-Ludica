package br.edu.ufabc.fisicaludica.domain.dataproviders

import android.util.Log
import br.edu.ufabc.fisicaludica.domain.Map
import com.beust.klaxon.Klaxon
import java.io.InputStream

class MapRepository (private val mapDtoToMapParser: MapDtoToMap){
    private lateinit var maps: List<Map>

    fun loadData(inputStream: InputStream) {
        maps = Klaxon().parseArray<MapDto>(inputStream)?.map { this.mapDtoToMapParser.execute(it) } ?: emptyList()

    }

    fun getAll(): List<Map> = if (this::maps.isInitialized) maps else {
        Log.d(
            "error",
            "No data has been fetched yet. On NoteRepository"
        )
        throw Exception("Didn't found any Notes")
    }

    fun getMapById(id: Long): Map {
        return maps.find { it.id == id }
            ?: throw IllegalArgumentException("Didn't found the map you're looking for").also {
                Log.d("error", "Didn't found map with id = $id")
            }
    }
}