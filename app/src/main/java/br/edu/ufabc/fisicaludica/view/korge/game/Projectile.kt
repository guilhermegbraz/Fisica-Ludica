package br.edu.ufabc.fisicaludica.view.korge.game

import com.soywiz.korge.box2d.body
import com.soywiz.korge.view.*
import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korim.color.Colors
import org.jbox2d.common.Vec2
import org.jbox2d.dynamics.BodyType

fun  Container.projectile(width: Double, height: Double, posX: Number, posY: Number, appelBitmap: Bitmap)
        = Projectile(width, height, posX, posY, appelBitmap).addTo(this)

class Projectile(width: Double, height: Double, posX: Number, posY: Number, appleImageBitmap: Bitmap): Container() {

    val projetilObject = roundRect(width, height,width/5, width/5, Colors.TRANSPARENT_WHITE)
    val type = BodyType.DYNAMIC

    init {
        projetilObject.position(posX.toDouble(), posY.toDouble())
        val appleImage = image(appleImageBitmap)
        appleImage.scale(projetilObject.width / appleImage.width,
            projetilObject.height / appleImage.height)
        projetilObject.addChild(appleImage)
    }

    fun launch(vec2: Vec2) {
        this.projetilObject.body!!.linearVelocity = vec2
        this.projetilObject.body!!.angularVelocity = 4.0F
    }
}