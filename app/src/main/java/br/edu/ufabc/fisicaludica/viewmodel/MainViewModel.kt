package br.edu.ufabc.fisicaludica.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import br.edu.ufabc.fisicaludica.model.dataproviders.firestore.GameHintFirestoreRepository
import br.edu.ufabc.fisicaludica.model.dataproviders.firestore.GameLevelFirestoreRepository
import br.edu.ufabc.fisicaludica.model.domain.GameHint
import br.edu.ufabc.fisicaludica.model.domain.GameLevel
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
        MutableLiveData(true)
    }
    val currentFragmentWindow by lazy {
        MutableLiveData(FragmentWindow.HomeFragment)
    }

    val currentHintCollection by lazy {
        MutableLiveData<GameHint?>(null)
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


    private val gameLevelRepository: GameLevelFirestoreRepository
    private val gameHintRepository: GameHintFirestoreRepository
    val app: Application

    init {
        app = application
        gameLevelRepository = GameLevelFirestoreRepository(app)
        gameHintRepository = GameHintFirestoreRepository(app)
        //application.resources.assets.open("enunciados.json").use {
           // repositoryHint.loadData(it)
        //}
    }


    fun getGameLevelById(id: Long) = liveData {
        try {
            emit(Status.Loading)
            val gameLevel = gameLevelRepository.getGameLevelById(id)
            changeCurrentGameHint(gameLevel.id)
            emit(Status.Success(Result.SingleGameLevel(gameLevel)))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to get element by id", e)))
        }
    }

    private suspend fun changeCurrentGameHint(id: Long)  {
        try {
            val hint = gameHintRepository.getHintByGameLevelId(id)
            currentHintCollection.value = hint
        } catch (e: Exception) {
        }
    }

    fun getAllGameLevels() = liveData {
        try {
            emit(Status.Loading)
            emit(Status.Success(Result.GameLevelList(gameLevelRepository.getAll())))
        } catch (e: Exception) {
            Log.d("erro firestore", e.toString())
            emit(Status.Failure(Exception("Failed to get elements", e)))
        }
    }


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

}

enum class FragmentWindow {
    ListFragment,
    HomeFragment,
    InputGameWindow
}
