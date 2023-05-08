package br.edu.ufabc.fisicaludica.model.dataproviders.firestore


import android.app.Application
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import br.edu.ufabc.fisicaludica.model.domain.GameLevel
import br.edu.ufabc.fisicaludica.model.domain.GameLevelRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.soywiz.kmem.NewFast32Buffer
import kotlinx.coroutines.tasks.await
import java.util.concurrent.atomic.AtomicBoolean

class GameLevelFirestoreRepository(application: Application) : GameLevelRepository {
    private val db: FirebaseFirestore = Firebase.firestore
    private val isConnected = AtomicBoolean(true)

    companion object {
        private const val gameLevelCollection = "game_levels"
        private const val gameLevelIdDoc = "gameLevelId"

        private object GameLevelDoc {
            const val id = "gameLevelId"
            const val backgroundUrl = "backgroundUrl"
            const val gravityX = "gravityX"
            const val gravityY = "gravityY"
            const val groundPosition = "groundPosition"
            const val initialAngleDegrees = "initialAngleDegrees"
            const val initialVelocity = "initialVelocity"
            const val isAngleVariable = "isAngleVariable"
            const val isVelocityVariable = "isVelocityVariable"
            const val launcherPositionX = "launcherPositionX"
            const val launcherPositionY = "launcherPositionY"
            const val targetPositionX = "targetPositionX"
            const val targetPositionY = "targetPositionY"
            const val targetRotation = "targetRotation"
            const val title = "title"
            const val widthInMeters = "widthInMeters"
        }
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

    private fun getSource() = if (isConnected.get()) Source.DEFAULT else Source.CACHE

    private fun getCollection() = db.collection(gameLevelCollection)
     override suspend fun getAll(): List<GameLevel> {
         return getCollection()
             .get(getSource())
             .await()
             .toObjects(GameLevelFirestore::class.java).map { it.toGameLevel() }
     }

    override suspend fun getGameLevelById(id: Long): GameLevel =
        getCollection()
        .whereEqualTo(gameLevelIdDoc, id)
        .get(getSource())
        .await()
        .toObjects(GameLevelFirestore::class.java)
        .first()
        .toGameLevel()


    fun add(gameLevelFirestore: GameLevelFirestore) {
        getCollection().add(gameLevelFirestore)
    }
}