package com.rsschool.quiz.data

import java.io.Serializable

class Data (val quiz: String): Serializable {

    private var answer: MutableList<String> = mutableListOf()
    private var numberQuiz: Int = 0

    fun addElement(nameAnswer: String) {
        answer.add(nameAnswer)
    }

    fun deleteElement(nameAnswer: String) {
        answer.remove(nameAnswer)
    }

    fun setNumberQuiz(number: Int){
        numberQuiz = number
    }

    fun getNumberQuiz(): Int {
        return numberQuiz
    }

    fun getList(): MutableList<String> {
        return answer
    }

    fun addList(nameAnswer: List<String>) {
        answer.addAll(nameAnswer)
    }

}





