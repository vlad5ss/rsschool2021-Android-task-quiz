package com.rsschool.quiz.data

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.rsschool.quiz.FragmentResult
import com.rsschool.quiz.R
import com.rsschool.quiz.databinding.FragmentQuizBinding


class QuizFragment: Fragment() {

    private var nameQuiz: String = ""
    private var nameAnswer: MutableList<String> = mutableListOf()
    private var numberQuiz: Int = 0

    private var prevFragment: StateFragment? = null
    private var nextFragment: StateFragment? = null

    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!

    private var _replace: Replace? = null
    private val replace get() = _replace!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        if (arguments?.getSerializable(DataQuiz) is Data) {
            nameQuiz = (arguments?.getSerializable(DataQuiz) as Data).quiz
            nameAnswer  = (arguments?.getSerializable(DataQuiz) as Data).getList()
            numberQuiz = (arguments?.getSerializable(DataQuiz) as Data).getNumberQuiz()
        }

        replace.updateTheme(colorTheme(numberQuiz))
        _binding = FragmentQuizBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments?.getSerializable(PrevFragment) is StateFragment) {

            prevFragment = arguments?.getSerializable(PrevFragment) as StateFragment
            binding.toolbar.setOnClickListener {
                navigationFragment(prevFragment)
            }

        }
        else {
            binding.toolbar.navigationIcon = null
            binding.previousButton.isEnabled = false
        }

        if (arguments?.getSerializable(NextFragment) is StateFragment) {

            nextFragment = arguments?.getSerializable(NextFragment) as StateFragment
            binding.nextButton.setOnClickListener {
                navigationFragment(nextFragment)
            }

        }
        else {

            binding.nextButton.text = resources.getString(R.string.submit)
            binding.nextButton.setOnClickListener {
                replace.updateTheme(R.style.Theme_Quiz_Result)
                replace.runOpenFragment( replace.getFragment(),  replace.getStateFragment() )
            }

        }

        binding.nextButton.isEnabled = false

        setSettings(nameQuiz, nameAnswer, numberQuiz)

        binding.previousButton.setOnClickListener {
            navigationFragment(prevFragment)
        }


        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->

            binding.nextButton.isEnabled = true

            when(checkedId) {

                binding.optionOne.id -> replace.setResult(1, nameQuiz)
                binding.optionTwo.id -> replace.setResult(2, nameQuiz)
                binding.optionThree.id -> replace.setResult(3, nameQuiz)
                binding.optionFour.id -> replace.setResult(4, nameQuiz)
                binding.optionFive.id ->replace.setResult(5, nameQuiz)
                else -> binding.nextButton.isEnabled = false
           }
        }

    }


    private fun navigationFragment(stateFragment: StateFragment?) {
        if (stateFragment is StateFragment)
            replace.runOpenFragment(replace.checkFragment(stateFragment.name)!!, stateFragment)

    }

    private fun setSettings(name: String, answer: MutableList<String>, numberQuiz: Int){

        binding.question.text = name
        binding.toolbar.title = "Question number $numberQuiz"

        if (nameAnswer.isNotEmpty()){

            binding.optionOne.text = answer[0]
            binding.optionTwo.text = answer[1]
            binding.optionThree.text = answer[2]
            binding.optionFour.text = answer[3]
            binding.optionFive.text = answer[4]

        }
    }


    companion object {

        @JvmStatic
        fun newInstance(data: Data, prev: StateFragment?, next: StateFragment?): QuizFragment {

            val fragment = QuizFragment()
            val args = Bundle()
            args.putSerializable(DataQuiz, data)
            args.putSerializable(PrevFragment, prev)
            args.putSerializable(NextFragment, next)
            fragment.arguments = args
            return fragment

        }

        private const val DataQuiz = "DataQuiz"
        private const val PrevFragment = "PrevFragment"
        private const val NextFragment = "NextFragment"

    }


    interface Replace{
        fun runOpenFragment(fragment: Fragment, stateFragment: StateFragment)
        fun checkFragment(nameFragment: String): QuizFragment?
        fun setResult(result: Int, name: String)
        fun getStateFragment(): StateFragment
        fun getFragment(): FragmentResult
        fun updateTheme(int: Int)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Replace)
            _replace = context
        else
            Toast.makeText(context, "Error create fragment to host", Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _replace = null

    }

    private fun colorTheme(numberQuiz: Int): Int{
        return when(numberQuiz){
            1 -> R.style.Theme_Quiz_First
            2 -> R.style.Theme_Quiz_Second
            3 -> R.style.Theme_Quiz_Three
            4 -> R.style.Theme_Quiz_Four
            5 -> R.style.Theme_Quiz_Five
            else -> R.style.Theme_Quiz
        }
    }

}