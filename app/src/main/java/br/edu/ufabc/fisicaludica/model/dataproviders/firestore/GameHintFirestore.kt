package br.edu.ufabc.fisicaludica.model.dataproviders.firestore

import android.util.Log
import br.edu.ufabc.fisicaludica.model.domain.GameHint

data class GameHintFirestore(
    val hintId: Long? = null,
    val gameLevelId: Long? = null,
    val hints: List<String>? = null
) {
    fun toGameLevelHint(): GameHint {
        Log.d("conversao hint", "o objeto chegou como ${this}")
        val result = GameHint(gameLevelId?: 0,
            hints?: listOf("")
        )
        Log.d("conversao hint", "o objeto saiu como ${result.hints}")
        return result
    }

}