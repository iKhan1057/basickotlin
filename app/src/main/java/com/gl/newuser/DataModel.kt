package com.gl.newuser

class DataModel {
    var name: String = ""
    var selection: Boolean = false

    constructor(name: String, selection: Boolean) {
        this.name = name
        this.selection = selection
    }

    override fun toString(): String {
        return "DataModel(name='$name', selection=$selection)"
    }

}