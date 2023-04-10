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

class MainViewModel(application: Application) : AndroidViewModel(application){

    val clickedMapId by lazy {
        MutableLiveData<Long?>()
    }

    private val app: Application
    val mapRepository = MapRepository(MapDtoToMap())
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

    fun getAllMaps() = mapRepository.getAll()

    fun getMapBackgroundInputStream(map: Map): InputStream = app.resources.assets.open(map.backgroud)

    fun getMapById(id: Long): Map = mapRepository.getMapById(id)
}