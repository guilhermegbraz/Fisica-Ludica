package br.edu.ufabc.fisicaludica.model.domain

interface GameHintRepository {
    suspend fun getHintByGameLevelId(gameLevelId: Long): GameHint
}