package br.edu.ufabc.fisicaludica.model.dataproviders.inMemory

import android.util.Log
import br.edu.ufabc.fisicaludica.model.domain.GameLevel
import br.edu.ufabc.fisicaludica.model.domain.GameLevelRepository
import com.beust.klaxon.Klaxon
import com.beust.klaxon.KlaxonException
import java.io.InputStream

/**
 * repository for maps.
 */
class GameLevelJsonRepository (private val gameLevelDtoToGameLevelParser: GameLevelDtoToGameLevel): GameLevelRepository{
    private lateinit var gameLevels: List<GameLevel>

    /**
     * load and parse the data.
     */
    fun loadData(inputStream: InputStream) {
        try {
            gameLevels = Klaxon().parseArray<GameLevelDTO>(inputStream)?.map { this.gameLevelDtoToGameLevelParser.execute(it) } ?: emptyList()
        } catch (exception: KlaxonException) {
            Log.d("klaxon", "Failed to parse json file")
        }
    }

    /**
     * get all the maps.
     */
    override suspend fun getAll(): List<GameLevel> = if (this::gameLevels.isInitialized) gameLevels else {
        Log.d(
            "error",
            "No data has been fetched yet. On GameLevelRepository"
        )
        throw Exception("Didn't found any data for game levels")
    }

    /**
     * get map by id.
     */
    override suspend fun getGameLevelById(id: Long): GameLevel {
        return gameLevels.find { it.id == id }
            ?: throw IllegalArgumentException("Didn't found the map you're looking for").also {
                Log.d("error", "Didn't found gameLevel with id = $id")
            }
    }
}