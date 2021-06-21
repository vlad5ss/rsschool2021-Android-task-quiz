package com.rsschool.quiz.data

import java.io.Serializable

class StateFragment(val name: String, val quiz: String): Serializable {

    private var v = false
    var data: Data = Data(quiz)

    fun updateStateFragment() {
        v = !v
    }

    fun checkStateFragment(): Boolean {
        return v
    }

}
