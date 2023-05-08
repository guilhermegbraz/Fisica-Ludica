package br.edu.ufabc.fisicaludica.model.dataproviders.firestore

import br.edu.ufabc.fisicaludica.model.domain.GameLevelAnswer

data class GameLevelAnswerFirestore(
    val userId: String? = null,
    val angle: Double? = null,
    val velocity: Double? = null
) {
    fun toGameLevelAnswer(): GameLevelAnswer = GameLevelAnswer( angle?: 0.0, velocity?: 0.0)


}