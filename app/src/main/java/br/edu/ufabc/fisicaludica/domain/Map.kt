package br.edu.ufabc.fisicaludica.domain

/**
 * domain class for game map.
 */
class Map (id:Long, backgroud: String, title: String) {
     var id: Long
        private set
      var backgroud: String
        private set
    var title: String
        private set

    init {
        this.id = id
        this.backgroud = backgroud
        this.title = title
    }
}