package br.edu.ufabc.fisicaludica.model.domain

class GameHint(val id: Long, val hints:List<String>) {
    var currentHint: Int
        private set
    init {
        currentHint = 0
    }
    fun getNextHint():String = hints[currentHint++]

}