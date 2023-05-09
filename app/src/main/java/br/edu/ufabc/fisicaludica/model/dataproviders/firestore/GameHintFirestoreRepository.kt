package br.edu.ufabc.fisicaludica.model.dataproviders.firestore

import android.app.Application
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.util.Log
import br.edu.ufabc.fisicaludica.model.domain.GameHint
import br.edu.ufabc.fisicaludica.model.domain.GameHintRepository
import com.beust.klaxon.Klaxon
import com.beust.klaxon.KlaxonException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.io.InputStream
import java.util.concurrent.atomic.AtomicBoolean

class GameHintFirestoreRepository(application: Application): GameHintRepository {
    private val db: FirebaseFirestore = Firebase.firestore
    private val isConnected = AtomicBoolean(true)

    companion object {
        private const val gameHintCollection = "game_hints"
        private const val gameLevelIdDoc = "gameLevelId"
    }

    init {
        application.applicationContext.getSystemService(ConnectivityManager::class.java)
            .apply {
                val connected = getNetworkCapabilities(activeNetwork)?.
                hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)?: false

                isConnected.set(connected)
                registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
                    override fun onAvailable(network: Network) {
                        super.onAvailable(network)
                        isConnected.set(true)
                    }

                    override fun onLost(network: Network) {
                        super.onLost(network)
                        isConnected.set(false)
                    }
                })
            }

    }

    private fun getCollection() = db.collection(gameHintCollection)

    override suspend fun getHintByGameLevelId(gameLevelId: Long): GameHint =
        getCollection()
        .whereEqualTo(gameLevelIdDoc, gameLevelId)
        .get()
        .await()
        .toObjects(GameHintFirestore::class.java)
        .first()
        .toGameLevelHint()

    fun loadData(inputStream: InputStream) {
        try {
            val gameHints = Klaxon().parseArray<GameHintFirestore>(inputStream)?: emptyList()
            gameHints.forEach {
                this.add(it)
            }
        } catch (exception: KlaxonException) {
            Log.d("klaxon", "Failed to parse json file")
        }
    }
    fun add(gameHint: GameHintFirestore) {
        getCollection().add(gameHint)
    }
}