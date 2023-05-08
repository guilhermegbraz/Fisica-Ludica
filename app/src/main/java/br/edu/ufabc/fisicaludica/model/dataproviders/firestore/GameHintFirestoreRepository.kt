package br.edu.ufabc.fisicaludica.model.dataproviders.firestore

import android.util.Log
import br.edu.ufabc.fisicaludica.model.dataproviders.inMemory.GameLevelDTO
import br.edu.ufabc.fisicaludica.model.domain.GameHint
import br.edu.ufabc.fisicaludica.model.domain.GameHintRepository
import com.beust.klaxon.Klaxon
import com.beust.klaxon.KlaxonException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.io.InputStream

class GameHintFirestoreRepository: GameHintRepository {
    private val db: FirebaseFirestore = Firebase.firestore

    companion object {
        private const val gameHintCollection = "game_hints"
        private const val gameHintIdDoc = "hintId"
        private const val gameLevelIdDoc = "gameLevelId"
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