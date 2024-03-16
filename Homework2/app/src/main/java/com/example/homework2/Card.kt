package com.example.homework2

class Card {

    public var name : String? = null
    public var colors : List<String?> = mutableListOf()

    constructor(name : String?, colors : List<String?>){
        this.name = name
        this.colors = colors
    }

    override fun toString(): String {
        return "${name?:""}, ${colors?.toString()}"
    }
}