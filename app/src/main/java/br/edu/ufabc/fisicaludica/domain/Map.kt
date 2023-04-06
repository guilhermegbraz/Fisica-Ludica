package br.edu.ufabc.fisicaludica.domain

class Map (id:Long, backgroud: String) {
     var id: Long
        private set
      var backgroud: String
        private set

    init {
        this.id = id
        this.backgroud = backgroud
    }
}