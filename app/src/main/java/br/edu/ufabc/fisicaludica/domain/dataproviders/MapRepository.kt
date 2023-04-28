package br.edu.ufabc.fisicaludica.domain.dataproviders

import android.util.Log
import br.edu.ufabc.fisicaludica.domain.Map
import com.beust.klaxon.Klaxon
import com.beust.klaxon.KlaxonException
import java.io.InputStream

/**
 * repository for maps.
 */
class MapRepository (private val mapDtoToMapParser: MapDtoToMap){
    private lateinit var maps: List<Map>

    /**
     * load and parse the data.
     */
    fun loadData(inputStream: InputStream) {
        try {
            maps = Klaxon().parseArray<MapDto>(inputStream)?.map { this.mapDtoToMapParser.execute(it) } ?: emptyList()
        } catch (exception: KlaxonException) {
            Log.d("klaxon", "Failed to parse json file")
        }
    }

    /**
     * get all the maps.
     */
    fun getAll(): List<Map> = if (this::maps.isInitialized) maps else {
        Log.d(
            "error",
            "No data has been fetched yet. On NoteRepository"
        )
        throw Exception("Didn't found any Notes")
    }

    /**
     * get map by id.
     */
    fun getMapById(id: Long): Map {
        return maps.find { it.id == id }
            ?: throw IllegalArgumentException("Didn't found the map you're looking for").also {
                Log.d("error", "Didn't found map with id = $id")
            }
    }
}