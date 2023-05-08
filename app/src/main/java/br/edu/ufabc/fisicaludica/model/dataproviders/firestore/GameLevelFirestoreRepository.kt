package br.edu.ufabc.fisicaludica.model.dataproviders.firestore


import android.app.Application
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import br.edu.ufabc.fisicaludica.model.domain.GameLevel
import br.edu.ufabc.fisicaludica.model.domain.GameLevelAnswer
import br.edu.ufabc.fisicaludica.model.domain.GameLevelRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.util.concurrent.atomic.AtomicBoolean

class GameLevelFirestoreRepository(application: Application) : GameLevelRepository {
    private val db: FirebaseFirestore = Firebase.firestore
    private val isConnected = AtomicBoolean(true)

    companion object {
        private const val gameLevelCollection = "game_levels"
        private const val gameLevelIdDoc = "gameLevelId"
        private const val gameAnswerCollection = "answers"
        private const val userIdDoc = "userId"

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

    data class GameLevelAnswerFirestore(
        val userId: String? = null,
        val angle: Double? = null,
        val velocity: Double? = null
    ) {
        fun toGameLevelAnswer(): GameLevelAnswer {
            val result = GameLevelAnswer( angle?: 0.0, velocity?: 0.0)
            return result
        }

    }

    private fun getSource() = if (isConnected.get()) Source.DEFAULT else Source.CACHE

    private fun getCurrentUser(): String = FirebaseAuth.getInstance().currentUser?.uid
        ?: throw Exception("No user is signed in")

    private fun getCollection() = db.collection(gameLevelCollection)
     override suspend fun getAll(): List<GameLevel> =
         getCollection()
             .get(getSource())
             .await()
             .toObjects(GameLevelFirestore::class.java).map { it.toGameLevel() }



    override suspend fun getGameLevelById(id: Long): GameLevel {
        val result =
            getCollection()
                .whereEqualTo(gameLevelIdDoc, id)
                .get(getSource())
                .await()
                .documents
                .first()
        val gameLevel = result.toObject(GameLevelFirestore::class.java)!!.toGameLevel()

        result.reference
            .collection(gameAnswerCollection)
            .whereEqualTo(userIdDoc, getCurrentUser())
            .get(getSource())
            .await()
            .let { querySnapshot ->
                if(querySnapshot.isEmpty.not()) {
                    val gameAnswer =
                    querySnapshot.first()
                    .toObject(GameLevelAnswerFirestore::class.java)
                    .toGameLevelAnswer()

                    gameLevel.worldAtributtes.initialVelocity = gameAnswer.velocity
                    gameLevel.worldAtributtes.initialAngleDegrees = gameAnswer.angle
                }
            }

        return gameLevel
    }

    suspend fun addOrUpdateAnswer(gameLevelAnswer: GameLevelAnswer, gameLevelId: Long) {
        val gameAnswerFirestore = GameLevelAnswerFirestore(
            userId = getCurrentUser(),
            angle = gameLevelAnswer.angle,
            velocity = gameLevelAnswer.velocity
        )
        val reference = getCollection()
            .whereEqualTo(gameLevelIdDoc, gameLevelId)
            .get(getSource())
            .await()
            .documents
            .first()
            .reference
            .collection(gameAnswerCollection)

        reference
            .whereEqualTo(userIdDoc, gameAnswerFirestore.userId)
            .get(getSource())
            .await()
            .let { querySnapshot ->
                if(querySnapshot.isEmpty) {
                    reference.add(gameAnswerFirestore)
                }
                else querySnapshot.first().reference.set(gameAnswerFirestore)
            }


    }

    fun add(gameLevelFirestore: GameLevelFirestore) {
        getCollection().add(gameLevelFirestore)
    }
}