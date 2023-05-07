package br.edu.ufabc.fisicaludica.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import br.edu.ufabc.fisicaludica.model.dataproviders.GameLevelFirestoreRepository
import br.edu.ufabc.fisicaludica.model.domain.GameLevel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.FileNotFoundException
import java.io.InputStream

/**
 * Main viewModel.
 */
class MainViewModel( application: Application) : AndroidViewModel(application) {

    /**
     * id of the clicked map
     */
    val clickedMapId by lazy {
        MutableLiveData<Long?>()
    }

    val isAppBarVisible by lazy {
        MutableLiveData<Boolean>(true)
    }

    var fragmentResolutionWidth: Int? = null
    var fragmentResolutionHeight: Int? = null

    /**
     * Status hierarchy.
     */
    sealed class Status {
        /**
         * The error status.
         * @property e the exception
         */
        class Failure(val e: Exception) : Status()

        /**
         * The success status.
         * @property result the result
         */
        class Success(val result: Result) : Status()

        /**
         * The loading status.
         */
        object Loading : Status()
    }

    /**
     * The result hierarchy.
     */
    sealed class Result {
        /**
         * Result type that holds a list of tasks.
         * @property value the list of tasks
         */
        data class GameLevelList(
            val value: List<GameLevel>
        ) : Result()

        /**
         * Result type that holds a single task.
         * @property value the task
         */
        data class SingleGameLevel(
            val value: GameLevel
        ) : Result()

    }

    private val repository = GameLevelFirestoreRepository()
    val app: Application

    init {
        app = application
        /*application.resources.assets.open("maps.json").use {
            repository.loadData(it)
        }*/
    }


    fun getMapById(id: Long) = liveData {
        try {
            emit(Status.Loading)
            emit(Status.Success(Result.SingleGameLevel(repository.getGameLevelById(id))))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to get element by id", e)))
        }
    }

    fun getAllMaps() = liveData {
        try {
            emit(Status.Loading)
            emit(Status.Success(Result.GameLevelList(repository.getAll())))
        } catch (e: Exception) {
            Log.d("erro firestore", e.toString())
            emit(Status.Failure(Exception("Failed to get elements", e)))
        }
    }

    /**
     * get all the game maps.
     */
    //fun getAllMaps() = gameLevelJsonRepository.getAll()

    /**
     * open inputStream for the map image.
     */
    fun getMapBackgroundInputStream(gameLevel: GameLevel): InputStream {
        try {
            return app.resources.assets.open(gameLevel.backgroudUrl)
        } catch (e: FileNotFoundException) {
            Log.d("FNF", "Não encontrei o arquivo ${gameLevel.backgroudUrl}")
            throw e
        }
    }

    /**
     * get a map by id.
     */
    //fun getMapById(id: Long): GameLevel = gameLevelJsonRepository.getGameLevelById(id)
}
