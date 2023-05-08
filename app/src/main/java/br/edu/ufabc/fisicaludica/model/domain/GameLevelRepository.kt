package br.edu.ufabc.fisicaludica.model.domain

interface GameLevelRepository {
    suspend fun getAll(): List<GameLevel>

    suspend fun getGameLevelById(id: Long) : GameLevel

    suspend fun addOrUpdateAnswer(gameLevelAnswer: GameLevelAnswer, gameLevelId: Long)

    suspend fun enableNextLevel(idCurrentLevel:Long)
}