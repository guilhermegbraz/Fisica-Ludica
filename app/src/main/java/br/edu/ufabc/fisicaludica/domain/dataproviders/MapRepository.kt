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
}