package com.bombo.template.domain.example

class Example(
    id: Long = 0,
    name: String,
) {
    val id: Long = id
    var name: String = name

    companion object {
        fun newInstance(name: String): Example {
            return Example(name = name)
        }
    }

    fun changeName(name: String) {
        this.name = name
    }

    fun anyMethod(args: String) {
        println(args)
    }

    fun typeMethod(args: String): String {
        this.name = args
        return args
    }
}
