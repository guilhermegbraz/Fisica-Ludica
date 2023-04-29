package br.edu.ufabc.fisicaludica.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import br.edu.ufabc.fisicaludica.domain.Map
import br.edu.ufabc.fisicaludica.domain.dataproviders.MapDtoToMap
import br.edu.ufabc.fisicaludica.domain.dataproviders.MapRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.InputStream

/**
 * Main viewModel.
 */
class MainViewModel(application: Application) : AndroidViewModel(application){

    /**
     * id of the clicked map
     */
    val clickedMapId by lazy {
        MutableLiveData<Long?>()
    }

    val isAppBarVisible by lazy {
        MutableLiveData<Boolean>(true)
    }

    private val app: Application
    private val mapRepository = MapRepository(MapDtoToMap())
    companion object {
        const val mapsJson = "maps.json"
    }

    init {
        app = application
        viewModelScope.launch(Dispatchers.IO) {
            application.resources.assets.open(mapsJson).use {
                mapRepository.loadData(it)
            }
        }
    }

    /**
     * get all the game maps.
     */
    fun getAllMaps() = mapRepository.getAll()

    /**
     * open inputStream for the map image.
     */
    fun getMapBackgroundInputStream(map: Map): InputStream = app.resources.assets.open(map.backgroud)

    /**
     * get a map by id.
     */
    fun getMapById(id: Long): Map = mapRepository.getMapById(id)
}