package br.edu.ufabc.fisicaludica.model.domain

interface GameLevelRepository {
    fun getAll(): List<GameLevel>

    fun getGameLevelById(id: Long) : GameLevel
}