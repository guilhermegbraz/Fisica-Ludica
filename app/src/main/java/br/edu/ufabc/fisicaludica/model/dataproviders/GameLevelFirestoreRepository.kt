package br.edu.ufabc.fisicaludica.model.dataproviders

import android.util.Log
import br.edu.ufabc.fisicaludica.model.domain.GameLevel
import br.edu.ufabc.fisicaludica.model.domain.GameLevelAtributtes
import br.edu.ufabc.fisicaludica.model.domain.GameLevelElementsPosition
import br.edu.ufabc.fisicaludica.model.domain.GameLevelRepository
import com.beust.klaxon.Klaxon
import com.beust.klaxon.KlaxonException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.io.InputStream

class GameLevelFirestoreRepository : GameLevelRepository {
    private val db: FirebaseFirestore = Firebase.firestore

    companion object {
        private const val gameLevelCollection = "game_levels"
        private const val gameLevelIdDoc = "gameLevelId"

        private object GameLevelDoc {
            const val id = "gameLevelId"
            const val backgroundUrl = "background_url"
            const val gravityX = "gravity_x"
            const val gravityY = "gravity_y"
            const val groundPosition = "ground_position"
            const val initialAngleDegrees = "initial_angle_degrees"
            const val initialVelocity = "initial_velocity"
            const val isAngleVariable = "is_angle_variable"
            const val isVelocityVariable = "is_velocity_variable"
            const val launcherPositionX = "launcher_position_x"
            const val launcherPositionY = "launcher_position_y"
            const val targetPositionX = "target_position_x"
            const val targetPositionY = "target_position_y"
            const val targetRotation = "target_rotation"
            const val title = "title"
            const val widthInMeters = "width_in_meters"
        }
    }

     data class GameLevelFirestore(
         val gameLevelId: Long? = null,
         val backgroundUrl: String? = null,
         val gravityX: Double? = null,
         val gravityY: Double? = null,
         val groundPosition: Double? = null,
         val initialAngleDegrees: Double? = null,
         val initialVelocity: Double? = null,
         val angleVariable: Boolean? = null,
         val velocityVariable: Boolean? = null,
         val launcherPositionX : Double? = null,
         val launcherPositionY : Double? = null,
         val targetPositionX : Double? = null,
         val targetPositionY : Double? = null,
         val targetRotation : Int? = null,
         val title: String? = null,
         val widthInMeters: Double? = null,
    ) {
        fun execute(): GameLevel {
            Log.d("conversao", "vou converter os objetos, a variavel boolean eh ${this}")
            val result =  GameLevel(
                id= gameLevelId?:0,
                title= this.title?:"",
                groundPosition = this.groundPosition?: 0.0,
                widthMeters = this.widthInMeters?:0.0,
                elementsPosition = createGameLevelElementsPosition(),
                worldAtributtes = createGameLevelAtributes(),
                backgroudUrl = this.backgroundUrl?: ""
            )
            Log.d("conversao", "o objeto convertido eh ${result.id} ${result.worldAtributtes.isAngleVariable}, ${result.worldAtributtes.isVelocityVariable}")
            return result
        }


        fun createGameLevelAtributes(): GameLevelAtributtes {
            val isAngleVariable = this.angleVariable == true
            val isVelocityVariable = this.velocityVariable == true
            return GameLevelAtributtes(initialVelocity= initialVelocity?: 0.0,
                isVelocityVariable= isVelocityVariable,
                initialAngleDegrees= this.initialAngleDegrees?: 0.0,
                isAngleVariable= isAngleVariable,
                gravityX= this.gravityX?: 0.0,
                gravityY= this.gravityY?: 9.8)
        }



        fun createGameLevelElementsPosition(): GameLevelElementsPosition =
            GameLevelElementsPosition(
                this.launcherPositionX?: 5.0, this.launcherPositionY?: 0.0,
                this.targetPositionX?: 20.0,
                this.targetPositionY?: 0.0,
                this.targetRotation?: 0)

    }


    private fun getCollection() = db.collection(gameLevelCollection)
     override suspend fun getAll(): List<GameLevel> {
         return getCollection()
             .get()
             .await()
             .toObjects(GameLevelFirestore::class.java).map { it.execute() }
     }

    override suspend fun getGameLevelById(id: Long): GameLevel =
        getCollection()
        .whereEqualTo(gameLevelIdDoc, id)
        .get()
        .await()
        .toObjects(GameLevelFirestore::class.java)
        .first()
        .execute()


    fun add(gameLevelFirestore: GameLevelFirestore) {
        getCollection().add(gameLevelFirestore)
    }
}