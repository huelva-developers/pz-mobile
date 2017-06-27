package com.huelvadevelopers.proyectozero.model

/**
 * Created by DrAP on 23/06/2017.
 */
class Category(id : Int,  name : String, icon : Int, type : Int) {

    val id: Int = id
    var parent : Category? = null
    var children : ArrayList<Category> = ArrayList()
    var name : String = name
    var icon : Int = icon
    var type : Int = type


    override fun toString(): String {
        return "Id: $id, parent: $parent, name: $name, icon: $icon, type: $type"
    }

    override fun equals(other: Any?): Boolean {
        return (other as Category).id==this.id
    }

    fun addChild(child : Category){
        children.add(child)
        child.parent = this
    }

}
