package com.rsschool.quiz

import android.content.Intent
import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.rsschool.quiz.data.QuizFragment
import com.rsschool.quiz.data.StateFragment
import com.rsschool.quiz.databinding.ActivityMainBinding
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity(), QuizFragment.Replace, FragmentResult.StartAppNew {

    private lateinit var binding: ActivityMainBinding

    private var listQuestionFirst: List<String> = listOf()
    private var listQuestionSecond: List<String> = listOf()
    private var listQuestionThree: List<String> = listOf()
    private var listQuestionFour: List<String> = listOf()
    private var listQuestionFive: List<String> = listOf()

    private val stateFragment: MutableList<StateFragment> = mutableListOf()
    private val fragmentList: MutableList<QuizFragment> = mutableListOf()

    private var listNameFragment: List<String> = listOf()
    private var listQuiz: List<String> = listOf()

    private var listAnswer: MutableMap<String, Int> = mutableMapOf()
    private var result: MutableMap<String, Int> = mutableMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setQuiz()

        if (fragmentList.isNotEmpty())
            runOpenFragment(fragmentList[0], stateFragment[0])

    }

    override fun runOpenFragment(fragment: Fragment, stateFragment: StateFragment) {

        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()

        if (!stateFragment.checkStateFragment()) {
            transaction.add(R.id.container, fragment, stateFragment.name)
            stateFragment.updateStateFragment()
        }
        if (stateFragment.checkStateFragment()) {
            transaction.replace(R.id.container, fragment, stateFragment.name)
        }

        transaction.commit()
    }

    private fun setQuiz(){

        listQuestionFirst = resources.getStringArray(R.array.listQuestionFirst).toList()
        listQuestionSecond = resources.getStringArray(R.array.listQuestionSecond).toList()
        listQuestionThree = resources.getStringArray(R.array.listQuestionThree).toList()
        listQuestionFour = resources.getStringArray(R.array.listQuestionFour).toList()
        listQuestionFive = resources.getStringArray(R.array.listQuestionFive).toList()

        listNameFragment = resources.getStringArray(R.array.listNameFragment).toList()
        listQuiz = resources.getStringArray(R.array.listQuiz).toList()

        val tempListAnswer = resources.getIntArray(R.array.listAnswer)


        result.clear()
        listAnswer.clear()
        stateFragment.clear()
        fragmentList.clear()

        for (index in listNameFragment.indices) {
            stateFragment.add(StateFragment(listNameFragment[index], listQuiz[index]))
            result[listQuiz[index]] = 0
            listAnswer[listQuiz[index]] = tempListAnswer[index]
        }

        stateFragment[0].data.addList(listQuestionFirst)
        stateFragment[1].data.addList(listQuestionSecond)
        stateFragment[2].data.addList(listQuestionThree)
        stateFragment[3].data.addList(listQuestionFour)
        stateFragment[4].data.addList(listQuestionFive)

        for (index in listNameFragment.indices)
            stateFragment[index].data.setNumberQuiz(index + 1)

        fragmentList.add( QuizFragment.newInstance(stateFragment[0].data, null, stateFragment[1]) )
        fragmentList.add( QuizFragment.newInstance(stateFragment[1].data, stateFragment[0], stateFragment[2]) )
        fragmentList.add( QuizFragment.newInstance(stateFragment[2].data, stateFragment[1], stateFragment[3]) )
        fragmentList.add( QuizFragment.newInstance(stateFragment[3].data, stateFragment[2], stateFragment[4]) )
        fragmentList.add( QuizFragment.newInstance(stateFragment[4].data, stateFragment[3], null) )


    }

    override fun checkFragment(nameFragment: String): QuizFragment? {

        for (index in stateFragment.indices)
            if (stateFragment[index].name == nameFragment)
                return fragmentList[index]

        return null

    }

    override fun setResult(result: Int, name: String){
        this.result[name] = result
    }


    override fun getStateFragment(): StateFragment {
        return StateFragment("result", "")
    }

    override fun getFragment(): FragmentResult {

        var del = 0f


        for (index in listAnswer.keys){
            if (result[index] == listAnswer[index]){
                del++
            }
        }

        del /= listAnswer.keys.size
        del *= 100

        return FragmentResult.newInstance(del.roundToInt().toString())
    }

    override fun onBackPressed() {

        if ( supportFragmentManager.findFragmentByTag("result") is FragmentResult)
            startApp()

    }

    private fun startApp() {

        clearFragment()
        setQuiz()
        if (fragmentList.isNotEmpty())
            runOpenFragment(fragmentList[0], stateFragment[0])

        if ( supportFragmentManager.findFragmentByTag("result") is FragmentResult) {
            val fragment: FragmentResult = supportFragmentManager.findFragmentByTag("result") as FragmentResult
            supportFragmentManager.beginTransaction().remove(fragment).commit()
        }

    }

    private fun clearFragment() {
        for(index in fragmentList.indices)
            supportFragmentManager.beginTransaction().remove(fragmentList[index]).commit()
    }

    override fun startAppNew() {
        startApp()
    }

    override fun closeApp() {
        finishAndRemoveTask()
    }

    override fun shareApp(resultQuiz: String) {

        var resultText = resultQuiz + "\n"

        for (index in listQuiz.indices) {

            val number = index + 1

            resultText += ("Question number $number:" + "\n" + listQuiz[index] + "\n")

            val tempListGetQuestion = listGetQuestion(index)

            resultText += if (tempListGetQuestion.isNotEmpty())
                ("Your answer:" + "\n" + tempListGetQuestion[result[listQuiz[index]]!! - 1] + "\n")
            else
                "Your answer not found"

            resultText += ("Try answer:" + "\n" + tempListGetQuestion[listAnswer[listQuiz[index]]!! - 1] + "\n\n")

        }

        val intentEmail = Intent(Intent.ACTION_SEND)
        intentEmail.putExtra(Intent.EXTRA_SUBJECT, "Quiz")
        intentEmail.putExtra(Intent.EXTRA_TEXT, resultText)
        intentEmail.type = "text/plain"
        startActivity(intentEmail)
    }

    private fun listGetQuestion(number: Int): List<String> {
        return when(number){
            0 -> listQuestionFirst
            1 -> listQuestionSecond
            2 -> listQuestionThree
            3 -> listQuestionFour
            4 -> listQuestionFive
            else -> emptyList()
        }
    }

    override fun updateTheme(theme: Int) {

        setTheme(theme)

        val typedValue = TypedValue()
        val themeApp: Resources.Theme = getTheme()

        themeApp.resolveAttribute(android.R.attr.windowBackground, typedValue, true)
        window.decorView.setBackgroundColor(typedValue.data)

        themeApp.resolveAttribute(android.R.attr.colorPrimary, typedValue, true)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(typedValue.data))

        themeApp.resolveAttribute(android.R.attr.colorPrimaryDark, typedValue, true)

        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = typedValue.data

    }

}