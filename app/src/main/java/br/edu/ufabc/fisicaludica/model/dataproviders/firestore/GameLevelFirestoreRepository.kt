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
import com.google.firebase.firestore.ktx.toObject
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
        private const val gameUserEnableCollection = "usersEnable"

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

    private fun getCurrentUser(): String = FirebaseAuth.getInstance().currentUser?.uid
        ?: throw Exception("No user is signed in")

    private fun getCollection() = db.collection(gameLevelCollection)
     override suspend fun getAll(): List<GameLevel> {
         val gameLevelList = mutableListOf<GameLevel>()
         val querySnapshot = getCollection()
             .get(getSource())
             .await()
             .documents.forEach{documentSnapshot ->
                 val gameLevel = documentSnapshot.toObject(GameLevelFirestore::class.java)!!.toGameLevel()
                 documentSnapshot
                     .reference
                     .collection(gameUserEnableCollection)
                     .whereEqualTo(userIdDoc, getCurrentUser())
                     .get(getSource())
                     .await()
                     .let { querySnapshot ->
                         if(querySnapshot.isEmpty.not()) gameLevel.isEnable = true
                     }
                 gameLevelList.add(gameLevel)
             }
         return gameLevelList
     }




    override suspend fun getGameLevelById(id: Long): GameLevel {
        val documentSnapshot =
            getCollection()
                .whereEqualTo(gameLevelIdDoc, id)
                .get(getSource())
                .await()
                .documents
                .first()
        val gameLevel = documentSnapshot.toObject(GameLevelFirestore::class.java)!!.toGameLevel()

        documentSnapshot.reference
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

    override suspend fun addOrUpdateAnswer(gameLevelAnswer: GameLevelAnswer, gameLevelId: Long) {
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

    override suspend fun enableNextLevel(idCurrentLevel: Long) {
        val documentReference =
            getCollection()
                .whereEqualTo(gameLevelIdDoc, idCurrentLevel + 1)
                .get(getSource())
                .await()
                .documents
                .first()
                .reference

        documentReference
            .collection(gameUserEnableCollection)
            .whereEqualTo(userIdDoc, getCurrentUser())
            .get(getSource())
            .await()
            .let { querySnapshot ->
                if(querySnapshot.isEmpty) documentReference
                    .collection(gameUserEnableCollection)
                    .add(hashMapOf("userId" to getCurrentUser()))
            }
    }

    fun add(gameLevelFirestore: GameLevelFirestore) {
        getCollection().add(gameLevelFirestore)
    }
}